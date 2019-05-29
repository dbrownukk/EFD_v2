
/* Run OIHM Reports2 
 * 
 *
 * Current codes 20/5/19
 * DRB
 * 
 * Note that Issue 145 has DI calc
 * 
223 HH Disposable Income (DI): sorted by ascending DI 223
224 HH DI with Standard of Living Costs: sorted by ascending DI 224
225 HH Cash Income by source type: sorted by ascending DI 225
226 HH Food Income by source type: sorted by ascending DI 226
227 HH Land Assets: sorted by ascending DI 227
228 HH Livestock Assets: sorted by ascending DI 228
237 Report HH Members characteristics 237
236 Calculate Adult Equivalent KCal requirement for a Household 236
238 Report Population by Age and Sex 238
 *
 * Get all HH for this study into hh array
 * 
 * Filter hh on Custom Report filters 
 * 
 * Calculate DI etc.
 * 
 * */

package efd.actions;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;
import efd.model.HouseholdMember.*;
import efd.model.StdOfLivingElement.*;
import efd.model.WealthGroupInterview.*;

public class OIHMReports extends ViewBaseAction implements IForwardAction, JxlsConstants {

	static Double RC = 2200.0 * 365;
	static final int NUMBER_OF_REPORTS = 15;
	private Study study = null;
	// private CustomReportSpec customReportSpec = null;
	private List<Report> reportList;
	private List<Household> households;
	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	List<HH> uniqueHousehold;

	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;

	ArrayList<HH> hh = new ArrayList<>();

	private String forwardURI = null;

	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	CellStyle style = null;
	CellStyle vstyle = null;
	CellStyle datestyle = null;
	CellStyle title = null;
	CellStyle cnumberStyle = null;

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run OIHM Reports ");

		String specID = getView().getValueString("customReportSpec.id");

		Object studyId = getPreviousView().getValue("id");

		if (studyId == null) {
			addError("Save Study before running reports");
			closeDialog();
			return;
		}

		System.out.println("In Run OIHM Reports specid, studyid = " + specID + " " + studyId.toString());

		CustomReportSpec customReportSpec = XPersistence.getManager().find(CustomReportSpec.class, specID);

		study = XPersistence.getManager().find(Study.class, studyId);
		defaultDietItems = (List<DefaultDietItem>) study.getDefaultDietItem();

		/* Only create reports if there are Validated Households */

		List<Household> households = XPersistence.getManager()
				.createQuery("from Household where study_id = :study and status = :status ")
				.setParameter("study", study.getId()).setParameter("status", Status.Validated).getResultList();

		/******************************************************************************************************************************************/
		/* Pre Report Run Validations */

		for (Household household : households) {
			System.out.println("hh to use = " + household.getHouseholdNumber() + household.getHouseholdName());
		}

		if (households.size() == 0) {
			System.out.println("no validated households in the is study");
			addError("No Validated Households in this Study");
			closeDialog();
			return;
		}

		int ddiTotPercent = 0;

		for (DefaultDietItem defaultDietItem : defaultDietItems) {
			ddiTotPercent += defaultDietItem.getPercentage();
		}
		if (ddiTotPercent != 100) {
			addError("Default Diet Total Percentage for this Study is not 100%");
			closeDialog();
			return;
		}
		/******************************************************************************************************************************************/

		// Populate HH array hh
		populateHHArray(households);

		// Filter according to Catalog/RT/RST/HH
		filterHH(customReportSpec);

		// Calculate DI

		calculateDI(); // uses hh filtered array based on CRS definition

		// Run reports
		try {
			JxlsWorkbook report = createReport(customReportSpec);
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
		} catch (Exception e) {
	System.out.println("Error in run report");
			addError(e.getMessage());
			closeDialog();
			return;
		}

		closeDialog();

		addMessage("Created OIHM Report for " + customReportSpec.getSpecName());

	}

	private void calculateDI() {

		for (HH hh2 : hh) {
			System.out.println("in calculateDI 002");
			hh2.hhDI = householdDI(hh2.household);
		}

		uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber()))
				.sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());

		uniqueHousehold.forEach(u -> {
			System.out.println("hh in createdireport = " + u.hhNumber + " " + u.getHhDI());
		});

	}

	/******************************************************************************************************************************************/
	private void filterHH(CustomReportSpec customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from hh populate array
		// Filter out reportspecuse non included HH if exist and for same Study
		System.out.println("size of hh before filter = " + hh.size());
		System.out.println("filter 000");

		if (customReportSpec.getReportSpecUse().size() > 0) // Apply Report Spec Usage Filter - HH must be Validated
		{
			System.out.println("filter 111");
			for (ReportSpecUse reportSpecUse : customReportSpec.getReportSpecUse()) {
				System.out.println("filter 222");
				for (Household household : reportSpecUse.getHousehold()) {
					System.out.println("filter 333");
					for (HH hh2 : hh) {
						System.out.println("filter 444");
						if (hh2.getHousehold() != household) {
							System.out.println("a RSU to delete ");
							hh2.setDelete(true);

							// Only if this rsu hh is for current study is this rsu used

						} else if (household.getStudy() == study) {
							System.out.println("a  RSU to save ");
							hh2.setDelete(false);
							break;
						}
					}

				}
			}
		}

		if (customReportSpec.getCategory().size() > 0) // Apply Category Filter
		{
			System.out.println("in Cat filter");

			for (HH hh2 : hh) {

				for (Category category : customReportSpec.getCategory()) {

					for (ResourceSubType resourceSubType : category.getResourceSubType()) {
						if (resourceSubType != hh2.getResourceSubType()) {
							System.out.println("a Category RST to delete ");
							hh2.setDelete(true);
						} else {
							System.out.println("a Category RST to save ");
							hh2.setDelete(false);
							break;
						}

					}

				}
			}
		}

		if (customReportSpec.getResourceType().size() > 0) // Apply RST Filter
		{
			System.out.println("in RT filter");
			for (HH hh2 : hh) {

				for (ResourceType resourceType : customReportSpec.getResourceType()) {
					if (resourceType.getIdresourcetype() != hh2.getResourceType().getIdresourcetype()) {
						System.out.println("a RT to delete ");
						hh2.setDelete(true);
					}

					else {
						System.out.println("an  RT to save ");
						hh2.setDelete(false);
						break;
					}

				}
			}
		}

		if (customReportSpec.getResourceSubType().size() > 0) // Apply RST Filter
		{
			System.out.println("in RST filter");
			for (HH hh2 : hh) {

				for (ResourceSubType resourceSubType : customReportSpec.getResourceSubType()) {
					if (resourceSubType.getIdresourcesubtype() != hh2.getResourceSubType().getIdresourcesubtype()) {
						System.out.println("a RST to delete ");
						hh2.setDelete(true);
					} else {
						System.out.println("an  RST to save ");
						hh2.setDelete(false);
						break;
					}
				}

			}
		}

		if (customReportSpec.getConfigAnswer().size() > 0) // Apply Q and A Filter
		{
			System.out.println("in Q and A filter");
			for (HH hh2 : hh) {

				for (ConfigAnswer configAnswer : customReportSpec.getConfigAnswer())

					if (configAnswer != hh2.getAnswer()) {

						hh2.setDelete(true);
					} else {

						System.out.println("an  Answer to save " + hh2.getAnswer().getAnswer());
						hh2.setDelete(false);
						break;
					}
			}

		}

		try {
			hh.removeIf(n -> n.getDelete() == true);
		} catch (Exception e) {
			System.out.println("nothing filtered to remove");
			//e.printStackTrace();
		}

		System.out.println("size of hh after all filter  = " + hh.size());
		for (HH hh2 : hh) {
			if (hh2.getResourceType() != null)
				System.out.println("hh after filtering now equals = " + hh2.getHhNumber() + " "
						+ hh2.getCategory().toArray().toString() + " " + (hh2.getResourceType().getResourcetypename()
								+ " " + hh2.getResourceSubType().getResourcetypename()));
			else
				System.out.println("print an answer" + hh2.toString());

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
		System.out.println("in populateArray");

		ConfigAnswer answer = null;
		for (Household household : households) {

			populateHHfromHousehold(household, answer);

			for (ConfigAnswer configAnswer : household.getConfigAnswer()) {
				System.out.println("answer = " + configAnswer.getAnswer());
				addTohhArray(household, null, null, null, configAnswer, "Answer", null, null, null, null, null, null,
						null, null, null, null, null, null, null);
				/*
				 * Found a HH with required answer - now iterate back through hh to populate
				 * assets
				 */

				populateHHfromHousehold(household, configAnswer);

			}

		}

		for (HH hh2 : hh) {
			if (hh2.getResourceType() != null)
				System.out.println("hh in arraypop now equals = " + hh2.getHhNumber() + " "
						+ hh2.getCategory().toArray().toString() + " " + (hh2.getResourceType().getResourcetypename()
								+ " " + hh2.getResourceSubType().getResourcetypename()));
			else
				System.out.println("print an answer" + hh2.toString());

		}

		System.out.println("end populateArray");

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

	private void addTohhArray(Household household, Collection<Category> category, ResourceType resourceType,
			ResourceSubType resourceSubType, ConfigAnswer answer, String type, AssetLand land, AssetFoodStock foodstock,
			AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable, AssetTree tree, Crop crop,
			Employment employment, Inputs inputs, LivestockProducts livestockproducts, LivestockSales livestocksales,
			Transfer transfer, WildFood wildfood) {

		HH e = new HH();
		e.household = household;
		e.hhNumber = household.getHouseholdNumber();
		e.category = category;
		e.resourceType = resourceType;
		e.resourceSubType = resourceSubType;
		e.answer = answer;
		e.type = type;
		e.land = land;
		e.employment = employment;
		e.foodstock = foodstock;
		e.inputs = inputs;
		e.livestock = livestock;
		e.livestockproducts = livestockproducts;
		e.livestocksales = livestocksales;
		e.tradeable = tradeable;
		e.transfer = transfer;
		e.tree = tree;
		e.wildfood = wildfood;
		e.crop = crop;
		e.cash = cash;

		hh.add(e);

	}

	/******************************************************************************************************************************************/

	private JxlsWorkbook createReport(CustomReportSpec customReportSpec) throws Exception {
		System.out.println("In Run OIHM Reports create xls ");

		String filename = customReportSpec.getSpecName() + Calendar.getInstance().getTime();

		reportWB = new JxlsWorkbook(filename);
		setStyles();

		createHeaderPage(customReportSpec); // populates reportList
		int ireportNumber = 0; // should be equal to the sheet number in workbook

		/*
		 * HH are included if they are Validated
		 * 
		 * If Custom Report Spec (CRS) has Config Answers then only HH with answers to
		 * that question and answer are included
		 * 
		 * Order by HH DI size
		 * 
		 * Include all resources unless CRS has Category/RT or RST rows which then form
		 * the filter
		 * 
		 */

		for (Report report : reportList) {
			System.out.println("code = " + report.getCode());
			int reportCode = report.getCode();
			System.out.println("pre switch 001");
			ireportNumber++; // keep track of number of reports = sheet on output spreadsheet

			switch (reportCode) {
			case 223:
				System.out.println("report 223");
				createDIreport(ireportNumber, report);
				break;
			case 224:
				System.out.println("report 224");
				createDIAfterSOLreport(ireportNumber, report);
				break;
			default:
				break;
			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/

	private void createDIreport(int isheet, Report report) {

		int row = 1;
		System.out.println("hh 001");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Disposable Income", textStyle);

		row = 3;

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		for (HH hh2 : uniqueHousehold) {
			System.out.println("hhDI 002");
			// hh2.hhDI = householdDI(hh2.household);

			reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
			reportWB.getSheet(isheet).setValue(2, row, hh2.hhDI, textStyle);
			row++;
		}
	}

	/******************************************************************************************************************************************/

	private void createDIAfterSOLreport(int isheet, Report report) {
		int row = 1;
		Double hhSOLC = 0.0;

		System.out.println("hh 001");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 25);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Standard of Living Requirement", textStyle);

		row = 3;

		// uniqeHousehold = number of households in array that are relavant

		for (HH hh3 : uniqueHousehold) {
			for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {
				if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {
					hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());
				} else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {

					for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {
						hhSOLC += calcHhmSolc(hh3, stdOfLivingElement);

					}

				}

			}

			hh3.setHhSOLC(hhSOLC);

		}

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		for (HH hh2 : uniqueHousehold) {
			System.out.println("hhSOLC 002");

			reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
			reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), textStyle);
			row++;
		}
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

	private Double householdDI(Household household) {

		/*
		 * Disposable Income (DI) = Total Income (TI) - Cost of covering Shortfall (SF)
		 * in Required Calories (RC) from Own Production (OP).
		 * 
		 * TI = Sum (Units Sold * Price per Unit) for all Crops, Wild Foods and
		 * Livestock Products
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
		 * RC = 2100 365 No. of People in Household
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
		System.out.println("DI 11");

		List<HH> thisHH = hh.stream().filter(d -> d.household == household).collect(Collectors.toList());

		List<HH> cropList = thisHH.stream().filter(d -> d.type == "Crop").collect(Collectors.toList());

		List<HH> wildfoodList = thisHH.stream().filter(d -> d.type == "Wildfood").collect(Collectors.toList());


		List<HH> livestockproductList = thisHH.stream().filter(d -> d.type == "LivestockProduct")
				.collect(Collectors.toList());

		List<HH> employmentList = thisHH.stream().filter(d -> d.type == "Employment").collect(Collectors.toList());

		List<HH> transferList = thisHH.stream().filter(d -> d.type == "Transfer").collect(Collectors.toList());

		try {
			if (cropList.size() > 0) {

				for (HH icrop : cropList) {

					// Crop crop = icrop.getCrop();

					cropTI += (icrop.getCrop().getUnitsSold().doubleValue()
							* icrop.getCrop().getPricePerUnit().doubleValue());

					cropOP += (icrop.getCrop().getUnitsConsumed().doubleValue()
							* Double.valueOf(icrop.getCrop().getResourceSubType().getResourcesubtypekcal()));
					
				}
			}
			
			if (wildfoodList.size() > 0) {
				for (HH iwildfood : wildfoodList) {

					wildfoodsTI += (iwildfood.getWildfood().getUnitsSold().doubleValue()
							* iwildfood.getWildfood().getPricePerUnit().doubleValue());

					cropOP += (iwildfood.getWildfood().getUnitsConsumed().doubleValue()
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			addError("Error in DI Calculation " + e);
		}

		// FIX for members
		requiredCalories = household.getHouseholdMember().size() * RC; // Unique Households after filter

		Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI + transfersTI;
		Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

		Double shortFall = requiredCalories - output;

		// Now it gets more complex , but not difficult

		// Diet
		// Diet Value = Sum (KCal per KG * Percentage of the Food type in default diet

		Double dietValue = 0.0;

		for (DefaultDietItem defaultDietItem : defaultDietItems) {
			dietValue += (defaultDietItem.getResourcesubtype().getResourcesubtypekcal()
					* defaultDietItem.getPercentage() / 100);
		}

		System.out.println(("DV = " + dietValue));

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

		return (disposableIncome); 
	}

	/******************************************************************************************************************************************/

	private void createHeaderPage(CustomReportSpec customReportSpec) {

		int i = 5;

		sheet[0] = reportWB.addSheet("Custom Report Spec");
		setSheetStyle(sheet[0]);

		sheet[0].setValue(1, 1, "Date:", textStyle);
		sheet[0].setValue(2, 1, new Date());
		sheet[0].setValue(1, 2, "Spec Name:", textStyle);
		sheet[0].setValue(2, 2, customReportSpec.getSpecName(), textStyle);
		sheet[0].setValue(1, 3, "Study:", textStyle);
		sheet[0].setValue(2, 3, study.getStudyName() + " " + study.getReferenceYear(), textStyle);
		sheet[0].setValue(1, i, "Reports", textStyle);
		/* get list of reports and create tabbed bsheets for each */

		/* If no reports in custom report spec then use all reports */

		if (customReportSpec.getReport().size() > 0)
			reportList = (List<Report>) customReportSpec.getReport();
		else
			reportList = XPersistence.getManager().createQuery("from Report").getResultList();

		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyle);
			sheet[i - 3] = reportWB.addSheet(report.getName());

			setSheetStyle(sheet[i - 3]);

			i++;
		}

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
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(RIGHT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT)
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setCellColor(BLUE);
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

	/*
	 * Using generic would be better instead of each asset class. Needs a revisit
	 * sometime
	 */
	public static class HH {
		private Household household;
		private int hhNumber;
		private Double hhDI;
		private Double hhSOLC;
		private Collection<Category> category;
		private ResourceType resourceType;
		private ResourceSubType resourceSubType;
		private ConfigAnswer answer;
		private Boolean delete;
		private String type;
		private AssetLand land;
		private AssetFoodStock foodstock;
		private AssetCash cash;
		private AssetLiveStock livestock;
		private AssetTradeable tradeable;
		private AssetTree tree;
		private Crop crop;
		private Employment employment;
		private Inputs inputs;
		private LivestockProducts livestockproducts;
		private LivestockSales livestocksales;
		private Transfer transfer;
		private WildFood wildfood;

		public Double getHhSOLC() {
			return hhSOLC;
		}

		public void setHhSOLC(Double hhSOLC) {
			this.hhSOLC = hhSOLC;
		}

		public AssetFoodStock getFoodstock() {
			return foodstock;
		}

		public void setFoodstock(AssetFoodStock foodstock) {
			this.foodstock = foodstock;
		}

		public AssetCash getCash() {
			return cash;
		}

		public void setCash(AssetCash cash) {
			this.cash = cash;
		}

		public AssetLiveStock getLivestock() {
			return livestock;
		}

		public void setLivestock(AssetLiveStock livestock) {
			this.livestock = livestock;
		}

		public AssetTradeable getTradeable() {
			return tradeable;
		}

		public void setTradeable(AssetTradeable tradeable) {
			this.tradeable = tradeable;
		}

		public AssetTree getTree() {
			return tree;
		}

		public void setTree(AssetTree tree) {
			this.tree = tree;
		}

		public Crop getCrop() {
			return crop;
		}

		public void setCrop(Crop crop) {
			this.crop = crop;
		}

		public Employment getEmployment() {
			return employment;
		}

		public void setEmployment(Employment employment) {
			this.employment = employment;
		}

		public Inputs getInputs() {
			return inputs;
		}

		public void setInputs(Inputs inputs) {
			this.inputs = inputs;
		}

		public LivestockProducts getLivestockproducts() {
			return livestockproducts;
		}

		public void setLivestockproducts(LivestockProducts livestockproducts) {
			this.livestockproducts = livestockproducts;
		}

		public LivestockSales getLivestocksales() {
			return livestocksales;
		}

		public void setLivestocksales(LivestockSales livestocksales) {
			this.livestocksales = livestocksales;
		}

		public Transfer getTransfer() {
			return transfer;
		}

		public void setTransfer(Transfer transfer) {
			this.transfer = transfer;
		}

		public WildFood getWildfood() {
			return wildfood;
		}

		public void setWildfood(WildFood wildfood) {
			this.wildfood = wildfood;
		}

		public Household getHousehold() {
			return household;
		}

		public void setHousehold(Household household) {
			this.household = household;
		}

		public int getHhNumber() {
			return hhNumber;
		}

		public void setHhNumber(int hhNumber) {
			this.hhNumber = hhNumber;
		}

		public Double getHhDI() {
			return hhDI;
		}

		public void setHhDI(Double hhDI) {
			this.hhDI = hhDI;
		}

		public Collection<Category> getCategory() {
			return category;
		}

		public void setCategory(Collection<Category> category) {
			this.category = category;
		}

		public ResourceType getResourceType() {
			return resourceType;
		}

		public void setResourceType(ResourceType resourceType) {
			this.resourceType = resourceType;
		}

		public ResourceSubType getResourceSubType() {
			return resourceSubType;
		}

		public void setResourceSubType(ResourceSubType resourceSubType) {
			this.resourceSubType = resourceSubType;
		}

		public ConfigAnswer getAnswer() {
			return answer;
		}

		public void setAnswer(ConfigAnswer answer) {
			this.answer = answer;
		}

		public Boolean getDelete() {
			return delete;
		}

		public void setDelete(Boolean delete) {
			this.delete = delete;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public AssetLand getLand() {
			return land;
		}

		public void setLand(AssetLand land) {
			this.land = land;
		}

	}

}