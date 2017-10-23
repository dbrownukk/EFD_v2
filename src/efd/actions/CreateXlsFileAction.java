package efd.actions;

/* Write XLS template for Community Interview */ 

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;

public class CreateXlsFileAction extends ViewBaseAction implements IForwardAction, JxlsConstants { // 1

	private String forwardURI = null;

	public void execute() throws Exception {
		try {
			JxlsWorkbook scenario = createScenario();
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

	private JxlsWorkbook createScenario() throws Exception {

		int i = 1;
		int j = 2;
		int w = 40;

		/* Get EFD Project Deatils */
		Project project = XPersistence.getManager().find(Project.class, getView().getValue("projectid"));
		// addMessage("Project details"+ project.getProjecttitle());

		/* XLS File Name */
		JxlsWorkbook scenarioWB = new JxlsWorkbook(project.getProjecttitle());
		JxlsStyle boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		JxlsStyle borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THICK, BORDER_THICK,
				BORDER_THICK, BORDER_THICK);

		/* XLS Sheets */

		JxlsSheet Interview = scenarioWB.addSheet("Interview Details");
		scenarioWB.addSheet("Assets");
		scenarioWB.addSheet("Crops");
		scenarioWB.addSheet("LS");
		scenarioWB.addSheet("EMP");
		scenarioWB.addSheet("Transfers");
		scenarioWB.addSheet("WILD FOODS");
		scenarioWB.addSheet("FOOD PURCHASE");
		scenarioWB.addSheet("NON FOOD PURCHASE");

		/* Interview Sheet */

		while (i < 7) {
			Interview.setColumnWidths(i, w); /* set col widths */
			i++;
		}
		while (j < 11) {
			Interview.setValue(3, j, "", borderStyle); /* set borders for data input fields */
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
