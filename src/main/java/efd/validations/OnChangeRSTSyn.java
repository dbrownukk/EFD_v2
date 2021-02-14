package efd.validations;

/*
 * if this record is a synonym for a parent RST then show parent RST KCal and set to read only
 * 
 */

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class OnChangeRSTSyn extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		System.out.println("in rstparentid xx");
		System.out.println("all vals = " + getView().getAllValues().toString());

		String parentRSTid = getView().getValueString("resourcesubtypesynonym.idresourcesubtype");

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
