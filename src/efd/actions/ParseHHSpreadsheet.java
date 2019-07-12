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

import org.apache.commons.lang.*;
import org.apache.commons.lang3.text.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.format.*;
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
	LivelihoodZone livelihoodZone = null;

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

		em("in xls parse ");

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
		/* Otherwise. delete assets from this Household */

		em("in xls parse 2 ");

		
		livelihoodZone = hhi.getStudy().getSite().getLivelihoodZone();
				
		
		String spreadsheetId = hhi.getSpreadsheet();

		Connection con = null;

		// what schema?

		// em("XP schema = "+XPersistence.getDefaultSchema());
		String schema = XPersistence.getDefaultSchema();
		em("schema = " + schema);

		try {
			con = DataSourceConnectionProvider.getByComponent("Household").getConnection();

			// em("schema con = "+con.getSchema().toString());
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
			em("done inputstream ");

			/* Move to first row */

			String spreadsheetPkey = rs.getString(1);

			spreadsfile = find(spreadsheetPkey);
			em("done attache file get");
			ps.close();

			wb = WorkbookFactory.create(input);
			wb.setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);

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

		HouseholdMember hm = null;
		for (HouseholdMember householdMember : hhi.getHouseholdMember()) {
			hm = XPersistence.getManager().find(HouseholdMember.class, householdMember.getId());
			XPersistence.getManager().remove(hm);

		}
		em("deleted hhm");

		em("size of ca q = " + hhi.getConfigAnswer().size());
		ConfigAnswer ca = null;
		for (ConfigAnswer configAnswer : hhi.getConfigAnswer()) {
			ca = XPersistence.getManager().find(ConfigAnswer.class, configAnswer.getId());
			em("current answer = " + ca.getConfigQuestionUse().getConfigQuestion().getPrompt());
			XPersistence.getManager().remove(ca);
		}
		em("deleted canswers");

		getView().findObject();

		em("hhm tot now =" + hhi.getHouseholdMember().size());

		// hhi.getFoodPurchase().removeAll(hhi.getFoodPurchase());
		// hhi.getNonFoodPurchase().removeAll(hhi.getNonFoodPurchase());

		/* Get the WS details */
		if (!getInterviewDetails(wb, hhi)) {
			addError("Failed to parse spreadsheet");
			return;
		}

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
		// return;

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
			// em(message);
			addMessage(message);
		}
	}

	/**************************************************************************************************************************************************************************************************/

	/*
	 * get Interview details
	 */

	private Boolean getInterviewDetails(Workbook wb, Household hhi) {

		Cell icell = null;
		Date hhIDate = null;

		em(" start getInterviewDetails ");

		Sheet sheet = wb.getSheetAt(0); // Interview Details is first sheet (0)

		/*
		 * Check if correct Project
		 */

		String studyName = hhi.getStudy().getStudyName() + " " + hhi.getStudy().getReferenceYear();
		em("hhi studyName = " + studyName);

		icell = sheet.getRow(3).getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

		em("first get = " + icell.getStringCellValue());

		if (!icell.getStringCellValue().equals(studyName)) // Not for this project
		{
			addWarning("Spreadsheet is not for current Study . Spreadsheet Study Name + Reference Year = "
					+ icell.getStringCellValue() + ", Study Project = " + studyName);
			// return false;
		}

		em("11");
		String hhName = sheet.getRow(5).getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();

		em("22 ct = " + sheet.getRow(5).getCell(4).getCellType());

		icell = sheet.getRow(5).getCell(4);
		em("get icell type = " + icell.getCellType());
		if (icell.getCellType() == 0) // Date
		{
			em("set i date 22");
			hhIDate = sheet.getRow(5).getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue();
			em("set i date 44");
		} else {
			addError("Interview Date is empty and required");
			return (false);
		}

		em("33");
		String hhInterviewers = sheet.getRow(5).getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
				.getStringCellValue();
		em("44");
		if (hhName.isEmpty())
			hhi.setHouseholdName(null);
		else
			hhi.setHouseholdName(hhName);
		em("55");
		hhi.setInterviewDate(hhIDate);
		em("66");
		hhi.setInterviewers(hhInterviewers);

		em(" end getInterviewDetails ");
		return (true);

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
				qcell = sheet.getRow(2 + i).getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

			} catch (Exception ex) {
				break;
			}

			if (qcell.getStringCellValue().isEmpty())
				break; // no more questions - but should be caught by number of questions

			// What celltype is answer?

			qa = new QandA();

			acell = sheet.getRow(2 + i).getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

			if (acell.getCellType() == Cell.CELL_TYPE_STRING) {

				qa.setAnswer(acell.getStringCellValue());

			} else if (acell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

				answerDouble = acell.getNumericCellValue();

				answerString = Double.toString(answerDouble);

				qa.setAnswer(answerString.toString());

			}

			qa.setQuestion(qcell.getStringCellValue());
			em("in loop 3");
			// if (qa.getAnswer().isEmpty()) { // answer is nullable so not needed
			// qa.setAnswer("-");
			// }
			em("qanda = " + i + " " + qa.question + " " + qa.answer);
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

						// qanswer.setStudy(nextq.getStudy());

						qanswer.setStudy(hhi.getStudy());

						em("adding HH answer " + qand.get(k).getAnswer());

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
		Integer age = 0;
		Integer yob = 0;
		String absent = null;
		String absentReason = null;
		int firstRow = 11;

		HouseholdMember hhm = null;

		Sheet sheet = wb.getSheetAt(HOUSEHOLDMEMBERS);
		/*
		 * get standard HHM details Note that members go across spreadsheet
		 * 
		 */

		// get first Member

		FormulaEvaluator objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) wb);
		DataFormatter objDefaultFormat = new DataFormatter();

		for (int hhmcol = 2; hhmcol < 22; hhmcol++) {

			hhm = new HouseholdMember();

			hhm.setHousehold(hhi);

			try {
				em("hh members try");

				/*
				 * Name / ID could be text or numeric
				 * 
				 * FormulaEvaluator objFormulaEvaluator = new
				 * HSSFFormulaEvaluator((HSSFWorkbook) wb); DataFormatter objDefaultFormat = new
				 * DataFormatter();
				 * 
				 * is the way to get number into a string from excel via POI
				 * 
				 */

				// em("HHM ID cell type = "+sheet.getRow(3).getCell(hhmcol).getCellType());

				hhm.setHouseholdMemberName(
						objDefaultFormat.formatCellValue(sheet.getRow(3).getCell(hhmcol), objFormulaEvaluator));

				// hhm.setHouseholdMemberName(
				// sheet.getRow(3).getCell(hhmcol).toString());
				em("hhm  18 ");
				gender = sheet.getRow(4).getCell(hhmcol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();
				em("hhm 19 ");
				age = (int) sheet.getRow(5).getCell(hhmcol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getNumericCellValue();
				yob = ((int) (sheet.getRow(6).getCell(hhmcol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getNumericCellValue()));

				// Improve check for no more HH
				if (gender == "" && age == 0 && yob == 0) {
					em("no more members");
					hhmcol = 23;
					break;
				}

				if (yob == 0 && age == 0) {
					em("age and yob not entered");
					addError("Parse Failed - Age and Year of Birth entered in spreadsheet for Household Member "
							+ (hhmcol - 1));
					return 23001;
				}

				gender = StringUtils.capitalize(gender); // now handles male/female as well as LOV Male/Female

				em("hhm 20 ");
				if (gender.equals("Male")) {
					hhm.setGender(Sex.Male);
				} else if (gender.equals("Female")) {
					hhm.setGender(Sex.Female);
				} else {
					hhm.setGender(Sex.Unknown);
				}

				int thisYear = hhm.getHousehold().getStudy().getReferenceYear();
				em("age in read ss = " + age);
				em("yob in read ss = " + yob);
				hhm.setAge(age);

				// calculated

				hhm.setYearOfBirth(yob);
				// hhm.setYearOfBirth(thisYear - hhm.getAge()); // ignore entered YOB unless it
				// is 0 or age is 0

				// Validate Age and YOB
				if (age == null || age < 0 || age > 120) {
					addError("Parse Failed - Age incorrect for Household Member " + (hhmcol - 1));
					return 23002;
				}
				if (age == 0) // use YOB to calc Age
				{
					em("Age is 0, this year - yob" + thisYear + " " + hhm.getYearOfBirth());
					hhm.setAge(thisYear - hhm.getYearOfBirth());
				}

				if (yob == 0) {
					em("YOB is 0, this year - yob" + thisYear + " " + hhm.getYearOfBirth());
					hhm.setYearOfBirth(thisYear - age);
				}

				em("hh 23 ");
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
					em("absent Y = " + absent);
					hhm.setAbsent(YN.Yes);
				} else if (absent.equals("No")) {
					em("absent N = " + absent);
					hhm.setAbsent(YN.No);
				}

				absentReason = sheet.getRow(9).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				if (!absentReason.isEmpty()) {
					hhm.setReasonForAbsence(absentReason);
				}

				hhm.setMonthsAway((int) (sheet.getRow(10).getCell(hhmcol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getNumericCellValue()));
				if (hhm.getMonthsAway() == 0) {
					hhm.setAbsent(YN.No);
				} else
					hhm.setAbsent(YN.Yes);

				int hhmNumber = hhmcol - 1;
				hhm.setHouseholdMemberNumber("HHM" + hhmNumber);

				XPersistence.getManager().persist(hhm);

			} catch (Exception ex) {
				addError("Parse Failed - Wrong template for Household Members " + ex);
				return (23003);
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
					qcell = sheet.getRow(firstRow + i).getCell(1, Row.CREATE_NULL_AS_BLANK);

				} catch (Exception ex) {
					break;
				}

				if (qcell.getStringCellValue().isEmpty())
					break; // no more questions - but should be caught by number of questions

				// What celltype is answer?

				qa = new QandA();

				/* 11 is first row of extra questions */

				acell = sheet.getRow(firstRow + i).getCell(hhmcol, Row.CREATE_NULL_AS_BLANK);

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
				em("save in array  qand = " + qa.getQuestion() + " " + qa.getAnswer());
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
				if (nextq.getConfigQuestion().getLevel() == Level.HouseholdMember) {
					em("prompt = " + nextq.getConfigQuestion().getPrompt().toString());
					// Find answer for this question
					for (k = 0; k < qand.size(); k++) {

						em("qand = " + qand.get(k).getQuestion());
						em("cq = " + nextq.getConfigQuestion().getPrompt());

						if (qand.get(k).getQuestion().equals(nextq.getConfigQuestion().getPrompt())) {

							em("qand a MATCH");

							qanswer = new ConfigAnswer();
							qanswer.setConfigQuestionUse(nextq);

							qanswer.setAnswer(qand.get(k).getAnswer());

							// qanswer.setHousehold(hhi);
							// not needed - it will appear in HH answer list

							// qanswer.setStudy(hhi.getStudy());

							qanswer.setHouseholdMember(hhm);

							XPersistence.getManager().persist(qanswer);

						}

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
							em("No more data in this sheet " + k + " " + i);
							numberRows[k] = i;
							i = 100;
							j = 100;

							break;

						}

						cell[k][i][j] = sheet.getRow(i + 3).getCell((j + 1), Row.CREATE_NULL_AS_BLANK);

						// Used for testing

						if (k == LIVESTOCKPRODUCT) {

							em("LIVESTOCKPRODUCT read cell type = " + cell[k][i][j].getCellType() + "ijk= " + k + " " + i + " " + j);

							if (cell[k][i][j].getCellType() == 0)
								em("read cell number type = " + cell[k][i][j].getNumericCellValue());

							if (cell[k][i][j].getCellType() == 1)
								em("read cell string type = " + cell[k][i][j].getStringCellValue());
						}

						// if first column is blank then no more data in this sheet

						if (cell[k][i][0].getCellType() == 3) {
							// em("In read No more data in this sheet " + k + " " + i);
							numberRows[k] = i; // re included DRB 19/11/2018
							i = 100;
							j = 100;

							break;
						}

						// if first col check if empty, if so then end of rows on this sheet

						if (j == 0 && cell[k][i][0].getStringCellValue().isEmpty()) {

							// record numrows in each sheet
							numberRows[k] = i;
							em("in read numberrows = " + numberRows[k] + " " + k);
							i = 100;
							break;
						}
					}
				}
			} catch (Exception ex) {
				em("in catch for get cell i j k =  " + ex + i + j + k);
				addError("Error in number of columns in Sheet = " + k + " " + i + " " + j + " " + k);

			}
		}

		em("done worksheet detail ");

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
		Boolean isCorrectRST = false;
		

		// for (int p = 0; p < 15; p++)
		// em("numrows array p = " + p + " " + numberRows[p]);

		/*
		 * Note that a cell matrix is used - populated in getWorkSheetDetail above
		 * 
		 * print cell array for (i = 1; i < 15; i++) { for (j = 0; j < 10; j++) { for (k
		 * = 0; k < 10; k++) em("Cell at i j k = " + i + j + k + " " + cell[i][j][k]); }
		 * }
		 */

		/* set validation rtypes array */

		List<ResourceType> rst3 = (List<ResourceType>) XPersistence.getManager().createQuery("from ResourceType")
				.getResultList();

		for (ResourceType type : rst3) {
			em("rst3 = " + type.getResourcetypename());
		}

		for (i = ASSETLAND; i <= NONFOODPURCHASE; i++) {
			// em("in rt setup loop " + ASSETLAND + " " + INPUTS + " " + i +
			// " "
			// + ws.get(i - 3).resourceType.toString());
			try {
				String qry = "from ResourceType where ResourceTypeName = '" + ws.get(i - 3).resourceType.toString()
						+ "'";
				em("qry = " + qry);
				rtype[i] = (ResourceType) XPersistence.getManager().createQuery(
						"from ResourceType where ResourceTypeName = '" + ws.get(i - 3).resourceType.toString() + "'")
						.getSingleResult();
				em("in get RT = " + rtype[i].getResourcetypename() + " " + i);

			} catch (Exception ex) {
				em("Exception in rst query " + ws.get(i - 3).resourceType + " " + ex);
				if (i == 16)
					return;
			}
		}

		for (i = ASSETLAND; i <= INPUTS; i++) { // Sheet
		
		breaksheet: for (j = 0; j < numberRows[i]; j++) { // ws num rows in each sheet Row
				em("inp loop assets 777 " + numberRows[i] + " " + j);
				switch (i) {

				case ASSETLAND:
					try {
						al = new AssetLand();
						em("in set assetLand cell = i =" + i);
						al.setLandTypeEnteredName(cell[i][j][0].getStringCellValue());
						al.setUnit(cell[i][j][1].getStringCellValue());
						al.setNumberOfUnits(getCellDouble(cell[i][j][2]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							em("done al get = " + rst.getResourcetypename());

							al.setResourceSubType(rst);
							al.setStatus(efd.model.Asset.Status.Valid);
							isCorrectRST = true;

							/* Is Unit Entered Valid for this resource */
							em("done al get 11 ");
							if (!checkSubTypeEntered(al.getUnit(), rst))
								al.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							al.setStatus(efd.model.Asset.Status.Invalid);
						}

						// Was a Local Unit entered for this RST LZ combination?
						em("about to do LAND  localunit");
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
						em("Land localunit =" + localUnit.getName());
						if (localUnit != null) {
							

							if (al.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								
								al.setUnit(localUnit.getName());
								al.setLocalUnit(localUnit.getName());
								al.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									al.setStatus(efd.model.Asset.Status.Valid);
								}
							}
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
						em("in set assetLivestock cell = i =" + i);
						als.setLiveStockTypeEnteredName(cell[i][j][0].getStringCellValue());
						als.setUnit(cell[i][j][1].getStringCellValue());

						als.setNumberOwnedAtStart(getCellDouble(cell[i][j][2]));
						als.setPricePerUnit(getCellDouble(cell[i][j][3]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// em("done als get = " + rst.getResourcetypename());

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
							// em("done atrade get = " + rst.getResourcetypename());

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
						// em("in set assetfoodstock cell = i =" + i);
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
						em("in set assettree cell = i =" + i);
						atree.setTreeTypeEnteredName(cell[i][j][0].getStringCellValue());
						atree.setUnit(cell[i][j][1].getStringCellValue());
						atree.setNumberOwned(getCellDouble(cell[i][j][2]));
						atree.setPricePerUnit(getCellDouble(cell[i][j][3]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// em("done atree get = " + rst.getResourcetypename());

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
						em("in set assetcash cell = i =" + i);
						warnMessage = "Currency";
						acash.setCurrencyEnteredName(cell[i][j][0].getStringCellValue());
						acash.setUnit("each"); // a default value - not used elsewhere

						warnMessage = "Currency Amount";
						acash.setAmount(getCellDouble(cell[i][j][1]));

						/* check against currency */

						for (icurr = 0; icurr < currency.size(); icurr++) {
							if (currency.get(icurr).getCurrency().equals(cell[i][j][0].getStringCellValue())) {
								em("cash 1 = " + currency.get(icurr).getCurrency());
								em("cash 2 = " + cell[i][j][0].getStringCellValue());

								// rst = checkSubType("Cash", rtype[i].getIdresourcetype().toString());
								rst = checkSubType(cell[i][j][0].getStringCellValue(),
										rtype[i].getIdresourcetype().toString());
								em("in cash rst = " + rst);
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
					em("in CROPs");
					try {
						acrop = new Crop();
						warnMessage = "";
						acrop.setCropType((cell[i][j][0].getStringCellValue()));
						acrop.setUnit(cell[i][j][1].getStringCellValue());

						acrop.setUnitsProduced(getCellDouble(cell[i][j][2]));
						acrop.setUnitsSold(getCellDouble(cell[i][j][3]));
						acrop.setPricePerUnit(getCellDouble(cell[i][j][6]));
						// acrop.setUnitsConsumed(getCellDouble(cell[i][j][5]));
						acrop.setUnitsOtherUse(getCellDouble(cell[i][j][4]));

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

						// em("in crop = " + cell[i][j][1].getStringCellValue());
						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// em("done acrop get = " + rst.getResourcetypename());

							acrop.setResourceSubType(rst);
							isCorrectRST = true;
							acrop.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(acrop.getUnit(), rst))
								acrop.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							acrop.setStatus(efd.model.Asset.Status.Invalid);
						}
						
						// Was a Local Unit entered for this RST LZ combination?
						em("about to do crops localunit");
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
						em("crop localunit =" + localUnit.getName());
						if (localUnit != null) {
							

							if (acrop.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								
								acrop.setUnit(localUnit.getName());
								acrop.setLocalUnit(localUnit.getName());
								acrop.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									acrop.setStatus(efd.model.Asset.Status.Valid);
								}
							}
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
						em("in ls sales cell = i =" + i);
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

						// em("rtype = " + rtype[i].getIdresourcetype().toString());

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// em("done alss get = " + rst.getResourcetypename());

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
						alsp.setPricePerUnit(getCellDouble(cell[i][j][7]));
						// alsp.setUnitsConsumed(getCellDouble(cell[i][j][6]));
						alsp.setUnitsOtherUse(getCellDouble(cell[i][j][5]));

						alsp.setMarket1(cell[i][j][8].getStringCellValue());
						alsp.setPercentTradeMarket1(getCellDouble(cell[i][j][9]));

						alsp.setMarket2(cell[i][j][10].getStringCellValue());
						alsp.setPercentTradeMarket2(getCellDouble(cell[i][j][11]));

						alsp.setMarket3(cell[i][j][12].getStringCellValue());
						alsp.setPercentTradeMarket3(getCellDouble(cell[i][j][13]));

						em("about to do LSP checksubtype " + cell[i][j][0].getStringCellValue());

						if ((rst = checkSubType(alsp.getLivestockType()+" "+alsp.getLivestockProduct(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							em("done alsp get = " + rst.getResourcetypename());

							alsp.setResourceSubType(rst);
							alsp.setStatus(efd.model.Asset.Status.Valid);
							isCorrectRST = true;
							if (!checkSubTypeEntered(alsp.getUnit(), rst))
								alsp.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							alsp.setStatus(efd.model.Asset.Status.Invalid);
						}
						
						// Was a Local Unit entered for this RST LZ combination?
						em("about to do LSP localunit");
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
						em("back from getLocalunit");
						
						if (localUnit != null) {

								if (alsp.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
									
									alsp.setUnit(localUnit.getName());
									alsp.setLocalUnit(localUnit.getName());
									alsp.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
									if (isCorrectRST) { // Is the correct RST entered and valid
										alsp.setStatus(efd.model.Asset.Status.Valid);
									}
								}
							}
				
						em("skipped lu");
						
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
						em("in emp  cell = i =" + i);
						aemp.setEmploymentName((cell[i][j][l++].getStringCellValue()));
						
						aemp.setPeopleCount(getCellDouble(cell[i][j][l++]));
						aemp.setUnit((cell[i][j][l++].getStringCellValue()));
						em("emp = 1");
						aemp.setUnitsWorked(getCellDouble(cell[i][j][l++]));
						em("emp = 2");
						aemp.setCashPaymentAmount(getCellDouble(cell[i][j][l++]));
						em("emp = 3");
						aemp.setFoodPaymentFoodType((cell[i][j][l++].getStringCellValue()));
						em("emp = 4");
						aemp.setFoodPaymentUnit((cell[i][j][l++].getStringCellValue()));
						em("emp = 5");
						aemp.setFoodPaymentUnitsPaidWork((cell[i][j][l++].getStringCellValue()));
						
						em(" empppp = 1");
						aemp.setWorkLocation1(cell[i][j][l++].getStringCellValue());
						em("1");
						aemp.setPercentWorkLocation1(getCellDouble(cell[i][j][l++]));
						em("2");
						aemp.setWorkLocation2(cell[i][j][l++].getStringCellValue());
						
						em("3");
						aemp.setPercentWorkLocation2(getCellDouble(cell[i][j][l++]));
						em("4");
						aemp.setWorkLocation3(cell[i][j][l++].getStringCellValue());
						em("5");
						aemp.setPercentWorkLocation3(getCellDouble(cell[i][j][l++]));
						em("6");
						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							// em("done emp get = " + rst.getResourcetypename());
							em("in emp  status is valid 1");
							aemp.setResourceSubType(rst);

							aemp.setStatus(efd.model.Asset.Status.Valid);
							if (!checkSubTypeEntered(aemp.getUnit(), rst))
								aemp.setStatus(efd.model.Asset.Status.Invalid);
							em("in emp  status is invalid 1");
						} else {

							aemp.setStatus(efd.model.Asset.Status.Invalid);
						}
						em("7");
						// If there is a food payment then need to validate food payment rst

						if (!aemp.getFoodPaymentFoodType().isEmpty()) {
							/* check Food as payment entered is valid */
							if ((checkSubType(aemp.getFoodPaymentFoodType(), "Crop") != null) // then it is a valid
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Wild Foods") != null)
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Livestock Product") != null)
									|| (checkSubType(aemp.getFoodPaymentFoodType(), "Food Purchase") != null)) {
								aemp.setStatus(efd.model.Asset.Status.Valid);
								isCorrectRST = true;
							}

							// Now need to check food unit is valid

							if (!checkSubTypeEntered(aemp.getFoodPaymentUnit(), rst))
								aemp.setStatus(efd.model.Asset.Status.Invalid);
						}

						// Was a Local Unit entered for this FoodPayment RST LZ combination?
						
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, aemp.getFoodResourceSubType());
						
						if (localUnit != null) {
							
							if (aemp.getFoodPaymentUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								
								aemp.setFoodPaymentUnit(localUnit.getName());
								aemp.setLocalUnit(localUnit.getName());
								aemp.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									aemp.setStatus(efd.model.Asset.Status.Valid);
								}
							}
						}
						
						
						
						em("in emp  33");
						hhi.getEmployment().add(aemp);
						// getView().refreshCollections();
						k = 100;
						break;

					}

					catch (Exception ex) {
						addMessage("Problem parsing Employee worksheet");
						em("Error in Emp = " + ex);
						k = 100;
						break breaksheet;
					}
				case TRANSFER:
					try {
						at = new Transfer();
						int l = 0;

						em("Official text = " + cell[i][j][l].getStringCellValue());

						if (cell[i][j][l++].getStringCellValue().equals("Official"))
							at.setIsOfficial(true);
						else
							at.setIsOfficial(false);

						// em("Official boolean set to = "+at.getIsOfficial());

						// Handle the enum

						em("Transfer 000 j l " + j + " " + l);

						at.setSource(cell[i][j][l++].getStringCellValue());

						String upperCaseTransferType = upperCaseFirst(cell[i][j][l].getStringCellValue());
						em("Transfer 001 j l " + j + " " + l);

						if (upperCaseTransferType.equals("Cash"))
							at.setTransferType(efd.model.Transfer.TransferType.Cash);
						else if (upperCaseTransferType.equals("Other"))
							at.setTransferType(efd.model.Transfer.TransferType.Other);
						if (upperCaseTransferType.equals("Food"))
							at.setTransferType(efd.model.Transfer.TransferType.Food);
						l++;
						em("Transfer 00 2 j l " + j + " " + l);

						at.setPeopleReceiving(getCellDouble(cell[i][j][l++]));

						at.setTimesReceived(getCellDouble(cell[i][j][l++]));
						at.setCashTransferAmount(getCellDouble(cell[i][j][l++]));
						at.setTransferFoodOtherType((cell[i][j][l++].getStringCellValue()));
						em("Transfer 2222 j l " + j + " " + l);
						at.setUnit(cell[i][j][l++].getStringCellValue());
						if (at.getUnit().isEmpty())
							at.setUnit("?"); // seem to have many examples where this is left blank..

						at.setUnitsTransferred(getCellDouble(cell[i][j][l++]));
						at.setUnitsSold(getCellDouble(cell[i][j][l++]));
						at.setPricePerUnit(getCellDouble(cell[i][j][l++]));
						em("Transfer 3333 j l " + j + " " + l);
						at.setOtherUse(getCellDouble(cell[i][j][l++]));
						// at.setUnitsConsumed(getCellDouble(cell[i][j][l++]));
						l++;
						at.setMarket1(cell[i][j][l++].getStringCellValue());
						at.setPercentTradeMarket1(getCellDouble(cell[i][j][l++]));
						em("Transfer 4444  j l " + j + " " + l);
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
							isCorrectRST = true;
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
						em("Transfer 111 j l " + j + " " + l);
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
						
						
						
						// Was a Local Unit entered for this RST LZ combination?
						
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
					
						if (localUnit != null) {
							
							if (at.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								
								at.setUnit(localUnit.getName());
								at.setLocalUnit(localUnit.getName());
								at.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									at.setStatus(efd.model.Asset.Status.Valid);
								}
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
						awf.setOtherUse(getCellDouble(cell[i][j][l++]));
						// awf.setUnitsConsumed(getCellDouble(cell[i][j][l++]));
						l++;
						awf.setPricePerUnit(getCellDouble(cell[i][j][l++]));

						awf.setMarket1(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket1(getCellDouble(cell[i][j][l++]));
						awf.setMarket2(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket2(getCellDouble(cell[i][j][l++]));
						awf.setMarket3(cell[i][j][l++].getStringCellValue());
						awf.setPercentTradeMarket3(getCellDouble(cell[i][j][l++]));

						if ((rst = checkSubType(cell[i][j][0].getStringCellValue(), // is this a valid resource type?
								rtype[i].getIdresourcetype().toString())) != null) {
							em("done wildfood get =  " + rst.getResourcetypename());

							awf.setResourceSubType(rst);
							awf.setStatus(efd.model.Asset.Status.Valid);
							isCorrectRST = true;
							if (!checkSubTypeEntered(awf.getUnit(), rst))
								awf.setStatus(efd.model.Asset.Status.Invalid);

						} else {
							awf.setStatus(efd.model.Asset.Status.Invalid);
						}
						
						// Was a Local Unit entered for this RST LZ combination?
					
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
				
						if (localUnit != null) {
							

							if (awf.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								em("local unit match, isCOrrectRST = " + isCorrectRST);
								awf.setUnit(localUnit.getName());
								awf.setLocalUnit(localUnit.getName());
								awf.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									awf.setStatus(efd.model.Asset.Status.Valid);
								}
							}
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
								em("tradeable  found ");
								assetOK1 = true;
								rst = rst1;
							} else {
								em("back in inputs rst not found ");
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
									isCorrectRST = true;
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

						// Was a Local Unit entered for this RST LZ combination?
						
						LocalUnit localUnit = ParseXLSFile2.getLocalUnit(livelihoodZone, rst);
					
						if (localUnit != null) {
						

							if (ins.getUnit().trim().equalsIgnoreCase(localUnit.getName().trim())) {
								
								ins.setUnit(localUnit.getName());
								ins.setLocalUnit(localUnit.getName());
								ins.setLocalUnitMultiplier(localUnit.getMultipleOfStandardMeasure());
								if (isCorrectRST) { // Is the correct RST entered and valid
									ins.setStatus(efd.model.Asset.Status.Valid);
								}
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
		em("done set resource");
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

		em("DRB RT = " + rst.getResourcetype().getResourcetypename());
		em("DRB RST = " + rst.getResourcetypename());
		em("in checkSubTypeEntered " + unitEntered + " " + rst.getResourcesubtypeunit());
		if (unitEntered.isEmpty() || unitEntered.equals(null)) {
			// empty thus invalid
			return false;

		}
		String unitRst = stripS(rst.getResourcesubtypeunit());

		unitEntered = stripS(unitEntered);

		if (unitRst.toLowerCase().equals(unitEntered.toLowerCase())) {

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

		em("var 1 = " + var1);
		em("var 2 = " + var2);
		em("var 3 = " + var3);

		
		try {

			em("in rst try 555");

			rsty = (ResourceSubType) XPersistence.getManager()
					.createQuery("from ResourceSubType where resourcetype = '" + resourceType + "'" + "and ("
							+ "upper(resourcetypename) = '" + var2 + "'" + " or " + "upper(resourcetypename) = '" + var3
							+ "'" + ") ")
					.setMaxResults(1).getSingleResult();

			em("rsty = " + rsty.getResourcetypename());

			return (rsty);
		} catch (Exception ex) {
			em("Exception:rst not found rsty = " + rsty + " " + ex);

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
	 * does // not exist catch (Exception ex) { em("return cash rsty =  " +
	 * rsty.getResourcetypename()); return rsty; }
	 * 
	 * // em("done rst get syn query in check sub type ");
	 * 
	 * // otherwise get the RST for the Synonym
	 * 
	 * ResourceSubType rstysyn = (ResourceSubType)
	 * XPersistence.getManager().find(ResourceSubType.class,
	 * rsty.getResourcesubtypesynonym().getIdresourcesubtype().toString()); return
	 * rstysyn; }
	 *
	 * catch (Exception ex) { em("Failed checkSubType " + ex); return null; // no
	 * record found to match data entered
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
			// em("in getDouble Number = "+acell.getNumericCellValue());
			return acell.getNumericCellValue();
		} else if (acell.getCellType() == 1) {
			// em("in getDouble String = ");
			return Double.valueOf(d);
		} else if (acell.getCellType() == Cell.CELL_TYPE_BLANK) {
			// em("in getDouble Empty = "+Double.valueOf(d));
			return Double.valueOf(d);
		}
		// em("returning from getcelldouble "+d);
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
			// em("check cell type = " + cname);
			cell = sheet.getRow(y).getCell(x, Row.CREATE_NULL_AS_BLANK);

			// em("77 cell type in check cell = " + cell.getCellType());
			/*
			 * em("Cell Type = "+cell.getCellType());
			 * em("Cell Type Blank = "+cell.CELL_TYPE_BLANK);
			 * em("Cell Type Bool = "+cell.CELL_TYPE_BOOLEAN);
			 * em("Cell Type Formula = "+cell.CELL_TYPE_FORMULA);
			 * em("Cell Type Numeric = "+cell.CELL_TYPE_NUMERIC);
			 * em("Cell Type String = "+cell.CELL_TYPE_STRING);
			 */

			if (cell.getCellType() == 0) /* is a valid cell with String or Number and test the fetch */
			{
				// em("in cell type 0 check for numeric ");
				try {
					// em("in try ");
					dtest = cell.getNumericCellValue();
					// em("done try ");
				} catch (Exception ex) {
					{
						if (!nullable) {
							addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
							// You can throw any runtime exception here
							em("caught in check cell");
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
				// em("in cell type 1 check for string null? " + nullable +
				// cell.getStringCellValue());
				try {
					stest = cell.getStringCellValue();
					if (stest.isEmpty() && nullable) {
						// em("in cell type 1 check for string return false");
						return false;
					}

				} catch (Exception ex) {
					if (nullable) /* empty is ok */
					{
						// em("nullable string return false");
						return false;
					}
					if (!nullable) {
						addError("Incomplete Spreadsheet data '" + cname + "' is empty ");
						// You can throw any runtime exception here
						// em("caught in check cell");
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

			// em("return true");
			localMessage(cname + " is correct");
			return true;

		}

		catch (Exception ex) {
			addError("Incomplete Spreadsheet data '" + cname + "' Error - correct spreadsheet and upload again");
			// You can throw any runtime exception here
			// em("caught in check cell");
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
