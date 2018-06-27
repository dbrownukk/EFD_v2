package efd.actions;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;

import javax.persistence.*;

/* Read XLS Community Interview  spreadsheet */
import java.util.*;
import java.util.Date;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.editors.*;
import efd.model.*;
import efd.model.WealthGroupInterview.*;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.*;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.helper.*;

public class ParseXLSFile2 extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {

	Boolean messaging = false;

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
		Cell cell;
		Workbook wb;
		String sdate;
		String interviewers;
		String typeOfYear;
		String stest;
		Boolean nullable = false;
		Query query = null;
		ResourceSubType rst = null;

		// System.out.println("in xls parse ");

		String wgiid = getView().getValueString("wgiid");

		WealthGroupInterview wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
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

			; /* Move to first row */

			String spreadsheetPkey = rs.getString(1);

			spreadsfile = find(spreadsheetPkey);
			// System.out.println("done attache file get");

			wb = WorkbookFactory.create(input);

			getInterviewDetails(wb, wgi);

			ps.close();
			getView().refresh();
			addMessage("Spreadsheet Parsed");
		}
			
			
			/* delete previous assets for this wgi */
			/* delete previous assets */


		catch (Exception ex) {
			addError("Incomplete Spreadsheet data, correct spreadsheet and upload again", ex);
			// You can throw any runtime exception here
			throw new SystemException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
			}
		}

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

		Cell cell = null;
		Integer cellNumber = 2;
		Integer rowNumber = 1;
		String sdate = null;
		String interviewers = null;
		String typeOfYear = null;

		// System.out.println("to loop ");

		Sheet sheet = wb.getSheetAt(0);
		cellNumber = 2;
		rowNumber = 1;
		int n = 0;

		System.out.println("get interview 111");

		/* Interview Number */
		Boolean nullable = false;
		if (checkCell("Interview Number", sheet, 2, 1, nullable))
			cell = sheet.getRow(1).getCell(2, Row.CREATE_NULL_AS_BLANK);
		else
			return;

		System.out.println("cell type " + cell.getCellType());

		if (cell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric ");
			Double interviewNumberD = cell.getNumericCellValue();
			Integer interviewNumber = interviewNumberD.intValue();
			wgi.setWgInterviewNumber(interviewNumber);

		} else {
			String interviewNumber = cell.getStringCellValue();
			System.out.println("get interview 1 " + interviewNumber);
			wgi.setWgInterviewNumber(Integer.parseInt(interviewNumber));
		}

		System.out.println("get interview 333");

		/* Number of Participants */
		nullable = false;
		if (checkCell("Number of Participants", sheet, 2, 7, nullable))
			cell = sheet.getRow(7).getCell(2, Row.CREATE_NULL_AS_BLANK);
		else
			return;

		if (cell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric ");
			Double intervieweesD = cell.getNumericCellValue();
			Integer interviewees = intervieweesD.intValue();
			wgi.setWgIntervieweesCount(interviewees);
		} else {
			System.out.println("in String ");
			String interviewees = cell.getStringCellValue();
			System.out.println("done cell get ");
			wgi.setWgIntervieweesCount(Integer.parseInt(interviewees));
			System.out.println("wgi set throw in participants ");

		}

		// System.out.println("get interview 444");

		/* Date */
		nullable = true;
		if (checkCell("Interview Date", sheet, 4, 1, nullable))
			cell = sheet.getRow(1).getCell(4, Row.CREATE_NULL_AS_BLANK);
		else {
			addError("Incomplete Spreadsheet data - Interview Date error ");
			return;
		}

		if (cell.getCellType() == 0) { /* Numeric */
			System.out.println("in Numeric Date");
			Double iDateD = cell.getNumericCellValue();
			// Date iDate = (Date) DateUtil.getJavaDate(iDateD);

			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

			System.out.println("in Numeric Date " + iDateD);
			wgi.setWgInterviewDate(new java.sql.Date(iDateD.longValue()));

		} else {
			sdate = cell.getStringCellValue();
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
			cell = sheet.getRow(7).getCell(4, Row.CREATE_NULL_AS_BLANK);

			if (cell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double menD = cell.getNumericCellValue();
				Integer men = menD.intValue();
				wgi.setWgMaleIVees(men);
			} else {
				String men = cell.getStringCellValue();
				wgi.setWgMaleIVees(Integer.parseInt(men));
			}
		}
		/* Women */
		nullable = true;
		if (checkCell("Number of Women", sheet, 6, 7, nullable)) {
			cell = sheet.getRow(7).getCell(6, Row.CREATE_NULL_AS_BLANK);

			// System.out.println("women cell type " + cell.getCellType());

			if (cell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double womenD = cell.getNumericCellValue();
				Integer women = womenD.intValue();
				wgi.setWgFemaleIVees(women);
			} else {
				String women = cell.getStringCellValue();
				wgi.setWgFemaleIVees(Integer.parseInt(women));
			}
		}
		/* Average Number in HH */
		nullable = true;
		if (checkCell("Average Number in Household", sheet, 4, 9, nullable)) {
			cell = sheet.getRow(9).getCell(4, Row.CREATE_NULL_AS_BLANK);

			if (cell.getCellType() == 0) { /* Numeric */
				// System.out.println("in Numeric ");
				Double numberOfPeopleInHHD = cell.getNumericCellValue();
				Integer numberOfPeopleInHH = numberOfPeopleInHHD.intValue();
				wgi.setWgAverageNumberInHH(numberOfPeopleInHH);
			} else {
				String numberOfPeopleInHH = cell.getStringCellValue();
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
