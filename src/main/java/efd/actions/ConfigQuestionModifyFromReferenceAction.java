package efd.actions;

import org.openxava.actions.*;

import efd.model.ConfigQuestion.*;



public class ConfigQuestionModifyFromReferenceAction extends SearchByViewKeyAction {



	public void execute() throws Exception {

		boolean exists = true;


		 String studyid;
		
		 String configuseid;
		
		 String questionid;
		
		configuseid = getPreviousView().getValueString("id");
		// studyid = getView().getValueString("study.id");
		questionid = getView().getValueString("id");

		System.out.println("in conquetmodidycall configuseid = " + configuseid);
		System.out.println("questionid = " + questionid);

		super.execute();

		System.out.println("in config mod level = " + getView().getValue("level"));
		Level level = (Level) getView().getValue("level");
		AnswerType  answerType = (AnswerType) getView().getValue("answerType");
		System.out.println("level domain = "+level.toString());
		
		if (answerType.equals(AnswerType.IntegerRange))
			// get integer range

			{
				System.out.println("10101c");
				getView().setHidden("intRangeLower", false);
				getView().setHidden("intRangeUpper", false);
				getView().setHidden("decRangeLower", true);
				getView().setHidden("decRangeUpper", true);
				getView().setHidden("questionLOVType", true);

			}

			else if (answerType.equals(AnswerType.DecimalRange)) {
				System.out.println("20202c");
				getView().setHidden("decRangeLower", false);
				getView().setHidden("decRangeUpper", false);
				getView().setHidden("intRangeLower", true);
				getView().setHidden("intRangeUpper", true);
				getView().setHidden("questionLOVType", true);

			}

			else if (answerType.equals(AnswerType.LOV)) {
				System.out.println("30303c");
				getView().setHidden("questionLOV", false);
				getView().setHidden("decRangeLower", true);
				getView().setHidden("decRangeUpper", true);
				getView().setHidden("intRangeLower", true);
				getView().setHidden("intRangeUpper", true);

			}

			else if (answerType.equals(AnswerType.Decimal)
					|| answerType.equals(AnswerType.Integer)
					|| answerType.equals(AnswerType.Text)) {
				System.out.println("40404c");
				getView().setHidden("decRangeLower", true);
				getView().setHidden("decRangeUpper", true);
				getView().setHidden("intRangeLower", true);
				getView().setHidden("intRangeUpper", true);
				getView().setHidden("questionLOVType", true);

			}
		
		
		if (level.equals(Level.HouseholdMember)) {
			System.out.println("show HH gender and ages");
			getView().setHidden("gender", false);
			getView().setHidden("ageRangeLower", false);
			getView().setHidden("ageRangeUpper", false);

		}

		else {
			System.out.println("hide HH gender and ages c");
			getView().setHidden("gender", true);
			getView().setHidden("ageRangeLower", true);
			getView().setHidden("ageRangeUpper", true);

		}
		

	}
}