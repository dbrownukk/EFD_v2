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
 * Report of products of the selected subfamily. <p>
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
				
		String[] reports = new String[] {"wgi_asset.jrxml", "wgi_asset_foodstocks.jrxml" ,"wgi_asset_land.jrxml"}; 
    	return reports; 
    	
    	
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
	protected JRDataSource[] getDataSources() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
 

	
	
}