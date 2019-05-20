
/* IOX Dialog to enter new name of Study  */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class GoOIHMReports extends ViewBaseAction {

	public void execute() throws Exception {

		// get Custom Report Spec
		
		List<CustomReportSpec> topic = XPersistence.getManager().createQuery("from CustomReportSpec").getResultList();
		
		
		
		CustomReportSpecList customReportSpecList = new CustomReportSpecList();
		
		
		
		
		showDialog();
		getView().setTitle("Enter Custom Report Spec name to run");

		getView().setModelName("CustomReportSpecList");

		setControllers("OIHMReports", "Dialog");

		
	}

}
