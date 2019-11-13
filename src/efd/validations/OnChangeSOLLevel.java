package efd.validations;

import java.math.*;

import javax.inject.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class OnChangeSOLLevel extends OnChangePropertyBaseAction {

	@Inject
	private String studyid;

	public void execute() throws Exception {

		System.out.println("in SOL level onchange");
		String level = null;

		
		
		if (getNewValue() == null) {

			// New - set defaults

			System.out.println("SOL level onchange = null" + getNewValue());
			setNewValue(Level.Study);
			getView().setValue("level", Level.Study);

		}

		System.out.println("about to get level 3434" + getView().getAllValues());

		System.out.println("level in onchange question level 4545 = " + level);

		System.out.println("New changed level  to = " + getNewValue().toString());
		System.out.println("level = " + getView().getValueString("level"));
		System.out.println("config q level = " + getView().getAllValues().toString());

		if (getNewValue().equals(StdOfLivingElement.StdLevel.HouseholdMember)) {
			// if(getView().getValue("level").equals(StdOfLivingElement.StdLevel.HouseholdMember))
			// {
			System.out.println("show HH gender and ages b");
			getView().setHidden("gender", false);
			getView().setHidden("ageRangeLower", false);
			getView().setHidden("ageRangeUpper", false);
			if (getView().getValue("gender") == null)
				getView().setValue("gender", StdOfLivingElement.Gender.Both);
			if (getView().getValue("ageRangeLower") == null)
				getView().setValue("ageRangeLower", 0);
			if (getView().getValue("ageRangeUpper") == null)
				getView().setValue("ageRangeUpper", 110);

		}

		else {
			System.out.println("hide HH gender and ages");
			getView().setHidden("gender", true);
			getView().setHidden("ageRangeLower", true);
			getView().setHidden("ageRangeUpper", true);

		}

	}
}
