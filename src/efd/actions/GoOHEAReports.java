
/* 
 * Call Dialog to run reports. Show customerreport pick list and list of Sites/Communities
 */

package efd.actions;

import java.util.*;

import org.openxava.actions.*;

import efd.model.*;

public class GoOHEAReports extends CollectionBaseAction {

	public void execute() throws Exception {
		System.out.println("in go ohea");
		LivelihoodZone livelihoodZone = null;
		Iterator it = getSelectedObjects().iterator(); // Only reads one row but possible in future to run report for
														// many lzs
		while (it.hasNext()) {
			livelihoodZone = (LivelihoodZone) it.next();
			// System.out.println("selected lz = " + livelihoodZone.getLzname());
			// for (Site site : livelihoodZone.getSite()) {
			// {
			// System.out.println("site = "+site.getLocationdistrict());
			// }

			// }
		}

		System.out.println("in go ohea done get selected");

		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecListOHEA");

		setControllers("OHEAReports", "Dialog");

		getView().setValue("livelihoodZone.lzid", livelihoodZone.getLzid());
		// Tab tab = getView().getSubview("community.wealthgroup").getCollectionTab();

		// tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'"); //
		// Validated
		getView().refreshCollections();
	}

}
