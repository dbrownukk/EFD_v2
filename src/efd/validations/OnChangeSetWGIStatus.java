package efd.validations;

import java.math.*;
import java.util.*;

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

public class OnChangeSetWGIStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		removeActions("SetEditable.SetEditable");
		System.out.println("In change for WGIstatus");

		//EntityManager em = XPersistence.createManager();

		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity(); // Displayed WGI

		/* need to get original status value from database */

		// String wgiid = wgint.getWgiid();
		// Query querywgi = em.createQuery("select status from WealthGroupInterview
		// where wgiid = '" + wgiid + "'");
		// Object wgistatus = querywgi.getSingleResult();

		/*
		 * If changed to Validated, disallow and reset to previous Need to use 'Validate
		 * Asset Data to set to Validate
		 */

		if (getNewValue().toString().equals("Validated") && wgint.getStatus().toString() != "Validated") {
			addError("To set Wealthgroup to Validated run Validate Asset Data");
			wgint.setStatus(wgint.getStatus());
			getView().refresh();
		}

		if (wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated)) {
			System.out.println("set to read only in On Change 3");

			getView().setEditable(false);
			// getView().setViewName("ReadOnly");
			// getView().displayAsDescriptionsListAndReferenceView();
			addActions("SetEditable.SetEditable");
		}

		//em.close();

	}

}
