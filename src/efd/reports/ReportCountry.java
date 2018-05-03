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
 * Report of products of the selected subfamily. <p>
 *
 * Uses JasperReports. <br>
 *
 * @author David Brown from eample Javier Paniza
 */
public class ReportCountry extends JasperReportBaseAction {           // 1
 
 
    public Map getParameters() throws Exception  {                                 // 2
       // Messages errors =
       //     MapFacade.validate("FilterBySubfamily", getView().getValues());
       // if (errors.contains()) throw new ValidationException(errors);              // 3
       Map parameters = new HashMap();
       // parameters.put("family", getSubfamily().getFamily().getDescription());
       // parameters.put("subfamily", getSubfamily().getDescription());
    		parameters.put("test","test");
    	return parameters;
    }
 
    protected JRDataSource getDataSource() throws Exception {                      // 4
        return null;    /* should use jdbc in the report itself */
    }
 
    protected String getJRXML() {                                                  // 5
        return "/Users/david/git/EFD_v2/src/efd/JasperReports/Country.jrxml"; // To read from classpath
        // return "/home/javi/Products.jrxml"; // To read from file system
    }
 

    
 
}