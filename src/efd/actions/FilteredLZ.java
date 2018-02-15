package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class FilteredLZ extends ReferenceSearchAction  {

	public void execute() throws Exception {
		
		
		String cprojectid = getPreviousView().getValue("projectlz.projectid").toString();
		System.out.println("cprojectid = "+cprojectid);
		super.execute();

		Query query = XPersistence.getManager().createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr "
				+ " where pr.projectid = '" + cprojectid + "'");
		List<LivelihoodZone> lzs = query.getResultList();
		String lzs1 = lzs.toString().replace("[]", " ");
		System.out.println("LZS = " + lzs + lzs.size() + lzs1);
		String inlist ="";;
		for (int k = 0; k < lzs.size(); k++){
			System.out.println(lzs.get(k));
			inlist += "'"+lzs.get(k)+"'";
			if(k+1 == lzs.size()) break; 
			inlist += ",";
			System.out.println(k);
			
		}
		
		//System.out.println(inlist);
		
		//getTab().setBaseCondition("${locationid} != '" + locid + "'" + " and ${LZ} in (select lz.lzid from LivelihoodZone lz join lz.project pr "
		//+ " where pr.projectid = '" + cprojectid + ")'");
	
		getTab().setBaseCondition("${lzid} in (" + inlist + ")");
		
		
		
		
	}

}
