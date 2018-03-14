package efd.actions;

import java.io.*;
import java.math.*;
import java.sql.*;

/* Read XLS Community Interview  spreadsheet */

import java.util.Collection;
import java.text.SimpleDateFormat;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.editors.*;
import efd.model.*;
import efd.model.WealthGroupInterview.*;

import org.apache.poi.ss.usermodel.*;

public class ParseXLSFile extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {

	private int row;
	private String forwardURI = null;
	Blob spreadsheet = null;
	private AttachedFile spreadsfile = null;
	private Integer rowNumber = 0;
	private Integer cellNumber = 0;
	private Integer sheetNumber = 0;
	private Sheet sheet;
	private String[] cellArray;
	private int arrayCount = 0;
	private Cell cell;

	public void execute() throws Exception {

		System.out.println("in xls parse ");

		String wgiid = getView().getValueString("wgiid");

		WealthGroupInterview wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
		if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Parsed)) {
			addError("Cannot Parse Interview Spreadsheet - Already Parsed");
			return;
		}
		if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Validated)) {
			addError("Cannot Parse Interview Spreadsheet - Already Validated");
			return;
		}
		if (wgi.getSpreadsheet().isEmpty()) {
			addError("Upload completed Interview Spreadsheet before parsing");
			return;
		}
		
		/* Otherwise. delete assets from this wealthgroupinterview */
		

		System.out.println("in xls parse " + getView().getValueString("wgiid"));

		System.out.println("in xls 2        " + wgi.getSpreadsheet());

		String spreadsheetId = wgi.getSpreadsheet();

		Connection con = null;
		try {
			con = DataSourceConnectionProvider.getByComponent("WealthGroupInterview").getConnection();

			PreparedStatement ps = con
					.prepareStatement("select id,data from OXFILES where ID = '" + spreadsheetId + "'");
			System.out.println("prepped statment = " + ps.toString());
			System.out.println("prepped");
			ResultSet rs = ps.executeQuery();
			System.out.println("queried");

			System.out.println("In Blob col = " + rs.findColumn("ID"));

			rs.next();
			InputStream input = rs.getBinaryStream("DATA"); /* get stream data as well */
			System.out.println("done inputstream ");

			; /* Move to first row */

			String spreadsheetPkey = rs.getString(1);



			spreadsfile = find(spreadsheetPkey);
			System.out.println("done attache file get");

			Workbook wb = WorkbookFactory.create(input);

	
			/* Interview Details */

			
			cellNumber = 2;
			rowNumber = 1;
			System.out.println("to loop ");

			sheet = wb.getSheetAt(0);
			cellNumber = 2;
			rowNumber = 1;
			int n = 0;

			System.out.println("get interview ");

			/* Interview Number */

			Cell cell = sheet.getRow(1).getCell(2, Row.CREATE_NULL_AS_BLANK);

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
			cell = sheet.getRow(7).getCell(2, Row.CREATE_NULL_AS_BLANK);

			if (cell.getCellType() == 0) { /* Numeric */
				System.out.println("in Numeric ");
				Double intervieweesD = cell.getNumericCellValue();
				Integer interviewees = intervieweesD.intValue();
				wgi.setWgIntervieweesCount(interviewees);
			} else {
				String interviewees = cell.getStringCellValue();
				wgi.setWgIntervieweesCount(Integer.parseInt(interviewees));
			}

			System.out.println("get interview 444");

			/* Date */

			String sdate = sheet.getRow(1).getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			wgi.setWgInterviewDate(formatter1.parse(sdate));

			/* Interviewers */
			String interviewers = sheet.getRow(5).getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			wgi.setWgInterviewers(interviewers);
			System.out.println("get interview " + n++);

			/* Men */
			cell = sheet.getRow(7).getCell(4, Row.CREATE_NULL_AS_BLANK);
			System.out.println("men cell type " + cell.getCellType());

			if (cell.getCellType() == 0) { /* Numeric */
				System.out.println("in Numeric ");
				Double menD = cell.getNumericCellValue();
				Integer men = menD.intValue();
				wgi.setWgMaleIVees(men);
			} else {
				String men = cell.getStringCellValue();
				wgi.setWgMaleIVees(Integer.parseInt(men));
			}

			/* Women */
			cell = sheet.getRow(7).getCell(6, Row.CREATE_NULL_AS_BLANK);
			System.out.println("women cell type " + cell.getCellType());

			if (cell.getCellType() == 0) { /* Numeric */
				System.out.println("in Numeric ");
				Double womenD = cell.getNumericCellValue();
				Integer women = womenD.intValue();
				wgi.setWgFemaleIVees(women);
			} else {
				String women = cell.getStringCellValue();
				wgi.setWgFemaleIVees(Integer.parseInt(women));
			}

			/* Average Number in HH */
			System.out.println("HH ");
			cell = sheet.getRow(9).getCell(4, Row.CREATE_NULL_AS_BLANK);
			System.out.println("women cell type " + cell.getCellType());

			if (cell.getCellType() == 0) { /* Numeric */
				System.out.println("in Numeric ");
				Double numberOfPeopleInHHD = cell.getNumericCellValue();
				Integer numberOfPeopleInHH = numberOfPeopleInHHD.intValue();
				wgi.setWgAverageNumberInHH(numberOfPeopleInHH);
			} else {
				String numberOfPeopleInHH = cell.getStringCellValue();
				wgi.setWgAverageNumberInHH(Integer.parseInt(numberOfPeopleInHH));
			}

			/* Type of Year */
			System.out.println("Type Year ");

			String typeOfYear = sheet.getRow(9).getCell(6, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			wgi.setWgYearType(typeOfYear);

			wgi.setStatus(Status.Parsed);
			// getView().refresh();

			// XPersistence.getManager().persist(wgi);

			System.out.println("sheet = " + wb.getSheetName(1));
			System.out.println("sheet = " + wb.getSheetName(2));
			System.out.println("sheet = " + wb.getSheetName(3));
			System.out.println("sheet = " + wb.getSheetName(4));
			System.out.println("sheet = " + wb.getSheetName(5));
			System.out.println("sheet = " + wb.getSheetName(6));

			System.out.println("done wb setup ");
			ps.close();

			/* delete previous assets for this wgi */
			/* delete previous assets */
			// Collection delals = wgi.getAssetLiveStock();
			wgi.getAssetLiveStock().removeAll(wgi.getAssetLiveStock());
			wgi.getAssetLand().removeAll(wgi.getAssetLand());
			wgi.getAssetFoodStock().removeAll(wgi.getAssetFoodStock());
			wgi.getAssetTradeable().removeAll(wgi.getAssetTradeable());
			wgi.getCrop().removeAll(wgi.getCrop());
			wgi.getEmployment().removeAll(wgi.getEmployment());
			wgi.getLiveStockUse().removeAll(wgi.getLiveStockUse());
			wgi.getTransfer().removeAll(wgi.getTransfer());
			wgi.getWildFood().removeAll(wgi.getWildFood());
			
			
			
			sheet = wb.getSheetAt(1);
			String var1, var2, var3;
			Integer int1, int2, int3;
			Cell cell1, cell2, cell3;
			Double d1, d2, d3;
			Boolean getNextRow = false;

			/* Now process the ASSETS - Livestock and Land sheet */

			/* Get Asset id for Livestock Asset */
			ResourceType livestock = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Livestock'").getSingleResult();

		
			sheet = wb.getSheet("Assets");
			
			AssetLiveStock als = new AssetLiveStock();
			
			for (rowNumber = 3; rowNumber < 50; rowNumber++) {
				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				if (var1.isEmpty())
					break;
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue();
				d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();
				System.out.println("Assets =  " + var1 + var2);

				als = new AssetLiveStock();
				// AssetLiveStock als = new AssetLiveStock();
				System.out.println("Assets = 33 ");

				als.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Assets = 44 ");
				als.setLiveStockTypeEnteredName(var1);
				System.out.println("Assets = 45 ");
				als.setUnitEntered(var2);

				als.setNumberOwned(d1.intValue());
				als.setPricePerUnit(BigDecimal.valueOf(d2));
				als.setResourceType(livestock);

				wgi.getAssetLiveStock().add(als);
			}

			/* Get Asset id for LAND Asset */
			ResourceType land = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Land'").getSingleResult();

			AssetLand al = new AssetLand();
			for (rowNumber = 14; rowNumber < 100; rowNumber++) {
				
				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("LAnd loop =  "+var1);
				if (var1.isEmpty() && var1 != "Land Type" && !getNextRow) {
					continue;
				}
				else if (!getNextRow){      /* get the next row after Land Type header */
					getNextRow = true;
					continue;
				}
				else if (getNextRow && var1.isEmpty())   /* end of list */
					break;
				
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue();
				
				System.out.println("Land =  " + var1 + var2);

				al = new AssetLand();
				
				System.out.println("Land = 33 ");

				al.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Land = 44 ");
				al.setLandTypeEnteredName(var1);
				System.out.println("Land = 45 ");
				al.setUnitEntered(var2);

				al.setLandArea(d1.intValue());
		
			
				System.out.println("asset resource type = " + land.getResourcetypename());
				al.setResourceType(land);
				
				wgi.getAssetLand().add(al);
			}

			
			
			
			/* Get Asset id for FOODSTOCK  Asset - on same sheet as LiveStock */
			sheet = wb.getSheet("FOOD PURCHASE");
			
			ResourceType foodstock = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Food Stocks'").getSingleResult();

			AssetFoodStock afs = new AssetFoodStock();
			for (rowNumber = 3; rowNumber < 100; rowNumber++) {
				
				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("Food loop =  "+var1);
				if (var1.isEmpty() ) {
					break;
				}
			
				
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue();
				d1 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();
				
				System.out.println("Land =  " + var1 + var2);

				afs = new AssetFoodStock();
				
				System.out.println("Food = 33 ");

				afs.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Food = 44 ");
				afs.setFoodName(var1);
				/*******************************************************/
				/* No place to hold Unit/Quantity/ppu in FOOD PURCHASE */ 
				/*******************************************************/
				/* asking DAI */

				//afs   .setLandArea(d1.intValue());
		
				afs.setResourceType(foodstock);

				wgi.getAssetFoodStock().add(afs);
			}
			
			
			
			XPersistence.getManager().persist(wgi);
			System.out.println("Assets = 46 ");
			getView().refresh();

			return;
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

}
