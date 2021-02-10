/* 
 * Call Dialog to run modelling reports reports. 
 * 
 *  For modelling need to determine if OHEA - Community/LZ or OIHM Study / HH  
 */

package efd.actions;

import java.util.*;

import efd.rest.domain.Project;
import org.openxava.actions.*;
import org.openxava.tab.*;

public class GoModellingReport2 extends ViewBaseAction {

	public void execute() throws Exception {

		System.out.println("in gomodellingreport 2 ");

		Tab mainTab;
		Boolean isOIHM = false;
		Boolean isOHEA = false;
		Project project = null;
		String conditionrep = "";

		String correctLoc = "";
		String previousCorrectLoc = "";
		int lzcount = 0;

		List<String> lzidList = new ArrayList<String>();
		List<String> locList = new ArrayList<String>();

		String lzid = "";

		Map allValues = getView().getAllValues();

		String projectid = getView().getValueString("project.projectid");
		String studyid = getView().getValueString("study.id");
		String lzidval = getView().getValueString("livelihoodZone.lzid");



		showDialog();
		getView().setTitle("Livelihoods Modelling Reports");

		getView().setModelName("CustomReportSpecListModelling"); // Note no CustomReport Spec in Modelling scenario
		setControllers("ModellingReports", "Dialog");

		// Efdutils.em("set OIHM Study view");

		//getView().setViewName("study");

		//getView().setValue("study.id", studyid);

		//mainTab = getView().getSubview("study.household").getCollectionTab();
		//mainTab.setBaseCondition(mainTab.getBaseCondition() + " and ${status} = '4'");



		//getView().setValue("modelType", 0);

		//getView().refreshCollections();


	}

}
