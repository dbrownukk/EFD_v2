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

import com.openxava.naviox.model.Module;

import efd.model.*;
import efd.model.WealthGroupInterview.*;

public class GoOIHMReports extends ViewBaseAction {
//	public class GoOIHMReports extends ViewBaseAction {

	@Inject
	private Tab tab;

	public void execute() throws Exception {

		String studyid = getView().getValueString("id");

		System.out.println("In GoIHMReports studyid = " + studyid);

		showDialog();

		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecList");


			setControllers("OIHMReports", "Dialog");
		
		getView().setValue("study.id", studyid);
		getView().setValue("studyToSelect.id", studyid);

		if (studyid.isEmpty()) {
			System.out.println("studid " + studyid);
			// getView().addListAction("OIHMReporting.run");
		}

		// getView().findObject();
		Tab tab = getView().getSubview("study.household").getCollectionTab();

		tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'"); // Validated
		getView().refreshCollections();

		/* Set which report visualization can be downloaded */
		/* Codes used will be 1 OHEA,2 OIHM ,3,4,5,6 Modelling */

		Tab tabrep = getView().getSubview("visreports").getCollectionTab();
		String conditionrep = tabrep.getBaseCondition() + " and ${code} = '2'";
		tabrep.setCustomizeAllowed(false);
		tabrep.setBaseCondition(conditionrep);
		tabrep.setFilterVisible(false);

	}

}
