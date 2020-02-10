
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
 * */

package efd.actions;

import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

import org.apache.commons.lang.*;
import org.apache.poi.ss.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;
import efd.model.CustomReportSpecListModelling.*;
import efd.model.HouseholdMember.*;
import efd.model.Project.*;
import efd.model.Report.*;
import efd.model.StdOfLivingElement.*;
import efd.model.Transfer.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;

public class ModellingReports extends TabBaseAction implements IForwardAction, JxlsConstants {

	static Double RC = 2100.0 * 365;

	static final int NUMBER_OF_REPORTS = 15;
	static final int NUMBEROFAVERAGES = 10;

	static final int PRICE = 0;
	static final int YIELD = 1;

	static final int OHEA = 0;
	static final int OIHM = 1;

	private Community community = null;
	private LivelihoodZone livelihoodZone = null;
	private Project project = null;
	private Study study = null;
	private ModellingScenario modellingScenario = null;
	// private CustomReportSpec customReportSpec = null;

	private List<Report> reportList;
	private List<Site> selectedSites = new ArrayList<>();

	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	List<WGI> uniqueWealthgroupInterview;
	List<WGI> uniqueCommunity;
	List<HH> uniqueHousehold;
	List<ExpandabilityRule> expandabilityRules;

	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;

	ArrayList<WGI> wgi = new ArrayList<>();

	ArrayList<Site> sites = new ArrayList<>();

	ArrayList<WGI> wgiSelected = new ArrayList<>();
	ArrayList<WealthGroupInterview> wgiList = new ArrayList<>();
	ArrayList<QuantHousehold> quanthh = new ArrayList<>();
	ArrayList<WealthGroup> displayWealthgroup = new ArrayList<>();; // Note that assuming 1:1 WG:WGI
	ArrayList<HH> hh = new ArrayList<>();
	ArrayList<HH> hhSelected = new ArrayList<>();
	private List<Household> selectedHouseholds = new ArrayList<Household>();
	private List<PriceYieldVariation> priceYieldVariations = new ArrayList<PriceYieldVariation>();

	List<WGI> orderedQuantSeq = null;
	List<WealthGroup> orderedWealthgroups;
	List<WealthGroup> allOrderedWealthgroups;

	Map<Integer, Double> quantAvg = null;

	private String forwardURI = null;

	JxlsStyle boldRStyle = null;
	JxlsStyle boldLStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle textStyleLeft = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;
	JxlsStyle textStyleBlue = null;
	JxlsStyle numberd0 = null;

	CellStyle style = null;
	CellStyle vstyle = null;
	CellStyle datestyle = null;
	CellStyle title = null;
	CellStyle cnumberStyle = null;

	int errno = 0;
	Boolean isQuantile = false;
	Boolean isDisplayWealthgroupDone = false;
	Boolean isSelectedSites = false;
	String currency2;
	Boolean isOHEA = false;
	Boolean isOIHM = false;
	Boolean isCopingStrategy = false;
	Boolean isSelectedHouseholds = false;
	Boolean isValid = false;
	Boolean isChangeScenario = false;
	ModelType modelType;

	private static DecimalFormat df2 = new DecimalFormat("#.##");
	private static DecimalFormat df0 = new DecimalFormat("#");

	int numCommunities = 0;

	// ArrayList<ArrayList<Double>> averageTotal = new
	// ArrayList<>(NUMBEROFAVERAGES);

	Double[][] averageTotal = new Double[3][NUMBEROFAVERAGES];

	private String floatFormat = "##########0.00";
	private String integerFormat = "##############";

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run Modelling Reports ");
		int countValidated = 0;
		String selectionView;
		WealthGroupInterview wealthGroupInterview = null;
		Site site = null;
		int ddiTotPercent = 0;
		Boolean isCommunityHasExpandabilityRule = false;

		/*
		 * Get Change Scenario Details
		 * 
		 */
		String modellingScenarioId = getPreviousView().getValueString("id");
		modellingScenario = XPersistence.getManager().find(ModellingScenario.class, modellingScenarioId);

		Efdutils.em("Mod scenario = " + modellingScenario.getTitle() + " " + modellingScenarioId);

		/* Need to determine if for OHEA or OIHM (LZ or HH) */

		if (modellingScenario.getStudy() != null) {
			isOIHM = true; // OIHM Study and HH

			selectionView = "study.household";
			study = modellingScenario.getStudy();

		}

		else {
			isOHEA = true; // OHEA Community and LZ

			selectionView = "livelihoodZone.site";

			project = modellingScenario.getProject();

		}

		/*
		 * Get Model Type - Change Scenario or Coping Strategy, though Coping Strategy
		 * performs Change Scenario before Coping Strategy
		 * 
		 * If Coping Strategy need to find Coping Strategy for this Study/Project and
		 * alert if one has not been created
		 * 
		 */

		String model = getView().getValueString("modelType");
		Efdutils.em("modelType = " + model);
		if (model == "CopingStrategy") {
			isCopingStrategy = true;
		} else {
			isCopingStrategy = false;
		}

		Object communityId = null; // getPreviousView().getValue("communityid");

		Tab targetTab = getView().getSubview(selectionView).getCollectionTab();

		Map[] selectedOnes = targetTab.getSelectedKeys();

		System.out.println("selected keys a = " + selectedOnes.length);

		/* Populate local price yield variation for this Study/Project */

		for (PriceYieldVariation priceYieldVariation : modellingScenario.getPriceYieldVariations()) {
			priceYieldVariations.add(priceYieldVariation);
		}

		if (selectedOnes.length == 0) {
			addError("Select at least one Community/Site");
			return;
		} else if (selectedOnes.length != 0 && isOHEA) {
			isSelectedSites = true; // One or more Site selected in dialog
			for (int i = 0; i < selectedOnes.length; i++) {
				Map<?, ?> key = selectedOnes[i];

				String locidofsite = key.get("locationid").toString();
				Efdutils.em("OHEA key = " + key);
				Efdutils.em("OHEA locid from key  = " + locidofsite);

				// String subKey = key.toString().substring(12, 44);

				site = XPersistence.getManager().find(Site.class, locidofsite);

				Collection<Community> community3 = site.getCommunity();

				// add to array for Sites/Communities
				Site s = new Site();
				s = site;
				sites.add(s);

				// From Site get Communities/Sites then get WG and WGI

				for (Community community2 : site.getCommunity()) {
					countValidated = 0;

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
					System.out.println("countValidated = " + countValidated);
					if (countValidated < 3) // Community needs at least 3 Validated WGs
					{
						wgi.removeIf(p -> p.getCommunity() == community2);
						wgiList.removeIf(p -> p.getWealthgroup().getCommunity() == community2);
						System.out.println("removed community " + community2.getSite().getLocationdistrict() + " "
								+ community2.getSite().getSubdistrict());
					}
				}

				if (!isCommunityHasExpandabilityRule && isCopingStrategy) {
					addError("No Expandability Rules for selected Communities for Coping Strategy Report");
					closeDialog();
					return;
				}

			}

			// Populate WGI array wgis - use dialog selected list if enter

			populateWGIArray(wgiList);

			uniqueCommunity = wgi.stream().filter(distinctByKey(WGI::getCommunity)).collect(Collectors.toList());
			Efdutils.em("Unique Community count = " + uniqueCommunity.size());
			errno = 51;

			int ddipercent = 0;
			Iterator<WealthGroupInterview> wgiIter = wgiList.iterator();

			while (wgiIter.hasNext()) {

				WealthGroupInterview wgiNext = wgiIter.next();

				System.out.println("dditotal = " + (wgiNext.getWealthgroup().getCommunity().getDdipercenttotal()));
				if (wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 100
						&& wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 0) {
					addError("Default Diet Total Percentage for a chosen Community is not 100% or 0%");
					closeDialog();
					return;
				}

			}

			errno = 52;
			// Calculate DI

			calculateDI(); 
			System.out.println("done calc DI");
			// calculateAE(); // Calculate the Adult equivalent
			System.out.println("done calc AE");

		} else if (selectedOnes.length != 0 && isOIHM) { // OIHM

			// Does Study have Expandability Rule?

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

				Map<?, ?> membersNames = getView().getSubview("study.household").getMembersNames();

				String subKey = key.toString().substring(4, 36);

				Household singleHHSelected = XPersistence.getManager().find(Household.class, subKey);
				if (singleHHSelected.getStatus() == Status.Validated) {
					isValid = true;
				}

				HH e = new HH();
				e.household = singleHHSelected;
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

			JxlsWorkbook report = createReport("ModellingReports");
			errno = 55;
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
			errno = 56;
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
			errno = 57;
		} catch (Exception e) {
			addError(e + " Errno = " + errno);
			closeDialog();
			return;
		}

		closeDialog();

	}

	/******************************************************************************************************************************************/

	private void calculateAE() {

		if (isOIHM) {
			for (HH hh2 : uniqueHousehold) {

				hh2.setHhAE(householdAE(hh2.household));

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

			for (WGI wgi2 : uniqueWGI) {
				isChangeScenario = false;
				wgiDI = wealthgroupInterviewDI(wgi2.getWealthgroupInterview(), isChangeScenario);
				wgi2.setWgiDI(wgiDI);

				isChangeScenario = true;
				wgiDI = wealthgroupInterviewDI(wgi2.getWealthgroupInterview(), isChangeScenario);
				wgi2.setWgiDIAfterChangeScenario(wgiDI);

				// wgi2.getWealthgroup().setDefaultDI(wgiDI);

			}
			// uniqueWealthgroupInterview =
			// hh.stream().filter(distinctByKey(WGI::getWealthgroupInterview))
			// .sorted(Comparator.comparing(WGI::getWgiDI)).collect(Collectors.toList());
		} else if (isOIHM) {
			uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber()))
					.collect(Collectors.toList());

			for (HH hh2 : uniqueHousehold) {

				isChangeScenario = false;
				hh2.setHhDI(householdDI(hh2.household, isChangeScenario));
				isChangeScenario = true;
				hh2.setHhDIAfterChangeScenario(householdDI(hh2.household, isChangeScenario));

				Efdutils.em("DI and DI after = " + hh2.getHhDI() + " " + hh2.getHhDIAfterChangeScenario());

			}
			uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber())) // Sort by
																											// DI low -
																											// high
					.sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());
		}

	}

	/******************************************************************************************************************************************/

	private Double householdDI(Household household, Boolean isChangeScenario) {

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
		 * if isChangeScenario is Truie then take into account Scenario Rules from
		 * ModellingScenario where a Study is used
		 * 
		 */

		Double cropTI = 0.0;
		Double wildfoodsTI = 0.0;
		Double lspTI = 0.0;
		Double employmentTI = 0.0;
		Double transfersTI = 0.0;

		Double cropOP = 0.0;
		Double wildfoodsOP = 0.0;
		Double lspOP = 0.0;
		Double employmentOP = 0.0;
		Double transfersOP = 0.0;

		int requiredCalories = 0;

		System.out.println("In HH DI calc ");

		List<HH> thisHH = hh.stream().filter(d -> d.household == household).collect(Collectors.toList());

		List<HH> cropList = thisHH.stream().filter(d -> d.getType() == "Crop").collect(Collectors.toList());

		List<HH> wildfoodList = thisHH.stream().filter(d -> d.getType() == "Wildfood").collect(Collectors.toList());

		List<HH> livestockproductList = thisHH.stream().filter(d -> d.getType() == "LivestockProduct")
				.collect(Collectors.toList());

		List<HH> employmentList = thisHH.stream().filter(d -> d.getType() == "Employment").collect(Collectors.toList());

		List<HH> transferList = thisHH.stream().filter(d -> d.getType() == "Transfer").collect(Collectors.toList());

		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		try {
			if (cropList.size() > 0) {

				for (HH icrop : cropList) {

					// Crop crop = icrop.getCrop();

					if (isChangeScenario) {

						priceChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), PRICE);
						yieldChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), YIELD);

					} else {
						// Not in modelling scenario calc of DI
						priceChange = 1.0;
						yieldChange = 1.0;
					}

					cropTI += icrop.getCrop().getUnitsSold() * priceChange * icrop.getCrop().getPricePerUnit();

					cropOP += icrop.getCrop().getUnitsConsumed() * yieldChange
							* Double.valueOf(icrop.getCrop().getResourceSubType().getResourcesubtypekcal());

				}
			}

			if (wildfoodList.size() > 0) {
				for (HH iwildfood : wildfoodList) {

					wildfoodsTI += (iwildfood.getWildfood().getUnitsSold().doubleValue()
							* iwildfood.getWildfood().getPricePerUnit().doubleValue());

					wildfoodsOP += (iwildfood.getWildfood().getUnitsConsumed().doubleValue()
							* Double.valueOf(iwildfood.getWildfood().getResourceSubType().getResourcesubtypekcal()));
				}
			}
			if (livestockproductList.size() > 0) {
				for (HH ilivestockproduct : livestockproductList) {

					lspTI += (ilivestockproduct.getLivestockproducts().getUnitsSold()
							* ilivestockproduct.getLivestockproducts().getPricePerUnit());
					lspOP += (ilivestockproduct.getLivestockproducts().getUnitsConsumed().doubleValue()
							* Double.valueOf(ilivestockproduct.getLivestockproducts().getResourceSubType()
									.getResourcesubtypekcal()));
				}
			}
			if (employmentList.size() > 0) {
				for (HH iemployment : employmentList) {

					employmentTI += (iemployment.getEmployment().getUnitsWorked()
							* iemployment.getEmployment().getCashPaymentAmount()
							* iemployment.getEmployment().getPeopleCount());

					/* handle null resource type */
					int empresourcekcal = 0;
					ResourceSubType resourcesubtypekcal = iemployment.getEmployment().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						empresourcekcal = 0;
					else
						empresourcekcal = iemployment.getEmployment().getFoodResourceSubType().getResourcesubtypekcal();

					employmentOP += iemployment.getEmployment().getUnitsWorked() * empresourcekcal;
				}
			}
			if (transferList.size() > 0) {
				for (HH itransfer : transferList) {

					if (itransfer.getTransfer().getTransferType().toString().equals("Cash"))
						transfersTI += (itransfer.getTransfer().getCashTransferAmount()
								* itransfer.getTransfer().getPeopleReceiving()
								* itransfer.getTransfer().getTimesReceived());
					System.out.println("in DI transfer calc " + transfersTI);
					Double transresourcekcal = 0.0;
					ResourceSubType resourcesubtypekcal = itransfer.getTransfer().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						transresourcekcal = 0.0;
					else
						transresourcekcal = (double) itransfer.getTransfer().getFoodResourceSubType()
								.getResourcesubtypekcal();

					if (itransfer.getTransfer().getTransferType().toString().equals("Food"))
						transfersOP += (transresourcekcal * itransfer.getTransfer().getUnitsConsumed());
				}
			}
			System.out.println("done DI transfer calc " + transfersTI);
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			addWarning("Error in DI Calculation ");
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
			} else if (gender == Sex.Male) {
				energyNeed = whoEnergy.getMale();
			}

			requiredCalories += energyNeed * 365 * (12 - monthsAway) / 12;

		}

		System.out.println("requiredCalories = " + requiredCalories);

		Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI + transfersTI;

		System.out.println("cropTI = " + cropTI);
		System.out.println("wfTI = " + wildfoodsTI);
		System.out.println("lspTI = " + lspTI);
		System.out.println("empTI = " + employmentTI);
		System.out.println("transfTI = " + transfersTI);

		Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

		Double shortFall = requiredCalories - output;

		System.out.println("totalIncome = " + totalIncome);
		System.out.println("output = " + output);

		// Now it gets more complex , but not difficult

		// Diet
		// Diet Value = Sum (KCal per KG * Percentage of the Food type in default diet

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

		// Diet Amount Purchased DA = Shortfall / Diet Value in KGs
		Double dietAmountPurchased = 0.0;

		dietAmountPurchased = shortFall / dietValue;

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
	/*
	 * Return price yield variation in this modelling scenario for current
	 * Project/Study
	 */

	private Double priceYieldVariation(ModellingScenario modellingScenario2, ResourceSubType resourceSubType,
			int type) { /* Type PRICE or YIELD */

		Optional<PriceYieldVariation> pyc;
		pyc = priceYieldVariations.stream().filter(p -> p.getResource() == resourceSubType).findAny();

		if (pyc.isPresent()) {

			Efdutils.em("found a valid pyc = " + pyc.get().getResource().getResourcetypename());

			if (type == PRICE) {
				if (pyc.get().getPrice() == 0)
					return 1.0;
				else {
					Efdutils.em("in pyc price = " + pyc.get().getPrice());
					return pyc.get().getPrice() / 100.0;
				}
			} else if (type == YIELD) {
				if (pyc.get().getYield() == 0)
					return 1.0;
				else {
					Efdutils.em("in pyc yield = " + pyc.get().getYield());
					return pyc.get().getYield() / 100.0;
				}
			}

		}

		return 1.0;
	}

	/******************************************************************************************************************************************/
	private void populateWGIArray(List<WealthGroupInterview> wealthgroupsInterviews) {
		/*
		 * create array of all Validated wealthgroups for this commmunity
		 * 
		 * they can then be filtered
		 * 
		 */
		System.out.println("drb in populateArray begin size = " + wealthgroupsInterviews.size());

		for (WealthGroupInterview wealthgroupInterview : wealthgroupsInterviews) {
			System.out.println("in pop wgi array ");
			populateWGIfromWealthgroupInterview(wealthgroupInterview);
			System.out.println("back from  pop wgi array ");

		}

		System.out.println("end populateArray =" + wgi.size());

		for (WGI wgi2 : wgi) {
			for (AssetLand assetLand : wgi2.getWealthgroupInterview().getAssetLand()) {
				System.out.println("post populate assetland = " + assetLand.getResourceSubType().getResourcetypename()
						+ " " + assetLand.getNumberOfUnits());
			}
		}

		System.out.println(Arrays.toString(wgi.toArray()));
		// System.out.println("WGI Array = " + wgi.toString());

	}

	/******************************************************************************************************************************************/
	private void populateHHArray(List<Household> households) {
		/*
		 * create array of all Validated households for this study
		 * 
		 * they can then be filtered
		 * 
		 */
		System.out.println("drb in populateArray begin size = " + households.size());

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

		// System.out.println("in populateHHfromHosuehold, hosuehold =
		// "+household.getHouseholdName()+" answer =
		// "+answer.getConfigQuestionUse().getConfigQuestion().getPrompt());;

		System.out.println("assetland size = " + household.getAssetLand().size());
		System.out.println("assetfs size = " + household.getAssetFoodStock().size());
		System.out.println("transfer ass size = " + household.getTransfer().size());

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

		// System.out.println("in populateHHfromHosuehold, hosuehold =
		// "+household.getHouseholdName()+" answer =
		// "+answer.getConfigQuestionUse().getConfigQuestion().getPrompt());;

		WealthGroupInterview wgi2;

		System.out.println("assetland size = " + wealthGroupInterview.getAssetLand().size());
		System.out.println("assetfs size = " + wealthGroupInterview.getAssetFoodStock().size());
		System.out.println("transfer ass size = " + wealthGroupInterview.getTransfer().size());
		System.out.println("crop size = " + wealthGroupInterview.getCrop().size());

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

		// for (WGI wgi3 : wgi) {
		// System.out.println(" wgi 3 Land =
		// "+wgi3.getLand().getResourceSubType().getResourcetypename());
		// System.out.println(" wgi 3 RST = " +
		// wgi3.getResourceSubType().getResourcetypename());
		// System.out.println(
		// " wgi 3 Site = " + wgi3.getSite().getLocationdistrict() + " " +
		// wgi3.getSite().getSubdistrict());
		// System.out.println(" wgi 3 LZ = " + wgi3.getLivelihoodZone().getLzname());
		// System.out.println(" wgi 3 Proj = " + wgi3.getProject().getProjecttitle());

		// }

		System.out.println("return from array pop");
	}

	/******************************************************************************************************************************************/

	private void addToWGIArray(WealthGroupInterview wealthGroupInterview, Collection<Category> category,
			ResourceType resourceType, ResourceSubType resourceSubType, String type, AssetLand land,
			AssetFoodStock foodstock, AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable,
			AssetTree tree, Crop crop, Employment employment, Inputs inputs, LivestockProducts livestockproducts,
			LivestockSales livestocksales, Transfer transfer, WildFood wildfood) {

		WGI e = new WGI();

		System.out.println("in addtowgiarray " + wealthGroupInterview);

		e.setWealthgroupInterview(wealthGroupInterview);
		e.setSite(wealthGroupInterview.getWealthgroup().getCommunity().getSite());
		e.setCommunity(wealthGroupInterview.getWealthgroup().getCommunity());
		e.setWealthgroup(wealthGroupInterview.getWealthgroup());
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

		String filename = reportTitle + Calendar.getInstance().getTime();
		reportWB = new JxlsWorkbook(filename);
		Boolean isCopingStrategy = true;
		Boolean isnotCopingStrategy = false;

		setStyles();

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

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);

		reportWB.getSheet(isheet).setValue(1, row, "Household Number", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, row, "DI As Reported", boldTopStyle);
		reportWB.getSheet(isheet).setValue(3, row, "DI After Change Scenario", boldTopStyle);
		row++;

		Efdutils.em("uniqueHH = " + uniqueHousehold.size());
		for (HH hh2 : uniqueHousehold) {

			reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), numberd0);
			reportWB.getSheet(isheet).setValue(2, row, hh2.getHhDI(), numberd0);
			reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDIAfterChangeScenario(), numberd0);
			row++;

		}

		copingStrategyOutput(isheet, isCopingStrategy, 1, col);

	}

	/******************************************************************************************************************************************/

	private void createOHEADIreport(int isheet, Report report, Boolean isCopingStrategy) {

		int row = 2;
		int col = 5;
		int i = 0;
		double hhSize = 0;

		/*
		 * Will need to revisit average total array if WGs per community increase
		 */

		averageReset();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20);

		populateFirstThreeColumns(isheet, 1);

		// reportWB.getSheet(isheet).setValue(4, 1, "Family Group Size", boldTopStyle);
		reportWB.getSheet(isheet).setValue(3, 1, "Disposable Income As Reported", boldTopStyle);
		reportWB.getSheet(isheet).setValue(4, 1, "DI After Change Scenario", boldTopStyle);

		// NOTE OHEA ordered by wg Number order not DI

		for (WGI wgi2 : uniqueCommunity) {

			orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
					.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
					.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

			for (i = 0; i < orderedWealthgroups.size(); i++) {

				WealthGroup thisWealthGroup = orderedWealthgroups.get(i);

				// Handle null hhsize

				List<WealthGroupInterview> wealthGroupInterview3 = orderedWealthgroups.get(i).getWealthGroupInterview();

				reportWB.getSheet(isheet).setValue(3, row, wgi2.getWgiDI(), textStyle);
				reportWB.getSheet(isheet).setValue(4, row, wgi2.wgiDIAfterChangeScenario, textStyle);

				row++;
			}

		}

		copingStrategyOutput(isheet, isCopingStrategy, 1, col);

	}

	/******************************************************************************************************************************************/
	/*
	 * If Coping Strategy is set then print Rule name headers and perform calcs
	 * 
	 * 
	 */
	private void copingStrategyOutput(int isheet, Boolean isCopingStrategy, int row, int col) {

		int startRow = row;
		int startCol = col;
		Double cashGain = 0.0;

		if (!isCopingStrategy) {
			return;
		}

		startCol = col;
		/* Expandability Headings */
		for (ExpandabilityRule expandabilityRule : expandabilityRules) {

			reportWB.getSheet(isheet).setValue(startCol++, startRow, expandabilityRule.getRuleName(), boldTopStyle);

		}
		
		/* Expandability Data */

		if (isOIHM) {
			System.out.println("Coping Strategy for OIHM ExpRule size = " + expandabilityRules.size());
			for (ExpandabilityRule expandabilityRule : expandabilityRules) {
				row=startRow+1;
				for (HH hh2 : uniqueHousehold) {
					cashGain = 0.0;
					if (hh2.getHhDIAfterChangeScenario() > 0)

					// If DI after change scenario is > 0 then use DI after change scenario,
					// otherwise apply expandability rule if applicaable

					{

						reportWB.getSheet(isheet).setValue(col, row, hh2.getHhDIAfterChangeScenario(), numberd0);
					} else // get check if expandability rule applies for this hh
					{

						List<Crop> cropCollection = hh2.getHousehold().getCrop().stream()
						.filter(p -> p.getResourceSubType() == expandabilityRule.getAppliedResourceSubType()).collect(Collectors.toList());
						
						List<WildFood> wfCollection = hh2.getHousehold().getWildFood().stream()
								.filter(p -> p.getResourceSubType() == expandabilityRule.getAppliedResourceSubType()).collect(Collectors.toList());
						
						
						//List<LivestockSales> lssCollection = hh2.getHousehold().getLivestockSales().stream()
						//		.filter(p -> p.getResourceSubType() == expandabilityRule.getAppliedResourceSubType()).collect(Collectors.toList());
								
						List<LivestockProducts> lspCollection = hh2.getHousehold().getLivestockProducts().stream()
								.filter(p -> p.getResourceSubType() == expandabilityRule.getAppliedResourceSubType()).collect(Collectors.toList());
						
								
						
						if(cropCollection.size()>0)
						 {
							System.out.println("found a crop match ");
							
							for (Crop crop : cropCollection) {
							
								cashGain += (crop.getUnitsProduced() - crop.getUnitsSold()) * crop.getPricePerUnit();
								System.out.println("exp match found = "+crop.getResourceSubType().getResourcetypename()+" "+cashGain);
							}
						} 
						else if (wfCollection.size() >0) {
							System.out.println("found a wf match ");
						
							for (WildFood wf : wfCollection) {
							
								cashGain += (wf.getUnitsProduced() - wf.getUnitsSold()) * wf.getPricePerUnit();
								System.out.println("exp match found = "+wf.getResourceSubType().getResourcetypename()+" "+cashGain);
							}
						}
						else if (lspCollection.size() >0) {
							System.out.println("found a lss match ");
						
							for (LivestockProducts lsp : lspCollection) {
							
								cashGain += (lsp.getUnitsProduced() - lsp.getUnitsSold()) * lsp.getPricePerUnit();
								System.out.println("exp match found = "+lsp.getResourceSubType().getResourcetypename()+" "+cashGain);
							}
					}
						else {
							cashGain = hh2.getHhDIAfterChangeScenario();
						}
						reportWB.getSheet(isheet).setValue(col, row, hh2.getHhDIAfterChangeScenario()+cashGain, numberd0);

						

					}
					row++;
				}
				col++;
			}

		} else if (isOHEA) {
			System.out.println("Coping Strategy for OHEA ExpRule size = " + expandabilityRules.size());

			/* Does any WG have and RST in teh Expandability Rules? */

			row++;

		}

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
		int i = 0;
		double hhSOLC = 0.0;
		double hhSize = 0.0;
		double wgSOLInclusion = 0.0;
		double wgSOLSurvival = 0.0;

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20);

		populateFirstThreeColumns(isheet, 1);

		reportWB.getSheet(isheet).setValue(3, row, "Std of Living Reqt - Inclusion", boldTopStyle); // B
		reportWB.getSheet(isheet).setValue(4, row, "Std of Living Reqt - Survival", boldTopStyle); // B
		reportWB.getSheet(isheet).setValue(5, row, "Disposable Income As Reported", boldTopStyle); // C
		reportWB.getSheet(isheet).setValue(6, row, "DI After Change Scenario", boldTopStyle); // D
		reportWB.getSheet(isheet).setValue(7, row, "DI With StoL as Reported", boldTopStyle); // E
		reportWB.getSheet(isheet).setValue(8, row, "DI With StoL as Reported After Change Scenario", boldTopStyle); // F

		row = 2;
		for (WGI wgi2 : uniqueCommunity) {

			System.out.println("in DI STOL uc loop ");
			orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
					.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
					.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

			for (i = 0; i < orderedWealthgroups.size(); i++) {

				WealthGroup thisWealthGroup = orderedWealthgroups.get(i);

				List<WealthGroupInterview> wealthGroupInterview3 = orderedWealthgroups.get(i).getWealthGroupInterview();

				hhSOLC = 0.0;
				hhSize = 0.0;
				wgSOLInclusion = 0.0;
				wgSOLSurvival = 0.0;

				for (int k = 0; k < wealthGroupInterview3.size(); k++) {

					if (wealthGroupInterview3.get(k).getWgAverageNumberInHH() == null) {

						hhSize = 0;
					} else {
						hhSize = wealthGroupInterview3.get(k).getWgAverageNumberInHH();
					}
				}
				// Standard of Living

				for (StdOfLivingElement stdOfLivingElement : thisWealthGroup.getCommunity().getStdOfLivingElement()) {
					if (stdOfLivingElement.getLevel() == StdLevel.Household) {
						wgSOLInclusion += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount();
						wgSOLSurvival += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
								* stdOfLivingElement.getSurvival() / 100;
					} else if (stdOfLivingElement.getLevel() == StdLevel.HouseholdMember) {
						wgSOLInclusion += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount() * hhSize;
						wgSOLSurvival += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
								* (stdOfLivingElement.getSurvival() / 100.0) * hhSize;

					}

				}
				System.out.println("in DI STOL uc loop Output ");
				reportWB.getSheet(isheet).setValue(3, row, wgSOLInclusion, numberd0); // B
				reportWB.getSheet(isheet).setValue(4, row, wgSOLSurvival, numberd0);
				reportWB.getSheet(isheet).setValue(5, row, wgi2.getWgiDI(), numberd0); // C
				reportWB.getSheet(isheet).setValue(6, row, wgi2.getWgiDIAfterChangeScenario(), numberd0);
				reportWB.getSheet(isheet).setValue(7, row, wgi2.getWgiDI() - wgSOLInclusion, numberd0);
				reportWB.getSheet(isheet).setValue(8, row, wgi2.getWgiDIAfterChangeScenario() - wgSOLInclusion,
						numberd0);
				row++;

			}
		}
		errno = 109;
	}

	/******************************************************************************************************************************************/

	private void createOIHMDIAfterSOLreport(int isheet, Report report, Boolean isCopingStrategy) {
		int row = 1;
		Double hhSOLC = 0.0;

		Efdutils.em("In OIHM DISOL report");

		// uniqeHousehold = number of households in array that are relevant
		reportWB.getSheet(isheet).setColumnWidths(1, 17, 17, 28, 28, 28, 28);
		for (HH hh3 : uniqueHousehold) {

			hhSOLC = 0.0;
			for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {

				if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {

					hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());

					Efdutils.em("Household STOL " + hhSOLC);

				} else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {
					errno = 104;
					for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {

						hhSOLC += calcHhmSolc(householdMember, stdOfLivingElement);

						Efdutils.em("HouseholdMember STOL " + hhSOLC);

						errno = 105;
					}

				}

			}

			hh3.setHhSOLC(hhSOLC);

		}

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Std of Living Reqt", boldTopStyle); // B
		reportWB.getSheet(isheet).setValue(3, row, "Disposable Income As Reported", boldTopStyle); // C
		reportWB.getSheet(isheet).setValue(4, row, "DI After Change Scenario", boldTopStyle); // D
		reportWB.getSheet(isheet).setValue(5, row, "DI With StoL as Reported", boldTopStyle); // E
		reportWB.getSheet(isheet).setValue(6, row, "DI With StoL as Reported After Change Scenario", boldTopStyle); // F

		row++;

		for (HH hh2 : uniqueHousehold) {

			reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), numberd0);
			reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), numberd0); // B
			reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDI(), numberd0); // C
			reportWB.getSheet(isheet).setValue(4, row, hh2.getHhDIAfterChangeScenario(), numberd0); // D
			reportWB.getSheet(isheet).setValue(5, row, hh2.getHhDI() - hh2.getHhSOLC(), numberd0); // C - B
			reportWB.getSheet(isheet).setValue(6, row, hh2.getHhDIAfterChangeScenario() - hh2.getHhSOLC(), numberd0); // D
																														// -
																														// B
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
		Double lsIncome = 0.0;
		Double trIncome = 0.0;
		Double wfIncome = 0.0;
		Double total = 0.0;

		/* asc = after change scenario */

		Double acsCropIncome = 0.0;
		Double acsEmpIncome = 0.0;
		Double acsLsIncome = 0.0;
		Double acsTrIncome = 0.0;
		Double acsWfIncome = 0.0;
		Double acsTotal = 0.0;

		int col = 0;
		Boolean isAfterChangeScenario = false;

		String initType = StringUtils.capitalize(type);

		System.out.println("in  income report");

		errno = 2262;

		if (isOIHM) {
			col = 2;
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 40, 20, 40, 20, 40, 20, 40, 20, 40, 20, 40);
			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
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

		row = 2;

		if (model == OIHM) {

			for (HH hh2 : uniqueHousehold) {

				isAfterChangeScenario = false;
				cropIncome = calcCropIncome(hh2, type, isAfterChangeScenario);
				empIncome = calcEmpIncome(hh2, type, isAfterChangeScenario);
				lsIncome = calcLSIncome(hh2, type, isAfterChangeScenario);
				trIncome = calcTransIncome(hh2, type, isAfterChangeScenario);
				wfIncome = calcWFIncome(hh2, type, isAfterChangeScenario);

				isAfterChangeScenario = true;
				acsCropIncome = calcCropIncome(hh2, type, isAfterChangeScenario);
				acsEmpIncome = calcEmpIncome(hh2, type, isAfterChangeScenario);
				acsLsIncome = calcLSIncome(hh2, type, isAfterChangeScenario);
				acsTrIncome = calcTransIncome(hh2, type, isAfterChangeScenario);
				acsWfIncome = calcWFIncome(hh2, type, isAfterChangeScenario);

				total = cropIncome + empIncome + lsIncome + trIncome + wfIncome;
				acsTotal = acsCropIncome + acsEmpIncome + acsLsIncome + acsTrIncome + acsWfIncome;

				col = 1;
				reportWB.getSheet(isheet).setValue(col++, row, hh2.getHousehold().getHouseholdNumber(), numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, cropIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsCropIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, empIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsEmpIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, lsIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsLsIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, trIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsTrIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, wfIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsWfIncome, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, total, numberd0);
				reportWB.getSheet(isheet).setValue(col++, row, acsTotal, numberd0);
				row++;

			}
		} else if (model == OHEA) {
			for (WGI wgi2 : uniqueCommunity) {

				orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
						.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
						.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

				for (int i = 0; i < orderedWealthgroups.size(); i++) {

					List<WealthGroupInterview> wealthGroupInterviews = orderedWealthgroups.get(i)
							.getWealthGroupInterview();

					isAfterChangeScenario = false;
					cropIncome = calcCropIncome(wgi2, type, isAfterChangeScenario);
					empIncome = calcEmpIncome(wgi2, type, isAfterChangeScenario);
					lsIncome = calcLSIncome(wgi2, type, isAfterChangeScenario);
					trIncome = calcTransIncome(wgi2, type, isAfterChangeScenario);
					wfIncome = calcWFIncome(wgi2, type, isAfterChangeScenario);

					isAfterChangeScenario = true;
					acsCropIncome = calcCropIncome(wgi2, type, isAfterChangeScenario);
					acsEmpIncome = calcEmpIncome(wgi2, type, isAfterChangeScenario);
					acsLsIncome = calcLSIncome(wgi2, type, isAfterChangeScenario);
					acsTrIncome = calcTransIncome(wgi2, type, isAfterChangeScenario);
					acsWfIncome = calcWFIncome(wgi2, type, isAfterChangeScenario);

					total = cropIncome + empIncome + lsIncome + trIncome + wfIncome;
					acsTotal = acsCropIncome + acsEmpIncome + acsLsIncome + acsTrIncome + acsWfIncome;

					col = 3;
					reportWB.getSheet(isheet).setValue(col++, row, cropIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsCropIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, empIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsEmpIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, lsIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsLsIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, trIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsTrIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, wfIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsWfIncome, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, total, numberd0);
					reportWB.getSheet(isheet).setValue(col++, row, acsTotal, numberd0);
					row++;

				}
			}
		}

	}

	/******************************************************************************************************************************************/

	private void populateFirstThreeColumns(int isheet, int startRow) {
		int row = startRow;
		int wgrow;
		int averagesRow = startRow + 2;
		int countWGs = 0;

		/* Print Communities */
		String comm;

		// reportWB.getSheet(isheet).setValue(1, startRow, "LZ", boldTopStyle);
		reportWB.getSheet(isheet).setValue(1, startRow, "Community", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, startRow, "Wealthgroup", boldTopStyle);

		// reportWB.getSheet(isheet).setValue(1, row, livelihoodZone.getLzname(),
		// textStyle);

		/* TODO LZ needs populating */

		System.out.println("In pop first three cols, nos of communities =  " + uniqueCommunity.size());

		for (WGI wgi2 : uniqueCommunity) {
			comm = wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict();
			reportWB.getSheet(isheet).setValue(1, row + 1, wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict(), textStyle);

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
						reportWB.getSheet(isheet).setValue(2, wgrow, wealthGroup.getWgnameeng(), textStyle);
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

			row = wgrow;

		}

	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

	private void createAdultEquivalentreport(int isheet, Report report) {
		/*
		 * int row = 1;
		 * 
		 * reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);
		 * 
		 * if (isQuantile) {
		 * 
		 * reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 30);
		 * 
		 * orderedQuantSeq =
		 * uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
		 * .collect(Collectors.toList());
		 * 
		 * quantAvg = orderedQuantSeq.stream().collect(
		 * Collectors.groupingBy(HH::getQuantSeq, TreeMap::new,
		 * Collectors.averagingDouble(HH::getHhAE)));
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, "Adult Daily Equivalent KCal",
		 * textStyle); row++;
		 * 
		 * for (Quantile quantile : quantiles) { reportWB.getSheet(isheet).setValue(1,
		 * row, quantile.getName(), textStyle); reportWB.getSheet(isheet).setValue(2,
		 * row, quantile.getPercentage(), textStyle); //
		 * reportWB.getSheet(isheet).setValue(3, row, quantile.getSequence(), //
		 * textStyle); reportWB.getSheet(isheet).setValue(3, row,
		 * fillDouble(quantAvg.get(quantile.getSequence())), textStyle); row++; }
		 * 
		 * } else { reportWB.getSheet(isheet).setColumnWidths(1, 20, 30);
		 * System.out.println("DI non quantile "); reportWB.getSheet(isheet).setValue(1,
		 * row, "Household Number", textStyle); reportWB.getSheet(isheet).setValue(2,
		 * row, "Adult Daily Equivalent KCal", textStyle); row++; for (HH hh2 :
		 * uniqueHousehold) {
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, hh2.hhAE, textStyle); row++; } }
		 */
	}

	/******************************************************************************************************************************************/

	// handle if Synonym is used and return base Kcal value
	private int findRSTKcal(ResourceSubType rst) {

		if (rst.getResourcesubtypesynonym() != null) {
			System.out.println("its a synonym");
			try {
				System.out.println("syn kcal = " + rst.getResourcesubtypesynonym().getResourcesubtypekcal());
				return (rst.getResourcesubtypesynonym().getResourcesubtypekcal());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addMessage("Cannot get Synonym KCal value");
				return (0);
			}

		}
		System.out.println("its not a synonym");
		return (rst.getResourcesubtypekcal());
	}

	/******************************************************************************************************************************************/

	private Double calcWFIncome(WGI wgi, String type, Boolean isAfterChangeScenario) {
		Double wfTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		/* What about food payments? */

		for (WildFood wf : wgi.getWealthgroupInterview().getWildFood()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}

			if (type == "cash") {
				wfTot += wf.getUnitsSold() * wf.getPricePerUnit() * priceChange;

			} else if (type == "food") {

				Double production = yieldChange * wf.getUnitsProduced();

				Double consumed = production - wf.getUnitsSold();

				wfTot += consumed * findRSTKcal(wf.getResourceSubType());
				// wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType()) *
				// yieldChange;

			}
		}

		return wfTot;
	}

	/******************************************************************************************************************************************/

	private Double calcWFIncome(HH hh2, String type, Boolean isAfterChangeScenario) {
		Double wfTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		/* What about food payments? */

		for (WildFood wf : hh2.getHousehold().getWildFood()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, wf.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}
			if (type == "cash") {
				wfTot += wf.getUnitsSold() * wf.getPricePerUnit() * priceChange;
			} else if (type == "food") {

				Double production = yieldChange * wf.getUnitsProduced();

				Double consumed = production - wf.getUnitsSold();

				wfTot += consumed * findRSTKcal(wf.getResourceSubType());

				// wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType()) *
				// yieldChange;
			}
		}
		if (wfTot.isNaN())
			return 0.0;
		else

			return wfTot;
	}

	/******************************************************************************************************************************************/

	private Double calcTransIncome(WGI wgi, String type, Boolean isAfterChangeScenario) {
		Double trTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		WealthGroupInterview wealthGroupInterview = wgi.getWealthgroupInterview();

		for (Transfer tr : wealthGroupInterview.getTransfer()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, tr.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, tr.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}
			if (tr.getTransferType() == null)
				break;

			if (type == "cash") {

				if (tr.getTransferType().equals(TransferType.Food)) {
					trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving() * tr.getTimesReceived()
							* priceChange;
				} else {
					trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
				}

			} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
					&& tr.getFoodResourceSubType() != null) {

				trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) * tr.getPeopleReceiving()
						* tr.getTimesReceived() * yieldChange;

			}
		}

		return trTot;
	}

	/******************************************************************************************************************************************/

	private Double calcTransIncome(HH hh2, String type, Boolean isAfterChangeScenario) {
		Double trTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		/* Handle transfer types food/cash/other */

		for (Transfer tr : hh2.getHousehold().getTransfer()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, tr.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, tr.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}
			if (type == "cash") {
				if (tr.getTransferType().equals(TransferType.Food)) {
					trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving() * tr.getTimesReceived()
							* priceChange;
				} else // Cash or Other
				{
					trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
				}
			} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
					&& tr.getFoodResourceSubType() != null) {

				trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) * tr.getPeopleReceiving()
						* tr.getTimesReceived() * yieldChange;

			}

		}
		if (trTot.isNaN())
			return 0.0;
		else

			return trTot;
	}

	/******************************************************************************************************************************************/

	private Double calcLSIncome(WGI wgi, String type, Boolean isAfterChangeScenario) { // LSS and LSP sales
		Double lsTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		System.out.println("in calcIncome type =" + type);

		for (LivestockProducts lsp : wgi.getWealthgroupInterview().getLivestockProducts()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}

			if (type == "cash") {

				lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit() * priceChange;

			}

			else if (type == "food") {

				Double production = yieldChange * lsp.getUnitsProduced();

				Double consumed = production - lsp.getUnitsSold();

				lsTot += consumed * findRSTKcal(lsp.getResourceSubType());

				// lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType()) *
				// yieldChange;

			}

		}

		for (LivestockSales lss : wgi.getWealthgroupInterview().getLivestockSales()) {
			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, lss.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, lss.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}

			if (type == "cash") {

				lsTot += lss.getUnitsSold() * lss.getPricePerUnit() * priceChange;

			}

		}

		return lsTot;

	}

	/******************************************************************************************************************************************/

	private Double calcLSIncome(HH hh2, String type, Boolean isAfterChangeScenario) {// LSS and LSP sales

		Double yieldChange = 0.0;
		Double priceChange = 0.0;
		Double lsTot = 0.0;

		if (type == "cash") {

			for (LivestockSales ls : hh2.getHousehold().getLivestockSales()) {

				if (isAfterChangeScenario) {

					priceChange = priceYieldVariation(modellingScenario, ls.getResourceSubType(), PRICE);
					yieldChange = priceYieldVariation(modellingScenario, ls.getResourceSubType(), YIELD);

				} else {

					priceChange = 1.0;
					yieldChange = 1.0;
				}

				lsTot += ls.getUnitsSold() * ls.getPricePerUnit() * priceChange;
			}

			for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {
				if (isAfterChangeScenario) {

					priceChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), PRICE);
					yieldChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), YIELD);

				} else {

					priceChange = 1.0;
					yieldChange = 1.0;
				}

				lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit() * priceChange;
			}
		} else if (type == "food") {
			for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {
				if (isAfterChangeScenario) {

					priceChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), PRICE);
					yieldChange = priceYieldVariation(modellingScenario, lsp.getResourceSubType(), YIELD);

				} else {

					priceChange = 1.0;
					yieldChange = 1.0;
				}

				Double production = yieldChange * lsp.getUnitsProduced();

				Double consumed = production - lsp.getUnitsSold();

				lsTot += consumed * findRSTKcal(lsp.getResourceSubType());

				// lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType()) *
				// yieldChange;
			}
		}

		if (lsTot.isNaN())
			return 0.0;
		else

			return lsTot;

	}

	/******************************************************************************************************************************************/

	private Double calcEmpIncome(WGI wgi, String type, Boolean isAfterChangeScenario) {

		Double empTot = 0.0;
		System.out.println("in calcEmpIncome type =" + type);

		for (Employment emp : wgi.getWealthgroupInterview().getEmployment()) {

			if (type == "cash") {

				empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();

			} else if (type == "food" && emp.getFoodResourceSubType() != null) {

				empTot += emp.getPeopleCount() * emp.getUnitsWorked() * findRSTKcal(emp.getFoodResourceSubType());
			} else {
				empTot = 0.0;
			}

		}

		return empTot;

	}

	/******************************************************************************************************************************************/

	private Double calcEmpIncome(HH hh2, String type, Boolean isAfterChangeScenario) {
		Double empTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		/* What about food payments? for cash */
		if (hh2.getHousehold().getEmployment().size() == 0) {

			return (0.0);
		}
		try {

			for (Employment emp : hh2.getHousehold().getEmployment()) {

				if (isAfterChangeScenario) {

					priceChange = priceYieldVariation(modellingScenario, emp.getResourceSubType(), PRICE);
					yieldChange = priceYieldVariation(modellingScenario, emp.getResourceSubType(), YIELD);

				} else {

					priceChange = 1.0;
					yieldChange = 1.0;
				}

				if (type == "cash") {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount() * priceChange;

				} else if (type == "food" && emp.getFoodResourceSubType() != null) {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * findRSTKcal(emp.getFoodResourceSubType())
							* yieldChange;
				} else
					empTot = 0.0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errno = 991;
		}

		if (empTot.isNaN())
			return 0.0;
		else
			return empTot;

	}

	/******************************************************************************************************************************************/
	private Double calcCropIncome(HH hh2, String type, Boolean isAfterChangeScenario) {

		Double cropTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		for (Crop crop : hh2.getHousehold().getCrop()) {

			Efdutils.em("isAfterChangeScenario = " + isAfterChangeScenario);

			if (isAfterChangeScenario) {

				Efdutils.em("in calcCropIncome rst = " + crop.getResourceSubType().getResourcetypename());
				priceChange = priceYieldVariation(modellingScenario, crop.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, crop.getResourceSubType(), YIELD);
				Efdutils.em("priceChange = " + priceChange);
				Efdutils.em("yieldChange = " + yieldChange);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}

			if (type == "cash") {

				cropTot += crop.getUnitsSold() * crop.getPricePerUnit() * priceChange;

			} else if (type == "food") {
				// ref Issue 413

				Double production = yieldChange * crop.getUnitsProduced();

				Double consumed = production - crop.getUnitsSold();

				cropTot += consumed * findRSTKcal(crop.getResourceSubType());

			}
		}

		if (cropTot.isNaN())
			return 0.0;
		else
			return cropTot;
	}

	/******************************************************************************************************************************************/

	private Double calcCropIncome(WGI wgi, String type, Boolean isAfterChangeScenario) {

		Double cropTot = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		for (Crop crop : wgi.getWealthgroupInterview().getCrop()) {

			if (isAfterChangeScenario) {

				priceChange = priceYieldVariation(modellingScenario, crop.getResourceSubType(), PRICE);
				yieldChange = priceYieldVariation(modellingScenario, crop.getResourceSubType(), YIELD);

			} else {

				priceChange = 1.0;
				yieldChange = 1.0;
			}

			if (type == "cash") {

				cropTot += crop.getUnitsSold() * crop.getPricePerUnit() * priceChange;

			} else if (type == "food") {
				Double production = yieldChange * crop.getUnitsProduced();

				Double consumed = production - crop.getUnitsSold();

				cropTot += consumed * findRSTKcal(crop.getResourceSubType());

			}
		}

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

		Efdutils.em("hh member in calcHHmSOLC " + hhm.getHouseholdMemberNumber() + " " + hhm.getAge() + " "
				+ hhm.getGender());

		if (genderSOL.equals(Gender.Both) && (hhm.getAge() >= lowerAgeSOL) && (hhm.getAge() <= upperAgeSOL)) {
			Efdutils.em("hh member stol both " + stdL.getCost() * stdL.getAmount());
			Efdutils.em(
					"hh member  both " + hhm.getHouseholdMemberNumber() + " " + hhm.getAge() + " " + hhm.getGender());
			return (stdL.getCost() * stdL.getAmount());

		}
		if (genderSOL.equals(Gender.Male) && hhm.getGender().equals(Sex.Male) && (hhm.getAge() >= lowerAgeSOL)
				&& (hhm.getAge() <= upperAgeSOL)) {
			Efdutils.em("hh member stol male " + stdL.getCost() * stdL.getAmount());
			Efdutils.em(
					"hh member  male " + hhm.getHouseholdMemberNumber() + " " + hhm.getAge() + " " + hhm.getGender());
			return (stdL.getCost() * stdL.getAmount());

		}

		if (genderSOL.equals(Gender.Female) && hhm.getGender().equals(Sex.Female) && (hhm.getAge() >= lowerAgeSOL)
				&& (hhm.getAge() <= upperAgeSOL)) {
			Efdutils.em("hh member stol female " + stdL.getCost() * stdL.getAmount());
			Efdutils.em(
					"hh member  female " + hhm.getHouseholdMemberNumber() + " " + hhm.getAge() + " " + hhm.getGender());
			return (stdL.getCost() * stdL.getAmount());

		}

		return (0.0);
	}

	/******************************************************************************************************************************************/

	private Double wealthgroupInterviewDI(WealthGroupInterview wealthGroupInterview, Boolean isChangeScenario) {

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
		Double employmentTI = 0.0;
		Double transfersTI = 0.0;

		Double cropOP = 0.0;
		Double wildfoodsOP = 0.0;
		Double lspOP = 0.0;
		Double employmentOP = 0.0;
		Double transfersOP = 0.0;
		Double yieldChange = 0.0;
		Double priceChange = 0.0;

		Double requiredCalories = 0.0;

		System.out.println("In WGI DI calc ");

		List<WGI> thisWGI = wgi.stream().filter(d -> d.getWealthgroupInterview() == wealthGroupInterview)
				.collect(Collectors.toList());

		List<WGI> cropList = thisWGI.stream().filter(d -> d.getType() == "Crop").collect(Collectors.toList());

		List<WGI> wildfoodList = thisWGI.stream().filter(d -> d.getType() == "Wildfood").collect(Collectors.toList());

		List<WGI> livestockproductList = thisWGI.stream().filter(d -> d.getType() == "LivestockProduct")
				.collect(Collectors.toList());

		List<WGI> employmentList = thisWGI.stream().filter(d -> d.getType() == "Employment")
				.collect(Collectors.toList());

		List<WGI> transferList = thisWGI.stream().filter(d -> d.getType() == "Transfer").collect(Collectors.toList());

		try {
			if (!cropList.isEmpty()) {

				for (WGI icrop : cropList) {

					// Crop crop = icrop.getCrop();

					if (isChangeScenario) {

						priceChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), PRICE);
						yieldChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), YIELD);

					} else {
						// Not in modelling scenario calc of DI
						priceChange = 1.0;
						yieldChange = 1.0;
					}

					cropTI += (icrop.getCrop().getUnitsSold().doubleValue()
							* icrop.getCrop().getPricePerUnit().doubleValue()) * priceChange;

					cropOP += (icrop.getCrop().getUnitsConsumed().doubleValue()
							* Double.valueOf(icrop.getCrop().getResourceSubType().getResourcesubtypekcal()))
							* yieldChange;

				}
			}

			if (!wildfoodList.isEmpty()) {
				for (WGI iwildfood : wildfoodList) {

					wildfoodsTI += (iwildfood.getWildfood().getUnitsSold().doubleValue()
							* iwildfood.getWildfood().getPricePerUnit().doubleValue());

					wildfoodsOP += (iwildfood.getWildfood().getUnitsConsumed().doubleValue()
							* Double.valueOf(iwildfood.getWildfood().getResourceSubType().getResourcesubtypekcal()));
				}
			}
			if (!livestockproductList.isEmpty()) {
				for (WGI ilivestockproduct : livestockproductList) {

					lspTI += (ilivestockproduct.getLivestockproducts().getUnitsSold()
							* ilivestockproduct.getLivestockproducts().getPricePerUnit());
					lspOP += (ilivestockproduct.getLivestockproducts().getUnitsConsumed().doubleValue()
							* Double.valueOf(ilivestockproduct.getLivestockproducts().getResourceSubType()
									.getResourcesubtypekcal()));
				}
			}
			if (!employmentList.isEmpty()) {
				for (WGI iemployment : employmentList) {

					employmentTI += (iemployment.getEmployment().getUnitsWorked()
							* iemployment.getEmployment().getCashPaymentAmount()
							* iemployment.getEmployment().getPeopleCount());

					/* handle null resource type */

					int empresourcekcal = 0;
					ResourceSubType resourcesubtypekcal = iemployment.getEmployment().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						empresourcekcal = 0;
					else
						empresourcekcal = iemployment.getEmployment().getFoodResourceSubType().getResourcesubtypekcal();

					employmentOP += iemployment.getEmployment().getUnitsWorked() * empresourcekcal;
				}
			}
			if (!transferList.isEmpty()) {
				for (WGI itransfer : transferList) {

					if (itransfer.getTransfer().getTransferType().toString().equals("Cash"))
						transfersTI += (itransfer.getTransfer().getCashTransferAmount()
								* itransfer.getTransfer().getPeopleReceiving()
								* itransfer.getTransfer().getTimesReceived());
					System.out.println("in DI transfer calc " + transfersTI);
					Double transresourcekcal = 0.0;
					ResourceSubType resourcesubtypekcal = itransfer.getTransfer().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						transresourcekcal = 0.0;
					else
						transresourcekcal = (double) itransfer.getTransfer().getFoodResourceSubType()
								.getResourcesubtypekcal();

					if (itransfer.getTransfer().getTransferType().toString().equals("Food"))
						transfersOP += (transresourcekcal * itransfer.getTransfer().getUnitsConsumed());
				}
			}
			System.out.println("done DI transfer calculation " + transfersTI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			addWarning("Error in WG DI Calculation ");
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
		System.out.println("nos of wgs = " + wghhsizeBD);

		if (wghhsizeBD != null) {
			wghhsize = wghhsize.doubleValue();
		} else {
			wghhsize = 0.0;
		}

		// wealthGroupInterview.getWealthgroup().getWghhsize().intValue();

		requiredCalories = wghhsize * energyNeed * 365.0;
		System.out.println("required Calories = " + requiredCalories);

		System.out.println("requiredCalories = " + requiredCalories);

		Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI + transfersTI;

		System.out.println("cropTI = " + cropTI);
		System.out.println("wfTI = " + wildfoodsTI);
		System.out.println("lspTI = " + lspTI);
		System.out.println("empTI = " + employmentTI);
		System.out.println("transfTI = " + transfersTI);

		Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

		Double shortFall = requiredCalories - output;

		System.out.println("totalIncome = " + totalIncome);
		System.out.println("output = " + output);

		// Now it gets more complex , but not difficult

		// Diet // Diet Value = Sum (KCal per KG * Percentage of the Food type in
		// default diet

		Double dietValue = 0.0;

		defaultDietItems = (List<DefaultDietItem>) wealthGroupInterview.getWealthgroup().getCommunity()
				.getDefaultDietItem();

		System.out.println("ddi list = " + defaultDietItems.size());

		// Any DDI?

		for (DefaultDietItem defaultDietItem : defaultDietItems) {
			dietValue += (defaultDietItem.getResourcesubtype().getResourcesubtypekcal()
					* defaultDietItem.getPercentage() / 100);
		}

		// Diet Amount Purchased DA = Shortfall / Diet Value in KGs
		Double dietAmountPurchased = 0.0;

		dietAmountPurchased = shortFall / dietValue;

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

	private Double wealthgroupInterviewAE(WealthGroupInterview wealthGroupInterview) {

		System.out.println("in wealthgroupInterview Adult Equivalent calc = ");

		Double totAE = 0.0;
		Double ageReq = 0.0;
		int age = 0;
		Sex gender;

		// TODO
		/*
		 * for (HouseholdMember hhm : household.getHouseholdMember()) { age =
		 * hhm.getAge(); gender = hhm.getGender();
		 * 
		 * WHOEnergyRequirements whoEnergy = WHOEnergyRequirements.findByAge(age);
		 * 
		 * // System.out.println("whoEnergy = " + whoEnergey.getFemale() + " " + //
		 * whoEnergey.getMale() + " " + gender); if (gender == Sex.Female) { totAE +=
		 * whoEnergy.getFemale(); } else if (gender == Sex.Male) { totAE +=
		 * whoEnergy.getMale(); }
		 * 
		 * }
		 */
		/* AE = TE / 2600 */

		totAE = totAE / 2600;
		// System.out.println("done householdAE calc");
		return Double.parseDouble(df2.format(totAE));

	}

	/******************************************************************************************************************************************/

	private Double householdAE(Household household) {

		System.out.println("in householdAE calc, household = " + household.getHouseholdName());

		Double totAE = 0.0;
		Double ageReq = 0.0;
		int age = 0;
		Sex gender;

		for (HouseholdMember hhm : household.getHouseholdMember()) {
			age = hhm.getAge();
			gender = hhm.getGender();

			WHOEnergyRequirements whoEnergy = WHOEnergyRequirements.findByAge(age);

			// System.out.println("whoEnergy = " + whoEnergey.getFemale() + " " +
			// whoEnergey.getMale() + " " + gender);
			if (gender == Sex.Female) {
				totAE += whoEnergy.getFemale();
			} else if (gender == Sex.Male) {
				totAE += whoEnergy.getMale();
			}

		}

		/* AE = TE / 2600 */

		totAE = totAE / 2600;
		// System.out.println("done householdAE calc");
		return Double.parseDouble(df2.format(totAE));

	}

	/******************************************************************************************************************************************/

	private void createHeaderPage() {
		final int STARTROW = 6;
		int i = STARTROW; // used for row number
		String currency = "";
		int col = 3;
		String siteName = "";
		String wgName = "";
		Method modellingreports = null;

		Efdutils.em("In Create Header Page for Coping Strategy ");
		Efdutils.em("IS OHEA = " + isOHEA);
		Efdutils.em("IS OIHM = " + isOIHM);

		if (isCopingStrategy)
			sheet[0] = reportWB.addSheet("Coping Strategy Summary");
		else
			sheet[0] = reportWB.addSheet("Change Scenario Summary");

		setSheetStyle(sheet[0]);
		sheet[0].setColumnWidths(1, 22, 50, 30, 30, 30);

		sheet[0].setValue(1, 1, "Date:", boldRStyle); // col,row
		sheet[0].setValue(2, 1, new Date(), dateStyle);
		sheet[0].setValue(1, 2, "Spec Name:", boldRStyle);
		sheet[0].setValue(2, 2, "All Reports", textStyleLeft);
		sheet[0].setValue(1, 4, "Reporting Currency:", boldRStyle);

		sheet[0].setValue(1, 13, "Change Scenario Title:", boldRStyle);
		sheet[0].setValue(1, 14, "Change Scenario Author:", boldRStyle);
		sheet[0].setValue(1, 15, "Change Scenario Description:", boldRStyle);

		sheet[0].setValue(2, 13, modellingScenario.getTitle(), textStyleLeft);
		sheet[0].setValue(2, 14, modellingScenario.getAuthor(), textStyleLeft);
		sheet[0].setValue(2, 15, modellingScenario.getDescription(), textStyleLeft);

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
					sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyle);
					i++;
				}
			} else {
				sheet[0].setValue(col, i, "Report Spec Households in Report = " + uniqueHousehold.size(), boldTopStyle);
				i++;
				for (HH hh2 : uniqueHousehold) {
					if ((hhName = hh2.getHousehold().getHouseholdName()) == null)
						hhName = "-";
					sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyle);
					i++;
				}
			}

			// Default reporting currency based on Currency for an LZ within this Project
			for (LivelihoodZone livelihoodZone3 : study.getProjectlz().getLivelihoodZone()) {
				currency = livelihoodZone3.getCountry().getCurrencySymbol();
				sheet[0].setValue(2, 4, currency, textStyleLeft);
				break;
			}

			/* Which set of reports to Run */
			if (isCopingStrategy) {
				modellingreports = Method.MODELLINGOIHMCOPING;
			} else {
				modellingreports = Method.MODELLINGOIHMSCENARIO;
			}

		} else if (isOHEA) {
			sheet[0].setValue(1, 3, "Project:", boldRStyle);
			sheet[0].setValue(2, 3, project.getProjecttitle(), textStyleLeft);
			sheet[0].setValue(3, STARTROW, "Community/Sites", boldRStyle);
			sheet[0].setValue(4, STARTROW, "Wealthgroup", boldRStyle);

			/* Which set of reports to Run */
			if (isCopingStrategy) {
				modellingreports = Method.MODELLINGOHEACOPING;
			} else {
				modellingreports = Method.MODELLINGOHEASCENARIO;
			}

			// Default reporting currency based on Currency for an LZ within this Project

			Optional<LivelihoodZone> firstLZ = project.getLivelihoodZone().stream().findFirst();
			currency = firstLZ.get().getCountry().getCurrency();
			sheet[0].setValue(2, 4, currency, textStyleLeft);

			i = STARTROW + 1;
			for (Site site : sites) {
				siteName = site.getLocationdistrict() + " " + site.getSubdistrict();
				sheet[0].setValue(col, i, siteName, textStyle);
				for (Community community2 : site.getCommunity()) {
					for (WealthGroup wealthGroup : community2.getWealthgroup()) {
						wgName = wealthGroup.getWgnameeng() + " " + wealthGroup.getWgnamelocal();
						sheet[0].setValue(col + 1, i, wgName, textStyle);
						i++;
					}
				}

				i++;

			}

		}

		reportList = XPersistence.getManager().createQuery("from Report where method = :method")
				.setParameter("method", modellingreports).getResultList();

		sheet[0].setValue(2, STARTROW, "Reports", boldLStyle);

		/* get list of reports for modelling data spreadsheet */

		Efdutils.em("No of reports = " + reportList.size());

		i = STARTROW + 1;

		reportList.sort(Comparator.comparingInt(Report::getCode));

		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyleLeft);
			sheet[i - 3] = reportWB.addSheet(report.getCode() + " " + report.getName());

			setSheetStyle(sheet[i - 3]);

			i++;
		}

		errno = 1108;
	}

	/******************************************************************************************************************************************/

	private void setSheetStyle(JxlsSheet sheet) {
		sheet.setColumnWidths(1, 20, 60);

	}

	/******************************************************************************************************************************************/

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/******************************************************************************************************************************************/

	private void setStyles() {

		reportWB.setIntegerFormat(integerFormat);
		reportWB.setFloatFormat(floatFormat);
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldLStyle = reportWB.addStyle(TEXT).setBold().setAlign(LEFT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleLeft = reportWB.addStyle(TEXT).setAlign(LEFT);
		textStyleBlue = reportWB.addStyle(TEXT).setAlign(LEFT).setTextColor(BLUE);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(LEFT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT);

		numberd0 = reportWB.addStyle(INTEGER);
		f1Style = reportWB.addStyle("0.0");

	}

	/******************************************************************************************************************************************/

	@Override
	public String getForwardURI() {
		return forwardURI;
	}

	/******************************************************************************************************************************************/

	@Override
	public boolean inNewWindow() {
		if (forwardURI == null)
			return false;
		return true;
	}

	/******************************************************************************************************************************************/

	public void setForwardURI(String forwardURI) {
		this.forwardURI = forwardURI;
	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

}