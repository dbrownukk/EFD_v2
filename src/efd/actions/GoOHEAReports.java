
/* 
 * Call Dialog to run reports. Show customerreport pick list and list of Sites/Communities
 */

package efd.actions;

import java.util.*;

import javax.inject.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;

import efd.model.*;

public class GoOHEAReports extends CollectionBaseAction {
	@Inject
	private Tab tab;

	public void execute() throws Exception {
		System.out.println("in go ohea");

		Project project = null;
		LivelihoodZone livelihoodZone = null;
		Site site = null;
		Boolean isCorrectProject = false;
		String correctLZ = "";

		Map<?, ?> allValues = getView().getAllValues();
		System.out.println("allvals in GOOHEAREP = " + allValues.toString());
		String projectid = getView().getValueString("projectid");
		project = XPersistence.getManager().find(Project.class, projectid);
		System.out.println("project context  = " + project.getProjecttitle());

		Iterator it = getSelectedObjects().iterator();
		livelihoodZone = (LivelihoodZone) it.next();

		// List of Sites for this LZ
		List<Site> sites = new ArrayList<Site>();
		sites = (List<Site>) livelihoodZone.getSite();

		// For each site - is it in this Project?
		for (Site site2 : sites) {
			for (Community community : site2.getCommunity()) {

				System.out.println("project = " + community.getProjectlz().getProjecttitle());
				if(community.getProjectlz() == project)
				{
					System.out.println("we have a match");
					correctLZ += community.getSite().getLocationid()+",";
					
					
					/*  
					 * Filter on Validated only...
					 * 
					 * TODO
					 * 
					for (WealthGroup wealthGroup : community.getWealthgroup()) {
						
						XPersistence.getManager().createQuery("from WealthGroupInterview where status = 4);
					}
					*/
					
				}
			}

		}

		
		
		

		correctLZ = StringUtils.chop(correctLZ);



		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecListOHEA");

		setControllers("OHEAReports", "Dialog");

		getView().setValue("livelihoodZone.lzid", livelihoodZone.getLzid());
		Tab tab = getView().getSubview("livelihoodZone.site").getCollectionTab();

		System.out.println("tab = " + tab.getPropertiesNamesAsString());

		String condition = tab.getBaseCondition() + " and ${locationid} in ('" + correctLZ + "')";
		System.out.println("condition = " + condition);
		tab.setBaseCondition(condition);
		// Validated
		getView().refreshCollections();
	}

}
