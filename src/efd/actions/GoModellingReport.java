/* 
 * Call Dialog to run modelling reports reports. 
 * 
 *  For modelling need to determine if OHEA - Community/LZ or OIHM Study / HH  
 */

package efd.actions;

import java.util.*;
import java.util.stream.*;

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

		String lzid = "";

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

			Map allValues2 = getView().getAllValues();

			Efdutils.em("allvals " + allValues);

			Collection<LivelihoodZone> livelihoodZone2 = project.getLivelihoodZone();

			

		} else if (!studyid.isEmpty()) {
			// OIHM
			isOIHM = true;
			isOHEA = false;
			Efdutils.em("OIHM");

		}

		if (isOHEA) {

			/*
			 * get array of LZ for this project
			 */

			Collection<LivelihoodZone> lzs = project.getLivelihoodZone();

			/*
			 * for each lzs determine if community / site / wg /wgi is Valid
			 */

			for (LivelihoodZone lzs2 : lzs) {
				for (Site site2 : lzs2.getSite()) {
					for (Community community : site2.getCommunity()) {
						for (WealthGroup wealthGroup : community.getWealthgroup()) {
							for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {
								Status status = wealthGroupInterview.getStatus();
								if (status == Status.Validated) // add this to valid LZ / Site/ WG community list
								{
									Efdutils.em("valid lz = " + lzs2.getLzid());
									lzid = lzs2.getLzid();
									correctLZ += "'" + community.getSite().getLocationid() + "',";
								}
							}

						}
					}
				}
			}

			correctLZ = StringUtils.chop(correctLZ);

			if (correctLZ.isEmpty()) {
				addError("No Valid Communities for LivelihoodZone and Project");

				return;

			}

		}
		
		
		showDialog();

		getView().setModelName("CustomReportSpecListModelling"); // Note no CustomReport Spec in Modelling scenario
		setControllers("ModellingReports", "Dialog");

		if (isOHEA) {
			Efdutils.em("set OHEA LZ view");

			getView().setViewName("livelihoodZone");

			getView().setValue("livelihoodZone.lzid", lzid);

			Tab tab = getView().getSubview("livelihoodZone.site").getCollectionTab();

			String condition = tab.getBaseCondition() + " and ${locationid} in (" + correctLZ + ")";

			tab.setBaseCondition(condition);
			

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
