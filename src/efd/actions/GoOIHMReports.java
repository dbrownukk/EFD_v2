/* 
 * Call Dialog to run reports. Show customerreport pick list and list of Validated Households
 */

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

public class GoOIHMReports extends ViewBaseAction {
//	public class GoOIHMReports extends ViewBaseAction {

	@Inject
	private Tab tab;

	public void execute() throws Exception {

		String studyid = getView().getValueString("id");

		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecList");

		setControllers("OIHMReports", "Dialog");

		getView().setValue("study.id", studyid);

		Tab tab = getView().getSubview("study.household").getCollectionTab();

		tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'"); // Validated
		getView().refreshCollections();
	}

}
