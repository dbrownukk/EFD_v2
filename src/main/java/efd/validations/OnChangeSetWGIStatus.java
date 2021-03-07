package efd.validations;

import java.util.*;

import org.hibernate.query.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

/*
 * Prevent Status change to Validated by User using LOV
 * If Set to Validated then make fields readonly and add button which is used to set to Editable - could be controlled by Role
 * DRB 16/8/18
 */

public class OnChangeSetWGIStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		
		System.out.println("In change for WGIstatus");
		
		removeActions("SetEditable.SetEditable");
	

		//EntityManager em = XPersistence.createManager();

		//WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity(); // Displayed WGI
		
		
		
		
		/* need to get original status value from database */
		String wgiid = getView().getValueString("wgiid");
		//String wgiid = wgint.getWgiid();
		
		//WealthGroupInterview wgidb = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);

		javax.persistence.Query query = XPersistence.getManager()
				.createQuery(
						"from WealthGroupInterview where wgiid = :wgiid");
		query.setParameter("wgiid", wgiid);

		List resultList = query.getResultList();		
		
		WealthGroupInterview wgidb = (WealthGroupInterview) resultList.get(0);
		
		
		
		
		// System.out.println("existing status = "+wgidb.getStatus());
		/*
		 * If changed to Validated, disallow and reset to previous Need to use 'Validate
		 * Asset Data to set to Validate
		 */


		if (getNewValue().toString().equals("Validated") && wgidb.getStatus().toString() != "Validated") {
			addError("To set Wealthgroup to Validated run Validate Asset Data");
			//wgidb.setStatus(wgidb.getStatus());
			getView().refresh();
		}

		if (wgidb.getStatus() == (efd.model.WealthGroupInterview.Status.Validated)) {

			getView().setEditable(false);
			// getView().setViewName("ReadOnly");
			// getView().displayAsDescriptionsListAndReferenceView();
			addActions("SetEditable.SetEditable");
		}

		//em.close();

	}

}
