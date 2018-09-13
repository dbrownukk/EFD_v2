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
public class NonFoodExpenditureChart extends JasperReportBaseAction {             
    
	WealthGroupInterview wealthGroupInterview;
	WealthGroup wealthGroup;
	
	protected String getJRXML() {      
    	return "wgi_nonfoodexpenditure.jrxml"; 
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
		Boolean normalise = false; // normalised data
		parameters.put("communityid", communityid);
		parameters.put("normalise", normalise);

		parameters.put("normalisationMessage", "");
		
		return parameters;
	}
	
	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
 

	
	
}