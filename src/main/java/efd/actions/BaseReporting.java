/**
   @author david 7 Feb 2021
	
 */
package efd.actions;

import org.apache.poi.ss.usermodel.CellStyle;

/* Common base routines for IDAPS Reporting */

import org.openxava.actions.TabBaseAction;
import org.openxava.util.jxls.JxlsConstants;
import org.openxava.util.jxls.JxlsSheet;
import org.openxava.util.jxls.JxlsStyle;
import org.openxava.util.jxls.JxlsWorkbook;

abstract public class BaseReporting extends TabBaseAction implements JxlsConstants {
	static final int NUMBER_OF_REPORTS = 15;
	JxlsStyle boldRStyle = null;
	JxlsStyle boldLStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle textStyleLeft = null;
	JxlsStyle textStyleRight = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;
	JxlsStyle textStyleBlue = null;
	JxlsStyle numberd2 = null;
	JxlsStyle numberRedStyle = null;
	JxlsStyle numberYellowStyle = null;
	JxlsStyle numberGreenStyle = null;
	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	

	CellStyle style = null;
	CellStyle vstyle = null;
	CellStyle datestyle = null;
	CellStyle cnumberStyle = null;
	
	
	private String floatFormat = "##########0.00";
	
	public void setStyles(JxlsWorkbook reportWB) {

		reportWB.setFloatFormat(floatFormat);
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldLStyle = reportWB.addStyle(TEXT).setBold().setAlign(LEFT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleLeft = reportWB.addStyle(TEXT).setAlign(LEFT);
		textStyleRight = reportWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleBlue = reportWB.addStyle(TEXT).setAlign(LEFT).setTextColor(BLUE);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(LEFT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT);

		numberRedStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT).setCellColor(RED);
		numberGreenStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT).setCellColor(GREEN);
		numberYellowStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT).setCellColor(LIGHT_YELLOW);

		numberd2 = reportWB.addStyle(INTEGER);
		
		f1Style = reportWB.addStyle("0.0");

	}
}
