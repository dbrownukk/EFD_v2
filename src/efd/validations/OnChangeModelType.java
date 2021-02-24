/**
 * 
 */
package efd.validations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openxava.actions.IChainAction;
import org.openxava.actions.OnChangePropertyBaseAction;
import org.openxava.jpa.XPersistence;
import org.openxava.tab.Tab;
import org.openxava.view.View;

import efd.model.Community;
import efd.model.ExpandabilityRule;
import efd.model.LivelihoodZone;
import efd.model.Project;
import efd.model.Site;
import efd.model.WealthGroup;
import efd.model.WealthGroupInterview;
import efd.model.WealthGroupInterview.Status;
import efd.utils.Efdutils;

/**
 * @author DRB 14/4/2020 In Customer Report CustomReportSpecListModelling filter
 *         detail based on whether a Coping STrategy exists for the Row
 *
 */
public class OnChangeModelType extends OnChangePropertyBaseAction {

	Boolean isOHEA = false;
	Boolean isOIHM = false;
	Boolean isCopingStrategy = false;
	Boolean isChangeScenario = false;
	String expandabilityBaseCondition = "";
	String changeScenarioBaseCondition = "";

	public void execute() throws Exception {
		

		/*
		 * If change to Coping Strategy need to check if each of the collection types
		 * has a Coping Strategy in Expandability Rules
		 * 
		 * Also, on change back to Change Scenario need to relax restriction
		 * 
		 */

		Map allValues = getView().getAllValues();
		View subview = null;
		Tab collectionTab = null;

		isOHEA = allValues.containsKey("livelihoodZone");
	
		isOIHM = allValues.containsKey("study");
	

		if (getNewValue().toString().equals("CopingStrategy")) {
			isCopingStrategy = true;

		} else {
			isChangeScenario = true;
		}

		if (isOHEA) {
			subview = getView().getSubview("livelihoodZone.site");
			subview.collectionDeselectAll();
			collectionTab = subview.getCollectionTab();
		} else if (isOIHM) {
			return;
		}

		if (isOHEA && isCopingStrategy) {

			for (Map<String, Object> sites : subview.getCollectionValues()) {
				System.out.println("site = " + sites.toString());
				String locationid = (String) sites.get("locationid");
				System.out.println("locid = " + locationid);
				Site site = XPersistence.getManager().find(Site.class, locationid);
				for (Community community : site.getCommunity()) {
					
					
					long expcount = community.getExpandabilityRule().stream().count();
					System.out.println("Exp Count = "+expcount);
					
					
					Long result = (Long) XPersistence.getManager()
							.createQuery("select count(*) from ExpandabilityRule where communityRuleSet = :community")
							.setParameter("community", community).getSingleResult();
					
					changeScenarioBaseCondition += "'" + locationid + "',";
					if (result != 0) // remove from Site list in collection
					{
						expandabilityBaseCondition += "'" + locationid + "',";
					}

				}
			}
			if (expandabilityBaseCondition.length() > 0) {
				expandabilityBaseCondition = expandabilityBaseCondition.substring(0,
						expandabilityBaseCondition.length() - 1);
				collectionTab.setBaseCondition(
						"${livelihoodZone.lzid} = ? and ${locationid} in (" + expandabilityBaseCondition + ")");
			} else {
				collectionTab.setBaseCondition("${livelihoodZone.lzid} = ? and ${locationid} in ('xxx')");
				addError("No Coping Strategies for this Livelihood Zone");
				subview.getParent().refreshCollections();
				return;
			}

		} else if (isOHEA && isChangeScenario) {
			String correctLoc = "";
			String previousCorrectLoc = "";
			int lzcount = 0;
			String lzid = "";
			List<String> lzidList = new ArrayList<String>();
			List<String> locList = new ArrayList<String>();

			String projectid = getPreviousView().getValueString("project.projectid");
			Project project = XPersistence.getManager().find(Project.class, projectid);
			Collection<LivelihoodZone> lzs = project.getLivelihoodZone();
			for (LivelihoodZone lzs2 : lzs) {
				for (Site site2 : lzs2.getSite()) {
					for (Community community : site2.getCommunity()) {
						for (WealthGroup wealthGroup : community.getWealthgroup()) {
							for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {
								Status status = wealthGroupInterview.getStatus();
								if (status == Status.Validated) // add this to valid LZ / Site/ WG community list
								{
									lzcount++;

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

				}
			}
			correctLoc = StringUtils.chop(correctLoc);
			lzid = StringUtils.chop(lzid);

			if (correctLoc.isEmpty()) {
				addError("No Valid Communities for LivelihoodZone and Project");

				return;

			}

			if (correctLoc.length() > 0) {
				collectionTab.setBaseCondition("${livelihoodZone.lzid} = ? and ${locationid} in (" + correctLoc + ")");
			} else {
				collectionTab.setBaseCondition("${livelihoodZone.lzid} = ? and ${locationid} in ('xxx')");

			}

		}

		subview.getParent().refreshCollections();
	}

}
