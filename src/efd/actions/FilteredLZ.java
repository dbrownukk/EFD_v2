package efd.actions;

import java.util.*;

import javax.inject.*;
import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class FilteredLZ extends ReferenceSearchAction {

	// detect if OIHM
	// if so then the LZ should not be in Country of parent Project of the Study

	@Inject
	private String efdModel;

	public void execute() throws Exception {

		System.out.println("model = " + efdModel);

		if (efdModel == "OHEA") {
			/* If in standalone Site the filter is not needed */

			System.out.println("In Filter1 " + getView().getMembersNames());
			System.out.println("In Filter2 " + getView().getBaseModelName());

			// System.out.println("vnames"+getView().);

			try {
				getPreviousView().getValue("projectlz.projectid");
				System.out.println("In Filter2 " + getView().getBaseModelName());
			} catch (EmptyStackException | ElementNotFoundException ex) {
				super.execute();
				System.out.println("caught");
				getTab().setBaseCondition("");
				return;
			}

			String cprojectid = getPreviousView().getValue("projectlz.projectid").toString();
			System.out.println("In LZ Filter cprojectid = " + cprojectid);
			super.execute();

			Query query = XPersistence.getManager()
					.createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr " + " where pr.projectid = '"
							+ cprojectid + "'");
			List<LivelihoodZone> lzs = query.getResultList();
			String lzs1 = lzs.toString().replace("[]", " ");
			System.out.println("LZS = " + lzs + lzs.size() + lzs1);
			String inlist = "";
			;
			for (int k = 0; k < lzs.size(); k++) {
				System.out.println(lzs.get(k));
				inlist += "'" + lzs.get(k) + "'";
				if (k + 1 == lzs.size())
					break;
				inlist += ",";
				System.out.println(k);

			}

			// System.out.println(inlist);

			// getTab().setBaseCondition("${locationid} != '" + locid + "'" + " and ${LZ} in
			// (select lz.lzid from LivelihoodZone lz join lz.project pr "
			// + " where pr.projectid = '" + cprojectid + ")'");

			getTab().setBaseCondition("${lzid} in (" + inlist + ")");
		}

		if (efdModel == "OIHM") {
			// LZ in SIte in a Study in a Project
			
			//String projectId = getPreviousView().getValue("projectlz.projectid").toString();
			//Project project = XPersistence.getManager().find(Project.class, projectId);
			
			//System.out.println("project country = "+project.getAltCurrency().getIsocountrycode());
			//System.out.println("project country = "+project.getAltCurrency().getIdcountry());
			
			super.execute();
			//getTab().setBaseCondition("${country.isocountrycode} != '"+project.getAltCurrency().getIsocountrycode().toString()+"'");

		}

	}

}
