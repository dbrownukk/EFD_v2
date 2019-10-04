
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

public class GoOIHMReports extends ViewBaseAction {
//	public class GoOIHMReports extends ViewBaseAction {

	@Inject
	private Tab tab;

	public void execute() throws Exception {

		// get Custom Report Spec

		//List<CustomReportSpec> topic = XPersistence.getManager().createQuery("from CustomReportSpec").getResultList();

		//CustomReportSpecList customReportSpecList = new CustomReportSpecList();

		// Map allValues = getPreviousView().getAllValues();
		// System.out.println("allprevvals in gooihmreports = "+allValues);

		//Map allValues = getView().getAllValues();

		String studyid = getView().getValueString("id");

		//System.out.println("allvals in gooihmreports 1 = " + allValues);

		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecList");
		

		setControllers("OIHMReports", "Dialog");



		getView().setValue("study.id", studyid);
		/*
		getView().setValue("study.household.status", "Validated");

		Map allValues1 = getView().getAllValues();
		Map allValues2 = getView().getSubview("study.household").getAllValues();

		System.out.println("allvals in gooihmreports 2 = " + allValues1);
		System.out.println("allvals in gooihmreports 2 = "+allValues2);
		*/
		Tab tab = getView().getSubview("study.household").getCollectionTab();
	
		tab.setBaseCondition(tab.getBaseCondition()+" and ${status} = '4'"); //Validated
		getView().refreshCollections();
	}

}
