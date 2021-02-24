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
import org.openxava.filters.FilterException;
import org.openxava.filters.IFilter;
import org.openxava.jpa.*;
import org.openxava.tab.*;

import efd.model.*;
import efd.model.CustomReportSpecListModelling.ModelType;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;

public class GoModellingReport extends ViewBaseAction {

	public void execute() throws Exception {

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

		if (!projectid.isEmpty()) {
			// OHEA
			isOHEA = true;
			isOIHM = false;
			Efdutils.em("OHEA");

			project = XPersistence.getManager().find(Project.class, projectid);

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
			Status status;
			for (LivelihoodZone lzs2 : lzs) {
				for (Site site2 : lzs2.getSite()) {
					for (Community community : site2.getCommunity()) {
						lzcount=0; //reset valid wg count #591
						for (WealthGroup wealthGroup : community.getWealthgroup()) {
							for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {
								status = wealthGroupInterview.getStatus();
								if (status == Status.Validated) // add this to valid LZ / Site/ WG community list
								{
									lzcount++;
									// Efdutils.em("valid lz = " + lzs2.getLzid());
									lzid += "'" + lzs2.getLzid() + "',";

									correctLoc += "'" + community.getSite().getLocationid() + "',";

									lzidList.add(lzs2.getLzid());
									locList.add(community.getSite().getLocationid());

								}
							}

						}
					}
					if (lzcount < 3) { // Need at least 3 Valid WGIs in a Site
						correctLoc = previousCorrectLoc;
					}
					previousCorrectLoc = correctLoc;

					Efdutils.em("site lzcount in site loop = " + lzcount);
				}
				// Efdutils.em("lz lzcount in LZ loop= " + lzcount);
			}

			correctLoc = StringUtils.chop(correctLoc);
			lzid = StringUtils.chop(lzid);

			if (correctLoc.isEmpty()) {
				addError("No Valid Communities for LivelihoodZone and Project");

				return;

			}

		}

		showDialog();
		getView().setTitle("Livelihoods Modelling Reports");

		getView().setModelName("CustomReportSpecListModelling"); // Note no CustomReport Spec in Modelling scenario
		setControllers("ModellingReports", "Dialog");

		if (isOHEA) {
			/* dedupe */

			List<String> dedupeLZIDList = lzidList.stream().distinct().collect(Collectors.toList());
			String lzid2List = "";
			for (String lzid2 : dedupeLZIDList) {
				lzid2List += "'" + lzid2 + "',";
			}
			lzid2List = lzid2List.substring(0, lzid2List.length() - 1);

			List<String> dedupeLocList = locList.stream().distinct().collect(Collectors.toList());

			String locidList = "";
			for (String loc : dedupeLocList) {
				locidList += "'" + loc + "',";
			}
			locidList = locidList.substring(0, locidList.length() - 1);

			Efdutils.em("set OHEA LZ view");

			getView().setViewName("livelihoodZone");

			// getView().setValue("livelihoodZone.lzid", lzid);

			mainTab = getView().getSubview("livelihoodZone.site").getCollectionTab();

			getView().setValue("livelihoodZone.lzid", lzidval);

			String condition = mainTab.getBaseCondition() + " and ${locationid} in (" + correctLoc + ")";
			// String condition = "${livelihoodZone.lzid} in ("+lzid2List+")";
			// String condition = "${locationid} in (" + locidList + ")";

			mainTab.setBaseCondition(condition);

		} else {

			// Efdutils.em("set OIHM Study view");

			getView().setViewName("study");

			getView().setValue("study.id", studyid);

			mainTab = getView().getSubview("study.household").getCollectionTab();
			mainTab.setBaseCondition(mainTab.getBaseCondition() + " and ${status} = '4'");

		}
		mainTab.setFilterVisible(false);
		mainTab.setCustomizeAllowed(false);

		getView().setValue("modelType", 0);

		getView().refreshCollections();

		/* Set which report visualization can be downloaded */
		/* Codes used will be 1 OHEA,2 OIHM ,3,4,5,6 Modelling */

		Tab tabrep = getView().getSubview("visreports").getCollectionTab();
		if (isOHEA) {
			conditionrep = tabrep.getBaseCondition() + " and ${code} in (3,5)";
		} else if (isOIHM) {
			conditionrep = tabrep.getBaseCondition() + " and ${code} in (4,6)";
		}
		String modelName = getView().getModelName();
		String modelName2 = tabrep.getModelName();

		tabrep.setCustomizeAllowed(false);
		tabrep.setBaseCondition(conditionrep);
		tabrep.setFilterVisible(false);
		// tabrep.setEditor("DownloadSS");
		tabrep.setResizeColumns(false);
		// tabrep.setRowsHidden(true); hides rows but not frame

		getView().getSubview("visreports").setCollectionDetailVisible(false);
		getView().getSubview("visreports").setKeyEditable(false);
		getView().getSubview("visreports").setFrameClosed("visreports", true);
		getView().getSubview("visreports").setEditable(false);
		getView().getSubview("visreports").setCollectionMembersEditables(false);
		getView().getSubview("visreports").setCollectionEditable(false);

		getView().getSubview("visreports").setCollectionEditable(false);

		getView().setFrameClosed("visreports", true);

		getView().setCollectionDetailVisible(false);
		getView().setCollectionEditable(false);
		getView().setCollectionMembersEditables(false);

	}

}
