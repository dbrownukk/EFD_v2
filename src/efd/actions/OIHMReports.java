
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

import javax.persistence.*;

import org.apache.commons.lang.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.tab.impl.*;
import org.openxava.util.jxls.*;
import org.openxava.view.*;
import org.openxava.web.servlets.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;
import efd.model.HouseholdMember.*;
import efd.model.StdOfLivingElement.*;
import efd.model.Transfer.*;
import efd.model.WealthGroupInterview.*;

public class OIHMReports extends TabBaseAction implements IForwardAction, JxlsConstants {

	static Double RC = 2200.0 * 365;
	static final int NUMBER_OF_REPORTS = 15;
	private Study study = null;
	// private CustomReportSpec customReportSpec = null;
	private List<Report> reportList;
	private List<Household> households;
	private List<Household> selectedHouseholds = new ArrayList<Household>();

	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	List<HH> uniqueHousehold;
	List<Quantile> quantiles;
	int numberOfQuantiles = 0;

	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;

	ArrayList<HH> hh = new ArrayList<>();
	ArrayList<HH> hhSelected = new ArrayList<>();

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

	int errno = 0;
	Boolean isQuantile = false;
	Boolean isSelectedHouseholds = false;

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run OIHM Reports ");

		String specID = getView().getValueString("customReportSpec.id");
		if (specID.equals("")) {
			addError("Choose a Report Spec");
			return;
		}

		Object studyId = getPreviousView().getValue("id");

		if (studyId == null) {
			addError("Save Study before running reports");
			closeDialog();
			return;
		}

		Tab targetTab = getView().getSubview("study.household").getCollectionTab();

		Map[] selectedOnes = targetTab.getSelectedKeys();

		System.out.println("selected keys a = " + selectedOnes.length);

		if (selectedOnes.length != 0) {
			isSelectedHouseholds = true; // One or more HH selected in dialog
			for (int i = 0; i < selectedOnes.length; i++) {
				Map<?, ?> key = selectedOnes[i];

				Map<?, ?> membersNames = getView().getSubview("study.household").getMembersNames();
				// System.out.println("mnames = "+i+" "+membersNames);
				System.out.println("key = " + key.toString());

				String subKey = key.toString().substring(4, 36);
				System.out.println("sub key = " + subKey);

				// Map househ = MapFacade.getValues("Household", sub, membersNames);

				Household singleHHSelected = XPersistence.getManager().find(Household.class, subKey);

				System.out.println("single hh selected = " + singleHHSelected);

				HH e = new HH();
				e.household = singleHHSelected;
				hhSelected.add(e);

				System.out.println(" hhselected size = " + hhSelected.size());

				selectedHouseholds.add(singleHHSelected);
			}

		}

		System.out.println("isselected = " + isSelectedHouseholds);
		System.out.println("In Run OIHM Reports specid, studyid = " + specID + " " + studyId.toString());

		CustomReportSpec customReportSpec = XPersistence.getManager().find(CustomReportSpec.class, specID);

		study = XPersistence.getManager().find(Study.class, studyId);

		defaultDietItems = (List<DefaultDietItem>) study.getDefaultDietItem();

		/* Populate WHO table */

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

		// Quantiles
		if (customReportSpec.getQuantile().size() > 0 && customReportSpec.getTotalQuantilePercentage() != 100) {
			addError("Quantile for this Report Spec is set but does not total 100%");
			closeDialog();

			return;
		} else if (customReportSpec.getTotalQuantilePercentage() == 100) {
			isQuantile = true;
			quantiles = (List<Quantile>) customReportSpec.getQuantile();
			numberOfQuantiles = customReportSpec.getQuantile().size();
		}

		/******************************************************************************************************************************************/

		errno = 50;

		// Populate HH array hh - use dialog selected list if entered

		if (!isSelectedHouseholds) {
			System.out.println("no hh selection");
			populateHHArray(households);
		} else if (isSelectedHouseholds) {
			System.out.println("made a  hh selection");
			populateHHArray(selectedHouseholds);
		}

		errno = 51;
		// Filter according to Catalog/RT/RST/HH
		filterHH(customReportSpec);
		errno = 52;
		// Calculate DI

		calculateDI(); // uses hh filtered array based on CRS definition
		calculateAE(); // Calculate the Adult equivalent

		errno = 54;
		// Run reports
		try {
			JxlsWorkbook report = createReport(customReportSpec);
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

		addMessage("Created OIHM Report for Study " + study.getStudyName() + " using Spec "
				+ customReportSpec.getSpecName());

	}

	private void calculateAE() {

		for (HH hh2 : hh) {

			hh2.hhAE = householdAE(hh2.household);
		}

	}

	private void calculateDI() {

		for (HH hh2 : hh) {

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

		/*
		 * Remove RSU from CRS if (customReportSpec.getReportSpecUse().size() > 0) //
		 * Apply Report Spec Usage Filter - HH must be Validated {
		 * 
		 * for (ReportSpecUse reportSpecUse : customReportSpec.getReportSpecUse()) {
		 * 
		 * for (Household household : reportSpecUse.getHousehold()) {
		 * 
		 * for (HH hh2 : hh) {
		 * 
		 * if (hh2.getHousehold() != household) {
		 * System.out.println("a RSU to delete "); hh2.setDelete(true);
		 * 
		 * // Only if this rsu hh is for current study is this rsu used
		 * 
		 * } else if (household.getStudy() == study) {
		 * System.out.println("a  RSU to save "); hh2.setDelete(false); break; } }
		 * 
		 * } } }
		 */

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
			// e.printStackTrace();
		}

		// System.out.println("size of hh after all filter = " + hh.size());
		// for (HH hh2 : hh) {
		// if (hh2.getResourceType() != null)
		// System.out.println("hh after filtering now equals = " + hh2.getHhNumber() + "
		// "
		// + hh2.getCategory().toArray().toString() + " " +
		// (hh2.getResourceType().getResourcetypename()
		// + " " + hh2.getResourceSubType().getResourcetypename()));
		// else
		// System.out.println("print an answer" + hh2.toString());

		// }

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

		// for (HH hh2 : hh) {
		// if (hh2.getResourceType() != null)
		// System.out.println("hh in arraypop now equals = " + hh2.getHhNumber() + " "
		// + hh2.getCategory().toArray().toString() + " " +
		// (hh2.getResourceType().getResourcetypename()
		// + " " + hh2.getResourceSubType().getResourcetypename()));
		// else
		// System.out.println("print an answer" + hh2.toString());

		// }

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
			case 225:
				System.out.println("report 225");
				createIncomereport(ireportNumber, report, "cash");
				break;
			case 226:
				System.out.println("report 226");
				createIncomereport(ireportNumber, report, "food");
				break;

			case 227:
				System.out.println("report 227");
				createLandAssetreport(ireportNumber, report);
				break;

			case 228:
				System.out.println("report 228");
				createLivestockAssetreport(ireportNumber, report);
				break;

			case 237:
				System.out.println("report 237");
				createHHMemberreport(ireportNumber, report);
				break;
			case 236:
				System.out.println("report 236");
				createAdultEquivalentreport(ireportNumber, report);
				break;
			case 238:
				System.out.println("report 238");
				createPopulationreport(ireportNumber, report);
				break;

			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/

	private void createDIreport(int isheet, Report report) {

		int row = 1;

		System.out.println("in 223 drb quantie = " + isQuantile);
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		if (isQuantile) {
			Double totDI = 0.0;
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);
			System.out.println("DI quantile ");

			for (HH hh3 : uniqueHousehold) {

				totDI += hh3.getHhDI();

			}
			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Disposable Income", textStyle);
			row++;

			for (Quantile quantile : quantiles) {
				Double quantileAmount = quantile.getPercentage() / 100.0 * totDI;
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, quantileAmount, textStyle);
				row++;
			}

		} else {
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);
			System.out.println("DI non quantile ");
			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Disposable Income", textStyle);
			row++;
			for (HH hh2 : uniqueHousehold) {

				reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.hhDI, textStyle);
				row++;
			}
		}
	}

	/******************************************************************************************************************************************/

	private void createDIAfterSOLreport(int isheet, Report report) {
		int row = 1;
		Double hhSOLC = 0.0;

		System.out.println("In SOL Report 224");

		// uniqeHousehold = number of households in array that are relevant

		for (HH hh3 : uniqueHousehold) {
			errno = 101;
			hhSOLC = 0.0;
			for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {
				errno = 102;

				if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {
					errno = 103;
					hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());
				} else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {
					errno = 104;
					for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {
						hhSOLC += calcHhmSolc(hh3, stdOfLivingElement);
						errno = 105;
					}

				}

			}
			errno = 106;
			hh3.setHhSOLC(hhSOLC);

		}

		if (isQuantile) {
			Double totSTOL = 0.0;

			reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);

			List<HH> orderedHH = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getHhSOLC))
					.collect(Collectors.toList());

			for (HH hh5 : orderedHH) {
				totSTOL += hh5.getHhSOLC();
			}

			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Standard of Living Requirement", textStyle);
			row++;

			for (Quantile quantile : quantiles) {
				Double quantileAmount = quantile.getPercentage() / 100.0 * totSTOL;
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, quantileAmount, textStyle);
				row++;
			}
		} else {
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 30);
			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Standard of Living Requirement", textStyle);
			row++;
			for (HH hh2 : uniqueHousehold) {

				reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), textStyle);
				row++;
			}
		}
	}

	/******************************************************************************************************************************************/

	private void createIncomereport(int isheet, Report report, String type) {
		int row = 1;
		// String type = "food" or "cash"

		Double cropIncome = 0.0;
		Double empIncome = 0.0;
		Double lsIncome = 0.0;
		Double trIncome = 0.0;
		Double wfIncome = 0.0;

		String initType = StringUtils.capitalise(type);
		
		System.out.println("in  income report");
		reportWB.getSheet(isheet).setColumnWidths(1, 25, 25, 25, 25, 25, 25, 25);

		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
		} else {

			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		}

		reportWB.getSheet(isheet).setValue(2, row, "Crop "+initType+" Income", textStyle);
		reportWB.getSheet(isheet).setValue(3, row, "Employment "+initType+" Income", textStyle);
		reportWB.getSheet(isheet).setValue(4, row, "Livestock "+initType+" Income", textStyle);
		reportWB.getSheet(isheet).setValue(5, row, "Transfer "+initType+" Income", textStyle);
		reportWB.getSheet(isheet).setValue(6, row, "Wildfood "+initType+" Income", textStyle);

		row = 3;
		System.out.println("in food income report done heading");

		if (isQuantile) {

			for (HH hh3 : uniqueHousehold) {
				cropIncome += calcCropIncome(hh3, type);
				System.out.println("about to do calcempincome");
				empIncome += calcEmpIncome(hh3, type);
				System.out.println("done calcempincome");
				lsIncome += calcLSIncome(hh3, type);
				trIncome += calcTransIncome(hh3, type);
				wfIncome += calcWFIncome(hh3, type);

			}

			row++;

			for (Quantile quantile : quantiles) {
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, quantile.getPercentage() / 100.0 * cropIncome, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, quantile.getPercentage() / 100.0 * empIncome, textStyle);
				reportWB.getSheet(isheet).setValue(5, row, quantile.getPercentage() / 100.0 * lsIncome, textStyle);
				reportWB.getSheet(isheet).setValue(6, row, quantile.getPercentage() / 100.0 * trIncome, textStyle);
				reportWB.getSheet(isheet).setValue(7, row, quantile.getPercentage() / 100.0 * wfIncome, textStyle);
				row++;
			}
		} else {

			for (HH hh2 : uniqueHousehold) {

				cropIncome = calcCropIncome(hh2, type);
				empIncome = calcEmpIncome(hh2, type);
				lsIncome = calcLSIncome(hh2, type);
				trIncome = calcTransIncome(hh2, type);
				wfIncome = calcWFIncome(hh2, type);

				reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
				reportWB.getSheet(isheet).setValue(2, row, cropIncome, textStyle);
				reportWB.getSheet(isheet).setValue(3, row, empIncome, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, lsIncome, textStyle);
				reportWB.getSheet(isheet).setValue(5, row, trIncome, textStyle);
				reportWB.getSheet(isheet).setValue(6, row, wfIncome, textStyle);
				row++;
			}
		}
	}

	/******************************************************************************************************************************************/
	/**
	 * @param isheet
	 * @param report
	 */
	private void createLandAssetreport(int isheet, Report report) {
		int row = 7;
		int col = 3;
		Map<ResourceSubType, Double> landTot = new HashMap<>();

		System.out.println("In SOL Report 227 - Land Assets");

		ArrayList<HHSub> hhl = new ArrayList<>();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20);
		errno = 2271;
		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, 6, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 6, "Quantile %", textStyle);
		} else {
			reportWB.getSheet(isheet).setValue(1, 6, "Household Number", textStyle);
		}
		errno = 2272;
		/* need to create matrix of hh id against land assets */

		/* How many land assets for this set of filtered households */

		errno = 2273;

		// Populate hhLand array for matrix

		for (HH hh3 : uniqueHousehold) {
			for (AssetLand assetLand : hh3.getHousehold().getAssetLand()) {
				HHSub hhLand = new HHSub();

				hhLand.setAssetLand(assetLand);
				hhLand.setHhid(hh3.hhNumber);
				hhLand.setAssetRST(assetLand.getResourceSubType());
				hhLand.setAssetValue(assetLand.getNumberOfUnits());
				hhLand.setHhDI(hh3.getHhDI());

				hhl.add(hhLand);

				if (landTot.containsKey(assetLand.getResourceSubType())) {
					Double total = landTot.get(assetLand.getResourceSubType());
					total += assetLand.getNumberOfUnits();
					landTot.remove(assetLand.getResourceSubType());
					landTot.put(assetLand.getResourceSubType(), total);

				} else // create new
				{
					landTot.put(assetLand.getResourceSubType(), assetLand.getNumberOfUnits());
				}
			}

		}

		System.out.println("landout " + landTot.toString());

		// get distinct list of RST
		List<HHSub> uniqueLand = hhl.stream().filter(distinctByKey(l -> l.getAssetRST())).collect(Collectors.toList());

		// Populate the relevant column value for the RST
		for (HHSub land : uniqueLand) {

			land.setColumn(col);

			Iterator<HHSub> hhlIterator = hhl.iterator();
			while (hhlIterator.hasNext()) {

				HHSub currentHHLand = hhlIterator.next();
				if (currentHHLand.getAssetRST() == land.getAssetRST()) {
					currentHHLand.setColumn(land.getColumn());
				}
			}

			col++;
		}

		errno = 2274;

		for (HHSub hhl2 : uniqueLand) {

			// Heading
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 3, "Asset Category", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 4, "Land", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 5, "Asset Type", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 6, hhl2.getAssetRST().getResourcetypename(),
					textStyle);

		}
		// sort into DI order

		int i;
		hhl = (ArrayList<HHSub>) hhl.stream().sorted(Comparator.comparing(HHSub::getHhDI)).collect(Collectors.toList());

		int currentHHid = 0;
		for (HHSub hhl3 : hhl) {
			i = 0;
			if (currentHHid != hhl3.getHhid() && currentHHid != 0) {
				row++;
			}

			if (!isQuantile) {

				currentHHid = hhl3.getHhid();
				reportWB.getSheet(isheet).setValue(1, row, hhl3.getHhid(), textStyle);
				reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row, hhl3.getAssetValue(), textStyle);
			} else // Quantiles are set
			{
				System.out.println("i and number quant = " + i + " " + numberOfQuantiles);

				for (Quantile quantile : quantiles) {
					System.out.println("print quant value row and i " + row + " " + i);
					reportWB.getSheet(isheet).setValue(1, row + i, quantile.getName(), textStyle);
					reportWB.getSheet(isheet).setValue(2, row + i, quantile.getPercentage(), textStyle);
					reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row + i,
							quantile.getPercentage() / 100.0 * landTot.get(hhl3.getAssetRST()), textStyle);
					i++;
					if (i > numberOfQuantiles)
						break;
				}

			}
		}
	}

	/******************************************************************************************************************************************/

	private void createLivestockAssetreport(int isheet, Report report) {
		int row = 7;
		int col = 2;
		Double hhSOLC = 0.0;
		Map<ResourceSubType, Double> lsTot = new HashMap<>();

		System.out.println("In SOL Report 228 - Livestock Assets");

		ArrayList<HHSub> hhl = new ArrayList<>();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20);
		errno = 2281;

		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, 6, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 6, "Quantile %", textStyle);
		} else {
			reportWB.getSheet(isheet).setValue(1, 6, "Household Number", textStyle);
		}

		errno = 2282;
		/* need to create matrix of hh id against land assets */

		/* How many land assets for this set of filtered households */

		errno = 2283;

		// Populate hhLand array for matrix
		for (HH hh3 : uniqueHousehold) {
			for (AssetLiveStock assetLS : hh3.getHousehold().getAssetLiveStock()) {
				HHSub hhLS = new HHSub();

				hhLS.setAssetLivestock(assetLS);
				hhLS.setHhid(hh3.hhNumber);
				hhLS.setAssetRST(assetLS.getResourceSubType());
				hhLS.setAssetValue(assetLS.getNumberOwnedAtStart());
				hhLS.setHhDI(hh3.getHhDI());

				hhl.add(hhLS);

				if (lsTot.containsKey(assetLS.getResourceSubType())) {
					Double total = lsTot.get(assetLS.getResourceSubType());
					total += assetLS.getNumberOwnedAtStart();
					lsTot.remove(assetLS.getResourceSubType());
					lsTot.put(assetLS.getResourceSubType(), total);

				} else // create new
				{
					lsTot.put(assetLS.getResourceSubType(), assetLS.getNumberOwnedAtStart());
				}

			}
		}

		for (HHSub ls : hhl) {
			System.out.println("ls" + ls.getAssetRST().getResourcetypename());

		}

		List<HHSub> uniqueLS = hhl.stream().filter(distinctByKey(l -> l.getAssetRST())).collect(Collectors.toList());

		// Populate the relevant column value for the RST
		for (HHSub ls : uniqueLS) {

			ls.setColumn(col);

			Iterator<HHSub> hhlIterator = hhl.iterator();
			while (hhlIterator.hasNext()) {

				HHSub currentHHLS = hhlIterator.next();
				if (currentHHLS.getAssetRST() == ls.getAssetRST()) {
					currentHHLS.setColumn(ls.getColumn());
				}
			}

			col++;
		}

		errno = 2284;

		for (HHSub hhl2 : uniqueLS) {

			// Heading
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 3, "Asset Category", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 4, "Livestock", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 5, "Asset Type", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 6, hhl2.getAssetRST().getResourcetypename(),
					textStyle);

			// HH data

		}
		errno = 2285;
		// sort into DI order
		hhl = (ArrayList<HHSub>) hhl.stream().sorted(Comparator.comparing(HHSub::getHhDI)).collect(Collectors.toList());
		int i;
		int currentHHid = 0;
		errno = 2286;
		for (HHSub hhl3 : hhl) {
			i = 0;
			if (currentHHid != hhl3.getHhid() && currentHHid != 0) {
				row++;
			}
			if (!isQuantile) {

				currentHHid = hhl3.getHhid();
				reportWB.getSheet(isheet).setValue(1, row, hhl3.getHhid(), textStyle);
				reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row, hhl3.getAssetValue(), textStyle);
			} else {

				for (Quantile quantile : quantiles) {
					System.out.println("print quant value row and i " + row + " " + i);
					reportWB.getSheet(isheet).setValue(1, row + i, quantile.getName(), textStyle);
					reportWB.getSheet(isheet).setValue(2, row + i, quantile.getPercentage(), textStyle);
					reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row + i,
							quantile.getPercentage() / 100.0 * lsTot.get(hhl3.getAssetRST()), textStyle);
					i++;
					if (i > numberOfQuantiles)
						break;
				}

			}
		}
		errno = 2287;

	}

	/******************************************************************************************************************************************/

	private void createHHMemberreport(int isheet, Report report) {
		int row = 1;
		int col = 6;
		errno = 2371;
		System.out.println("In HHMember report 237");
		reportWB.getSheet(isheet).setColumnWidths(1, 15, 10, 10, 10, 10, 10, 10, 30, 30, 30, 30, 30);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Member", textStyle);
		reportWB.getSheet(isheet).setValue(3, row, "Age", textStyle);
		reportWB.getSheet(isheet).setValue(4, row, "Sex", textStyle);
		reportWB.getSheet(isheet).setValue(5, row, "Notes", textStyle);

		/* Need any extra questions/answers that are defined for this Study */
		/*
		 * Question header obtained from just one household as all questions same for
		 * hh/hhm in a study
		 */
		errno = 2372;
		System.out.println("In HHMember report 2372");
		Optional<HH> findFirstHH = uniqueHousehold.stream().findFirst();
		System.out.println("In HHMember report done find first" + findFirstHH.toString());

		for (HouseholdMember member : findFirstHH.get().household.getHouseholdMember()) {
			System.out.println("hhmember question get ");
			for (ConfigAnswer answer : member.getConfigAnswer()) {
				System.out.println("hhm prompt = " + answer.getConfigQuestionUse().getConfigQuestion().getPrompt());
				String prompt = answer.getConfigQuestionUse().getConfigQuestion().getPrompt();

				reportWB.getSheet(isheet).setValue(col, row, prompt, textStyle);
				col++;

			}

		}
		errno = 2373;
		System.out.println("In HHMember report 2373");
		for (HH hh2 : uniqueHousehold) {

			Collection<HouseholdMember> householdMember = hh2.getHousehold().getHouseholdMember();
			List<HouseholdMember> hhm = householdMember.stream()
					.sorted(Comparator.comparing(HouseholdMember::getHouseholdMemberNumber))
					.collect(Collectors.toList());
			for (HouseholdMember hh3 : hhm) {
				row++;
				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh3.getHouseholdMemberNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, hh3.getAge(), textStyle);
				reportWB.getSheet(isheet).setValue(4, row, hh3.getGender(), textStyle);
				reportWB.getSheet(isheet).setValue(5, row, hh3.getNotes(), textStyle);
				col = 6;
				// Answers
				for (ConfigAnswer configAnswer : hh3.getConfigAnswer()) {
					reportWB.getSheet(isheet).setValue(col, row, configAnswer.getAnswer(), textStyle);
					col++;
				}

			}

		}

		errno = 2374;
		System.out.println("In HHMember report 2374");
	}

	/******************************************************************************************************************************************/

	private void createPopulationreport(int isheet, Report report) {
		int row = 1;
		System.out.println("Population Report");

		if (!isQuantile) {
			reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20);
			reportWB.getSheet(isheet).setValue(1, row, "Age Range", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Total Males", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Total Females", textStyle);
			reportWB.getSheet(isheet).setValue(1, row + 1, "0 - 4", textStyle);

			int lower = 5;
			int upper = 9;
			row = 3;
			// Print 5 - 9, 10 - 14 groups
			for (int i = 2; i < 21; i++) {
				reportWB.getSheet(isheet).setValue(1, row, lower + " - " + upper, textStyle);
				lower += 5;
				upper += 5;
				row++;
			}

			int ageMaleGroup[] = new int[20];
			int ageFemaleGroup[] = new int[20];

			for (HH hh2 : uniqueHousehold) {
				for (HouseholdMember hhm : hh2.getHousehold().getHouseholdMember()) {

					if (hhm.getGender().equals(Sex.Male))
						ageMaleGroup[(int) Math.ceil(hhm.getAge() / 5)]++;
					else if (hhm.getGender().equals(Sex.Female))
						ageFemaleGroup[(int) Math.ceil(hhm.getAge() / 5)]++;
				}
			}
			int i = 0;
			row = 2;
			for (int j : ageMaleGroup) {
				reportWB.getSheet(isheet).setValue(2, row, j, textStyle);
				row++;
			}
			row = 2;
			for (int j : ageFemaleGroup) {
				reportWB.getSheet(isheet).setValue(3, row, j, textStyle);
				row++;
			}
		} else { // Quantile
			Double totMale = 0.0;
			Double totFemale = 0.0;

			reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20, 20);
			reportWB.getSheet(isheet).setValue(1, 1, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 1, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Male%", textStyle);
			reportWB.getSheet(isheet).setValue(4, row, "Female%", textStyle);

			for (HH hh3 : uniqueHousehold) {

				for (HouseholdMember hhm3 : hh3.getHousehold().getHouseholdMember()) {

					if (hhm3.getGender().equals(Sex.Male)) {

						totMale += 1.0;
					} else if (hhm3.getGender().equals(Sex.Female)) {

						totFemale += 1.0;
					}
				}

			}

			row = 2;
			for (Quantile quantile : quantiles) {
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, quantile.getPercentage() * totMale / 100.0, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, quantile.getPercentage() * totFemale / 100.0, textStyle);
				row++;
			}

		}

	}

	/******************************************************************************************************************************************/

	private void createAdultEquivalentreport(int isheet, Report report) {
		int row = 1;
		Double totAE = 0.0;

		System.out.println("Adult Equivalent report");
		reportWB.getSheet(isheet).setColumnWidths(1, 30, 30);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Adult Daily Equivalent KCal", textStyle);

		row = 3;

		for (HH hh3 : uniqueHousehold) {
			totAE += hh3.getHhAE();
		}

		if (!isQuantile) {
			for (HH hh2 : uniqueHousehold) {
				reportWB.getSheet(isheet).setColumnWidths(1, 30, 30);
				reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
				reportWB.getSheet(isheet).setValue(2, row, "Adult Daily Equivalent KCal", textStyle);

				reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.getHhAE(), textStyle);
				row++;
			}
		} else { // Quantiles

			row = 2;
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 30);
			reportWB.getSheet(isheet).setValue(1, 1, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 1, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, 1, "Adult Daily Equivalent KCal", textStyle);
			for (Quantile qq : quantiles) {
				reportWB.getSheet(isheet).setValue(1, row, qq.getSequence(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, qq.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, qq.getPercentage() / 100.0 * totAE, textStyle);
				row++;
			}
		}
	}

	/******************************************************************************************************************************************/

	private Double calcWFIncome(HH hh2, String type) {
		Double wfTot = 0.0;

		/* What about food payments? */

		for (WildFood wf : hh2.getHousehold().getWildFood()) {

			if (type == "cash") {
				wfTot += wf.getUnitsSold() * wf.getPricePerUnit();
			} else if (type == "food") {
				wfTot += wf.getUnitsConsumed() * wf.getResourceSubType().getResourcesubtypekcal();
			}
		}

		return wfTot;
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
			} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)) {
				trTot += tr.getUnitsConsumed() * tr.getFoodResourceSubType().getResourcesubtypekcal()
						* tr.getPeopleReceiving() * tr.getTimesReceived();

			}

		}
		return trTot;
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
				lsTot += lsp.getUnitsConsumed() * lsp.getResourceSubType().getResourcesubtypekcal();
			}
		}

		return lsTot;

	}

	/******************************************************************************************************************************************/

	private Double calcEmpIncome(HH hh2, String type) {
		Double empTot = 0.0;

		/* What about food payments? for cash */
		if (hh2.getHousehold().getEmployment().size() == 0) {
			System.out.println("no employment income");
			return (0.0);
		}
		try {
			for (Employment emp : hh2.getHousehold().getEmployment()) {
				if (type == "cash") {
					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();
				} else if (type == "food") {
					empTot += emp.getPeopleCount() * emp.getUnitsWorked()
							* emp.getFoodResourceSubType().getResourcesubtypekcal();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				cropTot += crop.getUnitsConsumed() * crop.getResourceSubType().getResourcesubtypekcal();
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

	private Double householdAE(Household household) {

		Double totAE = 0.0;
		Double ageReq = 0.0;
		int age = 0;
		Sex gender;

		for (HouseholdMember hhm : household.getHouseholdMember()) {
			age = hhm.getAge();
			gender = hhm.getGender();

			WHOEnergyRequirements whoEnergey = WHOEnergyRequirements.findByAge(age);

			// System.out.println("whoEnergy = " + whoEnergey.getFemale() + " " +
			// whoEnergey.getMale() + " " + gender);
			if (gender == Sex.Female) {
				totAE += whoEnergey.getFemale();
			} else if (gender == Sex.Male) {
				totAE += whoEnergey.getMale();
			}

		}
		/* AE = TE / 2600 */
		totAE = totAE / 2600;
		return (totAE);
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
		i++;
		// Selected HH
		if (isSelectedHouseholds) {
			sheet[0].setValue(2, i, "Households in Report", textStyle);
			i++;
			for (HH hh2 : hhSelected) {
				System.out.println("selected hh = " + hh2.getHhNumber());
				sheet[0].setValue(2, i,
						hh2.getHousehold().getHouseholdNumber() + " " + hh2.getHousehold().getHouseholdName(),
						textStyle);
				i++;
			}
		} else {
			sheet[0].setValue(2, i, "Households in Report", textStyle);
			i++;
			for (HH hh2 : uniqueHousehold) {

				sheet[0].setValue(2, i,
						hh2.getHousehold().getHouseholdNumber() + " " + hh2.getHousehold().getHouseholdName(),
						textStyle);
				i++;
			}
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
		private Double hhAE;
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

		public Double getHhAE() {
			return hhAE;
		}

		public void setHhAE(Double hhAE) {
			this.hhAE = hhAE;
		}

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

	public static class HHSub {
		private int hhid;
		private AssetLand assetLand;
		private AssetLiveStock assetLivestock;
		private ResourceSubType assetRST;
		private Double assetValue;
		private Double hhDI;
		private int column;

		public int getHhid() {
			return hhid;
		}

		public void setHhid(int hhid) {
			this.hhid = hhid;
		}

		public AssetLand getAssetLand() {
			return assetLand;
		}

		public void setAssetLand(AssetLand assetLand) {
			this.assetLand = assetLand;
		}

		public AssetLiveStock getAssetLivestock() {
			return assetLivestock;
		}

		public void setAssetLivestock(AssetLiveStock assetLivestock) {
			this.assetLivestock = assetLivestock;
		}

		public ResourceSubType getAssetRST() {
			return assetRST;
		}

		public void setAssetRST(ResourceSubType assetRST) {
			this.assetRST = assetRST;
		}

		public Double getAssetValue() {
			return assetValue;
		}

		public void setAssetValue(Double assetValue) {
			this.assetValue = assetValue;
		}

		public Double getHhDI() {
			return hhDI;
		}

		public void setHhDI(Double hhDI) {
			this.hhDI = hhDI;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

	}

}