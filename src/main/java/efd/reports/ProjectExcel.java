package efd.reports;
 
import java.util.*;

import efd.model.Project;
import net.sf.jasperreports.engine.*;

import org.openxava.actions.*;
import efd.model.*;


/**
 * Report of products of the selected subfamily. <p>
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from sample Javier Paniza
 */
public class ProjectExcel extends JasperReportBaseAction {             
    
	Project project;
	WealthGroupInterview wealthGroupInterview;
	//WealthGroup wealthGroup;
	
	
	
	protected String getJRXML() throws Exception {      
		setFormat("excel");
		
		String fileName = getView().getValueString("projecttitle");
		
		
		setFileName(fileName);
   
		return "projectreport.jrxml"; 
	
    	
    }
	
	
	
    
	public boolean inNewWindow() {
		return true;
	}
	
	
	@Override
	public Map getParameters() throws Exception  {
		
		String projectid = getView().getValueString("projectid");
		Map parameters = new HashMap();			
		
		System.out.println("projid = "+projectid);
		parameters.put("projectid", projectid);
		return parameters;
	}
	
	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}