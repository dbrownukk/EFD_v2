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
public class FoodConsumedChartNormalised extends JasperReportBaseAction {             
    
	WealthGroupInterview wealthGroupInterview;
	WealthGroup wealthGroup;
	
	protected String getJRXML() {      
    	return "wgi_foodconsumed.jrxml"; 
    	// return "Countrys.jrxml"; // To read from classpath
    	
    }
    
	public boolean inNewWindow() {
		return true;
	}
	
	@Override
	public Map getParameters() throws Exception  {
		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview", getView().getValues());
		
		String communityid = wealthGroupInterview.getWealthgroup().getCommunity().getCommunityid();
		
		
		Map parameters = new HashMap();			
		Boolean normalise = true; // normalised data
		parameters.put("communityid", communityid);
		parameters.put("normalise", normalise);

		parameters.put("normalisationMessage", "Normalised by Average in Household");
		return parameters;
	}
	
	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
 

	
	
}