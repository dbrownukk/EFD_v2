package efd.reports;
 
import java.util.*;
 
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

import org.hibernate.envers.internal.tools.query.*;
import org.openxava.actions.*;
import org.openxava.model.*;

import org.openxava.util.*;
import org.openxava.validators.*;
 
/**
 * Report 
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from eample Javier Paniza
 */
public class SitesbyCountry extends JasperReportBaseAction {         
 

 

 
    protected String getJRXML() {                                                
        return "SitesByCountry.jrxml"; 
    }
    
	public boolean inNewWindow() {
		return false;
	}

	@Override
	protected Map getParameters() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected JRDataSource getDataSource() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
 

    
 
}