package efd.validations;

import java.math.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;


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


public class OnChangeSetWGIStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		
		removeActions("SetEditable.SetEditable");
		System.out.println("In change for WGIstatus");

		EntityManager em = XPersistence.createManager();

		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity(); // Displayed WGI

		/* need to get original status value from database */

		String wgiid = wgint.getWgiid();
		Query querywgi = em.createQuery("select status from WealthGroupInterview where wgiid = '" + wgiid + "'");
		Object wgistatus = querywgi.getSingleResult();

	
		/* If changed to Validated, disallow and reset to previous 
		 * Need to use 'Validate Asset Data to set to Validate */

		if (getNewValue().toString().equals("Validated") && wgistatus.toString() != "Validated") {
			addError("To set Wealthgroup to Validated run Validate Asset Data");
			wgint.setStatus((Status) wgistatus);
			getView().refresh();
		}
		
		if(wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{
			System.out.println("set to read only in On Change");
			getView().setEditable(false);
			addActions("SetEditable.SetEditable");
		}
		
		System.out.println("sub in change - section = "+getView().getSectionView(3).isCollectionEditable());
		//getView().getSectionView(0).setHidden("resourceSubType", true);
		
	
		
		em.close(); 
	}

}
