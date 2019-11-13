
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
import efd.model.WealthGroupInterview.*;

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
		System.out.println("in go ohea 2");

		String projectid = getView().getValueString("projectid");
		System.out.println("in go ohea 3");
		project = XPersistence.getManager().find(Project.class, projectid);
		System.out.println("in go ohea 4");
		Iterator it = getSelectedObjects().iterator();
		livelihoodZone = (LivelihoodZone) it.next();

		// List of Sites for this LZ
		List<Site> sites = new ArrayList<Site>();
		sites = (List<Site>) livelihoodZone.getSite();
		System.out.println("sites count = "+sites.size());
		if(sites.size() == 0)
		{
			addError("No Valid Communities for LivelihoodZone and Project");
			return;
		}
		
		System.out.println("in go ohea 5");
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
						System.out.println("in loop corrected LZ "+correctLZ);
					}
				}
			}

		}

		System.out.println("in go ohea 6");
		correctLZ = StringUtils.chop(correctLZ);
		if(correctLZ.isEmpty())
		{
			addError("No Valid Communities for LivelihoodZone and Project");
			return;
			
		}
		
		
		showDialog();
		getView().setTitle("Enter Custom Report Spec name to Run");

		getView().setModelName("CustomReportSpecListOHEA");

		setControllers("OHEAReports", "Dialog");

		getView().setValue("livelihoodZone.lzid", livelihoodZone.getLzid());
		System.out.println("in go ohea 7");
		Tab tab = getView().getSubview("livelihoodZone.site").getCollectionTab();
		System.out.println("in go ohea 8 " + correctLZ);
		String condition = tab.getBaseCondition() + " and ${locationid} in (" + correctLZ + ")";
		System.out.println("condition = " + condition);
		tab.setBaseCondition(condition);
		// Validated WGIs
		System.out.println("in go ohea 9");
		getView().getRoot().refreshCollections();
	}

}
