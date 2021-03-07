
/* Run Modelling Reports
 *
 *
 * Current codes 13/1/20
 * DRB
 *
 * No Custom report spec
 * Assume runs all reports
 * #410-#425
 *
 * Works across OHEA Community/WGI and OIHM Study/HH
 *
 * Aug 2020 Add Nutrients change scenario
 *
 * Oct 2020 ref Issue #477
 *  Produced, Sold, Other Use and Consumed all need to be changed wrt yield change
 *
 *
 * */

package efd.actions;

import efd.model.*;
import efd.model.CustomReportSpecListModelling.ModelType;
import efd.model.HouseholdMember.Sex;
import efd.model.Report.Method;
import efd.model.StdOfLivingElement.Gender;
import efd.model.StdOfLivingElement.StdLevel;
import efd.model.Transfer.TransferType;
import efd.model.WealthGroupInterview.Status;
import efd.utils.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openxava.jpa.XPersistence;
import org.openxava.tab.Tab;
import org.openxava.util.jxls.JxlsConstants;
import org.openxava.util.jxls.JxlsSheet;
import org.openxava.util.jxls.JxlsWorkbook;
import org.openxava.web.servlets.ReportXLSServlet;

import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public @Getter
@Setter
class ModellingReports extends BaseReporting implements JxlsConstants {

    static final int NUMBER_OF_REPORTS = 15;
    static final int NUMBEROFAVERAGES = 10;
    static final int PRICE = 0;
    static final int YIELD = 1;
    static final int OHEA = 0;
    static final int OIHM = 1;
    static final String FOOD = "food";
    static final String CASH = "cash";
    static Double RC = 2100.0 * 365;
    static Boolean isChangeScenario = false;
    private static List<DefaultDietItem> defaultDietItems; // At Study not Household level
    private static List<PriceYieldVariation> priceYieldVariations = new ArrayList<PriceYieldVariation>();
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static DecimalFormat df0 = new DecimalFormat("#");
    ArrayList<ExpandabilityValues> expandabilityValues = new ArrayList<>();
    // private CustomReportSpec customReportSpec = null;
    List<WGI> uniqueWealthgroupInterview;
    List<WGI> uniqueCommunity;
    List<WGI> uniqueWG;
    List<HH> uniqueHousehold;
    List<ExpandabilityRule> expandabilityRules;
    JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
    JxlsWorkbook reportWB;
    ArrayList<WGI> wgi = new ArrayList<>();
    ArrayList<Site> sites = new ArrayList<>();
    ArrayList<WGI> wgiSelected = new ArrayList<>();
    ArrayList<WealthGroupInterview> wgiList = new ArrayList<>();
    ArrayList<QuantHousehold> quanthh = new ArrayList<>();
    ArrayList<WealthGroup> displayWealthgroup = new ArrayList<>();
    ArrayList<HH> hh = new ArrayList<>();
    ArrayList<HH> hhSelected = new ArrayList<>();
    List<MicroNutrient> nutrients = XPersistence.getManager().createQuery("from MicroNutrient order by Name")
            .getResultList();
    ArrayList<NutrientCount> overallNutrientCount = new ArrayList<NutrientCount>();
    // Note that assuming 1:1 WG:WGI
    ArrayList<NutrientCount> totalNutrientCount = new ArrayList<NutrientCount>();
    ArrayList<NutrientCount> totalNutrientCountFoodSubstitution = new ArrayList<NutrientCount>();
    List<WGI> orderedQuantSeq = null;
    List<WealthGroup> orderedWealthgroups;
    List<WealthGroup> allOrderedWealthgroups;
    Map<Integer, Double> quantAvg = null;
    int errno = 0;
    Boolean isQuantile = false;
    Boolean isDisplayWealthgroupDone = false;
    Boolean isSelectedSites = false;
    Boolean isFoodSubstitution = false;
    String currency2;
    Boolean isOHEA = false;
    Boolean isOIHM = false;
    Boolean isCopingStrategy = false;
    Boolean isSelectedHouseholds = false;
    Boolean isValid = false;
    ModelType modelType;
    ExpandabilityValues expval = null;
    double[] hhSize = {0.0, 0.0, 0.0};
    int numCommunities = 0;
    Double[][] averageTotal = new Double[3][NUMBEROFAVERAGES];
    HttpSession session;
    //private String selectionView;
    private Community community = null;
    private LivelihoodZone livelihoodZone = null;
    private Project project = null;
    private Project ihmProject = null;
    private Study study = null;
    private ModellingScenario modellingScenario;
    private List<Report> reportList = new ArrayList<>();
    private List<Site> selectedSites = new ArrayList<>();
    // ArrayList<ArrayList<Double>> averageTotal = new
    // ArrayList<>(NUMBEROFAVERAGES);
    private List<Household> selectedHouseholds = new ArrayList<Household>();
    private String forwardURI = null;
    private String floatFormat = "##########0.00";
    private String integerFormat = "##############";
    private String modellingScenarioId;
    //private String model;
    private Tab targetTab;
    private JxlsWorkbook report;


    /******************************************************************************************************************************************/
    static Double householdDI(Household household, Boolean isChangeScenario, List<DefaultDietItem> defaultDietItems, ModellingScenario modellingScenario) {

        /*
         * Disposable Income (DI) = Total Income (TI) - Cost of covering Shortfall (SF)
         * in Required Calories (RC) from Own Production (OP).
         *
         * TI (Total Income) = Sum (Units Sold * Price per Unit) for all Crops, Wild
         * Foods and Livestock Products
         *
         * Sum (Units Worked No. of People Working Cash Payment) for all Employments Sum
         * (Transfer Amount No. of People Receiving No. of Times Received) for all Cash
         * Transfers
         *
         * OP = Sum (Units Consumed * KCal per Unit) for all Crops, Wild Foods and
         * Livestock Products
         *
         * Sum (KCal per Unit of Food Type * No. of Units Paid) for all Employment paid
         * in Food Sum (KCal per Unit of Food Type Transferred * No. of Units Consumed)
         * for all Food Transfers
         *
         * Required KCalaries = 2100 x 365 No. of People in Household (that was from
         * OHEA - can be more accurate in OIHM
         *
         *
         * if isChangeScenario is True then take into account Scenario Rules from
         * ModellingScenario where a Study is used
         *
         */

        Double cropTI = 0.0;
        Double wildfoodsTI = 0.0;
        Double lspTI = 0.0;
        Double lssTI = 0.0;
        Double employmentTI = 0.0;
        Double transfersTI = 0.0;

        Double cropOP = 0.0;
        Double wildfoodsOP = 0.0;
        Double lspOP = 0.0;
        // Double lssOP = 0.0;
        Double employmentOP = 0.0;
        Double transfersOP = 0.0;

        int requiredCalories = 0;

        try {
            if (household.getCrop().size() > 0) {

                cropTI = calcCropIncome(household.getCrop(), CASH, isChangeScenario, false, false, modellingScenario);
                cropOP = calcCropIncome(household.getCrop(), FOOD, isChangeScenario, false, false, modellingScenario);
            }

            if (household.getWildFood().size() > 0) {

                wildfoodsTI = calcWFIncome(household.getWildFood(), CASH, isChangeScenario,
                        false, false, modellingScenario);
                wildfoodsOP = calcWFIncome(household.getWildFood(), FOOD, isChangeScenario,
                        false, false, modellingScenario);

            }
            if (household.getLivestockProducts().size() > 0) {

                lspTI = calcLSP(household.getLivestockProducts(), CASH, isChangeScenario, false, false, modellingScenario);
                lspOP = calcLSP(household.getLivestockProducts(), FOOD, isChangeScenario, false, false, modellingScenario);

            }
            if (household.getLivestockSales().size() > 0) {

                lssTI = calcLSS(household.getLivestockSales(), CASH, isChangeScenario, modellingScenario);
                // No output from LSS

            }
            if (household.getEmployment().size() > 0) {

                employmentTI = calcEmpIncome(household.getEmployment(), CASH, isChangeScenario, false, false, modellingScenario);
                employmentOP = calcEmpIncome(household.getEmployment(), FOOD, isChangeScenario, false, false, modellingScenario);

            }
            if (household.getTransfer().size() > 0) {

                transfersTI = calcTransIncome(household.getTransfer(), CASH, isChangeScenario, false, false, modellingScenario);
                transfersOP = calcTransIncome(household.getTransfer(), FOOD, isChangeScenario, false, false, modellingScenario);

            }

        } catch (

                Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Error in DI Calculation ");
        }

        // FIX for members
        // requiredCalories = household.getHouseholdMember().size() * RC; // Unique
        // Households after filter

        int age;
        Sex gender;
        int monthsAway = 0;
        int energyNeed = 0;

        WHOEnergyRequirements whoEnergy;
        for (HouseholdMember hm : household.getHouseholdMember()) {
            age = hm.getAge();
            gender = hm.getGender();
            monthsAway = hm.getMonthsAway();
            whoEnergy = WHOEnergyRequirements.findByAge(age);

            if (gender == Sex.Female) {
                energyNeed = whoEnergy.getFemale();
            } else if (gender == Sex.Male || gender == Sex.Unknown) {
                energyNeed = whoEnergy.getMale();
            }
            int reprequiredCals = energyNeed * 365 * (12 - monthsAway) / 12;
            requiredCalories += energyNeed * 365 * (12 - monthsAway) / 12;

        }

        Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI + transfersTI + lssTI;

        Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

        Double shortFall = requiredCalories - output;

        // Now it gets more complex , but not difficult

        // Diet
        // Diet KCAL Value = Sum (KCal per KG * Percentage of the Food type in default
        // diet)
        // Diet Price Value = Sum (Unit Price * Percentage of the Food type in default
        // diet)

        /* Calc each part of ddi shortfall */
        for (DefaultDietItem defaultDietItem : defaultDietItems) {

            double calsNeededByThisDDI = shortFall / defaultDietItem.getPercentage();

            defaultDietItem.setCalsNeededofThisDDI(calsNeededByThisDDI);
        }

        Double dietValue = 0.0;
        Double dietPriceValue = 0.0;

        // Diet Amount Purchased DA = Shortfall / Diet Value in KGs
        Double dietAmountPurchased = 0.0;

        if (defaultDietItems.size() != 0) {
            dietValue = calcDdiKCalValue(defaultDietItems);
            dietPriceValue = calcDdiPriceValue(defaultDietItems);
            dietAmountPurchased = shortFall / dietValue;
        }

        household.setDdiKCalValue(dietValue);
        household.setDdiPriceValue(dietPriceValue);

        if (isChangeScenario) {
            household.setTotalOutputAfterChangeScenario(output);
            household.setTotalIncomeAfterChangeScenario(totalIncome);
        } else {
            household.setTotalOutput(output);
            household.setTotalIncome(totalIncome);
        }

        // Cost of Shortfall = Unit Price * Diet Amount Purchased
        // How many KGs in % of diet is needed
        // i.e. 20% of KGs in Diet item 1 +80% diet item 2

        Double costOfShortfall = 0.0;

        for (DefaultDietItem defaultDietItem : defaultDietItems) {
            costOfShortfall += ((dietAmountPurchased * defaultDietItem.getPercentage() / 100)
                    * defaultDietItem.getUnitPrice().doubleValue());
        }

        // Disposable Income = Total Income - Cost of Shortfall
        Double disposableIncome = 0.0;

        disposableIncome = totalIncome - costOfShortfall;

        return Double.parseDouble(df2.format(disposableIncome));
    }

    /******************************************************************************************************************************************/

    private static Double calcDdiKCalValue(List<DefaultDietItem> defaultDietItems) {

        Double dietValue = 0.0;

        for (DefaultDietItem defaultDietItem : defaultDietItems) {

            // CUrrently there is a chance that KCAL = 0 which throws calc. Get Synonym
            // parent KCAL if = 0
            if (defaultDietItem.getResourcesubtype().getResourcesubtypekcal() == 0) {
                dietValue += (defaultDietItem.getResourcesubtype().getResourcesubtypesynonym().getResourcesubtypekcal()
                        * defaultDietItem.getPercentage() / 100);
            } else {

                dietValue += (defaultDietItem.getResourcesubtype().getResourcesubtypekcal()
                        * defaultDietItem.getPercentage() / 100);
            }
        }
        return dietValue;
    }

    /******************************************************************************************************************************************/

    private static Double calcDdiPriceValue(List<DefaultDietItem> defaultDietItems) {

        Double dietPriceValue = 0.0;

        for (DefaultDietItem defaultDietItem : defaultDietItems) {

            Double unitPrice = defaultDietItem.getUnitPrice().doubleValue();
            Double ddi = defaultDietItem.getPercentage().doubleValue() / 100.0;

            dietPriceValue += unitPrice * ddi;

        }
        return dietPriceValue;
    }

    /******************************************************************************************************************************************/

    public static Double wealthgroupInterviewDI(WealthGroupInterview wealthGroupInterview, Boolean isChangeScenario, ModellingScenario modellingScenario) {

        /*
         * Disposable Income (DI) = Total Income (TI) - Cost of covering Shortfall (SF)
         * in Required Calories (RC) from Own Production (OP).
         *
         * TI (Total Income) = Sum (Units Sold * Price per Unit) for all Crops, Wild
         * Foods and Livestock Products
         *
         * Sum (Units Worked No. of People Working Cash Payment) for all Employments Sum
         * (Transfer Amount No. of People Receiving No. of Times Received) for all Cash
         * Transfers
         *
         * OP = Sum (Units Consumed * KCal per Unit) for all Crops, Wild Foods and
         * Livestock Products
         *
         * Sum (KCal per Unit of Food Type * No. of Units Paid) for all Employment paid
         * in Food Sum (KCal per Unit of Food Type Transferred * No. of Units Consumed)
         * for all Food Transfers
         *
         * Required KCalaries = 2100 x 365 No. of People in Household (that was from
         * OHEA - can be more accurate in OIHM
         *
         *
         *
         */

        Double cropTI = 0.0;
        Double wildfoodsTI = 0.0;
        Double lspTI = 0.0;
        Double lssTI = 0.0;
        Double employmentTI = 0.0;
        Double transfersTI = 0.0;

        Double cropOP = 0.0;
        Double wildfoodsOP = 0.0;
        Double lspOP = 0.0;
        // Double lssOP = 0.0;
        Double employmentOP = 0.0;
        Double transfersOP = 0.0;

        Double requiredCalories = 0.0;

        try {
            if (!wealthGroupInterview.getCrop().isEmpty()) {

                cropTI = calcCropIncome(wealthGroupInterview.getCrop(), CASH, isChangeScenario, false, false, modellingScenario);
                cropOP = calcCropIncome(wealthGroupInterview.getCrop(), FOOD, isChangeScenario, false, false, modellingScenario);
            }

            if (!wealthGroupInterview.getWildFood().isEmpty()) {

                wildfoodsTI = calcWFIncome(wealthGroupInterview.getWildFood(), CASH, isChangeScenario, false, false, modellingScenario);
                wildfoodsOP = calcWFIncome(wealthGroupInterview.getWildFood(), FOOD, isChangeScenario, false, false, modellingScenario);

            }
            if (!wealthGroupInterview.getLivestockProducts().isEmpty()) {

                lspTI = calcLSP(wealthGroupInterview.getLivestockProducts(), CASH, isChangeScenario, false, false, modellingScenario);
                lspOP = calcLSP(wealthGroupInterview.getLivestockProducts(), FOOD, isChangeScenario, false, false, modellingScenario);

            }
            if (!wealthGroupInterview.getLivestockSales().isEmpty()) {

                lssTI = calcLSS(wealthGroupInterview.getLivestockSales(), CASH, isChangeScenario, modellingScenario);
                // No output from LSS

            }
            if (!wealthGroupInterview.getEmployment().isEmpty()) {

                employmentTI = calcEmpIncome(wealthGroupInterview.getEmployment(), CASH, isChangeScenario, false,
                        false, modellingScenario);
                employmentOP = calcEmpIncome(wealthGroupInterview.getEmployment(), FOOD, isChangeScenario, false,
                        false, modellingScenario);

            }
            if (!wealthGroupInterview.getTransfer().isEmpty()) {

                transfersTI = calcTransIncome(wealthGroupInterview.getTransfer(), CASH, isChangeScenario,
                        false, false, modellingScenario);
                transfersOP = calcTransIncome(wealthGroupInterview.getTransfer(), FOOD, isChangeScenario,
                        false, false, modellingScenario);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            // addWarning("Error in WG DI Calculation ");
            System.out.println("Exception in DI Calc");
        }

        // FIX for members
        // requiredCalories = household.getHouseholdMember().size() * RC; // Unique
        // Households after filter

        Double energyNeed = 2100.0;

        // For OHEA Required Calories is Number of people in wealthgroup * 2100

        Double wghhsize = 0.0;
        Integer wghhsizeBD = 0;
        try {
            wghhsizeBD = wealthGroupInterview.getWgAverageNumberInHH();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (wghhsizeBD != null) {
            wghhsize = wghhsizeBD.doubleValue();
        } else {
            wghhsize = 0.0;
        }

        // wealthGroupInterview.getWealthgroup().getWghhsize().intValue();

        requiredCalories = wghhsize * energyNeed * 365.0;

        Double totalIncome = cropTI + wildfoodsTI + lspTI + lssTI + employmentTI + transfersTI;

        Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

        Double shortFall = requiredCalories - output;

        // Now it gets more complex , but not difficult

        // Diet // Diet Value = Sum (KCal per KG * Percentage of the Food type in
        // default diet

        defaultDietItems = (List<DefaultDietItem>) wealthGroupInterview.getWealthgroup().getCommunity()
                .getDefaultDietItem();

        /* Calc each part of ddi shortfall */
        for (DefaultDietItem defaultDietItem : defaultDietItems) {

            double calsNeededByThisDDI = shortFall / defaultDietItem.getPercentage();

            defaultDietItem.setCalsNeededofThisDDI(calsNeededByThisDDI);
        }

        Double dietValue = 0.0;
        Double dietPriceValue = 0.0;
        // Diet Amount Purchased DA = Shortfall / Diet Value in KGs
        Double dietAmountPurchased = 0.0;

        if (!defaultDietItems.isEmpty()) {

            dietValue = calcDdiKCalValue(defaultDietItems);
            dietPriceValue = calcDdiPriceValue(defaultDietItems);
            dietAmountPurchased = shortFall / dietValue;
        }
        wealthGroupInterview.setDdiKCalValue(dietValue);
        wealthGroupInterview.setDdiPriceValue(dietPriceValue);

        if (isChangeScenario) {
            wealthGroupInterview.setTotalOutputAfterChangeScenario(output);
            wealthGroupInterview.setTotalIncomeAfterChangeScenario(totalIncome);
        } else {
            wealthGroupInterview.setTotalOutput(output);
            wealthGroupInterview.setTotalIncome(totalIncome);
        }

        // Cost of Shortfall = Unit Price * Diet Amount Purchased // How many KGs in %
        // of diet is needed
        // i.e. 20% of KGs in Diet item 1 +80% diet item 2

        Double costOfShortfall = 0.0;

        for (DefaultDietItem defaultDietItem : defaultDietItems) {
            costOfShortfall += ((dietAmountPurchased * defaultDietItem.getPercentage() / 100)
                    * defaultDietItem.getUnitPrice().doubleValue());
        }

        // Disposable Income = Total Income - Cost of Shortfall
        Double disposableIncome = 0.0;

        disposableIncome = totalIncome - costOfShortfall;

        return Double.parseDouble(df2.format(disposableIncome));

    }

    /******************************************************************************************************************************************/

    /******************************************************************************************************************************************/
    /*
     * Return price yield variation in this modelling scenario for current
     * Project/Study
     */
    private static Double priceYieldVariation(ModellingScenario modellingScenario2, ResourceSubType resourceSubType,
                                              int type) { /* Type PRICE or YIELD */

        ResourceSubType rst;
        /*
         * Need to handle synonyms i.e. Mangoes is a synonym for Mango The price is the
         * same in parent and synonym Also need to handle a price / yield RST as a
         * Synonym
         *
         * Parent only sent to this func
         *
         */

        if (resourceSubType.getResourcesubtypesynonym() != null) {
            rst = resourceSubType.getResourcesubtypesynonym();
        } else {
            rst = resourceSubType;
        }

        Optional<PriceYieldVariation> pyc = null;

        pyc = priceYieldVariations.stream().filter(p -> p.getResource() == rst).findAny();
        if (!pyc.isPresent()) { // not found then try syn
            pyc = priceYieldVariations.stream().filter(p -> p.getResource().getResourcesubtypesynonym() == rst)
                    .findAny();
        }

        if (pyc.isPresent()) {

            if (type == PRICE) {
                if (pyc.get().getPrice() == 0)
                    return 1.0;
                else {

                    return pyc.get().getPrice() / 100.0;
                }
            } else if (type == YIELD) {
                if (pyc.get().getYield() == 0)
                    return 1.0;
                else {

                    return pyc.get().getYield() / 100.0;
                }
            }

        }
        // else
        return 1.0;
    }

    /******************************************************************************************************************************************/
    private static ResourceSubType calcRSTforFoodSubstitution(ResourceSubType rst, Boolean isFoodSubstitution, ModellingScenario modellingScenario) {
        /* Has this RST been substituted in model scenario ? */

        final ResourceSubType rstfinal = rst;

        if (!isFoodSubstitution) {
            return (rst);
        }

        if (modellingScenario.getFoodSubstitution().stream().filter(p -> p.getCurrentFood() == rstfinal).findAny()
                .isPresent()) {
            Optional<FoodSubstitution> findFirst = modellingScenario.getFoodSubstitution().stream()
                    .filter(p -> p.getCurrentFood() == rstfinal).findFirst();
            rst = findFirst.get().getSubstitutionFood();

            /* TODO - Is this s Synonym?? */

            if (rst.getResourcesubtypesynonym() != null) {
                rst = rst.getResourcesubtypesynonym();
            }
        }

        return rst;
    }

    /******************************************************************************************************************************************/

    // handle if Synonym is used and return base Kcal value
    public static int findRSTKcal(ResourceSubType rst) {

        if (rst.getResourcesubtypesynonym() != null) {

            try {

                return (rst.getResourcesubtypesynonym().getResourcesubtypekcal());

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // addMessage("Cannot get Synonym KCal value");
                return (0);
            }

        }

        return (rst.getResourcesubtypekcal());
    }

    /**
     * @param isFoodSubstitution TODO
     * @param isOHEA             TODO
     * @param modellingScenario
     ****************************************************************************************************************************************/

    public static Double calcWFIncome(Collection<WildFood> wildfoods, String type, Boolean isAfterChangeScenario,
                                      Boolean isFoodSubstitution, Boolean isOHEA, ModellingScenario modellingScenario) {
        Double wfTot = 0.0;
        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        ResourceSubType rst = null;

        /* What about food payments? */

        for (WildFood wf : wildfoods) {

            if (BooleanUtils.isTrue(wf.getIsFilteredOut())) {
                continue;
            }

            rst = wf.getResourceSubType();
            if (isFoodSubstitution && isOHEA) {
                rst = calcRSTforFoodSubstitution(rst, true, modellingScenario); /* RST + isFoodSubstitution */
            }

            if (isAfterChangeScenario) {

                priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);

            } else {

                priceChange = 1.0;
                yieldChange = 1.0;
            }

            Double produced = yieldChange * wf.getUnitsProduced();
            Double consumed = wf.getUnitsConsumed() * yieldChange;
            Double sold = wf.getUnitsSold() * yieldChange;
            Double otheruse = wf.getOtherUse() * yieldChange;

            if (type == "cash") {
                wfTot += sold * wf.getPricePerUnit() * priceChange;

            } else if (type == "food") {

                // Double consumed = production - wf.getUnitsSold();

                /* #501 yiedlchange already done */
                // wfTot += consumed * findRSTKcal(rst) * yieldChange;

                wfTot += consumed * findRSTKcal(rst);

                // wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType()) *
                // yieldChange;

            }
        }

        return wfTot;
    }

    /**
     * @param isFoodSubstitution TODO
     * @param isOHEA             TODO
     * @param modellingScenario
     ****************************************************************************************************************************************/

    public static Double calcTransIncome(Collection<Transfer> trans, String type, Boolean isAfterChangeScenario,
                                         Boolean isFoodSubstitution, Boolean isOHEA, ModellingScenario modellingScenario) {
        Double trTot = 0.0;
        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        ResourceSubType rst = null;
        ResourceSubType rst2 = null;

        /* Handle transfer types food/cash/other */

        for (Transfer tr : trans) {

            if (BooleanUtils.isTrue(tr.getIsFilteredOut())) {
                continue;
            }

            /*
             * #524
             */
            rst = tr.getFoodResourceSubType();

            /* #532 if a pure cash transfer then rst will be null */
            /* Just need to add the cash transfer amount to trtot */

            if (isFoodSubstitution && isOHEA && tr.getTransferType() == TransferType.Food) {
                rst = calcRSTforFoodSubstitution(rst, true, modellingScenario); /* RST + isFoodSubstitution */
            }

            if (isAfterChangeScenario && tr.getTransferType() == TransferType.Food) {

                priceChange = priceYieldVariation(modellingScenario, rst, PRICE);

                /* #528 Transfers only apply Price change */
                // yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);
                yieldChange = 1.0;

            } else {

                priceChange = 1.0;
                yieldChange = 1.0;
            }

            if (type == "cash") {
                /* #528 no yieldchange */
                if (tr.getTransferType().equals(TransferType.Food)) {

                    trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving() * tr.getTimesReceived()
                            * priceChange;

                } else // Cash or Other
                {
                    trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
                }
            } else if (type == "food" && tr.getTransferType().equals(TransferType.Food) && rst != null) {
                rst = tr.getFoodResourceSubType();
                rst = calcRSTforFoodSubstitution(rst, isFoodSubstitution, modellingScenario);
                if (isAfterChangeScenario) {
                    priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                    /* #528 Transfers only apply Price change */
                    // yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);
                    yieldChange = 1.0;
                }

                trTot += (tr.getUnitsConsumed() * yieldChange) * findRSTKcal(rst) * tr.getPeopleReceiving()
                        * tr.getTimesReceived();

            }

        }
        if (trTot.isNaN())
            return 0.0;
        else

            return trTot;
    }

    /******************************************************************************************************************************************/

    /**
     * @param isFoodSubstitution TODO
     * @param isOHEA             TODO
     * @param modellingScenario
     ****************************************************************************************************************************************/

    public static Double calcLSP(Collection<LivestockProducts> lsps, String type, Boolean isAfterChangeScenario,
                                 Boolean isFoodSubstitution, Boolean isOHEA, ModellingScenario modellingScenario) {

        Double lsTot = 0.0;
        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        ResourceSubType rst = null;

        for (LivestockProducts lsp : lsps) {

            if (BooleanUtils.isTrue(lsp.getIsFilteredOut())) {
                continue;
            }

            rst = lsp.getResourceSubType();
            if (isFoodSubstitution && isOHEA) {
                rst = calcRSTforFoodSubstitution(rst, true, modellingScenario); /* RST + isFoodSubstitution */
            }

            if (isAfterChangeScenario) {

                priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);

            } else {

                priceChange = 1.0;
                yieldChange = 1.0;
            }

            Double produced = yieldChange * lsp.getUnitsProduced();
            Double consumed = lsp.getUnitsConsumed() * yieldChange;
            Double sold = lsp.getUnitsSold() * yieldChange;
            Double otheruse = lsp.getUnitsOtherUse() * yieldChange;

            if (type == "cash") {

                lsTot += sold * lsp.getPricePerUnit() * priceChange;

            } else if (type == "food") {

                // Double consumed = production - lsp.getUnitsSold();
                // Double consumed = lsp.getUnitsConsumed();

                lsTot += consumed * findRSTKcal(rst);

            }

        }

        return lsTot;

    }

    /******************************************************************************************************************************************/

    public static Double calcLSS(Collection<LivestockSales> lss, String type, Boolean isAfterChangeScenario, ModellingScenario modellingScenario) {

        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        Double lsTot = 0.0;

        if (type == "cash") {

            for (LivestockSales ls : lss) {
                if (BooleanUtils.isTrue(ls.getIsFilteredOut())) {
                    continue;
                }
                if (isAfterChangeScenario) {

                    priceChange = priceYieldVariation(modellingScenario, ls.getResourceSubType(), PRICE);
                    yieldChange = priceYieldVariation(modellingScenario, ls.getResourceSubType(), YIELD);

                } else {

                    priceChange = 1.0;
                    yieldChange = 1.0;
                }

                lsTot += ls.getUnitsSold() * ls.getPricePerUnit() * priceChange;
            }
        }
        if (lsTot.isNaN())
            return 0.0;
        else

            return lsTot;

    }

    /**
     * @param isFoodSubstitution TODO
     * @param isOHEA             TODO
     * @param modellingScenario
     ****************************************************************************************************************************************/

    public static Double calcEmpIncome(Collection<Employment> emps, String type, Boolean isAfterChangeScenario,
                                       Boolean isFoodSubstitution, Boolean isOHEA, ModellingScenario modellingScenario) {

        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        Double empTot = 0.0;
        ResourceSubType rst = null;

        for (Employment emp : emps) {

            if (BooleanUtils.isTrue(emp.getIsFilteredOut())) {
                continue;
            }

            rst = emp.getResourceSubType();

            if (isFoodSubstitution && isOHEA) {
                rst = calcRSTforFoodSubstitution(rst, true, modellingScenario); /* RST + isFoodSubstitution */
            }

            if (isAfterChangeScenario) {

                if (emp.getFoodResourceSubType() != null) {

                    priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                    yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);
                }
            } else {

                priceChange = 1.0;
                yieldChange = 1.0;
            }

            if (type == "cash") {

                empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();

            } else if (type == "food" && emp.getFoodResourceSubType() != null) {
                rst = emp.getFoodResourceSubType();
                if (isFoodSubstitution)
                    rst = calcRSTforFoodSubstitution(rst, true, modellingScenario);

                empTot += (int) (emp.getFoodPaymentUnitsPaidWork() * emp.getPeopleCount()) * emp.getUnitsWorked()
                        * findRSTKcal(rst) * yieldChange;
            }

            // else {
            // empTot = 0.0;
            // }

        }

        return empTot;

    }

    /**
     * @param isFoodSubstitution TODO
     * @param isOHEA             TODO
     * @param modellingScenario
     ****************************************************************************************************************************************/
    public static Double calcCropIncome(Collection<Crop> crops, String type, Boolean isAfterChangeScenario,
                                        Boolean isFoodSubstitution, Boolean isOHEA, ModellingScenario modellingScenario) {

        Double cropTot = 0.0;
        Double yieldChange = 0.0;
        Double priceChange = 0.0;
        ResourceSubType rst = null;

        for (Crop crop : crops) {

            if (BooleanUtils.isTrue(crop.getIsFilteredOut())) {
                continue;
            }

            rst = crop.getResourceSubType();

            /* #512 - need to do food substitution for OIHM and OHEA */

            // if (isFoodSubstitution && isOHEA) {
            if (isFoodSubstitution) {

                rst = calcRSTforFoodSubstitution(rst, true, modellingScenario); /* RST + isFoodSubstitution */

            }

            if (isAfterChangeScenario) {

                priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);

            } else {

                priceChange = 1.0;
                yieldChange = 1.0;
            }

            if (type == "cash") {

                cropTot += crop.getUnitsSold() * crop.getPricePerUnit() * priceChange * yieldChange;

            } else if (type == "food") {
                // ref Issue 413

                Double produced = yieldChange * crop.getUnitsProduced();
                Double consumed = crop.getUnitsConsumed() * yieldChange;
                Double sold = crop.getUnitsSold() * yieldChange;
                Double otheruse = crop.getUnitsOtherUse() * yieldChange;

                Double foodIncome = (produced - sold - otheruse) * findRSTKcal(rst);

                // cropTot += consumed * findRSTKcal(crop.getResourceSubType()) * yieldChange;

                Double colR = produced - consumed;
                Double colS = (produced - colR);

                // This looks wrong, hence following lines to calc colTotal
                Double colTotal = colS * findRSTKcal(rst);

                /* #501 Food Income = consumed * RST KCal */

                colTotal = consumed * findRSTKcal(rst);

                cropTot += colTotal;

                // cropTot += foodIncome;

            }
        }

        if (cropTot.isNaN())
            return 0.0;
        else
            return cropTot;
    }

    /******************************************************************************************************************************************/
    static Double calcHhmSolc(HouseholdMember hhm, StdOfLivingElement stdL) {

        int lowerAgeSOL = 0;
        int upperAgeSOL = 0;
        Gender genderSOL = null;

        lowerAgeSOL = stdL.getAgeRangeLower();
        upperAgeSOL = stdL.getAgeRangeUpper();
        genderSOL = stdL.getGender();

        if (genderSOL.equals(Gender.Both) && (hhm.getAge() >= lowerAgeSOL) && (hhm.getAge() <= upperAgeSOL)) {

            return (stdL.getCost() * stdL.getAmount());

        }
        if (genderSOL.equals(Gender.Male) && hhm.getGender().equals(Sex.Male) && (hhm.getAge() >= lowerAgeSOL)
                && (hhm.getAge() <= upperAgeSOL)) {

            return (stdL.getCost() * stdL.getAmount());

        }

        if (genderSOL.equals(Gender.Female) && hhm.getGender().equals(Sex.Female) && (hhm.getAge() >= lowerAgeSOL)
                && (hhm.getAge() <= upperAgeSOL)) {

            return (stdL.getCost() * stdL.getAmount());

        }

        return (0.0);
    }

    /******************************************************************************************************************************************/

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /******************************************************************************************************************************************/
    @Override

    public void execute() throws Exception {


        System.out.println("In Run Modelling Reports ");
        int countValidated = 0;

        WealthGroupInterview wealthGroupInterview = null;
        Site site = null;
        int ddiTotPercent = 0;
        Boolean isCommunityHasExpandabilityRule = false;

        /*
         * Get Change Scenario Details
         *
         */
        //modellingScenarioId = getPreviousView().getValueString("id");
        //modellingScenario = (ModellingScenario) XPersistence.getManager().find(ModellingScenario.class, this.getModellingScenarioId());


        Efdutils.em("Mod scenario = " + modellingScenario.getTitle() + " " + getModellingScenarioId());

        if (modellingScenario.getStudy() != null) {
            isOIHM = true;
            isOHEA = false;
        } else if (modellingScenario.getProject() != null) {
            isOIHM = false;
            isOHEA = true;
        } else {
            addError("No Project or Study set in Modelling Scenario");
        }


        /*
         * Get Model Type - Change Scenario or Coping Strategy, though Coping Strategy
         * performs Change Scenario before Coping Strategy
         *
         * If Coping Strategy need to find Coping Strategy for this Study/Project and
         * alert if one has not been created
         *
         */

        //model = getView().getValueString("modelType");

        if (getModelType().equals(ModelType.CopingStrategy)) {
            isCopingStrategy = true;
        } else if (getModelType().equals(ModelType.ChangeScenario)) {
            isCopingStrategy = false;
        } else {
            addError("Select Change Scenario or Coping Strategy Modelling");
            return;

        }

        Object communityId = null; // getPreviousView().getValue("communityid");

        //targetTab = getView().getSubview(get()).getCollectionTab();

        // init Map array

        // TODO FIX Size to be size needed for selectones i.e. all sites
        Map[] selectedOnes = new Map[0];

        try {
            selectedOnes = getTargetTab().getSelectedKeys();
        } catch (Exception e) {
            //e.printStackTrace();
            // ModellingReports called from API - run for all HH or Villages
            if (isOHEA) {
                //=======================================================================================================
                // Needed for call from API with no populated LZ Site list
                int lzcount = 0;
                Status status;
                String lzid = "";
                String correctLoc = "";
                String previousCorrectLoc = "";
                List<String> lzidList = new ArrayList<String>();
                List<String> locList = new ArrayList<String>();

                Collection<LivelihoodZone> lzs = modellingScenario.getProject().getLivelihoodZone();
                for (LivelihoodZone lzs2 : lzs) {
                    for (Site site2 : lzs2.getSite()) {
                        for (Community community : site2.getCommunity()) {
                            lzcount = 0; //reset valid wg count #591
                            for (WealthGroup wealthGroup : community.getWealthgroup()) {
                                for (WealthGroupInterview wealthGroupInterview3 : wealthGroup.getWealthGroupInterview()) {
                                    status = wealthGroupInterview3.getStatus();
                                    if (status == Status.Validated) // add this to valid LZ / Site/ WG community list
                                    {
                                        lzcount++;
                                        // Efdutils.em("valid lz = " + lzs2.getLzid());
                                        lzid += "'" + lzs2.getLzid() + "',";

                                        correctLoc += "'" + community.getSite().getLocationid() + "',";

                                        lzidList.add(lzs2.getLzid());
                                        locList.add(community.getSite().getLocationid());

                                    }
                                }

                            }
                        }
                        if (lzcount < 3) { // Need at least 3 Valid WGIs in a Site
                            correctLoc = previousCorrectLoc;
                        }
                        previousCorrectLoc = correctLoc;
                        sites.add(site2);

                    }
                    Efdutils.em("lz lzcount in LZ loop= " + lzcount);
                    for (String s : locList) {
                        System.out.println("site = " + s);
                    }

                    //======================================================================================================
                }


                Map<String, String> map = new HashMap<>();
                //map.put("locationid",locList.get(0));

                selectedOnes = new Map[locList.size()];
                for (int i = 0; i < locList.size(); i++) {
                    map.put("locationid", locList.get(i));

                    selectedOnes[i] = new HashMap();
                    selectedOnes[i].putAll(map);

                }


                System.out.println("done array create == " + selectedOnes[0].get("locationid"));


            }


        }

        /* Populate local price yield variation for this Study/Project */


        for (PriceYieldVariation priceYieldVariation : modellingScenario.getPriceYieldVariations()) {
            priceYieldVariations.add(priceYieldVariation);
        }
        if (!modellingScenario.getFoodSubstitution().isEmpty()) {

            isFoodSubstitution = true;

        }
        if (selectedOnes.length == 0) {
            addError("Select at least one Community or Site");
            return;
        } else if (selectedOnes.length != 0 && isOHEA) {
            isSelectedSites = true; // One or more Site selected in dialog
            /* Any Food Substitution to calc Nutrient Change Scenario? */

            project = modellingScenario.getProject();


            for (int i = 0; i < selectedOnes.length; i++) {
                Map<?, ?> key = selectedOnes[i];

                String locidofsite = key.get("locationid").toString();


                //try {
                //  site = XPersistence.getManager().find(Site.class, locidofsite);
                //} catch (Exception e) {
                //e.printStackTrace();

                // Need JPA Get from Spring 5
                List<Site> siteCollect = sites.stream().filter(p -> p.getLocationid() == locidofsite).collect(Collectors.toList());

                site = siteCollect.get(0);             //}


                livelihoodZone = site.getLivelihoodZone();

                Collection<Community> community3 = site.getCommunity();

                // add to array for Sites/Communities
                Site s = new Site();
                s = site;
                sites.add(s);

                // From Site get Communities/Sites then get WG and WGI

                for (Community community2 : site.getCommunity()) {

                    int countValidatedWGI = 0;

                    countValidated = 0;

                    for (WealthGroup wg : community2.getWealthgroup()) {
                        for (WealthGroupInterview wgi : wg.getWealthGroupInterview()) {

                            if (wgi.getStatus() == Status.Validated) {
                                countValidatedWGI++;
                            }

                        }
                    }

                    // long countOfValidatedWGIs = community2.getWealthgroup().stream()
                    // .filter(p -> p.getWealthGroupInterview().get(0).getStatus() ==
                    // Status.Validated).count();

                    if (countValidatedWGI < 3) {
                        // if (countOfValidatedWGIs != 3) {
                        sites.remove(s);
                        continue;
                    }

                    if (isCopingStrategy) {
                        expandabilityRules = XPersistence.getManager().createQuery(
                                "from ExpandabilityRule where communityRuleSet = :communityRuleSet order by sequence")
                                .setParameter("communityRuleSet", community2).getResultList();
                        Efdutils.em("expandability rule is empty = " + expandabilityRules.isEmpty());
                        if (!expandabilityRules.isEmpty()) {
                            isCommunityHasExpandabilityRule = true;
                        }
                    }

                    Iterator<WealthGroup> wgIterator = community2.getWealthgroup().iterator();
                    while (wgIterator.hasNext()) {

                        WealthGroup wgNext = wgIterator.next();
                        if (wgNext.getWgorder() > 3 || wgNext.getWgorder() < 1) {
                            addError("Wealthgroup " + wgNext.getCommunity().getSite().getLocationdistrict() + " "
                                    + wgNext.getCommunity().getSite().getSubdistrict() + " " + wgNext.getWgnameeng()
                                    + "Order not between 1 and 3");
                            continue;
                        }

                        Iterator<WealthGroupInterview> wgiIterator = wgNext.getWealthGroupInterview().iterator();
                        while (wgiIterator.hasNext()) {
                            WealthGroupInterview wgiNext = wgiIterator.next();

                            if (wgiNext.getStatus() == Status.Validated) {
                                WealthGroupInterview w = new WealthGroupInterview();
                                w = wgiNext;

                                wgiList.add(w);

                                WGI e = new WGI();
                                e.setWealthgroupInterview(wgiNext);
                                e.setWealthgroup(wgNext);
                                e.setSite(community2.getSite());
                                e.setCommunity(community2);
                                isSelectedSites = true;
                                wgiSelected.add(e);
                                countValidated++;

                            }

                        }

                    }

                }

                if (!isCommunityHasExpandabilityRule && isCopingStrategy) {


                    addError("No Expandability Rules for selected Communities for Coping Strategy Report");
                    closeDialog();
                    return;
                }

            }

            // Populate WGI array wgis - use dialog selected list if enter

            /* TODO - allow for less than 3 WGIs and average out - as per ohea report */
            /* meanwhile - do not use if <3 Validated WGIs */

            populateWGIArray(wgiList);

            uniqueCommunity = wgi.stream().filter(distinctByKey(WGI::getCommunity))
                    .sorted(Comparator.comparing(WGI::getSubDistrict)).collect(Collectors.toList());

            uniqueWG = wgi.stream().filter(distinctByKey(WGI::getWealthgroup)).collect(Collectors.toList());

            errno = 51;

            int ddipercent = 0;
            Iterator<WealthGroupInterview> wgiIter = wgiList.iterator();

            while (wgiIter.hasNext()) {

                WealthGroupInterview wgiNext = wgiIter.next();

                if (wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 100
                        && wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 0) {
                    addError("Default Diet Total Percentage for a chosen Community is not 100% or 0%");
                    closeDialog();
                    return;
                }

            }

            errno = 52;

            calculateDI();
            System.out.println("done calc DI");
            // calculateAE(); // Calculate the Adult equivalent
            System.out.println("done calc AE");

        } else if (selectedOnes.length != 0 && isOIHM) { // OIHM

            // Does Study have Expandability Rule?
            study = modellingScenario.getStudy();
            ihmProject = modellingScenario.getStudy().getProjectlz();

            if (isCopingStrategy) {
                expandabilityRules = XPersistence.getManager()
                        .createQuery("from ExpandabilityRule where studyRuleSet = :study order by sequence")
                        .setParameter("study", study).getResultList();
                Efdutils.em("expandability rule is empty = " + expandabilityRules.isEmpty());
                if (expandabilityRules.isEmpty()) {
                    addError("No Expandability Rules for Study in Coping Strategy Report");
                    closeDialog();
                    return;
                }
            }

            isSelectedHouseholds = true; // One or more HH selected in dialog
            for (int i = 0; i < selectedOnes.length; i++) {
                Map<?, ?> key = selectedOnes[i];

                // TODO
                //Map<?, ?> membersNames = getView().getSubview("study.household").getMembersNames();

                String subKey = key.toString().substring(4, 36);

                Household singleHHSelected = XPersistence.getManager().find(Household.class, subKey);
                if (singleHHSelected.getHouseholdMember().size() == 0) {
                    continue;
                }

                if (singleHHSelected.getStatus() == Status.Validated) {
                    isValid = true;
                }

                HH e = new HH();
                e.setHousehold(singleHHSelected);
                hhSelected.add(e);

                selectedHouseholds.add(singleHHSelected);
            }


            defaultDietItems = (List<DefaultDietItem>) study.getDefaultDietItem();
            List<Household> households = XPersistence.getManager()
                    .createQuery("from Household where study_id = :study and status = :status ")
                    .setParameter("study", study.getId()).setParameter("status", Status.Validated).getResultList();

            if (households.isEmpty()) {
                addError("No Validated Households in this Study");
                closeDialog();
                return;
            }

            // Check DDI total percent

            for (DefaultDietItem defaultDietItem : defaultDietItems) {
                ddiTotPercent += defaultDietItem.getPercentage();
            }
            if (ddiTotPercent != 100) {
                addError("Default Diet Total Percentage for this Study is not 100%");
                closeDialog();
                return;
            }

            populateHHArray(selectedHouseholds);
            // No Filtering as CustomReportSpec not used in Modelling Reports

            Efdutils.em("No in HH after populate = " + hh.size());

            calculateDI(); // uniqueHH set in calculateDI

            calculateAE(); // Calculate the Adult equivalent

        }

        errno = 54;
        // Run reports
        try {

            report = createReport("ModellingReports");
            errno = 55;

            System.out.println("report = " + report);
            System.out.println("session " + ReportXLSServlet.SESSION_XLS_REPORT);



            //session.setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
            errno = 56;
            System.out.println("session done  " + errno);
            //  setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
            errno = 57;
        } catch (Exception e) {
            addError(e + " Errno = " + errno);
            closeDialog();
            return;
        }

        //closeDialog();

    }

    /******************************************************************************************************************************************/

    private void calculateAE() {

        if (isOIHM) {
            for (HH hh2 : uniqueHousehold) {

                hh2.setHhAE(householdAE(hh2.getHousehold()));

            }
        } else if (isOHEA) {

            for (WGI wg2 : uniqueWealthgroupInterview) {

                wg2.setWgiAE(wealthgroupInterviewAE(wg2.getWealthgroupInterview()));

            }
        }
    }

    /******************************************************************************************************************************************/

    private void calculateDI() {

        double wgiDI = 0.0;

        if (isOHEA) {

            Efdutils.em("In OHEA DI Calc wgilist tot = " + wgiList.size());

            List<WGI> uniqueWGI = wgi.stream().filter(distinctByKey(WGI::getWealthgroupInterview))
                    .collect(Collectors.toList());

            uniqueWealthgroupInterview = uniqueWGI;

            for (WGI wgi2 : uniqueWGI) {

                isChangeScenario = false;
                wgiDI = wealthgroupInterviewDI(wgi2.getWealthgroupInterview(), isChangeScenario, modellingScenario);
                wgi2.setWgiDI(wgiDI);
                wgi2.getWealthgroupInterview().setdI(wgiDI);

                isChangeScenario = true;
                Double wgiDIacs = wealthgroupInterviewDI(wgi2.getWealthgroupInterview(), isChangeScenario, modellingScenario);
                wgi2.setWgiDIAfterChangeScenario(wgiDIacs);
                wgi2.getWealthgroupInterview().setdIAfterChangeScenario(wgiDIacs);

                if (isCopingStrategy) {
                    calcCopingStrategy(wgi2.getWealthgroupInterview(),
                            expandabilityRules); /*
                     * sets hh2 household transients for DI,Stol, food, cash after
                     * Coping Strategy
                     */
                }

            }
            // Need sort by LZ and then Order (Poor/Middle/Better Off in WG Order field
            Comparator<WGI> compareBy = Comparator.comparing(WGI::getSubDistrict).thenComparing(WGI::getOrder);

            uniqueWealthgroupInterview = wgi.stream().filter(distinctByKey(WGI::getWealthgroupInterview))
                    .sorted(compareBy).collect(Collectors.toList());

        } else if (isOIHM) {

            uniqueHousehold = hh.stream().filter(p -> p.getHousehold().getHouseholdMember().size() > 0)
                    .filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber())).collect(Collectors.toList());
            defaultDietItems = (List<DefaultDietItem>) study.getDefaultDietItem();
            for (HH hh2 : uniqueHousehold) {

                isChangeScenario = false;
                hh2.setHhDI(householdDI(hh2.getHousehold(), isChangeScenario, defaultDietItems, modellingScenario));
                isChangeScenario = true;
                hh2.setHhDIAfterChangeScenario(householdDI(hh2.getHousehold(), isChangeScenario, defaultDietItems, modellingScenario));

                /* change to use household transient */
                hh2.getHousehold().setdIAfterChangeScenario(hh2.getHhDIAfterChangeScenario());
                hh2.getHousehold().setdI(hh2.getHhDI());

                calcHHSOL();
                if (isCopingStrategy) {
                    calcCopingStrategy(hh2.getHousehold(),
                            expandabilityRules); /*
                     * sets hh2 household transients for DI,Stol, food, cash after
                     * Coping Strategy
                     */
                }

            }
            uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber())) // Sort by
                    .filter(p -> p.getHousehold().getHouseholdMember().size() > 0) // DI low -
                    // high
                    .sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());
        }

    }

    /******************************************************************************************************************************************/

    private void calcCopingStrategy(Household household, List<ExpandabilityRule> expandabilityRules2) {
        if (isCopingStrategy) {

            Double cashGain = 0.0;
            Double prevCashGain = 0.0;
            Double prevHHAfterChangeScenario = household.getdIAfterChangeScenario();
            Double prevHHOSolAfterChangeScenario = household.getTotalIncomeAfterChangeScenario() - household.getSOLC();

            for (ExpandabilityRule expandabilityRule : expandabilityRules2) {

                if (prevHHAfterChangeScenario > 0.0 && prevHHOSolAfterChangeScenario > 0.0) {
                    /*
                     * No need to calc coping strategy change as already positive Need to set HH
                     * Expandability as used later - set to 0 value as not needed as already +ve
                     *
                     */
                    household.setExpandabilityRule(expandabilityRule);
                    household.getExpandabilityRule().setExpandabilityDI(0.0);
                    household.getExpandabilityRule().setExpandabilityCostOfDeficitPurchase(0.0);

                } else // get check if expandability rule applies for this hh
                {
                    prevCashGain = cashGain;
                    cashGain = expandabilityGain(expandabilityRule, household, "cash");
                    Double totalCashGain = prevCashGain + cashGain;

                    /*
                     * Also Need to account for extra spend on DDI
                     *
                     */

                    /*
                     * Get Increase in HH Food Deficit after Change and Expandability
                     */
                    Double foodDeficit = -expandabilityGain(expandabilityRule, household, "food");

                    // M
                    Double m5 = household.getTotalOutput() - household.getTotalOutputAfterChangeScenario();
                    // N And F2
                    Double n5 = m5 - foodDeficit;

                    // DI With Coping 418
                    Double f2 = n5;

                    // G2
                    Double g2 = f2 / household.getDdiKCalValue();
                    // Cost of Deficit
                    // H2

                    Double h2 = g2 * household.getDdiPriceValue();

                    Double i2 = household.getdIAfterChangeScenario() + totalCashGain - h2;

                    household.setExpandabilityRule(expandabilityRule);
                    household.getExpandabilityRule().setExpandabilityDI(i2);
                    household.getExpandabilityRule().setExpandabilityCostOfDeficitPurchase(h2);

                    // reportWB.getSheet(isheet).setValue(col, row, i2,
                    // numberd2);
                    prevHHAfterChangeScenario = i2;
                }

            }
        }

    }

    /******************************************************************************************************************************************/

    private void calcCopingStrategy(WealthGroupInterview wgi, List<ExpandabilityRule> expandabilityRules2) {
        if (isCopingStrategy) {

            Double cashGain = 0.0;
            Double prevCashGain = 0.0;
            Double prevWGIAfterChangeScenario = wgi.getdIAfterChangeScenario();

            for (ExpandabilityRule expandabilityRule : expandabilityRules2) {

                // expval.setWealthGroupInterview(wgi);

                if (prevWGIAfterChangeScenario > 0) {
                    /*
                     * TODO
                     */
                } else // get check if expandability rule applies for this hh
                {
                    prevCashGain = cashGain;
                    cashGain = expandabilityGain(expandabilityRule, wgi, "cash");
                    Double totalCashGain = prevCashGain + cashGain;

                    /*
                     * Also Need to account for extra spend on DDI
                     *
                     */

                    /*
                     * Get Increase in HH Food Deficit after Change and Expandability
                     */
                    Double foodDeficit = -expandabilityGain(expandabilityRule, wgi, "food");

                    // M
                    Double m5 = wgi.getTotalOutput() - wgi.getTotalOutputAfterChangeScenario();
                    // N And F2
                    Double n5 = m5 - foodDeficit;

                    // DI With Coping 418
                    Double f2 = n5;

                    // G2
                    Double g2 = f2 / wgi.getDdiKCalValue();
                    // Cost of Deficit
                    // H2

                    Double h2 = g2 * wgi.getDdiPriceValue();

                    Double i2 = wgi.getdIAfterChangeScenario() + totalCashGain - h2;

                    prevWGIAfterChangeScenario += totalCashGain;

                }

            }

        }
    }

    /******************************************************************************************************************************************/
    private void populateWGIArray(List<WealthGroupInterview> wealthgroupsInterviews) {
        /*
         * create array of all Validated wealthgroups for this commmunity
         *
         * they can then be filtered
         *
         */

        // wealthgroupinterviews

        for (WealthGroupInterview wealthgroupInterview : wealthgroupsInterviews) {

            populateWGIfromWealthgroupInterview(wealthgroupInterview);

        }

    }

    /******************************************************************************************************************************************/
    private void populateHHArray(List<Household> households) {
        /*
         * create array of all Validated households for this study
         *
         * they can then be filtered
         *
         */

        ConfigAnswer answer = null;

        for (Household household : households) {
            populateHHfromHousehold(household, answer);

            for (ConfigAnswer configAnswer : household.getConfigAnswer()) {
                addTohhArray(household, null, null, null, configAnswer, "Answer", null, null, null, null, null, null,
                        null, null, null, null, null, null, null);

            }

        }

    }

    /******************************************************************************************************************************************/

    private void addTohhArray(Household household, Collection<Category> category, ResourceType resourceType,
                              ResourceSubType resourceSubType, ConfigAnswer answer, String type, AssetLand land, AssetFoodStock foodstock,
                              AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable, AssetTree tree, Crop crop,
                              Employment employment, Inputs inputs, LivestockProducts livestockproducts, LivestockSales livestocksales,
                              Transfer transfer, WildFood wildfood) {

        HH e = new HH();

        // e.wgiNumber = 0;

        // System.out.println("add to hh array rst =
        // "+resourceSubType.getResourcetypename());

        e.setHousehold(household);
        e.setHhNumber(household.getHouseholdNumber());
        e.setCategory(category);
        e.setResourceType(resourceType);
        e.setResourceSubType(resourceSubType);

        e.setAnswer(answer);

        e.setType(type);
        e.setLand(land);
        e.setEmployment(employment);
        e.setFoodstock(foodstock);
        e.setInputs(inputs);
        e.setLivestock(livestock);
        e.setLivestockproducts(livestockproducts);
        e.setLivestocksales(livestocksales);
        e.setTradeable(tradeable);
        e.setTransfer(transfer);
        e.setTree(tree);
        e.setWildfood(wildfood);
        e.setCrop(crop);
        e.setCash(cash);


        hh.add(e);

    }

    /******************************************************************************************************************************************/

    private void populateHHfromHousehold(Household household, ConfigAnswer answer) {

        for (AssetLand asset : household.getAssetLand()) {

            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory(); // List of Categories that
            // include this RST
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Land", asset, null, null, null,
                    null, null, null, null, null, null, null, null, null);

        }
        for (AssetFoodStock asset : household.getAssetFoodStock()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Foodstock", null, asset, null,
                    null, null, null, null, null, null, null, null, null, null);
        }
        for (AssetCash asset : household.getAssetCash()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Cash", null, null, asset, null,
                    null, null, null, null, null, null, null, null, null);

        }
        for (AssetLiveStock asset : household.getAssetLiveStock()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Livestock", null, null, null,
                    asset, null, null, null, null, null, null, null, null, null);
        }
        for (AssetTradeable asset : household.getAssetTradeable()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Tradeable", null, null, null,
                    null, asset, null, null, null, null, null, null, null, null);
        }
        for (AssetTree asset : household.getAssetTree()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Tree", null, null, null, null,
                    null, asset, null, null, null, null, null, null, null);
        }
        for (Crop asset : household.getCrop()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Crop", null, null, null, null,
                    null, null, asset, null, null, null, null, null, null);
        }
        for (Employment asset : household.getEmployment()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Employment", null, null, null,
                    null, null, null, null, asset, null, null, null, null, null);
        }
        for (Inputs asset : household.getInputs()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Input", null, null, null, null,
                    null, null, null, null, asset, null, null, null, null);
        }
        for (LivestockProducts asset : household.getLivestockProducts()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "LivestockProduct", null, null,
                    null, null, null, null, null, null, null, asset, null, null, null);
        }
        for (LivestockSales asset : household.getLivestockSales()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "LivestockSale", null, null, null,
                    null, null, null, null, null, null, null, asset, null, null);
        }
        for (Transfer asset : household.getTransfer()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Transfer", null, null, null, null,
                    null, null, null, null, null, null, null, asset, null);
        }
        for (WildFood asset : household.getWildFood()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addTohhArray(household, category, resourceType, resourceSubType, answer, "Wildfood", null, null, null, null,
                    null, null, null, null, null, null, null, null, asset);
        }

    }

    /******************************************************************************************************************************************/

    private void populateWGIfromWealthgroupInterview(WealthGroupInterview wealthGroupInterview) {

        WealthGroupInterview wgi2;

        for (AssetLand asset : wealthGroupInterview.getAssetLand()) {

            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory(); // List of Categories that
            // include this RST
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Land", asset, null, null,
                    null, null, null, null, null, null, null, null, null, null);

        }
        for (AssetFoodStock asset : wealthGroupInterview.getAssetFoodStock()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Foodstock", null, asset, null,
                    null, null, null, null, null, null, null, null, null, null);
        }
        for (AssetCash asset : wealthGroupInterview.getAssetCash()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = null;
            ResourceType resourceType = null;
            try {
                category = asset.getResourceSubType().getCategory();
                resourceType = asset.getResourceSubType().getResourcetype();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }

            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Cash", null, null, asset,
                    null, null, null, null, null, null, null, null, null, null);

        }
        for (AssetLiveStock asset : wealthGroupInterview.getAssetLiveStock()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Livestock", null, null, null,
                    asset, null, null, null, null, null, null, null, null, null);
        }
        for (AssetTradeable asset : wealthGroupInterview.getAssetTradeable()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Tradeable", null, null, null,
                    null, asset, null, null, null, null, null, null, null, null);
        }
        for (AssetTree asset : wealthGroupInterview.getAssetTree()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Tree", null, null, null, null,
                    null, asset, null, null, null, null, null, null, null);
        }
        for (Crop asset : wealthGroupInterview.getCrop()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Crop", null, null, null, null,
                    null, null, asset, null, null, null, null, null, null);
        }
        for (Employment asset : wealthGroupInterview.getEmployment()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Employment", null, null, null,
                    null, null, null, null, asset, null, null, null, null, null);
        }

        for (LivestockProducts asset : wealthGroupInterview.getLivestockProducts()) {
            ResourceSubType resourceSubType = null;
            Collection<Category> category = null;
            ResourceType resourceType = null;
            try {
                resourceSubType = asset.getResourceSubType();
                category = asset.getResourceSubType().getCategory();
                resourceType = asset.getResourceSubType().getResourcetype();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "LivestockProduct", null, null,
                    null, null, null, null, null, null, null, asset, null, null, null);
        }
        for (LivestockSales asset : wealthGroupInterview.getLivestockSales()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "LivestockSale", null, null,
                    null, null, null, null, null, null, null, null, asset, null, null);
        }
        for (Transfer asset : wealthGroupInterview.getTransfer()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Transfer", null, null, null,
                    null, null, null, null, null, null, null, null, asset, null);
        }
        for (WildFood asset : wealthGroupInterview.getWildFood()) {
            ResourceSubType resourceSubType = asset.getResourceSubType();
            Collection<Category> category = asset.getResourceSubType().getCategory();
            ResourceType resourceType = asset.getResourceSubType().getResourcetype();
            addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Wildfood", null, null, null,
                    null, null, null, null, null, null, null, null, null, asset);
        }

        // If no assets need to create a record and set assets to null

        long numberWithAssets = wgi.stream().filter(g -> g.getWealthgroupInterview() == wealthGroupInterview).count();
        if (numberWithAssets == 0) {
            addToWGIArray(wealthGroupInterview, null, null, null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null);
        }

    }

    /******************************************************************************************************************************************/

    private void addToWGIArray(WealthGroupInterview wealthGroupInterview, Collection<Category> category,
                               ResourceType resourceType, ResourceSubType resourceSubType, String type, AssetLand land,
                               AssetFoodStock foodstock, AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable,
                               AssetTree tree, Crop crop, Employment employment, Inputs inputs, LivestockProducts livestockproducts,
                               LivestockSales livestocksales, Transfer transfer, WildFood wildfood) {

        WGI e = new WGI();

        e.setWealthgroupInterview(wealthGroupInterview);
        e.setSite(wealthGroupInterview.getWealthgroup().getCommunity().getSite());
        e.setSubDistrict(wealthGroupInterview.getWealthgroup().getCommunity().getSite().getSubdistrict());
        e.setCommunity(wealthGroupInterview.getWealthgroup().getCommunity());
        e.setWealthgroup(wealthGroupInterview.getWealthgroup());
        e.setOrder(wealthGroupInterview.getWealthgroup().getWgorder());
        e.setProject(project);
        e.setLivelihoodZone(livelihoodZone);

        // e.wgiNumber = 0;
        e.setCategory(category);
        e.setResourceType(resourceType);
        e.setResourceSubType(resourceSubType);

        e.setType(type);
        e.setLand(land);
        e.setEmployment(employment);
        e.setFoodstock(foodstock);
        e.setInputs(inputs);
        e.setLivestock(livestock);
        e.setLivestockproducts(livestockproducts);
        e.setLivestocksales(livestocksales);
        e.setTradeable(tradeable);
        e.setTransfer(transfer);
        e.setTree(tree);
        e.setWildfood(wildfood);
        e.setCrop(crop);
        e.setCash(cash);

        wgi.add(e);

    }

    /******************************************************************************************************************************************/

    private JxlsWorkbook createReport(String reportTitle) throws Exception {

        String sDate = Calendar.getInstance().getTime().toString();
        String filename = "";

        if (!isCopingStrategy && isOHEA) {
            filename = "Change"
                    + project.getProjecttitle().substring(0, Math.min(10, project.getProjecttitle().length()))
                    + Calendar.getInstance().getTime();
        } else if (isCopingStrategy && isOHEA) {
            filename = "Coping"
                    + project.getProjecttitle().substring(0, Math.min(10, project.getProjecttitle().length()))
                    + Calendar.getInstance().getTime();
        } else if (!isCopingStrategy && isOIHM) {

            filename = "Change"
                    + ihmProject.getProjecttitle().substring(0, Math.min(10, ihmProject.getProjecttitle().length()))
                    + Calendar.getInstance().getTime();
        } else if (isCopingStrategy && isOIHM) {

            filename = "Coping"
                    + ihmProject.getProjecttitle().substring(0, Math.min(10, ihmProject.getProjecttitle().length()))
                    + Calendar.getInstance().getTime();
        }

        reportWB = new JxlsWorkbook(filename);
        Boolean isCopingStrategy = true;
        Boolean isnotCopingStrategy = false;


        createHeaderPage(); // populates reportList and creates first worksheet

        int ireportNumber = 0; // should be equal to the sheet number in workbook

        /*
         * Modelling reports - run them all for OIHM or OHEA
         *
         */

        for (Report report : reportList) {
            int reportCode = report.getCode();
            ireportNumber++; // keep track of number of reports = sheet on output spreadsheet

            switch (reportCode) {
                case 410:
                    // HH DI Before & After Change Scenario
                    createOIHMDIreport(ireportNumber, report, isnotCopingStrategy);
                    break;
                case 411:
                    // HH DI with StoL before & after Change Scenario
                    createOIHMDIAfterSOLreport(ireportNumber, report, isnotCopingStrategy);
                    break;
                case 412:
                    // HH Cash Income before & after Change Scenario
                    createIncomereport(ireportNumber, report, "cash", OIHM, isnotCopingStrategy);
                    break;
                case 413:
                    // HH Food Income before & after Change Scenario
                    createIncomereport(ireportNumber, report, "food", OIHM, isnotCopingStrategy);
                    break;

                case 414:
                    createIncomereport(ireportNumber, report, "cash", OHEA, isnotCopingStrategy);
                    // Report WG Cash Income before & after Change Scenario
                    break;

                case 415:
                    createIncomereport(ireportNumber, report, "food", OHEA, isnotCopingStrategy);
                    // Report WG Food Income before & after Change Scenario
                    break;

                case 416:
                    // WG DI with StoL before & after Change Scenario
                    createOHEADIAfterSOLreport(ireportNumber, report, isnotCopingStrategy);
                    break;
                case 417:
                    // WG DI before & after Change Scenario
                    createOHEADIreport(ireportNumber, report, isnotCopingStrategy);
                    break;

                case 430:
                    // WG Nutrient before & after Change Scenario
                    createOHEANutrientreport(ireportNumber, report, isnotCopingStrategy);
                    break;
                case 431:
                    // HH Nutrient before & after Change Scenario
                    createOIHMNutrientreport(ireportNumber, report, isnotCopingStrategy);
                    break;
                /*
                 * Coping Strategy Reports
                 *
                 */

                case 418:
                    // Report HH DI after Change Scenario with Coping Strategy
                    createOIHMDIreport(ireportNumber, report, isCopingStrategy);
                    break;
                case 419:
                    // Report HH DI with Stol after Change Scenario with Coping Strategy
                    createOIHMDIAfterSOLreport(ireportNumber, report, isCopingStrategy);
                    break;
                case 420:
                    // Report HH Cash Income after Change Scenario with Coping Strategy
                    createIncomereport(ireportNumber, report, "cash", OIHM, isCopingStrategy);
                    break;
                case 421:
                    // Report HH Food Income after Change Scenario with Coping Strategy
                    createIncomereport(ireportNumber, report, "food", OIHM, isCopingStrategy);
                    break;
                case 422:
                    // Report WG Food Income after Change Scenario with Coping Strategy
                    createIncomereport(ireportNumber, report, "food", OHEA, isCopingStrategy);
                    break;
                case 423:
                    // Report WG Cash Income after Change Scenario with Coping Strategy
                    createIncomereport(ireportNumber, report, "cash", OHEA, isCopingStrategy);
                    break;
                case 424:
                    // Report WG DI with StoL after Change Scenario with Coping Strategy
                    createOHEADIAfterSOLreport(ireportNumber, report, isCopingStrategy);
                    break;
                case 425:
                    // Report WG DI after Change Scenario with Coping Strategy
                    createOHEADIreport(ireportNumber, report, isCopingStrategy);
                    break;
            }
        }

        return reportWB;
    }

    /******************************************************************************************************************************************/
    private void createOIHMDIreport(int isheet, Report report, Boolean isCopingStrategy) {

        int row = 1;
        int col = 4;
        Double cashGain = 0.0;
        Double prevCashGain = 0.0;
        Double totalCashGain = 0.0;
        String type = "cash";

        reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20);

        // hh is array of all data
        // need an array of valid HH now left
        // ordered by DI

        reportWB.getSheet(isheet).setValue(1, row, "Household Number", boldTopStyle);
        reportWB.getSheet(isheet).setValue(2, row, "DI As Reported", boldTopStyle);
        reportWB.getSheet(isheet).setValue(3, row, "DI After Change Scenario", boldTopStyle);

        if (isCopingStrategy) {
            expandabilityHeadings(isheet, row, col);
        }

        row++;

        Efdutils.em("uniqueHH = " + uniqueHousehold.size());
        for (HH hh2 : uniqueHousehold) {

            reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), numberd2);
            reportWB.getSheet(isheet).setValue(2, row, hh2.getHhDI(), numberd2);
            reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDIAfterChangeScenario(), numberd2);
            /****************************************************************************************/
            if (isCopingStrategy) {
                col = 4;
                cashGain = 0.0;
                prevCashGain = 0.0;
                Double prevHHOSolAfterChangeScenario = hh2.getHhDIAfterChangeScenario();
                for (ExpandabilityRule expandabilityRule : expandabilityRules) {
                    // #527
                    // if (prevHHOSolAfterChangeScenario > 0)

                    // If DI value after change scenario is > 0 then use DI after change scenario,
                    // otherwise apply expandability rule if applicaable

                    // {

                    // reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario,
                    // numberd2);
                    // } else {
                    prevCashGain = cashGain;

                    System.out.println("In 418 DI report ");
                    System.out.println("type = " + type);
                    System.out.println("exp rule = " + expandabilityRule.getAppliedResourceSubType().getResourcetypename());
                    System.out.println("prev cash gain = " + prevCashGain);
                    System.out.println("cash gain pre calc = " + cashGain);

                    cashGain = expandabilityGain(expandabilityRule, hh2.getHousehold(), type);
                    System.out.println("cash gain post calc = " + cashGain);
                    System.out.println("prevHHSolAfterChangeScenario  = " + prevHHOSolAfterChangeScenario);
                    totalCashGain = prevCashGain + cashGain;

                    reportWB.getSheet(isheet).setValue(col, row,
                            // prevHHOSolAfterChangeScenario + totalCashGain
                            prevHHOSolAfterChangeScenario + cashGain,
                            // -
                            // hh2.getHousehold().getExpandabilityRule().getExpandabilityCostOfDeficitPurchase(),
                            numberd2);
                    // prevHHOSolAfterChangeScenario += totalCashGain;
                    prevHHOSolAfterChangeScenario += cashGain;
                    // }

                    col++;
                }

            }

            row++;

        }

    }

    /******************************************************************************************************************************************/

    private void createOHEADIreport(int isheet, Report report, Boolean isCopingStrategy) {

        int row = 2;
        int col = 5;
        int i = 0;
        double hhSize = 0;
        Double cashGain = 0.0;
        Double prevCashGain = 0.0;
        Double totalCashGain = 0.0;
        String type = "cash";
        int counter = 0;
        double thisDI = 0.0;
        double thisDIAfterChangeScenario = 0.0;
        int numberOfAverages = 0;
        int averageCounter = 0;
        ArrayList<WGI> averageWGIs = new ArrayList<>();

        /*
         * Will need to revisit average total array if WGs per community increase
         */

        averageReset();

        reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20);

        populateFirstThreeColumns(isheet, 1);

        reportWB.getSheet(isheet).setValue(3, 1, "Disposable Income As Reported", boldTopStyle);
        reportWB.getSheet(isheet).setValue(4, 1, "DI After Change Scenario -", boldTopStyle);

        if (isCopingStrategy) {
            expandabilityHeadings(isheet, 1, col);
        }

        // NOTE OHEA ordered by wg Number order not DI

        int x = 1;
        for (WGI community : uniqueCommunity) {

            // Need all 3 WGOrders

            /* row is increment in call to function if data exists for the WGORDER */
            /* If not is incremented here */

            for (int k = 0; k < 3; k++) {

                if (community.getCommunity().getCountWGinCommunity()[k] > 0) {
                    row = calcAvgDI(isheet, 3, 5, row, community, k + 1, type, false); // start col = 3 expandability
                    // col = // 5
                } else {
                    row++;
                }

            }
        }

    }

    /******************************************************************************************************************************************/
    /*
     * calculate the average DI and DI After Change Scenario and return row++ if
     * wgorder existed
     */
    private int calcAvgDI(int isheet, int col, int expcol, int row, WGI community, int wgOrder, String type,
                          boolean isSTOL) {

        OptionalDouble averageWGDIAfterChangeScenario = null;
        OptionalDouble averageWGDI = null;
        Double prevWGAfterChangeScenario = 0.0;
        double cashGain = 0.0;
        double prevCashGain = 0.0;
        Double totalCashGain = 0.0;
        double hhSize = 0.0;
        // double hhSize = 0.0;
        // double hhSize = 0.0;
        double wgSOLInclusion = 0.0;
        double wgSOLSurvival = 0.0;
        int numberOfWealthgroups = 0;

        Predicate<WGI> isWGOrder = e -> e.getWealthgroup().getWgorder() == wgOrder;
        WGI community2 = community;
        Predicate<WGI> isComm = e -> e.getCommunity() == community2.getCommunity();

        List<WGI> collect2 = uniqueWealthgroupInterview.stream().filter(isComm.and(isWGOrder))
                .collect(Collectors.toList());
        // get distinct wg for wgdi

        if (collect2.stream().findAny().isPresent()) {

            numberOfWealthgroups = (int) collect2.stream().filter(distinctByKey(WGI::getWealthgroup)).count();

            averageWGDI = collect2.stream().mapToDouble(p -> p.getWgiDI()).average();
            averageWGDIAfterChangeScenario = collect2.stream().mapToDouble(p -> p.getWgiDIAfterChangeScenario())
                    .average();
            if (averageWGDI.isPresent()) {
                this.reportWB.getSheet(isheet).setValue(col, row, averageWGDI.getAsDouble(), numberStyle);
                this.reportWB.getSheet(isheet).setValue(++col, row, averageWGDIAfterChangeScenario.getAsDouble(),
                        numberStyle);

                if (isSTOL) {

                    /* STOL is at Community level, thus same for all wgs in a community */
                    /* Calc SOL Inclusion and Survival */

                    if (community2.getCommunity().getStdOfLivingElement().stream().findAny().isPresent()) {

                        hhSize = community2.getWealthgroupInterview().getWgAverageNumberInHH();

                        for (WGI wgistol : collect2.stream().distinct().filter(isComm.and(isWGOrder))
                                .collect(Collectors.toList())) {

                            for (StdOfLivingElement stdOfLivingElement : wgistol.getWealthgroup().getCommunity()
                                    .getStdOfLivingElement()) {

                                if (stdOfLivingElement.getLevel() == StdLevel.Household) {
                                    wgSOLInclusion += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount();
                                    wgSOLSurvival += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
                                            * stdOfLivingElement.getSurvival() / 100;
                                } else if (stdOfLivingElement.getLevel() == StdLevel.HouseholdMember) {
                                    wgSOLInclusion += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
                                            * hhSize;
                                    wgSOLSurvival += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
                                            * (stdOfLivingElement.getSurvival() / 100.0) * hhSize;

                                }

                            }
                            /* calc averages */

                        }
                    }
                    /* Moved here */
                    wgSOLInclusion = wgSOLInclusion / numberOfWealthgroups;
                    wgSOLSurvival = wgSOLSurvival / numberOfWealthgroups;

                    reportWB.getSheet(isheet).setValue(3, row, wgSOLInclusion, numberStyle);
                    reportWB.getSheet(isheet).setValue(4, row, wgSOLSurvival, numberStyle);
                    // reportWB.getSheet(isheet).setValue(5, row, wgi2.getWgiDI(), numberd2);
                    // reportWB.getSheet(isheet).setValue(6, row,
                    // wgi2.getWgiDIAfterChangeScenario(), numberd2);
                    reportWB.getSheet(isheet).setValue(7, row, averageWGDI.getAsDouble() - wgSOLInclusion, numberStyle);
                    reportWB.getSheet(isheet).setValue(8, row,
                            averageWGDIAfterChangeScenario.getAsDouble() - wgSOLInclusion, numberStyle);

                }

            } else {
                return (row);
            }

            if (isCopingStrategy) {

                prevWGAfterChangeScenario = averageWGDIAfterChangeScenario.getAsDouble() - wgSOLInclusion;
                /* get all wgs for this wgorder in a list and get expandability */

                System.out.println("before collectwg");
                List<WGI> collectWGs = wgi.stream().filter(isComm.and(isWGOrder)).collect(Collectors.toList());
                System.out.println("after collectwg ");

                for (ExpandabilityRule expandabilityRule : expandabilityRules) {
                    System.out.println("after collectwg in exp rule ");
                    prevCashGain = cashGain;

                    for (int k = 0; k < collectWGs.size(); k++) {
                        System.out.println("about to do calcgain " + collectWGs.get(k).getSubDistrict());
                        cashGain += expandabilityGain(expandabilityRule, collectWGs.get(k).getWealthgroupInterview(),
                                type);
                        System.out.println("done calcgain");
                    }

                    /* Calc average */
                    if (collectWGs.size() == 0) {
                        return (row);
                    }
                    cashGain = cashGain / collectWGs.size();

                    totalCashGain = prevCashGain + cashGain;

                    reportWB.getSheet(isheet).setValue(expcol, row, prevWGAfterChangeScenario + totalCashGain,
                            numberStyle);
                    prevWGAfterChangeScenario += totalCashGain;

                    // }
                    expcol++;

                    System.out.println("after collect end loop  ");

                }

            }

            row++;
        }
        return (row);

    }

    /******************************************************************************************************************************************/
    private Double expandabilityGain(ExpandabilityRule expandabilityRule, Household hh, String type) { // Type = food or
        // cash
        Double gain = 0.0;

        // Handle synonyms
        ResourceSubType expRST;
        if (expandabilityRule.getAppliedResourceSubType().getResourcesubtypesynonym() != null) {
            expRST = expandabilityRule.getAppliedResourceSubType().getResourcesubtypesynonym();
        } else {
            expRST = expandabilityRule.getAppliedResourceSubType();
        }

        List<Crop> cropCollection = hh.getCrop().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        List<WildFood> wfCollection = hh.getWildFood().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        /* #528 Need to include Transfers */

        /* #532 Food RST will not exist if there is not a Food Transfer Type */
        /*
         * orig List<Transfer> transCollection = hh.getTransfer().stream() .filter(p ->
         * p.getFoodResourceSubType() == expRST ||
         * p.getFoodResourceSubType().getResourcesubtypesynonym() == expRST)
         * .collect(Collectors.toList());
         */
        List<Transfer> transCollection = new ArrayList<>();

        transCollection = hh.getTransfer().stream().filter(p -> p.getFoodResourceSubType() != null)
                .filter(p -> p.getFoodResourceSubType() == expRST
                        || p.getFoodResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        /*
         * #527
         *
         * No need to check synonym for LSS - no KCal values
         *
         *
         * List<LivestockSales> lssCollection = hh.getLivestockSales().stream().filter(
         * p -> p.getResourceSubType() == expRST ||
         * p.getResourceSubType().getResourcesubtypesynonym() == expRST)
         * .collect(Collectors.toList());
         */

        List<LivestockSales> lssCollection = hh.getLivestockSales().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        List<LivestockProducts> lspCollection = hh.getLivestockProducts().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        gain = calcGain(expandabilityRule, cropCollection, wfCollection, lssCollection, lspCollection, transCollection,
                type);

        return gain;
    }

    /******************************************************************************************************************************************/

    private Double expandabilityGain(ExpandabilityRule expandabilityRule, WealthGroupInterview wgilocal, String type) { // Type
        // =
        // food
        // or
        // cash
        Double gain = 0.0;

        // Handle synonyms
        ResourceSubType expRST;

        if (expandabilityRule.getAppliedResourceSubType().getResourcesubtypesynonym() != null) {
            expRST = expandabilityRule.getAppliedResourceSubType().getResourcesubtypesynonym();
        } else {
            expRST = expandabilityRule.getAppliedResourceSubType();
        }

        List<Crop> cropCollection = wgilocal.getCrop().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        List<WildFood> wfCollection = wgilocal.getWildFood().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        List<LivestockSales> lssCollection = wgilocal.getLivestockSales().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        List<LivestockProducts> lspCollection = wgilocal.getLivestockProducts().stream().filter(
                p -> p.getResourceSubType() == expRST || p.getResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        // No need to process Transfer Type = Cash
        Stream<Transfer> transtypefood = wgilocal.getTransfer().stream()
                .filter(p -> p.getTransferType() == TransferType.Food);

        // List<Transfer> transCollection = wgilocal.getTransfer().stream()
        List<Transfer> transCollection = transtypefood
                .filter(p -> p.getFoodResourceSubType() == expRST
                        || p.getFoodResourceSubType().getResourcesubtypesynonym() == expRST)
                .collect(Collectors.toList());

        gain = calcGain(expandabilityRule, cropCollection, wfCollection, lssCollection, lspCollection, transCollection,
                type);

        return gain;
    }

    /******************************************************************************************************************************************/
    /*
     * Create columns headings for each expandability rule
     */
    private void expandabilityHeadings(int isheet, int startRow, int startCol) {
        /* Expandability Headings */
        for (ExpandabilityRule expandabilityRule : expandabilityRules) {
            reportWB.getSheet(isheet).setColumnWidths(startCol, 20);
            reportWB.getSheet(isheet).setValue(startCol++, startRow,
                    "Income After Change Scenario and " + expandabilityRule.getRuleName(), boldTopStyle);

        }
    }

    /******************************************************************************************************************************************/
    private Double calcGain(ExpandabilityRule expandabilityRule, List<Crop> cropCollection, List<WildFood> wfCollection,
                            List<LivestockSales> lssCollection, List<LivestockProducts> lspCollection, List<Transfer> transCollection,
                            String type) {

        // type is food or cash
        Double priceChange;
        Double yieldChange;
        Double gain = 0.0;
        ResourceSubType rst;

        /*
         * Handle if Food Substitution has been added to Model Scenario Aug 2020
         */

        if (cropCollection.size() > 0) {

            for (Crop crop : cropCollection) {
                rst = crop.getResourceSubType();
                /*
                 * if (isFoodSubstitution && isOHEA) { rst =
                 * calcRSTforFoodSubstitution(crop.getResourceSubType()); } else { rst =
                 * crop.getResourceSubType(); }
                 */

                priceChange = priceYieldVariation(modellingScenario, rst, PRICE);
                yieldChange = priceYieldVariation(modellingScenario, rst, YIELD);


                if (type == "cash") {

                    gain += ((crop.getUnitsProduced() * yieldChange)
                            - (crop.getUnitsSold() * yieldChange)) * (crop.getPricePerUnit() * priceChange);


                } else if (type == "food") {
                    gain += ((crop.getUnitsProduced() * yieldChange) - (crop.getUnitsSold() * yieldChange))
                            * findRSTKcal(crop.getResourceSubType());
                }
                System.out.println("crop exp gain =   = " + gain);
            }
        } else if (wfCollection.size() > 0) {

            for (WildFood wf : wfCollection) {

                priceChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), PRICE);
                yieldChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), YIELD);

                if (type == "cash") {
                    gain += ((wf.getUnitsProduced() * yieldChange) - (wf.getUnitsSold() * yieldChange))
                            * (wf.getPricePerUnit() * priceChange);
                } else if (type == "food") {
                    gain += ((wf.getUnitsProduced() * yieldChange) - (wf.getUnitsSold() * yieldChange))
                            * findRSTKcal(wf.getResourceSubType());
                }

            }
        } else if (lspCollection.size() > 0) {

            for (LivestockProducts lsp : lspCollection) {

                priceChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), PRICE);
                yieldChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), YIELD);

                if (type == "cash") {
                    gain += ((lsp.getUnitsProduced() * yieldChange) - (lsp.getUnitsSold() * yieldChange))
                            * lsp.getPricePerUnit() * priceChange;
                } else if (type == "food") {
                    gain += ((lsp.getUnitsProduced() * yieldChange) - (lsp.getUnitsSold() * yieldChange))
                            * findRSTKcal(lsp.getResourceSubType());
                }

            }
        } else if (transCollection.size() > 0) {
            System.out.println("in transcollection ");
            for (Transfer trans : transCollection) {


                if (trans.getTransferType() == TransferType.Food) {
                    priceChange = priceYieldVariation(modellingScenario, trans.getFoodResourceSubType(), PRICE);
                } else {
                    priceChange = 1.0;
                }
                yieldChange = 1.0; // priceYieldVariation(modellingScenario, lsp.getResourceSubType(), YIELD);

                if (type == "cash") {

					/* #566
					gain += ((trans.getUnitsTransferred() * yieldChange) - (trans.getUnitsSold() * priceChange))
							* trans.getPricePerUnit() * priceChange;
							* */
                    gain += ((trans.getUnitsTransferred() * yieldChange) - (trans.getUnitsSold() * yieldChange))
                            * trans.getPricePerUnit() * priceChange;

                } else if (type == "food") {
                    // no change in yield see #528
                }

            }
        } else if (lssCollection.size() > 0) {

            for (LivestockSales lss : lssCollection) {

                if (type == "cash") {

                    Double expandabilityLimit = expandabilityRule.getExpandabilityLimit() / 100.0;
                    Double expandabilityIncreaseLimit = expandabilityRule.getExpandabilityIncreaseLimit() / 100.0;
                    Double newSold = Math.min(lss.getUnitsSold() * expandabilityIncreaseLimit,
                            lss.getUnitsAtStartofYear() * expandabilityLimit);

                    priceChange = priceYieldVariation(modellingScenario, lss.getResourceSubType(), PRICE);

                    // But already sold some so

                    newSold -= lss.getUnitsSold();

                    if (newSold < 0) {
                        gain = 0.0;
                    } else {

                        newSold = Math.floor(newSold); // round down to a whole animal sale
                        gain += (newSold * lss.getPricePerUnit() * priceChange);

                    }

                }

            }
        }

        return gain;
    }

    /******************************************************************************************************************************************/
    private void averageReset() {

        int l = 0;
        for (int i = 0; i < 3; i++) {

            for (l = 0; l < NUMBEROFAVERAGES; l++) {
                averageTotal[i][l] = 0.0;
            }

        }
    }

    /******************************************************************************************************************************************/

    private void createOHEADIAfterSOLreport(int isheet, Report report, Boolean isCopingStrategy) {
        int row = 1;
        int col = 0;
        int startRow = 1;
        int i = 0;
        double hhSOLC = 0.0;
        double hhSize = 0.0;
        double wgSOLInclusion = 0.0;
        double wgSOLSurvival = 0.0;
        Double cashGain = 0.0;
        Double prevCashGain = 0.0;
        Double totalCashGain = 0.0;
        String type = "cash";

        reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20);

        if (isCopingStrategy) {
            expandabilityHeadings(isheet, row, 9);
        }

        populateFirstThreeColumns(isheet, row);

        reportWB.getSheet(isheet).setValue(3, row, "Std of Living Reqt - Inclusion", boldTopStyle); // B
        reportWB.getSheet(isheet).setValue(4, row, "Std of Living Reqt - Survival", boldTopStyle); // B
        reportWB.getSheet(isheet).setValue(5, row, "Disposable Income As Reported", boldTopStyle); // C
        reportWB.getSheet(isheet).setValue(6, row, "DI After Change Scenario", boldTopStyle); // D
        reportWB.getSheet(isheet).setValue(7, row, "DI With StoL as Reported", boldTopStyle); // E
        reportWB.getSheet(isheet).setValue(8, row, "DI With StoL as Reported After Change Scenario", boldTopStyle); // F

        row = 2;

        for (WGI community : uniqueCommunity) {

            // Need all 3 WGOrders

            /* row is increment in call to function if data exists for the WGORDER */
            /* STOL in last parameter true/false */

            // row = calcAvgDI(isheet, 5, 9, row, community, 1, type, true);

            // row = calcAvgDI(isheet, 5, 9, row, community, 2, type, true);

            // row = calcAvgDI(isheet, 5, 9, row, community, 3, type, true);

            /* row is increment in call to function if data exists for the WGORDER */
            /* If not is incremented here */

            for (int k = 0; k < 3; k++) {

                if (community.getCommunity().getCountWGinCommunity()[k] > 0) {
                    row = calcAvgDI(isheet, 5, 9, row, community, k + 1, type, true); // start col = 3 expandability col
                    // = // 5
                } else {
                    row++;
                }

            }

        }

    }

    /******************************************************************************************************************************************/

    private void createOIHMDIAfterSOLreport(int isheet, Report report, Boolean isCopingStrategy) {
        int row = 1;
        int col = 0;
        Double hhSOLC = 0.0;
        Double cashGain = 0.0;
        Double prevCashGain = 0.0;
        Double totalCashGain = 0.0;
        String type = "cash";

        Efdutils.em("In OIHM DISOL report");

        // uniqeHousehold = number of households in array that are relevant
        reportWB.getSheet(isheet).setColumnWidths(1, 17, 17, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28);

        reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);
        reportWB.getSheet(isheet).setValue(1, row, "Household Number", boldTopStyle);
        reportWB.getSheet(isheet).setValue(2, row, "Std of Living Reqt", boldTopStyle); // B
        reportWB.getSheet(isheet).setValue(3, row, "Disposable Income As Reported", boldTopStyle); // C
        reportWB.getSheet(isheet).setValue(4, row, "DI After Change Scenario", boldTopStyle); // D
        reportWB.getSheet(isheet).setValue(5, row, "DI With StoL as Reported", boldTopStyle); // E
        reportWB.getSheet(isheet).setValue(6, row, "DI With StoL as Reported After Change Scenario", boldTopStyle); // F

        if (isCopingStrategy) {
            expandabilityHeadings(isheet, row, 7);
        }

        row++;

        for (HH hh2 : uniqueHousehold) {

            reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), numberd2);
            reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), numberd2); // B
            reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDI(), numberd2); // C
            reportWB.getSheet(isheet).setValue(4, row, hh2.getHhDIAfterChangeScenario(), numberd2); // D
            reportWB.getSheet(isheet).setValue(5, row, hh2.getHhDI() - hh2.getHhSOLC(), numberd2); // C - B
            reportWB.getSheet(isheet).setValue(6, row, hh2.getHhDIAfterChangeScenario() - hh2.getHhSOLC(), numberd2); // D
            // -
            if (isCopingStrategy) {
                col = 7;
                cashGain = 0.0;
                prevCashGain = 0.0;
                Double prevHHOSolAfterChangeScenario = hh2.getHhDIAfterChangeScenario() - hh2.getHhSOLC();
                for (ExpandabilityRule expandabilityRule : expandabilityRules) {

                    // #527 - print all changes not just until +ve
                    // if (prevHHOSolAfterChangeScenario > 0)

                    // If DI value after change scenario is > 0 then use DI after change scenario,
                    // otherwise apply expandability rule if applicaable

                    // {

                    // reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario,
                    // numberd2);
                    // } else {

                    System.out.println("Exp Rule = " + expandabilityRule.getAppliedResourceSubType().getResourcetypename());
                    prevCashGain = cashGain;
                    cashGain = expandabilityGain(expandabilityRule, hh2.getHousehold(), type);
                    totalCashGain = prevCashGain + cashGain;

                    /*
                     * #515
                     *
                     * Why subtracting hh2.getHousehold().getExpandabilityRule().
                     * getExpandabilityCostOfDeficitPurchase()
                     *
                     *
                     * reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario +
                     * totalCashGain - hh2.getHousehold().getExpandabilityRule().
                     * getExpandabilityCostOfDeficitPurchase(), numberd2);
                     */
                    /*
                     * #527 change totalGain to cashGain
                     */
                    // reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario +
                    // totalCashGain,
                    // numberd2);

                    reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario + cashGain, numberd2);

                    // reportWB.getSheet(isheet).setValue(col, row, prevHHOSolAfterChangeScenario +
                    // totalCashGain
                    // -
                    // hh2.getHousehold().getExpandabilityRule().getExpandabilityCostOfDeficitPurchase(),
                    // numberd2);
                    // #527
                    // prevHHOSolAfterChangeScenario += totalCashGain;
                    prevHHOSolAfterChangeScenario += cashGain;
                    // }

                    col++;
                }

            }

            row++;
        }

    }

    private void calcHHSOL() {
        Double hhSOLC;
        for (HH hh3 : uniqueHousehold) {

            hhSOLC = 0.0;
            for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {

                if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {

                    hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());

                } else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {

                    for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {

                        hhSOLC += calcHhmSolc(householdMember, stdOfLivingElement);

                        errno = 105;
                    }

                }

            }

            hh3.setHhSOLC(hhSOLC);
            hh3.getHousehold().setSOLC(hhSOLC);

        }
    }

    /******************************************************************************************************************************************/

    private void createOHEANutrientreport(int isheet, Report report, Boolean isCopingStrategy) {

        int col = 4;
        int row = 1;
        int kcalrow = 2;
        ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();
        int[] wgCounter = {0, 0, 0};
        int nrow = 2;
        double totalFoodubstitutionNutrient = 0.0;

        if (isCopingStrategy) {
            /* Coping Strategy not supported with Nutrients at the moment Aug 2020 */
            return;
        }

        nutrients.sort(Comparator.comparing(MicroNutrient::getName));

        reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);
        populateFirstThreeColumns(isheet, 1);

        reportWB.getSheet(isheet).setValue(3, 1, "KCal Requirement", boldTopStyle);

        printNutrientHeader(isheet, col, row);

        for (WGI wgi2 : uniqueCommunity) {

            for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
                    .filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
                listWealthgroup.add(wgi4.getWealthgroup());
            }

            /* add DDI nutrients to nutrients array */
            defaultDietItems = (List<DefaultDietItem>) wgi2.getCommunity().getDefaultDietItem();

            for (DefaultDietItem defaultDietItem : defaultDietItems) {
                /* how many calories needed of this RST */
                double calsNeeded = defaultDietItem.getCalsNeededofThisDDI();
                double rstKCAL = defaultDietItem.getResourcesubtype().getResourcesubtypekcal();

                double kgNeeded = calsNeeded / rstKCAL;

                /* Kgs needed getcals/rst kcal */

                if (defaultDietItem.getResourcesubtype().getMccwFoodSource() != null) {

                    for (MicroNutrientLevel mnl : defaultDietItem.getResourcesubtype().getMccwFoodSource()
                            .getMicroNutrientLevel()) {
                        /* Kgs * mn level in this RST */

                        /* switch from negative to add amount made up in DDI */
                        double mnLevel = NumberUtils.toDouble(mnl.getMnLevel()) * kgNeeded * -1.0;
                        errno = 4373_4;

                        /* Same for all WG as DDI is property of Community */
                        for (int k = 1; k < 4; k++) {

                            /* Note that wealthgroup and avgnumberinH not set in the NutrientCount array */

                            NutrientCount thismn = new NutrientCount();
                            thismn.setMn(mnl.getMicroNutrient());
                            thismn.setMnAmount(mnLevel);

                            thismn.setWgOrder(k);

                            thismn.setDDI(true);

                            overallNutrientCount.add(thismn);
                            totalNutrientCount.add(thismn);
                            totalNutrientCountFoodSubstitution.add(thismn);// TODO - What happens if DDI is substituted
                            // ?
                        }

                    }

                }
            }

            /* find any wgs and group by wgorder and get avg */
            errno = 4374;
            col = 3;

            wgCounter[0] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 1).count();
            wgCounter[1] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 2).count();
            wgCounter[2] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 3).count();

            List<WealthGroup> wg1 = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
                    .collect(Collectors.toList());
            List<WealthGroup> wg2 = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
                    .collect(Collectors.toList());
            List<WealthGroup> wg3 = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
                    .collect(Collectors.toList());

            /* Note that this calc prints hhSize on spreadsheet */

            calcWGNutrients(isheet, col, kcalrow++, wg1, 1);
            calcWGNutrients(isheet, col, kcalrow++, wg2, 2);
            calcWGNutrients(isheet, col, kcalrow++, wg3, 3);

            /* print nutrients array for LZ and WGs */
            /* let convert all to kgs as that is the consumption unit */

            for (int k = 0; k < 3; k++) {
                if (wgi2.getCommunity().getCountWGinCommunity()[k] == 0) {
                    /* No data for this WGOrder */
                    nrow++;
                    continue;
                }

                /* first print kcal average */
                double hh = calcAvgWGHH(k + 1).orElse(0.0);

                double kcal = RC * hh;
                // reportWB.getSheet(isheet).setValue(4, kcalrow++, kcal, numberStyle);

                for (int j = 0; j < nutrients.size(); j++) {

                    if (wgCounter[k] == 0) {
                        break;
                    }

                    MicroNutrient microNutrient = nutrients.get(j);

                    double totalNutrient = calcWGNutrientFromArray(microNutrient, k + 1, false, totalNutrientCount);

                    totalFoodubstitutionNutrient = calcWGNutrientFromArray(microNutrient, k + 1, false,
                            totalNutrientCountFoodSubstitution);

                    /* totalNutrient is in KGs * RDA unit */

                    /* Data bug avgHHsize could be 0 - 0 is no longer allowed */

                    if (hhSize[k] == 0.0) {
                        hhSize[k] = 1;
                    }

                    double mnYearRDA = microNutrient.getRda() * hhSize[k] * 365;

                    mnYearRDA = calcMNYearRDA(microNutrient, mnYearRDA);

                    // divide by wgCounter to get average - normally will be 1.

                    // daily RDA held in Nutrients table

                    Double nutrientPerCent = 100 * totalNutrient / mnYearRDA;

                    reportWB.getSheet(isheet).setValue(4 + (2 * j), nrow,
                            100 * (totalNutrient / mnYearRDA) / wgCounter[k], numberStyle);
                    reportWB.getSheet(isheet).setValue(5 + (2 * j), nrow,
                            100 * (totalFoodubstitutionNutrient / mnYearRDA) / wgCounter[k], numberStyle);
                }
                nrow++;
            }

            errno = 4375;

            totalNutrientCount.clear();
            totalNutrientCountFoodSubstitution.clear();
            listWealthgroup.clear();
        }

    }

    private void printNutrientHeader(int isheet, int col, int row) {
        for (int j = 0; j < nutrients.size(); j++) {
            reportWB.getSheet(isheet).setColumnWidths(col, 20);
            reportWB.getSheet(isheet).setValue(col++, row, nutrients.get(j).getName(), boldTopStyle);
            reportWB.getSheet(isheet).setColumnWidths(col, 20);
            reportWB.getSheet(isheet).setValue(col++, row, nutrients.get(j).getName() + " After Food Change Scenario",
                    boldTopStyle);
        }
    }

    /******************************************************************************************************************************************/

    private double calcMNYearRDA(MicroNutrient microNutrient, double mnYearRDA) {
        if (microNutrient.getRdaUnit() == "�g") {

            mnYearRDA = mnYearRDA / 1000000000; // in kgs
        } else if (microNutrient.getRdaUnit() == "mg") {

            mnYearRDA = mnYearRDA / 1000000; // in kgs
        } else if (microNutrient.getRdaUnit() == "g") {

            mnYearRDA = mnYearRDA / 1000; // in kgs
        }
        return mnYearRDA;
    }

    /******************************************************************************************************************************************/
    /* For totalNutrient array */
    private double calcWGNutrientFromArray(MicroNutrient microNutrient, int wgOrder, boolean isWithFoodSub,
                                           ArrayList<NutrientCount> thisArray) {
        double totalNutrient = thisArray.stream().filter(p -> p.getWgOrder() == wgOrder)
                .filter(p -> p.getMn() == microNutrient).mapToDouble(p -> p.getMnAmount()).sum();

        return totalNutrient;
    }

    /******************************************************************************************************************************************/

    /* For totalNutrient array */
    private double calcHHNutrientFromArray(MicroNutrient microNutrient, int hhNumber, boolean isWithFoodSub,
                                           ArrayList<NutrientCount> thisArray) {
        double totalNutrient = thisArray.stream().filter(p -> p.getHhNumber() == hhNumber)
                .filter(p -> p.getMn() == microNutrient).mapToDouble(p -> p.getMnAmount()).sum();

        return totalNutrient;
    }

    /******************************************************************************************************************************************/

    private void calcWGNutrients(int isheet, int col, int row, List<WealthGroup> wg, int wgOrder) {

        int numberOfWGs = wg.size();
        double houseavgsize = 0.0;
        MCCWFoodSource thisMccwFoodSource;
        errno = 45000;

        if (wg.stream().filter(p -> p.getWgorder() == wgOrder).findAny().isPresent()) {

            for (int j = 0; j < wg.size(); j++) {

                for (WealthGroupInterview wgi6 : wg.get(j).getWealthGroupInterview()) {
                    houseavgsize += wgi6.getWgAverageNumberInHH();

                }
            }
            hhSize[wgOrder - 1] = houseavgsize / wg.size();

            reportWB.getSheet(isheet).setValue(col++, row, hhSize[wgOrder - 1] * RC, numberStyle);

            /*
             * Calc KCALS Consumed by crops/lsp/emp/transfer/wf for each wgi Then need to
             * average across equal wgorders
             *
             * Add to array nutrientCount
             *
             * Aug 2020 Nutrients need to use RST Synonym
             *
             */
            errno = 45010;

            ResourceSubType substitutionFoodrst = null;

            for (int k = 0; k < wg.size(); k++) {
                for (WealthGroupInterview wgimnut : wg.get(k).getWealthGroupInterview()) {

                    for (Crop crop2 : wgimnut.getCrop()) {

                        calcSubFood(wgOrder, hhSize[wgOrder - 1], crop2.getResourceSubType(), crop2.getUnitsConsumed());

                        thisMccwFoodSource = getMccWincludeSyn(crop2.getResourceSubType());

                        /*
                         * if (thisMccwFoodSource != null) {
                         *
                         * for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                         * populateNutrientSource(wgOrder, hhSize[wgOrder - 1],
                         * crop2.getUnitsConsumed(), mn, false); } }
                         */

                    }
                    errno = 45020;
                    for (LivestockProducts lsp : wgimnut.getLivestockProducts()) {
                        calcSubFood(wgOrder, hhSize[wgOrder - 1], lsp.getResourceSubType(), lsp.getUnitsConsumed());

                        thisMccwFoodSource = getMccWincludeSyn(lsp.getResourceSubType());

                        // if (thisMccwFoodSource != null) {

                        // for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {

                        // populateNutrientSource(wgOrder, hhSize[wgOrder - 1], lsp.getUnitsConsumed(),
                        // mn, false);

                        // }
                        // }
                    }
                    errno = 45030;
                    for (Transfer tr : wgimnut.getTransfer()) {

                        calcSubFood(wgOrder, hhSize[wgOrder - 1], tr.getResourceSubType(), tr.getUnitsConsumed());

                        thisMccwFoodSource = getMccWincludeSyn(tr.getResourceSubType());

                        // if (thisMccwFoodSource != null) {

                        // for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                        // populateNutrientSource(wgOrder, hhSize[wgOrder - 1], tr.getUnitsConsumed(),
                        // mn, false);

//							}
                        // }
                    }
                    errno = 45040;
                    for (Employment emp : wgimnut.getEmployment()) {
                        calcSubFood(wgOrder, hhSize[wgOrder - 1], emp.getResourceSubType(),
                                emp.getFoodPaymentUnitsPaidWork());

                        thisMccwFoodSource = getMccWincludeSyn(emp.getResourceSubType());

                        // if (thisMccwFoodSource != null) {

//							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                        // populateNutrientSource(wgOrder, hhSize[wgOrder - 1],
                        // emp.getPeopleCount() * emp.getFoodPaymentUnitsPaidWork(), mn, false);

                        // }
                        // }
                    }
                    errno = 45050;
                    for (WildFood wf : wgimnut.getWildFood()) {
                        calcSubFood(wgOrder, hhSize[wgOrder - 1], wf.getResourceSubType(), wf.getUnitsConsumed());

                        thisMccwFoodSource = getMccWincludeSyn(wf.getResourceSubType());

                        // if (thisMccwFoodSource != null) {

                        // for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                        // populateNutrientSource(wgOrder, hhSize[wgOrder - 1], wf.getUnitsConsumed(),
                        // mn, false);

                        // }
                        // }
                    }

                }

            }

        }

        /* need to work out average if more than one WG in this wgorder */

        /* Now to consolidate nutrients by WG */

        ArrayList<NutrientCount> dummyWGCount = (ArrayList<NutrientCount>) totalNutrientCount.clone();
        ArrayList<NutrientCount> dummyWGCountSubstitution = (ArrayList<NutrientCount>) totalNutrientCountFoodSubstitution
                .clone();
        /* Dont forget number in wgorder */

        /* Do not delete the DDI components marked as isDDI true */

        Predicate<NutrientCount> equalWGOrder = i -> i.getWgOrder() == wgOrder;
        Predicate<NutrientCount> isnotDDI = p -> p.isDDI() == false;

        // totalWGNutrientCount.removeIf(p -> p.getWgOrder() == wgOrder);

        totalNutrientCount.removeIf(equalWGOrder.and(isnotDDI));
        totalNutrientCountFoodSubstitution.removeIf(equalWGOrder.and(isnotDDI));

        for (int j = 0; j < nutrients.size(); j++) {
            MicroNutrient mn = nutrients.get(j);
            double sumOfMn = dummyWGCount.stream().filter(p -> p.getWgOrder() == wgOrder).filter(p -> p.getMn() == mn)
                    .mapToDouble(p -> p.getMnAmount()).sum();
            double sumOfMnSubst = dummyWGCountSubstitution.stream().filter(p -> p.getWgOrder() == wgOrder)
                    .filter(p -> p.getMn() == mn).mapToDouble(p -> p.getMnAmount()).sum();
            if (sumOfMn != 0.0) {
                NutrientCount thismn = new NutrientCount();
                thismn.setMn(mn);
                thismn.setWgOrder(wgOrder);

                // thismn.setMnAmount(sumOfMn / wg.size()); // AVerage is set here for all
                // nutrients in array for a
                thismn.setMnAmount(sumOfMn); // WG
                thismn.setAverageNumberInHousehold(hhSize[3 - 1]);
                totalNutrientCount.add(thismn);
            }
            if (sumOfMnSubst != 0.0) {
                NutrientCount thismn = new NutrientCount();
                thismn.setMn(mn);
                thismn.setWgOrder(wgOrder);

                // thismn.setMnAmount(sumOfMn / wg.size()); // AVerage is set here for all
                // nutrients in array for a
                thismn.setMnAmount(sumOfMnSubst); // WG
                thismn.setAverageNumberInHousehold(hhSize[3 - 1]);
                totalNutrientCountFoodSubstitution.add(thismn);
            }
        }

        dummyWGCount.clear();
        dummyWGCountSubstitution.clear();

    }

    /******************************************************************************************************************************************/

    private void calcSubFood(int wgOrder, double hhSize, ResourceSubType rst, double unitsConsumed) {
        MCCWFoodSource thisMccwFoodSource;
        ResourceSubType substitutionFoodrst;

        // Add Substitute Food to TotalNutrientCountSubFood and then add Current Food to
        // TotalNutrientCount

        if (isFoodSubstitution && modellingScenario.getFoodSubstitution().stream()
                .filter(p -> p.getCurrentFood() == rst).findAny().isPresent()) {
            /* substitute the crop */
            List<FoodSubstitution> cropSubstitutionList = modellingScenario.getFoodSubstitution().stream()
                    .filter(p -> p.getCurrentFood() == rst).collect(Collectors.toList());
            substitutionFoodrst = cropSubstitutionList.get(0).getSubstitutionFood();

            thisMccwFoodSource = getMccWincludeSyn(substitutionFoodrst);

            if (thisMccwFoodSource != null) {

                for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                    populateNutrientSource(wgOrder, hhSize, unitsConsumed, mn, true);
                }
            }
        } else { // add Non Food sub rst to totalNutrientFoodCountSubstitution array with non
            // substituted Food
            thisMccwFoodSource = getMccWincludeSyn(rst);

            if (thisMccwFoodSource != null) {

                for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                    populateNutrientSource(wgOrder, hhSize, unitsConsumed, mn, true);
                }
            }
        }

        // and add to TotalNutrintCount without Substitution

        thisMccwFoodSource = getMccWincludeSyn(rst);

        if (thisMccwFoodSource != null) {

            for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                populateNutrientSource(wgOrder, hhSize, unitsConsumed, mn, false);
            }
        }

    }

    /******************************************************************************************************************************************/

    private void calcHHNutrients(int isheet, int col, int row, List<HH> hh) {

        int numberOfHHs = hh.size();
        double houseavgsize = 0.0;
        MCCWFoodSource thisMccwFoodSource;

        ResourceSubType substitutionFoodrst = null;

        for (int k = 0; k < hh.size(); k++) {

            int hhNumber = hh.get(k).getHhNumber();
            double sizeHH = hh.get(k).getHousehold().getHouseholdMember().size();
            HH hh2 = hh.get(k);

            for (Crop crop2 : hh2.getHousehold().getCrop()) {

                calcSubFood(hhNumber, sizeHH, crop2.getResourceSubType(), crop2.getUnitsConsumed());

            }

            for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {
                calcSubFood(hhNumber, sizeHH, lsp.getResourceSubType(), lsp.getUnitsConsumed());
                thisMccwFoodSource = getMccWincludeSyn(lsp.getResourceSubType());
                populateNutrientArray(thisMccwFoodSource, hhNumber, sizeHH, lsp.getUnitsConsumed());
            }

            for (Transfer tr : hh2.getHousehold().getTransfer()) {

                calcSubFood(hhNumber, sizeHH, tr.getResourceSubType(), tr.getUnitsConsumed());

                thisMccwFoodSource = getMccWincludeSyn(tr.getResourceSubType());

                // populateNutrientArray(thisMccwFoodSource, hhNumber, sizeHH,
                // tr.getUnitsConsumed());
            }

            for (Employment emp : hh2.getHousehold().getEmployment()) {
                calcSubFood(hhNumber, sizeHH, emp.getResourceSubType(), emp.getFoodPaymentUnitsPaidWork());

                thisMccwFoodSource = getMccWincludeSyn(emp.getResourceSubType());

                // populateNutrientArray(thisMccwFoodSource, hhNumber, sizeHH,
                // emp.getPeopleCount() * emp.getFoodPaymentUnitsPaidWork());
            }

            for (WildFood wf : hh2.getHousehold().getWildFood()) {
                calcSubFood(hhNumber, sizeHH, wf.getResourceSubType(), wf.getUnitsConsumed());

                thisMccwFoodSource = getMccWincludeSyn(wf.getResourceSubType());

                // populateNutrientArray(thisMccwFoodSource, hhNumber, sizeHH,
                // wf.getUnitsConsumed());
            }

        }

    }

    /******************************************************************************************************************************************/

    private void populateNutrientArray(MCCWFoodSource thisMccwFoodSource, int hhNumber, double sizeHH,
                                       double unitsConsumed) {
        if (thisMccwFoodSource != null) {

            for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
                populateNutrientSource(hhNumber, sizeHH, unitsConsumed, mn, false);
            }
        }
    }

    /******************************************************************************************************************************************/
    /*
     * Report 431 Print out Nutrient by HH and show change if a Food is substituted
     */
    private void createOIHMNutrientreport(int isheet, Report report, Boolean isnotCopingStrategy) {

        System.out.println("In rep 431 OIHM Nutrient report ");

        int row = 1;
        int col = 1;

        reportWB.getSheet(isheet).setColumnWidths(col, 20, 20);

        reportWB.getSheet(isheet).setValue(col, row, "Household Number", boldTopStyle);

        row++;

        for (HH hh2 : uniqueHousehold) {

            reportWB.getSheet(isheet).setValue(col, row++, hh2.getHhNumber(), numberd2);
        }
        row = 1;
        col = 2;
        printNutrientHeader(isheet, col, row);

        for (HH hh : uniqueHousehold) {

            /* add DDI nutrients to nutrients array */
            defaultDietItems = (List<DefaultDietItem>) hh.getHousehold().getStudy().getDefaultDietItem();

            for (DefaultDietItem defaultDietItem : defaultDietItems) {
                /* how many calories needed of this RST */
                double calsNeeded = defaultDietItem.getCalsNeededofThisDDI();
                double rstKCAL = defaultDietItem.getResourcesubtype().getResourcesubtypekcal();

                double kgNeeded = calsNeeded / rstKCAL;

                /* Kgs needed getcals/rst kcal */

                if (defaultDietItem.getResourcesubtype().getMccwFoodSource() != null) {

                    for (MicroNutrientLevel mnl : defaultDietItem.getResourcesubtype().getMccwFoodSource()
                            .getMicroNutrientLevel()) {
                        /* Kgs * mn level in this RST */

                        /* switch from negative to add amount made up in DDI */
                        double mnLevel = NumberUtils.toDouble(mnl.getMnLevel()) * kgNeeded * -1.0;

                        /* Note that wealthgroup and avgnumberinH not set in the NutrientCount array */

                        NutrientCount thismn = new NutrientCount();
                        thismn.setMn(mnl.getMicroNutrient());
                        thismn.setMnAmount(mnLevel);
                        thismn.setHhNumber(hh.getHhNumber());
                        thismn.setDDI(true);

                        totalNutrientCount.add(thismn);
                        totalNutrientCountFoodSubstitution.add(thismn);// TODO - What happens if DDI is substituted ?

                    }

                }
            }

        }

        /* Populate nutrientsCount array for use in report */
        calcHHNutrients(isheet, col, 0, uniqueHousehold);

        /* Now output report */

        row = 2;
        for (HH hh : uniqueHousehold) {

            for (int j = 0; j < nutrients.size(); j++) {

                MicroNutrient microNutrient = nutrients.get(j);

                double totalNutrient = calcHHNutrientFromArray(microNutrient, hh.getHhNumber(), false,
                        totalNutrientCount);

                double totalFoodubstitutionNutrient = calcHHNutrientFromArray(microNutrient, hh.getHhNumber(), false,
                        totalNutrientCountFoodSubstitution);

                /* totalNutrient is in KGs * RDA unit */

                /* Need to use AE */
                /* TODO */

                int hhSize = hh.getHousehold().getHouseholdMember().size();
                double mnYearRDA = microNutrient.getRda() * hhSize * 365;

                mnYearRDA = calcMNYearRDA(microNutrient, mnYearRDA);

                // divide by wgCounter to get average - normally will be 1.

                // daily RDA held in Nutrients table

                double nutrientPercent = 100 * totalNutrient / mnYearRDA;
                double foodSubNutrientPercent = 100 * (totalFoodubstitutionNutrient / mnYearRDA);

                System.out.println("ihm nutrient 1 = " + nutrientPercent);
                System.out.println("ihm nutrient 2 = " + foodSubNutrientPercent);


                reportWB.getSheet(isheet).setValue(2 + (2 * j), row, nutrientPercent, numberStyle);

                reportWB.getSheet(isheet).setValue(3 + (2 * j), row, foodSubNutrientPercent,
                        numberStyle);
            }
            row++;
        }

    }

    /******************************************************************************************************************************************/
    private void createIncomereport(int isheet, Report report, String type, int model, Boolean isCopingStrategy) {
        int row = 1;
        // String type = "food" or "cash"
        // Model OHEA or OIHM

        Double cropIncome = 0.0;
        Double empIncome = 0.0;
        Double lspIncome = 0.0;
        Double lssIncome = 0.0;
        Double trIncome = 0.0;
        Double wfIncome = 0.0;
        Double total = 0.0;

        Double lsTotal = 0.0;
        Double acslsTotal = 0.0;

        /* asc = after change scenario */

        Double acsCropIncome = 0.0;
        Double acsEmpIncome = 0.0;
        Double acsLspIncome = 0.0;
        Double acsLssIncome = 0.0;
        Double acsTrIncome = 0.0;
        Double acsWfIncome = 0.0;
        Double acsTotal = 0.0;

        Double cashGain = 0.0;
        Double prevCashGain = 0.0;
        Double totalGain = 0.0;
        Double totalIncome = 0.0;
        int col = 0;
        Boolean isAfterChangeScenario = false;

        String initType = StringUtils.capitalize(type);

        int numberInWGWGorder = 0;

        errno = 2262;

        if (isOIHM) {
            col = 2;
            reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 40, 20, 40, 20, 40, 20, 40, 20, 40, 20, 40);
            reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyleRight);
        } else {
            reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20);
            col = 3;
            populateFirstThreeColumns(isheet, 1);
        }

        reportWB.getSheet(isheet).setValue(col++, row, "Crop " + initType + " Income ", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Crop " + initType + " Income After Change Scenario",
                boldTopStyle);

        reportWB.getSheet(isheet).setValue(col++, row, "Employment " + initType + " Income", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Employment " + initType + " Income After Change Scenario",
                boldTopStyle);

        reportWB.getSheet(isheet).setValue(col++, row, "Livestock " + initType + " Income", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Livestock " + initType + " Income After Change Scenario",
                boldTopStyle);

        reportWB.getSheet(isheet).setValue(col++, row, "Transfer " + initType + " Income", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Transfer " + initType + " Income After Change Scenario",
                boldTopStyle);

        reportWB.getSheet(isheet).setValue(col++, row, "Wildfood " + initType + " Income", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Wildfood " + initType + " Income After Change Scenario",
                boldTopStyle);

        reportWB.getSheet(isheet).setValue(col++, row, "Total " + initType + " Income", boldTopStyle);
        reportWB.getSheet(isheet).setValue(col++, row, "Total " + initType + " Income After Change Scenario ",
                boldTopStyle);

        if (isCopingStrategy) {
            expandabilityHeadings(isheet, row, col);
        }

        row = 2;
        errno = 2263;

        if (model == OIHM) {

            for (HH hh2 : uniqueHousehold) {

                isAfterChangeScenario = false;

                /*
                 * Set isAfterChangeScenario/isFoodSubstitution to false for first runs - set in
                 * attributes to function call
                 */

                try {
                    cropIncome = calcCropIncome(hh2.getHousehold().getCrop(), type, false, false, false, modellingScenario);
                    empIncome = calcEmpIncome(hh2.getHousehold().getEmployment(), type, false, false, false, modellingScenario);
                    lspIncome = calcLSP(hh2.getHousehold().getLivestockProducts(), type, false, false, false, modellingScenario);
                    lssIncome = calcLSS(hh2.getHousehold().getLivestockSales(), type, false, modellingScenario);
                    trIncome = calcTransIncome(hh2.getHousehold().getTransfer(), type, false, false, false, modellingScenario);
                    wfIncome = calcWFIncome(hh2.getHousehold().getWildFood(), type, false, false, false, modellingScenario);

                    isAfterChangeScenario = true;
                    acsCropIncome = calcCropIncome(hh2.getHousehold().getCrop(), type, isAfterChangeScenario,
                            isFoodSubstitution, false, modellingScenario);
                    acsEmpIncome = calcEmpIncome(hh2.getHousehold().getEmployment(), type, isAfterChangeScenario,
                            isFoodSubstitution, false, modellingScenario);
                    acsLspIncome = calcLSP(hh2.getHousehold().getLivestockProducts(), type, isAfterChangeScenario,
                            isFoodSubstitution, false, modellingScenario);
                    acsLssIncome = calcLSS(hh2.getHousehold().getLivestockSales(), type, isAfterChangeScenario, modellingScenario);

                    acsTrIncome = calcTransIncome(hh2.getHousehold().getTransfer(), type, isAfterChangeScenario,
                            isFoodSubstitution, false, modellingScenario);

                    acsWfIncome = calcWFIncome(hh2.getHousehold().getWildFood(), type, isAfterChangeScenario,
                            isFoodSubstitution, false, modellingScenario);

                    total = cropIncome + empIncome + lssIncome + lspIncome + trIncome + wfIncome;
                    acsTotal = acsCropIncome + acsEmpIncome + acsLssIncome + acsLspIncome + acsTrIncome + acsWfIncome;

                    lsTotal = lssIncome + lspIncome;
                    acslsTotal = acsLssIncome + acsLspIncome;
                } catch (Exception e) {

                    /* #532 */
                    e.printStackTrace();

                    addError("OIHM Modelling Income Report Failure ");
                }

                col = 1;
                reportWB.getSheet(isheet).setValue(col++, row, hh2.getHousehold().getHouseholdNumber(), numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, cropIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acsCropIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, empIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acsEmpIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, lsTotal, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acslsTotal, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, trIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acsTrIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, wfIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acsWfIncome, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, total, numberd2);
                reportWB.getSheet(isheet).setValue(col++, row, acsTotal, numberd2);

                totalIncome = acsTotal;

                if (isCopingStrategy) {
                    // if (type == "cash") {
                    totalGain = hh2.getHhDIAfterChangeScenario();
                    // } else if (type == "food") {
                    // totalGain = acsTotal;
                    // }

                    for (ExpandabilityRule expandabilityRule : expandabilityRules) {

                        /* #527 now need all changes, do not stop once +ve */

                        Double expandabilityValue = expandabilityGain(expandabilityRule, hh2.getHousehold(), type);

                        if (type == "cash") {
                            totalIncome += expandabilityValue;
                            totalGain += expandabilityValue;
                        } else if (type == "food") {
                            totalIncome -= expandabilityValue;
                        }

                        reportWB.getSheet(isheet).setValue(col, row, totalIncome, numberd2);

                        col++;
                    }
                }

                row++;

            }

        } else if (model == OHEA) {
            errno = 2264;
            // for (WGI wgi2 : uniqueWealthgroupInterview) {
            for (WGI wgi2 : uniqueCommunity) {
                /*
                 * Work on communities, not individual wealthgroups so that can handle averages
                 */

                isAfterChangeScenario = false;

                ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

                for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
                        .filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
                    listWealthgroup.add(wgi4.getWealthgroup());
                }

                for (int k = 0; k < 3; k++) {

                    if (wgi2.getCommunity().getCountWGinCommunity()[k] > 0) {
                        row = calcIncomeWGI(isheet, type, isCopingStrategy, row, isAfterChangeScenario, wgi2,
                                listWealthgroup, k + 1);
                    } else {
                        row++;
                    }

                }

            }
        }

    }

    private int calcIncomeWGI(int isheet, String type, Boolean isCopingStrategy, int row, Boolean isAfterChangeScenario,
                              WGI wgi2, ArrayList<WealthGroup> listWealthgroup, int wgOrder) {
        Double cropIncome = 0.0;
        Double empIncome = 0.0;
        Double lspIncome = 0.0;
        Double lssIncome = 0.0;
        Double trIncome = 0.0;
        Double wfIncome = 0.0;
        Double total = 0.0;
        Double lsTotal = 0.0;
        Double acslsTotal = 0.0;
        Double acsCropIncome = 0.0;
        Double acsEmpIncome = 0.0;
        Double acsLspIncome = 0.0;
        Double acsLssIncome = 0.0;
        Double acsTrIncome = 0.0;
        Double acsWfIncome = 0.0;
        Double acsTotal = 0.0;
        Double totalGain = 0.0;
        Double totalIncome = 0.0;

        int col;
        int numberInWGWGorder;
        if (listWealthgroup.stream().filter(p -> p.getWgorder() == wgOrder).findAny().isPresent()) {

            Double[] income = new Double[20];
            for (int k = 0; k < income.length; k++)
                income[k] = 0.0;

            // How many in this Wealthgroup of same wgorders

            numberInWGWGorder = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == wgOrder).count();

            for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == wgOrder)
                    .collect(Collectors.toList())) {

                // always 1 wgi for a wg - fix the relationship!
                /*
                 * last two params are isFoodSubstitution (always false for pre change scenario)
                 * and isOHEA (true here for WGI
                 */
                isAfterChangeScenario = false;
                income[0] += calcCropIncome(wealthGroup2.getWealthGroupInterview().get(0).getCrop(), type,
                        isAfterChangeScenario, false, true, modellingScenario);

                income[1] += calcEmpIncome(wealthGroup2.getWealthGroupInterview().get(0).getEmployment(), type,
                        isAfterChangeScenario, false, false, modellingScenario);
                income[2] += calcLSP(wealthGroup2.getWealthGroupInterview().get(0).getLivestockProducts(), type,
                        isAfterChangeScenario, false, false, modellingScenario);
                income[3] += calcLSS(wealthGroup2.getWealthGroupInterview().get(0).getLivestockSales(), type,
                        isAfterChangeScenario, modellingScenario);
                income[4] += calcTransIncome(wealthGroup2.getWealthGroupInterview().get(0).getTransfer(), type,
                        isAfterChangeScenario, false, false, modellingScenario);
                income[5] += calcWFIncome(wealthGroup2.getWealthGroupInterview().get(0).getWildFood(), type,
                        isAfterChangeScenario, false, false, modellingScenario);

                isAfterChangeScenario = true;

                income[6] += calcCropIncome(wealthGroup2.getWealthGroupInterview().get(0).getCrop(), type,
                        isAfterChangeScenario, isFoodSubstitution, true, modellingScenario);
                income[7] += calcEmpIncome(wealthGroup2.getWealthGroupInterview().get(0).getEmployment(), type,
                        isAfterChangeScenario, isFoodSubstitution, true, modellingScenario);
                income[8] += calcLSP(wealthGroup2.getWealthGroupInterview().get(0).getLivestockProducts(), type,
                        isAfterChangeScenario, isFoodSubstitution, true, modellingScenario);
                income[9] += calcLSS(wealthGroup2.getWealthGroupInterview().get(0).getLivestockSales(), type,
                        isAfterChangeScenario, modellingScenario);
                income[10] += calcTransIncome(wealthGroup2.getWealthGroupInterview().get(0).getTransfer(), type,
                        isAfterChangeScenario, isFoodSubstitution, true, modellingScenario);
                income[11] += calcWFIncome(wealthGroup2.getWealthGroupInterview().get(0).getWildFood(), type,
                        isAfterChangeScenario, isFoodSubstitution, true, modellingScenario);

            }

            cropIncome = income[0] / numberInWGWGorder;
            empIncome = income[1] / numberInWGWGorder;
            lspIncome = income[2] / numberInWGWGorder;
            lssIncome = income[3] / numberInWGWGorder;
            trIncome = income[4] / numberInWGWGorder;
            wfIncome = income[5] / numberInWGWGorder;

            acsCropIncome = income[6] / numberInWGWGorder;
            acsEmpIncome = income[7] / numberInWGWGorder;
            acsLspIncome = income[8] / numberInWGWGorder;
            acsLssIncome = income[9] / numberInWGWGorder;
            acsTrIncome = income[10] / numberInWGWGorder;
            acsWfIncome = income[11] / numberInWGWGorder;

            total = cropIncome + empIncome + lspIncome + lssIncome + trIncome + wfIncome;
            acsTotal = acsCropIncome + acsEmpIncome + acsLspIncome + acsLssIncome + acsTrIncome + acsWfIncome;
            lsTotal = lssIncome + lspIncome;
            acslsTotal = acsLssIncome + acsLspIncome;

            col = 3;
            reportWB.getSheet(isheet).setValue(col++, row, cropIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acsCropIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, empIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acsEmpIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, lsTotal, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acslsTotal, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, trIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acsTrIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, wfIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acsWfIncome, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, total, numberStyle);
            reportWB.getSheet(isheet).setValue(col++, row, acsTotal, numberStyle);
            totalIncome = acsTotal;

            Double prevDI = 0.0;
            Double cashIncome = 0.0;
            Double prevIncome = acsTotal;
            Boolean isDIgreaterthanZero = false;
            Double expandabilityValue = 0.0;

            prevDI = wgi2.getWealthgroupInterview().getdIAfterChangeScenario();

            if (isCopingStrategy) {

                if (wgi2.getWealthgroupInterview().getdIAfterChangeScenario() > 0) {
                    isDIgreaterthanZero = true;
                }

                totalGain = wgi2.wgiDIAfterChangeScenario;

                for (ExpandabilityRule expandabilityRule : expandabilityRules) {

                    /*
                     * #527 #520 Do not need to only report if value is < 0 - report all increments
                     */

                    // if (prevDI > 0) {

                    // show prev Income - note that DI could go negative again if other exp rules
                    // applied
                    // reportWB.getSheet(isheet).setValue(col, row, prevIncome, numberStyle);

                    // } else // get check if expandability rule applies for this wg
                    // {

                    expandabilityValue = expandabilityGain(expandabilityRule, wgi2.getWealthgroupInterview(), type);

                    if (type == "cash") {
                        totalIncome += expandabilityValue;
                    } else if (type == "food") {
                        totalIncome -= expandabilityValue;
                    }

                    reportWB.getSheet(isheet).setValue(col, row, totalIncome, numberStyle);

                    // }
                    cashIncome = expandabilityGain(expandabilityRule, wgi2.getWealthgroupInterview(), "cash");
                    // was prevDI += expandabilityValue;
                    prevDI += cashIncome;
                    prevIncome = totalIncome;
                    col++;
                }

            }
            row++;
        }
        return (row);
    }

    /******************************************************************************************************************************************/

    private void populateFirstThreeColumns(int isheet, int startRow) {
        int row = startRow;
        int wgrow;
        int averagesRow = startRow + 2;
        int countWGs = 0;

        /* Print Communities */
        String comm;

        reportWB.getSheet(isheet).setValue(1, startRow, "Community", boldTopStyle);
        reportWB.getSheet(isheet).setValue(2, startRow, "Wealthgroup", boldTopStyle);

        JxlsSheet sheet2 = reportWB.getSheet(isheet);
        printAllSitesNames(sheet2, startRow + 1, 1, false);

        for (WGI wgi2 : uniqueCommunity) {
            comm = wgi2.getCommunity().getSite().getLocationdistrict() + " "
                    + wgi2.getCommunity().getSite().getSubdistrict();

            /* Print Wealthgroups within Community */
            /* Order by WG Order */

            wgi2.getCommunity().getWealthgroup();

            String communityID = wgi2.getCommunity().getCommunityid();
            orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
                    .filter(p -> p.getCommunity().getCommunityid() == communityID)
                    .sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

            wgrow = row + 1;

            for (WealthGroup wealthGroup : orderedWealthgroups) {

                for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {

                    if (wealthGroupInterview.getStatus() == Status.Validated) {
                        // reportWB.getSheet(isheet).setValue(2, wgrow, wealthGroup.getWgnameeng(),
                        // textStyleRight);

                        wgrow++;
                    }
                }

                if (!isDisplayWealthgroupDone) // store list of displayed WGs - only do once
                {

                    for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {

                        if (wealthGroupInterview.getStatus() == Status.Validated) {
                            displayWealthgroup.add(wealthGroup);

                            countWGs++;
                        }
                    }
                }
            }

            isDisplayWealthgroupDone = true;

            row = wgrow - 1;

        }

    }

    /******************************************************************************************************************************************/

    private void createAdultEquivalentreport(int isheet, Report report) {
        /*
         * Not used
         */
    }

    /******************************************************************************************************************************************/

    private Double wealthgroupInterviewAE(WealthGroupInterview wealthGroupInterview) {

        Double totAE = 0.0;
        Double ageReq = 0.0;
        int age = 0;
        Sex gender;

        /* AE = TE / 2600 */

        totAE = totAE / 2600;

        return Double.parseDouble(df2.format(totAE));

    }

    /******************************************************************************************************************************************/

    private Double householdAE(Household household) {

        Double totAE = 0.0;
        Double ageReq = 0.0;
        int age = 0;
        Sex gender;

        for (HouseholdMember hhm : household.getHouseholdMember()) {
            age = hhm.getAge();
            gender = hhm.getGender();

            WHOEnergyRequirements whoEnergy = WHOEnergyRequirements.findByAge(age);

            if (gender == Sex.Female) {
                totAE += whoEnergy.getFemale();
            } else if (gender == Sex.Male) {
                totAE += whoEnergy.getMale();
            }

        }

        /* AE = TE / 2600 */

        totAE = totAE / 2600;

        return Double.parseDouble(df2.format(totAE));

    }

    /*
     * Print all sites - only print wg is in wgorder 1,2,3
     */

    /******************************************************************************************************************************************/

    private void createHeaderPage() {
        final int STARTROW = 6;
        int i = STARTROW; // used for row number
        String currency = "";
        int col = 3;
        String siteName = "";
        String wgName = "";
        Method modellingreports = null;
        String strModellingReports = null;
        int iModellingReports=0;

        Efdutils.em("In Create Header Page for Coping Strategy ");
        Efdutils.em("IS OHEA = " + isOHEA);
        Efdutils.em("IS OIHM = " + isOIHM);

        if (isCopingStrategy)
            sheet[0] = reportWB.addSheet("Coping Strategy Summary");
        else
            sheet[0] = reportWB.addSheet("Change Scenario Summary");

        setSheetStyle(sheet[0]);
        sheet[0].setColumnWidths(1, 30, 50, 30, 30, 30);

        sheet[0].setValue(1, 1, "Date:", boldRStyle); // col,row
        sheet[0].setValue(2, 1, new Date(), dateStyle);
        sheet[0].setValue(1, 2, "Spec Name:", boldRStyle);
        if (isOHEA) {
            sheet[0].setValue(2, 2, "OHEA Modelling - All Reports", textStyleLeft);
            sheet[0].setValue(3, 4, "Livelihood Zone", boldRStyle);
            sheet[0].setValue(4, 4, livelihoodZone.getLzname(), textStyleRight);
            // textStyle);
        } else {
            sheet[0].setValue(2, 2, "OIHM Modelling - All Reports", textStyleLeft);
        }
        sheet[0].setValue(1, 4, "Reporting Currency:", boldRStyle);

        sheet[0].setValue(1, 13, "Change Scenario Title:", boldRStyle);
        sheet[0].setValue(1, 14, "Change Scenario Author:", boldRStyle);
        sheet[0].setValue(1, 15, "Change Scenario Description:", boldRStyle);

        sheet[0].setValue(2, 13, modellingScenario.getTitle(), textStyleLeft);
        sheet[0].setValue(2, 14, modellingScenario.getAuthor(), textStyleLeft);
        sheet[0].setValue(2, 15, modellingScenario.getDescription(), textStyleLeft);
        Efdutils.em("createheader = " + 10001);
        if (isOIHM) {
            sheet[0].setValue(1, 3, "Study:", boldRStyle);
            sheet[0].setValue(2, 3, study.getStudyName() + " " + study.getReferenceYear(), textStyleLeft);

            String hhName;
            if (isSelectedHouseholds) {
                sheet[0].setValue(col, i, "Selected Households in Report = " + hhSelected.size(), boldTopStyle);
                i++;
                for (HH hh2 : hhSelected) {
                    if ((hhName = hh2.getHousehold().getHouseholdName()) == null)
                        hhName = "-";
                    sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyleRight);
                    i++;
                }
            } else {
                sheet[0].setValue(col, i, "Report Spec Households in Report = " + uniqueHousehold.size(), boldTopStyle);
                i++;
                for (HH hh2 : uniqueHousehold) {
                    if ((hhName = hh2.getHousehold().getHouseholdName()) == null)
                        hhName = "-";
                    sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyleRight);
                    i++;
                }
            }

            // Default reporting currency based on Currency for an LZ within this Project

            for (LivelihoodZone livelihoodZone3 : study.getProjectlz().getLivelihoodZone()) {

                currency = livelihoodZone3.getCountry().getCurrency();

                break;
            }
            if (currency.isEmpty()) {
                currency = study.getProjectlz().getAltCurrency().getCurrency();
            }
            sheet[0].setValue(2, 4, currency, textStyleLeft);
            /* Which set of reports to Run */
            if (isCopingStrategy) {
                modellingreports = Method.MODELLINGOIHMCOPING;
            } else {
                modellingreports = Method.MODELLINGOIHMSCENARIO;
            }

        } else if (isOHEA) {
            Efdutils.em("createheader = " + 10009);
            sheet[0].setValue(1, 3, "Project:", boldRStyle);
            sheet[0].setValue(2, 3, project.getProjecttitle(), textStyleLeft);
            sheet[0].setValue(3, STARTROW, "Community/Sites", boldRStyle);
            sheet[0].setValue(4, STARTROW, "Wealthgroup", boldRStyle);

            /* Which set of reports to Run */
           // TODO Figure out why enum not working in query with Spring 5 and JPA - works ok in OX
            if (isCopingStrategy) {
             //   reportList = XPersistence.getManager().createQuery("from Report where code > 6 and method = 5").getResultList();
             //   iModellingReports=4;
             //   strModellingReports="MODELLINGOHEACOPING";
                modellingreports = Method.MODELLINGOHEACOPING;
            } else {
              //  reportList = XPersistence.getManager().createQuery("from Report where code > 6 and method = 3").getResultList();
              //  iModellingReports=5;
              //  strModellingReports="MODELLINGOHEASCENARIO";
                modellingreports = Method.MODELLINGOHEASCENARIO;
            }

            // Default reporting currency based on Currency for an LZ within this Project

            Optional<LivelihoodZone> firstLZ = project.getLivelihoodZone().stream().findFirst();
            currency = firstLZ.get().getCountry().getCurrency();
            sheet[0].setValue(2, 4, currency, textStyleLeft);

            // Need to group any with the same WGOrder

            i = STARTROW + 1;
            printAllSitesNames(sheet[0], i, col, true);
            Efdutils.em("createheader = " + 10010);
        }
        /* Note code 1-6 are visualisation spreadsheets */
        Efdutils.em("createheader = " + 10011);

         reportList = XPersistence.getManager().createQuery("from Report where method = :method and code > 6")
                    .setParameter("method", modellingreports).getResultList();
         //   reportList = XPersistence.getManager().createQuery("from Report where code > 6 and method = :method")
           //         .setParameter("method",iModellingReports).getResultList();


        Efdutils.em("createheader = " + 10012);
        sheet[0].setValue(2, STARTROW, "Reports", boldLStyle);

        /* get list of reports for modelling data spreadsheet */

        Efdutils.em("No of reports = " + reportList.size());

        i = STARTROW + 1;

        for (Report report : reportList) {
            System.out.println("reprt = "+report.getName());
        }


        if (modellingreports.equals(Method.MODELLINGOHEACOPING)
                || modellingreports.equals(Method.MODELLINGOHEASCENARIO)) {
            System.out.println("about to do report ordering");
            reportList.sort(Comparator.comparingInt(Report::getCode).reversed());
            System.out.println("done report ordering");
        } else {
            reportList.sort(Comparator.comparingInt(Report::getCode));
        }
        Efdutils.em("createheader = " + 10013);
        for (Report report : reportList) {
            sheet[0].setValue(2, i, report.getName(), textStyleLeft);
            sheet[i - 3] = reportWB.addSheet(report.getCode() + " " + report.getName());

            setSheetStyle(sheet[i - 3]);

            i++;
        }
        Efdutils.em("createheader = " + 10014);
        errno = 1108;
        int row = 18;
        /* Print Change Scenario and Food Substitutions */

        sheet[0].setValue(1, row, "Price Yield Variations", boldRStyle);

        for (PriceYieldVariation priceYieldVariation2 : modellingScenario.getPriceYieldVariations()) {
            sheet[0].setValue(2, row++,
                    priceYieldVariation2.getResource().getResourcetypename() + " Yield = "
                            + priceYieldVariation2.getYield() + "% Price = " + priceYieldVariation2.getPrice() + "%",
                    textStyleLeft);
        }
        row++;
        sheet[0].setValue(1, row, "Food Substitutions", boldRStyle);
        for (FoodSubstitution foodSubstitution2 : modellingScenario.getFoodSubstitution()) {
            sheet[0].setValue(2, row++, foodSubstitution2.getCurrentFood().getResourcetypename() + " replaced by "
                    + foodSubstitution2.getSubstitutionFood().getResourcetypename(), textStyleLeft);
        }
        Efdutils.em("createheader = " + 10015);
        if (isCopingStrategy) {
            sheet[0].setValue(1, row, "Coping Strategy", boldRStyle);
            sheet[0].setValue(2, row++, "(Resource,Exp. Increase Limit, Exp. Limit)", boldLStyle);

            for (ExpandabilityRule expandability : expandabilityRules) {
                sheet[0].setValue(2, row++,
                        expandability.getAppliedResourceSubType().getResourcetype().getResourcetypename() + " "
                                + expandability.getAppliedResourceSubType().getResourcetypename() + " "
                                + expandability.getExpandabilityIncreaseLimit() + "% "
                                + expandability.getExpandabilityLimit() + "%",
                        textStyleLeft);
            }
        }
        Efdutils.em("Completed Header ");
    }

    private void printAllSitesNames(JxlsSheet sheet2, int i, int col, boolean isHeader) {
        String siteName;

        for (Site site : sites) {
            siteName = site.getLocationdistrict() + " " + site.getSubdistrict();
            sheet2.setValue(col, i, siteName, textStyleRight);
            for (Community community2 : site.getCommunity()) {

                i = printWGName(sheet2, i, col, community2, 1); // Check if WGOrder present 1,2,3 more than 4 not
                // handled.
                i = printWGName(sheet2, i, col, community2, 2);
                i = printWGName(sheet2, i, col, community2, 3);

            }

            if (isHeader) /* Only space out on Header page - not on allk other HEA pages */ {
                i++;
            }

        }
    }

    /*
     *
     * If present print wgname - bsed on wgorder 1,2,3
     */
    private int printWGName(JxlsSheet sheet2, int i, int col, Community community2, int wgOrder) {
        String wgName;
        if (community2.getWealthgroup().stream().filter(p -> p.getWgorder() == wgOrder).findAny().isPresent()) {
            Optional<WealthGroup> wg = community2.getWealthgroup().stream().filter(p -> p.getWgorder() == wgOrder)
                    .findFirst();
            wgName = wg.get().getWgnameeng() + " (" + wg.get().getWgorder() + ")";

            if (community2.getCountWGinCommunity()[wgOrder - 1] > 1) {
                wgName = wgName + "*";
            }

            sheet2.setValue(col + 1, i, wgName, textStyleRight);
            i++;
        } else {
            sheet2.setValue(col + 1, i, " - ", textStyleRight);
            i++;
        }
        return i;
    }

    /******************************************************************************************************************************************/

    private void setSheetStyle(JxlsSheet sheet) {
        sheet.setColumnWidths(1, 20, 60);

    }

    /******************************************************************************************************************************************/

    //@Override
    //public String getForwardURI() {
    //    return forwardURI;
    //}

    /******************************************************************************************************************************************/

    //public void setForwardURI(String forwardURI) {
    //    this.forwardURI = forwardURI;
    //}

    /******************************************************************************************************************************************/

    //@Override
    //public boolean inNewWindow() {
    //  if (forwardURI == null)
    //     return false;
    // return true;
    //}

    /******************************************************************************************************************************************/
    /*
     * Is identified RST a Synonym? If so return the Micronutrient level of the base
     * RST
     */
    private MCCWFoodSource getMccWincludeSyn(ResourceSubType rst) {
        MCCWFoodSource thisMccwFoodSource;

        if (rst.getResourcesubtypesynonym() != null) {
            thisMccwFoodSource = rst.getResourcesubtypesynonym().getMccwFoodSource();
        } else {
            thisMccwFoodSource = rst.getMccwFoodSource();
        }

        return thisMccwFoodSource;
    }

    /******************************************************************************************************************************************/

    private void populateNutrientSource(int id, double avgInHH, double used, MicroNutrientLevel mn,
                                        boolean foodSubRun) {

        /* id is wgOrder or HHNumber */

        errno = 440010;
        if (NumberUtils.toDouble(mn.getMnLevel()) != 0.0) {

            NutrientCount thismn = new NutrientCount();
            thismn.setMn(mn.getMicroNutrient());

            double mnLevel = NumberUtils.toDouble(mn.getMnLevel());

            thismn.setWgOrder(id);
            thismn.setHhNumber(id);
            thismn.setMnAmount(mnLevel * used * 10); // x10 as mnLevel is per 100g - convert to kg
            thismn.setAverageNumberInHousehold(avgInHH);
            thismn.setDDI(false);
            /*
             * If food substitutions exist in change scenario then populate array and use in
             * nutrient calc
             */
            if (foodSubRun) {

                totalNutrientCountFoodSubstitution.add(thismn);
            } else {

                overallNutrientCount.add(thismn);
                totalNutrientCount.add(thismn);
            }
        }
    }

    /******************************************************************************************************************************************/
    private OptionalDouble calcAvgWGHH(int wgOrder) {

        long count = uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
                .mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).count();

        double sum = uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
                .mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).sum();

        return uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
                .mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).average();

    }
}