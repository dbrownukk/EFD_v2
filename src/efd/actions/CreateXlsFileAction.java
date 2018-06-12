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
	JxlsStyle textstyle = null;
	JxlsStyle datestyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	int width = 30;
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
		int rows;
		int landrow = 0;
		int col;
		Map key;
		List wgl;
		String rt;
		String resub;
		String rtunit;
		ArrayList<Rtype> rtypes = new ArrayList<Rtype>();
		String resourcesubtypeid;
		String resourcetypeid;
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

		System.out.println("post query of wgi  " + wgi.toString());
		System.out.println("post query of wgi empty?? " + wgi.isEmpty());

		// List wgi = querywgi.getResultList();
		System.out.println(" in Template, size = " + wgi.size());

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

		// Community community = XPersistence.getManager().find(Community.class,
		// wealthgroup.getCommunity().getCommunityid());
		// System.out.println("In careetscenario 222");
		Project project = XPersistence.getManager().find(Project.class, community.getProjectlz().getProjectid());
		// System.out.println("In careetscenario 221");
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
		boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT);
		borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textstyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setCellColor(WHITE).setTextColor(BLACK);
		datestyle = scenarioWB.getDefaultDateStyle();
		numberStyle = scenarioWB.addStyle(INTEGER).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		f1Style = scenarioWB.addStyle("0.0");

		// System.out.println("done Jxl 1");
		/* XLS Sheets */

		JxlsSheet interview = scenarioWB.addSheet("Interview Details");
		JxlsSheet assetLand = scenarioWB.addSheet("Assets - Land");
		JxlsSheet assetLivestock = scenarioWB.addSheet("Assets - Livestock");

		JxlsSheet assetOtherTradeable = scenarioWB.addSheet("Assets - Other Tradeable");
		JxlsSheet assetFoodStocks = scenarioWB.addSheet("Assets - Food Stocks");

		JxlsSheet assetTrees = scenarioWB.addSheet("Assets - Trees");
		JxlsSheet assetCash = scenarioWB.addSheet("Assets - Cash");

		JxlsSheet livestockSales = scenarioWB.addSheet("Livestock - Sales");
		JxlsSheet livestockProducts = scenarioWB.addSheet("Livestock - Products");
		JxlsSheet Crop = scenarioWB.addSheet("Crops");
		JxlsSheet Wildfood = scenarioWB.addSheet("Wild Foods");
		JxlsSheet Emp = scenarioWB.addSheet("Employment");
		JxlsSheet Transfer = scenarioWB.addSheet("Transfers");

		JxlsSheet purchaseFood = scenarioWB.addSheet("Purchase - Food");
		JxlsSheet purchaseNonFood = scenarioWB.addSheet("Purchase - NonFood");

		// REMOVE THE FOLLOWING WHEN DONE....
		JxlsSheet LS = scenarioWB.addSheet("Livestock Products");
		JxlsSheet Foodpurchase = scenarioWB.addSheet("FOOD PURCHASE");
		JxlsSheet Nonfoodpurchase = scenarioWB.addSheet("NON FOOD PURCHASE");

		interview.setColumnWidths(1, w, w, w, w, w); /* set col widths */

		// System.out.println("done Jxl 2");
		while (j < 11) {
			interview.setValue(3, j, "", borderStyle); /* set borders for data input fields */
			interview.setValue(5, j, "", borderStyle);
			j += 2;
		}
		interview.setValue(7, 8, "", borderStyle);
		interview.setValue(7, 10, "", borderStyle);

		interview.setValue(1, 1, "Project Date: " + project.getPdate());
		interview.setValue(2, 2, "Interview Number", boldRStyle);
		interview.setValue(2, 4, "District", boldRStyle);
		interview.setValue(2, 6, "Livelihood Zone", boldRStyle);
		interview.setValue(2, 8, "Number of Particpants", boldRStyle);
		interview.setValue(2, 10, "Wealth Group", boldRStyle);

		interview.setValue(4, 2, "Date", boldRStyle);
		interview.setValue(4, 4, "Village / Sub District", boldRStyle);
		interview.setValue(4, 6, "Interviewers", boldRStyle);
		interview.setValue(4, 8, "Men", boldRStyle);
		interview.setValue(4, 10, "Number of People in Household", boldRStyle);

		interview.setValue(6, 8, "Women", boldRStyle);
		interview.setValue(6, 10, "Type of Year", boldRStyle);

		/* Data */

		interview.setValue(3, 4, community.getSite().getLocationdistrict(), borderStyle); /* District */
		interview.setValue(3, 6, community.getSite().getLivelihoodZone().getLzname(),
				borderStyle); /* Livelihood Zone */

		interview.setValue(3, 10, wealthgroup.getWgnameeng(), borderStyle); /* Wealth Group */

		interview.setValue(5, 4, community.getSite().getSubdistrict(), borderStyle); /* Sub District */

		
	
		printAssetLand(assetLand);
		printAssetLivestock(assetLivestock);
		printAssetOtherTradeable(assetOtherTradeable);
		printAssetFoodStocks(assetFoodStocks);
		
		
		
		
		/* Crop Sheet */

		Crop.setValue(8, 3, "For Crops which are sold", boldTopStyle);
		Crop.setValue(2, 4, "Crop Type", boldTopStyle);
		Crop.setValue(3, 4, "Unit", boldTopStyle);
		Crop.setValue(4, 4, "Quantity Produced", boldTopStyle);
		Crop.setValue(5, 4, "Quantity Sold", boldTopStyle);
		Crop.setValue(6, 4, "Price per Unit", boldTopStyle);
		Crop.setValue(7, 4, "Other Use", boldTopStyle);
		Crop.setValue(8, 4, "Market 1", boldTopStyle);
		Crop.setValue(9, 4, "% Trade at 1", boldTopStyle);
		Crop.setValue(10, 4, "Market 2", boldTopStyle);
		Crop.setValue(11, 4, "% Trade at 2", boldTopStyle);
		Crop.setValue(12, 4, "Market 3", boldTopStyle);
		Crop.setValue(13, 4, "% Trade at 3", boldTopStyle);

		i = 2;
		while (i < 14) {
			Crop.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 5;
		while (col < 14) {
			while (row < 16) {
				Crop.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}

			col++;
			row = 5;
		}

		row = 5;
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

			if (rt.contains("Crops")) {
				Crop.setValue(2, row, resub, borderStyle);
				Crop.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

		/* LS Sheet */

		LS.setValue(2, 4, "Income Type i.e Milk", boldTopStyle);

		LS.setValue(3, 4, "Livestock Type", boldTopStyle);
		LS.setValue(4, 4, "Unit", boldTopStyle);
		LS.setValue(5, 4, "Quantity Produced", boldTopStyle);
		LS.setValue(6, 4, "Quantity Sold", boldTopStyle);
		LS.setValue(7, 4, "Price per Unit", boldTopStyle);
		LS.setValue(8, 4, "Other Use", boldTopStyle);
		LS.setValue(9, 4, "Market 1", boldTopStyle);
		LS.setValue(10, 4, "% Trade at 1", boldTopStyle);
		LS.setValue(11, 4, "Market 2", boldTopStyle);
		LS.setValue(12, 4, "% Trade at 2", boldTopStyle);
		LS.setValue(13, 4, "Market 3", boldTopStyle);
		LS.setValue(14, 4, "% Trade at 3", boldTopStyle);

		i = 2;
		while (i < 15) {
			LS.setColumnWidths(i, w); /* set col widths */
			i++;
		}
		col = 2;
		row = 5;
		while (col < 14) {
			while (row < 16) {
				LS.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 5;
		}

		/* get data */
		/* this needs pulling into hash table */

		row = 5;
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

			/* Not Livestock Asset - this is Use of Livestock */

			if (rt.equals("Livestock Products")) {
				LS.setValue(2, row, resub, borderStyle);
				LS.setValue(4, row, rtunit, borderStyle);
				row++;
			}

		}

		/* EMP Sheet */

		Emp.setValue(2, 3, "Employment Type", boldTopStyle);
		Emp.setValue(3, 3, "Number of People Working", boldTopStyle);
		Emp.setValue(4, 3, "Frequency e.g. per week, Month", boldTopStyle);
		Emp.setValue(5, 3, "Pay Food kg", boldTopStyle);
		Emp.setValue(6, 3, "Food Type", boldTopStyle);
		Emp.setValue(7, 3, "Pay Cash", boldTopStyle);
		Emp.setValue(8, 3, "Work Location 1", boldTopStyle);
		Emp.setValue(9, 3, "% Work at 1", boldTopStyle);
		Emp.setValue(10, 3, "Work Location 2", boldTopStyle);
		Emp.setValue(11, 3, "% Work at 2", boldTopStyle);
		Emp.setValue(12, 3, "Work Location 3", boldTopStyle);
		Emp.setValue(13, 3, "% Trade at 3", boldTopStyle);

		i = 2;
		while (i < 14) {
			Emp.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 14) {
			while (row < 16) {
				Emp.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
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

			if (rt.equals("Employment")) {
				Emp.setValue(2, row, resub, borderStyle);
				// LS.setValue(4, row,
				// wgrloop.getResourcesubtype().getResourcesubtypeunit(),
				// boldTopStyle);
				row++;
			}

		}

		/* Transfer Sheet */

		Transfer.setValue(2, 3, "Transfer Type", boldTopStyle);
		Transfer.setValue(3, 3, "Unit", boldTopStyle);
		Transfer.setValue(4, 3, "Quantity Received", boldTopStyle);
		Transfer.setValue(5, 3, "Quantity Sold", boldTopStyle);
		Transfer.setValue(6, 3, "Price per Unit", boldTopStyle);
		Transfer.setValue(7, 3, "Other Use", boldTopStyle);
		Transfer.setValue(8, 3, "Market 1", boldTopStyle);
		Transfer.setValue(9, 3, "% Trade at 1", boldTopStyle);
		Transfer.setValue(10, 3, "Market 2", boldTopStyle);
		Transfer.setValue(11, 3, "% Trade at 2", boldTopStyle);
		Transfer.setValue(12, 3, "Market 3", boldTopStyle);
		Transfer.setValue(13, 3, "% Trade at 3", boldTopStyle);

		i = 2;
		while (i < 14) {
			Transfer.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 14) {
			while (row < 16) {
				Transfer.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
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

			if (rt.equals("Transfer")) {
				Transfer.setValue(2, row, resub, borderStyle);
				Transfer.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

		/* Wildfood Sheet */

		Wildfood.setValue(2, 3, "Wild Food Type", boldTopStyle);
		Wildfood.setValue(3, 3, "Unit", boldTopStyle);
		Wildfood.setValue(4, 3, "Quantity Received", boldTopStyle);
		Wildfood.setValue(5, 3, "Quantity Sold", boldTopStyle);
		Wildfood.setValue(6, 3, "Price per Unit", boldTopStyle);
		Wildfood.setValue(7, 3, "Other Use", boldTopStyle);
		Wildfood.setValue(8, 3, "Market 1", boldTopStyle);
		Wildfood.setValue(9, 3, "% Trade at 1", boldTopStyle);
		Wildfood.setValue(10, 3, "Market 2", boldTopStyle);
		Wildfood.setValue(11, 3, "% Trade at 2", boldTopStyle);
		Wildfood.setValue(12, 3, "Market 3", boldTopStyle);
		Wildfood.setValue(13, 3, "% Trade at 3", boldTopStyle);

		i = 2;
		while (i < 14) {
			Wildfood.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 14) {
			while (row < 16) {
				Wildfood.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
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

			if (rt.equals("Wild food")) {
				Wildfood.setValue(2, row, resub, borderStyle);
				Wildfood.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

		/* Food Purchase Sheet */

		Foodpurchase.setValue(2, 3, "Food Type", boldTopStyle);
		Foodpurchase.setValue(3, 3, "Unit", boldTopStyle);
		Foodpurchase.setValue(4, 3, "Quantity Purchased", boldTopStyle);
		Foodpurchase.setValue(5, 3, "Price Per Unit", boldTopStyle);

		i = 2;
		while (i < 6) {
			Foodpurchase.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 6) {
			while (row < 16) {
				Foodpurchase.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}

		row = 4;
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

			if (rt.equals("Food Purchase")) {
				Foodpurchase.setValue(2, row, resub, borderStyle);
				Foodpurchase.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}

		/* Non Food Purchase Sheet */

		Nonfoodpurchase.setValue(2, 3, "Item Purchased", boldTopStyle);
		Nonfoodpurchase.setValue(3, 3, "Unit", boldTopStyle);
		Nonfoodpurchase.setValue(4, 3, "Quantity Purchased", boldTopStyle);
		Nonfoodpurchase.setValue(5, 3, "Price Per Unit", boldTopStyle);

		i = 2;
		while (i < 6) {
			Nonfoodpurchase.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 6) {
			while (row < 16) {
				Nonfoodpurchase.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}
		row = 4;
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

			if (rt.equals("Non Food Purchase")) {
				Nonfoodpurchase.setValue(2, row, resub, borderStyle);
				Nonfoodpurchase.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}
	
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

		/* Asset Land Type */

		addLOV(sheet, landSheet, 3, numRows - 1, 1, 1, "Land");
		addLOV(sheet, landSheet, 3, numRows - 1, 2, 2, "Area");
		addNumberValidation(workbook, sheet, landSheet, 3, numRows, 3, 3);

		/* Assets - Livestock Type */

		addLOV(sheet, lsSheet, 3, numRows -1, 1, 1, "Livestock");
		addNumberValidation(workbook, sheet, lsSheet, 3, numRows, 3, 3); // Number Owned
		addNumberValidation(workbook, sheet, lsSheet, 3, numRows, 4, 4); // Price per unit

		/* Assets - Other Tradeable Type */

		addLOV(sheet,otSheet, 3, numRows -1 , 1, 1, "OtherTradeableGoods");
		addNumberValidation(workbook, sheet, otSheet, 3, numRows, 3, 3); // Number Owned
		addNumberValidation(workbook, sheet, otSheet, 3, numRows, 4, 4); // Price per unit

		/* Assets - Food Stocks */
		addLOV(sheet, fsSheet, 3, numRows - 1, 1, 1, "FoodStocks");
		addNumberValidation(workbook, sheet, fsSheet, 3, numRows, 3, 3);
		
		
		
		/* Return the spreadsheet */

		return scenarioWB;

		/* end XLS setup */

	}
	/**************************************************************************************************************************************************/
	/* Validations																																	  */
	/**************************************************************************************************************************************************/

	private void addNumberValidation(HSSFWorkbook workbook, Sheet vsheet, Sheet iSheet, int firstRow, int lastRow,
			int firstCol, int lastCol) {
		
		CellRangeAddressList addressList = null;

		addressList = new CellRangeAddressList(firstRow, numRows, firstCol, lastCol);
		DataValidationHelper dvHelper = vsheet.getDataValidationHelper();

		DataValidationConstraint dvConstraint = DVConstraint.createNumericConstraint(
				DVConstraint.ValidationType.DECIMAL, DVConstraint.OperatorType.BETWEEN, "0", "10000");

		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
		validation.createErrorBox("", "Number between 0 and 10000 only");
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
		System.out.println("formating region " + region.getNumberOfCells());
		System.out.println("formating region 2 " + firstRow + " " + lastRow);

		for (int i = firstRow; i < lastRow - 1; i++) {

			System.out.println("formating region 2 " + region.getFirstRow() + " " + region.getLastRow());
			Row row = iSheet.getRow(i);
			System.out.println("format cell = " + i);
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
			System.out.println("LOV = "+rType);
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

		/* Land Types */
		List<ResourceSubType> rst = XPersistence.getManager().createQuery("from ResourceSubType").getResultList();

		Row laRow = dataSheet.createRow(landRowNum);
		Row lsRow = dataSheet.createRow(lsRowNum);
		Row areaRow = dataSheet.createRow(areaRowNum);
		Row otRow = dataSheet.createRow(otRowNum);
		Row fsRow = dataSheet.createRow(fsRowNum);

		int j = 0;
		int la = 0;
		int ls = 0;
		int ot =0;
		int fs = 0;
		int area = 0;

		for (int k = 0; k < rst.size(); k++) {
			/* Land */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Land".toString())) {
				cell = laRow.createCell(la);
				cell.setCellValue(rst.get(k).getResourcetypename());
				la++;
				System.out.println("LA cell set to " + la + " " + cell.getStringCellValue());
			}
			/* LiveStock */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Livestock".toString())) {
				cell = lsRow.createCell(ls);
				cell.setCellValue(rst.get(k).getResourcetypename());
				ls++;
				System.out.println("LS cell set to " + ls + " " + cell.getStringCellValue());
			}
			/* Other Tradeable  */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Other Tradeable Goods".toString())) {
				cell = otRow.createCell(ot);
				cell.setCellValue(rst.get(k).getResourcetypename());
				ot++;
				System.out.println("OT cell set to " + ot + " " + cell.getStringCellValue());
			}
			/* Asset Food Stocks   */
			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals("Food Stocks".toString())) {
				cell = fsRow.createCell(fs);
				cell.setCellValue(rst.get(k).getResourcetypename());
				fs++;
				System.out.println("FS cell set to " + fs + " " + cell.getStringCellValue());
			}
		}

		String lacol = getCharForNumber(la); // Convert for drop list creation
		String lscol = getCharForNumber(ls); // Convert for drop list creation
		String otcol = getCharForNumber(ot); // Convert for drop list creation
		String fscol = getCharForNumber(fs); // Convert for drop list creation

		/* Land */
		name = dataSheet.getWorkbook().createName();
		landRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + landRowNum + ":$" + lacol + "$" + landRowNum); // Need to allow
																										// for longer
																										// lists..
		name.setNameName("Land");

		/* LS */
		name = dataSheet.getWorkbook().createName();
		lsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + lsRowNum + ":$" + lscol + "$" + lsRowNum); // Need to allow for
																									// longer lists..
		name.setNameName("Livestock");

		/* Other Tradeable */
		name = dataSheet.getWorkbook().createName();
		otRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + otRowNum + ":$" + otcol + "$" + otRowNum); // Need to allow for
																									// longer lists..
		name.setNameName("OtherTradeableGoods");
		

		/* Asset Food Stocks  */
		name = dataSheet.getWorkbook().createName();
		fsRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + fsRowNum + ":$" + fscol + "$" + fsRowNum); // Need to allow for
																									// longer lists..
		name.setNameName("FoodStocks");
		
		
		/* Area */

		List<ReferenceCode> referenceCode = XPersistence.getManager()
				.createQuery("from ReferenceCode where ReferenceType = 'Area')").getResultList();
		// Get ref codes for Area and add to Validations sheet
		for (int k = 0; k < referenceCode.size(); k++) {
			cell = areaRow.createCell(area);
			cell.setCellValue(referenceCode.get(k).getReferenceName());
			area++;
			System.out.println("LA cell set to " + area + " " + cell.getStringCellValue());
		}

		String areacol = getCharForNumber(area); // Convert for drop list creation
		name = dataSheet.getWorkbook().createName();
		areaRowNum++;
		name.setRefersToFormula("Validations" + "!$A$" + areaRowNum + ":$" + areacol + "$" + areaRowNum);
		name.setNameName("Area");

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
	private void printAssetLand(JxlsSheet sheet) {

		/* Asset Land Sheet */

		sheet.setValue(2, 3, "Land Type", boldTopStyle);
		sheet.setValue(3, 3, "Unit e.g. Hectare", boldTopStyle);
		sheet.setValue(4, 3, "Number of Units", boldTopStyle);

		/* set grid for data input */

		int col = 2;
		int row = 4;
		sheet.setColumnWidths(2, width, width, width); /* set col widths */
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
		sheet.setValue(4, 3, "Owned at STart of Year", boldTopStyle);
		sheet.setValue(5, 3, "Price Per Unit", boldTopStyle);

		System.out.println("done asl create headings");
		sheet.setColumnWidths(2, width, width, width, width); /* set col widths */

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
		sheet.setColumnWidths(2, width, width, width, width); /* set col widths */

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

		System.out.println("asl set styles and size 33");

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

			if (rt.equals("OtherTradeableGoods")) {
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
		sheet.setColumnWidths(2, width, width, width); /* set col widths */
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