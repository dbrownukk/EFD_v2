package efd.validations;

import org.openxava.actions.*;
import org.openxava.jpa.*;


import efd.rest.domain.model.*;
import efd.rest.domain.model.WealthGroupInterview.*;

/*
 * Prevent Status change to Validated by User using LOV
 * If Set to Validated then make fields readonly and add button which is used to set to Editable - could be controlled by Role
 * DRB 16/8/18
 */

public class OnChangeSetHHStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		System.out.println("in onchangeHHstatus 1");
		
		if (getView().getValue("id") == null) { // do not fire for New HH
			//System.out.println("all vals =  = "+getView().getAllValues());
			
			//getView().getSubview("wildFood").setValue("unitsProduced", 0.0);
			
		//	System.out.println("section 12= "+getView().getSectionView(12).getViewName());
		//	System.out.println("section 13= "+getView().getSectionView(13).getViewName());
		//	System.out.println("section 14= "+getView().getSectionView(14).getViewName());
		System.out.println("returning from onchangeHHstatus");
			return;
		}
		System.out.println("in onchangeHHstatus 2");

		Object currentRecord = getView().getValue("id");
		System.out.println("hh id = "+currentRecord);
		
		// get database record and compare
		Household hh = XPersistence.getManager().find(Household.class, currentRecord);

		System.out.println("hh is "+hh.getHouseholdNumber());
		
		if (hh.getStatus().equals(Status.Validated)) {

			getView().setEditable(false);
			addActions("SetEditable.SetEditableHH");
			return; // already Validated
		} else {
			removeActions("SetEditable.SetEditableHH");

		}

		Object status = getNewValue();
		if (status.equals(Status.Validated)) {
			addError("To set Household to Validated run Validate Asset Data");
			getView().setValue("status", hh.getStatus());
		}
		
	}

}
