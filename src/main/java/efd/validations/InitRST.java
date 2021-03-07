package efd.validations;

/*
 * If an RST Syn is used then the KCal is set to parent KCal value
 * 
 */


import java.math.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.view.*;


import com.openxava.naviox.*;
import com.openxava.naviox.model.*;


import efd.model.*;
import efd.model.WealthGroupInterview.*;

public class InitRST extends ViewBaseAction {
	

	
	public void execute() throws Exception {

		
		
		
		System.out.println("in init RST ");
		String parentRSTid = getView().getValueString("resourcesubtypesynonym.idresourcesubtype");
		//System.out.println("in init RST 2 "+parentRSTid);
		
		if (!parentRSTid.isEmpty()) {
			ResourceSubType parentRST = XPersistence.getManager().find(ResourceSubType.class, parentRSTid);

			getView().setValue("resourcesubtypekcal", parentRST.getResourcesubtypekcal());
			getView().setValue("resourcesubtypeunit", parentRST.getResourcesubtypeunit());

			getView().setEditable("resourcesubtypekcal", false);
			getView().setEditable("resourcesubtypeunit", false);
		} else {
			getView().setEditable("resourcesubtypekcal", true);
			getView().setEditable("resourcesubtypeunit", true);
		}
		
	}
}
