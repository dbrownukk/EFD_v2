package efd.actions;

/* Write XLS template for Community Interview */

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;
import org.openxava.tab.*;

import efd.model.*;

public class CreateXlsFileAction extends ViewBaseAction implements IForwardAction, JxlsConstants { // 1

	private String forwardURI = null;
    public void execute() throws Exception {
		try {
			JxlsWorkbook scenario = createScenario();
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3

			/* Now to reset */
			getView().setModelName("Community");
			getView().clear();

			// setControllers("Return");

		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

	private JxlsWorkbook createScenario() throws Exception {

		int i = 1;
		int j = 2;
		int w = 30;
		int row;
		int col;
		

		/* Get EFD Project Details */

		/* Get WealthGroup data */
		WealthGroup wealthgroup = XPersistence.getManager().find(WealthGroup.class, getView().getValue("wgid"));

		/* Get Community Data */
		Community community = XPersistence.getManager().find(Community.class,
				wealthgroup.getCommunity().getCommunityid());
Project project = XPersistence.getManager().find(Project.class, community.getProjectlz().getProjectid());
		/* Get Project Data */
		

		/* Get Characteristic Resources */
		//WGCharacteristicsResource wgharacteristicsresource =
		//XPersistence.getManager().find(WGCharacteristicsResource.class
		//,getView().getValue("wgid"));

		Map[] participantKeys;
		if (getView().getSubview("wgcharacteristicsresource").getCollectionTab().hasSelected()) {
			participantKeys = getView().getSubview("wgcharacteristicsresource").getCollectionTab().getSelectedKeys();
			} else {
			participantKeys = getView().getSubview("wgcharacteristicsresource").getCollectionTab().getAllKeys();
			}
		
		for (int id = 0; id < participantKeys.length; id++) { 
			
			WGCharacteristicsResource wgcharacteristicsresource = (WGCharacteristicsResource) MapFacade
					.findEntity("wgcharacteristicsresource", participantKeys[id]);
			System.out.println("in loop");
			if (wgcharacteristicsresource != null) {
				System.out.println(wgcharacteristicsresource.getWgresourceamount());
			}
		}
		
		
		
		
		//Map values = new HashMap();
		//Map[] rows = tab.getAllKeys();

		//for (int id = 0; id < rows.length; id++) {
			//Map key = rows[id];
			//WGCharacteristicsResource wgharacteristicsresource 
			//= (WGCharacteristicsResource) MapFacade.findEntity("WGCharacteristicsResource", key);
			// User ToUser = RequestTo.getTouser();
			// Bundle RequestType = RequestTo.getBundle();
			// Timestamp StartDate = RequestTo.getTime();
			// Timestamp EndDate = RequestTo.getDueby();

		//}

		/* XLS File Name */
		JxlsWorkbook scenarioWB = new JxlsWorkbook(project.getProjecttitle());
		JxlsStyle boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		JxlsStyle boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT);
		JxlsStyle borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN,
				BORDER_THIN, BORDER_THIN);
		JxlsStyle textstyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setCellColor(LIGHT_GREEN).setTextColor(BLACK);
		JxlsStyle datestyle = scenarioWB
				.getDefaultDateStyle(); /*
										 * seems to e a bug to stop setting
										 * other params
										 */

		/* XLS Sheets */

		JxlsSheet Interview = scenarioWB.addSheet("Interview Details");
		JxlsSheet Asset = scenarioWB.addSheet("Assets");
		JxlsSheet Crop = scenarioWB.addSheet("Crops");
		JxlsSheet LS = scenarioWB.addSheet("LS");
		JxlsSheet Emp = scenarioWB.addSheet("EMP");
		JxlsSheet Transfer = scenarioWB.addSheet("Transfers");
		JxlsSheet Wildfood = scenarioWB.addSheet("WILD FOODS");
		JxlsSheet Foodpurchase = scenarioWB.addSheet("FOOD PURCHASE");
		JxlsSheet Nonfoodpurchase = scenarioWB.addSheet("NON FOOD PURCHASE");

		/* Interview Sheet */

		while (i < 7) {
			Interview.setColumnWidths(i, w); /* set col widths */
			i++;
		}
		while (j < 11) {
			Interview.setValue(3, j, "",
					borderStyle); /* set borders for data input fields */
			Interview.setValue(5, j, "", borderStyle);
			j += 2;
		}
		Interview.setValue(7, 8, "", borderStyle);
		Interview.setValue(7, 10, "", borderStyle);

		Interview.setValue(1, 1, "Project Date: " + project.getPdate());
		Interview.setValue(2, 2, "Interview Number", boldRStyle);
		Interview.setValue(2, 4, "District", boldRStyle);
		Interview.setValue(2, 6, "Livelihood Zone", boldRStyle);
		Interview.setValue(2, 8, "Number of Particpants", boldRStyle);
		Interview.setValue(2, 10, "Wealth Group", boldRStyle);

		Interview.setValue(4, 2, "Date", boldRStyle);
		Interview.setValue(4, 4, "Village / Sub District", boldRStyle);
		Interview.setValue(4, 6, "Interviewers", boldRStyle);
		Interview.setValue(4, 8, "Men", boldRStyle);
		Interview.setValue(4, 10, "Number of People in Household", boldRStyle);

		Interview.setValue(6, 8, "Women", boldRStyle);
		Interview.setValue(6, 10, "Type of Year", boldRStyle);

		/* Data */

		Interview.setValue(3, 2, community.getCinterviewsequence(),
				textstyle); /* Interview Number */
		Interview.setValue(3, 4, community.getSite().getLocationdistrict(), textstyle); /* District */
		Interview.setValue(3, 6, community.getSite().getLivelihoodZone().getLzname(),
				textstyle); /* Livelihood Zone */
		Interview.setValue(3, 8, community.getCivparticipants(),
				textstyle); /* Number of Participants */
		Interview.setValue(3, 10, wealthgroup.getWgnameeng(),
				textstyle); /* Wealth Group */

		Interview.setValue(5, 2, community.getCinterviewdate(), datestyle); /* Date */
		Interview.setValue(5, 4, community.getSite().getSubdistrict(),
				textstyle); /* Sub District */
		Interview.setValue(5, 6, community.getInterviewers(), textstyle); /* Interviewers */
		Interview.setValue(5, 8, community.getCivm(), textstyle); /* Men */
		Interview.setValue(5, 10, wealthgroup.getWghhsize(),
				textstyle); /* Number in Household */

		Interview.setValue(7, 8, community.getCivf(), textstyle); /* Women */
		Interview.setValue(7, 10, "NEED TYPE OF YEAR FIELD ",
				textstyle); /* Type of Year */

		/* Asset Sheet */

		Asset.setValue(2, 3, "Livestock Type", boldTopStyle);
		Asset.setValue(3, 3, "Unit", boldTopStyle);
		Asset.setValue(4, 3, "Number Owned", boldTopStyle);
		Asset.setValue(5, 3, "Price Per Unit", boldTopStyle);
		Asset.setValue(2, 16, "Land Type", boldTopStyle);
		Asset.setValue(3, 16, "Unit", boldTopStyle);
		Asset.setValue(4, 16, "Area", boldTopStyle);

		i = 1;
		while (i < 7) {
			Asset.setColumnWidths(i, w); /* set col widths */
			i++;
		}

		/* set grid for data input */

		col = 2;
		row = 4;
		while (col < 6) {
			while (row < 28) {
				if (row == 15) {
					row = 17;
				}

				Asset.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
				if (col == 5 && row > 16) {
					row = 30;
				}

			}
			col++;
			row = 4;
		}

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
				Crop.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}

			col++;
			row = 5;
		}

		/* LS Sheet */

		LS.setValue(2, 4, "Income Type i.e Milk", boldTopStyle);
		LS.setValue(3, 4, "", boldTopStyle);
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

		/* set grid for data input */

		col = 2;
		row = 5;
		while (col < 15) {
			while (row < 16) {
				LS.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}

			col++;
			row = 5;
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
				Emp.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
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
				Transfer.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
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
				Wildfood.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
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
				Foodpurchase.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
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
				Nonfoodpurchase.setValue(col, row, "",
						borderStyle); /* set borders for data input fields */
				row++;
			}
			col++;
			row = 4;
		}
		/* end XLS setup */

		return scenarioWB;

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
