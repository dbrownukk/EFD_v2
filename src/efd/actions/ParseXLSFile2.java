package efd.actions;

import java.io.*;
import java.sql.*;
import java.text.*;

import javax.persistence.*;

/* Read XLS Community Interview  spreadsheet */
import java.util.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.editors.*;
import efd.model.*;
import efd.model.WealthGroupInterview.*;

import org.apache.commons.lang3.text.*;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.hssf.record.formula.functions.*;
//import org.apache.poi.ss.usermodel.*;

public class ParseXLSFile2 extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {

	Boolean messaging = false;
	/* define tab numbers in spreadsheet */
	public static final int NUMBERSHEETS = 14;
	public static final int ASSETLAND = 1;
	public static final int ASSETLIVESTOCK = 2;
	public static final int ASSETTRADEABLE = 3;
	public static final int ASSETFOOD = 4;
	public static final int ASSETTREE = 5;
	public static final int ASSETCASH = 6;
	public static final int CROPS = 7;
	public static final int LIVESTOCKSALE = 8;
	public static final int LIVESTOCKPRODUCT = 9;
	public static final int EMPLOYMENT = 10;
	public static final int TRANSFER = 11;
	public static final int WILDFOOD = 12;
	public static final int FOODPURCHASE = 13;
	public static final int NONFOODPURCHASE = 14;

	/* Define the spreadsheet structure */
	public class Wsheet {
		int wsheet;
		String resourceType; // Type of resource i.e. Land, WildFoods
		int numcols;

		public Wsheet(int asheet, String rtype, int cols) {
			this.wsheet = asheet;
			this.resourceType = rtype;
			this.numcols = cols;
		}
	}

	ArrayList<Wsheet> ws = new ArrayList<>();

	/* Cells to hold all spreadsheet values */
	/* Sheet / Row / Col */

	Cell cell[][][] = new Cell[15][60][60];

	/* Class variable definition */

	WealthGroupInterview wgi;
	ResourceSubType rst = null;
	AssetLiveStock als = null;
	AssetLand al = null;
	AssetTradeable atrade = null;
	AssetFoodStock afood = null;
	AssetTree atree = null;
	AssetCash acash = null;
	LivestockSales alss = null;
	LivestockProducts alsp = null;
	Employment aemp = null;
	Transfer at = null;
	WildFood awf = null;
	FoodPurchase afp = null;
	NonFoodPurchase anfp = null;

	Crop acrop = null;

	public void execute() throws Exception {

		int row = 0;
		String forwardURI = null;
		Blob spreadsheet = null;
		AttachedFile spreadsfile = null;
		Integer rowNumber = 0;
		Integer cellNumber = 0;
		Integer sheetNumber = 0;
		Sheet sheet;
		String[] cellArray;
		int arrayCount = 0;
		// Cell cell;
		Workbook wb;
		String sdate;
		String interviewers;
		String typeOfYear;
		String stest;
		Boolean nullable = false;
		Query query = null;

		/* Definition of spreadsheet to parse */
		ws.add(new Wsheet(ASSETLAND, "Land", 3));
		ws.add(new Wsheet(ASSETLIVESTOCK, "Livestock", 4));
		ws.add(new Wsheet(ASSETTRADEABLE, "Other Tradeable Goods", 4));
		ws.add(new Wsheet(ASSETFOOD, "Food Stocks", 3));
		ws.add(new Wsheet(ASSETTREE, "Trees", 4));
		ws.add(new Wsheet(ASSETCASH, "Cash", 2));
		ws.add(new Wsheet(CROPS, "Crops", 13));
		ws.add(new Wsheet(LIVESTOCKSALE, "Livestock Sales", 11));
		ws.add(new Wsheet(LIVESTOCKPRODUCT, "Livestock Products", 14));
		ws.add(new Wsheet(EMPLOYMENT, "Employment", 14));
		ws.add(new Wsheet(TRANSFER, "Transfers", 19));
		ws.add(new Wsheet(WILDFOOD, "Wild Foods", 13));
		ws.add(new Wsheet(FOODPURCHASE, "Food Purchase", 4));
		ws.add(new Wsheet(NONFOODPURCHASE, "Non Food Purchase", 4));

		/*
		 * ws.add(new Wsheet(2, 3, 1, "Livestock Type")); ws.add(new Wsheet(2, 3, 2,
		 * "Unit")); ws.add(new Wsheet(2, 3, 3, "Owned at Start of Year")); ws.add(new
		 * Wsheet(2, 3, 3, "Price Per Unit"));
		 */

		// System.out.println("in xls parse ");

		String wgiid = getView().getValueString("wgiid");

		wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
		if (wgi.getSpreadsheet().isEmpty()) {
			addWarning("Upload completed Interview Spreadsheet before parsing");
			return;
		} else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Parsed)) {
			addWarning("Cannot Parse Interview Spreadsheet - Already Parsed");
			return;
		} else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Validated)) {
			addWarning("Cannot Parse Interview Spreadsheet - Already Validated");
			return;
		}

		/* Otherwise. delete assets from this wealthgroupinterview */

		// System.out.println("in xls parse " + getView().getValueString("wgiid"));

		// System.out.println("in xls 2 " + wgi.getSpreadsheet());

		String spreadsheetId = wgi.getSpreadsheet();

		Connection con = null;
		try {
			con = DataSourceConnectionProvider.getByComponent("WealthGroupInterview").getConnection();

			PreparedStatement ps = con
					.prepareStatement("select id,data from OXFILES where ID = '" + spreadsheetId + "'");
			// System.out.println("prepped statment = " + ps.toString());
			// System.out.println("prepped");
			ResultSet rs = ps.executeQuery();
			// System.out.println("queried");

			// System.out.println("In Blob col = " + rs.findColumn("ID"));

			rs.next();
			InputStream input = rs.getBinaryStream("DATA"); /* get stream data as well */
			// System.out.println("done inputstream ");

			/* Move to first row */

			String spreadsheetPkey = rs.getString(1);

			spreadsfile = find(spreadsheetPkey);
			// System.out.println("done attache file get");
			ps.close();

			wb = WorkbookFactory.create(input);

		} catch (Exception ex) {
			addError("Incomplete Spreadsheet data, correct spreadsheet and upload again", ex);
			// You can throw any runtime exception here
			throw new SystemException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
			}
		}

		/* delete previous assets for this wgi */
		/* delete previous assets */

		wgi.getAssetLand().removeAll(wgi.getAssetLand());
		wgi.getAssetLiveStock().removeAll(wgi.getAssetLiveStock());
		wgi.getAssetTradeable().removeAll(wgi.getAssetTradeable());
		wgi.getAssetFoodStock().removeAll(wgi.getAssetFoodStock());
		wgi.getAssetTree().removeAll(wgi.getAssetTree());
		wgi.getAssetCash().removeAll(wgi.getAssetCash());
		wgi.getCrop().removeAll(wgi.getCrop());
		wgi.getLivestockSales().removeAll(wgi.getLivestockSales());
		wgi.getLivestockProducts().removeAll(wgi.getLivestockProducts());
		wgi.getEmployment().removeAll(wgi.getEmployment());
		wgi.getTransfer().removeAll(wgi.getTransfer());
		wgi.getWildFood().removeAll(wgi.getWildFood());
		wgi.getFoodPurchase().removeAll(wgi.getFoodPurchase());
		wgi.getNonFoodPurchase().removeAll(wgi.getNonFoodPurchase());

		/* Get the WS details */
		getInterviewDetails(wb, wgi);

		getWorkSheetDetail(wb, wgi); // Get cells from ss

		setResource(); // populate resource intersection tables

		getView().refresh();
		getView().refreshCollections();

		addMessage("Spreadsheet Parsed");

	}

	@Override
	public String getForwardURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(AttachedFile file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLibrary(String libraryId) {
		// TODO Auto-generated method stub

	}

	@Override
	public AttachedFile find(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<AttachedFile> findLibrary(String libraryId) {
		// TODO Auto-generated method stub
		return null;
	}

	private void localMessage(String message) {
		if (messaging == true) {
			System.out.println(message);
			addMessage(message);
		}
	}

	/*
	 * get Interview details and add to Wealth Group Interview table for current
	 * wealthgroup wgi)
	 */

	private void getInterviewDetails(Workbook wb, WealthGroupInterview wgi) {

		Cell icell = null;
		Integer cellNumber = 2;
		Integer rowNumber = 1;
		String sdate = null;
		String interviewers = null;
		String typeOfYear = null;

		// System.out.println("to loop ");

		Sheet sheet = wb.getSheetAt(0);
		int n = 0;

		System.out.println("get interview 111");

		/* Interview Number */
		Boolean nullable = false;
		if (checkCell("Interview Number", sheet, 2, 1, nullable))
			icell = sheet.getRow(1).getCell(2, Row.CREATE_NULL_AS_BLANK);
		else
			return;

		System.out.println("cell type " + icell.getCellType());

		if (icell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric ");
			Double interviewNumberD = icell.getNumericCellValue();
			Integer interviewNumber = interviewNumberD.intValue();
			wgi.setWgInterviewNumber(interviewNumber);

		} else {
			String interviewNumber = icell.getStringCellValue();
			System.out.println("get interview 1 " + interviewNumber);
			wgi.setWgInterviewNumber(Integer.parseInt(interviewNumber));
		}

		System.out.println("get interview 333");

		/* Number of Participants */
		nullable = false;
		if (checkCell("Number of Participants", sheet, 2, 7, nullable))
			icell = sheet.getRow(7).getCell(2, Row.CREATE_NULL_AS_BLANK);
		else
			return;

		if (icell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric ");
			Double intervieweesD = icell.getNumericCellValue();
			Integer interviewees = intervieweesD.intValue();
			wgi.setWgIntervieweesCount(interviewees);
		} else {
			System.out.println("in String ");
			String interviewees = icell.getStringCellValue();
			System.out.println("done cell get ");
			wgi.setWgIntervieweesCount(Integer.parseInt(interviewees));
			System.out.println("wgi set throw in participants ");

		}

		// System.out.println("get interview 444");

		/* Date */
		nullable = true;
		if (checkCell("Interview Date", sheet, 4, 1, nullable))
			icell = sheet.getRow(1).getCell(4, Row.CREATE_NULL_AS_BLANK);
		else {
			addError("Incomplete Spreadsheet data - Interview Date error ");
			return;
		}

		if (icell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric Date");
			Double iDateD = icell.getNumericCellValue();
			// Date iDate = (Date) DateUtil.getJavaDate(iDateD);

			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

			System.out.println("in Numeric Date " + iDateD);
			wgi.setWgInterviewDate(new java.sql.Date(iDateD.longValue()));

		} else {
			sdate = icell.getStringCellValue();
			System.out.println("get interview date 5555");
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println("get interview date 5551");

			try {
				wgi.setWgInterviewDate(formatter1.parse(sdate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("get interview date 5552");

		}

		/* Interviewers */
		nullable = false;
		if (checkCell("Interviewers", sheet, 4, 5, nullable))
			interviewers = sheet.getRow(5).getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
		else
			return;
		wgi.setWgInterviewers(interviewers);
		// System.out.println("get interview " + n++);

		/* Men */
		nullable = true;
		if (checkCell("Number of Men", sheet, 4, 7, nullable)) {
			icell = sheet.getRow(7).getCell(4, Row.CREATE_NULL_AS_BLANK);

			if (icell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double menD = icell.getNumericCellValue();
				Integer men = menD.intValue();
				wgi.setWgMaleIVees(men);
			} else {
				String men = icell.getStringCellValue();
				wgi.setWgMaleIVees(Integer.parseInt(men));
			}
		}
		/* Women */
		nullable = true;
		if (checkCell("Number of Women", sheet, 6, 7, nullable)) {
			icell = sheet.getRow(7).getCell(6, Row.CREATE_NULL_AS_BLANK);

			// System.out.println("women cell type " + cell.getCellType());

			if (icell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double womenD = icell.getNumericCellValue();
				Integer women = womenD.intValue();
				wgi.setWgFemaleIVees(women);
			} else {
				String women = icell.getStringCellValue();
				wgi.setWgFemaleIVees(Integer.parseInt(women));
			}
		}
		/* Average Number in HH */
		nullable = true;
		if (checkCell("Average Number in Household", sheet, 4, 9, nullable)) {
			icell = sheet.getRow(9).getCell(4, Row.CREATE_NULL_AS_BLANK);

			if (icell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double numberOfPeopleInHHD = icell.getNumericCellValue();
				Integer numberOfPeopleInHH = numberOfPeopleInHHD.intValue();
				wgi.setWgAverageNumberInHH(numberOfPeopleInHH);
			} else {
				String numberOfPeopleInHH = icell.getStringCellValue();
				wgi.setWgAverageNumberInHH(Integer.parseInt(numberOfPeopleInHH));
			}
		}
		/* Type of Year */
		nullable = true;
		if (checkCell("Type of Year", sheet, 6, 9, nullable)) {
			typeOfYear = sheet.getRow(9).getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

			// System.out.println("Type Year ");

			wgi.setWgYearType(typeOfYear);

			wgi.setStatus(Status.Parsed);
		}
		System.out.println("done wb setup ");

	}

	/**************************************************************************************************************************************************************************************************/
	public void getWorkSheetDetail(Workbook wb, WealthGroupInterview wgi) {

		/* Load spreadsheet into multi dimensional Cell [sheet] [row] [col] */

		int i = 0, j = 0, k = 0;
		Sheet sheet = null;

		/* get the details for this sheet */

		for (k = ASSETLAND; k <= NUMBERSHEETS; k++) { // Sheets

			sheet = wb.getSheetAt(k);
			// System.out.println("in get details sheet = " + k);
			for (i = 0; i < 40; i++) { // ROWS
				for (j = 0; j < ws.get(k - 1).numcols; j++) {

					cell[k][i][j] = sheet.getRow(i + 3).getCell(j + 1);
					// System.out.println("cell after get = " + cell[k][i][j].getCellType());

					// if first column is blank then no more data in this sheet
					if (cell[k][i][0].getCellType() == 3) {
						// System.out.println("No more data in this sheet " + k);
						i = 100;
						j = 100;
						break;
					}

					if (cell[k][i][j].getCellType() == 0)
						System.out.println("Numeric " + cell[k][i][j].getNumericCellValue());

					else if (cell[k][i][j].getCellType() == 1) {
						System.out.println("String " + cell[k][i][j].getStringCellValue());
						if (cell[k][i][j].getStringCellValue().isEmpty()) {
							System.out.println("EMPTY String ");
							i = 40;
							break;
						}
					}
				}
			}
		}
	}

	/**************************************************************************************************************************************************************************************************/
	private void setResource() {

		ResourceType rtype = null;

		int i = 0, j = 0, k = 0;

		/*
		 * print cell array for (i = 1; i < 15; i++) { for (j = 0; j < 10; j++) { for (k
		 * = 0; k < 10; k++) System.out.println("Cell at i j k = " + i + j + k + " " +
		 * cell[i][j][k]); } }
		 */

		for (i = ASSETLAND; i <= NONFOODPURCHASE; i++) { // Sheet
			// TODO ASSETCASH needs a default for EACH
			// and other assets 30/6/18
			breaksheet:

			for (j = 0; j < 35; j++) { // Row
				// System.out.println("sheet, row, cell = " + i + j + k);

				// System.out.println("after rowbreak row = " + j);
				for (k = 0; k < ws.get(i - 1).numcols; k++) {
					// System.out.println("cell == " + k);
					// System.out.println("celltype == " + cell[i][j][k].getCellType());
					if (cell[i][j][k].getCellType() == 3) {
						if (k == 0) { // first col is blank thus no more rows in this ws
							// System.out.println("i j k at break = " + i + j + k);
							// System.out.println("cell = blank");
							break breaksheet;
						}
					}
					if (cell[i][j][k].getCellType() == 0)
						System.out.println("Numeric " + cell[i][j][k].getNumericCellValue());
					if (cell[i][j][k].getCellType() == 1)
						System.out.println("cell = " + i + j + k + " " + cell[i][j][k].getStringCellValue());

				}

				/* In loop for sheets - swicth on which asset/resource */

				if (j == 0) {// only need to set once per sheet
					System.out.println("get rtype = " + ws.get(i - 1).resourceType);
					rtype = (ResourceType) XPersistence.getManager()
							.createQuery(
									"from ResourceType where ResourceTypeName = '" + ws.get(i - 1).resourceType + "'")
							.getSingleResult();
				}

				switch (i) {

				case ASSETLAND:
					try {
						al = new AssetLand();
						// System.out.println("in set assetLand cell = i =" + i);
						al.setLandTypeEnteredName(cell[i][j][0].getStringCellValue());
						al.setUnit(cell[i][j][1].getStringCellValue());
						al.setNumberOfUnits(cell[i][j][2].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done al get =  " + rst.getResourcetypename());

							al.setResourceSubType(rst);
							al.setStatus(efd.model.Asset.Status.Valid);

						} else {
							al.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetLand().add(al);
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Land Asset worksheet");
						break;
					}

				case ASSETLIVESTOCK:
					try {
						als = new AssetLiveStock();
						System.out.println("in set assetLivestock cell = i =" + i);
						als.setLiveStockTypeEnteredName(cell[i][j][0].getStringCellValue());
						als.setUnit(cell[i][j][1].getStringCellValue());
						als.setNumberOwnedAtStart(cell[i][j][2].getNumericCellValue());
						als.setPricePerUnit(cell[i][j][3].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done als get =  " + rst.getResourcetypename());

							als.setResourceSubType(rst);
							als.setStatus(efd.model.Asset.Status.Valid);

						} else {
							als.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetLiveStock().add(als);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock worksheet");
						break;
					}

				case ASSETTRADEABLE:
					try {
						atrade = new AssetTradeable();
						atrade.setTradeableTypeEnteredName(cell[i][j][0].getStringCellValue());
						atrade.setUnit(cell[i][j][1].getStringCellValue());
						atrade.setNumberOwned(cell[i][j][2].getNumericCellValue());
						atrade.setPricePerUnit(cell[i][j][3].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done atrade get =  " + rst.getResourcetypename());

							atrade.setResourceSubType(rst);
							atrade.setStatus(efd.model.Asset.Status.Valid);

						} else {
							atrade.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetTradeable().add(atrade);
						;

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Other Tradeable worksheet");
						break;
					}

				case ASSETFOOD:
					try {
						afood = new AssetFoodStock();
						System.out.println("in set assetfoodstock cell = i =" + i);
						afood.setFoodTypeEnteredName(cell[i][j][0].getStringCellValue());
						afood.setUnit(cell[i][j][1].getStringCellValue());
						afood.setQuantity(cell[i][j][2].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done afood get =  " + rst.getResourcetypename());

							afood.setResourceSubType(rst);
							afood.setStatus(efd.model.Asset.Status.Valid);

						} else {
							afood.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetFoodStock().add(afood);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Food Stock worksheet");
						break;
					}

				case ASSETTREE:
					try {
						atree = new AssetTree();
						System.out.println("in set assettree cell = i =" + i);
						atree.setTreeTypeEnteredName(cell[i][j][0].getStringCellValue());
						atree.setUnit(cell[i][j][1].getStringCellValue());
						atree.setNumberOwned(cell[i][j][2].getNumericCellValue());
						atree.setPricePerUnit(cell[i][j][3].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done atree get =  " + rst.getResourcetypename());

							atree.setResourceSubType(rst);
							atree.setStatus(efd.model.Asset.Status.Valid);

						} else {
							atree.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetTree().add(atree);
						// j = 100;
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Tree worksheet");
						break;
					}

				case ASSETCASH:
					try {
						acash = new AssetCash();
						System.out.println("in set assetcash cell = i =" + i);
						acash.setCurrencyEnteredName(cell[i][j][0].getStringCellValue());
						acash.setUnit("each"); // a default value - not used elsewhere

						acash.setAmount(cell[i][j][1].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done acash get =  " + rst.getResourcetypename());

							acash.setResourceSubType(rst);
							acash.setStatus(efd.model.Asset.Status.Valid);

						} else {
							acash.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getAssetCash().add(acash);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Cash worksheet");
						break;
					}

				case CROPS:
					try {
						acrop = new Crop();

						acrop.setCropType((cell[i][j][0].getStringCellValue()));
						acrop.setUnit(cell[i][j][1].getStringCellValue());
						acrop.setUnitsProduced(cell[i][j][2].getNumericCellValue());
						acrop.setUnitsSold(cell[i][j][3].getNumericCellValue());
						acrop.setPricePerUnit(cell[i][j][4].getNumericCellValue());
						acrop.setUnitsConsumed(cell[i][j][5].getNumericCellValue());
						acrop.setUnitsOtherUse(cell[i][j][6].getNumericCellValue());
						acrop.setMarket1(cell[i][j][7].getStringCellValue());
						acrop.setPercentTradeMarket1(cell[i][j][8].getNumericCellValue());
						acrop.setMarket2(cell[i][j][9].getStringCellValue());
						acrop.setPercentTradeMarket2(cell[i][j][10].getNumericCellValue());

						acrop.setMarket3(cell[i][j][11].getStringCellValue());
						acrop.setPercentTradeMarket3(cell[i][j][12].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done acrop get =  " + rst.getResourcetypename());

							acrop.setResourceSubType(rst);
							acrop.setStatus(efd.model.Asset.Status.Valid);

						} else {
							acrop.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getCrop().add(acrop);
						// j = 100;
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Crops worksheet");
						break;
					}

				case LIVESTOCKSALE:

					try {
						alss = new LivestockSales();
						System.out.println("in ls sales cell = i =" + i);
						alss.setLivestockType((cell[i][j][0].getStringCellValue()));
						alss.setUnit(cell[i][j][1].getStringCellValue());

						alss.setUnitsAtStartofYear(cell[i][j][2].getNumericCellValue()); // is this units at start of
																							// year
																							// or units produced??

						alss.setUnitsSold(cell[i][j][3].getNumericCellValue());
						alss.setPricePerUnit(cell[i][j][4].getNumericCellValue());

						alss.setMarket1(cell[i][j][5].getStringCellValue());
						alss.setPercentTradeMarket1(cell[i][j][6].getNumericCellValue());

						alss.setMarket2(cell[i][j][7].getStringCellValue());
						alss.setPercentTradeMarket2(cell[i][j][8].getNumericCellValue());

						alss.setMarket3(cell[i][j][9].getStringCellValue());
						alss.setPercentTradeMarket3(cell[i][j][10].getNumericCellValue());

						System.out.println("rtype = " + rtype.getIdresourcetype().toString());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done alss get =  " + rst.getResourcetypename());

							alss.setResourceSubType(rst);
							alss.setStatus(efd.model.Asset.Status.Valid);

						} else {
							alss.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getLivestockSales().add(alss);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock Sales worksheet");
						break;
					}

				case LIVESTOCKPRODUCT:
					try {
						alsp = new LivestockProducts();
						System.out.println("in ls products cell = i =" + i);
						alsp.setLivestockType((cell[i][j][0].getStringCellValue()));
						alsp.setLivestockProduct(cell[i][j][1].getStringCellValue());
						alsp.setUnit(cell[i][j][2].getStringCellValue());
						alsp.setUnitsProduced(cell[i][j][3].getNumericCellValue()); // is this units at start of year
																					// or units produced??

						alsp.setUnitsSold(cell[i][j][4].getNumericCellValue());
						alsp.setPricePerUnit(cell[i][j][5].getNumericCellValue());
						alsp.setUnitsConsumed(cell[i][j][6].getNumericCellValue());
						alsp.setUnitsOtherUse(cell[i][j][7].getNumericCellValue());

						alsp.setMarket1(cell[i][j][8].getStringCellValue());
						alsp.setPercentTradeMarket1(cell[i][j][9].getNumericCellValue());
						alsp.setMarket2(cell[i][j][10].getStringCellValue());
						alsp.setPercentTradeMarket2(cell[i][j][11].getNumericCellValue());

						alsp.setMarket3(cell[i][j][12].getStringCellValue());
						alsp.setPercentTradeMarket3(cell[i][j][13].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done alsp get =  " + rst.getResourcetypename());

							alsp.setResourceSubType(rst);
							alsp.setStatus(efd.model.Asset.Status.Valid);

						} else {
							alsp.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getLivestockProducts().add(alsp);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock Products worksheet");
						break;
					}

				case EMPLOYMENT:
					try {
						aemp = new Employment();
						int l = 0;
						System.out.println("in ls products cell = i =" + i);
						aemp.setEmploymentName((cell[i][j][l++].getStringCellValue()));
						aemp.setPeopleCount(cell[i][j][l++].getNumericCellValue());
						aemp.setUnit((cell[i][j][l++].getStringCellValue()));
						aemp.setUnitsWorked(cell[i][j][l++].getNumericCellValue());
						aemp.setCashPaymentAmount(cell[i][j][l++].getNumericCellValue());
						aemp.setFoodPaymentFoodType((cell[i][j][l++].getStringCellValue()));
						aemp.setFoodPaymentUnit((cell[i][j][l++].getStringCellValue()));
						aemp.setFoodPaymentUnitsPaidWork((cell[i][j][l++].getStringCellValue()));

						aemp.setWorkLocation1(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation1(cell[i][j][l++].getNumericCellValue());
						aemp.setWorkLocation2(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation2(cell[i][j][l++].getNumericCellValue());
						aemp.setWorkLocation3(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation3(cell[i][j][l++].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done emp get =  " + rst.getResourcetypename());

							aemp.setResourceSubType(rst);
							aemp.setStatus(efd.model.Asset.Status.Valid);

						} else {
							aemp.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getEmployment().add(aemp);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Employee worksheet");
						break;
					}
				case TRANSFER:
					try {
						at = new Transfer();
						int l = 0;

						at.setIsOfficial(cell[i][j][l++].getBooleanCellValue());
						at.setSource(cell[i][j][l++].getStringCellValue());
						at.setTransferType(cell[i][j][l++].getStringCellValue());
						at.setPeopleReceiving(cell[i][j][l++].getNumericCellValue());
						at.setTimesReceived(cell[i][j][l++].getNumericCellValue());
						at.setCashTransferAmount(cell[i][j][l++].getNumericCellValue());
						at.setTransferFoodOtherType((cell[i][j][l++].getStringCellValue()));
						at.setUnit(cell[i][j][l++].getStringCellValue());
						at.setUnitsTransferred(cell[i][j][l++].getNumericCellValue());
						at.setUnitsSold(cell[i][j][l++].getNumericCellValue());
						at.setPricePerUnit(cell[i][j][l++].getNumericCellValue());
						at.setOtherUse(cell[i][j][l++].getNumericCellValue());
						at.setUnitsConsumed(cell[i][j][l++].getNumericCellValue());

						at.setMarket1(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket1(cell[i][j][l++].getNumericCellValue());
						at.setMarket2(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket2(cell[i][j][l++].getNumericCellValue());

						at.setMarket3(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket3(cell[i][j][l++].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done transfers get =  " + rst.getResourcetypename());

							at.setResourceSubType(rst);
							at.setStatus(efd.model.Asset.Status.Valid);

						} else {
							at.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getEmployment().add(aemp);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Transfers worksheet");
						break;
					}
				case WILDFOOD:
					try {
						awf = new WildFood();
						int l = 0;

						awf.setWildFoodName(cell[i][j][l++].getStringCellValue());
						awf.setUnit(cell[i][j][l++].getStringCellValue());

						awf.setUnitsProduced(cell[i][j][l++].getNumericCellValue());
						awf.setUnitsSold(cell[i][j][l++].getNumericCellValue());
						awf.setPricePerUnit(cell[i][j][l++].getNumericCellValue());
						awf.setUnitsConsumed(cell[i][j][l++].getNumericCellValue());
						awf.setOtherUse(cell[i][j][l++].getNumericCellValue());

						awf.setMarket1(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket1(cell[i][j][l++].getNumericCellValue());
						awf.setMarket2(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket2(cell[i][j][l++].getNumericCellValue());
						awf.setMarket3(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket3(cell[i][j][l++].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done wildfood get =  " + rst.getResourcetypename());

							awf.setResourceSubType(rst);
							awf.setStatus(efd.model.Asset.Status.Valid);

						} else {
							awf.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getWildFood().add(awf);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Wild Food worksheet ");
						break;
					}

				case FOODPURCHASE:
					try {
						afp = new FoodPurchase();
						int l = 0;

						afp.setFoodTypeTypeEnteredName(cell[i][j][l++].getStringCellValue());
						afp.setUnit(cell[i][j][l++].getStringCellValue());
						afp.setUnitsPurchased(cell[i][j][l++].getNumericCellValue());
						afp.setPricePerUnit(cell[i][j][l++].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done Food Purchase get =  " + rst.getResourcetypename());

							afp.setResourceSubType(rst);
							afp.setStatus(efd.model.Asset.Status.Valid);

						} else {
							afp.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getFoodPurchase().add(afp);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Food Purchase worksheet ");
						break;
					}

				case NONFOODPURCHASE:
					try {
						anfp = new NonFoodPurchase();
						int l = 0;

						anfp.setItemPurchased(cell[i][j][l++].getStringCellValue());
						anfp.setUnit(cell[i][j][l++].getStringCellValue());
						anfp.setUnitsPurchased(cell[i][j][l++].getNumericCellValue());
						anfp.setPricePerUnit(cell[i][j][l++].getNumericCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype.getIdresourcetype().toString())) != null) {
							System.out.println("done Non Food Purchase get =  " + rst.getResourcetypename());

							anfp.setResourceSubType(rst);
							anfp.setStatus(efd.model.Asset.Status.Valid);

						} else {
							anfp.setStatus(efd.model.Asset.Status.Invalid);
						}
						wgi.getNonFoodPurchase().add(anfp);

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Non Food Purchase worksheet ");
						break;
					}

				} // end switch

			}
		}
	}

	/**************************************************************************************************************************************************************************************************/

	private ResourceSubType checkSubType(String var1, String resourceType) {
		try {
			var1 = WordUtils.capitalize(var1);

			ResourceSubType rst = (ResourceSubType) XPersistence.getManager()
					.createQuery("from ResourceSubType where resourcetype = '" + resourceType + "'"
							+ "and resourcetypename = '" + var1 + "'")
					.getSingleResult();
			System.out.println("done asset sub type query " + rst.getResourcetypename() + var1);
			return rst;
		}

		catch (Exception ex) {
			return null; // no record found to match data entered

		}

	}

	private Boolean checkCell(String cname, Sheet sheet, int x, int y, Boolean nullable) /* Is spreadsheet Cell Valid */
	{
		Cell cell;
		String stest;
		Double dtest;

		try {
			System.out.println("check cell type = " + cname);
			cell = sheet.getRow(y).getCell(x, Row.CREATE_NULL_AS_BLANK);

			System.out.println("77 cell type in check cell = " + cell.getCellType());
			/*
			 * System.out.println("Cell Type = "+cell.getCellType());
			 * System.out.println("Cell Type Blank = "+cell.CELL_TYPE_BLANK);
			 * System.out.println("Cell Type Bool = "+cell.CELL_TYPE_BOOLEAN);
			 * System.out.println("Cell Type Formula = "+cell.CELL_TYPE_FORMULA);
			 * System.out.println("Cell Type Numeric = "+cell.CELL_TYPE_NUMERIC);
			 * System.out.println("Cell Type String = "+cell.CELL_TYPE_STRING);
			 */

			if (cell.getCellType() == 0) /* is a valid cell with String or Number and test the fetch */
			{
				System.out.println("in cell type 0 check for numeric ");
				try {
					System.out.println("in try ");
					dtest = cell.getNumericCellValue();
					System.out.println("done try ");
				} catch (Exception ex) {
					{
						if (!nullable) {
							addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
							// You can throw any runtime exception here
							System.out.println("caught in check cell");
						}
						return false;
					}
				}

				if (dtest.isNaN()) {
					if (!nullable)
						addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
					return false;
				}
			} else if (cell.getCellType() == 1) {
				System.out.println("in cell type 1 check for string null? " + nullable + cell.getStringCellValue());
				try {
					stest = cell.getStringCellValue();
					if (stest.isEmpty() && nullable) {
						System.out.println("in cell type 1 check for string return false");
						return false;
					}

				} catch (Exception ex) {
					if (nullable) /* empty is ok */
					{
						System.out.println("nullable string return false");
						return false;
					}
					if (!nullable) {
						addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
						// You can throw any runtime exception here
						System.out.println("caught in check cell");
					}
					return false;

				}
				if (stest.isEmpty() && !nullable) {

					addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
					return false;
				}
			}

			else if (cell.getCellType() == 3 && !nullable) { /* BLANK */

				addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
				return false;

			}

			System.out.println("return true");
			localMessage(cname + " is correct");
			return true;

		}

		catch (Exception ex) {
			addError("Incomplete Spreadsheet data '" + cname + "' Error - correct spreadsheet and upload again");
			// You can throw any runtime exception here
			System.out.println("caught in check cell");
			return false;
		}

	}

}
