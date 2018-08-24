
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class AddLZ extends GoAddElementsToCollectionAction {

	public void execute() throws Exception {

		super.execute();

		String cprojectid = getView().getValue("projectid").toString();
		Query query = XPersistence.getManager().createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr "
				+ " where pr.projectid = '" + cprojectid + "'");
		List<LivelihoodZone> lzs = query.getResultList();

		System.out.println(lzs);

		String lzs1 = lzs.toString().replace("[]", " ");

		String inlist = "";
		;
		for (int k = 0; k < lzs.size(); k++) {

			inlist += "'" + lzs.get(k) + "'";
			if (k + 1 == lzs.size())
				break;
			inlist += ",";

		}

		getTab().setBaseCondition("${lzid} not in (" + inlist + ")");

	}

}
