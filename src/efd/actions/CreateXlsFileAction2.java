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


public class CreateXlsFileAction2 extends CollectionBaseAction implements IForwardAction, JxlsConstants {

	private int row;
	private String forwardURI = null;
	JxlsSheet interview = null;
	JxlsSheet assetLand = null;
	JxlsSheet assetLivestock = null;
	
	JxlsWorkbook scenarioWB = null;
	
	//HSSFWorkbook workbook = null;   // used for drop downs as not available in jxls
	
	
	/*
	JxlsSheet Crop = scenarioWB.addSheet("Crops");
	JxlsSheet LS = scenarioWB.addSheet("Livestock Products");
	JxlsSheet Emp = scenarioWB.addSheet("EMP");
	JxlsSheet Transfer = scenarioWB.addSheet("Transfers");
	JxlsSheet Wildfood = scenarioWB.addSheet("WILD FOODS");
	JxlsSheet Foodpurchase = scenarioWB.addSheet("FOOD PURCHASE");
	JxlsSheet Nonfoodpurchase = scenarioWB.addSheet("NON FOOD PURCHASE");
	*/
	
	
	
	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textstyle = null;
	JxlsStyle datestyle = null;
	
	Sheet valSheet;
	
	int width = 30;
	List chrs = null;
	String rt;
	String resub;
	String rtunit;
	int numrows = 30;

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
		System.out.println("In careetscenario 221");
		Site site = XPersistence.getManager().find(Site.class, community.getSite().getLocationid());

		/******************
		 * Need to get charresource and types using getmanager and iterate
		 ********************/

		// List wgcharacteristicsresource =

		Query query = XPersistence.getManager()
				.createQuery("select idwgresource from WGCharacteristicsResource where wgid = '" + wgid + "'");
		chrs = query.getResultList();

		System.out.println("In careetscenario 223");

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
		System.out.println("In careetscenario 224");
		filename = community.getProjectlz().getProjecttitle() + '_' + site.getLivelihoodZone().getLzname() + '_'
				+ wealthgroup.getWgnameeng();

		scenarioWB = new JxlsWorkbook(filename);
		System.out.println("In careetscenario 225");
		
		

		System.out.println("In careetscenario 226");
		
		/* Styles */
		
		
		 boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		 boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT);
		 borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN,
				BORDER_THIN, BORDER_THIN);
		
		 textstyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setCellColor(WHITE).setTextColor(BLACK);
		 datestyle = scenarioWB.getDefaultDateStyle(); 
			System.out.println("created styles ");
		 
		 /*
																 * seems to e a bug to stop setting other params
																 */

		/* XLS Sheets */

		 interview = scenarioWB.addSheet("Interview Details");
		 assetLand = scenarioWB.addSheet("Assets - Land");
		 assetLivestock = scenarioWB.addSheet("Assets - Livestock");
		 System.out.println("created new sheets ");
		
		
		JxlsSheet Crop = scenarioWB.addSheet("Crops");
		JxlsSheet LS = scenarioWB.addSheet("Livestock Products");
		JxlsSheet Emp = scenarioWB.addSheet("EMP");
		JxlsSheet Transfer = scenarioWB.addSheet("Transfers");
		JxlsSheet Wildfood = scenarioWB.addSheet("WILD FOODS");
		JxlsSheet Foodpurchase = scenarioWB.addSheet("FOOD PURCHASE");
		JxlsSheet Nonfoodpurchase = scenarioWB.addSheet("NON FOOD PURCHASE");
		
		
		while (i < 7) {
			interview.setColumnWidths(i, w); /* set col widths */
			i++;
		}
		System.out.println("done Jxl 2");
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

	 /* Interview Number */
		interview.setValue(3, 4, community.getSite().getLocationdistrict(), borderStyle); /* District */
		interview.setValue(3, 6, community.getSite().getLivelihoodZone().getLzname(),
				borderStyle); /* Livelihood Zone */
		// Interview.setValue(3, 8, community.getCivparticipants(),textstyle);
		// /* Number
		// of Participants */
		interview.setValue(3, 10, wealthgroup.getWgnameeng(), borderStyle); /* Wealth Group */

		
		/**********************/
		/* Process each sheet */
		/**********************/
		
		/* Asset Land  Sheet */

		printAssetLand();
		System.out.println("done assetLand");
		
		/* Asset Livestock  Sheet */

		printAssetLivestock();
		
		System.out.println("done  assetLivestock");
		
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
		
		
		return scenarioWB;

		/* end XLS setup */

	}
	
	/*
	 * 
	 * Handle each Sheet
	 * 
	 * 
	 */
	
	private void printAssetLand()
	{
		
		/* Asset Land  Sheet */
	

		assetLand.setValue(2, 3, "Land Type", boldTopStyle);
		assetLand.setValue(3, 3, "Unit e.g. Hectare", boldTopStyle);
		assetLand.setValue(4, 3, "Number of Units", boldTopStyle);
	
		int i = 1;
		while (i < 5) {
			assetLand.setColumnWidths(i, width); /* set col widths */
			i++;
		}

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 5) {
			while (row < numrows) {
				assetLand.setValue(col, row, "", borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}
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
				assetLand.setValue(2, landrow, resub, borderStyle);
				assetLand.setValue(3, landrow, rtunit, borderStyle);
				landrow++;
			}

		}

		
		System.out.println("about to create validation sheet ");
		/* create hidden validation sheet for drop downs */
		
		
		HSSFWorkbook workbook = (HSSFWorkbook) scenarioWB.createPOIWorkbook();
		
		
		/* First time through create the validations sheet */
		valSheet =     workbook.createSheet("Validations");
		//valSheet =    (Sheet) scenarioWB.addSheet("Validations");
		System.out.println("done create validation");
		
		//buildDataSheet(valSheet,"Land",1);

		//Sheet AssetSheet = workbook.getSheetAt(1); // Assets LS Sheet
		//System.out.println("done create sheet "+AssetSheet.getSheetName());
		
		
		//CellRangeAddressList addressList=null;
		/*
		for(int l = 0;l< numrows;l++)
		{
		addressList = new CellRangeAddressList(3, l, 1, 1);
		DataValidationHelper dvHelper = valSheet.getDataValidationHelper();
		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Land");
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		validation.setEmptyCellAllowed(true);
		validation.setShowErrorBox(false);   // Allows for other values - combo style
		AssetSheet.addValidationData(validation);
		}
		*/
		
		
		
	}
	
	private void printAssetLivestock()
	{
		 
		/* Asset Livestock  Sheet */
	
		assetLivestock.setValue(2, 3, "Livestock Type", boldTopStyle);
		assetLivestock.setValue(3, 3, "Unit", boldTopStyle);
		assetLivestock.setValue(4, 3, "Owned at Start of", boldTopStyle);
		assetLivestock.setValue(5, 3, "Price Per Unit", boldTopStyle);
		
		System.out.println("done asl create headings");
		
		int i = 1;
		while (i < 6) {
			assetLivestock.setColumnWidths(i, width); /* set col widths */
			i++;
		}

		/* set grid for data input */

		int col = 2;
		int row = 4;
		while (col < 6) {
			while (row < numrows) {
				assetLivestock.setValue(col, row, "", borderStyle); /* set borders for data input fields */
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
				assetLivestock.setValue(2, row, resub, borderStyle);
				assetLivestock.setValue(3, row, rtunit, borderStyle);
				row++;
			}

		}
		
		System.out.println("asl have data ");
		
		//HSSFWorkbook workbook = (HSSFWorkbook) scenarioWB;
		//HSSFWorkbook workbook = (HSSFWorkbook) scenarioWB.createPOIWorkbook();
		// HSSFSheet hssfsheet = (HSSFSheet) scenarioWB.getSheet(1);

		System.out.println("about to create validation sheet ");
		/* create hidden validation sheet for drop downs */
		
		//valSheet = workbook.getSheet("Validations");
		
		
		//buildDataSheet(valSheet,"Livestock",10);
		System.out.println("done create validation valsheet = "+valSheet.getSheetName());
		
		//System.out.println("AssetSheet = "+workbook.getSheetName(2));
		
		//Sheet AssetSheet = workbook.getSheetAt(2); // Assets LS Sheet
		//System.out.println("done create sheet "+AssetSheet.getSheetName());
		
		
		//CellRangeAddressList addressList=null;
		
		//for(int l = 3;l< numrows;l++)
		//{
		//addressList = new CellRangeAddressList(1, l, 1, l);
		//DataValidationHelper dvHelper = valSheet.getDataValidationHelper();
		//DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Livestock");
		//DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		//validation.setEmptyCellAllowed(true);
		//validation.setShowErrorBox(false);   // Allows for other values - combo style
		//AssetSheet.addValidationData(validation);
		//}
		
		
	}
	
	
	

	private static void buildDataSheet(Sheet dataSheet, String rtype, int valrow) {
		Row row = null;
		Cell cell = null;
		Name name = null;

		List<ResourceSubType> rst = XPersistence.getManager().createQuery("from ResourceSubType").getResultList();

		row = dataSheet.createRow(valrow); // Livestock

		int j = 0;
		for (int k = 0; k < rst.size(); k++) {
			//System.out.println("subtype ="+rst.get(k).getResourcetypename());
			//System.out.println("type ="+rst.get(k).getResourcetype().getResourcetypename());

			if (rst.get(k).getResourcetype().getResourcetypename().toString().equals(rtype.toString())) {
				cell = row.createCell(j);
				cell.setCellValue(rst.get(k).getResourcetypename());
				j++;
				System.out.println("cell set to "+j+" "+cell.getStringCellValue());
			}

		}
		
		String col = getCharForNumber(j);   // Convert for drop list creation 
		
		System.out.println("done build list "+rtype);
		name = dataSheet.getWorkbook().createName();
		valrow++;
		name.setRefersToFormula("Validations" + "!$A$"+valrow+":$"+col+"$"+valrow); //Need to allow for longer lists.. 
		name.setNameName(rtype);
	}

	private void populateDataSheet(Sheet worksheet) {
		HSSFRow row = null;
		int rowIndex = 0;
		int lastColIndex = 0;
		int result = 0;
		String[] atype = { "Food", "Land" };

		// Firstly, add the atypes
		result = this.populateRow(worksheet.createRow(rowIndex++), "AType.", atype);
		if (result > lastColIndex) {
			lastColIndex = result;
		}

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
	
	public static String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 'A' - 1)) : null;
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