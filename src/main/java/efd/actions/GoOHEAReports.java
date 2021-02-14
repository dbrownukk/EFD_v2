
/* 
 * Call Dialog to run reports. Show customerreport pick list and list of Sites/Communities
 */

package efd.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.openxava.actions.CollectionBaseAction;
import org.openxava.tab.Tab;

import efd.model.Community;
import efd.model.LivelihoodZone;
import efd.model.Site;
import efd.model.WealthGroup;
import efd.model.WealthGroupInterview.Status;

public class GoOHEAReports extends CollectionBaseAction {
	@Inject
	private Tab tab;

	public void execute() throws Exception {
		System.out.println("in gooheareports");

		LivelihoodZone livelihoodZone = null;

		String correctLZ = "";

		Object projectid = getView().getValue("projectid");

		if (projectid == null) {
			projectid = getView().getValue("project.projectid");
		}

	

		Iterator<?> it = getSelectedObjects().iterator();

		

		livelihoodZone = (LivelihoodZone) it.next();

		// List of Sites for this LZ

		correctLZ = isLZinProject(projectid, livelihoodZone);

		correctLZ = StringUtils.chop(correctLZ);
		if (correctLZ.isEmpty()) {
			addError("No Valid Communities for LivelihoodZones and Project");
			return;

		}

		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecListOHEA");

		setControllers("OHEAReports", "Dialog");

		getView().setValue("livelihoodZone.lzid", livelihoodZone.getLzid());
		getView().setValue("customReportSpec.id", 0);

		Tab tab = getView().getSubview("livelihoodZone.site").getCollectionTab();

		String condition = tab.getBaseCondition() + " and ${locationid} in (" + correctLZ + ")";

		tab.setBaseCondition(condition);
		// Validated WGIs

		getView().getRoot().refreshCollections();

		/* Set which report visualization can be downloaded */
		/* Codes used will be 1 OHEA,2 OIHM ,3,4,5,6 Modelling 
		 * 7 is nutrient visualisation */

		Tab tabrep = getView().getSubview("visreports").getCollectionTab();
		getView().getSubview("visreports").setCollapsed("visreports", true);
		String conditionrep = tabrep.getBaseCondition() + " and ${code} in (1,7)";
		tabrep.setCustomizeAllowed(false);
		tabrep.setBaseCondition(conditionrep);
		tabrep.setFilterVisible(false);

	}

	public static String isLZinProject(Object projectid, LivelihoodZone livelihoodZone) {
		int lzcount;
		String correctLZ = "";
		Boolean isValidated;

		List<Site> sites = new ArrayList<Site>();
		sites = (List<Site>) livelihoodZone.getSite();
	

		if (sites.size() == 0) {

			// addError("No Valid Communities for LivelihoodZone and Project");
			return "";
		}

		// For each site - is it in this Project?
		for (Site site2 : sites) {

			for (Community community : site2.getCommunity()) {
				
				
				
				
				donesite: if (community.getProjectlz().getProjectid().equals(projectid)) {
					/*
					 * Is there a Valid WGI in this site?
					 * 
					 */
					isValidated = false;
					for (Community community2 : community.getSite().getCommunity()) {

						isValidated = false;
						lzcount = 0;
						for (WealthGroup wealthGroup3 : community2.getWealthgroup()) {
							if (wealthGroup3.getWealthGroupInterview().stream()
									.anyMatch(p -> p.getStatus() == Status.Validated)) {
								/* get list of Sites with a Valid WGI */
								correctLZ += "'" + community.getSite().getLocationid() + "',";
							//	System.out.println(
								//		"correctlz = " + correctLZ + " " + community2.getSite().getSubdistrict());
								break donesite;
							}

						}

					}
				}

			}
		}

		return correctLZ;
	}

}
