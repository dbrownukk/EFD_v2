/*
 * 
 * Expandability Rule RST On Change 
 * 
 * DRB 7/1/2020
 * 
 * Set defaults and hide/unhide based on RT
 */

package efd.validations;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class OnChangeRSTExpandability extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		Object rstid = getNewValue();
		if (rstid == null) {
			return;
		}

		System.out.println("new value rst = " + rstid);

		ResourceSubType rst = XPersistence.getManager().find(ResourceSubType.class, rstid.toString());

		System.out.println("rst type = " + rst.getResourcetype().getResourcetypename());
		String rtName = rst.getResourcetype().getResourcetypename();
		if (rtName.contains("Livestock")) {
			System.out.println("Livestock Sale ");
			//getView().setHidden("expandabilityLimit", false);
			//getView().setHidden("expandabilityIncreaseLimit", false);
			getView().setValue("expandabilityLimit", 25);
			getView().setValue("expandabilityIncreaseLimit", 200);
			getView().setEditable("expandabilityLimit", false);
			getView().setEditable("expandabilityIncreaseLimit", false);

		} else if (rtName.equals("Crops") || rtName.equals("Wild Foods") || rtName.equals("Employment") || rtName.equals("Transfers")) {
			System.out.println("No need for expand values ");
			//getView().setHidden("expandabilityLimit", true);
			//getView().setHidden("expandabilityIncreaseLimit", true);
			getView().setValue("expandabilityLimit", 0);
			getView().setValue("expandabilityIncreaseLimit", 0);
		} else {
			System.out.println("Not  ");
			//getView().setHidden("expandabilityLimit", false);
			//getView().setHidden("expandabilityIncreaseLimit", false);
			getView().setValue("expandabilityLimit", 0);
			getView().setValue("expandabilityIncreaseLimit", 0);
			getView().setEditable("expandabilityLimit", true);
			getView().setEditable("expandabilityIncreaseLimit", true);
		}

	}
}
