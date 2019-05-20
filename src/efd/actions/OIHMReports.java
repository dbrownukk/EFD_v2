
/* Run OIHM Reports2 */

package efd.actions;

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.model.*;

public class OIHMReports extends ViewBaseAction implements IForwardAction, JxlsConstants {

	static final int NUMBER_OF_REPORTS = 15;
	private Study study = null;
	private CustomReportSpec customReportSpec = null;;

	private String forwardURI = null;

	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;

	CellStyle style = null;
	CellStyle vstyle = null;
	CellStyle datestyle = null;
	CellStyle title = null;
	CellStyle cnumberStyle = null;

	public void execute() throws Exception {

		System.out.println("In Run OIHM Reports ");

		String specID = getView().getValueString("customReportSpec.id");

		Object studyId = getPreviousView().getValue("id");

		if (studyId == null) {
			addError("Save Study before running reports");
			closeDialog();
			return;
		}

		System.out.println("In Run OIHM Reports specid, studyid = " + specID + " " + studyId.toString());

		customReportSpec = XPersistence.getManager().find(CustomReportSpec.class, specID);

		study = XPersistence.getManager().find(Study.class, studyId);

		try {
			JxlsWorkbook report = createReport();
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
		} catch (Exception e) {
			addError(e.getMessage());
		}

		closeDialog();
		getView().findObject();
		getView().refreshCollections();
		getView().refresh();

		addMessage("Created OIHM Report for " + customReportSpec.getSpecName());

	}

	private JxlsWorkbook createReport() throws Exception {
		System.out.println("In Run OIHM Reports create xls ");

		String filename = customReportSpec.getSpecName() + Calendar.getInstance().getTime();

		JxlsWorkbook reportWB = new JxlsWorkbook(filename);
		setStyles(reportWB);

		createHeaderPage(reportWB);

		return reportWB;
	}

	private void createHeaderPage(JxlsWorkbook reportWB) {

		int i = 5;

		JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];

		sheet[0] = reportWB.addSheet("Custom Report Spec");
		setSheetStyle(sheet[0]);

		sheet[0].setValue(1, 1, "Date:", textStyle);
		sheet[0].setValue(2, 1, new Date());
		sheet[0].setValue(1, 2, "Spec Name:", textStyle);
		sheet[0].setValue(2, 2, customReportSpec.getSpecName(), textStyle);
		sheet[0].setValue(1, 3, "Study:", textStyle);
		sheet[0].setValue(2, 3, study.getStudyName() + " " + study.getReferenceYear(), textStyle);
		sheet[0].setValue(1, i, "Reports", textStyle);
		/* get list of reports and create tabbed bsheets for each */

		/* If no reports in custom report spec then use all reports */
		List<Report> reportList;
		if (customReportSpec.getReport().size() > 0)
			reportList = (List<Report>) customReportSpec.getReport();
		else
			reportList = XPersistence.getManager().createQuery("from Report").getResultList();

		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyle);
			sheet[i - 3] = reportWB.addSheet(report.getName());
			setSheetStyle(sheet[i - 3]);
			i++;
		}


	}

	private void setSheetStyle(JxlsSheet sheet) {
		sheet.setColumnWidths(1, 20, 60);

	}

	private void setStyles(JxlsWorkbook reportWB) {
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(RIGHT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT)
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setCellColor(BLUE);
		f1Style = reportWB.addStyle("0.0");
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