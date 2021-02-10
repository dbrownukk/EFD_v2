package efd.validations;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class OnChangeAge extends OnChangePropertyBaseAction {

	public void execute() throws Exception {

		System.out.println("in new age onchange 1");

		if (getNewValue() == null || getNewValue().toString() == "0")
			return;

		int newAge = (int) getNewValue();


		try {
			Object hhid = getPreviousView().getValue("id");
			Household hhm = XPersistence.getManager().find(Household.class, hhid);

			int refYear = hhm.getStudy().getReferenceYear();

			int yearOfBirth = (refYear - newAge);

			getView().trySetValue("yearOfBirth", yearOfBirth);
			
		} catch (Exception ex) {
			System.out.println("ex");
			return;
		}

	}

}
