package efd.reports;

import java.util.*;

import net.sf.jasperreports.engine.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import efd.model.*;

/**
 * Report of products of the selected subfamily.
 * <p>
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from eample Javier Paniza
 */
public class AssetChartNormalised extends JasperConcatReportBaseAction {

	WealthGroupInterview wealthGroupInterview;
	WealthGroup wealthGroup;

	@Override
	protected String[] getJRXMLs() throws Exception {
		System.out.println("in assetchart");
		setFormat("pdf");

		String[] reports = new String[] { "wgi_asset2.jrxml", "wgi_asset_foodstocks3.jrxml", "wgi_asset_land.jrxml" };
		return reports;

	}

	public boolean inNewWindow() {
		return true;
	}

	@Override
	public Map getParameters() throws Exception {

		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview",
				getView().getValues());
		String communityid = wealthGroupInterview.getWealthgroup().getCommunity().getCommunityid();

		Map parameters = new HashMap();

		Boolean normalise = true; // normalised data
		parameters.put("communityid", communityid);
		parameters.put("normalise", normalise);

		parameters.put("normalisationMessage", "Normalised by Average in Household in Wealthgroup");

		return parameters;
	}

	@Override
	protected JRDataSource[] getDataSources() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}