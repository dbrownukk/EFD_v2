package efd.actions;

import java.math.*;

/* Write XLS template for OIHM Study */

import java.util.*;

import javax.persistence.*;

import org.apache.poi.hssf.record.CFRuleRecord.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataValidationConstraint.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class CreateXlsFileActionOIHM extends ViewBaseAction implements IForwardAction, JxlsConstants {

	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	CellStyle style = null;
	CellStyle datestyle = null;
	CellStyle title = null;
	CellStyle cnumberStyle = null;

	int width = 30;
	int numwidth = 15;
	int numRows = 30;
	List chrs = null;
	int validationRowCount = 100;
	String rt;
	String resub;
	String rtunit;
	String studyID;

	List<ConfigQuestionUse> configQuestionUseHHC = null;
	List<ConfigQuestionUse> configQuestionUseHHM = null;

	private int row;
	private String forwardURI = null;

	// Sheet sheet = null;

	public void execute() throws Exception {

		System.out.println("in xls gen ");

		try {

			JxlsWorkbook scenario = createScenario();

			if (scenario.equals(null)) {

				addError("Template failed to be created");
			}
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2

			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3

		} catch (NullPointerException em) {
			addError("Template failed to be created - Null ");
		}

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	private JxlsWorkbook createScenario() throws Exception {

		int i = 1;
		int j = 2;
		int k = 0;
		int w = 30;
		int isheet = 0;

		String filename;
		int interviewNumber = 1;

		/* Get EFD Study Details */

		/* Get Study data */

		if (getView().getValueString("id").isEmpty()) {
			addError("No Valid Study ID");
			throw new NullPointerException("Study ID");
		}
		studyID = getView().getValueString("id");

		System.out.println("studyID = " + studyID);

		Study study = XPersistence.getManager().find(Study.class, studyID);
		/*
		 * Check if Study Interview Results header exists and if so is still at
		 * Generated status, otherwise do not allow
		 *
		 * TBD when StudyInt is create
		 */

		/* Get Study Data */
		/*
		 * There may not be a Site or LZ
		 */

		Site site = null;
		if (!getView().getValueString("locationdistrict").isEmpty()) {
			site = XPersistence.getManager().find(Site.class, study.getSite().getLocationid());
			System.out.println("site loc after query = " + site.getLocationdistrict());
		}
		Project project = XPersistence.getManager().find(Project.class, study.getProjectlz().getProjectid());

		/******************
		 * Need to get charresource and types using getmanager and iterate
		 ********************/

		// List wgcharacteristicsresource =

		Query query = XPersistence.getManager()
				.createQuery("select idwgresource from WGCharacteristicsResource where study_id = '" + studyID + "'");
		chrs = query.getResultList();

		// System.out.println("Number = " + chrs.size());
		for (k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));
			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

		}

		filename = study.getProjectlz().getProjecttitle() + '_' + study.getStudyName()
				+ Calendar.getInstance().getTime();

		JxlsWorkbook scenarioWB = new JxlsWorkbook(filename);
		// XSSFWorkbook scenarioWB = new XSSFWorkbook("filename");

		System.out.println("created workbook ");

		boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT).setWrap(true);
		borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT);

		scenarioWB.setDateFormat("dd/MM/yyyy");
		System.out.println("date style = " + scenarioWB.getDateFormat());

		dateStyle = scenarioWB.addStyle(scenarioWB.getDateFormat()).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		// dateStyle = scenarioWB.getDefaultDateStyle();

		numberStyle = scenarioWB.addStyle(FLOAT).setAlign(RIGHT)
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setCellColor(BLUE);
		f1Style = scenarioWB.addStyle("0.0");

		System.out.println("about to create Sheets");

		/* XLS Sheets */
		/*********************************************************************************************************************************************************/
		/* Create sheets */
		/*********************************************************************************************************************************************************/

		JxlsSheet interview = scenarioWB.addSheet("Interview Details");
		JxlsSheet hhConfig = scenarioWB.addSheet("HH Config");
		JxlsSheet hhMembers = scenarioWB.addSheet("HH Members");
		JxlsSheet assetLand = scenarioWB.addSheet("Assets - Land");
		JxlsSheet assetLivestock = scenarioWB.addSheet("Assets - Livestock");
		JxlsSheet assetOtherTradeable = scenarioWB.addSheet("Assets - Other Tradeable");
		JxlsSheet assetFoodStocks = scenarioWB.addSheet("Assets - Food Stocks");
		JxlsSheet assetTrees = scenarioWB.addSheet("Assets - Trees");
		JxlsSheet assetCash = scenarioWB.addSheet("Assets - Cash");
		JxlsSheet crops = scenarioWB.addSheet("Crops");
		JxlsSheet livestockSales = scenarioWB.addSheet("Livestock - Sales");
		JxlsSheet livestockProducts = scenarioWB.addSheet("Livestock - Products");
		JxlsSheet emp = scenarioWB.addSheet("Employment");
		JxlsSheet transfers = scenarioWB.addSheet("Transfers");
		JxlsSheet wildfood = scenarioWB.addSheet("Wild Foods");
		JxlsSheet inputs = scenarioWB.addSheet("Inputs");

		System.out.println(" created Sheets");

		/*********************************************************************************************************************************************************/
		/* Start print routines for resources */
		/*********************************************************************************************************************************************************/
		printInterview(interview, project, study, site);
		System.out.println("done interview print ");

		printHHConfig(hhConfig);
		printHHMembers(hhMembers);

		printAssetLand(assetLand);
		printAssetLivestock(assetLivestock);
		printAssetOtherTradeable(assetOtherTradeable);
		printAssetFoodStocks(assetFoodStocks);
		printAssetTrees(assetTrees);
		printAssetCash(assetCash);
		printCrops(crops);
		printLivestockSales(livestockSales);
		printLivestockProducts(livestockProducts);
		printEmp(emp);
		printTransfers(transfers);
		printWildFood(wildfood);
		printInputs(inputs);
		// printFoodPurchases(foodPurchases);
		// printNonFoodPurchases(nonFoodPurchases);

		/*
		 * add some drop downs and validations using POI have to map jxls wb to hssf wb
		 * 
		 */

		HSSFWorkbook workbook = null;

		/* convert OX jxls to HSSF */

		workbook = (HSSFWorkbook) scenarioWB.createPOIWorkbook();

		// set number validation and format style

		style = workbook.createCellStyle();
		datestyle = workbook.createCellStyle();
		title = workbook.createCellStyle();

		CreationHelper createHelper = workbook.getCreationHelper();

		datestyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

		// DataFormat format = workbook.createDataFormat();

		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("2"));
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);

		title.setAlignment(RIGHT);

		/*
		 * see
		 * https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss
		 * /examples/LinkedDropDownLists.java
		 */
		System.out.println("about to do validations ");
		Sheet sheet = workbook.createSheet("Validations");

		// Sheet sheetHH = workbook.createSheet("ValidationsHH");
		System.out.println("done validations");

		buildValidationSheet(sheet);
		// buildValidationsHH(sheetHH);

		Sheet interviewSheet = workbook.getSheetAt(0);

		Sheet hhcSheet = workbook.getSheetAt(1);
		Sheet hhmSheet = workbook.getSheetAt(2);

		Sheet landSheet = workbook.getSheetAt(3);

		Sheet lsSheet = workbook.getSheetAt(4);

		Sheet otSheet = workbook.getSheetAt(5);
		Sheet fsSheet = workbook.getSheetAt(6);
		Sheet treeSheet = workbook.getSheetAt(7);
		Sheet cashSheet = workbook.getSheetAt(8);
		Sheet cropSheet = workbook.getSheetAt(9);
		Sheet lssSheet = workbook.getSheetAt(10);
		Sheet lspSheet = workbook.getSheetAt(11);
		Sheet empSheet = workbook.getSheetAt(12);
		Sheet transSheet = workbook.getSheetAt(13);
		Sheet wildfSheet = workbook.getSheetAt(14);
		Sheet inputsSheet = workbook.getSheetAt(15);

		
		
		
		
		System.out.println("isheet = " + isheet);

		/* Interview Validations */

		addDateValidation(workbook, sheet, interviewSheet, 7, 7, 2, 2, dateStyle);
		addDateValidation(workbook, sheet, interviewSheet, 5, 5, 4, 4, dateStyle);

		/* HH Validations */

		/*
		 * 0 Text 1.Decimal 2.Integer 3.LOV 4.Integer Range 5.Decimal Range
		 */

		AnswerType answerType = null;

		for (i = 0; i < configQuestionUseHHC.size(); i++) {

			answerType = configQuestionUseHHC.get(i).getConfigQuestion().getAnswerType();

			if (answerType.equals(ConfigQuestion.AnswerType.Integer)) {
				addIntegerValidation(workbook, sheet, hhcSheet, i + 2, i + 2, 2, 2, style);
			} else if (answerType.equals(ConfigQuestion.AnswerType.Decimal)) {
				addNumberValidation(workbook, sheet, hhcSheet, i + 2, i + 2, 2, 2, style);
			} else if (answerType.equals(ConfigQuestion.AnswerType.IntegerRange)) {
				Integer intRangeLower = configQuestionUseHHC.get(i).getConfigQuestion().getIntRangeLower();
				Integer intRangeUpper = configQuestionUseHHC.get(i).getConfigQuestion().getIntRangeUpper();
				addIntegerRangeValidation(workbook, sheet, hhcSheet, i + 2, i + 2, 2, 2, style, intRangeLower,
						intRangeUpper);
			} else if (answerType.equals(ConfigQuestion.AnswerType.DecimalRange)) {
				Double decRangeLower = configQuestionUseHHC.get(i).getConfigQuestion().getDecRangeLower();
				Double decRangeUpper = configQuestionUseHHC.get(i).getConfigQuestion().getDecRangeUpper();
				addDecimalRangeValidation(workbook, sheet, hhcSheet, i + 2, i + 2, 2, 2, style, decRangeLower,
						decRangeUpper);
			} else if (answerType.equals(ConfigQuestion.AnswerType.LOV)) {
				System.out.println("about to do  LOV question build = ");
				String questionId = configQuestionUseHHC.get(i).getConfigQuestion().getId();
				System.out.println("about to do LOV questionID = " + questionId);
				addQuestionLOVValidation(workbook, sheet, hhcSheet, i + 2, i + 2, 2, 2, style, questionId);
				System.out.println("done LOV question build = ");
			}

		}
		/*
		 * HH Member Validations
		 * 
		 * Note that addLov is firstRow, numrows not firstRow LastRow....
		 */

		int numMembers = 20;

		// hhmSheet.setDefaultColumnWidth(40);
		addLOV(sheet, hhmSheet, 4, 5, 2, numMembers, "Sex");

		// Age and Year of Birth Integers
		addNumberValidation(workbook, sheet, hhmSheet, 5, 6, 2, numMembers, style);
		// Head of Household
		addLOV(sheet, hhmSheet, 7, 8, 2, numMembers, "YesNo");

		// Period away in Months
		addNumberValidation(workbook, sheet, hhmSheet, 8, 9, 2, numMembers, style);

		// Answer Validation
		int qStartRow = 9;
		for (i = 0; i < configQuestionUseHHM.size(); i++) {
			System.out.println(
					"hhm question data type = " + configQuestionUseHHM.get(i).getConfigQuestion().getAnswerType());

			answerType = configQuestionUseHHM.get(i).getConfigQuestion().getAnswerType();

			if (answerType.equals(ConfigQuestion.AnswerType.Integer)) {
				addIntegerValidation(workbook, sheet, hhmSheet, i + qStartRow, i + qStartRow, 2, numMembers + 2, style);
			} else if (answerType.equals(ConfigQuestion.AnswerType.Decimal)) {
				addNumberValidation(workbook, sheet, hhmSheet, i + qStartRow, i + qStartRow, 2, numMembers + 2, style);
			} else if (answerType.equals(ConfigQuestion.AnswerType.IntegerRange)) {
				Integer intRangeLower = configQuestionUseHHM.get(i).getConfigQuestion().getIntRangeLower();
				Integer intRangeUpper = configQuestionUseHHM.get(i).getConfigQuestion().getIntRangeUpper();
				addIntegerRangeValidation(workbook, sheet, hhmSheet, i + qStartRow, i + qStartRow, 2, numMembers + 2,
						style, intRangeLower, intRangeUpper);
			} else if (answerType.equals(ConfigQuestion.AnswerType.DecimalRange)) {
				Double decRangeLower = configQuestionUseHHM.get(i).getConfigQuestion().getDecRangeLower();
				Double decRangeUpper = configQuestionUseHHM.get(i).getConfigQuestion().getDecRangeUpper();
				addDecimalRangeValidation(workbook, sheet, hhmSheet, i + qStartRow, i + qStartRow, 2, numMembers + 2,
						style, decRangeLower, decRangeUpper);
			} else if (answerType.equals(ConfigQuestion.AnswerType.LOV)) {
				System.out.println("about to do HHM LOV question build = ");
				String questionId = configQuestionUseHHM.get(i).getConfigQuestion().getId();
				System.out.println("about to do HHM LOV questionID = " + questionId);
				addQuestionLOVValidation(workbook, sheet, hhmSheet, i + qStartRow, i + qStartRow, 2, numMembers + 2,
						style, questionId);
				System.out.println("done HHM LOV question build = ");
			}

		}

		/* Asset Land Type */

		addLOV(sheet, landSheet, 3, numRows - 1, 1, 1, "Land");

		addLOV(sheet, landSheet, 3, numRows - 1, 2, 2, "Area");

		addNumberValidation(workbook, sheet, landSheet, 3, numRows, 3, 3, style);

		/* Assets - Livestock Type */

		addLOV(sheet, lsSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lsSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lsSheet, 3, 30, 3, 3, style); // Number Owned
		addNumberValidation(workbook, sheet, lsSheet, 3, 30, 4, 4, style); // Price per unit

		/* Assets - Other Tradeable Type */

		addLOV(sheet, otSheet, 3, numRows - 1, 1, 1, "OtherTradeableGoods");
		addLOV(sheet, otSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, otSheet, 3, 30, 3, 3, style); // Number Owned
		addNumberValidation(workbook, sheet, otSheet, 3, 30, 4, 4, style); // Price per unit

		/* Assets - Food Stocks */
		addLOV(sheet, fsSheet, 3, numRows - 1, 1, 1, "FoodStocks");
		addLOV(sheet, fsSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, fsSheet, 3, 30, 3, 3, style);

		/* Assets - Trees */
		addLOV(sheet, treeSheet, 3, numRows - 1, 1, 1, "Trees");
		addLOV(sheet, treeSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, treeSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, treeSheet, 3, 30, 4, 4, style);

		/* Assets - Cash */
		addLOV(sheet, cashSheet, 3, numRows - 1, 1, 1, "Currency");
		addNumberValidation(workbook, sheet, cashSheet, 3, 30, 2, 2, style);

		/* Crops */
		addLOV(sheet, cropSheet, 3, numRows - 1, 1, 1, "Crops");
		addLOV(sheet, cropSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 6, 6, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 7, 7, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 9, 9, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 11, 11, style);
		addNumberValidation(workbook, sheet, cropSheet, 3, 30, 13, 13, style);

		/* Livestock Sales */

		addLOV(sheet, lssSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lssSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 7, 7, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 9, 9, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 11, 11, style);

		/* Livestock Products */

		addLOV(sheet, lspSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lspSheet, 3, numRows - 1, 3, 3, "Unit");
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 6, 6, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 7, 7, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 8, 8, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 10, 10, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 12, 12, style);
		addNumberValidation(workbook, sheet, lspSheet, 3, 30, 14, 14, style);

		/* Employment */

		addLOV(sheet, empSheet, 3, numRows - 1, 1, 1, "Employment");
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 2, 2, style);
		addLOV(sheet, empSheet, 3, numRows - 1, 3, 3, "EmpUnit");
		// addNumberValidation(workbook, sheet, empSheet, 3, numRows, 3, 3, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 10, 10, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 12, 12, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 14, 14, style);
		System.out.println("gggg ");

		/* Transfers */

		addLOV(sheet, transSheet, 3, numRows - 1, 1, 1, "TransferStyle");
		addLOV(sheet, transSheet, 3, numRows - 1, 3, 3, "Transfers");
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 6, 6, style);
		addLOV(sheet, transSheet, 3, numRows - 1, 8, 8, "Unit");
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 9, 9, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 10, 10, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 11, 11, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 12, 12, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 13, 13, style);
		;
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 15, 15, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 17, 17, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 19, 19, style);

		/* Wild Foods */

		System.out.println("in wf num validation " + wildfSheet.getSheetName());

		addLOV(sheet, wildfSheet, 3, numRows - 1, 1, 1, "WildFoods");
		addLOV(sheet, wildfSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 6, 6, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 7, 7, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 9, 9, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 11, 11, style);
		addNumberValidation(workbook, sheet, wildfSheet, 3, 30, 13, 13, style);

		/* Inputs */

		addLOV(sheet, inputsSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 3, 4, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 5, 5, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 6, 6, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 7, 7, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 8, 8, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 9, 9, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 10, 10, style);

		System.out.println("done inputs num validation " + wildfSheet.getSheetName());
		addFormula(workbook, sheet, inputsSheet, 3, 30, 3, 4, style); // allows for 1 formula per sheet

		// now hide validation sheet

		workbook.setSheetHidden(workbook.getSheetIndex("Validations"), true);

		/* Return the spreadsheet */
		System.out.println("printed ss ");
		return scenarioWB;

		/* end XLS setup */

	}

	/**************************************************************************************************************************************************/
	/* Validations */
	/**************************************************************************************************************************************************/
	private void addFormula(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow, int firstCol,
			int lastCol, CellStyle style) {

		Cell cell = null;
		int k=0;
		int j = 0;
		
	
		System.out.println("add formula");
		if (iSheet.getSheetName() == "Inputs") // Add % in cols G + I + K and put in L
		{
			// Create conditional format rule for this sheet
			// Is RED until value = 100%
			
			
			HSSFSheet mysheet = workbook.getSheet("inputs");
			
			HSSFSheetConditionalFormatting my_cond_format_layer = mysheet.getSheetConditionalFormatting();
			HSSFConditionalFormattingRule rule = my_cond_format_layer.createConditionalFormattingRule(ComparisonOperator.NOT_EQUAL, "100",null);
			
			 HSSFPatternFormatting patternFmt = rule.createPatternFormatting();
			 patternFmt.setFillBackgroundColor(RED);
		
			 CellRangeAddress[] addresses = {CellRangeAddress.valueOf("L4:L29")};
			 
			 
			 my_cond_format_layer.addConditionalFormatting(addresses,rule);
			
			
			
			for ( k = 3; k < 29; k++) {
				System.out.println("add formula pre ="+k);
				j = k+1;
				iSheet.getRow(k).createCell(11).setCellFormula("G"+j+"+I"+j+"+K"+j);
			}
		}

	}

	/**************************************************************************************************************************************************/

	private void addIntegerRangeValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style, Integer intRangeLower, Integer intRangeUpper) {

		CellRangeAddressList addressList = null;

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.INTEGER, DVConstraint.OperatorType.BETWEEN, intRangeLower.toString(),
				intRangeUpper.toString());

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter an Integer between " + intRangeLower + " and " + intRangeUpper);
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true);
		iSheet.addValidationData(validation);

		/*
		 * the above sets the validation but need to set the cell format to be Number
		 */

	}

	/**************************************************************************************************************************************************/
	private void addDecimalRangeValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style, Double decRangeLower, Double decRangeUpper) {

		CellRangeAddressList addressList = null;

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, decRangeLower.toString(),
				decRangeUpper.toString());

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter a Decimal between " + decRangeLower + " and " + decRangeUpper);
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true);
		iSheet.addValidationData(validation);

		/*
		 * the above sets the validation but need to set the cell format to be Number
		 */

	}

	/**************************************************************************************************************************************************/

	private void addNumberValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style) {

		Row row = null;
		Cell cell = null;

		CellRangeAddressList addressList = null;
		int i = 0;

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0", "1000000");

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter a Number only");

		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true);
		iSheet.addValidationData(validation);

		/*
		 * the above sets the validation but need to set the cell format to be Number
		 */

		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);

		System.out.println("set botttom style = " + style.getBorderBottom());

		// System.out.println("formating region " + region.getNumberOfCells());
		// System.out.println("formating region 2 " + firstRow + " " + lastRow);

		// System.out.println("firstrow, lastrow, firstCol = " + firstRow + " " +
		// lastRow + " " + firstCol);
		for (i = firstRow; i < lastRow - 1; i++) {
			// if (i == firstRow) {
			// System.out.println("formating number cells, col - " + firstCol + " " +
			// iSheet.getSheetName());
			// System.out.println("i and firstRow = " + i + " " + firstRow);
			// System.out.println("style = " + style.getBorderBottom());
			// }

			row = iSheet.getRow(i);
			// System.out.println("cell row = " + i + " " + row.getRowNum());

			cell = row.getCell(firstCol);

			// System.out.println("cell col, row = "+cell.getColumnIndex()+"
			// "+cell.getRowIndex());
			cell.setCellStyle(style);
			cell.setCellValue(0);
		}

	}

	/**************************************************************************************************************************************************/
	/*
	 * Create list of values on validation Page Get question Create LOV Add
	 * validation to the question answer
	 */

	private void addQuestionLOVValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style, String questionID) {

		Cell cell = null;
		Name name = null;
		CellRangeAddressList addressList = null;
		int questionCount = 0;

		Row questionRow = vsheet.createRow(validationRowCount);

		System.out.println("in configQuestion LOV. QID = " + questionID);

		List<QuestionLOV> questionLOVs = XPersistence.getManager()
				.createQuery("from QuestionLOV where configQuestion_ID  = '" + questionID + "')").getResultList();
		// Get LOV Values for Question and add to Validations sheet
		for (int k = 0; k < questionLOVs.size(); k++) {
			cell = questionRow.createCell(questionCount);
			cell.setCellValue(questionLOVs.get(k).getLovValue());
			questionCount++;
			System.out.println("configQuestion LOV = " + questionCount + " " + cell.getStringCellValue());
		}

		// Name the Range

		String questioncol = getCharForNumber(questionCount); // Convert for drop list creation
		name = vsheet.getWorkbook().createName();
		validationRowCount++;

		System.out.println("validation range in LOV = " + "!$A$" + validationRowCount + ":$" + questioncol + "$"
				+ validationRowCount);
		name.setRefersToFormula(
				"Validations" + "!$A$" + validationRowCount + ":$" + questioncol + "$" + validationRowCount);

		System.out.println("Name info = " + name.getNameName() + " " + name.getRefersToFormula() + " "
				+ name.getSheetIndex() + " " + name.getSheetName());

		String rangeName = "q" + questionID.toString();
		name.setNameName(rangeName);

		// add validation to the question answer

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();
		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(rangeName);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(false); // Allows for other values - combo style
		iSheet.addValidationData(validation);

		validationRowCount++;

	}

	/**************************************************************************************************************************************************/
	private void addIntegerValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style) {

		Row row = null;
		Cell cell = null;

		CellRangeAddressList addressList = null;
		int i = 0;

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.INTEGER, DVConstraint.OperatorType.BETWEEN, "0", "1000000");

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter an Integer only");
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true);
		iSheet.addValidationData(validation);

		/*
		 * the above sets the validation but need to set the cell format to be Number
		 */

	}

	/**************************************************************************************************************************************************/

	private void addDateValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			// int firstCol, int lastCol, CellStyle style) {
			int firstCol, int lastCol, JxlsStyle style) {

		CellRangeAddressList addressList = null;
		int i = 0;

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createDateConstraint(OperatorType.BETWEEN, "01/01/2000",
				"01/01/2100", "dd/mm/yyyy");
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter a Date only");
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true);
		iSheet.addValidationData(validation);

	}

	private void addLOV(Sheet vsheet, Sheet iSheet, int firstRow, int lastRow, int firstCol, int lastCol, String rType)
	/*
	 * vsheet validation sheet iSheet input sheet rstype type of validation
	 * 
	 */
	{

		// System.out.println("in LOV "+firstRow+" "+lastRow+" "+firstCol+" "+lastCol+"
		// "+rType);

		CellRangeAddressList addressList = null;
		for (int l = 3; l < lastRow; l++) {
			// System.out.println("LOV = "+rType);
			addressList = new CellRangeAddressList(firstRow, l, firstCol, lastCol);
			DataValidationHelper dvHelper = vsheet.getDataValidationHelper();
			DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(rType);
			DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
			validation.setEmptyCellAllowed(true);
			validation.setShowErrorBox(false); // Allows for other values - combo style
			iSheet.addValidationData(validation);
		}
	}

	private static void buildValidationSheet(Sheet dataSheet) {
		Row row = null;
		Cell cell = null;
		Name name = null;

		/* row for validation data on Validations sheet */
		int interviewRowNum = 0;
		int landRowNum = 1;
		int lsRowNum = 2;
		int areaRowNum = 3;
		int otRowNum = 4;
		int fsRowNum = 5;
		int unitRowNum = 6;
		int treeRowNum = 7;
		int currencyRowNum = 8;
		int cropRowNum = 9;
		int empRowNum = 10;
		int transRowNum = 11;
		int transStyleRowNum = 12;
		int wildfRowNum = 13;
		int hhcRowNum = 14;
		int hhmRowNum = 15;
		int empUnitRowNum = 16;

		/* For HH */
		int sexRowNum = 17;
		int yesNoRowNum = 18;
		int inputResourceRowNum = 19;

		/* Land Types */
		List<ResourceSubType> rst = XPersistence.getManager().createQuery("from ResourceSubType").getResultList();

		Row interviewRow = dataSheet.createRow(interviewRowNum);
		Row laRow = dataSheet.createRow(landRowNum);
		Row lsRow = dataSheet.createRow(lsRowNum);
		Row areaRow = dataSheet.createRow(areaRowNum);
		Row otRow = dataSheet.createRow(otRowNum);
		Row fsRow = dataSheet.createRow(fsRowNum);
		Row unitRow = dataSheet.createRow(unitRowNum);
		Row treeRow = dataSheet.createRow(treeRowNum);
		Row currencyRow = dataSheet.createRow(currencyRowNum);
		Row cropRow = dataSheet.createRow(cropRowNum);
		Row empRow = dataSheet.createRow(empRowNum);
		Row transRow = dataSheet.createRow(transRowNum);
		Row transStyleRow = dataSheet.createRow(transStyleRowNum);
		Row wildfRow = dataSheet.createRow(wildfRowNum);

		Row empUnitRow = dataSheet.createRow(empUnitRowNum);

		/* for HH */
		Row sexRow = dataSheet.createRow(sexRowNum);
		Row yesNoRow = dataSheet.createRow(yesNoRowNum);
		Row inputResourceRow = dataSheet.createRow(inputResourceRowNum);

		System.out.println("Section 1");

		int j = 0;
		int la = 0;
		int ls = 0;
		int ot = 0;
		int fs = 0;
		int area = 0;
		int unit = 0;
		int tree = 0;
		int currency = 0;
		int crop = 0;
		int emp = 0;
		int trans = 0;
		int transStyle = 0;
		int wildf = 0;
		int foodp = 0;
		int nonfp = 0;
		int empUnit = 0;

		/* for HH */

		int sex = 0;
		int yesno = 0;
		int inputResource = 0;

		for (int k = 0; k < rst.size(); k++) {
			/* Land */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Land".toString())) {
				cell = laRow.createCell(la);
				cell.setCellValue(rst.get(k).getResourcetypename());
				la++;

			}
			/* LiveStock */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Livestock".toString())) {
				cell = lsRow.createCell(ls);
				cell.setCellValue(rst.get(k).getResourcetypename());
				ls++;

			}
			/* Other Tradeable */
			if (rst.get(k).getResourcetype().getResourcetypename().toString()
					.equals("Other Tradeable Goods".toString())) {
				cell = otRow.createCell(ot);
				cell.setCellValue(rst.get(k).getResourcetypename());
				ot++;

			}
			/* Asset Food Stocks */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Food Stocks".toString())) {
				cell = fsRow.createCell(fs);
				cell.setCellValue(rst.get(k).getResourcetypename());
				fs++;

			}
			/* Trees */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Trees".toString())) {
				cell = treeRow.createCell(tree);
				cell.setCellValue(rst.get(k).getResourcetypename());
				tree++;

			}
			/* Crops */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Crops".toString())) {
				cell = cropRow.createCell(crop);
				cell.setCellValue(rst.get(k).getResourcetypename());
				crop++;

			}
			/* Employment */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Employment".toString())) {
				cell = empRow.createCell(emp);
				cell.setCellValue(rst.get(k).getResourcetypename());
				emp++;

			}
			/* Transfers */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Transfers".toString())) {
				cell = transRow.createCell(trans);
				cell.setCellValue(rst.get(k).getResourcetypename());
				trans++;

			}
			/* Wild Foods */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Wild Foods".toString())) {
				cell = wildfRow.createCell(wildf);
				cell.setCellValue(rst.get(k).getResourcetypename());
				wildf++;

			}

		}

		String lacol = getCharForNumber(la);
		String lscol = getCharForNumber(ls);
		String otcol = getCharForNumber(ot);
		String fscol = getCharForNumber(fs);
		String treecol = getCharForNumber(tree); // Convert for drop list creation

		String cropcol = getCharForNumber(crop);

		String empcol = getCharForNumber(emp);
		String transcol = getCharForNumber(trans);
		String wildfcol = getCharForNumber(wildf);

		/* Land */
		name = dataSheet.getWorkbook().createName();
		landRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + landRowNum + ":$" + lacol + "$" + landRowNum);
		// lists..
		name.setNameName("Land");

		/* LS */
		name = dataSheet.getWorkbook().createName();
		lsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + lsRowNum + ":$" + lscol + "$" + lsRowNum);

		name.setNameName("Livestock");

		/* Other Tradeable */
		name = dataSheet.getWorkbook().createName();
		otRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + otRowNum + ":$" + otcol + "$" + otRowNum);

		name.setNameName("OtherTradeableGoods");

		/* Asset Food Stocks */
		name = dataSheet.getWorkbook().createName();
		fsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + fsRowNum + ":$" + fscol + "$" + fsRowNum);

		name.setNameName("FoodStocks");

		/* Asset Trees */
		name = dataSheet.getWorkbook().createName();
		treeRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + treeRowNum + ":$" + treecol + "$" + treeRowNum);

		name.setNameName("Trees");

		/* Crops */
		name = dataSheet.getWorkbook().createName();

		cropRowNum++;

		System.out.println("cropRowNum = " + cropRowNum);
		System.out.println("cropcol = " + cropcol);

		name.setRefersToFormula("Validations" + "!$A$" + cropRowNum + ":$" + cropcol + "$" + cropRowNum);

		name.setNameName("Crops");

		/* Emp */
		name = dataSheet.getWorkbook().createName();
		empRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + empRowNum + ":$" + empcol + "$" + empRowNum);

		name.setNameName("Employment");

		/* Transfers */
		name = dataSheet.getWorkbook().createName();
		transRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + transRowNum + ":$" + transcol + "$" + transRowNum);

		name.setNameName("Transfers");

		/* Wild Foods */
		name = dataSheet.getWorkbook().createName();
		wildfRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + wildfRowNum + ":$" + wildfcol + "$" + wildfRowNum);

		name.setNameName("WildFoods");

		/* From Reference Table */
		/* Area */

		List<ReferenceCode> referenceCodeArea = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Area')").getResultList();
		// Get ref codes for Area and add to Validations sheet
		for (int k = 0; k < referenceCodeArea.size(); k++) {
			cell = areaRow.createCell(area);
			cell.setCellValue(referenceCodeArea.get(k).getReferenceName());
			area++;
			System.out.println("LA cell set to " + area + " " + cell.getStringCellValue());
		}

		String areacol = getCharForNumber(area); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		areaRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + areaRowNum + ":$" + areacol + "$" + areaRowNum);
		name.setNameName("Area");

		/* Unit */

		List<ReferenceCode> referenceCodeUnit = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Unit')").getResultList();
		// Get ref codes for Unit and add to Validations sheet
		for (int k = 0; k < referenceCodeUnit.size(); k++) {
			cell = unitRow.createCell(unit);
			cell.setCellValue(referenceCodeUnit.get(k).getReferenceName());
			unit++;

		}

		String unitcol = getCharForNumber(unit); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		unitRowNum++;

		System.out.println("Unit cell info  " + unitRowNum + " " + unitcol);

		name.setRefersToFormula("Validations" + "!$A$" + unitRowNum + ":$" + unitcol + "$" + unitRowNum);

		name.setNameName("Unit");

		/* Sex */

		List<ReferenceCode> referenceCodeSex = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Sex')").getResultList();
		for (int k = 0; k < referenceCodeSex.size(); k++) {
			cell = sexRow.createCell(sex);
			cell.setCellValue(referenceCodeSex.get(k).getReferenceName());
			sex++;

		}

		String sexcol = getCharForNumber(sex); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		sexRowNum++;

		System.out.println("Sex cell info  " + sexRowNum + " " + unitcol);

		name.setRefersToFormula("Validations" + "!$A$" + sexRowNum + ":$" + sexcol + "$" + sexRowNum);

		name.setNameName("Sex");

		/* Yes or No */

		List<ReferenceCode> referenceCodeYesNo = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'YesNo')").getResultList();
		for (int k = 0; k < referenceCodeYesNo.size(); k++) {
			cell = yesNoRow.createCell(yesno);
			cell.setCellValue(referenceCodeYesNo.get(k).getReferenceName());
			yesno++;

		}

		String yesNocol = getCharForNumber(yesno); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		yesNoRowNum++;

		System.out.println("YesNo cell info  " + yesNoRowNum + " " + yesNocol);

		name.setRefersToFormula("Validations" + "!$A$" + yesNoRowNum + ":$" + yesNocol + "$" + yesNoRowNum);

		name.setNameName("YesNo");

		/* Transfer Style */

		List<ReferenceCode> referenceTStyle = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Transfer Style')").getResultList();
		// Get ref codes for Transger Style Official / Unofficial and add to Validations
		// sheet
		for (int k = 0; k < referenceTStyle.size(); k++) {
			cell = transStyleRow.createCell(transStyle);
			cell.setCellValue(referenceTStyle.get(k).getReferenceName());
			transStyle++;
		}

		String transStylecol = getCharForNumber(transStyle); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		transStyleRowNum++;

		name.setRefersToFormula(
				"Validations" + "!$A$" + transStyleRowNum + ":$" + transStylecol + "$" + transStyleRowNum);
		name.setNameName("TransferStyle");

		/* Emp work Units */
		System.out.println("Emp Unit 441");
		List<ReferenceCode> referenceCodeEmpUnit = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'EmpUnit')").getResultList();
		// Get ref codes for Unit and add to Validations sheet
		for (int k = 0; k < referenceCodeEmpUnit.size(); k++) {
			cell = empUnitRow.createCell(empUnit);
			cell.setCellValue(referenceCodeEmpUnit.get(k).getReferenceName());
			System.out.println("cell = " + referenceCodeEmpUnit.get(k).getReferenceName());
			empUnit++;

		}

		String empUnitcol = getCharForNumber(empUnit); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		empUnitRowNum++;

		name.setRefersToFormula("Validations" + "!$A$" + empUnitRowNum + ":$" + empUnitcol + "$" + empUnitRowNum);
		name.setNameName("EmpUnit");

		/* Cash from Currency Table */

		List<Country> country = XPersistence.getManager().createQuery("from Country")
				.getResultList(); /* Currency unique - probably not */
		// Get codes for Currency and add to Validations sheet
		for (int k = 0; k < country.size(); k++) {
			cell = currencyRow.createCell(currency);
			cell.setCellValue(country.get(k).getCurrency());
			currency++;
			// System.out.println("Currency cell set to " + currency + " " +
			// cell.getStringCellValue());
		}

		String currencycol = getCharForNumber(currency); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		currencyRowNum++;

		name.setRefersToFormula("Validations" + "!$A$" + currencyRowNum + ":$" + currencycol + "$" + currencyRowNum);
		name.setNameName("Currency");

		/* input resources */

		ArrayList<String> inputStrings = new ArrayList(); // Prob want a sorted list..

		List<ResourceSubType> resourceSubTypes = XPersistence.getManager().createQuery("from ResourceSubType")
				.getResultList();
		List<ResourceType> resourceTypes = XPersistence.getManager().createQuery("from ResourceType").getResultList();
		List<Category> categories = XPersistence.getManager().createQuery("from Category").getResultList();

		// Get codes for Currency and add to Validations sheet
		for (int k = 0; k < resourceSubTypes.size(); k++) {
			inputStrings.add(resourceSubTypes.get(k).getResourcetypename().toString());
		}
		for (int k = 0; k < resourceTypes.size(); k++) {
			inputStrings.add(resourceTypes.get(k).getResourcetypename().toString());

		}
		for (int k = 0; k < categories.size(); k++) {
			inputStrings.add(categories.get(k).getCategoryName().toString());
		}
		Collections.sort(inputStrings);
		for (int k = 0; k < inputStrings.size(); k++) {
			cell = inputResourceRow.createCell(inputResource);
			cell.setCellValue(inputStrings.get(k).toString());
			inputResource++;
		}

		String inputResourceCol = getCharForNumber(inputResource); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		inputResourceRowNum++;
		name.setRefersToFormula(
				"Validations" + "!$A$" + inputResourceRowNum + ":$" + inputResourceCol + "$" + inputResourceRowNum);
		name.setNameName("InputResource");

	}

	public int populateRow(Row row, String label, String[] data) {
		Cell cell = null;
		int columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue(label);
		for (String item : data) {
			cell = row.createCell(columnIndex++);
			cell.setCellValue(item);
		}
		return (columnIndex);
	}

	/**************************************************************************************************************************************************/
	/* Print Each Sheet */
	/**************************************************************************************************************************************************/
	private void printInterview(JxlsSheet sheet, Project project, Study study, Site site) {

		int j = 2;
		System.out.println("in printInterview");
		sheet.setColumnWidths(2, 40, width, width, width, width, width); /* set col widths */

		sheet.setValue(2, 2, "IHM Household Interview - Interview Summary", boldRStyle);

		sheet.setValue(2, 4, "Project Name: ", textStyle); // Study or Project??
		sheet.setValue(3, 4, project.getProjecttitle(), borderStyle);

		// sheet.setValue(2, 6, "Household Number: ", textStyle);
		// sheet.setValue(3, 6, "", borderStyle);

		sheet.setValue(4, 6, "Interview Date: ", textStyle);
		sheet.setValue(5, 6, "", dateStyle);

		sheet.setValue(4, 4, "Site: ", textStyle);
		System.out.println("in printInterview 1");

		String locationdistrict = getView().getValueString("site.locationdistrict");
		if (locationdistrict != null)
			sheet.setValue(5, 4, locationdistrict, borderStyle);
		else
			sheet.setValue(5, 4, "", borderStyle);

		sheet.setValue(2, 6, "Household Name: ", textStyle);
		sheet.setValue(3, 6, "", borderStyle);

		sheet.setValue(6, 6, "Interviewers:", textStyle);
		sheet.setValue(7, 6, "", borderStyle);

		System.out.println("in printInterview 2");

		sheet.setValue(6, 4, "Study Start Date: ", textStyle);

		sheet.setValue(7, 4, study.getStartDate(), dateStyle);

		// sheet.setValue(6, 8, "Type of Year: ", textStyle);
		// sheet.setValue(7, 8, "", borderStyle);

	}

	/**************************************************************************************************************************************************/
	private void printHHConfig(JxlsSheet sheet) {

		int i = 0;
		/*
		 * need to get Config Questions and Answers for this Study ID where the usage
		 * (ConfigQuestionUse) is at the Household level
		 */
		sheet.setColumnWidths(2, width, width, width, width, width, numwidth);
		sheet.setValue(2, 2, "Household Characteristics", boldRStyle);
		sheet.setValue(3, 2, "Answers", boldRStyle);
		configQuestionUseHHC = XPersistence.getManager()
				.createQuery("from ConfigQuestionUse where study_ID = :studyid and level = '1'")
				.setParameter("studyid", studyID).getResultList();

		if (configQuestionUseHHC.isEmpty())
			return;

		for (i = 0; i < configQuestionUseHHC.size(); i++) {
			sheet.setValue(2, i + 3, configQuestionUseHHC.get(i).getConfigQuestion().getPrompt(), textStyle);

			AnswerType answerType = configQuestionUseHHC.get(i).getConfigQuestion().getAnswerType();
			// SetAnswerTypeValidation(sheet, 3,i+3,answerType) ; //col,row,type
		}

	}

	/**************************************************************************************************************************************************/
	private void printHHMembers(JxlsSheet sheet) {

		int i = 0;
		int k = 0;

		sheet.setColumnWidths(2, width, width, width, width, width, width, width, width, width, width, width, width,
				width, width, width, width, width, width, width, width, width, width);

		sheet.setValue(2, 2, "Household Members", boldRStyle);
		for (i = 3; i <= 22; i++) {
			k = i - 2;
			sheet.setValue(i, 3, "Member  " + k, boldRStyle);
		}

		sheet.setValue(2, 4, "Person Name/ID", boldRStyle);
		sheet.setValue(2, 5, "Sex", boldRStyle);
		sheet.setValue(2, 6, "Age", boldRStyle);
		sheet.setValue(2, 7, "Year of Birth", boldRStyle);
		sheet.setValue(2, 8, "Head of Household", boldRStyle);
		sheet.setValue(2, 9, "Period Away in Months", boldRStyle);

		/*
		 * need to get Config Questions and Answers for this Study ID where the usage
		 * (ConfigQuestionUse) is at the Household Member level (2)
		 */

		configQuestionUseHHM = XPersistence.getManager()
				.createQuery("from ConfigQuestionUse where study_ID = :studyid and level = '2'")
				.setParameter("studyid", studyID).getResultList();

		System.out.println("hhm questions count =  " + configQuestionUseHHM.size());
		if (configQuestionUseHHM.isEmpty())
			return;

		for (i = 0; i < configQuestionUseHHM.size(); i++) {
			System.out.println("hhm questions  =  " + configQuestionUseHHM.get(i).getConfigQuestion().getPrompt());
			sheet.setValue(2, i + 10, configQuestionUseHHM.get(i).getConfigQuestion().getPrompt(), textStyle);
		}

		return;
	}

	/**************************************************************************************************************************************************/

	private void printAssetLand(JxlsSheet sheet) {

		/* Asset Land Sheet */

		sheet.setValue(2, 3, "Land Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit e.g. Hectare", boldTopStyle);
		sheet.setValue(4, 3, "Number of Units", boldTopStyle);

		/* set grid for data input */

		int col = 2;
		int row = 4;
		sheet.setColumnWidths(2, 40, numwidth, numwidth); /* set col widths */
		while (col < 5) {

			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				if (col == 4)
					sheet.setColumnStyles(col, numberStyle);
				row++;
			}

			col++;
			row = 4;
		}

		// sheet.setColumnStyles(3, f1Style);
		sheet.setColumnStyles(3, numberStyle);

		row = 4;
		int landrow = 4;
		// get resource sub type //

		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */

			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			// System.out.println("characteristic rst =
			// "+wgcharacteristicsresource.getResourcesubtype().getResourcetypename());

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Land")) {
				sheet.setValue(2, landrow, resub, borderStyle);
				sheet.setValue(3, landrow, rtunit, borderStyle);
				landrow++;
			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printAssetLivestock(JxlsSheet sheet) {

		/* Asset Livestock Sheet */

		sheet.setValue(2, 3, "Livestock Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Owned at Start of Year", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);

		System.out.println("done asl create headings");
		sheet.setColumnWidths(2, 40, numwidth, numwidth, numwidth); /* set col widths */

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		System.out.println("asl set styles abd size");

		row = 4;
		int landrow = 5;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Livestock")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printAssetOtherTradeable(JxlsSheet sheet) {

		/* Asset Tradeable Sheet */

		sheet.setValue(2, 3, "Other Tradeable Goods", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Number", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);
		sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth); /* set col widths */

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
		int landrow = 5;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Other Tradeable Goods")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}
		System.out.println("done set styles and size 34");

	}

	/**************************************************************************************************************************************************/
	private void printAssetFoodStocks(JxlsSheet sheet) {

		/* Asset Food Stock Sheet */

		sheet.setValue(2, 3, "Food Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Quantity", boldTopStyle);

		/* set grid for data input */

		int col = 2;
		int row = 4;
		sheet.setColumnWidths(2, width, numwidth, numwidth); /* set col widths */
		while (col < 5) {

			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				if (col == 4)
					sheet.setColumnStyles(col, numberStyle);
				// sheetstyleol, row, "", numberStyle); /* set number of Units to Number
				// type */
				row++;
			}

			col++;
			row = 4;
		}

		sheet.setColumnStyles(3, numberStyle);

		row = 4;
		int fsrow = 4;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));
			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Food Stocks")) {
				sheet.setValue(2, fsrow, resub, borderStyle);
				sheet.setValue(3, fsrow, rtunit, borderStyle);
				fsrow++;
			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printAssetTrees(JxlsSheet sheet) {
		/* Asset Trees Sheet - Trees sold for timber */

		sheet.setValue(2, 3, "Tree Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Number", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);

		System.out.println("done tree create headings");
		sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth); /* set col widths */

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
		int treerow = 5;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Trees")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printAssetCash(JxlsSheet sheet) {
		{
			/* Cash Sheet - */

			sheet.setValue(2, 3, "Currency", boldTopStyle);
			sheet.setValue(3, 3, "Amount", boldTopStyle);

			sheet.setColumnWidths(2, 10, 10); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 4) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			// System.out.println("tree set styles tree size CASH");

			row = 4;
			int cashrow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Cash")) {
					sheet.setValue(2, row, resub, borderStyle);
					// sheet.setValue(3, row, rtunit, borderStyle);
					// Need to remove Unit for Asset Cash
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printCrops(JxlsSheet sheet) {
		{
			/* Crops Sheet - */

			sheet.setValue(2, 3, "Crop Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(8, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(9, 3, "Market 1", boldTopStyle);
			sheet.setValue(10, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(11, 3, "Market 2", boldTopStyle);
			sheet.setValue(12, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(13, 3, "Market 3", boldTopStyle);
			sheet.setValue(14, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, width, numwidth, numwidth, numwidth, numwidth, numwidth, width, numwidth,
					width, numwidth, width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 15) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int cropsrow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.contains("Crops")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printLivestockSales(JxlsSheet sheet) {
		{
			/* Livestock Sales Sheet - */

			sheet.setValue(2, 3, "Livestock Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units at Start of Year", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Market 1", boldTopStyle);
			sheet.setValue(8, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(9, 3, "Market 2", boldTopStyle);
			sheet.setValue(10, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(11, 3, "Market 3", boldTopStyle);
			sheet.setValue(12, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth, numwidth, width, width, numwidth, width,
					numwidth, width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 13) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int lssrow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Livestock Sales")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printLivestockProducts(JxlsSheet sheet) {
		{
			/* Livestock Products Sheet - */

			sheet.setValue(2, 3, "Livestock Type", boldTopStyle);
			sheet.setValue(3, 3, "Product (e.g. Milk", boldTopStyle);
			sheet.setValue(4, 3, "Unit", boldTopStyle);
			sheet.setValue(5, 3, "Units Produced", boldTopStyle);
			sheet.setValue(6, 3, "Units Sold", boldTopStyle);
			sheet.setValue(7, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(8, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(9, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(10, 3, "Market 1", boldTopStyle);
			sheet.setValue(11, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(12, 3, "Market 2", boldTopStyle);
			sheet.setValue(13, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(14, 3, "Market 3", boldTopStyle);
			sheet.setValue(15, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, width, width, numwidth, numwidth, numwidth, numwidth, numwidth, width,
					numwidth, width, numwidth, width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 16) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int lsprow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Livestock Products")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(4, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printEmp(JxlsSheet sheet) {
		{
			/* Employment Sheet - */

			sheet.setValue(2, 3, "Employment Type", boldTopStyle);
			sheet.setValue(3, 3, "Number of People Working", boldTopStyle);
			sheet.setValue(4, 3, "Unit of Work  e.g. Day, Month", boldTopStyle);
			sheet.setValue(5, 3, "Units worked  e.g. Days, Months", boldTopStyle);
			sheet.setValue(6, 3, "Cash Pay:Amount per Unit of Work", boldTopStyle);
			sheet.setValue(7, 3, "Food Pay:Food Type", boldTopStyle);
			sheet.setValue(8, 3, "Food Pay:Unit", boldTopStyle);
			sheet.setValue(9, 3, "Food Pay:Units Paid / Work Unit", boldTopStyle);
			sheet.setValue(10, 3, "Work Location 1", boldTopStyle);
			sheet.setValue(11, 3, "% Work at 1", boldTopStyle);
			sheet.setValue(12, 3, "Work Location 2", boldTopStyle);
			sheet.setValue(13, 3, "% Work at 2", boldTopStyle);
			sheet.setValue(14, 3, "Work Location 3", boldTopStyle);
			sheet.setValue(15, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth, numwidth, width, numwidth, numwidth, width,
					numwidth, width, numwidth, width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 16) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;

			int emprow = 5;

			// get resource sub type //

			for (int k = 0; k < chrs.size(); k++) {

				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();

				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Employment")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(9, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/

	private void printTransfers(JxlsSheet sheet) {
		{
			/* Transfers Sheet - */
			sheet.setValue(2, 3, "Official/Unofficial", boldTopStyle);
			sheet.setValue(3, 3, "Source e.g. Kin", boldTopStyle);
			sheet.setValue(4, 3, "Transfer Type (e.g. Food, Cash, Other)", boldTopStyle);
			sheet.setValue(5, 3, "No. People Receiving", boldTopStyle);
			sheet.setValue(6, 3, "No. of Times Received", boldTopStyle);
			sheet.setValue(7, 3, "Cash Transfer Amount", boldTopStyle);
			sheet.setValue(8, 3, "Food or Other Transfer:Type of food/other resource", boldTopStyle);
			sheet.setValue(9, 3, "Unit", boldTopStyle);
			sheet.setValue(10, 3, "Units Received / Transferred", boldTopStyle);
			sheet.setValue(11, 3, "Units Sold", boldTopStyle);
			sheet.setValue(12, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(13, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(14, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(15, 3, "Market 1", boldTopStyle);
			sheet.setValue(16, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(17, 3, "Market 2", boldTopStyle);
			sheet.setValue(18, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(19, 3, "Market 3", boldTopStyle);
			sheet.setValue(20, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, width, width, numwidth, numwidth, numwidth, width, numwidth, numwidth,
					numwidth, numwidth, numwidth, numwidth, width, numwidth, width, numwidth, width, numwidth);

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 21) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int trrow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Transfers")) {
					sheet.setValue(4, row, resub, borderStyle);
					sheet.setValue(9, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printWildFood(JxlsSheet sheet) {
		{
			/* Wildfood Sheet - */

			sheet.setValue(2, 3, "Wild Food Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(8, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(9, 3, "Market 1", boldTopStyle);
			sheet.setValue(10, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(11, 3, "Market 2", boldTopStyle);
			sheet.setValue(12, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(13, 3, "Market 3", boldTopStyle);
			sheet.setValue(14, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, width, numwidth, numwidth, numwidth, numwidth, numwidth, width, numwidth,
					width, numwidth, width, numwidth);
			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 15) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int wfrow = 5;
			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				rt = rst.getResourcetypename();
				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Wild Foods")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}

	}

	/**************************************************************************************************************************************************/
	private void printInputs(JxlsSheet sheet) {
		{
			/* Inputs Sheet - */

			sheet.setValue(2, 3, "Items Purchased", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Purchased", boldTopStyle);
			sheet.setValue(5, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(6, 3, "Resource 1 Used For", boldTopStyle);
			sheet.setValue(7, 3, "%", boldTopStyle);
			sheet.setValue(8, 3, "Resource 2 Used For", boldTopStyle);
			sheet.setValue(9, 3, "%", boldTopStyle);
			sheet.setValue(10, 3, "Resource 3 Used For", boldTopStyle);
			sheet.setValue(11, 3, "%", boldTopStyle);

			sheet.setColumnWidths(2, width, width, numwidth, numwidth, width, numwidth, width, numwidth, width,
					numwidth);
			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 12) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			row = 4;
			int wfrow = 5;
			// No existing rows so dot need to get them //

		}

	}

	/**************************************************************************************************************************************************/

	private void printFoodPurchases(JxlsSheet sheet) {

		/* Food Purchase Sheet */

		sheet.setValue(2, 3, "Food Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Units Purchased", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);

		sheet.setColumnWidths(2, width, width, numwidth, numwidth); /* set col widths */

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
		int fprow = 5;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Food Purchase")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

	}

	/**************************************************************************************************************************************************/

	private void printNonFoodPurchases(JxlsSheet sheet) {

		/* Non Food Purchase Sheet */

		sheet.setValue(2, 3, "Item Purchased", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Units Purchased", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);

		sheet.setColumnWidths(2, width, width, numwidth, numwidth); /* set col widths */

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numRows) {
				sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
		int nfprow = 5;
		// get resource sub type //
		for (int k = 0; k < chrs.size(); k++) {
			/* Get Resource Sub Type */
			WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
					.find(WGCharacteristicsResource.class, chrs.get(k));

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Non Food Purchase")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

	}

	/**************************************************************************************************************************************************/
	/* convert number to alpha bet numbered character i.e. 3 =C */
	public static String getCharForNumber(int i) {

		String columnLetter = CellReference.convertNumToColString(i);

		return columnLetter;

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
}