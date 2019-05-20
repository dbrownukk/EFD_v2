
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;


import efd.model.*;

public class RSUaddhhmanytomany extends GoAddElementsToCollectionAction {

	public void execute() throws Exception {

		
		System.out.println("in RSU add HH");
		super.execute();
		
		String studyid = getPreviousView().getValueString("study.id");
		String rsuid = getPreviousView().getValueString("id");
		Map allValues = getCollectionElementView().getAllValues();
		Map allValues2 = getPreviousView().getAllValues();
		
		System.out.println("all vals  = "+allValues);
		System.out.println("all vals  = "+allValues2);
		
		
		
		if(studyid.isEmpty())
		{
			addError("Select a Study before adding Households");
			
		}
		
		/* Which HH are already in RSU */
		ReportSpecUse rsu = XPersistence.getManager().find(ReportSpecUse.class, rsuid);
		
		String excludehh = "";
		for (Household household : rsu.getHousehold()) {
			excludehh += household.getId()+",";
			System.out.println("exclude "+excludehh);
		}
		excludehh = StringUtils.chop(excludehh);
		
		
		
		// Exclude not working so far..
		//String condition = "${study} =" + "'"+studyid+"'"+" and ${household.id} not in ('"+excludehh+"')";
		String condition = "${study} =" + "'"+studyid+"'";
		System.out.println("condition = "+condition);

		getTab().setPropertiesNames("householdNumber,householdName,interviewers,interviewDate");
		getTab().setBaseCondition(condition );
		
		
	
	}
	
	
	
	

}
