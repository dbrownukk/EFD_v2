package efd.actions;

import java.io.*;
import java.math.*;
import java.sql.*;

/* Read XLS Community Interview  spreadsheet */

import java.util.Collection;

import javax.persistence.*;

import java.text.SimpleDateFormat;

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

public class ParseXLSFile extends CollectionBaseAction implements IForwardAction, JxlsConstants, IFilePersistor {

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

			/* Interview Details */

			cellNumber = 2;
			rowNumber = 1;
			// System.out.println("to loop ");

			sheet = wb.getSheetAt(0);
			cellNumber = 2;
			rowNumber = 1;
			int n = 0;

			System.out.println("get interview 111");

			/* Interview Number */
			nullable = false;
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
				wgi.setWgInterviewDate(formatter1.parse(sdate));
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
			ps.close();

			/* delete previous assets for this wgi */
			/* delete previous assets */

			wgi.getAssetLiveStock().removeAll(wgi.getAssetLiveStock());
			wgi.getAssetLand().removeAll(wgi.getAssetLand());
			wgi.getAssetFoodStock().removeAll(wgi.getAssetFoodStock());
			wgi.getAssetTradeable().removeAll(wgi.getAssetTradeable());
			wgi.getCrop().removeAll(wgi.getCrop());
			wgi.getEmployment().removeAll(wgi.getEmployment());
			//wgi.getLiveStockUse().removeAll(wgi.getLiveStockUse());
			wgi.getTransfer().removeAll(wgi.getTransfer());
			wgi.getWildFood().removeAll(wgi.getWildFood());

			System.out.println("done delete ");

			sheet = wb.getSheetAt(1);
			String var1 = null, var2 = null, var3 = null, var4 = null;
			Double d1 = null, d2 = null, d3 = null, d4 = null;
			Boolean getNextRow = false;
			String market1, market2, market3;
			Double percent1, percent2, percent3;

			/*************************************************************************************************/
			/* Now process the ASSETS - Livestock and Land sheet */

			/* Get Asset id for Livestock Asset */
			ResourceType livestock = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Livestock'").getSingleResult();
			System.out.println("done asset query ");

			sheet = wb.getSheet("Assets");

			AssetLiveStock als;

			for (rowNumber = 3; rowNumber < 50; rowNumber++) {
				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				if (StringUtil.isBlank(var1))
					break;
				System.out.println("done asset 111 " + var1);

				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();

				System.out.println("done asset 112 " + var2);

				cell = sheet.getRow(rowNumber).getCell(3);
				if (checkCell("Livestock Asset Number Owned", sheet, 3, rowNumber, false)) {
					if (cell.getCellType() == 0) {
						System.out.println("done asset 112_1 ");
						d1 = cell.getNumericCellValue();
						System.out.println("done asset 112_2 ");
					} else if (cell.getCellType() == 1) {
						System.out.println("done asset 112_3 " + cell.getStringCellValue());
						d1 = Double.parseDouble(cell.getStringCellValue());
						System.out.println("done asset 112_4 ");
					}
				} else
					return;
				System.out.println("done asset 113 ");
				if (checkCell("Livestock Asset Price Per Unit", sheet, 4, rowNumber, true))
					d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();
				System.out.println("done asset 114 ");
				System.out.println("Assets = " + var1 + var2);

				als = new AssetLiveStock();

				System.out.println("Assets = 33 ");

				als.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Assets = 44 ");
				als.setLiveStockTypeEnteredName(var1);
				System.out.println("Assets = 45 ");
			//	als.setUnitEntered(var2);

				if (d1 != null)
			//		als.setNumberOwned(d1.intValue());
				System.out.println("Assets = 46 ");
				if (d2 != null)
					als.setPricePerUnit(BigDecimal.valueOf(d2));

				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, livestock.getIdresourcetype().toString())) != null) {
					System.out.println("done als get =  "+rst.getResourcetypename());
					
					als.setResourceSubType(rst);
					als.setStatus(efd.model.Asset.Status.Valid);
				}

				System.out.println("Assets = 47 ");

				wgi.getAssetLiveStock().add(als);
				System.out.println("Assets - done assetlivestock elementcollection add ");
			}
			localMessage("done Stock Asset");
			/*************************************************************************************************/			 
			/* Get Asset id for LAND Asset */
			
			
			ResourceType land = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Land'").getSingleResult();
			System.out.println("done land asset query ");
			AssetLand al;
			System.out.println("Land = 41 size = " + sheet.getLastRowNum());

			for (rowNumber = 14; rowNumber < 100; rowNumber++) {
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;

				try {
					var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				} catch (Exception ex) {
					continue;
				}

				System.out.println("LAnd loop = " + var1);
				if (var1.isEmpty() && var1 != "Land Type" && !getNextRow) {

					continue;
				} else if (!getNextRow) { /* get the next row after Land Type header */
					getNextRow = true;

					continue;
				} else if (getNextRow && var1.isEmpty()) /* end of list */
					break;

				System.out.println("Land = 42 ");
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue(); // Unit
				System.out.println("Land = 43 row = " + rowNumber);

				if (checkCell("Land Asset Area ", sheet, 3, rowNumber, false))
					d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue(); // Area
				else
					return;

				System.out.println("Land = " + var1 + var2);

				al = new AssetLand();

				// System.out.println("Land = 33 ");

				al.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Land = 44 ");
				al.setLandTypeEnteredName(var1);
				System.out.println("Land = 45 ");
				//al.setUnitEntered(var2);

				if (d1 != null)
			//		al.setLandArea(d1.intValue());
				System.out.println("Land = 46 ");

				System.out.println("asset resource type = " + land.getResourcetypename());
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, land.getIdresourcetype().toString())) != null) {
					System.out.println("done ls get =  "+rst.getResourcetypename());
					
					al.setResourceSubType(rst);
					al.setStatus(efd.model.Asset.Status.Valid);
				}
				
				
				
				
			//	al.setResourceType(land);

				wgi.getAssetLand().add(al);

			}
			localMessage("done Land Asset");
			/*************************************************************************************************/
			/* Get Asset id for FOODSTOCK Asset */

			
			sheet = wb.getSheet("FOOD PURCHASE");

			ResourceType foodstock = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Food Stocks'").getSingleResult();

			AssetFoodStock afs;
			for (rowNumber = 3; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
				
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;
				
				
				System.out.println("Food loop = row = " + rowNumber);
				System.out.println("Food loop = rows = " + sheet.getLastRowNum());
				System.out.println("Food loop type = " + sheet.getRow(rowNumber).getCell(1).getCellType());
				var1 = sheet.getRow(rowNumber).getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				System.out.println("Food loop = " + var1);
				if (StringUtil.isBlank(var1)) {
					break;
				}

				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue();
				d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();

				// System.out.println("Land = " + var1 + var2);

				afs = new AssetFoodStock();

				// System.out.println("Food = 33 ");

				afs.setStatus(efd.model.Asset.Status.NotChecked);
				System.out.println("Food = 44 ");
			//	afs.setFoodName(var1);
				/*******************************************************/
				/* No place to hold Unit/Quantity/ppu in FOOD PURCHASE */
				/*******************************************************/
				/* asking DAI */

				// afs .setLandArea(d1.intValue());
				System.out.println("Food = 441 ");
			//	afs.setResourceType(foodstock);
				System.out.println("Food = 442 ");
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, foodstock.getIdresourcetype().toString())) != null) {
					System.out.println("done afs get =  "+rst.getResourcetypename());
					
					afs.setResourceSubType(rst);
					afs.setStatus(efd.model.Asset.Status.Valid);
				}
				
				
				
				
				wgi.getAssetFoodStock().add(afs);
				System.out.println("Food = 443 ");
			}
			localMessage("Done Food Stock");
			/*************************************************************************************************/
			/* Get Asset id for CROPS Asset - */

			sheet = wb.getSheet("Crops");
			System.out.println("Crop sql todo ");
			ResourceType crops = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Crops - primarily for Sale'").getSingleResult();    // NOTE - Need RST to match spreadsheet tabs
			System.out.println("Crop sql done ");
			Crop ac;
			System.out.println("Crop sheet size loop = " + sheet.getLastRowNum());
			for (rowNumber = 4; rowNumber < 100; rowNumber++) {

				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;
				
				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("Crop = 3291 " + var1);
				System.out.println("Crop loop = " + var1);
				if (StringUtils.isBlank(var1)) {
					break;
				}
				System.out.println("Crop = 3292 ");
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				System.out.println("Crop = 3293 ");

				cell = sheet.getRow(rowNumber).getCell(3); // d1 QP
				if (cell.getCellType() == 0 && !cell.equals(null)) { /* Numeric */
					System.out.println("in Numeric and not null");
					d1 = cell.getNumericCellValue();

				} else if (cell.getCellType() == 1 && cell.getStringCellValue().isEmpty()) {
					continue;
				} else {
					System.out.println("Crop = 32999 vsal = ");
					d1 = Double.parseDouble(cell.getStringCellValue());
					System.out.println("Crop = 32999_1  ");
				}

				System.out.println("Crop = 3294 ");
				d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue(); // Sold
				System.out.println("Crop = 3295 " + d2);

				cell = sheet.getRow(rowNumber).getCell(5); // ppu String in Number or blank ?
				System.out.println("cell type = " + cell.getCellType());

				if (cell.getCellType() == 0 && !cell.equals(null)) { /* Numeric */
					System.out.println("in Numeric and not null");
					d3 = cell.getNumericCellValue();

				} else if (cell.getCellType() == 1 && cell.getStringCellValue().isEmpty()) {
					System.out.println("Crop = 32977 vsal = ");
					continue;
				} else if (cell.getCellType() == 1 && cell.getStringCellValue() != "") {
					System.out.println("Crop = 32966 vsal = ");
					System.out.println("Crop = String val = " + cell.getStringCellValue());

					// d3 = cell.getNumericCellValue();
					System.out.println("Crop = 32966_1  ");
					d3 = 0.0;
				}
				else if (cell.getCellType() == 3 )    // blank
					d3 = 0.0;

				System.out.println("Crop = 3296 ");
				System.out.println("Crop = 330 " + sheet.getRow(rowNumber).getCell(6).getCellType());
				cell = sheet.getRow(rowNumber).getCell(6); // Other - number or string

				if (cell.getCellType() == 1)
					var3 = cell.getStringCellValue(); // Other Use - SHOULD THIS BE A NUMBER?
				else if (cell.getCellType() == 0) {
					d4 = cell.getNumericCellValue();
					var3 = Double.toString(d4);
				}

				// System.out.println("Crop = 331 ");
				// market1 = sheet.getRow(rowNumber).getCell(7).getStringCellValue();
				// System.out.println("Crop = 332 ");
				// market2 = sheet.getRow(rowNumber).getCell(9).getStringCellValue();
				// System.out.println("Crop = 333 ");
				// market3 = sheet.getRow(rowNumber).getCell(11).getStringCellValue();

				// percent1 = sheet.getRow(rowNumber).getCell(8).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(10).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(12).getNumericCellValue();

				// System.out.println("Crop = " + var1 + var2);

				ac = new Crop();

				System.out.println("Crop = 33 ");

				ac.setStatus(efd.model.Asset.Status.NotChecked);
			//	ac.setCropName(var1);
				System.out.println("Crop = 33c ");
			//	ac.setLocalUnit(var2);
				System.out.println("Crop = 33b ");
			//	ac.setQuantityProduced(d1.intValue());

				System.out.println("Crop = 33a ");

			//	ac.setQuantitySold(d2.intValue());

				System.out.println("Crop = 3356__9 ");
				//if (d3.isNaN())
				//	System.out.println("Crop d3 nan = 3357 ");

				// String aa = Double.toString(d3);
				System.out.println("Crop = 33k ");

				ac.setPricePerUnit(BigDecimal.valueOf(d3));

				System.out.println("Crop = 3356 ");
		//		ac.setOtherUse(var3);
				System.out.println("Crop = 3357 ");
				/****************************/
				/* Where is the market data */
				/****************************/

				// ac.setResourceType(crops);
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, crops.getIdresourcetype().toString())) != null) {
					System.out.println("done crops get =  "+rst.getResourcetypename());
					
					ac.setResourceSubType(rst);
					ac.setStatus(efd.model.Asset.Status.Valid);
				}

				wgi.getCrop().add(ac);
			}
			localMessage("Done Crops");
			/*************************************************************************************************/
			/* Get Assets for Transfers */
			
			sheet = wb.getSheet("TRANSFERS");

			ResourceType transfers = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Transfers'").getSingleResult();

			Transfer tf;
			for (rowNumber = 3; rowNumber < 100; rowNumber++) {
				
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;

				/* Heavy handed - but cell may contain garbage that cannot be grabbed */
				try {
					System.out.println("TF = 39_2 ");
					cell = sheet.getRow(rowNumber).getCell(1);
					System.out.println("TF = 39_3 " + cell.getCellType());
				} catch (Exception ex) {
					break;
				}

				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("TF = 40 ");
				System.out.println("TF loop = " + var1);

				if (StringUtil.isBlank(var1)) {
					System.out.println("TF No records");
					break;
				}
				System.out.println("TF 10");
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue(); // Q received
				d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();
				d3 = sheet.getRow(rowNumber).getCell(5).getNumericCellValue();
				System.out.println("TF 20 = " + d1 + d2 + d3);

				cell = sheet.getRow(rowNumber).getCell(6); // Other - number or string
				System.out.println("TF Var 3 TYPE is  = "+cell.getCellType());
				if (cell.getCellType() == 1)
				{
					var3 = cell.getStringCellValue(); // Other Use - SHOULD THIS BE A NUMBER?
					System.out.println("TF Var 3 is a string = " +var3);
				}
					else if (cell.getCellType() == 0) {
					d4 = cell.getNumericCellValue();
					var3 = Double.toString(d4);
					System.out.println("TF Var 3 is a number = " +var3);
				}

				// var3 = sheet.getRow(rowNumber).getCell(6).getStringCellValue(); // Other Use
				// - SHOULD THIS BE A NUMBER?

				// market1 = sheet.getRow(rowNumber).getCell(7).getStringCellValue();

				// market2 = sheet.getRow(rowNumber).getCell(9).getStringCellValue();

				// market3 = sheet.getRow(rowNumber).getCell(11).getStringCellValue();
				System.out.println("TF 30");
				// percent1 = sheet.getRow(rowNumber).getCell(8).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(10).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(12).getNumericCellValue();
				// System.out.println("TF 40");
				// System.out.println("TRANS = " + var1 + var2);

				tf = new Transfer();

				System.out.println("TF = 33 ");

				tf.setStatus(efd.model.Asset.Status.NotChecked);
			//	tf.setTransferredResourceName(var1);
				//tf.setLocalUnit(var2);
				System.out.println("TF = 34 ");
			//	tf.setQuantityReceived(d1.intValue());
			//	tf.setQuantitySold(d2.intValue());
		//		System.out.println("TF = 35 ");
				tf.setPricePerUnit(BigDecimal.valueOf(d3));
				System.out.println("TF = 36 ");
				tf.setOtherUse(var3);

				/****************************/
				/* Where is the market data */
				/****************************/

			//	tf.setResourceType(transfers);
				System.out.println("TF = 37 ");
				
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, transfers.getIdresourcetype().toString())) != null) {
					System.out.println("done als get =  "+rst.getResourcetypename());
					
					tf.setResourceSubType(rst);
					tf.setStatus(efd.model.Asset.Status.Valid);
				}
				
				

				wgi.getTransfer().add(tf);
				System.out.println("TF = 38 ");
			}
			localMessage("Done Transfers");
			/*************************************************************************************************/
			/* Get Asset id for Livestock Product (was LS) Asset - */

			
			System.out.println("in ls");

			sheet = wb.getSheetAt(3);

			System.out.println("done LS sheet 2");

			ResourceType lsp = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Livestock Products'").getSingleResult();
			System.out.println("Done LS Products Query");
			LiveStockUse lsu;
			for (rowNumber = 3; rowNumber < 100; rowNumber++) {
				
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;
				
				System.out.println("LS 5");

				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();

				System.out.println("LS loop = " + var1);
				if (StringUtil.isBlank(var1)) {
					break;
				}
				System.out.println("LS 10");
				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue();
				var3 = sheet.getRow(rowNumber).getCell(3).getStringCellValue(); // Unit
				System.out.println("LS 20");
				if (checkCell("Livestock Products Quantity Produced", sheet, 4, rowNumber, false))
					d1 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue();
				else
					return;
				System.out.println("LS 21");
				if (checkCell("Livestock Products Quantity Sold ", sheet, 5, rowNumber, true))
					d2 = sheet.getRow(rowNumber).getCell(5).getNumericCellValue();
				System.out.println("LS 22");
				if (checkCell("Livestock Products Price Per Unit", sheet, 6, rowNumber, true))
					d3 = sheet.getRow(rowNumber).getCell(6).getNumericCellValue();
				// System.out.println("LS = 330 ");
				System.out.println("LS 30");

				cell = sheet.getRow(rowNumber).getCell(7); // Other - number or string

				if (cell.getCellType() == 1)
					var4 = cell.getStringCellValue(); // Other Use - SHOULD THIS BE A NUMBER?
				else if (cell.getCellType() == 0) {
					d4 = cell.getNumericCellValue();
					var4 = Double.toString(d4);
				}

				// var4 = sheet.getRow(rowNumber).getCell(7).getStringCellValue(); // Other

				// System.out.println("LS = 331 ");
				/* No database location for market data yet */
				// market1 = sheet.getRow(rowNumber).getCell(8).getStringCellValue();
				// System.out.println("LS = 332 ");
				// market2 = sheet.getRow(rowNumber).getCell(10).getStringCellValue();
				// System.out.println("LS = 333 ");
				// market3 = sheet.getRow(rowNumber).getCell(12).getStringCellValue();
				System.out.println("LS 40");
				// percent1 = sheet.getRow(rowNumber).getCell(9).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(11).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(13).getNumericCellValue();

				System.out.println("LS = " + var1 + var2);

				lsu = new LiveStockUse();

				// System.out.println("LS = 33 ");

				lsu.setStatus(efd.model.Asset.Status.NotChecked);
				//lsu.setLsIncomeType2(var1);
				lsu.setLsName(var1);
				lsu.setLocalUnit(var3);
				System.out.println("LS 45");
				if (d1 == null)
					d1 = 0.0;
				System.out.println("LS 46");
				lsu.setQuantityProduced(d1.intValue());
				System.out.println("LS 47");
				if (d2 == null)
					d2 = 0.0;
				lsu.setQuantitySold(d2.intValue());
				System.out.println("LS 48");
				if (d3 == null)
					d3 = 0.0;
				lsu.setPricePerUnit(BigDecimal.valueOf(d3));

				System.out.println("LS 49");
				lsu.setOtherUse(var4);
				System.out.println("LS 50");

				/****************************/
				/* Where is the market data */
				/****************************/

				// lsu.setResourceType(lsp);
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, lsp.getIdresourcetype().toString())) != null) {
					System.out.println("done als get =  "+rst.getResourcetypename());
					
					lsu.setResourceSubType(rst);
					lsu.setStatus(efd.model.Asset.Status.Valid);
				}
				
				
				

	//			wgi.getLiveStockUse().add(lsu);
			}
			localMessage("Done LS Use");
			/*************************************************************************************************/
			/* Get Asset id for WildFood Asset - */

			sheet = wb.getSheet("WILD FOODS"); /* WIll need to change to Livestock Products */

			ResourceType wft = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Wild foods'").getSingleResult();

			WildFood wf;
			for (rowNumber = 3; rowNumber < 100; rowNumber++) {
				
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;
				

				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("WF loop = " + var1);
				if (StringUtils.isBlank(var1)) {
					break;
				}

				var2 = sheet.getRow(rowNumber).getCell(2).getStringCellValue(); // Unit

				d1 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue(); // QP
				d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue(); // QS
				d3 = sheet.getRow(rowNumber).getCell(5).getNumericCellValue(); // PPU
				System.out.println("WF = 330 ");

				cell = sheet.getRow(rowNumber).getCell(7); // Other - number or string

				if (cell.getCellType() == 1)
					var4 = cell.getStringCellValue(); // Other Use - SHOULD THIS BE A NUMBER?
				else if (cell.getCellType() == 0) {
					d4 = cell.getNumericCellValue();
					var4 = Double.toString(d4);
				}

				// var4 = sheet.getRow(rowNumber).getCell(6).getStringCellValue(); // Other

				// System.out.println("WF = 331 ");
				// market1 = sheet.getRow(rowNumber).getCell(7).getStringCellValue();
				// System.out.println("WF = 332 ");
				// market2 = sheet.getRow(rowNumber).getCell(9).getStringCellValue();
				// System.out.println("WF = 333 ");
				// market3 = sheet.getRow(rowNumber).getCell(11).getStringCellValue();

				// percent1 = sheet.getRow(rowNumber).getCell(8).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(10).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(12).getNumericCellValue();

				System.out.println("WF = " + var1 + var2);

				wf = new WildFood();

				// System.out.println("WF = 33 ");

				wf.setStatus(efd.model.Asset.Status.NotChecked);
				wf.setWildFoodName(var1);
	//			wf.setLocalUnit(var2);
	//			wf.setQuantityProduced(d1.intValue());
	//			wf.setQuantitySold(d2.intValue());
				wf.setPricePerUnit(BigDecimal.valueOf(d3));
				wf.setOtherUse(var4);

				/****************************/
				/* Where is the market data */
				/****************************/

				// wf.setResourceType(wft);
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, wft.getIdresourcetype().toString())) != null) {
					System.out.println("done als get =  "+rst.getResourcetypename());
					
					wf.setResourceSubType(rst);
					wf.setStatus(efd.model.Asset.Status.Valid);
				}
				
				

				wgi.getWildFood().add(wf);
			}
			localMessage("Done Wild Food");
			/*************************************************************************************************/
			/* Get Asset EMPLOYMENT - */

			
			sheet = wb.getSheet("EMP");

			ResourceType empt = (ResourceType) XPersistence.getManager()
					.createQuery("from ResourceType where ResourceTypeName = 'Employment'").getSingleResult();

			Employment emp;
			for (rowNumber = 3; rowNumber < 100; rowNumber++) {
				
				/* Reset vars */
				var1 = ""; var2 = ""; var3 = ""; var4 = "";
				d1 = 0.0; d2 = 0.0; d3 = 0.0; d4 = 0.0;

				var1 = sheet.getRow(rowNumber).getCell(1).getStringCellValue();
				System.out.println("EMP loop = " + var1);
				if (StringUtil.isBlank(var1)) {
					break;
				}
				System.out.println("EMP 80-1");

				if (checkCell("Employment - Number of People Working ", sheet, 2, rowNumber, false))
					d1 = sheet.getRow(rowNumber).getCell(2).getNumericCellValue(); // Number of people
				else
					return;

				System.out.println("EMP 80-2");
				if (checkCell("Employment - Frequency ", sheet, 3, rowNumber, true))
					d4 = sheet.getRow(rowNumber).getCell(3).getNumericCellValue(); // Frequency
				System.out.println("EMP 80");
				if (checkCell("Employment - Duration ", sheet, 4, rowNumber, true))
					d2 = sheet.getRow(rowNumber).getCell(4).getNumericCellValue(); // Duration
				System.out.println("EMP 81");
				var3 = sheet.getRow(rowNumber).getCell(5).getStringCellValue(); // Pay

				System.out.println("EMP 82");
				var4 = sheet.getRow(rowNumber).getCell(6).getStringCellValue(); 
				
				if (checkCell("Employement - Pay Cash ", sheet, 7, rowNumber, true))
					d3 = sheet.getRow(rowNumber).getCell(7).getNumericCellValue(); // Pay Cash
				System.out.println("EMP 83");
				// market1 = sheet.getRow(rowNumber).getCell(8).getStringCellValue();
				// market2 = sheet.getRow(rowNumber).getCell(10).getStringCellValue();
				// market3 = sheet.getRow(rowNumber).getCell(12).getStringCellValue();

				// percent1 = sheet.getRow(rowNumber).getCell(9).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(11).getNumericCellValue();
				// percent1 = sheet.getRow(rowNumber).getCell(13).getNumericCellValue();

				System.out.println("EMP = " + var1 + var2);

				emp = new Employment();

				System.out.println("EMP = 33 ");
				emp.setStatus(efd.model.Asset.Status.NotChecked);
				emp.setEmploymentName(var1);
				emp.setPeopleCount(d1.intValue());
		//		emp.setFrequency(Double.toString(d4));
		///		emp.setDuration(d2.intValue());
				// emp.setFoodPaymentPerUnit(var3);

				/* No Food payment type and Pay Food should be Varchar */

		//		emp.setCashPaymentPerUnit(BigDecimal.valueOf(d3));

				/****************************/
				/* Where is the work location data */
				/****************************/

				// emp.setResourceType(empt);
				
				/* Set to Resource Sub Type.. */

				if ((rst = checkSubType(var1, empt.getIdresourcetype().toString())) != null) {
					System.out.println("done als get =  "+rst.getResourcetypename());
					
					emp.setResourceSubType(rst);
					emp.setStatus(efd.model.Asset.Status.Valid);
				}
				

				wgi.getEmployment().add(emp);
			}
			localMessage("Done Employment ");
			System.out.println("Assets = 46 222");
			XPersistence.getManager().persist(wgi);
			System.out.println("Assets = 47 222");
			
			getView().refresh();
			
			System.out.println("Assets = 48 222 ");
			
			addMessage("Successful Parse");
			return;
		}

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
