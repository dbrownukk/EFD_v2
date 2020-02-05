package efd.validations;

import java.math.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.view.*;

import com.openxava.naviox.*;
import com.openxava.naviox.model.*;
import com.openxava.naviox.util.*;

import com.openxava.phone.web.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;

/*
 * Prevent Status change to Validated by User using LOV
 * If Set to Validated then make fields readonly and add button which is used to set to Editable - could be controlled by Role
 * DRB 16/8/18
 */

public class OnChangeSetHHStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		System.out.println("in onchangeHHstatus 1");
		
		if (getView().getValue("id") == null) // do not fire for New HH
			return;
		
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
