
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
import java.util.function.*;
import java.util.stream.*;

import org.apache.commons.lang.*;
import org.apache.poi.ss.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.actions.OIHMReports.*;
import efd.model.*;
import efd.model.HouseholdMember.*;
import efd.model.Project.*;
import efd.model.Report.*;
import efd.model.StdOfLivingElement.*;
import efd.model.Transfer.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;
import efd.utils.HH;
import efd.utils.QuantHousehold;

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
	JxlsStyle numberd2 = null;

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
	Boolean isSelectedHouseholds = false;
	Boolean isValid = false;
	Boolean isChangeScenario = false;

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	int numCommunities = 0;

	// ArrayList<ArrayList<Double>> averageTotal = new
	// ArrayList<>(NUMBEROFAVERAGES);

	Double[][] averageTotal = new Double[3][NUMBEROFAVERAGES];

	private String floatFormat = "##########0.00";

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run Modelling Reports ");
		int countValidated = 0;
		String selectionView;
		WealthGroupInterview wealthGroupInterview = null;
		Site site = null;
		int ddiTotPercent = 0;

		/*
		 * Get Change Scenario Details
		 * 
		 */
		String modellingScenarioId = getPreviousView().getValueString("id");
		modellingScenario = XPersistence.getManager().find(ModellingScenario.class, modellingScenarioId);

		Efdutils.em("Mod scenario = " + modellingScenario.getTitle() + " " + modellingScenarioId);

		/* Need to determine if for OHEA or OIHM (LZ or HH) */

		Map allValues = getView().getAllValues();

		if (modellingScenario.getStudy() != null) {
			isOIHM = true; // OIHM Study and HH

			selectionView = "study.household";
			study = modellingScenario.getStudy();

		}

		else {
			isOHEA = true; // OHEA Community and LZ
			addError("OHEA Project Report not implemented yet");

			selectionView = "livelihoodZone.lzid";

			project = modellingScenario.getProject();

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
				System.out.println("key = " + key.toString());
				String subKey = key.toString().substring(12, 44);
				System.out.println("subkey = " + subKey.toString());
				site = XPersistence.getManager().find(Site.class, subKey);
				System.out.println("site = " + site.toString());

				// add to array for Sites/Communities
				Site s = new Site();
				s = site;
				sites.add(s);

				// From Site get Communities/Sites then get WG and WGI

				for (Community community2 : site.getCommunity()) {
					countValidated = 0;

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

			}

			// Populate WGI array wgis - use dialog selected list if enter

			populateWGIArray(wgiList);

			uniqueCommunity = wgi.stream().filter(distinctByKey(WGI::getCommunity)).collect(Collectors.toList());

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

			calculateDI(); // uses wgi filtered array based on CRS definition
			System.out.println("done calc DI");
			calculateAE(); // Calculate the Adult equivalent
			System.out.println("done calc AE");

		} else if (selectedOnes.length != 0 && isOIHM) { // OIHM

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

			if (households.size() == 0) {
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

			for (WealthGroupInterview wgi2 : wgiList) {
				wgiDI = wealthgroupInterviewDI(wgi2);
				System.out.println(
						"DI = " + wgi2.getWealthgroup().getCommunity().getSite().getSubdistrict() + " " + wgiDI);
				System.out.println("DI WG = " + wgi2.getWealthgroup().getWgnameeng());

				wgi2.getWealthgroup().setDefaultDI(wgiDI);

			}
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

		int i = 0;
		int yieldChange = 0;
		int priceChange = 0;

		try {
			if (cropList.size() > 0) {

				for (HH icrop : cropList) {

					// Crop crop = icrop.getCrop();

					if (isChangeScenario) {

						priceChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), PRICE);
						yieldChange = priceYieldVariation(modellingScenario, icrop.getResourceSubType(), YIELD);

					} else {
						// Not in modelling scenario calc of DI
						priceChange = 1;
						yieldChange = 1;
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
			addError("Error in DI Calculation " + e);
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

	private int priceYieldVariation(ModellingScenario modellingScenario2, ResourceSubType resourceSubType,
			int type) { /* Type PRICE or YIELD */

		Optional<PriceYieldVariation> pyc;
		pyc = priceYieldVariations.stream().filter(p -> p.getResource() == resourceSubType).findAny();

		if (pyc.isPresent()) {

			Efdutils.em("found a valid pyc = " + pyc.get().getResource().getResourcetypename());

			if (type == PRICE) {
				if (pyc.get().getPrice() == 0)
					return 1;
				else
					return pyc.get().getPrice();

			} else if (type == YIELD) {
				if (pyc.get().getYield() == 0)
					return 1;
				else
					return pyc.get().getYield();
			}

		}

		return 1;
	}

	/******************************************************************************************************************************************/
	private int filterWGI(CustomReportSpecOHEA customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from wg populate array
		// Filter out reportspecuse non included wg if exist and for same Community

		System.out.println("filter 000");

		if (customReportSpec.getCategory().size() > 0) // Apply Category Filter
		{
			System.out.println("in Cat filter");
			List<WGI> wgCAT = wgi.stream().filter(p -> p.getCategory() != null).collect(Collectors.toList());

			for (Category category : customReportSpec.getCategory()) {
				for (WGI wg2 : wgCAT) {
					if (wg2.getResourceSubType().getCategory() == category) {

						wg2.setDelete(false);
					} else {

						wg2.setDelete(true);
					}
				}

			}

		}

		if (customReportSpec.getResourceType().size() > 0) // Apply RST Filter

		{
			System.out.println("in RT filter");
			List<WGI> wgRT = wgi.stream().filter(p -> p.getResourceType() != null).collect(Collectors.toList());
			System.out.println("After RT size  wg2 = " + wgRT.size());

			for (WGI wg2 : wgRT) {

				for (ResourceType resourceType : customReportSpec.getResourceType()) {

					if (resourceType != wg2.getResourceType()) {

						wg2.setDelete(true);
					}

					else {

						wg2.setDelete(false);
						break;
					}

				}
			}

		}

		System.out.println("After RT wgi = " + wgi.size());
		if (customReportSpec.getResourceSubType().isEmpty()) // Apply RST Filter
		{
			System.out.println("in RST filter");
			List<WGI> wgRST = wgi.stream().filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

			for (WGI wg2 : wgRST) {

				for (ResourceSubType resourceSubType : customReportSpec.getResourceSubType()) {
					if (resourceSubType.getIdresourcesubtype() != wg2.getResourceSubType().getIdresourcesubtype()) {

						wg2.setDelete(true);
					} else {

						wg2.setDelete(false);
						break;
					}
				}

			}

		}

		try {
			wgi.removeIf(n -> n.getDelete() == true);
		} catch (Exception e) {
			System.out.println("nothing filtered to remove");

		}

//		wgi.stream().forEach(p -> System.out.println("wgi post filter = "+p.community.getSite().getSubdistrict()+
//				" "+p.wealthgroup.getWgnameeng()+
//				" "+p.getLand().getResourceSubType().getResourcetypename()));

		return wgi.size();

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

		for (AssetLand asset : wealthGroupInterview.getAssetLand()) {

			System.out.println("assetland = " + asset.getResourceSubType().getResourcetypename());
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

		setStyles();

		createHeaderPage(); // populates reportList

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

				createOIHMDIreport(ireportNumber, report);
				break;
			case 411:

				createOIHMDIAfterSOLreport(ireportNumber, report);
				break;
			case 412:

				 createIncomereport(ireportNumber, report, "cash", OIHM);
				break;
			case 413:

				 createIncomereport(ireportNumber, report, "food", OIHM);
				break;

			case 418:

				// createLandAssetreport(ireportNumber, report);
				break;

			case 419:

				// createLivestockAssetreport(ireportNumber, report);
				break;

			case 420:

				// createHHMemberreport(ireportNumber, report);
				break;
			case 421:

				// createAdultEquivalentreport(ireportNumber, report);
				break;

			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/
	private void createOIHMDIreport(int isheet, Report report) {

		int row = 1;

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);

		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "DI As Reported", textStyle);
		reportWB.getSheet(isheet).setValue(3, row, "DI After Change Scenario", textStyle);
		row++;

		Efdutils.em("uniqueHH = " + uniqueHousehold.size());
		for (HH hh2 : uniqueHousehold) {

			reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
			reportWB.getSheet(isheet).setValue(2, row, hh2.getHhDI(), textStyle);
			reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDIAfterChangeScenario(), textStyle);
			row++;

		}
	}

	/******************************************************************************************************************************************/

	private void createOHEADIreport(int isheet, Report report) {

		int row = 7;
		int i = 0;
		double hhSize = 0;

		System.out.println("in 366 report ");
		/*
		 * Will need to revisit average total array if WGs per community increase
		 */

		averageReset();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 15, 15, 20);

		// populateFirstThreeColumns(isheet, 1);

		reportWB.getSheet(isheet).setValue(4, 1, "Family Group Size", boldTopStyle);
		reportWB.getSheet(isheet).setValue(5, 1, "Disposable Income", boldTopStyle);

		// NOTE OHEA ordered by wg Number order not DI

		for (WGI wgi2 : uniqueCommunity) {

			orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
					.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
					.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

			for (i = 0; i < orderedWealthgroups.size(); i++) {

				WealthGroup thisWealthGroup = orderedWealthgroups.get(i);

				// Handle null hhsize

				List<WealthGroupInterview> wealthGroupInterview3 = orderedWealthgroups.get(i).getWealthGroupInterview();

				for (int k = 0; k < wealthGroupInterview3.size(); k++) {
					if (wealthGroupInterview3.get(k).getWgAverageNumberInHH() == null) {

						hhSize = 0;
					} else {
						hhSize = wealthGroupInterview3.get(k).getWgAverageNumberInHH();
					}

				}

				reportWB.getSheet(isheet).setValue(4, row, hhSize, textStyle);

				averageTotal[i][0] = hhSize + averageTotal[i][0];

				reportWB.getSheet(isheet).setValue(5, row, orderedWealthgroups.get(i).getDefaultDI(), textStyle);

				averageTotal[i][1] = orderedWealthgroups.get(i).getDefaultDI() + averageTotal[i][1];

				row++;
			}

		}
		errno = 3666;
		row = 3;
		for (i = 0; i < orderedWealthgroups.size(); i++) {
			reportWB.getSheet(isheet).setValue(4, row, averageTotal[i][0] / uniqueCommunity.size(), numberStyle);
			reportWB.getSheet(isheet).setValue(5, row, averageTotal[i][1] / uniqueCommunity.size(), numberStyle);
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

	private Double fillDouble(Double val) {
		if (val == null || val.isNaN())
			val = 0.0;
		val = Double.parseDouble(df2.format(val));
		return val;
	}

	/******************************************************************************************************************************************/

	private void createOHEADIAfterSOLreport(int isheet, Report report) {
		int row = 1;
		int i = 0;
		double hhSOLC = 0.0;
		double hhSize = 0.0;
		double wgSOLInclusion = 0.0;
		double wgSOLSurvival = 0.0;

		averageReset();
		System.out.println("in 367 report drb");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 15, 20, 20, 15, 22, 20);

		populateFirstThreeColumns(isheet, 1);

		reportWB.getSheet(isheet).setValue(4, row, "Disposable Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(5, row, "Family Group Size", boldTopStyle);
		reportWB.getSheet(isheet).setValue(6, row, "KCal Requirement", boldTopStyle);
		reportWB.getSheet(isheet).setValue(7, row, "Social Inclusion SToL", boldTopStyle);
		reportWB.getSheet(isheet).setValue(8, row, "Survival SToL", boldTopStyle);

		row = 7;
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
								* (stdOfLivingElement.getSurvival() / 100) * hhSize;

					}

				}
				System.out.println("in DI STOL uc loop Output ");
				reportWB.getSheet(isheet).setValue(4, row, orderedWealthgroups.get(i).getDefaultDI(), textStyle);
				reportWB.getSheet(isheet).setValue(5, row, hhSize, textStyle);
				reportWB.getSheet(isheet).setValue(6, row, hhSize * RC, textStyle);
				reportWB.getSheet(isheet).setValue(7, row, wgSOLInclusion, textStyle);
				reportWB.getSheet(isheet).setValue(8, row, wgSOLSurvival, textStyle);
				row++;
				System.out.println("in DI STOL uc loop  done OUTPUT");

				// Averages

				// averageTotal.get(i).set(0, hhSize+averageTotal.get(i).get(0));

				averageTotal[i][0] = orderedWealthgroups.get(i).getDefaultDI() + averageTotal[i][0];
				averageTotal[i][1] = hhSize + averageTotal[i][1];
				averageTotal[i][2] = (hhSize * RC) + averageTotal[i][2];
				averageTotal[i][3] = wgSOLInclusion + averageTotal[i][3];
				averageTotal[i][4] = wgSOLSurvival + averageTotal[i][4];

				System.out.println("");

			}
		}
		// Print Averages

		for (int col = 0; col < 5; col++) {
			row = 3;
			for (i = 0; i < 3; i++) {
				reportWB.getSheet(isheet).setValue(4 + col, row, averageTotal[i][col] / uniqueCommunity.size(),
						numberStyle);
				row++;
			}
		}
		errno = 109;
	}

	/******************************************************************************************************************************************/

	private void createOIHMDIAfterSOLreport(int isheet, Report report) {
		int row = 1;
		Double hhSOLC = 0.0;

		Efdutils.em("In OIHM DISOL report");

		// uniqeHousehold = number of households in array that are relevant
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 30, 30);
		for (HH hh3 : uniqueHousehold) {

			hhSOLC = 0.0;
			for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {

				if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {

					hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());
				} else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {
					errno = 104;
					for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {

						hhSOLC += calcHhmSolc(hh3, stdOfLivingElement);
						errno = 105;
					}

				}

			}

			hh3.setHhSOLC(hhSOLC);

		}

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Std of Living Reqt", textStyle);
		reportWB.getSheet(isheet).setValue(3, row, "Std of Living Reqt After Change Scenario", textStyle);
		reportWB.getSheet(isheet).setValue(4, row, "DI With StoL as Reported", textStyle);
		reportWB.getSheet(isheet).setValue(5, row, "DI With StoL as Reported After Change Scenario", textStyle);

		row++;
		row++;
		for (HH hh2 : uniqueHousehold) {

			reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
			reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), textStyle);
			reportWB.getSheet(isheet).setValue(3, row, hh2.getHhSOLCAfterChangeScenario(), textStyle);
			/*
			 * TODO
			 */
			reportWB.getSheet(isheet).setValue(4, row, 0, textStyle);
			reportWB.getSheet(isheet).setValue(5, row, 0, textStyle);

			row++;
		}

	}

	/******************************************************************************************************************************************/
	private void createIncomereport(int isheet, Report report, String type, int model) {
		int row = 1;
		// String type = "food" or "cash"
		// Model OHEA or OIHM

		Double cropIncome = 0.0;
		Double empIncome = 0.0;
		Double lsIncome = 0.0;
		Double trIncome = 0.0;
		Double wfIncome = 0.0;
		int col = 0;

		String initType = StringUtils.capitalize(type);

		System.out.println("in  income report");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30, 30, 30, 30, 30,30, 30, 30, 30, 30, 30);
		errno = 2262;

		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);

		col = 2;

		reportWB.getSheet(isheet).setValue(col++, row, "Crop "+type+" Income " + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Crop "+type+" Income After Change Scenario" + initType + " Income",
				textStyle);

		reportWB.getSheet(isheet).setValue(col++, row, "Employment "+type+" Income" + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row,
				"Employment "+type+" Income After Change Scenario" + initType + " Income", textStyle);

		reportWB.getSheet(isheet).setValue(col++, row, "Livestock "+type+" Income" + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row,
				"Livestock "+type+" Income After Change Scenario" + initType + " Income", textStyle);

		reportWB.getSheet(isheet).setValue(col++, row, "Transfer "+type+" Income" + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row,
				"Transfer "+type+" Income After Change Scenario" + initType + " Income", textStyle);

		reportWB.getSheet(isheet).setValue(col++, row, "Wildfood "+type+" Income" + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row,
				"Wildfood "+type+" Income After Change Scenario" + initType + " Income", textStyle);

		reportWB.getSheet(isheet).setValue(col++, row, "Total "+type+" Income" + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Total "+type+" Income After Change Scenario" + initType + " Income",
				textStyle);

		row = 2;

		if (model == OIHM) {

			for (HH hh2 : uniqueHousehold) {

				cropIncome = calcCropIncome(hh2, type);
				empIncome = calcEmpIncome(hh2, type);
				lsIncome = calcLSIncome(hh2, type);
				trIncome = calcTransIncome(hh2, type);
				wfIncome = calcWFIncome(hh2, type);

			}
		} else if (model == OHEA) {
			for (WGI wgi2 : uniqueCommunity) {

				orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
						.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
						.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

				for (int i = 0; i < orderedWealthgroups.size(); i++) {

					List<WealthGroupInterview> wealthGroupInterview = orderedWealthgroups.get(i)
							.getWealthGroupInterview();

					cropIncome = calcCropIncome(wealthGroupInterview, type);
					empIncome = calcEmpIncome(wealthGroupInterview, type);
					lsIncome = calcLSIncome(wealthGroupInterview, type);
					trIncome = calcTransIncome(wealthGroupInterview, type);
					wfIncome = calcWFIncome(wealthGroupInterview, type);

				}
			}
		}

		/* TODO need a print loop */
		// reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber, textStyle);
		reportWB.getSheet(isheet).setValue(2, row, cropIncome, textStyle);
		reportWB.getSheet(isheet).setValue(3, row, empIncome, textStyle);
		reportWB.getSheet(isheet).setValue(4, row, lsIncome, textStyle);
		reportWB.getSheet(isheet).setValue(5, row, trIncome, textStyle);
		reportWB.getSheet(isheet).setValue(6, row, wfIncome, textStyle);
		row++;

	}

	/******************************************************************************************************************************************/


	/******************************************************************************************************************************************/
	private void createLandAssetreport(int isheet, Report report) {

		int col = 4;
		int datarow = 10;
		int avgrow = 6;
		double totalLand = 0.0;
		int assetTypeCounter = 0;
		int startRow = 4;

		averageReset();
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 20, 20, 20, 20, 20, 20);
		populateFirstThreeColumns(isheet, startRow);

		reportWB.getSheet(isheet).setValue(1, 2, "Unit of Measure", boldTopStyle);

		// get Default Area in use

		Area areaMeasurement = project.getAreaMeasurement();

		if (areaMeasurement == null) {
			areaMeasurement = Area.Acre;
		}

		reportWB.getSheet(isheet).setValue(2, 2, areaMeasurement.toString(), textStyle);

		errno = 2273;

		// Populate hhLand array for matrix

		numCommunities = uniqueCommunity.size(); // Used for averages

		double thisAverageTotal = 0.0;

		// Need all Land RST types that remain in wgi array
		// wgiLandRST has array of unique LAND RST

		List<WGI> wgiLandRST = wgi.stream().filter(p -> p.getLand() != null)
				.filter(distinctByKey(p -> p.getLand().getResourceSubType())).collect(Collectors.toList());

		List<WealthGroup> orderedWealthgroups2;

		for (WGI wgi3 : wgiLandRST) {

			String communityID = wgi3.getCommunity().getCommunityid();
			reportWB.getSheet(isheet).setValue(col, 1, "Asset Category", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 2, "Land", textStyle);
			reportWB.getSheet(isheet).setValue(col, 3, "Asset Type", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 4, wgi3.getLand().getResourceSubType().getResourcetypename(),
					textStyle);

			/* Now work through Communities and Wealthgroups for this Land RST */

			for (WGI wgi2 : uniqueCommunity) {

				// System.out.println("this community = " + wgi2.getSite().getLocationdistrict()
				// + " "
				// + wgi2.getSite().getSubdistrict());

				orderedWealthgroups2 = wgi2.getCommunity().getWealthgroup().stream()
						.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
						.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

				Iterator<WealthGroup> owgiter = orderedWealthgroups2.iterator();

				int wgcount = 0;
				while (owgiter.hasNext()) {
					WealthGroup owgit = owgiter.next();
					List<WealthGroupInterview> wealthGroupInterview = owgit.getWealthGroupInterview();
					for (int i = 0; i < wealthGroupInterview.size(); i++) {
						for (AssetLand assetLand : wealthGroupInterview.get(i).getAssetLand()) {
							// System.out.println("test assetLand " +
							// assetLand.getResourceSubType().getResourcetypename()
							// + " " + wgi3.getResourceSubType().getResourcetypename());

							if (assetLand.getResourceSubType() == wgi3.getResourceSubType()) {

								totalLand += assetLand.getNumberOfUnits();

							}

						}

						reportWB.getSheet(isheet).setValue(col, datarow, totalLand, textStyle);

						averageTotal[wgcount][assetTypeCounter] = totalLand + averageTotal[wgcount][assetTypeCounter];

						wgcount++;

						totalLand = 0.0;
						i++;
						datarow++;
					}

				}

			}

			// print averages

			for (int j = 0; j < 3; j++) {

				reportWB.getSheet(isheet).setValue(col, avgrow + j,
						averageTotal[j][assetTypeCounter] / uniqueCommunity.size(), numberStyle);

			}

			datarow = 10;
			col++;
			assetTypeCounter++;
		}

	}

	/******************************************************************************************************************************************/

	private void populateFirstThreeColumns(int isheet, int startRow) {
		int row = startRow + 6;
		int wgrow;
		int averagesRow = startRow + 2;
		int countWGs = 0;

		/* Print Communities */
		String comm;

		reportWB.getSheet(isheet).setValue(1, startRow, "LZ", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, startRow, "Community", boldTopStyle);
		reportWB.getSheet(isheet).setValue(3, startRow, "Wealthgroup", boldTopStyle);

		// Place Averages Here
		reportWB.getSheet(isheet).setValue(2, averagesRow, "Averages", boldTopStyle);

		reportWB.getSheet(isheet).setValue(3, averagesRow, "Poor", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 1, "Middle", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 2, "Better Off", textStyle);

		reportWB.getSheet(isheet).setValue(1, row, livelihoodZone.getLzname(), textStyle);

		System.out.println("In pop first three cols, nos of communities =  " + uniqueCommunity.size());

		for (WGI wgi2 : uniqueCommunity) {
			comm = wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict();
			reportWB.getSheet(isheet).setValue(2, row, wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict(), textStyle);

			/* Print Wealthgroups within Community */
			/* Order by WG Order */

			wgi2.getCommunity().getWealthgroup();

			String communityID = wgi2.getCommunity().getCommunityid();
			orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
					.filter(p -> p.getCommunity().getCommunityid() == communityID)
					.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

			wgrow = row;

			for (WealthGroup wealthGroup : orderedWealthgroups) {

				for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {

					if (wealthGroupInterview.getStatus() == Status.Validated) {
						reportWB.getSheet(isheet).setValue(3, wgrow, wealthGroup.getWgnameeng(), textStyle);
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
			// System.out.println("done populate displayWGs size = " +
			// displayWealthgroup.size());
			isDisplayWealthgroupDone = true;

			row = wgrow;

		}

		// Display WG details

		// for (int l = 0; l < displayWealthgroup.size(); l++) {
		// System.out.println("display wg = " +
		// displayWealthgroup.get(l).getWealthGroupInterview().size());

		// }

	}

	/******************************************************************************************************************************************/

	private void createLivestockAssetreport(int isheet, Report report) {
		int col = 4;
		int datarow = 10;
		int avgrow = 6;
		double totalLS = 0.0;
		int assetTypeCounter = 0;

		averageReset();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 20, 20, 20, 20, 20, 20);

		System.out.println("uc = " + uniqueCommunity.size());
		int startRow = 4;
		populateFirstThreeColumns(isheet, startRow);

		// Populate hhLand array for matrix

		numCommunities = uniqueCommunity.size(); // Used for averages

		double thisAverageTotal = 0.0;

		// Need all Livestock RST types that remain in wgi array
		// wgiLSRST has array of unique LAND RST

		List<WGI> wgiLSRST = wgi.stream().filter(p -> p.getLivestock() != null)
				.filter(distinctByKey(p -> p.getLivestock().getResourceSubType())).collect(Collectors.toList());

		List<WealthGroup> orderedWealthgroups2;

		for (WGI wgi3 : wgiLSRST) {
			String communityID = wgi3.getCommunity().getCommunityid();
			reportWB.getSheet(isheet).setValue(col, 1, "Asset Category", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 2, "Livestock", textStyle);
			reportWB.getSheet(isheet).setValue(col, 3, "Asset Type", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 4, wgi3.getLivestock().getResourceSubType().getResourcetypename(),
					textStyle);

			/* Now work through Communities and Wealthgroups for this Land RST */

			for (WGI wgi2 : uniqueCommunity) {

				// System.out.println("this community = " + wgi2.getSite().getLocationdistrict()
				// + " "
				// + wgi2.getSite().getSubdistrict());

				orderedWealthgroups2 = wgi2.getCommunity().getWealthgroup().stream()
						.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
						.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

				Iterator<WealthGroup> owgiter = orderedWealthgroups2.iterator();

				int wgcount = 0;
				while (owgiter.hasNext()) {
					WealthGroup owgit = owgiter.next();
					List<WealthGroupInterview> wealthGroupInterview = owgit.getWealthGroupInterview();
					for (int i = 0; i < wealthGroupInterview.size(); i++) {
						for (AssetLiveStock assetLivestock : wealthGroupInterview.get(i).getAssetLiveStock()) {

							if (assetLivestock.getResourceSubType() == wgi3.getResourceSubType()) {

								totalLS += assetLivestock.getNumberOwnedAtStart();

							}

						}

						reportWB.getSheet(isheet).setValue(col, datarow, totalLS, textStyle);

						averageTotal[wgcount][assetTypeCounter] = totalLS + averageTotal[wgcount][assetTypeCounter];

						wgcount++;

						totalLS = 0.0;
						i++;
						datarow++;
					}

				}

			}

			// print averages

			for (int j = 0; j < 3; j++) {

				reportWB.getSheet(isheet).setValue(col, avgrow + j,
						averageTotal[j][assetTypeCounter] / uniqueCommunity.size(), numberStyle);

			}

			datarow = 10;
			col++;
			assetTypeCounter++;

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

	private Double calcWFIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double wfTot = 0.0;

		/* What about food payments? */

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (WildFood wf : wealthGroupInterview.get(i).getWildFood()) {
				if (type == "cash") {
					wfTot += wf.getUnitsSold() * wf.getPricePerUnit();

				} else if (type == "food") {

					wfTot += wf.getUnitsConsumed() * wf.getResourceSubType().getResourcesubtypekcal();
					wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType());

				}
			}

		}

		return wfTot;
	}
	/******************************************************************************************************************************************/

	private Double calcWFIncome(HH hh2, String type) {
		Double wfTot = 0.0;

		/* What about food payments? */

		for (WildFood wf : hh2.getHousehold().getWildFood()) {

			if (type == "cash") {
				wfTot += wf.getUnitsSold() * wf.getPricePerUnit();
			} else if (type == "food") {
				// wfTot += wf.getUnitsConsumed() *
				// wf.getResourceSubType().getResourcesubtypekcal();
				wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType());
			}
		}

		return wfTot;
	}
	/******************************************************************************************************************************************/

	private Double calcTransIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double trTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Transfer tr : wealthGroupInterview.get(i).getTransfer()) {
				if (tr.getTransferType() == null)
					break;

				if (type == "cash") {

					if (tr.getTransferType().equals(TransferType.Food)) {
						trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving()
								* tr.getTimesReceived();
					} else {
						trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
					}

				} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
						&& tr.getFoodResourceSubType() != null) {

					trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) * tr.getPeopleReceiving()
							* tr.getTimesReceived();

				}
			}

		}

		return trTot;
	}
	/******************************************************************************************************************************************/

	private Double calcTransIncome(HH hh2, String type) {
		Double trTot = 0.0;

		/* Handle transfer types food/cash/other */

		for (Transfer tr : hh2.getHousehold().getTransfer()) {
			if (type == "cash") {
				if (tr.getTransferType().equals(TransferType.Food)) {
					trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving() * tr.getTimesReceived();
				} else // Cash or Other
				{
					trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
				}
			} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
					&& tr.getFoodResourceSubType() != null) {
				trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) * tr.getPeopleReceiving()
						* tr.getTimesReceived();

			}

		}
		return trTot;
	}
	/******************************************************************************************************************************************/

	private Double calcLSIncome(List<WealthGroupInterview> wealthGroupInterview, String type) { // LSS and LSP sales
		Double lsTot = 0.0;

		System.out.println("in calcIncome type =" + type);

		for (int i = 0; i < wealthGroupInterview.size(); i++) {

			if (type == "cash") {

				for (LivestockSales ls : wealthGroupInterview.get(i).getLivestockSales()) {

					lsTot += ls.getUnitsSold() * ls.getPricePerUnit();
				}
				for (LivestockProducts lsp : wealthGroupInterview.get(i).getLivestockProducts()) {
					lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit();
				}
			}

			else if (type == "food") {
				for (LivestockProducts lsp : wealthGroupInterview.get(i).getLivestockProducts()) {
					lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType());
				}
			} else {
				lsTot = 0.0;
			}
		}
		return lsTot;

	}
	/******************************************************************************************************************************************/

	private Double calcLSIncome(HH hh2, String type) { // LSS and LSP sales
		Double lsTot = 0.0;
		if (type == "cash") {
			for (LivestockSales ls : hh2.getHousehold().getLivestockSales()) {
				lsTot += ls.getUnitsSold() * ls.getPricePerUnit();
			}

			for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {
				lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit();
			}
		} else if (type == "food") {
			for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {
				lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType());
			}
		}

		return lsTot;

	}
	/******************************************************************************************************************************************/

	private Double calcEmpIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double empTot = 0.0;
		System.out.println("in calcEmpIncome type =" + type);

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Employment emp : wealthGroupInterview.get(i).getEmployment()) {
				if (type == "cash") {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();

				} else if (type == "food" && emp.getFoodResourceSubType() != null) {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * findRSTKcal(emp.getFoodResourceSubType());
				} else {
					empTot = 0.0;
				}
			}
		}

		return empTot;

	}
	/******************************************************************************************************************************************/

	private Double calcEmpIncome(HH hh2, String type) {
		Double empTot = 0.0;

		/* What about food payments? for cash */
		if (hh2.getHousehold().getEmployment().size() == 0) {

			return (0.0);
		}
		try {
			System.out.println("type = " + type);

			for (Employment emp : hh2.getHousehold().getEmployment()) {
				if (type == "cash") {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();

				} else if (type == "food" && emp.getFoodResourceSubType() != null) {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * findRSTKcal(emp.getFoodResourceSubType());
				} else
					empTot = 0.0;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errno = 991;
		}

		return empTot;

	}
	/******************************************************************************************************************************************/
	private Double calcCropIncome(HH hh2, String type) {

		Double cropTot = 0.0;

		for (Crop crop : hh2.getHousehold().getCrop()) {
			if (type == "cash") {

				cropTot += crop.getUnitsSold() * crop.getPricePerUnit();

			} else if (type == "food") {

				cropTot += crop.getUnitsConsumed() * findRSTKcal(crop.getResourceSubType());

			}
		}

		return cropTot;
	}

	/******************************************************************************************************************************************/

	private Double calcCropIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {

		Double cropTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Crop crop : wealthGroupInterview.get(i).getCrop()) {
				if (type == "cash") {

					cropTot += crop.getUnitsSold() * crop.getPricePerUnit();
					
				} else if (type == "food") {
					
					cropTot += crop.getUnitsConsumed() * findRSTKcal(crop.getResourceSubType());
				}
			}
		}

		return cropTot;
	}

	/******************************************************************************************************************************************/
	private Double calcHhmSolc(HH hh3, StdOfLivingElement stdL) {

		int lowerAgeSOL = 0;
		int upperAgeSOL = 0;
		Gender genderSOL = null;

		lowerAgeSOL = stdL.getAgeRangeLower();
		upperAgeSOL = stdL.getAgeRangeUpper();
		genderSOL = stdL.getGender();

		for (HouseholdMember hhm : hh3.getHousehold().getHouseholdMember()) {
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

		}

		return (0.0);
	}

	/******************************************************************************************************************************************/

	private Double wealthgroupInterviewDI(WealthGroupInterview wealthGroupInterview) {

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
			if (cropList.size() > 0) {

				for (WGI icrop : cropList) {

					// Crop crop = icrop.getCrop();

					cropTI += (icrop.getCrop().getUnitsSold().doubleValue()
							* icrop.getCrop().getPricePerUnit().doubleValue());

					cropOP += (icrop.getCrop().getUnitsConsumed().doubleValue()
							* Double.valueOf(icrop.getCrop().getResourceSubType().getResourcesubtypekcal()));

				}
			}

			if (wildfoodList.size() > 0) {
				for (WGI iwildfood : wildfoodList) {

					wildfoodsTI += (iwildfood.getWildfood().getUnitsSold().doubleValue()
							* iwildfood.getWildfood().getPricePerUnit().doubleValue());

					wildfoodsOP += (iwildfood.getWildfood().getUnitsConsumed().doubleValue()
							* Double.valueOf(iwildfood.getWildfood().getResourceSubType().getResourcesubtypekcal()));
				}
			}
			if (livestockproductList.size() > 0) {
				for (WGI ilivestockproduct : livestockproductList) {

					lspTI += (ilivestockproduct.getLivestockproducts().getUnitsSold()
							* ilivestockproduct.getLivestockproducts().getPricePerUnit());
					lspOP += (ilivestockproduct.getLivestockproducts().getUnitsConsumed().doubleValue()
							* Double.valueOf(ilivestockproduct.getLivestockproducts().getResourceSubType()
									.getResourcesubtypekcal()));
				}
			}
			if (employmentList.size() > 0) {
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
			if (transferList.size() > 0) {
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
			addError("Error in DI Calculation " + e);
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

		Efdutils.em("In Create Header Page for Coping Strategy");

		sheet[0] = reportWB.addSheet("Coping Strategy Summary");
		setSheetStyle(sheet[0]);
		sheet[0].setColumnWidths(1, 40, 80, 50, 50, 50);

		sheet[0].setValue(1, 1, "Date:", boldRStyle); // col,row
		sheet[0].setValue(2, 1, new Date(), dateStyle);
		sheet[0].setValue(1, 2, "Spec Name:", boldRStyle);
		sheet[0].setValue(2, 2, "All Reports", textStyleLeft);
		sheet[0].setValue(1, 4, "Reporting Currency:", boldRStyle);

		sheet[0].setValue(1, 25, "Chage Scenario Title:", boldRStyle);
		sheet[0].setValue(1, 26, "Change Scenario Author:", boldRStyle);
		sheet[0].setValue(1, 27, "Change Scenario Description:", boldRStyle);

		sheet[0].setValue(2, 25, modellingScenario.getTitle(), textStyleLeft);
		sheet[0].setValue(2, 26, modellingScenario.getAuthor(), textStyleLeft);
		sheet[0].setValue(2, 27, modellingScenario.getDescription(), textStyleLeft);

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

			reportList = XPersistence.getManager().createQuery("from Report where method = :method")
					.setParameter("method", Method.MODELLINGOIHM).getResultList();

		} else if (isOHEA) {
			sheet[0].setValue(1, 3, "Livelihood Zone:", boldRStyle);
			sheet[0].setValue(2, 3, project.getProjecttitle() + " / " + livelihoodZone.getLzname(), textStyleLeft);
			reportList = XPersistence.getManager().createQuery("from Report where method = :method")
					.setParameter("method", Method.MODELLINGOHEA).getResultList();

			// Default reporting currency based on Currency for an LZ within this Project
			for (LivelihoodZone livelihoodZone3 : project.getLivelihoodZone()) {
				currency = livelihoodZone3.getCountry().getCurrencySymbol();
				sheet[0].setValue(2, 4, currency, textStyleLeft);
				break;

			}

		}
		sheet[0].setValue(2, STARTROW, "Reports", boldLStyle);
		/* get list of reports for modelling data spreadsheet */

		reportList = XPersistence.getManager().createQuery("from Report where method = :method")
				.setParameter("method", Method.MODELLINGOIHM).getResultList();

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

		numberd2 = reportWB.addStyle(INTEGER);
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