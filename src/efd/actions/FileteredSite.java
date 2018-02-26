package efd.actions;

import java.util.List;

import javax.mail.*;
import javax.persistence.*;

import org.apache.commons.validator.*;
import org.hibernate.mapping.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.model.*;

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

public class FileteredSite extends ReferenceSearchAction {

	public void execute() throws Exception {

		// System.out.println("current proj "
		// +getView().getValue("projectlz.projectid"));
		// System.out.println("current site "
		// +getView().getValue("site.locationid"));

		// System.out.println("In Site Filter from Comm
		// "+getView().getSearchKeyName());

		/* need to set project context */
		if (getView().getValueString("projectlz.projectid").isEmpty()) {
			throw new IllegalStateException(XavaResources.getString("Site lookup must be in context of a Project"));
		}

		// String locid =
		// getPreviousView().getValue("site.locationid").toString();
		// String cprojectid =
		// getPreviousView().getValue("projectlz.projectid").toString();
		super.execute();
		String locid = getPreviousView().getValueString("site.locationid");
		String cprojectid = getPreviousView().getValueString("projectlz.projectid");

		// System.out.println("locid = "+locid);
		// System.out.println("cprojectid = "+cprojectid);
		/*
		 * select sites that are valid for current LZ in project LZ for this
		 * Project
		 */

		Query query = XPersistence.getManager().createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr "
				+ " where pr.projectid = '" + cprojectid + "'");
		List<LivelihoodZone> lzs = query.getResultList();
		String lzs1 = lzs.toString().replace("[]", " ");
		// System.out.println("LZS = " + lzs + lzs.size() + lzs1);
		String inlist = "";
		for (int k = 0; k < lzs.size(); k++) {
			// System.out.println("in loop 1"+lzs.get(k));
			inlist += "'" + lzs.get(k) + "'";
			if (k + 1 == lzs.size())
				break;
			inlist += ",";
			// System.out.println(k);

		}

		/* Need to remove sites already used in this project */
		/* Select Clocation from Community where CProject = this projectid */

		Query Cquery = XPersistence.getManager()
				.createQuery("select c from Community c" + " where c.projectlz = '" + cprojectid + "'");
		// System.out.println("done query");
		List<Community> com = Cquery.getResultList();
		// System.out.println("done list get");

		String alist = "";

		for (int j = 0; j < com.size(); j++) {
			// System.out.println("in loop");

			// System.out.println("in loop2");
			// System.out.println("Comsiteid = "+com.get(j).getSite());
			// System.out.println("ComProjid = "+com.get(j).getProjectlz());

			// System.out.println("ComSite = "+com.get(j).getSite().toString());
			// System.out.println("in loop
			// 1"+com.get(j).getSite().getLocationid());
			alist += "'" + com.get(j).getSite().getLocationid() + "'";
			if (j + 1 == com.size())
				break;
			alist += ",";
			// System.out.println(j);
		}

		// System.out.println("inlist ="+inlist);
		// System.out.println("alist = "+alist);

		// getTab().setBaseCondition("${locationid} != '" + locid + "'" + " and
		// ${LZ} in (select lz.lzid from LivelihoodZone lz join lz.project pr "
		// + " where pr.projectid = '" + cprojectid + ")'");

		getTab().setBaseCondition("${locationid} != '" + locid + "'" + " and ${livelihoodZone.lzid} in (" + inlist + ")"
				+ "and ${locationid} not in ( " + alist + ")");

	}
}