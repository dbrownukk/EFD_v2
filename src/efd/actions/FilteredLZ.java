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

		if (efdModel == "OHEA" || efdModel == "OIHM") {
			/* OIHM Site must now behave same as OHEA see Issue 352  */

		

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

		

			getTab().setBaseCondition("${lzid} in (" + inlist + ")");
		}
/*
		if (efdModel == "OIHM") {
			
			super.execute();
		

		}
*/
	}

}
