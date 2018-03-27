package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class ResourceSubTypeFilter extends ReferenceSearchAction  {

	public void execute() throws Exception {
		
		String rst = null,rst2=null,rst3 = null;
		View vrst = null;
		int irst = 0;
		
	/* Filter Resource Sub Type to correct Resource Type for WealthGroupInterview element collection */

		
		try{
			
			
			irst = getView().getActiveSection();
			vrst = getView().getSectionView(irst);
			rst = vrst.getMemberName();
			rst2 = vrst.getMetaModel().getLabel();
			
			System.out.println("In Filter for RST Tabname = "+irst+rst+rst2+rst3);
			
			rst = getTab().getTabName().toString();
			
			System.out.println("In Filter for RST Tabname = "+rst);
			getTab().setBaseCondition("${resourcetype} in (select IDResourceType from ResourceType where ResourceTypeName = 'Livestock'");
		
		}
		catch (EmptyStackException|ElementNotFoundException ex)
		{
			super.execute();
			System.out.println("caught");
			//getTab().setBaseCondition("");
			return;
		}
	
		getTab().setBaseConditionForReference("${resourcetype} in (select IDResourceType from ResourceType where ResourceTypeName = 'Livestock'");
		System.out.println("basecondition = "+getTab().getBaseCondition().toString());
		super.execute();


		
	}

}
