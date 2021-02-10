package efd.validations;

import org.openxava.actions.*;


import efd.rest.domain.model.*;

public class InitQuestionAnswerType extends ViewBaseAction {
	public void execute() throws Exception {

		

		// getView().refresh();

		// System.out.println("init members Name = "+getView().getMembersNames());
		// System.out.println("init model name = "+getView().getModelName());
		// System.out.println("init answer type =
		// "+getView().getValue("configQuestion.answerType"));

		System.out.println("view values  = " +getView().getValues());
		
		Object answerType = null;

		if (getView().getModelName().equals("ConfigQuestionUse")) {
			answerType = getView().getValue("configQuestion.answerType");
			System.out.println("init 1 answer type = " +answerType);
			
		} else if (getView().getModelName().equals("ConfigQuestion")) {
			answerType = getView().getValue("answerType");
			System.out.println("init 2 answer type = " + answerType.toString());
		}
		// trap null pointer exception if called from configQuestionUse, but needed for
		// the onchange of answerType
		try {

			if (answerType.equals(ConfigQuestion.AnswerType.IntegerRange))
			// get integer range
			{
				System.out.println("111");
				getView().setHidden("intRangeLower", false);
				getView().setHidden("intRangeUpper", false);
			} else {
				System.out.println("222");
				getView().setHidden("intRangeLower", true);
				getView().setHidden("intRangeUpper", true);
			}

			if (answerType.equals(ConfigQuestion.AnswerType.DecimalRange)) {
				System.out.println("333 "+getView().getModelName());
				getView().setHidden("decRangeLower", false);
				getView().setHidden("decRangeUpper", false);
			} else {
				System.out.println("444");
				getView().setHidden("decRangeLower", true);
				getView().setHidden("decRangeUpper", true);
			}
		
			//super.execute();
			
			getView().setHidden("intRangeLower", true);
			getView().setHidden("intRangeUpper", true);
			getView().setHidden("configQuestionUse.intRangeLower", true);
			getView().setHidden("configQuestionUse.intRangeUpper", true);
			getView().setHidden("configQuestion.intRangeLower", true);
			getView().setHidden("configQuestion.intRangeUpper", true);
			
			getView().setHidden("decRangeLower", true);
			getView().setHidden("decRangeUpper", true);
			getView().setHidden("configQuestionUse.decRangeLower", true);
			getView().setHidden("configQuestionUse.decRangeUpper", true);
			getView().setHidden("configQuestion.decRangeLower", true);
			getView().setHidden("configQuestion.decRangeUpper", true);
			
			
			
		} catch (Exception e) {
			System.out.println("initQuest exception "+e);
			return;
		}

	}

	

}
