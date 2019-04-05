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

		// get database record and compare
		Household hh = XPersistence.getManager().find(Household.class, currentRecord);

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
		
	
		
		/*
		 * 
		 * 
		 * System.out.println("in on change status hh");
		 * removeActions("SetEditable.SetEditableHH");
		 * 
		 * 
		 * EntityManager em = XPersistence.createManager();
		 * 
		 * Household hhint = (Household) getView().getEntity(); // Displayed WGI
		 */
		/* need to get original status value from database */
		/*
		 * String hhiid = hhint.getId();
		 * 
		 * 
		 * Query querywgi = em.createQuery("select status from Household where id = '" +
		 * hhiid + "'"); Object hhstatus = querywgi.getSingleResult();
		 * 
		 */
		/*
		 * If changed to Validated, disallow and reset to previous Need to use 'Validate
		 * Asset Data to set to Validate
		 */
		/*
		 * if (getNewValue().toString().equals("Validated") && hhstatus.toString() !=
		 * "Validated") {
		 * addError("To set Household to Validated run Validate Asset Data"); //
		 * hhint.setStatus((Status) hhstatus); //getView().refresh(); }
		 * 
		 * if(hhint.getStatus() == (Status.Validated)) {
		 * System.out.println("set to read only in On Change 3");
		 * 
		 * getView().setEditable(false); //getView().setViewName("ReadOnly");
		 * //getView().displayAsDescriptionsListAndReferenceView();
		 * addActions("SetEditable.SetEditableHH"); }
		 * 
		 * 
		 * 
		 * try { // not working - still have hex value
		 * System.out.println("sub in change section = "+getView().getActiveSection());
		 * //getView().setHidden("resourceSubType", true); } catch (Exception ex) {
		 * System.out.println("Exception thrown  :" + ex); }
		 * 
		 * //getView().getSectionView(3).getAllValues().
		 * 
		 * 
		 * em.close();
		 * 
		 */
	}

}
