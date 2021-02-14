
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import efd.model.*;

public class AddLZ extends GoAddElementsToCollectionAction {

	public void execute() throws Exception {

		super.execute();

		
		
		String cprojectid = getView().getValue("projectid").toString();

		if(cprojectid.isEmpty())
		{
			System.out.println("proj not saved");
		}
		
		Query query = XPersistence.getManager().createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr "
				+ " where pr.projectid = '" + cprojectid + "'");
		List<LivelihoodZone> lzs = query.getResultList();


		String inlist = "";
		
		for (int k = 0; k < lzs.size(); k++) {

			inlist += "'" + lzs.get(k) + "'";
			if (k + 1 == lzs.size())
				break;
			inlist += ",";

		}
		if (lzs.size() > 0)
			getTab().setBaseCondition("${lzid} not in (" + inlist + ")");

	}
	
	
	
	

}
