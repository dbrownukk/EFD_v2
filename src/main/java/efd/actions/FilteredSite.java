package efd.actions;

import java.util.List;

import javax.inject.*;
import javax.persistence.*;

import efd.rest.domain.Project;
import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.rest.domain.model.*;

/*
 * DRB Do not show existing Site in pick list 
 * 29/01/2018
 * 
 *  Rules 
 *  Should not offer Sites already associated with Communities in this Project
 *  Should only offer Sites in the Livelihood that is the current focus
 *	Will not allow you to add a Community based on a Site in a different LZ
 *	Will not allow you to ad the same site twice
*/

public class FilteredSite extends ReferenceSearchAction {

	@Inject
	private String efdModel;

	public void execute() throws Exception {

		Boolean isConditionSet = false;
		String resourceUnit = null;
		Boolean isInOHEA = false;

		System.out.println("In FilteredSite");
		System.out.println("vals = " + getView().getBaseModelName().toString());
		/*
		 * try {
		 * 
		 * System.out.println("vals = "+getView().getBaseModelName().toString());
		 * 
		 * Map allValues = getPreviousView().getAllValues();
		 * System.out.println("all values = " + allValues);
		 * 
		 * if (!getPreviousView().getValueString("wgid").isEmpty() ||
		 * !getPreviousView().getValueString("communityid").isEmpty() ||
		 * !getView().getValueString("wgid").isEmpty() ||
		 * !getView().getValueString("communityid").isEmpty()) { isInOHEA = true;
		 * System.out.println("in ohea set isInOHEA"); } } catch (Exception e) { //
		 * Previous did not exist // e.printStackTrace(); }
		 */

		if (getView().getBaseModelName().toString().equals("Community")) {
			isInOHEA = true;
			System.out.println("set isInOHEA to TRUE");
		}

		if (isInOHEA) {
			System.out.println("isInOHEA");
			/* need to set project context */
			if (getView().getValueString("projectlz.projectid").isEmpty()) {
				throw new IllegalStateException(XavaResources.getString("Site lookup must be in context of a Project"));
			}

			super.execute();
			String locid = getPreviousView().getValueString("site.locationid");
			String cprojectid = getPreviousView().getValueString("projectlz.projectid");

			/*
			 * select sites that are valid for current LZ in project LZ for this Project
			 */

			Query query = XPersistence.getManager()
					.createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr " + " where pr.projectid = '"
							+ cprojectid + "'");
			List<LivelihoodZone> lzs = query.getResultList();
			String lzs1 = lzs.toString().replace("[]", " ");
			System.out.println("LZS = " + lzs + lzs.size() + lzs1);

			String inlist = "";
			for (int k = 0; k < lzs.size(); k++) {
				System.out.println("in loop 1 " + lzs.get(k));
				inlist += "'" + lzs.get(k) + "'";
				if (k + 1 == lzs.size()) {
					break;
				}
				inlist += ",";
				System.out.println(k);

			}

			/* Need to remove sites already used in this project */
			/* Select Clocation from Community where CProject = this projectid */

			Query cquery = XPersistence.getManager()
					.createQuery("select c from Community c" + " where c.projectlz = '" + cprojectid + "'");
			// System.out.println("done query");
			List<Community> com = cquery.getResultList();
			// System.out.println("done list get");

			String alist = "";

			for (int j = 0; j < com.size(); j++) {

				alist += "'" + com.get(j).getSite().getLocationid() + "'";
				if (j + 1 == com.size())
					break;
				alist += ",";

			}

			System.out.println("inlist = " + inlist);

			if (lzs.isEmpty() && com.isEmpty()) /* No existing Sites/LZS in this project */
			{
				// System.out.println("In setbasecondition 1");
				addWarning("No Livelihood Zones or Sites for current Project");
				getTab().setBaseCondition("${locationid} = '" + locid + "'");
			} else if (com.isEmpty()) /* No previously used sites in this project - show all sites from this proj */
			{
				// System.out.println("In setbasecondition 2");
				getTab().setBaseCondition(
						"${locationid} != '" + locid + "'" + " and ${livelihoodZone.lzid} in (" + inlist + ")");
				// getTab().setBaseCondition("${locationid} != '" + locid);
			} else /* Show all relevant sites */
			{
				// #501 removed not in from alist
				// System.out.println("In setbasecondition 4 query = "+"${locationid} != '" +
				// locid + "'" + " and ${livelihoodZone.lzid} in ("
				// + inlist + ")" + "and ${locationid} not in ( " + alist + ")");
				getTab().setBaseCondition("${locationid} != '" + locid + "'" + " and ${livelihoodZone.lzid} in ("
						+ inlist + ")" + "and ${locationid} in ( " + alist + ")");
				// getTab().setBaseCondition("${locationid} != '" + locid);
			
			}

		}

		if (!isInOHEA) {
			System.out.println("isInOIHM");
			Project project = null;
			String condition = "'";

			

			String projectId = getView().getValue("projectlz.projectid").toString();
			

			try {
				project = XPersistence.getManager().find(Project.class, projectId);
				System.out.println("project =  " + project.getProjecttitle());
			} catch (Exception ex) {
				System.out.println("Failed to find Project " + ex);
				// failed to find a project

			}
			final Project lzproject = project;
			
			Object currentSite = getView().getValue("site.locationid");

			System.out.println("current site from view = " + currentSite);

			super.execute();

			

			for (LivelihoodZone livelihoodZone : project.getLivelihoodZone()) {
				for (Site site : livelihoodZone.getSite()) {

					// #501

					// #501 remove restriction
					// if (site.getStudy().size() > 0) {

					// System.out.println("current site = "+currentSite);
					// System.out.println("this is = "+site.getLocationid());

					/* Do not need to include current site in list */
					if (currentSite == null) {
						condition += site.getLocationid() + "','";
					}

					else if (!currentSite.equals(site.getLocationid())) {
						condition += site.getLocationid() + "','";
					}
				}

			}
			if (condition.length() == 1) // nothing found
			{
				System.out.println("condition unset");
				getTab().setBaseCondition("${locationid} = '0'");
			} else {

				condition = StringUtils.chop(condition);
				condition = StringUtils.chop(condition);
			
				getTab().setBaseCondition("${locationid} in (" + condition + ")");
			}

			// getTab().setBaseCondition("${country.isocountrycode} !=
			// '"+project.getAltCurrency().getIsocountrycode().toString()+"'");

		}
	}
}