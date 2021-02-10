package efd.actions;



/* Write XLS template for OIHM Study */

import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

import efd.rest.domain.Project;
        import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataValidationConstraint.*;
import org.apache.poi.ss.util.*;
        import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.rest.domain.model.*;
import efd.rest.domain.model.ConfigQuestion.*;

public class CreateXlsFileActionOIHM extends ViewBaseAction implements IForwardAction, JxlsConstants, IChainAction {

	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle textStyleWrap = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	CellStyle style = null;
	CellStyle vstyle = null;
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
	int errno = 0;

	List<ConfigQuestionUse> configQuestionUseHHC = null;
	List<ConfigQuestionUse> configQuestionUseHHM = null;

	private int row;
	private String forwardURI = null;

	Project project;
	Site site;
	static LivelihoodZone livelihoodZone;

	//private String nextAction = "ParseSpreadsheet.Validate";  // Automatically call Validate after the Parse
	private String nextAction = null;
	// Sheet sheet = null;

	public void execute() throws Exception {

		em("in xls generate ");

		try {

			JxlsWorkbook scenario = createScenario();

			if (scenario.equals(null)) {

				addError("Template failed to be created");
			}
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2

			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3

		} catch (NullPointerException em) {
			addError("Template failed to be created " + errno + " ");
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

		em("studyID = " + studyID);

		Study study = XPersistence.getManager().find(Study.class, studyID);
		site = study.getSite();

		if (site == null) {
			addError("Add Site details before creating Template Spreadsheet");
			throw new NullPointerException("No Site");
		}

		livelihoodZone = site.getLivelihoodZone();



		Query query = XPersistence.getManager()
				.createQuery("select idwgresource from WGCharacteristicsResource where study_id = '" + studyID + "'");
		chrs = query.getResultList();

		// em("Number = " + chrs.size());
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

		em("created workbook ");

		boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleWrap = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setWrap(true);

		scenarioWB.setDateFormat("dd/MM/yyyy");
		em("date style = " + scenarioWB.getDateFormat());

		dateStyle = scenarioWB.addStyle(scenarioWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(RIGHT);
		// dateStyle = scenarioWB.getDefaultDateStyle();

		numberStyle = scenarioWB.addStyle(FLOAT).setAlign(RIGHT)
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setCellColor(BLUE);
		f1Style = scenarioWB.addStyle("0.0");

		em("about to create Sheets");

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

		em(" created Sheets");

		/*********************************************************************************************************************************************************/
		/* Start print routines for resources */
		/*********************************************************************************************************************************************************/
		printInterview(interview, project, study, site);
		em("done interview print ");

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
		vstyle = workbook.createCellStyle();
		datestyle = workbook.createCellStyle();
		title = workbook.createCellStyle();

		CreationHelper createHelper = workbook.getCreationHelper();

		datestyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
		datestyle.setBorderBottom(CellStyle.BORDER_THIN);
		datestyle.setBorderTop(CellStyle.BORDER_THIN);
		datestyle.setBorderRight(CellStyle.BORDER_THIN);
		datestyle.setBorderLeft(CellStyle.BORDER_THIN);

		// DataFormat format = workbook.createDataFormat();

		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("2"));
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setAlignment(RIGHT);

		title.setAlignment(RIGHT);

		vstyle.setAlignment(RIGHT);

		/*
		 * see
		 * https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss
		 * /examples/LinkedDropDownLists.java
		 */
		em("about to do validations ");
		Sheet sheet = workbook.createSheet("Validations");

		// Sheet sheetHH = workbook.createSheet("ValidationsHH");
		em("done validations");

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

		em("isheet = " + isheet);

		/* Interview Validations */

		addDateValidation(workbook, sheet, interviewSheet, 7, 7, 2, 2, dateStyle);
		addDateValidation(workbook, sheet, interviewSheet, 5, 5, 4, 4, dateStyle);

		/* HH Validations */

		/*
		 * 0 Text 1.Decimal 2.Integer 3.LOV 4.Integer Range 5.Decimal Range
		 */
		Level level = null;
		AnswerType answerType = null;
		int hhrow = 2;
		for (i = 0; i < configQuestionUseHHC.size(); i++) {


			answerType = configQuestionUseHHC.get(i).getConfigQuestion().getAnswerType();
			level = configQuestionUseHHC.get(i).getConfigQuestion().getLevel();

			if (answerType.equals(AnswerType.Integer) && (level.equals(Level.Household))) {
				addIntegerValidation(workbook, sheet, hhcSheet, hhrow, hhrow, 2, 2, style);
				hhrow++;
			} else if (answerType.equals(AnswerType.Decimal) && (level.equals(Level.Household))) {
				addNumberValidation(workbook, sheet, hhcSheet, hhrow, hhrow, 2, 2, style);
				hhrow++;
			} else if (answerType.equals(AnswerType.IntegerRange) && (level.equals(Level.Household))) {
				Integer intRangeLower = configQuestionUseHHC.get(i).getConfigQuestion().getIntRangeLower();
				Integer intRangeUpper = configQuestionUseHHC.get(i).getConfigQuestion().getIntRangeUpper();
				addIntegerRangeValidation(workbook, sheet, hhcSheet, hhrow, hhrow, 2, 2, style, intRangeLower,
						intRangeUpper);
				hhrow++;
			} else if (answerType.equals(AnswerType.DecimalRange) && (level.equals(Level.Household))) {
				Double decRangeLower = configQuestionUseHHC.get(i).getConfigQuestion().getDecRangeLower();
				Double decRangeUpper = configQuestionUseHHC.get(i).getConfigQuestion().getDecRangeUpper();
				addDecimalRangeValidation(workbook, sheet, hhcSheet, hhrow, hhrow, 2, 2, style, decRangeLower,
						decRangeUpper);
				hhrow++;
			} else if (answerType.equals(AnswerType.LOV) && (level.equals(Level.Household))) {
				// em("about to do LOV question build = ");
				String questionId = configQuestionUseHHC.get(i).getConfigQuestion().getId();
				String questionLOVType = configQuestionUseHHC.get(i).getConfigQuestion().getQuestionLOVType().getId();
				// em("about to do LOV questionID = " + questionId);
				addQuestionLOVValidation(workbook, sheet, hhcSheet, hhrow, hhrow, 2, 2, style, questionId,
						questionLOVType);
				hhrow++;
				em("done LOV question build = ");
			}

		}
		/*
		 * HH Member Validations
		 * 
		 * Note that addLov is firstRow, numrows not firstRow LastRow....
		 */
		em("isheet 22 = " + isheet);

		int numMembers = 21;

		// hhmSheet.setDefaultColumnWidth(40);

		addLOV(sheet, hhmSheet, 4, 4, 2, numMembers, "Sex");

		// Age and Year of Birth Integers
		addNumberValidation(workbook, sheet, hhmSheet, 5, 6, 2, numMembers, style);
		// Head of Household
		addLOV(sheet, hhmSheet, 7, 7, 2, numMembers, "YesNo");

		// Absent ?
		addLOV(sheet, hhmSheet, 8, 8, 2, numMembers, "YesNo");

		// Period away in Months
		addNumberValidation(workbook, sheet, hhmSheet, 10, 10, 2, numMembers, style);

		// Answer Validation
		int qStartRow = 11;
		for (i = 0; i < configQuestionUseHHM.size(); i++) {
			em("hhm question data type = " + configQuestionUseHHM.get(i).getConfigQuestion().getAnswerType());

			answerType = configQuestionUseHHM.get(i).getConfigQuestion().getAnswerType();
			level = configQuestionUseHHM.get(i).getConfigQuestion().getLevel();

			if (answerType.equals(AnswerType.Integer) && (level.equals(Level.HouseholdMember))) {
				addIntegerValidation(workbook, sheet, hhmSheet, qStartRow, qStartRow, 2, numMembers, style);
				qStartRow++;
			} else if (answerType.equals(AnswerType.Decimal) && (level.equals(Level.HouseholdMember))) {
				addNumberValidation(workbook, sheet, hhmSheet, qStartRow, qStartRow, 2, numMembers, style);
				qStartRow++;
			} else if (answerType.equals(AnswerType.IntegerRange)
					&& (level.equals(Level.HouseholdMember))) {
				Integer intRangeLower = configQuestionUseHHM.get(i).getConfigQuestion().getIntRangeLower();
				Integer intRangeUpper = configQuestionUseHHM.get(i).getConfigQuestion().getIntRangeUpper();

				addIntegerRangeValidation(workbook, sheet, hhmSheet, qStartRow, qStartRow, 2, numMembers, style,
						intRangeLower, intRangeUpper);

				qStartRow++;
			} else if (answerType.equals(AnswerType.DecimalRange)
					&& (level.equals(Level.HouseholdMember))) {
				Double decRangeLower = configQuestionUseHHM.get(i).getConfigQuestion().getDecRangeLower();
				Double decRangeUpper = configQuestionUseHHM.get(i).getConfigQuestion().getDecRangeUpper();
				addDecimalRangeValidation(workbook, sheet, hhmSheet, qStartRow, qStartRow, 2, numMembers, style,
						decRangeLower, decRangeUpper);
				qStartRow++;
			} else if (answerType.equals(AnswerType.LOV) && (level.equals(Level.HouseholdMember))) {

				String questionId = configQuestionUseHHM.get(i).getConfigQuestion().getId();

				try {

					String questionLOVType = configQuestionUseHHC.get(i).getConfigQuestion().getQuestionLOVType()
							.getId();
					em("about to do HHM LOV questionID = " + questionId);
					addQuestionLOVValidation(workbook, sheet, hhmSheet, qStartRow, qStartRow, 2, numMembers, style,
							questionId, questionLOVType);
					em("done HHM LOV question build = ");
					qStartRow++;
				} catch (Exception ex) {
					System.out.println(ex);
				}
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
		addNumberValidation(workbook, sheet, cashSheet, 3, numRows, 3, 3, style); // new Exchange Rate

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
		addMarketsFormula(workbook, cropSheet, 'J', 'O');

		/* Livestock Sales */

		addLOV(sheet, lssSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lssSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 7, 7, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 9, 9, style);
		addNumberValidation(workbook, sheet, lssSheet, 3, 30, 11, 11, style);
		addMarketsFormula(workbook, lssSheet, 'H', 'M');

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
		addMarketsFormula(workbook, lspSheet, 'K', 'P');

		/* Employment */

		addLOV(sheet, empSheet, 3, numRows - 1, 1, 1, "Employment");
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 2, 2, style);
		addLOV(sheet, empSheet, 3, numRows - 1, 3, 3, "EmpUnit");
		// addNumberValidation(workbook, sheet, empSheet, 3, numRows, 3, 3, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 4, 4, style);

		addNumberValidation(workbook, sheet, empSheet, 3, 30, 5, 5, style);// cash payment

		addLOV(sheet, empSheet, 3, numRows - 1, 6, 6, "FoodStocks");
		addLOV(sheet, empSheet, 3, numRows - 1, 7, 7, "Unit");
		//addLOV(sheet, empSheet, 3, numRows - 1, 8, 8, "EmpUnit");

		addNumberValidation(workbook, sheet, empSheet, 3, 30, 8, 8, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 10, 10, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 12, 12, style);
		addNumberValidation(workbook, sheet, empSheet, 3, 30, 14, 14, style);
		addMarketsFormula(workbook, empSheet, 'K', 'P');

		/* Transfers */

		addLOV(sheet, transSheet, 3, numRows - 1, 1, 1, "TransferStyle");
		addLOV(sheet, transSheet, 3, numRows - 1, 3, 3, "Transfers");
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 4, 4, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 5, 5, style);
		addNumberValidation(workbook, sheet, transSheet, 3, 30, 6, 6, style);
		addLOV(sheet, transSheet, 3, numRows - 1, 7, 7, "FoodStocks");
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
		addMarketsFormula(workbook, transSheet, 'P', 'U');

		/* Wild Foods */

		em("in wf num validation " + wildfSheet.getSheetName());

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
		addMarketsFormula(workbook, wildfSheet, 'J', 'O');

		/* Inputs */

		addLOV(sheet, inputsSheet, 3, numRows - 1, 1, 1, "Inputs");
		addLOV(sheet, inputsSheet, 3, numRows - 1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 3, 3, style);
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 4, 4, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 5, 5, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 6, 6, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 7, 7, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 8, 8, style);
		addLOV(sheet, inputsSheet, 3, numRows - 1, 9, 9, "InputResource");
		addNumberValidation(workbook, sheet, inputsSheet, 3, 30, 10, 10, style);

		em("done inputs num validation " + wildfSheet.getSheetName());
		addMarketsFormula(workbook, inputsSheet, 'G', 'L'); // allows for 1 formula per sheet (,, startCol, totCol)

		// now hide validation sheet

		workbook.setSheetHidden(workbook.getSheetIndex("Validations"), true);

		/* Return the spreadsheet */
		em("printed ss ");
		return scenarioWB;

		/* end XLS setup */

	}

	/**************************************************************************************************************************************************/
	/* Validations */
	/**************************************************************************************************************************************************/
	private void addMarketsFormula(HSSFWorkbook workbook, Sheet iSheet, char startCol, char totCol) {

		int k = 0;
		int j = 0;

		HSSFSheet mysheet = workbook.getSheet(iSheet.getSheetName());

		HSSFSheetConditionalFormatting my_cond_format_layer = mysheet.getSheetConditionalFormatting();
		HSSFConditionalFormattingRule rule = my_cond_format_layer
				.createConditionalFormattingRule(ComparisonOperator.EQUAL, "100");
		HSSFConditionalFormattingRule rule2 = my_cond_format_layer
				.createConditionalFormattingRule(ComparisonOperator.NOT_EQUAL, "100");

		// HSSFConditionalFormattingRule rule = my_cond_format_layer
		// .createConditionalFormattingRule(ComparisonOperator.NOT_EQUAL, "100", null);

		HSSFPatternFormatting patternFmt = rule.createPatternFormatting();
		patternFmt.setFillBackgroundColor(GREEN);
		HSSFPatternFormatting patternFmt2 = rule2.createPatternFormatting();
		patternFmt2.setFillBackgroundColor(RED);

		String range = totCol + "4:" + totCol + "29";

		CellRangeAddress[] addresses = { CellRangeAddress.valueOf(range) };

		ConditionalFormattingRule[] cfRules = { rule, rule2 };

		my_cond_format_layer.addConditionalFormatting(addresses, rule, rule2);

		char firstCol = startCol;
		char secondCol = firstCol;
		;
		secondCol++;
		secondCol++;
		char thirdCol = secondCol;
		thirdCol++;
		thirdCol++;

		int displayTot = (int) totCol - (int) 'A';

		for (k = 3; k < 29; k++) {

			j = k + 1;

			String formula = "+" + firstCol + j + "+" + secondCol + j + "+" + thirdCol + j;

			iSheet.getRow(k).createCell(displayTot).setCellFormula(formula);

		}

	}

	/**************************************************************************************************************************************************/
	private void calcUnitsConsumed(JxlsSheet sheet, char unitsProducedCol, char unitsSoldCol, char unitsOtherCol,
			int unitsConsumedCol) {

		// Calc for Units Consumeed
		// Units Consumed = unitsProduced-unitsSold-UnitsOther

		for (int j = 4; j < 30; j++) {
			String formula = "+" + unitsProducedCol + j + "-" + unitsSoldCol + j + "-" + unitsOtherCol + j;

			sheet.setFormula(unitsConsumedCol, j, formula);

		}
		
	}

	/**************************************************************************************************************************************************/

	private void addIntegerRangeValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style, Integer intRangeLower, Integer intRangeUpper) {

		try {
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
		} catch (Exception ex) {
			em(ex);
		}

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
				DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0", "1000000001000");

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

		// em("formating region " + region.getNumberOfCells());
		// em("formating region 2 " + firstRow + " " + lastRow);

		// em("firstrow, lastrow, firstCol = " + firstRow + " " +
		// lastRow + " " + firstCol);
		for (i = firstRow; i < lastRow - 1; i++) {

			row = iSheet.getRow(i);
			cell = row.getCell(firstCol);
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
			int firstCol, int lastCol, CellStyle style, String questionID, String questionLOVTypeID) {

		Cell cell = null;
		Name name = null;
		CellRangeAddressList addressList = null;
		int questionCount = 0;

		Row questionRow = vsheet.createRow(validationRowCount);

		em("in configQuestion LOV. QID = " + questionID);

		List<QuestionLOV> questionLOVs = XPersistence.getManager()
				.createQuery("from QuestionLOV where questionLOVType_id  = '" + questionLOVTypeID + "'")
				.getResultList();

		em("done get questionLOV");
		// Get LOV Values for this LOVType and add to Validations sheet

		for (int k = 0; k < questionLOVs.size(); k++) {
			cell = questionRow.createCell(questionCount);
			cell.setCellValue(questionLOVs.get(k).getLovValue());
			questionCount++;
			em("configQuestion LOV = " + questionCount + " " + cell.getStringCellValue());
		}

		// Name the Range

		String questioncol = getCharForNumber(questionCount); // Convert for drop list creation
		name = vsheet.getWorkbook().createName();
		validationRowCount++;

		em("validation range in LOV = " + "!$A$" + validationRowCount + ":$" + questioncol + "$" + validationRowCount);
		name.setRefersToFormula(
				"Validations" + "!$A$" + validationRowCount + ":$" + questioncol + "$" + validationRowCount);

		em("Name info = " + name.getNameName() + " " + name.getRefersToFormula() + " " + name.getSheetIndex() + " "
				+ name.getSheetName());

		String rangeName = "q" + questionID.toString();

		name.setNameName(rangeName);

		// add validation to the question answer

		addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		// addressList = new CellRangeAddressList(8, 9, 2, 22);

		em("addresslist = " + firstRow + "," + lastRow + "," + firstCol + "," + lastCol + "'" + rangeName);

		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(rangeName);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(false); // Allows for other values - combo style
		iSheet.addValidationData(validation);

		em("validation = " + validation.getRegions().getSize());

		// set style to style - right justified
		em("numbering f and l = " + firstRow + " " + lastRow + " " + firstCol + " " + lastCol);

		// setValidationStyle(iSheet, firstRow, lastRow, firstCol, lastCol); // Not
		// Right justifying??

		validationRowCount++;

	}

	/**************************************************************************************************************************************************/

	private void setValidationStyle(Sheet iSheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		Row row;
		Cell cell = null;

		for (int i = firstRow; i <= lastRow; i++) {

			row = iSheet.getRow(i);

			for (int j = firstCol; j <= lastCol; j++) {

				cell = row.createCell(j);

				try {

					cell.setCellStyle(style);
				} catch (Exception ex) {
					em(ex.toString());
				}

			}
		}
	}

	/**************************************************************************************************************************************************/
	private void addIntegerValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, CellStyle style) {

		CellRangeAddressList addressList = null;

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

		// em("in LOV "+firstRow+" "+lastRow+" "+firstCol+" "+lastCol+"
		// "+rType);

		CellRangeAddressList addressList = null;
		for (int l = 3; l < lastRow; l++) {
			// em("LOV = " + rType + firstRow + " " + l + " " + firstCol + " " + lastCol);

			addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
			// addressList = new CellRangeAddressList(firstRow, l, firstCol, lastCol);

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

		int nonfpRowNum = 20;
		int inputsRowNum = 21;

		/* Land Types */
		List<ResourceSubType> rst = XPersistence.getManager()
				.createQuery("from ResourceSubType order by ResourceTypeName").getResultList();

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
		Row nonfpRow = dataSheet.createRow(nonfpRowNum);
		Row inputsRow = dataSheet.createRow(inputsRowNum);

		Row empUnitRow = dataSheet.createRow(empUnitRowNum);

		/* for HH */
		Row sexRow = dataSheet.createRow(sexRowNum);
		Row yesNoRow = dataSheet.createRow(yesNoRowNum);
		Row inputResourceRow = dataSheet.createRow(inputResourceRowNum);

		em("Section 1");

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
		int inputs = 0;

		LocalUnit localUnit;
		LocalUnit localArea;
		ArrayList<String> localUnits = new ArrayList<>();
		ArrayList<String> localAreas = new ArrayList<>();

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
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Crops".toString())
					|| rst.get(k).getResourcetype().getResourcetypename().toString().equals("Wild Foods".toString())
					|| rst.get(k).getResourcetype().getResourcetypename().toString()
							.equals("Livestock Products".toString())) {
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
			/* Non Food Purchase */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Non Food Purchase".toString())) {
				cell = nonfpRow.createCell(nonfp);
				cell.setCellValue(rst.get(k).getResourcetypename());
				nonfp++;

			}

			/* Inputs - Other Tradeable + NFS */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Other Tradeable Goods".toString())
					|| rst.get(k).getResourcetype().getResourcetypename().toString()
							.equals("Non Food Purchase".toString())) {
				cell = inputsRow.createCell(inputs);
				cell.setCellValue(rst.get(k).getResourcetypename());
				inputs++;

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
		String nonfpcol = getCharForNumber(nonfp);

		String inputscol = getCharForNumber(inputs);

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

		em("cropRowNum = " + cropRowNum);
		em("cropcol = " + cropcol);

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

		/* Non Food Purchase */
		name = dataSheet.getWorkbook().createName();
		nonfpRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + nonfpRowNum + ":$" + nonfpcol + "$" + nonfpRowNum);

		name.setNameName("NonFoodPurchase");

		/* Inputs */
		name = dataSheet.getWorkbook().createName();
		inputsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + inputsRowNum + ":$" + inputscol + "$" + inputsRowNum);

		name.setNameName("Inputs");

		/* From Reference Table */
		/* Area */

		List<ReferenceCode> referenceCodeArea = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Area'").getResultList();
		// Get ref codes for Area and add to Validations sheet
		for (int k = 0; k < referenceCodeArea.size(); k++) {
			cell = areaRow.createCell(area);
			cell.setCellValue(referenceCodeArea.get(k).getReferenceName());
			area++;

		}

		// TODO could filter rst down for specific RT using .streams, however,
		// performance is good so far.

		// Get any localunits for RST and livelihoodZone

		em("get any local area units");

		em("about to do lu loop ");
		for (int k = 0; k < rst.size(); k++) {
			try {
				localUnit = ParseWGISpreadsheet.getLocalUnit(livelihoodZone, rst.get(k));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			if (localUnit != null && rst.get(k).getResourcetype().getResourcetypename().trim().equals("Land")) {
				em("found local Land unit =" + localUnit.getName().trim());
				localAreas.add(localUnit.getName().trim());
			}

			else if (localUnit != null && !rst.get(k).getResourcetype().getResourcetypename().trim().equals("Land")) {
				localUnits.add(localUnit.getName().trim());

			}

		}

		List<String> localar = localAreas.stream().distinct().collect(Collectors.toList());

		for (String lu : localar) {
			System.out.println("in localAreas loop lunit = " + lu);
			cell = areaRow.createCell(area);
			cell.setCellValue(lu);
			area++;
		}
		area--;

		String areacol = getCharForNumber(area); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		areaRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + areaRowNum + ":$" + areacol + "$" + areaRowNum);
		name.setNameName("Area");

		/* Unit */

		List<ReferenceCode> referenceCodeUnit = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Unit'").getResultList();

		// Get ref codes for Unit and add to Validations sheet

		for (int k = 0; k < referenceCodeUnit.size(); k++) {
			localUnits.add(referenceCodeUnit.get(k).getReferenceName().trim());
		}

		// Get any localunits for RST and livelihoodZone


		List<String> localus = localUnits.stream().distinct().collect(Collectors.toList());
		em("have lus");
		for (String lu : localus) {
			em("in lus loop lunit = " + lu);
			cell = unitRow.createCell(unit);
			cell.setCellValue(lu);
			unit++;
		}
		unit--;

		String unitcol = getCharForNumber(unit); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		unitRowNum++;

		em("Unit cell info  " + unitRowNum + " " + unitcol);

		name.setRefersToFormula("Validations" + "!$A$" + unitRowNum + ":$" + unitcol + "$" + unitRowNum);

		name.setNameName("Unit");

		/* Sex */

		List<ReferenceCode> referenceCodeSex = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Sex'").getResultList();
		for (int k = 0; k < referenceCodeSex.size(); k++) {
			cell = sexRow.createCell(sex);
			cell.setCellValue(referenceCodeSex.get(k).getReferenceName());
			sex++;

		}

		String sexcol = getCharForNumber(sex); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		sexRowNum++;

		name.setRefersToFormula("Validations" + "!$A$" + sexRowNum + ":$" + sexcol + "$" + sexRowNum);

		name.setNameName("Sex");

		/* Yes or No */

		List<ReferenceCode> referenceCodeYesNo = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'YesNo'").getResultList();
		for (int k = 0; k < referenceCodeYesNo.size(); k++) {
			cell = yesNoRow.createCell(yesno);
			cell.setCellValue(referenceCodeYesNo.get(k).getReferenceName());
			yesno++;

		}

		String yesNocol = getCharForNumber(yesno); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		yesNoRowNum++;

		em("YesNo cell info  " + yesNoRowNum + " " + yesNocol);

		name.setRefersToFormula("Validations" + "!$A$" + yesNoRowNum + ":$" + yesNocol + "$" + yesNoRowNum);

		name.setNameName("YesNo");

		/* Transfer Style */

		List<ReferenceCode> referenceTStyle = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Transfer Style'").getResultList();
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

		List<ReferenceCode> referenceCodeEmpUnit = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'EmpUnit'").getResultList();
		// Get ref codes for Unit and add to Validations sheet
		for (int k = 0; k < referenceCodeEmpUnit.size(); k++) {
			cell = empUnitRow.createCell(empUnit);
			cell.setCellValue(referenceCodeEmpUnit.get(k).getReferenceName());
			em("cell = " + referenceCodeEmpUnit.get(k).getReferenceName());
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
			// em("Currency cell set to " + currency + " " +
			// cell.getStringCellValue());
		}

		String currencycol = getCharForNumber(currency); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		currencyRowNum++;

		name.setRefersToFormula("Validations" + "!$A$" + currencyRowNum + ":$" + currencycol + "$" + currencyRowNum);
		name.setNameName("Currency");

		/* input resources */

		ArrayList<String> inputStrings = new ArrayList(); // Prob want a sorted list..

		List<ResourceSubType> resourceSubTypes = XPersistence.getManager()
				.createQuery("from ResourceSubType order by ResourceType").getResultList();
		List<ResourceType> resourceTypes = XPersistence.getManager().createQuery("from ResourceType").getResultList();
		List<Category> categories = XPersistence.getManager().createQuery("from Category").getResultList();

		// Get codes for Currency and add to Validations sheet

		for (int k = 0; k < categories.size(); k++) {
			inputStrings.add(categories.get(k).getCategoryName().toString());
		}

		for (int k = 0; k < resourceTypes.size(); k++) {
			inputStrings.add(resourceTypes.get(k).getResourcetypename().toString());

		}
		for (int k = 0; k < resourceSubTypes.size(); k++) {
			inputStrings.add(resourceSubTypes.get(k).getResourcetypename().toString());
		}

		// Collections.sort(inputStrings);
		// BUG ISSUE #355

		// switch LOV lookup from Horiz ro Vert as list is over 255 (max for xls )
		
		inputResource = 150;  // Start row for validation array - vertical list
		
		for (int k = 0; k < inputStrings.size(); k++) {
			// for (int k = 0; k < inputStrings.size(); k++) {
			//cell = inputResourceRow.createCell(inputResource);
			//cell.setCellValue(inputStrings.get(k).toString());
			//inputResource++;
			inputResourceRow = dataSheet.createRow(inputResource);	
			cell = inputResourceRow.createCell(0);
			cell.setCellValue(inputStrings.get(k).toString());
			inputResource++;
			
		}

		String inputResourceCol = getCharForNumber(inputResource); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		inputResourceRowNum++;
	//	name.setRefersToFormula(
	//			"Validations" + "!$A$150" + inputResourceRowNum + ":$" + inputResourceCol + "$" + inputResourceRowNum);
		name.setRefersToFormula("Validations" + "!$A$150:$A$" + inputResource);
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

		/*
		 * To track Version of spreadsheet generator and parse now store version number
		 * in database in table Version
		 * 
		 * Single row table CurrentVersion
		 */
		errno = 10001;
		CurrentVersion currentVersion = (CurrentVersion) XPersistence.getManager().createQuery("from CurrentVersion")
				.getSingleResult();

		em("currentVersion = " + currentVersion.getCurrentVersion());

		int j = 2;
		em("in printInterview");
		sheet.setColumnWidths(2, 40, width, width - 15, width, width - 15, width); /* set col widths */

		sheet.setValue(2, 2, "IHM Household Interview - Interview Summary", boldRStyle);

		sheet.setValue(2, 4, "Study Name: ", textStyle); // Study or Project??
		sheet.setValue(3, 4, study.getStudyName() + " " + study.getReferenceYear(), borderStyle);

		// sheet.setValue(2, 6, "Household Number: ", textStyle);
		// sheet.setValue(3, 6, "", borderStyle);

		sheet.setValue(4, 6, "Interview Date: ", textStyle);
		sheet.setValue(5, 6, "", dateStyle);

		sheet.setValue(4, 4, "Site: ", textStyle);
		em("in printInterview 1");
		errno = 10004;
		String locationdistrict = study.getSite().getSubdistrict();
		errno = 10005;
		// String locationdistrict = getView().getValueString("site.locationdistrict");
		if (locationdistrict != null)
			sheet.setValue(5, 4, locationdistrict, borderStyle);
		else
			sheet.setValue(5, 4, "unknown", borderStyle);

		errno = 10006;

		sheet.setValue(2, 6, "Household Name: ", textStyle);
		sheet.setValue(3, 6, "", borderStyle);

		sheet.setValue(6, 6, "Interviewers:", textStyle);
		sheet.setValue(7, 6, "", borderStyle);

		sheet.setValue(6, 4, "Study Start Date: ", textStyle);

		sheet.setValue(7, 4, study.getStartDate(), borderStyle);
		sheet.setValue(7, 4, study.getStartDate(), dateStyle);

		sheet.setValue(2, 10, "Spreadsheet Version " + currentVersion.getCurrentVersion());
		errno = 10010;
	}

	/**************************************************************************************************************************************************/
	private void printHHConfig(JxlsSheet sheet) {

		int i = 0;
		int k = 3;
		/*
		 * need to get Config Questions and Answers for this Study ID where the usage
		 * (ConfigQuestionUse) is at the Household level
		 */

		sheet.setColumnWidths(2, width + 10, width, width, width, width, numwidth);
		sheet.setValue(2, 2, "Household Characteristics", boldRStyle);
		sheet.setValue(3, 2, "Answers", boldRStyle);
		configQuestionUseHHC = XPersistence.getManager().createQuery("from ConfigQuestionUse where study_ID = :studyid")
				.setParameter("studyid", studyID).getResultList();

		if (configQuestionUseHHC.isEmpty())
			return;

		for (i = 0; i < configQuestionUseHHC.size(); i++) {

			if (configQuestionUseHHC.get(i).getConfigQuestion().getLevel() == Level.Household) {
				sheet.setValue(2, k, configQuestionUseHHC.get(i).getConfigQuestion().getPrompt(), textStyleWrap);

				AnswerType answerType = configQuestionUseHHC.get(i).getConfigQuestion().getAnswerType();
				k++; // row
			}
			// SetAnswerTypeValidation(sheet, 3,i+3,answerType) ; //col,row,type
		}

	}

	/**************************************************************************************************************************************************/
	private void printHHMembers(JxlsSheet sheet) {

		int i = 0;
		int k = 0;

		sheet.setColumnWidths(2, width + 10, width, width, width, width, width, width, width, width, width, width,
				width, width, width, width, width, width, width, width, width, width, width);

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
		sheet.setValue(2, 9, "Absent", boldRStyle);
		sheet.setValue(2, 10, "Reason for Absence", boldRStyle);
		sheet.setValue(2, 11, "Period Away in Months", boldRStyle);

		/*
		 * need to get Config Questions and Answers for this Study ID where the usage
		 * (ConfigQuestion) is at the Household Member level (2)
		 */

		configQuestionUseHHM = XPersistence.getManager()
				.createQuery("from ConfigQuestionUse where study_ID = :studyid ").setParameter("studyid", studyID)
				.getResultList();

		em("hhm questions count =  " + configQuestionUseHHM.size());
		if (configQuestionUseHHM.isEmpty())
			return;
		k = 12; // first row for configQuestions
		for (i = 0; i < configQuestionUseHHM.size(); i++) {

			if (configQuestionUseHHM.get(i).getConfigQuestion().getLevel() == Level.HouseholdMember) {
				sheet.setValue(2, k, configQuestionUseHHM.get(i).getConfigQuestion().getPrompt(), textStyleWrap);
				k++;
			}

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

			// em("characteristic rst =
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

		em("done asl create headings");
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

		em("asl set styles abd size");

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
		em("done set styles and size 34");

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

			// em("food stocks get "
			// +
			// wgcharacteristicsresource.getResourcesubtype().getResourcetype().getResourcetypename());

			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			em("in food stocks rst = " + wgcharacteristicsresource.getType());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
			rtunit = wgcharacteristicsresource.getWgresourceunit();
			if (rtunit.isEmpty())
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (wgcharacteristicsresource.getType().toString().equals("Food Stocks")) {
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
		em("Trees print done");

	}

	/**************************************************************************************************************************************************/
	private void printAssetCash(JxlsSheet sheet) {
		{
			/* Cash Sheet - */

			String lzCurrency = "";
			try {
				System.out.println("in printassetcash get site lz curr");
				lzCurrency = site.getLivelihoodZone().getCountry().getCurrency();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();

				lzCurrency = "Unknown";

			}

			sheet.setValue(2, 1, "Livelihood Zone Currency = " + lzCurrency);

			sheet.setValue(2, 3, "Currency", boldTopStyle);
			sheet.setValue(3, 3, "Amount", boldTopStyle);
			sheet.setValue(4, 3, "Exchange Rate", boldTopStyle);

			sheet.setColumnWidths(2, 10, 10, 10); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 5) {
				while (row < numRows) {
					sheet.setValue(col, row, "", borderStyle); /* set borders for data input fields */
					row++;
				}
				col++;
				row = 4;
			}

			em("tree set styles tree size CASH");

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

		/* Crops Sheet - */

		sheet.setValue(2, 3, "Crop Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit", boldTopStyle);
		sheet.setValue(4, 3, "Units Produced", boldTopStyle);
		sheet.setValue(5, 3, "Units Sold", boldTopStyle);
		sheet.setValue(8, 3, "Price per Unit", boldTopStyle);
		sheet.setValue(7, 3, "Units Consumed", boldTopStyle);
		sheet.setValue(6, 3, "Units Other Use", boldTopStyle);
		sheet.setValue(9, 3, "Market 1", boldTopStyle);
		sheet.setValue(10, 3, "% Trade at 1", boldTopStyle);
		sheet.setValue(11, 3, "Market 2", boldTopStyle);
		sheet.setValue(12, 3, "% Trade at 2", boldTopStyle);
		sheet.setValue(13, 3, "Market 3", boldTopStyle);
		sheet.setValue(14, 3, "% Trade at 3", boldTopStyle);
		sheet.setValue(15, 3, "% Total", boldTopStyle);

		sheet.setColumnWidths(2, width, width, numwidth, numwidth, numwidth, numwidth, numwidth, width, numwidth, width,
				numwidth, width, numwidth); /* set col widths */

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
		calcUnitsConsumed(sheet, 'D', 'E', 'F', 7);
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
			sheet.setValue(13, 3, "% Total", boldTopStyle);

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

				if (wgcharacteristicsresource.getType().toString().equals("Livestock Sales")) {
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
			sheet.setValue(9, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(8, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(7, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(10, 3, "Market 1", boldTopStyle);
			sheet.setValue(11, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(12, 3, "Market 2", boldTopStyle);
			sheet.setValue(13, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(14, 3, "Market 3", boldTopStyle);
			sheet.setValue(15, 3, "% Trade at 3", boldTopStyle);
			sheet.setValue(16, 3, "% Total", boldTopStyle);

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
		calcUnitsConsumed(sheet, 'E', 'F', 'G', 8);

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
			sheet.setValue(16, 3, "% Total", boldTopStyle);

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
			sheet.setValue(14, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(13, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(12, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(15, 3, "Market 1", boldTopStyle);
			sheet.setValue(16, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(17, 3, "Market 2", boldTopStyle);
			sheet.setValue(18, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(19, 3, "Market 3", boldTopStyle);
			sheet.setValue(20, 3, "% Trade at 3", boldTopStyle);
			sheet.setValue(21, 3, "% Total", boldTopStyle);

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
		calcUnitsConsumed(sheet, 'J', 'K', 'M', 12);

	}

	/**************************************************************************************************************************************************/
	private void printWildFood(JxlsSheet sheet) {
		{
			/* Wildfood Sheet - */

			sheet.setValue(2, 3, "Wild Food Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(8, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Consumed", boldTopStyle);
			sheet.setValue(6, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(9, 3, "Market 1", boldTopStyle);
			sheet.setValue(10, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(11, 3, "Market 2", boldTopStyle);
			sheet.setValue(12, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(13, 3, "Market 3", boldTopStyle);
			sheet.setValue(14, 3, "% Trade at 3", boldTopStyle);
			sheet.setValue(15, 3, "% Total", boldTopStyle);

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
		calcUnitsConsumed(sheet, 'D', 'E', 'F', 7);
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
			sheet.setValue(7, 3, "Resource 1 %", boldTopStyle);
			sheet.setValue(8, 3, "Resource 2 Used For", boldTopStyle);
			sheet.setValue(9, 3, "Resource 2 %", boldTopStyle);
			sheet.setValue(10, 3, "Resource 3 Used For", boldTopStyle);
			sheet.setValue(11, 3, "Resource 3 %", boldTopStyle);
			sheet.setValue(12, 3, "Total %", boldTopStyle);

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

			// get resource sub type //
			for (int k = 0; k < chrs.size(); k++) {
				/* Get Resource Sub Type */
				WGCharacteristicsResource wgcharacteristicsresource = XPersistence.getManager()
						.find(WGCharacteristicsResource.class, chrs.get(k));

				/* Get Resource Type */
				ResourceType rst = XPersistence.getManager().find(ResourceType.class,
						wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

				// rt = rst.getResourcetypename(); // Inputs is of type Inputs in WGcharres but
				// a NonFoodPurchase
				rt = wgcharacteristicsresource.getType().toString();

				resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
				rtunit = wgcharacteristicsresource.getWgresourceunit();
				if (rtunit.isEmpty())
					rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Inputs")) {
					em("get inputs for out put = " + resub + " " + rtunit);
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

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

	private static void em(Object em) {
		// return;
		System.out.println(em);

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

	@Override
	public String getNextAction() throws Exception {
		// TODO Auto-generated method stub
		return nextAction;
	}
}