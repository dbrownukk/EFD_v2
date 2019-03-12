package efd.actions;

import org.openxava.actions.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class EditQuestionUse extends EditElementInCollectionAction  {

	public void execute() throws Exception {

		super.execute();
		
		
		System.out.println("in EditQuestionUse collection edit "+getCollectionElementView().getAllValues());
		//getCollectionElementView().getSubview("configQuestion").setEditable(true);
		
		
		//getCollectionElementView().getSubview("configQuestion").setHidden("configQuestion.ageRangeLower", true);
		
		getCollectionElementView().setEditable("study", false);
		//getCollectionElementView().setEditable("level", false);
		
		
		
		
		
		
		
		System.out.println("in EditQuestion level = "+getCollectionElementView().getValue("configQuestion.level"));
		
		
		
		
		if (getCollectionElementView().getValue("configQuestion.level").equals(ConfigQuestion.Level.HouseholdMember)) {
			System.out.println("edit quse show HH gender and ages");
			getCollectionElementView().setHidden("configQuestion.gender", false);
			getCollectionElementView().setHidden("configQuestion.ageRangeLower", false);
			getCollectionElementView().setHidden("configQuestion.ageRangeUpper", false);

		}

		else {
			System.out.println("edit quse hide HH gender and ages "+getCollectionElementView().getBaseModelName()+" "+getCollectionElementView().getValueString("configQuestion.level"));
			getView().setHidden("configQuestion.gender", true);
			getCollectionElementView().setHidden("configQuestion.gender", true);
			getCollectionElementView().setHidden("ageRangeLower", true);
			getCollectionElementView().setHidden("ageRangeUpper", true);

		}
		
		System.out.println("commented out level - now do answertype");
		
		AnswerType answerType = (AnswerType) getCollectionElementView().getValue("configQuestion.answerType");
		
		if (answerType.equals(efd.model.ConfigQuestion.AnswerType.IntegerRange))
			// get integer range

			{
				System.out.println("a10101");
				getCollectionElementView().setHidden("configQuestion.intRangeLower", false);
				getCollectionElementView().setHidden("configQuestion.intRangeUpper", false);
				getCollectionElementView().setHidden("configQuestion.decRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.decRangeUpper", true);
				getCollectionElementView().setHidden("configQuestion.questionLOV", true);

			}

			else if (answerType.equals(efd.model.ConfigQuestion.AnswerType.DecimalRange)) {
				System.out.println("a20202");
				getCollectionElementView().setHidden("configQuestion.decRangeLower", false);
				getCollectionElementView().setHidden("configQuestion.decRangeUpper", false);
				getCollectionElementView().setHidden("configQuestion.intRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.intRangeUpper", true);
				getCollectionElementView().setHidden("configQuestion.questionLOV", true);

			}

			else if (answerType.equals(efd.model.ConfigQuestion.AnswerType.LOV)) {
				System.out.println("a30303");
				getCollectionElementView().setHidden("configQuestion.questionLOV", false);
				getCollectionElementView().setHidden("configQuestion.decRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.decRangeUpper", true);
				getCollectionElementView().setHidden("configQuestion.intRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.intRangeUpper", true);

			}

			else if (answerType.equals(efd.model.ConfigQuestion.AnswerType.Decimal)
					|| answerType.equals(efd.model.ConfigQuestion.AnswerType.Integer)
					|| answerType.equals(efd.model.ConfigQuestion.AnswerType.Text)) {
				System.out.println("a40404");
				getCollectionElementView().setHidden("decRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.decRangeUpper", true);
				getCollectionElementView().setHidden("configQuestion.intRangeLower", true);
				getCollectionElementView().setHidden("configQuestion.intRangeUpper", true);
				getCollectionElementView().setHidden("configQuestion.questionLOV", true);

			}
		
	
		
		
	}

}
