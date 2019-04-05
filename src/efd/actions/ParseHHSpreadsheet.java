/* Parse Household Spreadsheet
 * DRB Mar 2019
 * 
 * Accessed from Household Module
 * 
 *  */
package efd.actions;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.time.*;

import javax.inject.*;
import javax.persistence.*;

/* Read XLS Community Interview  spreadsheet */
import java.util.*;
import java.util.Date;

import org.openxava.actions.*;
import org.openxava.component.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.editors.*;
import efd.model.*;
import efd.model.ConfigQuestion.*;
import efd.model.HouseholdMember.*;
import efd.model.WealthGroupInterview.*;

import org.apache.commons.lang3.text.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.*;
import org.apache.poi.util.*;
import org.directwebremoting.hibernate.*;

public class ParseHHSpreadsheet extends CollectionBaseAction
		implements IForwardAction, JxlsConstants, IFilePersistor, IJDBCAction {

	@Inject // Since v4m2
	private String defaultSchema;

	Boolean messaging = false;
	/* define tab numbers in spreadsheet */

	private IConnectionProvider provider;

	public static final int NUMBERSHEETS = 15; // Number of sheets after HH Members
	public static final int HOUSEHOLD = 1;
	public static final int HOUSEHOLDMEMBERS = 2;

	public static final int ASSETLAND = 3;
	public static final int ASSETLIVESTOCK = 4;
	public static final int ASSETTRADEABLE = 5;
	public static final int ASSETFOOD = 6;
	public static final int ASSETTREE = 7;
	public static final int ASSETCASH = 8;
	public static final int CROPS = 9;
	public static final int LIVESTOCKSALE = 10;
	public static final int LIVESTOCKPRODUCT = 11;
	public static final int EMPLOYMENT = 12;
	public static final int TRANSFER = 13;
	public static final int WILDFOOD = 14;

	public static final int INPUTS = 15;
	// public static final int FOODPURCHASE = 16;
	public static final int NONFOODPURCHASE = 16;

	/* Define the spreadsheet structure */
	public class Wsheet {
		int wsheet;
		String resourceType; // Type of resource i.e. Land, WildFoods
		int numcols;
		int numrows;

		public Wsheet(int asheet, String rtype, int cols, int rows) {
			this.wsheet = asheet;
			this.resourceType = rtype;
			this.numcols = cols;
			this.numrows = rows;
		}
	}

	ArrayList<Wsheet> ws = new ArrayList<>();

	/* Cells to hold all spreadsheet values */
	/* Sheet / Row / Col */

	Cell cell[][][] = new Cell[20][60][60];

	/* Class variable definition */

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
	Inputs inputs = null;
	ConfigAnswer qanswer = null;

	int numberRows[] = new int[16]; // Sheet / Rows

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
		String emessage = null;
		String interviewers;
		String typeOfYear;
		String stest;
		Boolean nullable = false;
		Query query = null;
		int numberRows[] = new int[NUMBERSHEETS];

		/* Definition of spreadsheet to parse */

		ws.add(new Wsheet(ASSETLAND, "Land", 3, 0)); // 0
		ws.add(new Wsheet(ASSETLIVESTOCK, "Livestock", 4, 0));
		ws.add(new Wsheet(ASSETTRADEABLE, "Other Tradeable Goods", 4, 0));
		ws.add(new Wsheet(ASSETFOOD, "Food Stocks", 3, 0)); // 3
		ws.add(new Wsheet(ASSETTREE, "Trees", 4, 0));
		ws.add(new Wsheet(ASSETCASH, "Cash", 2, 0));
		ws.add(new Wsheet(CROPS, "Crops", 13, 0));
		ws.add(new Wsheet(LIVESTOCKSALE, "Livestock", 11, 0));
		ws.add(new Wsheet(LIVESTOCKPRODUCT, "Livestock Products", 14, 0));
		ws.add(new Wsheet(EMPLOYMENT, "Employment", 14, 0));
		ws.add(new Wsheet(TRANSFER, "Transfers", 19, 0));
		ws.add(new Wsheet(WILDFOOD, "Wild Foods", 13, 0)); // 11
		ws.add(new Wsheet(INPUTS, "Inputs", 10, 0));

		ws.add(new Wsheet(NONFOODPURCHASE, "Non Food Purchase", 4, 0)); // used for INPUTS

		/*
		 * ws.add(new Wsheet(2, 3, 1, "Livestock Type")); ws.add(new Wsheet(2, 3, 2,
		 * "Unit")); ws.add(new Wsheet(2, 3, 3, "Owned at Start of Year")); ws.add(new
		 * Wsheet(2, 3, 3, "Price Per Unit"));
		 */

		System.out.println("in xls parse ");

		/*
		 * does household and spreadsheet exist
		 */

		String hhId = null;
		try {
			em("about to do get ss");
			String spreadsheetFile = getView().getValueString("spreadsheet");
			hhId = getView().getValueString("id");
			em("done get id and ss" + hhId);

			if (hhId.isEmpty()) {
				addError("Select or Create a Household before trying to parse a spreadsheet");
				return;
			}

			if (spreadsheetFile.isEmpty()) {
				addError("No Spreadsheet file available");
				return;
			}

		} catch (Exception ex) {
			addError(ex.toString());
		}

		// getView().refresh();
		em("about to get hhi" + hhId);
		Household hhi = XPersistence.getManager().find(Household.class, hhId);
		if (hhi.getSpreadsheet().isEmpty()) {
			addWarning("Upload completed Interview Spreadsheet before parsing");
			return;
		}

		if (hhi.getStatus() == null) {
			em("status is empty"); // do nothing and continue parse
		}

		else if ((hhi.getStatus().equals(efd.model.WealthGroupInterview.Status.PartParsed)
				|| hhi.getStatus().equals(efd.model.WealthGroupInterview.Status.FullyParsed))) {
			addWarning("Cannot Parse Interview Spreadsheet - Already Parsed");
			return;
		} else if (hhi.getStatus().equals(efd.model.WealthGroupInterview.Status.Validated)) {
			addWarning("Cannot Parse Interview Spreadsheet - Already Validated");
			return;
		}
		em("done get hhi");
		/* Otherwise. delete assets from this wealthgroupinterview */

		System.out.println("in xls parse 2 ");

		String spreadsheetId = hhi.getSpreadsheet();

		Connection con = null;

		// what schema?

		// System.out.println("XP schema = "+XPersistence.getDefaultSchema());
		String schema = XPersistence.getDefaultSchema();
		System.out.println("schema = " + schema);

		try {
			con = DataSourceConnectionProvider.getByComponent("Household").getConnection();

			// System.out.println("schema con = "+con.getSchema().toString());
			PreparedStatement ps;
			if (schema != null) {
				schema = schema + ".";
				ps = con.prepareStatement(
						"select id,data from " + schema + "OXFILES where ID = '" + spreadsheetId + "'");
			} else
				ps = con.prepareStatement("select id,data from OXFILES where ID = '" + spreadsheetId + "'");

			ResultSet rs = ps.executeQuery();

			rs.next();

			InputStream input = rs.getBinaryStream("DATA"); /* get stream data as well */
			System.out.println("done inputstream ");

			/* Move to first row */

			String spreadsheetPkey = rs.getString(1);

			spreadsfile = find(spreadsheetPkey);
			System.out.println("done attache file get");
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

		/* delete previous assets for this hh */
		/* delete previous assets */

		hhi.getAssetLand().removeAll(hhi.getAssetLand());
		hhi.getAssetLiveStock().removeAll(hhi.getAssetLiveStock());
		hhi.getAssetTradeable().removeAll(hhi.getAssetTradeable());
		hhi.getAssetFoodStock().removeAll(hhi.getAssetFoodStock());
		hhi.getAssetTree().removeAll(hhi.getAssetTree());
		hhi.getAssetCash().removeAll(hhi.getAssetCash());
		hhi.getCrop().removeAll(hhi.getCrop());
		hhi.getLivestockSales().removeAll(hhi.getLivestockSales());
		hhi.getLivestockProducts().removeAll(hhi.getLivestockProducts());
		hhi.getEmployment().removeAll(hhi.getEmployment());
		hhi.getTransfer().removeAll(hhi.getTransfer());
		hhi.getWildFood().removeAll(hhi.getWildFood());
		hhi.getInputs().removeAll(hhi.getInputs());
		// hhi.getHouseholdMember().removeAll(hhi.getHouseholdMember()); // Remove not
		// working for non embedded collections - use jdbc statement
		// hhi.getConfigAnswer().removeAll(hhi.getConfigAnswer());

		String hhid = hhi.getId();

		HouseholdMember hm = null;
		for (HouseholdMember householdMember : hhi.getHouseholdMember()) {
			hm = XPersistence.getManager().find(HouseholdMember.class, householdMember.getId());
			XPersistence.getManager().remove(hm);

		}

		getView().findObject();

		System.out.println("hhm tot now =" + hhi.getHouseholdMember().size());

		// hhi.getFoodPurchase().removeAll(hhi.getFoodPurchase());
		// hhi.getNonFoodPurchase().removeAll(hhi.getNonFoodPurchase());

		/* Get the WS details */
		getInterviewDetails(wb, hhi);

		getHHDetails(wb, hhi);

		if (getHHMDetails(wb, hhi) != 0)
			return;

		getWorkSheetDetail(wb, hhi); // Get cells from ss

		setResource(hhi); // populate resource intersection tables

		getView().findObject(); // refresh views and collection tab count

		hhi.setStatus(Status.FullyParsed);

		getView().refresh();
		getView().refreshCollections();
		getView().refreshDescriptionsLists();
		addMessage("Spreadsheet Parsed");

	}

	private void em(String em) {
		System.out.println(em);

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
			// System.out.println(message);
			addMessage(message);
		}
	}

	/**************************************************************************************************************************************************************************************************/

	/*
	 * get Interview details
	 */

	private void getInterviewDetails(Workbook wb, Household hhi) {

		Cell icell = null;

		em(" start getInterviewDetails ");

		Sheet sheet = wb.getSheetAt(0); // Interview Details is first sheet (0)

		/*
		 * Check if correct Project
		 */

		String studyName = hhi.getStudy().getStudyName() + " " + hhi.getStudy().getReferenceYear();
		em("hhi studyName = " + studyName);

		icell = sheet.getRow(3).getCell(2, Row.CREATE_NULL_AS_BLANK);

		System.out.println("first get = " + icell.getStringCellValue());

		if (!icell.getStringCellValue().equals(studyName)) // Not for this project
		{
			addWarning("Spreadsheet is not for current Study . Spreadsheet Study Name + Reference Year = "
					+ icell.getStringCellValue() + ", Study Project = " + studyName);
			return;
		}

		// icell = sheet.getRow(5).getCell(2,
		// Row.CREATE_NULL_AS_BLANK).getStringCellValue();

		String hhName = sheet.getRow(5).getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
		Date hhIDate = sheet.getRow(5).getCell(4, Row.CREATE_NULL_AS_BLANK).getDateCellValue();
		String hhInterviewers = sheet.getRow(5).getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

		if (hhName.isEmpty())
			hhi.setHouseholdName(null);
		else
			hhi.setHouseholdName(hhName);

		hhi.setInterviewDate(hhIDate);
		hhi.setInterviewers(hhInterviewers);

		em(" end getInterviewDetails ");
		// hhi.setStatus(Status.PartParsed);
	}

	/**************************************************************************************************************************************************************************************************/

	/*
	 * get Household details
	 */

	private void getHHDetails(Workbook wb, Household hhi) {
		em(" start getHHDetails ");
		Integer k = 0;
		Cell qcell = null;
		Cell acell = null;
		Double answerDouble = null;
		String answerString = null;

		Sheet sheet = wb.getSheetAt(HOUSEHOLD);
		int qsize = hhi.getStudy().getConfigQuestionUse().size(); // Number of questions in this study for HH

		if (qsize == 0)
			return; // no HH Questions

		/*
		 * Only want Household questions
		 */
		Iterator<ConfigQuestionUse> iterator = hhi.getStudy().getConfigQuestionUse().iterator();
		while (iterator.hasNext()) {

			ConfigQuestionUse nextq = iterator.next();
			if (nextq.getConfigQuestion().getLevel().toString() == "Household") {
				k++;
			}
		}
		qsize = k;

		em("No of HH questions = " + qsize);

		/*
		 * get spreadsheet HH Q and A
		 */

		ArrayList<QandA> qand = new ArrayList<QandA>();

		QandA qa = null;

		for (int i = 0; i < qsize; i++) {

			try {
				qcell = sheet.getRow(2 + i).getCell(1, Row.CREATE_NULL_AS_BLANK);

			} catch (Exception ex) {
				break;
			}

			if (qcell.getStringCellValue().isEmpty())
				break; // no more questions - but should be caught by number of questions

			// What celltype is answer?

			qa = new QandA();

			acell = sheet.getRow(2 + i).getCell(2, Row.CREATE_NULL_AS_BLANK);

			if (acell.getCellType() == Cell.CELL_TYPE_STRING) {

				qa.setAnswer(acell.getStringCellValue());

			} else if (acell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				answerDouble = acell.getNumericCellValue();

				answerString = Double.toString(answerDouble);

				qa.setAnswer(answerString.toString());

			}

			qa.setQuestion(qcell.getStringCellValue());
			System.out.println("in loop 3");
			if (qa.getAnswer().isEmpty()) {
				qa.setAnswer("-");
			}
			System.out.println("qanda = " + i + " " + qa.question + " " + qa.answer);
			qand.add(qa);

		}

		/*
		 * Number of HH Questions in this study
		 */

		iterator = hhi.getStudy().getConfigQuestionUse().iterator();
		while (iterator.hasNext()) {

			ConfigQuestionUse nextq = iterator.next();
			if (nextq.getConfigQuestion().getLevel().toString() == "Household") {
				em("prompt = " + nextq.getConfigQuestion().getPrompt().toString());
				// Find answer for this question
				for (k = 0; k < qand.size(); k++) {
					if (qand.get(k).getQuestion().equals(nextq.getConfigQuestion().getPrompt())) {

						qanswer = new ConfigAnswer();
						qanswer.setConfigQuestionUse(nextq);

						qanswer.setAnswer(qand.get(k).getAnswer());

						qanswer.setHousehold(hhi);

						qanswer.setStudy(nextq.getStudy());

						qanswer.setStudy(hhi.getStudy());

						XPersistence.getManager().persist(qanswer);
						// hhi.getConfigAnswer().add(qanswer);

					}

				}

			}
		}
		em("end getHHDetails");

	}

	/**************************************************************************************************************************************************************************************************/
	private int getHHMDetails(Workbook wb, Household hhi) { // return 0 if success
		em(" start getHHMDetails ");
		Integer k = 0;
		Cell qcell = null;
		Cell acell = null;
		Double answerDouble = null;
		String answerString = null;
		String gender = null;
		String head = null;
		String absent = null;
		String absentReason = null;

		HouseholdMember hhm = null;

		Sheet sheet = wb.getSheetAt(HOUSEHOLDMEMBERS);
		/*
		 * get standard HHM details Note that members go across spreadsheet
		 * 
		 */

		// get first Member

		for (int hhmcol = 2; hhmcol < 22; hhmcol++) {

			hhm = new HouseholdMember();

			hhm.setHousehold(hhi);

			try {
				hhm.setHouseholdMemberName(
						sheet.getRow(3).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
				if (hhm.getHouseholdMemberName() == "") {
					em("no more members");
					hhmcol = 23;
					break;
				}

				gender = sheet.getRow(4).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (gender.equals("Male")) {
					hhm.setGender(Sex.Male);
				} else if (gender.equals("Female")) {
					hhm.setGender(Sex.Female);
				} else {
					hhm.setGender(Sex.Unknown);
				}

				hhm.setAge((int) (sheet.getRow(5).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()));

				// calculated
				/*
				hhm.setYearOfBirth(
						(int) (sheet.getRow(6).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()));

				if (hhm.getYearOfBirth() == 0) {
					// calc year
				
					int thisYear=hhm.getHousehold().getStudy().getReferenceYear();
					
					
					hhm.setYearOfBirth(thisyear - hhm.getAge());
				}
				 */

				head = sheet.getRow(7).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (head.equals("Yes")) {
					hhm.setHeadofHousehold(YN.Yes);
				} else if (head.equals("No")) {
					hhm.setHeadofHousehold(YN.No);
				}

				head = sheet.getRow(7).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (head.equals("Yes")) {
					hhm.setHeadofHousehold(YN.Yes);
				} else if (head.equals("No")) {
					hhm.setHeadofHousehold(YN.No);
				}

				absent = sheet.getRow(8).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (absent.equals("Yes")) {
					System.out.println("absent Y = " + absent);
					hhm.setAbsent(YN.Yes);
				} else if (absent.equals("No")) {
					System.out.println("absent N = " + absent);
					hhm.setAbsent(YN.No);
				}

				absentReason = sheet.getRow(9).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (!absentReason.isEmpty()) {
					hhm.setReasonForAbsence(absentReason);
				}

				hhm.setMonthsAway(
						(int) (sheet.getRow(10).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()));
				if (hhm.getMonthsAway() == 0) {
					hhm.setAbsent(YN.No);
				} else
					hhm.setAbsent(YN.Yes);

				int hhmNumber = hhmcol - 1;
				hhm.setHouseholdMemberNumber("HHM" + hhmNumber);

				XPersistence.getManager().persist(hhm);

			} catch (Exception ex) {
				addError("Parse Failed - Wrong template for Household Members " + ex);
				return (23001);
			}

			/*
			 * Needs some refactoring to combine HH and HHM QandA
			 * 
			 */

			int qsize = hhi.getStudy().getConfigQuestionUse().size(); // Number of questions in this study for HH

			if (qsize == 0)
				return (0); // no HH Questions

			/*
			 * Only want HouseholdMember questions
			 */
			Iterator<ConfigQuestionUse> iterator = hhi.getStudy().getConfigQuestionUse().iterator();
			while (iterator.hasNext()) {

				ConfigQuestionUse nextq = iterator.next();
				if (nextq.getConfigQuestion().getLevel() == Level.HouseholdMember) {
					k++;
				}
			}
			qsize = k;

			em("No of HHM questions = " + qsize);

			/*
			 * get spreadsheet HH Q and A
			 */

			ArrayList<QandA> qand = new ArrayList<QandA>();

			QandA qa = null;

			for (int i = 0; i < qsize; i++) {

				try {
					qcell = sheet.getRow(9 + i).getCell(1, Row.CREATE_NULL_AS_BLANK);

				} catch (Exception ex) {
					break;
				}

				if (qcell.getStringCellValue().isEmpty())
					break; // no more questions - but should be caught by number of questions

				// What celltype is answer?

				qa = new QandA();

				acell = sheet.getRow(9 + i).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK);

				if (acell.getCellType() == Cell.CELL_TYPE_STRING) {

					qa.setAnswer(acell.getStringCellValue());

				} else if (acell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

					answerDouble = acell.getNumericCellValue();

					answerString = Double.toString(answerDouble);

					qa.setAnswer(answerString.toString());

				}

				qa.setQuestion(qcell.getStringCellValue());

				if (qa.getAnswer() == null) {
					em("getanswer is empty");
					qa.setAnswer("-");
				}

				qand.add(qa);

			}

			em("no of qanda size = " + qand.size());

			/*
			 * Number of HH Questions in this study
			 */

			iterator = hhi.getStudy().getConfigQuestionUse().iterator();
			while (iterator.hasNext()) {

				ConfigQuestionUse nextq = iterator.next();
				em("hhm level = " + nextq.getConfigQuestion().getLevel().toString());
				if (nextq.getConfigQuestion().getLevel() == Level.HouseholdMember)
					em("prompt = " + nextq.getConfigQuestion().getPrompt().toString());
				// Find answer for this question
				for (k = 0; k < qand.size(); k++) {
					if (qand.get(k).getQuestion().equals(nextq.getConfigQuestion().getPrompt())) {

						qanswer = new ConfigAnswer();
						qanswer.setConfigQuestionUse(nextq);

						qanswer.setAnswer(qand.get(k).getAnswer());

						qanswer.setHousehold(hhi);

						// qanswer.setStudy(nextq.getStudy());

						// qanswer.setStudy(hhi.getStudy());

						qanswer.setHouseholdMember(hhm);

						XPersistence.getManager().persist(qanswer);

					}

				}

			}
		}
		em("end getHHMDetails");
		return (0);

	}

	/**************************************************************************************************************************************************************************************************/

	public void getWorkSheetDetail(Workbook wb, Household hhi) {

		/* Load spreadsheet into multi dimensional Cell [sheet] [row] [col] */

		Cell icell;
		Row irow;
		int i = 0, j = 0, k = 1;
		Sheet sheet = null;
		// int numberRows[] = new int[NUMBERSHEETS+10];

		/* get the details for this sheet */

		for (k = ASSETLAND; k <= NUMBERSHEETS; k++) { // Sheets ---- was <= DRB

			try {

				sheet = wb.getSheetAt(k);

				for (i = 0; i < 40; i++) { // ROWS

					for (j = 0; j < ws.get(k - 3).numcols; j++) {

						// new check for null row
						irow = sheet.getRow(i + 3);

						if (irow == null) {
							System.out.println("No more data in this sheet " + k + " " + i);
							numberRows[k] = i;
							i = 100;
							j = 100;

							break;

						}

						cell[k][i][j] = sheet.getRow(i + 3).getCell((j + 1), Row.CREATE_NULL_AS_BLANK);

						/*
						 * Used for testing if (k == EMPLOYMENT) {
						 * 
						 * System.out.println("read cell type = " + cell[k][i][j].getCellType() +
						 * "ijk= " + k + " " + i + " " + j);
						 * 
						 * if (cell[k][i][j].getCellType() == 0)
						 * System.out.println("read cell number type = " +
						 * cell[k][i][j].getNumericCellValue());
						 * 
						 * if (cell[k][i][j].getCellType() == 1)
						 * System.out.println("read cell string type = " +
						 * cell[k][i][j].getStringCellValue()); }
						 * 
						 * 
						 */

						// if first column is blank then no more data in this sheet

						if (cell[k][i][0].getCellType() == 3) {
							// System.out.println("In read No more data in this sheet " + k + " " + i);
							numberRows[k] = i; // re included DRB 19/11/2018
							i = 100;
							j = 100;

							break;
						}

						// if first col check if empty, if so then end of rows on this sheet

						if (j == 0 && cell[k][i][0].getStringCellValue().isEmpty()) {

							// record numrows in each sheet
							numberRows[k] = i;
							System.out.println("in read numberrows = " + numberRows[k] + " " + k);
							i = 100;
							break;
						}
					}
				}
			} catch (Exception ex) {
				System.out.println("in catch for get cell i j k =  " + ex + i + j + k);
				addError("Error in number of columns in Sheet = " + k + " " + i + " " + j + " " + k);

			}
		}

		System.out.println("done worksheet detail ");

	}

	/**************************************************************************************************************************************************************************************************/
	private void setResource(Household hhi) {

		em("in getresource");
		ResourceType[] rtype = new ResourceType[50];

		/* Get list of currencies for validation of Cash Asset */
		List<Country> currency = XPersistence.getManager().createQuery("from Country").getResultList();

		int icurr = 0;
		int i = 0, j = 0, k = 0;
		String s1 = null, s2 = null;
		String warnMessage = null;
		String unitEntered = null;

		// for (int p = 0; p < 15; p++)
		// System.out.println("numrows array p = " + p + " " + numberRows[p]);

		/*
		 * Note that a cell matrix is used - populated in getWorkSheetDetail above
		 * 
		 * print cell array for (i = 1; i < 15; i++) { for (j = 0; j < 10; j++) { for (k
		 * = 0; k < 10; k++) System.out.println("Cell at i j k = " + i + j + k + " " +
		 * cell[i][j][k]); } }
		 */

		/* set validation rtypes array */

		List<ResourceType> rst3 = (List<ResourceType>) XPersistence.getManager().createQuery("from ResourceType")
				.getResultList();

		for (ResourceType type : rst3) {
			System.out.println("rst3 = " + type.getResourcetypename());
		}

		for (i = ASSETLAND; i <= NONFOODPURCHASE; i++) {
			// System.out.println("in rt setup loop " + ASSETLAND + " " + INPUTS + " " + i +
			// " "
			// + ws.get(i - 3).resourceType.toString());
			try {
				String qry = "from ResourceType where ResourceTypeName = '" + ws.get(i - 3).resourceType.toString()
						+ "'";
				System.out.println("qry = " + qry);
				rtype[i] = (ResourceType) XPersistence.getManager().createQuery(
						"from ResourceType where ResourceTypeName = '" + ws.get(i - 3).resourceType.toString() + "'")
						.getSingleResult();
				System.out.println("in get RT = " + rtype[i].getResourcetypename() + " " + i);

			} catch (Exception ex) {
				em("Exception in rst query " + ws.get(i - 3).resourceType + " " + ex);
				if (i == 16)
					return;
			}
		}

		for (i = ASSETLAND; i <= INPUTS; i++) { // Sheet
			// em("in loop ASSETLAND TO INPUTS = " + i);
			// breaksheet: for (j = 0; j < 35; j++) { // Row
			breaksheet: for (j = 0; j < numberRows[i]; j++) { // ws num rows in each sheet Row
				em("inp loop assets 777 " + numberRows[i] + " " + j);
				switch (i) {

				case ASSETLAND:
					try {
						al = new AssetLand();
						System.out.println("in set assetLand cell = i =" + i);
						al.setLandTypeEnteredName(cell[i][j][0].getStringCellValue());
						al.setUnit(cell[i][j][1].getStringCellValue());
						al.setNumberOfUnits(getCellDouble(cell[i][j][2]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							System.out.println("done al get = " + rst.getResourcetypename());

							al.setResourceSubType(rst);
							al.setStatus(efd.model.Asset.Status.Valid);

							/* Is Unit Entered Valid for this resource */
							em("done al get 11 ");
							if (!checkSubTypeEntered(al.getUnit(), rst))
								al.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							al.setStatus(efd.model.Asset.Status.Invalid);
						}

						hhi.getAssetLand().add(al);

						em("done al get 33 ");

						k = 100;

						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Land Asset worksheet " + ex);
						k = 100;
						break breaksheet;
					}

				case ASSETLIVESTOCK:
					try {
						als = new AssetLiveStock();
						System.out.println("in set assetLivestock cell = i =" + i);
						als.setLiveStockTypeEnteredName(cell[i][j][0].getStringCellValue());
						als.setUnit(cell[i][j][1].getStringCellValue());

						als.setNumberOwnedAtStart(getCellDouble(cell[i][j][2]));
						als.setPricePerUnit(getCellDouble(cell[i][j][3]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done als get = " + rst.getResourcetypename());

							als.setResourceSubType(rst);
							als.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(als.getUnit(), rst))
								als.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							als.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getAssetLiveStock().add(als);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock worksheet");
						k = 100;
						break breaksheet;
					}

				case ASSETTRADEABLE:
					try {
						atrade = new AssetTradeable();
						atrade.setTradeableTypeEnteredName(cell[i][j][0].getStringCellValue());
						atrade.setUnit(cell[i][j][1].getStringCellValue());

						atrade.setNumberOwned(getCellDouble(cell[i][j][2]));
						atrade.setPricePerUnit(getCellDouble(cell[i][j][3]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done atrade get = " + rst.getResourcetypename());

							atrade.setResourceSubType(rst);
							atrade.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(atrade.getUnit(), rst))
								atrade.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							atrade.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getAssetTradeable().add(atrade);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Other Tradeable worksheet");
						k = 100;
						break breaksheet;
					}

				case ASSETFOOD:
					try {
						afood = new AssetFoodStock();
						// System.out.println("in set assetfoodstock cell = i =" + i);
						warnMessage = "Food Type ";
						afood.setFoodTypeEnteredName(cell[i][j][0].getStringCellValue());
						warnMessage = "Unit";
						afood.setUnit(cell[i][j][1].getStringCellValue());

						warnMessage = "Quantity ";

						afood.setQuantity(getCellDouble(cell[i][j][2]));

						// Food Stocks come from Crops, Wild Foods, Food Purchases or Livestock Products
						// - there is not a Food Stock type in RST

						// Current Food Stock from Spreadsheet
						// is this a valid resource type?

						ResourceSubType rstcrops = null;
						ResourceSubType rstwildfoods = null;
						ResourceSubType rstfoodpurchase = null;

						if (((rstcrops = checkSubType(cell[i][j][0].getStringCellValue(),
								rtype[9].getIdresourcetype().toString())) != null)
								|| ((rstwildfoods = checkSubType(cell[i][j][0].getStringCellValue(),
										rtype[14].getIdresourcetype().toString())) != null)
								|| ((rstfoodpurchase = checkSubType(cell[i][j][0].getStringCellValue(),
										rtype[11].getIdresourcetype().toString())) != null))

						{
							// which rst?
							if (rstcrops != null)
								rst = rstcrops;
							else if (rstwildfoods != null)
								rst = rstwildfoods;
							else if (rstfoodpurchase != null)
								rst = rstfoodpurchase;

							afood.setResourceSubType(rst);
							afood.setStatus(efd.model.Asset.Status.Valid);

							// if (!checkSubTypeEntered(afood.getUnit(), rst))
							// afood.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							afood.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getAssetFoodStock().add(afood);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Asset Food Stocks worksheet " + warnMessage);
						k = 100;
						break breaksheet;
					}

				case ASSETTREE:
					try {
						atree = new AssetTree();
						System.out.println("in set assettree cell = i =" + i);
						atree.setTreeTypeEnteredName(cell[i][j][0].getStringCellValue());
						atree.setUnit(cell[i][j][1].getStringCellValue());
						atree.setNumberOwned(getCellDouble(cell[i][j][2]));
						atree.setPricePerUnit(getCellDouble(cell[i][j][3]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done atree get = " + rst.getResourcetypename());

							atree.setResourceSubType(rst);
							atree.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(atree.getUnit(), rst))
								atree.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							atree.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getAssetTree().add(atree);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Tree worksheet");
						k = 100;
						break breaksheet;
					}

				case ASSETCASH:
					try {
						acash = new AssetCash();
						System.out.println("in set assetcash cell = i =" + i);
						warnMessage = "Currency";
						acash.setCurrencyEnteredName(cell[i][j][0].getStringCellValue());
						acash.setUnit("each"); // a default value - not used elsewhere

						warnMessage = "Currency Amount";
						acash.setAmount(getCellDouble(cell[i][j][1]));

						/* check against currency */

						for (icurr = 0; icurr < currency.size(); icurr++) {
							if (currency.get(icurr).getCurrency().equals(cell[i][j][0].getStringCellValue())) {
								System.out.println("cash 1 = " + currency.get(icurr).getCurrency());
								System.out.println("cash 2 = " + cell[i][j][0].getStringCellValue());

								// rst = checkSubType("Cash", rtype[i].getIdresourcetype().toString());
								rst = checkSubType(cell[i][j][0].getStringCellValue(),
										rtype[i].getIdresourcetype().toString());
								System.out.println("in cash rst = " + rst);
								acash.setStatus(efd.model.Asset.Status.Valid);
								acash.setResourceSubType(rst);
								break;
							} else {
								acash.setStatus(efd.model.Asset.Status.Invalid);
							}

						}
						hhi.getAssetCash().add(acash);
						getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Cash worksheet - " + warnMessage);
						k = 100;
						break breaksheet;
					}

				case CROPS:
					System.out.println("in CROPs");
					try {
						acrop = new Crop();
						warnMessage = "";
						acrop.setCropType((cell[i][j][0].getStringCellValue()));
						acrop.setUnit(cell[i][j][1].getStringCellValue());

						acrop.setUnitsProduced(getCellDouble(cell[i][j][2]));
						acrop.setUnitsSold(getCellDouble(cell[i][j][3]));
						acrop.setPricePerUnit(getCellDouble(cell[i][j][4]));
						// acrop.setUnitsConsumed(getCellDouble(cell[i][j][5]));
						acrop.setUnitsOtherUse(getCellDouble(cell[i][j][6]));

						warnMessage = "Market 1";
						acrop.setMarket1(cell[i][j][7].getStringCellValue());
						warnMessage = "Percent Trade Market 1";
						acrop.setPercentTradeMarket1(getCellDouble(cell[i][j][8]));

						warnMessage = "Market 2";
						acrop.setMarket2(cell[i][j][9].getStringCellValue());
						warnMessage = "Percent Trade Market 2";
						acrop.setPercentTradeMarket2(getCellDouble(cell[i][j][10]));

						warnMessage = "Market 3";
						acrop.setMarket2(cell[i][j][11].getStringCellValue());
						warnMessage = "Percent Trade Market 3";
						acrop.setPercentTradeMarket3(getCellDouble(cell[i][j][12]));

						// System.out.println("in crop = " + cell[i][j][1].getStringCellValue());
						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done acrop get = " + rst.getResourcetypename());

							acrop.setResourceSubType(rst);
							acrop.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(acrop.getUnit(), rst))
								acrop.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							acrop.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getCrop().add(acrop);
						// getView().refreshCollections();

						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Crops worksheet - " + warnMessage);
						k = 100;
						break breaksheet;
					}

				case LIVESTOCKSALE:

					try {
						alss = new LivestockSales();
						System.out.println("in ls sales cell = i =" + i);
						alss.setLivestockType((cell[i][j][0].getStringCellValue()));
						alss.setUnit(cell[i][j][1].getStringCellValue());

						alss.setUnitsAtStartofYear(getCellDouble(cell[i][j][2]));

						alss.setUnitsSold(getCellDouble(cell[i][j][3]));
						alss.setPricePerUnit(getCellDouble(cell[i][j][4]));

						alss.setMarket1(cell[i][j][5].getStringCellValue());
						alss.setPercentTradeMarket1(getCellDouble(cell[i][j][6]));

						alss.setMarket2(cell[i][j][7].getStringCellValue());
						alss.setPercentTradeMarket2(getCellDouble(cell[i][j][8]));

						alss.setMarket3(cell[i][j][9].getStringCellValue());
						alss.setPercentTradeMarket3(getCellDouble(cell[i][j][10]));

						// System.out.println("rtype = " + rtype[i].getIdresourcetype().toString());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done alss get = " + rst.getResourcetypename());

							alss.setResourceSubType(rst);
							alss.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(alss.getUnit(), rst))
								alss.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							alss.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getLivestockSales().add(alss);
						getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock Sales worksheet");
						k = 100;
					}

				case LIVESTOCKPRODUCT:
					try {
						alsp = new LivestockProducts();
						alsp.setLivestockType((cell[i][j][0].getStringCellValue()));

						alsp.setLivestockProduct(cell[i][j][1].getStringCellValue());

						alsp.setUnit(cell[i][j][2].getStringCellValue());

						if (alsp.getUnit().isEmpty())
							alsp.setUnit("?");

						alsp.setUnitsProduced(getCellDouble(cell[i][j][3]));
						alsp.setUnitsSold(getCellDouble(cell[i][j][4]));
						alsp.setPricePerUnit(getCellDouble(cell[i][j][5]));
						// alsp.setUnitsConsumed(getCellDouble(cell[i][j][6]));
						alsp.setUnitsOtherUse(getCellDouble(cell[i][j][7]));

						alsp.setMarket1(cell[i][j][8].getStringCellValue());
						alsp.setPercentTradeMarket1(getCellDouble(cell[i][j][9]));

						alsp.setMarket2(cell[i][j][10].getStringCellValue());
						alsp.setPercentTradeMarket2(getCellDouble(cell[i][j][11]));

						alsp.setMarket3(cell[i][j][12].getStringCellValue());
						alsp.setPercentTradeMarket3(getCellDouble(cell[i][j][13]));

						System.out.println("about to do LSP checksubtype " + cell[i][j][0].getStringCellValue());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							System.out.println("done alsp get = " + rst.getResourcetypename());

							alsp.setResourceSubType(rst);
							alsp.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(alsp.getUnit(), rst))
								alsp.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							alsp.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getLivestockProducts().add(alsp);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Livestock Products worksheet " + warnMessage);
						k = 100;
						break breaksheet;
					}

				case EMPLOYMENT:
					try {
						aemp = new Employment();
						int l = 0;
						System.out.println("in emp  cell = i =" + i);
						aemp.setEmploymentName((cell[i][j][l++].getStringCellValue()));
						aemp.setPeopleCount(getCellDouble(cell[i][j][l++]));
						aemp.setUnit((cell[i][j][l++].getStringCellValue()));
						aemp.setUnitsWorked(getCellDouble(cell[i][j][l++]));
						aemp.setCashPaymentAmount(getCellDouble(cell[i][j][l++]));
						aemp.setFoodPaymentFoodType((cell[i][j][l++].getStringCellValue()));
						aemp.setFoodPaymentUnit((cell[i][j][l++].getStringCellValue()));
						aemp.setFoodPaymentUnitsPaidWork((cell[i][j][l++].getStringCellValue()));
						System.out.println("in emp  cell = i =" + i);
						aemp.setWorkLocation1(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation1(getCellDouble(cell[i][j][l++]));
						aemp.setWorkLocation2(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation2(getCellDouble(cell[i][j][l++]));
						aemp.setWorkLocation3(cell[i][j][l++].getStringCellValue());
						aemp.setPercentWorkLocation3(getCellDouble(cell[i][j][l++]));
						System.out.println("in emp  cell = l =" + l);
						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// System.out.println("done emp get = " + rst.getResourcetypename());
							System.out.println("in emp  status is valid 1");
							aemp.setResourceSubType(rst);

							aemp.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(aemp.getUnit(), rst))
								aemp.setStatus(efd.model.Asset.Status.Invalid);
							System.out.println("in emp  status is invalid 1");
						} else {

							aemp.setStatus(efd.model.Asset.Status.Invalid);
						}

						// If there is a food payment then need to validate food payment rst

						if (!aemp.getFoodPaymentFoodType().isEmpty()) {
							/* check Food as payment entered is valid */
							if ((checkSubType(aemp.getFoodPaymentFoodType(), "Crop") != null) // then it is a valid
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Wild Foods") != null)
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Livestock Product") != null)
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Food Purchase") != null)) {
								aemp.setStatus(efd.model.Asset.Status.Valid);
							}

							// Now need to check food unit is valid

							if (!checkSubTypeEntered(aemp.getFoodPaymentUnit(), rst))
								aemp.setStatus(efd.model.Asset.Status.Invalid);
						}

						System.out.println("in emp  33");
						hhi.getEmployment().add(aemp);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Employee worksheet");
						System.out.println("Error in Emp = " + ex);
						k = 100;
						break breaksheet;
					}
				case TRANSFER:
					try {
						at = new Transfer();
						int l = 0;

						System.out.println("Official text = " + cell[i][j][l].getStringCellValue());

						if (cell[i][j][l++].getStringCellValue().equals("Official"))
							at.setIsOfficial(true);
						else
							at.setIsOfficial(false);

						// System.out.println("Official boolean set to = "+at.getIsOfficial());

						// Handle the enum

						System.out.println("Transfer 000 j l " + j + " " + l);

						at.setSource(cell[i][j][l++].getStringCellValue());

						String upperCaseTransferType = upperCaseFirst(cell[i][j][l].getStringCellValue());
						System.out.println("Transfer 001 j l " + j + " " + l);

						if (upperCaseTransferType.equals("Cash"))
							at.setTransferType(efd.model.Transfer.TransferType.Cash);
						else if (upperCaseTransferType.equals("Other"))
							at.setTransferType(efd.model.Transfer.TransferType.Other);
						if (upperCaseTransferType.equals("Food"))
							at.setTransferType(efd.model.Transfer.TransferType.Food);
						l++;
						System.out.println("Transfer 00 2 j l " + j + " " + l);

						at.setPeopleReceiving(getCellDouble(cell[i][j][l++]));

						at.setTimesReceived(getCellDouble(cell[i][j][l++]));
						at.setCashTransferAmount(getCellDouble(cell[i][j][l++]));
						at.setTransferFoodOtherType((cell[i][j][l++].getStringCellValue()));
						System.out.println("Transfer 2222 j l " + j + " " + l);
						at.setUnit(cell[i][j][l++].getStringCellValue());
						if (at.getUnit().isEmpty())
							at.setUnit("?"); // seem to have many examples where this is left blank..

						at.setUnitsTransferred(getCellDouble(cell[i][j][l++]));
						at.setUnitsSold(getCellDouble(cell[i][j][l++]));
						at.setPricePerUnit(getCellDouble(cell[i][j][l++]));
						System.out.println("Transfer 3333 j l " + j + " " + l);
						at.setOtherUse(getCellDouble(cell[i][j][l++]));
						// at.setUnitsConsumed(getCellDouble(cell[i][j][l++]));
						l++;
						at.setMarket1(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket1(getCellDouble(cell[i][j][l++]));
						System.out.println("Transfer 4444  j l " + j + " " + l);
						at.setMarket2(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket2(getCellDouble(cell[i][j][l++]));

						at.setMarket3(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket3(getCellDouble(cell[i][j][l++]));

						if ((rst = checkSubType(cell[i][j][2].getStringCellValue(), // is this a valid resource type -
								// note that Transfer type is not in
								// column 0 like everything else
								rtype[i].getIdresourcetype().toString())) != null) {

							at.setResourceSubType(rst);
							at.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(at.getUnit(), rst))
								at.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							at.setStatus(efd.model.Asset.Status.Invalid);

						}

						/* Need to check if Transfer Type of Food is valid if entered in column 6 */
						/* Need to check if Valid Food */
						/*
						 * Only test if there is a rst in this cell
						 * 
						 * Crops, Wild Food, Food Purchases, Livestock, Livestock Products
						 */
						System.out.println("Transfer 111 j l " + j + " " + l);
						if (!cell[i][j][6].getStringCellValue().isEmpty()) {
							if (((rst = checkSubType(cell[i][j][6].getStringCellValue(),
									rtype[CROPS].getIdresourcetype().toString())) != null)
									|| ((rst = checkSubType(cell[i][j][6].getStringCellValue(),
											rtype[WILDFOOD].getIdresourcetype().toString())) != null)
									|| ((rst = checkSubType(cell[i][j][6].getStringCellValue(),
											rtype[LIVESTOCKPRODUCT].getIdresourcetype().toString())) != null)
									|| ((rst = checkSubType(cell[i][j][6].getStringCellValue(),
											rtype[ASSETTRADEABLE].getIdresourcetype().toString())) != null))

							{ // set FoodRST and check Unit entered

								at.setFoodResourceSubType(rst);
								at.setStatus(efd.model.Asset.Status.Valid);
								if (!checkSubTypeEntered(at.getUnit(), rst))
									at.setStatus(efd.model.Asset.Status.Invalid);

							} else {
								at.setStatus(efd.model.Asset.Status.Invalid);

							}
						}
						hhi.getTransfer().add(at);
						// getView().refreshCollections();
						k = 100;

						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Transfers worksheet " + ex);

						k = 100;
						break breaksheet;
					}
				case WILDFOOD:
					try {
						awf = new WildFood();
						int l = 0;

						awf.setWildFoodName(cell[i][j][l++].getStringCellValue());
						awf.setUnit(cell[i][j][l++].getStringCellValue());

						awf.setUnitsProduced(getCellDouble(cell[i][j][l++]));
						awf.setUnitsSold(getCellDouble(cell[i][j][l++]));
						awf.setPricePerUnit(getCellDouble(cell[i][j][l++]));
						// awf.setUnitsConsumed(getCellDouble(cell[i][j][l++]));
						l++;
						awf.setOtherUse(getCellDouble(cell[i][j][l++]));

						awf.setMarket1(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket1(getCellDouble(cell[i][j][l++]));
						awf.setMarket2(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket2(getCellDouble(cell[i][j][l++]));
						awf.setMarket3(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket3(getCellDouble(cell[i][j][l++]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							System.out.println("done wildfood get =  " + rst.getResourcetypename());

							awf.setResourceSubType(rst);
							awf.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(awf.getUnit(), rst))
								awf.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							awf.setStatus(efd.model.Asset.Status.Invalid);
						}
						hhi.getWildFood().add(awf);
						getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Wild Food worksheet ");
						k = 100;
						break breaksheet;
					}

				case INPUTS:

					ResourceSubType rst1 = null;
					ResourceSubType rst2 = null;

					em("in INPUTS i = " + i);
					try {

						Inputs ins = new Inputs();
						int l = 0;
						Boolean assetOK1 = false;
						Boolean assetOK2 = false;

						ins.setItemPurchased(cell[i][j][l++].getStringCellValue());

						ins.setUnit(cell[i][j][l++].getStringCellValue());

						ins.setUnitsPurchased(getCellDouble(cell[i][j][l++]));
						ins.setPricePerUnit(getCellDouble(cell[i][j][l++]));

						ins.setResource1UsedFor(cell[i][j][l++].getStringCellValue());
						ins.setPercentResource1(getCellDouble(cell[i][j][l++]));
						ins.setResource2UsedFor(cell[i][j][l++].getStringCellValue());
						ins.setPercentResource2(getCellDouble(cell[i][j][l++]));
						ins.setResource3UsedFor(cell[i][j][l++].getStringCellValue());
						ins.setPercentResource3(getCellDouble(cell[i][j][l++]));

						// Check Input resource - Non Food Product or Other Tradeable Goods

						if (!ins.getItemPurchased().isEmpty()) {

							if ((rst1 = checkSubType(cell[i][j][0].getStringCellValue(),
									rtype[ASSETTRADEABLE].getIdresourcetype().toString())) != null) {
								System.out.println("tradeable  found ");
								assetOK1 = true;
								rst = rst1;
							} else {
								System.out.println("back in inputs rst not found ");
							}

							if ((rst2 = checkSubType(cell[i][j][0].getStringCellValue(),
									rtype[NONFOODPURCHASE].getIdresourcetype().toString())) != null) {

								assetOK2 = true;
								rst = rst2;
							}

							if (assetOK1 || assetOK2)

							{

								// check Unit entered

								ins.setResourceSubType(rst);

								ins.setStatus(efd.model.Asset.Status.Valid);

								// Check if Resource 1/2/3 Used for are Valid

								if (checkInputsResource(ins.getResource1UsedFor(), ins.getResource2UsedFor(),
										ins.getResource3UsedFor()))

								{

									ins.setStatus(efd.model.Asset.Status.Valid);

								}

								else {

									ins.setStatus(efd.model.Asset.Status.Invalid);
								}

								if (!checkSubTypeEntered(ins.getUnit(), rst)) {
									ins.setStatus(efd.model.Asset.Status.Invalid);

								}

							} else {

								ins.setStatus(efd.model.Asset.Status.Invalid);

							}

						}

						em("done INPUTS " + ins.getItemPurchased());
						hhi.getInputs().add(ins);

						// k = 100; // Possibility of next row being valid
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Inputs worksheet " + ex);
						k = 100;
						break breaksheet;
					}

				} // end switch

			}
		}
		System.out.println("done set resource");
	}

	/**************************************************************************************************************************************************************************************************/

	private Boolean checkInputsResource(String resource1UsedFor, String resource2UsedFor, String resource3UsedFor) {

		ArrayList<String> inputStrings = new ArrayList(); // Prob want a sorted list..

		List<ResourceSubType> resourceSubTypes = XPersistence.getManager().createQuery("from ResourceSubType")
				.getResultList();
		List<ResourceType> resourceTypes = XPersistence.getManager().createQuery("from ResourceType").getResultList();
		List<Category> categories = XPersistence.getManager().createQuery("from Category").getResultList();

		for (int k = 0; k < resourceSubTypes.size(); k++) {
			inputStrings.add(resourceSubTypes.get(k).getResourcetypename().toString());
		}
		for (int k = 0; k < resourceTypes.size(); k++) {
			inputStrings.add(resourceTypes.get(k).getResourcetypename().toString());

		}
		for (int k = 0; k < categories.size(); k++) {
			inputStrings.add(categories.get(k).getCategoryName().toString());
		}
		// Collections.sort(inputStrings);

		if (inputStrings.contains(resource1UsedFor) || resource1UsedFor.isEmpty())
			return (true);
		else if (inputStrings.contains(resource2UsedFor) || resource2UsedFor.isEmpty())
			return (true);
		else if (inputStrings.contains(resource3UsedFor) || resource3UsedFor.isEmpty())
			return (true);
		else
			return (false);
	}

	/**************************************************************************************************************************************************************************************************/
	private Boolean checkSubTypeEntered(String unitEntered, ResourceSubType rst) {

		/*
		 * need to handle as Valid
		 * 
		 * kg = KG
		 * 
		 * kgs = KG
		 * 
		 * kg = kgs +acres etc.
		 */

		// Change to use .isEmpty

		System.out.println("in checkSubTypeEntered " + unitEntered + " " + rst.getResourcesubtypeunit());
		if (unitEntered.isEmpty() || unitEntered.equals(null)) {
			// empty thus invalid
			return false;

		}
		String unitRst = stripS(rst.getResourcesubtypeunit());

		unitEntered = stripS(unitEntered);

		if (unitRst.equals(unitEntered)) {

			return (true);
		}

		return (false);
	}

	/**************************************************************************************************************************************************************************************************/

	private ResourceSubType checkSubType(String var1, String resourceType) {

		/*
		 * 
		 * Test for RST pig/pigs/Pig/pIG entered value is in RST table and set Valid if
		 * match found
		 * 
		 * 
		 */
		ResourceSubType rsty = null;
		// var1 = WordUtils.capitalize(var1);
		String var2 = stripS(var1);
		String var3 = addS(var1);

		em("checksubtype rst = " + resourceType);

		System.out.println("var 1 = " + var1);
		System.out.println("var 2 = " + var2);
		System.out.println("var 3 = " + var3);

		try {

			System.out.println("in rst try 555");

			rsty = (ResourceSubType) XPersistence.getManager()
					.createQuery("from ResourceSubType where resourcetype = '" + resourceType + "'" + "and ("
							+ "upper(resourcetypename) = '" + var2 + "'" + " or " + "upper(resourcetypename) = '" + var3
							+ "'" + ") ")
					.setMaxResults(1).getSingleResult();

			System.out.println("rsty = " + rsty.getResourcetypename());

			return (rsty);
		} catch (Exception ex) {
			System.out.println("Exception:rst not found rsty = " + rsty + " " + ex);

			return (rsty);
		}

	}

	/*
	 * check RST synonym
	 * 
	 */

	/*
	 * 
	 * try { if (rsty.getResourcesubtypesynonym().getResourcetypename() == null)
	 * return rsty;
	 * 
	 * }
	 * 
	 * // return RST that is not a synonym - just catch exception that the synonym
	 * does // not exist catch (Exception ex) {
	 * System.out.println("return cash rsty =  " + rsty.getResourcetypename());
	 * return rsty; }
	 * 
	 * // System.out.println("done rst get syn query in check sub type ");
	 * 
	 * // otherwise get the RST for the Synonym
	 * 
	 * ResourceSubType rstysyn = (ResourceSubType)
	 * XPersistence.getManager().find(ResourceSubType.class,
	 * rsty.getResourcesubtypesynonym().getIdresourcesubtype().toString()); return
	 * rstysyn; }
	 *
	 * catch (Exception ex) { System.out.println("Failed checkSubType " + ex);
	 * return null; // no record found to match data entered
	 * 
	 * }
	 */

	/**************************************************************************************************************************************************************************************************/
	private String stripS(String str) {

		str = str.toUpperCase();

		if (str.substring(str.length() - 1).equals("S")) {

			return str.substring(0, str.length() - 1);
		}

		return str;
	}

	/**************************************************************************************************************************************************************************************************/
	private String addS(String str) {
		str = str.toUpperCase();
		if (str.substring(str.length() - 1).equals("S")) {
			return (str);
		}

		return str + "S";
	}

	/**************************************************************************************************************************************************************************************************/
	private String convertToString(Double cellValue) {

		if (cellValue.equals(""))
			return "0.0";
		else
			return cellValue.toString();

	}

	private Double convertToDouble(String cellValue) {
		if (cellValue.equals(""))
			return 0.0;
		else
			return Double.parseDouble(cellValue);

	}

	/**************************************************************************************************************************************************************************************************/

	private Double getCellDouble(Cell acell) {

		Double d = 0.0;

		if (acell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			// System.out.println("in getDouble Number = "+acell.getNumericCellValue());
			return acell.getNumericCellValue();
		} else if (acell.getCellType() == 1) {
			// System.out.println("in getDouble String = ");
			return Double.valueOf(d);
		} else if (acell.getCellType() == Cell.CELL_TYPE_BLANK) {
			// System.out.println("in getDouble Empty = "+Double.valueOf(d));
			return Double.valueOf(d);
		}
		// System.out.println("returning from getcelldouble "+d);
		return d;

	}

	/**************************************************************************************************************************************************************************************************/
	public static String upperCaseFirst(String value) {

		// Convert String to char array.
		char[] array = value.toCharArray();
		// Modify first element in array.
		array[0] = Character.toUpperCase(array[0]);
		// Return string.
		return new String(array);
	}

	/**************************************************************************************************************************************************************************************************/

	private Boolean checkCell(String cname, Sheet sheet, int x, int y, Boolean nullable) /* Is spreadsheet Cell Valid */
	{
		Cell cell;
		String stest;
		Double dtest;

		try {
			// System.out.println("check cell type = " + cname);
			cell = sheet.getRow(y).getCell(x, Row.CREATE_NULL_AS_BLANK);

			// System.out.println("77 cell type in check cell = " + cell.getCellType());
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
				// System.out.println("in cell type 0 check for numeric ");
				try {
					// System.out.println("in try ");
					dtest = cell.getNumericCellValue();
					// System.out.println("done try ");
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
				// System.out.println("in cell type 1 check for string null? " + nullable +
				// cell.getStringCellValue());
				try {
					stest = cell.getStringCellValue();
					if (stest.isEmpty() && nullable) {
						// System.out.println("in cell type 1 check for string return false");
						return false;
					}

				} catch (Exception ex) {
					if (nullable) /* empty is ok */
					{
						// System.out.println("nullable string return false");
						return false;
					}
					if (!nullable) {
						addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
						// You can throw any runtime exception here
						// System.out.println("caught in check cell");
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

			// System.out.println("return true");
			localMessage(cname + " is correct");
			return true;

		}

		catch (Exception ex) {
			addError("Incomplete Spreadsheet data '" + cname + "' Error - correct spreadsheet and upload again");
			// You can throw any runtime exception here
			// System.out.println("caught in check cell");
			return false;
		}

	}

	class QandA {
		private String question;
		private String answer;

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

	}

	@Override
	public void setConnectionProvider(IConnectionProvider provider) {
		// TODO Auto-generated method stub
		this.provider = provider;

	}

}
