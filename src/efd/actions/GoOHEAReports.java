
/* IOX Dialog to enter new name of Study  */

package efd.actions;

import java.util.*;

import javax.inject.*;
import javax.persistence.*;

import org.hibernate.loader.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;
import efd.model.WealthGroupInterview.*;

public class GoOHEAReports extends ViewBaseAction {
//	public class GoOIHMReports extends ViewBaseAction {

	@Inject
	private Tab tab;

	public void execute() throws Exception {

		String communityid = getView().getValueString("communityid");
		//Map values = getView().getAllValues();
		//System.out.println("allvals= "+values.toString());
		System.out.println("coomunityid = "+communityid);
		
		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecListOHEA");

		setControllers("OHEAReports", "Dialog");

		//getView().setValue("community.communityid", communityid);

		// Tab tab = getView().getSubview("community.wealthgroup").getCollectionTab();

		// tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'"); //
		// Validated
		getView().refreshCollections();
	}

}
