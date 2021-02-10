package efd.validations;

import org.openxava.actions.*;

import efd.rest.domain.model.ConfigQuestion.*;

public class OnChangeQuestionLevel extends OnChangePropertyBaseAction {

	public void execute() throws Exception {

		String level = null;

		if (getNewValue() == null) {

			// New - set defaults

			System.out.println("questionlevel onchange = null" + getNewValue());
			setNewValue(Level.Study);
			getView().setValue("level", Level.Study);
			getView().setValue("answerType", AnswerType.Text);

			getView().setHidden("questionLOVType", false);
			getView().setHidden("intRangeLower", false);
			getView().setHidden("intRangeUpper", false);
			getView().setHidden("decRangeLower", false);
			getView().setHidden("decRangeUpper", false);

		}

		if (getNewValue().equals(Level.HouseholdMember)) {
			System.out.println("show HH gender and ages b");
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
