/* 
 * Call Dialog to run reports. 
 * 
 *  For modelling need to determine if OHEA - Community/LZ or OIHM Study / HH  
 */

package efd.actions;

import java.util.*;

import javax.inject.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;

public class GoModellingReport extends ViewBaseAction {

	@Inject
	private Tab tab;
	Boolean isOIHM = false;
	Boolean isOHEA = false;
	Project project = null;
	LivelihoodZone livelihoodZone = null;
	Site site = null;
	Boolean isCorrectProject = false;
	String correctLZ = "";

	public void execute() throws Exception {

		Map allValues = getView().getAllValues();
		Efdutils.em("In Gomdellingreport all vals sss = " + allValues);

		String projectid = getView().getValueString("project.projectid");
		String studyid = getView().getValueString("study.id");

		Efdutils.em("studyid = " + studyid);
		Efdutils.em("projectid = " + projectid);

		if (!projectid.isEmpty()) {
			// OHEA
			isOHEA = true;
			isOIHM = false;
			Efdutils.em("OHEA");
			
			project = XPersistence.getManager().find(Project.class, projectid);

			//Iterator it = getSelectedObjects().iterator();

		//	it.getClass();

			//livelihoodZone = (LivelihoodZone) it.next();

			// List of Sites for this LZ
			List<Site> sites = new ArrayList<Site>();
			sites = (List<Site>) livelihoodZone.getSite();
			System.out.println("sites count = " + sites.size());
			if (sites.size() == 0) {
				addError("No Valid Communities for LivelihoodZone and Project");
				return;
			}

			
			Boolean isValidated = false;
			// For each site - is it in this Project?
			for (Site site2 : sites) {

				for (Community community : site2.getCommunity()) {
					if (community.getProjectlz() == project) {
						/*
						 * Is there a Valid WGI in this site?
						 * 
						 */
						System.out.println("community = ");
						isValidated = false;
						for (Community community2 : community.getSite().getCommunity()) {
							isValidated = false;
							for (WealthGroup wealthGroup3 : community2.getWealthgroup()) {
								for (WealthGroupInterview wealthGroupInterview3 : wealthGroup3.getWealthGroupInterview()) {
									if (wealthGroupInterview3.getStatus() == Status.Validated) {
										isValidated = true;
									}

								}

							}
						}

						if (isValidated) {
							correctLZ += "'" + community.getSite().getLocationid() + "',";

						}
					}
				}

			}

			correctLZ = StringUtils.chop(correctLZ);
			if (correctLZ.isEmpty()) {
				addError("No Valid Communities for LivelihoodZone and Project");
				return;

			}
			
			
			
			
			

		} else if (!studyid.isEmpty()) {
			// OIHM
			isOIHM = true;
			isOHEA = false;
			Efdutils.em("OIHM");

		}

		showDialog();

		getView().setModelName("CustomReportSpecListModelling");  // Note no CustomReport Spec in Modelling scenario
		setControllers("ModellingReports", "Dialog");

		if (isOHEA) {
			Efdutils.em("set OHEA LZ view");
			getView().setViewName("livelihoodZone");
			getView().setValue("livelihoodZone.project.id", projectid);
			
			getView().setValue("livelihoodZone.lzid", livelihoodZone.getLzid());

			Tab tab = getView().getSubview("livelihoodZone.site").getCollectionTab();

			String condition = tab.getBaseCondition() + " and ${locationid} in (" + correctLZ + ")";

			tab.setBaseCondition(condition);
			// Validated WGIs

			getView().getRoot().refreshCollections();
			
		} else {

			Efdutils.em("set OIHM Study view");
			getView().setViewName("study");
			allValues = getView().getAllValues();
			Efdutils.em("In Gomdellingreport all vals s = " + allValues);
			getView().setValue("study.id", studyid);

			Tab tab = getView().getSubview("study.household").getCollectionTab();
			tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'");	
		}
		getView().refreshCollections();
	}

}
