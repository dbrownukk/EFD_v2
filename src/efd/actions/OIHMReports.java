
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
 * */

package efd.actions;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;

public class OIHMReports extends ViewBaseAction implements IForwardAction, JxlsConstants {

	static Double RC = 2200.0 * 365;
	static final int NUMBER_OF_REPORTS = 15;
	private Study study = null;
	// private CustomReportSpec customReportSpec = null;
	private List<Report> reportList;
	private List<Household> households;
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

		/* Only create reports if there are Validated Households */

		households = XPersistence.getManager()
				.createQuery("from Household where study_id = :study and status = :status ")
				.setParameter("study", study.getId()).setParameter("status", Status.Validated).getResultList();

		System.out.println("Validated Households count =" + households.size());

		if (households.size() == 0) {
			System.out.println("no validated households in the is study");
			addError("No Validated Households in this Study");
			closeDialog();
			return;
		}

		// Populate HH array hh
		populateHHArray(households);

		filterHH(customReportSpec);

		try {
			JxlsWorkbook report = createReport(customReportSpec);
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
		} catch (Exception e) {
			addError(e.getMessage());
			closeDialog();
			return;
		}

		closeDialog();
		getView().findObject();
		getView().refreshCollections();
		getView().refresh();

		addMessage("Created OIHM Report for " + customReportSpec.getSpecName());

	}

	private void filterHH(CustomReportSpec customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from hh populate array
		System.out.println("size of hh before filter = " + hh.size());

		if (customReportSpec.getReportSpecUse().size() > 0) // Apply Report Spec Usage Filter - HH must be Validated
		{
			for (ReportSpecUse reportSpecUse : customReportSpec.getReportSpecUse()) {
				for (Household household : reportSpecUse.getHousehold()) {
					for (HH hh2 : hh) {
						if (hh2.getHousehold() != household) {
							hh2.setDelete(true);
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
						System.out.println("a Answer to delete ");
						hh2.setDelete(true);
					} else {
						System.out.println("an  Answer to save ");
						hh2.setDelete(false);
						break;
					}
			}

		}

		hh.removeIf(n -> n.getHousehold() == null);

		System.out.println("size of hh after all filter  = " + hh.size());
		for (HH hh2 : hh) {
			System.out.println("hh now equals after  filter  = " + hh2.getHhNumber() + " "
					+ hh2.getCategory().toArray().toString() + " " + hh2.getResourceType().getResourcetypename() + " "
					+ hh2.getResourceSubType().getResourcetypename());
		}

	}

	private void populateHHArray(List<Household> households) {
		/*
		 * create array of all Validated households for this study
		 * 
		 * they can then be filtered
		 * 
		 */
		ConfigAnswer answer = null;
		for (Household household : households) {

			for (AssetLand asset : household.getAssetLand()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory(); // List of Categories that
																							// include this RST
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (AssetFoodStock asset : household.getAssetFoodStock()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (AssetCash asset : household.getAssetCash()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (AssetLiveStock asset : household.getAssetLiveStock()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (AssetTradeable asset : household.getAssetTradeable()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (AssetTree asset : household.getAssetTree()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (Crop asset : household.getCrop()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (Employment asset : household.getEmployment()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (Inputs asset : household.getInputs()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (LivestockProducts asset : household.getLivestockProducts()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (LivestockSales asset : household.getLivestockSales()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (Transfer asset : household.getTransfer()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}
			for (WildFood asset : household.getWildFood()) {
				ResourceSubType resourceSubType = asset.getResourceSubType();
				Collection<Category> category = asset.getResourceSubType().getCategory();
				ResourceType resourceType = asset.getResourceSubType().getResourcetype();
				addTohhArray(household, category, resourceType, resourceSubType, answer);
			}

			for (ConfigAnswer configAnswer : household.getConfigAnswer()) {
				addTohhArray(household, null, null, null, configAnswer);
			}

		}

		for (HH hh2 : hh) {

			System.out.println("hh now equals = " + hh2.getHhNumber() + " " + hh2.getCategory().toArray().toString()
					+ " " + hh2.getResourceType().getResourcetypename() + " "
					+ hh2.getResourceSubType().getResourcetypename());
		}

	}

	private void addTohhArray(Household household, Collection<Category> category, ResourceType resourceType,
			ResourceSubType resourceSubType, ConfigAnswer answer) {
		HH e = new HH();
		e.household = household;
		e.hhNumber = household.getHouseholdNumber();
		e.category = category;
		e.resourceType = resourceType;
		e.resourceSubType = resourceSubType;
		e.answer = answer;
		hh.add(e);

	}

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
				System.out.println("hh 1");
				createDIreport(ireportNumber, report);
				break;

			default:
				break;
			}
		}

		return reportWB;
	}

	private void createDIreport(int isheet, Report report) {

		int row = 1;
		System.out.println("hh 001");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Disposable Income", textStyle);

		row = 3;

		for (HH hh2 : hh) {
			hh2.hhDI = householdDI(hh2.household);
			reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
			reportWB.getSheet(isheet).setValue(2, row, hh2.hhDI, textStyle);
			row++;
		}
	}

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

		for (Crop crop : household.getCrop()) {
			cropTI += (crop.getUnitsSold().doubleValue() * crop.getPricePerUnit().doubleValue());
			cropOP += (crop.getUnitsConsumed().doubleValue()
					* Double.valueOf(crop.getResourceSubType().getResourcesubtypekcal()));
		}
		for (WildFood wildfood : household.getWildFood()) {
			wildfoodsTI += (wildfood.getUnitsSold().doubleValue() * wildfood.getPricePerUnit().doubleValue());
			cropOP += (wildfood.getUnitsConsumed().doubleValue()
					* Double.valueOf(wildfood.getResourceSubType().getResourcesubtypekcal()));
		}
		for (LivestockProducts livestockProducts : household.getLivestockProducts()) {
			lspTI += (livestockProducts.getUnitsSold() * livestockProducts.getPricePerUnit());
			lspOP += (livestockProducts.getUnitsConsumed().doubleValue()
					* Double.valueOf(livestockProducts.getResourceSubType().getResourcesubtypekcal()));
		}
		for (Employment employment : household.getEmployment()) {
			employmentTI += (employment.getUnitsWorked() * employment.getCashPaymentAmount()
					* employment.getPeopleCount());
			employmentOP += (employment.getUnitsWorked()
					* employment.getFoodResourceSubType().getResourcesubtypekcal());
		}
		for (Transfer transfer : household.getTransfer()) {
			if (transfer.getTransferType().toString().equals("Cash"))
				transfersTI += (transfer.getCashTransferAmount() * transfer.getPeopleReceiving()
						* transfer.getTimesReceived());
			if (transfer.getTransferType().toString().equals("Food"))
				transfersOP += (transfer.getFoodResourceSubType().getResourcesubtypekcal()
						* transfer.getUnitsConsumed());
		}
		requiredCalories = household.getHouseholdMember().size() * RC;

		Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI + transfersTI;
		Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;

		Double shortFall = requiredCalories - output;

		return (totalIncome - output); /// JUST FOR TESTING
	}

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

	private void setSheetStyle(JxlsSheet sheet) {
		sheet.setColumnWidths(1, 20, 60);

	}

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

	public String getForwardURI() {
		return forwardURI;
	}

	public boolean inNewWindow() {
		if (forwardURI == null)
			return false;
		return true;
	}

	public void setForwardURI(String forwardURI) {
		this.forwardURI = forwardURI;
	}

	public static class HH {
		private Household household;
		private int hhNumber;
		private Double hhDI;
		private Collection<Category> category;
		private ResourceType resourceType;
		private ResourceSubType resourceSubType;
		private ConfigAnswer answer;
		private Boolean delete;

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

		public HH() {
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
	}

}