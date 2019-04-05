package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class HouseholdMemberSave extends SaveElementInCollectionAction {


	public void execute() throws Exception {

		// set the resource type in characteristic resources for use in spreadsheet create
		
		System.out.println("in HHM Save");
		
		String hhid = getPreviousView().getValueString("id");
		Household hhm = XPersistence.getManager().find(Household.class, hhid);
		
		int nextNumberInHH = hhm.getHouseholdMember().size() + 1;
		
		System.out.println("hhm prepersist num = "+nextNumberInHH);

		getView().setValue("householdMember.householdMemberNumber","HHM" + nextNumberInHH);
		
		
		super.execute();
		

		
		
		
	}

}
