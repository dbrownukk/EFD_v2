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


public class OnChangeSetWGIStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		System.out.println("In change for ss");

		EntityManager em = XPersistence.createManager();

		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity(); // Displayed WGI

		/* need to get original status value from database */

		String wgiid = wgint.getWgiid();
		Query querywgi = em.createQuery("select status from WealthGroupInterview where wgiid = '" + wgiid + "'");
		Object wgistatus = querywgi.getSingleResult();

		System.out.println("XPers singleresult wgistatus =    " + wgistatus.toString());

		System.out.println("New Value =  " + getNewValue());

		/* If changed to Validated, disallow and reset to previous */

		if (getNewValue().toString().equals("Validated")) {
			addError("To set Wealthgroup to Validated run Validate Asset Data");
			wgint.setStatus((Status) wgistatus);
			getView().refresh();
		}
		Role myrole = Role.findJoinedRole();
		
		
		
		
		em.close(); 
	}

}
