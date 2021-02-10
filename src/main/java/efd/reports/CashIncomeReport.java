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
 * @author David Brown from example Javier Paniza
 */
public class CashIncomeReport extends JasperReportBaseAction {             
    
	WealthGroupInterview wealthGroupInterview;
	//WealthGroup wealthGroup;
	
	
	
	protected String getJRXML() throws Exception {      
		setFormat("excel");
		
		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview", getView().getValues());
		
		
		
		String fileName = wealthGroupInterview.getWealthgroup().getCommunity().getProjectlz().getProjecttitle()+" "
				+wealthGroupInterview.getWealthgroup().getCommunity().getSite().getLivelihoodZone().getLzname()+" "+
				wealthGroupInterview.getWealthgroup().getCommunity().getSite().getLocationdistrict()+" "+
				wealthGroupInterview.getWealthgroup().getCommunity().getSite().getSubdistrict()+" "+
				wealthGroupInterview.getWealthgroup().getWgnameeng();

		/*
		System.out.println("wgiid status = "+wealthGroupInterview.getStatus());
		if (wealthGroupInterview.getStatus() != (WealthGroupInterview.Status.Validated))
		{
			addWarning("Wealthgroup must be Validated before running report");
			return null;
		}
		*/
		
		setFileName(fileName);
    	
		
   
		return "wgi_wealthincomereport.jrxml"; 
	
    	
    }
	
	
	
    
	public boolean inNewWindow() {
		return true;
	}
	
	
	@Override
	public Map getParameters() throws Exception  {
		
		wealthGroupInterview = (WealthGroupInterview) MapFacade.findEntity("WealthGroupInterview", getView().getValues());
		//String communityid = wealthGroupInterview.getWealthgroup().getCommunity().getCommunityid();
		
		
		Map parameters = new HashMap();			
		//String wgiid = getView().getValueString("wgiid");
		String wgid = wealthGroupInterview.getWealthgroup().getWgid();
		
		System.out.println("wgid = "+wgid);
		
		//parameters.put("communityid", communityid);
		
		parameters.put("wealthgroupid", wgid);
		
	
		
		return parameters;
	}
	
	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}