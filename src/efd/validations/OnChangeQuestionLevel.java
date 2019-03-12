package efd.validations;

import java.math.*;

import javax.inject.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class OnChangeQuestionLevel extends OnChangePropertyBaseAction {

	@Inject
	private String studyid;
	@Inject
	private String configuseid;
	@Inject
	private String questionid;

	public void execute() throws Exception {

		String level;

		if (getNewValue() == null) {
			System.out.println("questionlevel onchange = null" + getNewValue());
			return;
		}

		level = getView().getValue("level").toString();

		System.out.println("aaa question id = " + questionid);
		System.out.println("ccc configuseid id  = " + configuseid);

		/*   March 2019 - no longer using configQuestionUse Level 
		if (!configuseid.isEmpty()) {
			System.out.println("in configuseid SET  = " + configuseid);
			ConfigQuestionUse configQuestionUse = XPersistence.getManager().find(ConfigQuestionUse.class, configuseid);

			if (level.equals("Household"))
				configQuestionUse.setLevel(Level.Household); // set configQuestionUse Level to be same as Question level
			else if (level.equals("Household Member"))
				configQuestionUse.setLevel(Level.HouseholdMember);
			else if (level.equals("Study"))
				configQuestionUse.setLevel(Level.Study);

			XPersistence.getManager().persist(configQuestionUse);
		}
		*/

		System.out.println("New changed level  to = " + getNewValue().toString());
		System.out.println("level = " + getView().getValueString("level"));
		System.out.println("config q level = " + getView().getAllValues().toString());

		if (getNewValue().equals(ConfigQuestion.Level.HouseholdMember)) {
			System.out.println("show HH gender and ages");
			getView().setHidden("gender", false);
			getView().setHidden("ageRangeLower", false);
			getView().setHidden("ageRangeUpper", false);

		}

		else {
			System.out.println("hide HH gender and ages");
			getView().setHidden("gender", true);
			getView().setHidden("ageRangeLower", true);
			getView().setHidden("ageRangeUpper", true);

		}

	}
}
