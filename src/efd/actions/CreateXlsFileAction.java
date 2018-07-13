package efd.actions;

/* Write XLS template for Community Interview */

import java.util.*;

import javax.persistence.*;

import org.hsqldb.lib.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.view.*;
import org.openxava.web.servlets.*;

import com.openxava.naviox.model.*;

import antlr.*;

import org.openxava.tab.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;
import sun.swing.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.DVConstraint;

//public class CreateXlsFileAction2 extends CollectionElementViewBaseAction implements IForwardAction, JxlsConstants { // 1
public class CreateXlsFileAction extends CollectionBaseAction implements IForwardAction, JxlsConstants {

	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle datestyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	int width = 30;
	int numwidth = 15;
	int numRows = 30;
	List chrs = null;
	String rt;
	String resub;
	String rtunit;

	private int row;
	private String forwardURI = null;

	public void execute() throws Exception {

		System.out.println("in xls gen ");

		try {
			System.out.println("In spreadsheet 1");
			JxlsWorkbook scenario = createScenario();
			System.out.println("In spreadsheet 2");
			if (scenario.equals(null)) {
				// System.out.println("In spreadsheet exit");
				addError("Template failed to be created");
			}
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2
			System.out.println("In spreadsheet 3");
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3
			System.out.println("In spreadsheet 4");
		} catch (NullPointerException em) {
			addError("Template failed to be created - ");
		}

	}

	// setControllers("Return");

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

		String filename;
		int interviewNumber = 1;

		/* Get EFD Project Details */

		/* Get WealthGroup data */
		System.out.println("before n");
		Map n = (Map) getCollectionElementView().getCollectionValues().get(row);

		System.out.println("after n");

		String wgid = (String) n.get("wgid");

		WealthGroup wealthgroup = XPersistence.getManager().find(WealthGroup.class, wgid);
		// System.out.println("wgid after wg get= " + wgid);
		Community community = XPersistence.getManager().find(Community.class,
				wealthgroup.getCommunity().getCommunityid());

		/*
		 * Check if WGInterview exists and if so is still at Generated status, otherwise
		 * do not allow
		 */

		// Query querywgi = XPersistence.getManager().
		// createQuery("select wi from WealthGroupInterview wi join
		// wi.wealthgroup wg where wg.wgid = '" + wgid +"'");

		Query querywgi = XPersistence.getManager().createQuery(
				"select wi from WealthGroupInterview wi join wi.wealthgroup wg where wg.wgid = '" + wgid + "'");

		List<WealthGroupInterview> wgi = querywgi.getResultList();


		if (wgi.isEmpty()) { /* New WGInter */
			// addMessage("Generating WGI Record");
			System.out.println("Generating WGI 111");

			/* Now write the wealthgroup interview header data */

			WealthGroupInterview wginew = new WealthGroupInterview();

			if (community.getCinterviewsequence() == null) {

				wginew.setWgInterviewNumber(interviewNumber);

			} else {

				wginew.setWgInterviewNumber(community.getCinterviewsequence());

			}

			wginew.setWgIntervieweesCount(1);

			System.out.println("Generating WGI interviewers " + community.getInterviewers());
			if (StringUtils.isBlank(community.getInterviewers())) {

				wginew.setWgInterviewers("-");

			} else {

				wginew.setWgInterviewers(community.getInterviewers());
			}

			wginew.setStatus(Status.Generated);
			wginew.setWealthgroup(wealthgroup);
			System.out.println("Generating Template done sets ");

			XPersistence.getManager().persist(wginew);
			// XPersistence.commit();

			System.out.println("Generating Template done persist ");

		} else if (wgi.size() == 1 && wgi.get(0).getStatus().equals("Generated")) {
			System.out.println("ok to print again as status still generated in template gen " + wgi.get(0).getStatus());
		} else if (wgi.size() == 1 && wgi.get(0).getStatus().toString() != "Generated") /* template already generated */
		{

			addError(".... Template cannot be regenerated once it has been uploaded, parsed or validated");
			return null;
		}

		System.out.println("In careetscenario 22");

		/* Need to reset Welathgroup */

		wealthgroup = XPersistence.getManager().find(WealthGroup.class, wgid);
		System.out.println("wgid 22 after wg get= " + wgid);

		/* Get Community Data */

	
		Project project = XPersistence.getManager().find(Project.class, community.getProjectlz().getProjectid());
	
		Site site = XPersistence.getManager().find(Site.class, community.getSite().getLocationid());

		/******************
		 * Need to get charresource and types using getmanager and iterate
		 ********************/

		// List wgcharacteristicsresource =

		Query query = XPersistence.getManager()
				.createQuery("select idwgresource from WGCharacteristicsResource where wgid = '" + wgid + "'");
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

		filename = community.getProjectlz().getProjecttitle() + '_' + site.getLivelihoodZone().getLzname() + '_'
				+ wealthgroup.getWgnameeng();

		JxlsWorkbook scenarioWB = new JxlsWorkbook(filename);

		boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT).setWrap(true);
		borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setCellColor(WHITE).setTextColor(BLACK).setWrap(true);
		datestyle = scenarioWB.getDefaultDateStyle();
		numberStyle = scenarioWB.addStyle(INTEGER).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		f1Style = scenarioWB.addStyle("0.0");

	
		/* XLS Sheets */
		/*********************************************************************************************************************************************************/
		/* Interview details sheet																													 */
		/*********************************************************************************************************************************************************/
		
		
		JxlsSheet interview = scenarioWB.addSheet("Interview Details");
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
		JxlsSheet foodPurchases = scenarioWB.addSheet("Food Purchases");
		JxlsSheet nonFoodPurchases = scenarioWB.addSheet("Non-Food Purchases");

	

		
		
		/*********************************************************************************************************************************************************/
		/* Start print routines for resources 																													 */
		/*********************************************************************************************************************************************************/
		printInterview(interview, project, community, wealthgroup);
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
		printFoodPurchases(foodPurchases);
		printNonFoodPurchases(nonFoodPurchases);
		


		
		/*
		 * add some drop downs and validations using POI have to map jxls wb to hssf wb
		 * 
		 */

		HSSFWorkbook workbook = null;

		/* convert OX jxls to HSSF */

		workbook = (HSSFWorkbook) scenarioWB.createPOIWorkbook();

		/*
		 * see
		 * https://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss
		 * /examples/LinkedDropDownLists.java
		 */

		Sheet sheet = workbook.createSheet("Validations");
		buildValidationSheet(sheet);

		Sheet landSheet = workbook.getSheetAt(1);
		Sheet lsSheet = workbook.getSheetAt(2);
		Sheet otSheet = workbook.getSheetAt(3);
		Sheet fsSheet = workbook.getSheetAt(4);
		Sheet treeSheet = workbook.getSheetAt(5);
		Sheet cashSheet = workbook.getSheetAt(6);
		Sheet cropSheet = workbook.getSheetAt(7);
		Sheet lssSheet = workbook.getSheetAt(8);
		Sheet lspSheet = workbook.getSheetAt(9);
		Sheet empSheet = workbook.getSheetAt(10);
		Sheet transSheet = workbook.getSheetAt(11);
		Sheet wildfSheet = workbook.getSheetAt(12);
		Sheet foodPurchaseSheet = workbook.getSheetAt(13);
		Sheet nonFoodPurchaseSheet = workbook.getSheetAt(14);


		/* Asset Land Type */

		addLOV(sheet, landSheet, 3, numRows - 1, 1, 1, "Land");
		addLOV(sheet, landSheet, 3, numRows - 1, 2, 2, "Area");
		addNumberValidation(workbook, sheet, landSheet, 3, numRows, 3, 3);

		/* Assets - Livestock Type */

		addLOV(sheet, lsSheet, 3, numRows -1, 1, 1, "Livestock");
		addLOV(sheet, lsSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lsSheet, 3, numRows, 3, 3); // Number Owned
		addNumberValidation(workbook, sheet, lsSheet, 3, numRows, 4, 4); // Price per unit

		/* Assets - Other Tradeable Type */

		addLOV(sheet,otSheet, 3, numRows -1 , 1, 1, "OtherTradeableGoods");
		addLOV(sheet, otSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, otSheet, 3, numRows, 3, 3); // Number Owned
		addNumberValidation(workbook, sheet, otSheet, 3, numRows, 4, 4); // Price per unit

		/* Assets - Food Stocks */
		addLOV(sheet, fsSheet, 3, numRows - 1, 1, 1, "FoodStocks");
		addLOV(sheet, fsSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, fsSheet, 3, numRows, 3, 3);
		
		
		/* Assets - Trees */
		addLOV(sheet, treeSheet, 3, numRows - 1, 1, 1, "Trees");
		addLOV(sheet, treeSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, treeSheet, 3, numRows, 3, 3);
		addNumberValidation(workbook, sheet, treeSheet, 3, numRows, 4, 4);
		
		/* Assets - Cash */
		addLOV(sheet, cashSheet, 3, numRows - 1, 1, 1, "Currency");
		addNumberValidation(workbook, sheet, cashSheet, 3, numRows, 2, 2);
		
		/* Crops */
		addLOV(sheet, cropSheet, 3, numRows - 1, 1, 1, "Crops");
		addLOV(sheet, cropSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, cropSheet, 3, numRows, 3, 6);
		addNumberValidation(workbook, sheet, cropSheet, 3, numRows, 8, 8 );
		addNumberValidation(workbook, sheet, cropSheet, 3, numRows, 10, 10 );
		addNumberValidation(workbook, sheet, cropSheet, 3, numRows, 12, 12 );
		
		/* Livestock Sales  */
		
		addLOV(sheet, lssSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lssSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lssSheet, 3, numRows, 3, 6);
		addNumberValidation(workbook, sheet, lssSheet, 3, numRows, 8, 8 );
		addNumberValidation(workbook, sheet, lssSheet, 3, numRows, 10, 10 );
		addNumberValidation(workbook, sheet, lssSheet, 3, numRows, 12, 12 );
		
		/* Livestock Products  */
		
		addLOV(sheet, lspSheet, 3, numRows - 1, 1, 1, "Livestock");
		addLOV(sheet, lspSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, lspSheet, 3, numRows, 3, 6);
		addNumberValidation(workbook, sheet, lspSheet, 3, numRows, 8, 8 );
		addNumberValidation(workbook, sheet, lspSheet, 3, numRows, 10, 10 );
		addNumberValidation(workbook, sheet, lspSheet, 3, numRows, 12, 12 );
		
		/* Employment  */
		
		addLOV(sheet, empSheet, 3, numRows - 1, 1, 1, "Employment");
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 2, 2);
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 4, 5 );
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 10, 10 );
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 12, 12 );
		addNumberValidation(workbook, sheet, empSheet, 3, numRows, 14, 14 );
		
		/* Transfers  */
		
		addLOV(sheet, transSheet, 3, numRows - 1, 1, 1, "TransferStyle");
		addLOV(sheet, transSheet, 3, numRows - 1, 3, 3, "Transfers");
		addNumberValidation(workbook, sheet, transSheet, 3, numRows, 4, 6);
		addLOV(sheet, transSheet, 3, numRows -1, 8, 8, "Unit");
		addNumberValidation(workbook, sheet, transSheet, 3, numRows, 9, 13 );
		addNumberValidation(workbook, sheet, transSheet, 3, numRows, 15, 15 );
		addNumberValidation(workbook, sheet, transSheet, 3, numRows, 17, 17 );
		addNumberValidation(workbook, sheet, transSheet, 3, numRows, 19, 19 );
		
		
		/* Wild Foods  */
		
		addLOV(sheet, wildfSheet, 3, numRows - 1, 1, 1, "WildFoods");
		addLOV(sheet, wildfSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, wildfSheet, 3, numRows, 3, 7);
		addNumberValidation(workbook, sheet, wildfSheet, 3, numRows, 9, 9 );
		addNumberValidation(workbook, sheet, wildfSheet, 3, numRows, 11, 11 );
		addNumberValidation(workbook, sheet, wildfSheet, 3, numRows, 13, 13 );
		
		/* Food Purchases  */
		
		addLOV(sheet, foodPurchaseSheet, 3, numRows - 1, 1, 1, "FoodPurchase");
		addLOV(sheet, foodPurchaseSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, foodPurchaseSheet, 3, numRows, 3, 4);
	
		/* Food Purchases  */
		
		addLOV(sheet, nonFoodPurchaseSheet, 3, numRows - 1, 1, 1, "NonFoodPurchase");
		addLOV(sheet, nonFoodPurchaseSheet, 3, numRows -1, 2, 2, "Unit");
		addNumberValidation(workbook, sheet, nonFoodPurchaseSheet, 3, numRows, 3, 4);
		
		/* Return the spreadsheet */

		return scenarioWB;

		/* end XLS setup */

	}
	/**************************************************************************************************************************************************/
	/* Validations																																	  */
	/**************************************************************************************************************************************************/

	private void addNumberValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol)  {
		
		CellRangeAddressList addressList = null;

		addressList = new CellRangeAddressList(firstRow, numRows, firstCol, lastCol);
		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0", "100000");

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Enter a Number only");
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(true); // Allows for other values - combo style
		iSheet.addValidationData(validation);

		/*
		 * the above sets the validation but need to set the cell format to be Number
		 */
		CellRangeAddress region = CellRangeAddress.valueOf("D4:D30");
		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();

		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("3"));
		style.setBorderBottom(BORDER_THIN);
		style.setBorderTop(BORDER_THIN);
		style.setBorderRight(BORDER_THIN);
		style.setBorderLeft(BORDER_THIN);
		//System.out.println("formating region " + region.getNumberOfCells());
		//System.out.println("formating region 2 " + firstRow + " " + lastRow);

		for (int i = firstRow; i < lastRow - 1; i++) {

			//System.out.println("formating region 2 " + region.getFirstRow() + " " + region.getLastRow());
			Row row = iSheet.getRow(i);
			//System.out.println("format cell = " + i);
			Cell cell = row.getCell(firstCol);
			cell.setCellStyle(style);

		}

	}

	private void addLOV(Sheet vsheet, Sheet iSheet, int firstRow, int lastRow, int firstCol, int lastCol, String rType)
	/*
	 * vsheet validation sheet iSheet input sheet rstype type of validation
	 * 
	 */
	{
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
		int foodpRowNum = 14;
		int nonfpRowNum = 15;
		
		
		/* Land Types */
		List<ResourceSubType> rst = XPersistence.getManager().createQuery("from ResourceSubType").getResultList();

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
		Row foodpRow = dataSheet.createRow(foodpRowNum);
		Row nonfpRow = dataSheet.createRow(nonfpRowNum);

		int j = 0;
		int la = 0;
		int ls = 0;
		int ot =0;
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
			/* Other Tradeable  */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Other Tradeable Goods".toString())) {
				cell = otRow.createCell(ot);
				cell.setCellValue(rst.get(k).getResourcetypename());
				ot++;
				
			}
			/* Asset Food Stocks   */
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
			/* Food Purchase */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Food Purchase".toString())) {
				cell = foodpRow.createCell(foodp);
				cell.setCellValue(rst.get(k).getResourcetypename());
				foodp++;
				
			}
			/* Non Food Purchase */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Non Food Purchase".toString())) {
				cell = nonfpRow.createCell(nonfp);
				cell.setCellValue(rst.get(k).getResourcetypename());
				nonfp++;
				
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
		String foodpcol = getCharForNumber(foodp);
		String nonfpcol = getCharForNumber(nonfp);
		

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
		

		/* Asset Food Stocks  */
		name = dataSheet.getWorkbook().createName();
		fsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + fsRowNum + ":$" + fscol + "$" + fsRowNum); 
																									
		name.setNameName("FoodStocks");
		
		/* Asset Trees  */
		name = dataSheet.getWorkbook().createName();
		treeRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + treeRowNum + ":$" + treecol + "$" + treeRowNum); 
																									
		name.setNameName("Trees");
		
		/* Crops  */
		name = dataSheet.getWorkbook().createName();
		cropRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + cropRowNum + ":$" + cropcol + "$" + cropRowNum); 
																									
		name.setNameName("Crops");
		
		/* Emp  */
		name = dataSheet.getWorkbook().createName();
		empRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + empRowNum + ":$" + empcol + "$" + empRowNum); 
																									
		name.setNameName("Employment");
		
		/* Transfers  */
		name = dataSheet.getWorkbook().createName();
		transRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + transRowNum + ":$" + transcol + "$" + transRowNum); 
																									
		name.setNameName("Transfers");
		
		/* Wild Foods  */
		name = dataSheet.getWorkbook().createName();
		wildfRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + wildfRowNum + ":$" + wildfcol + "$" + wildfRowNum); 
																									
		name.setNameName("WildFoods");
		
		/* Food Purchase  */
		name = dataSheet.getWorkbook().createName();
		foodpRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + foodpRowNum + ":$" + foodpcol + "$" + foodpRowNum); 
																									
		name.setNameName("FoodPurchase");
		
		
		/* Non Food Purchase  */
		name = dataSheet.getWorkbook().createName();
		nonfpRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + nonfpRowNum + ":$" + nonfpcol + "$" + nonfpRowNum); 
																									
		name.setNameName("NonFoodPurchase");
		
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
			System.out.println("Unit cell set to " + unit + " " + cell.getStringCellValue());
		}

		String unitcol = getCharForNumber(unit); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		unitRowNum++;
		
		System.out.println("Unit cell info  "+unitRowNum+" "+unitcol); 
		
		name.setRefersToFormula("Validations" + "!$A$" + unitRowNum + ":$" + unitcol + "$" + unitRowNum);
		name.setNameName("Unit");
		
		
		/* Transfer Style */

		
		List<ReferenceCode> referenceTStyle = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Transfer Style')").getResultList();
		// Get ref codes for Transger Style Official / Unofficial and add to Validations sheet
		for (int k = 0; k < referenceTStyle.size(); k++) {
			cell = transStyleRow.createCell(transStyle);
			cell.setCellValue(referenceTStyle.get(k).getReferenceName());
			transStyle++;
		}

		String transStylecol = getCharForNumber(transStyle); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		transStyleRowNum++;
		
		name.setRefersToFormula("Validations" + "!$A$" + transStyleRowNum + ":$" + transStylecol + "$" + transStyleRowNum);
		name.setNameName("TransferStyle");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/* Cash from Currency Table */ 
		
		List<Country> country = XPersistence.getManager()
				.createQuery("from Country").getResultList();      /* Currency unique - probably not */
		// Get codes for Currency and add to Validations sheet
		for (int k = 0; k < country.size(); k++) {
			cell = currencyRow.createCell(currency);
			cell.setCellValue(country.get(k).getCurrency());
			currency++;
			System.out.println("Currency cell set to " + currency + " " + cell.getStringCellValue());
		}

		String currencycol = getCharForNumber(currency); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		currencyRowNum++;
		
		name.setRefersToFormula("Validations" + "!$A$" + currencyRowNum + ":$" + currencycol + "$" + currencyRowNum);
		name.setNameName("Currency");
		
		
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
	/* Print Each Sheet 																															  */
	/**************************************************************************************************************************************************/
	private void printInterview(JxlsSheet sheet, Project project, Community community, WealthGroup wealthgroup) {
	
	int j = 2;
		
	sheet.setColumnWidths(2, width, width, width, width, width, numwidth); /* set col widths */

	
	while (j < 11) {
		sheet.setValue(3, j, "", borderStyle); /* set borders for data input fields */
		sheet.setValue(5, j, "", borderStyle);
		j += 2;
	}
	sheet.setValue(7, 8, "", borderStyle);
	sheet.setValue(7, 10, "", borderStyle);

	sheet.setValue(1, 1, "Project Date: " + project.getPdate());
	sheet.setValue(2, 2, "Interview Number", boldRStyle);
	sheet.setValue(2, 4, "District", boldRStyle);
	sheet.setValue(2, 6, "Livelihood Zone", boldRStyle);
	sheet.setValue(2, 8, "Number of Particpants", boldRStyle);
	sheet.setValue(2, 10, "Wealth Group", boldRStyle);

	sheet.setValue(4, 2, "Date", boldRStyle);
	sheet.setValue(4, 4, "Village / Sub District", boldRStyle);
	sheet.setValue(4, 6, "Interviewers", boldRStyle);
	sheet.setValue(4, 8, "Men", boldRStyle);
	sheet.setValue(4, 10, "Number of People in Household", boldRStyle);

	sheet.setValue(6, 8, "Women", boldRStyle);
	sheet.setValue(6, 10, "Type of Year", boldRStyle);

	/* Data */

	sheet.setValue(3, 4, community.getSite().getLocationdistrict(), borderStyle); /* District */
	sheet.setValue(3, 6, community.getSite().getLivelihoodZone().getLzname(),
			borderStyle); /* Livelihood Zone */

	sheet.setValue(3, 10, wealthgroup.getWgnameeng(), borderStyle); /* Wealth Group */

	sheet.setValue(5, 4, community.getSite().getSubdistrict(), borderStyle); /* Sub District */
	
	
	
	
	
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
		sheet.setColumnWidths(2, width, numwidth, numwidth); /* set col widths */
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
			/* Get Resource Type */
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,
					wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());

			rt = rst.getResourcetypename();
			resub = wgcharacteristicsresource.getResourcesubtype().getResourcetypename();
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
				// sheet.setValue(col, row, "", numberStyle); /* set number of Units to Number
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
			rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Food Stocks")) {
				sheet.setValue(2, fsrow, resub, borderStyle);
				sheet.setValue(3, fsrow, rtunit, borderStyle);
				fsrow++;
			}

		}

	}
	/**************************************************************************************************************************************************/
	private void printAssetTrees(JxlsSheet sheet)
	{
		/* Asset Trees Sheet - Trees sold for timber  */

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
			rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

			if (rt.equals("Trees")) {
				sheet.setValue(2, row, resub, borderStyle);
				sheet.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

	}

		
	
	/**************************************************************************************************************************************************/
	private void printAssetCash(JxlsSheet sheet)
	{
		{
			/* Cash Sheet -   */

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

			System.out.println("tree set styles tree size");

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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Cash")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}
	/**************************************************************************************************************************************************/
	private void printCrops(JxlsSheet sheet)
	{
		{
			/* Crops Sheet -   */

		
			sheet.setValue(2, 3, "Crop Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(8, 3, "Market 1", boldTopStyle);
			sheet.setValue(9, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(10, 3, "Market 2", boldTopStyle);
			sheet.setValue(11, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(12, 3, "Market 3", boldTopStyle);
			sheet.setValue(13, 3, "% Trade at 3", boldTopStyle);

			sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth, numwidth, width, width, numwidth, width, numwidth,width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 14) {
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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Crops")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}
	/**************************************************************************************************************************************************/
	private void printLivestockSales(JxlsSheet sheet)
	{
		{
			/* Livestock Sales  Sheet -   */

		
			sheet.setValue(2, 3, "Livestock Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(8, 3, "Market 1", boldTopStyle);
			sheet.setValue(9, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(10, 3, "Market 2", boldTopStyle);
			sheet.setValue(11, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(12, 3, "Market 3", boldTopStyle);
			sheet.setValue(13, 3, "% Trade at 3", boldTopStyle);
			

			sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth, numwidth, width, width, numwidth, width, numwidth,width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 14) {
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
	private void printLivestockProducts(JxlsSheet sheet)
	{
		{
			/* Livestock Products  Sheet -   */

		
			sheet.setValue(2, 3, "Livestock Type", boldTopStyle);
			sheet.setValue(3, 3, "Unit", boldTopStyle);
			sheet.setValue(4, 3, "Units Produced", boldTopStyle);
			sheet.setValue(5, 3, "Units Sold", boldTopStyle);
			sheet.setValue(6, 3, "Price per Unit", boldTopStyle);
			sheet.setValue(7, 3, "Units Other Use", boldTopStyle);
			sheet.setValue(8, 3, "Market 1", boldTopStyle);
			sheet.setValue(9, 3, "% Trade at 1", boldTopStyle);
			sheet.setValue(10, 3, "Market 2", boldTopStyle);
			sheet.setValue(11, 3, "% Trade at 2", boldTopStyle);
			sheet.setValue(12, 3, "Market 3", boldTopStyle);
			sheet.setValue(13, 3, "% Trade at 3", boldTopStyle);
			

			sheet.setColumnWidths(2, width, numwidth, numwidth, numwidth, numwidth, width, width,  numwidth, width, numwidth,width, numwidth); /* set col widths */

			/* set grid for data input */

			int col = 2;
			int row = 4;
			while (col < 14) {
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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Livestock Products")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}
	/**************************************************************************************************************************************************/
	private void printEmp(JxlsSheet sheet)
	{
		{
			/* Employment Sheet -   */

			sheet.setValue(2, 3, "Employment Type", boldTopStyle);
			sheet.setValue(3, 3, "Number of People Working", boldTopStyle);
			sheet.setValue(4, 3, "Unit of Work  e.g. Day, Month", boldTopStyle);
			sheet.setValue(5, 3, "Units worked  e.g. Days, Months", boldTopStyle);
			sheet.setValue(6, 3, "Cash Pay:Amount", boldTopStyle);
			sheet.setValue(7, 3, "Food Pay:Food Type", boldTopStyle);
			sheet.setValue(8, 3, "Food Pay:Unit", boldTopStyle);
			sheet.setValue(9, 3, "Food Pay:Units Paid / Work Unit", boldTopStyle);
			sheet.setValue(10, 3, "Work Location 1", boldTopStyle);
			sheet.setValue(11, 3, "% Work at 1", boldTopStyle);
			sheet.setValue(12, 3, "Work Location 2", boldTopStyle);
			sheet.setValue(13, 3, "% Work at 2", boldTopStyle);
			sheet.setValue(14, 3, "Work Location 3", boldTopStyle);
			sheet.setValue(15, 3, "% Trade at 3", boldTopStyle);

			

			sheet.setColumnWidths(2, width, numwidth,numwidth,numwidth,numwidth,width,numwidth,numwidth,width,numwidth,width,numwidth,width,numwidth); /* set col widths */

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

			System.out.println("tree set styles tree size");

			row = 4;
			int emprow = 5;
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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Employment")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}
	/**************************************************************************************************************************************************/

	private void printTransfers(JxlsSheet sheet)
	{
		{
			/* Transfers Sheet -   */
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

			sheet.setColumnWidths(2, width, width, width,numwidth,numwidth,numwidth,width,numwidth,numwidth,numwidth,
					numwidth,numwidth,numwidth,width,numwidth,width,numwidth,width,numwidth);

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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Transfers")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}

	/**************************************************************************************************************************************************/
	private void printWildFood(JxlsSheet sheet)
	{
		{
			/* Wildfood Sheet -   */

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

			sheet.setColumnWidths(2, width,width,numwidth,numwidth,numwidth,numwidth,numwidth,width,numwidth, width,numwidth,width,numwidth);
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
				rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

				if (rt.equals("Wildfood")) {
					sheet.setValue(2, row, resub, borderStyle);
					sheet.setValue(3, row, rtunit, borderStyle);
					row++;
				}

			}

		}
		
	}
	/**************************************************************************************************************************************************/

	private void printFoodPurchases(JxlsSheet sheet)
	{
	
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
		rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

		if (rt.equals("Food Purchase")) {
			sheet.setValue(2, row, resub, borderStyle);
			sheet.setValue(3, row, rtunit, borderStyle);
			row++;
		}

	}

}
	/**************************************************************************************************************************************************/

	private void printNonFoodPurchases(JxlsSheet sheet)
	{
		
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
		rtunit = wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit();

		if (rt.equals("Non Food Purchase")) {
			sheet.setValue(2, row, resub, borderStyle);
			sheet.setValue(3, row, rtunit, borderStyle);
			row++;
		}

	}

}
	
	
	/**************************************************************************************************************************************************/

	public static String getCharForNumber(int i) {
		return i > 0 && i < 27 ? String.valueOf((char) (i + 'A' - 1)) : null;
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