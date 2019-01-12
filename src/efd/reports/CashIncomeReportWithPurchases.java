package efd.reports;

import java.text.*;
import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

import org.hibernate.envers.internal.tools.query.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;
import efd.model.*;

/**
 * Report of products of the selected subfamily.
 * <p>
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from example Javier Paniza
 */
public class CashIncomeReportWithPurchases extends JasperReportBaseAction {

	
	
	WealthGroupInterview wealthGroupInterview;
	// WealthGroup wealthGroup;
	
	protected String getJRXML() throws Exception {
		setFormat("excel");

		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview",
				getView().getValues());

		String fileName = wealthGroupInterview.getWealthgroup().getCommunity().getProjectlz().getProjecttitle() + " "
				+ wealthGroupInterview.getWealthgroup().getCommunity().getSite().getLivelihoodZone().getLzname() + " "
				+ wealthGroupInterview.getWealthgroup().getCommunity().getSite().getLocationdistrict() + " "
				+ wealthGroupInterview.getWealthgroup().getCommunity().getSite().getSubdistrict() + " "
				+ wealthGroupInterview.getWealthgroup().getWgnameeng();

		setFileName(fileName);

		return "wgi_wealthincomereportwithpurchases.jrxml";

	}

	public boolean inNewWindow() {
		return false;
	}

	@Override
	public Map getParameters() throws Exception {

		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview",
				getView().getValues());


		Map parameters = new HashMap();
	
		String wgid = wealthGroupInterview.getWealthgroup().getWgid();

		System.out.println("wgid = " + wgid);

		

		parameters.put("wealthgroupid", wgid);

		return parameters;
	}

	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}