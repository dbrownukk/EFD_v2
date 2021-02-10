package efd.reports;
 
import java.util.*;
 
import net.sf.jasperreports.engine.*;

import org.openxava.actions.*;
import org.openxava.model.*;
import efd.rest.domain.model.*;


/**
 * Report of products of the selected subfamily. <p>
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from eample Javier Paniza
 */
public class AssetLandChartNormalised extends JasperReportBaseAction {             
    
	WealthGroupInterview wealthGroupInterview;
	WealthGroup wealthGroup;
	
	protected String getJRXML() {      
		
    	return "wgi_asset_land.jrxml"; 
    	
    	
    }
    
	public boolean inNewWindow() {
		return true;
	}
	
	@Override
	public Map getParameters() throws Exception  {
		
		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview", getView().getValues());
		String communityid = wealthGroupInterview.getWealthgroup().getCommunity().getCommunityid();
	
		Map parameters = new HashMap();			
		//String wgiid = getView().getValueString("wgiid");

		/* Add two params for HH Average so that Normalised data is caluclated */
		
		Integer wgAverageNumberInHH =  wealthGroupInterview.getWgAverageNumberInHH();
		System.out.println("wgAverageNumberInHH = "+wgAverageNumberInHH);
		//Integer wgAverageNumberInHH = 1;   // non normalised data 
		parameters.put("communityid", communityid);
		parameters.put("wgAverageNumberInHH", wgAverageNumberInHH);
		parameters.put("normalisationMessage","Normalised by Average in Household");
		
		return parameters;
	}
	
	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
 

	
	
}