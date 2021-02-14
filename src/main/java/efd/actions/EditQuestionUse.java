package efd.actions;

import org.openxava.actions.*;

import efd.model.ConfigQuestion.*;

public class EditQuestionUse extends EditElementInCollectionAction  {

	public void execute() throws Exception {

		super.execute();
		
		
		System.out.println("in EditQuestionUse collection edit "+getCollectionElementView().getAllValues());
		//getCollectionElementView().getSubview("configQuestion").setEditable(true);
		
		
		//getCollectionElementView().getSubview("configQuestion").setHidden("configQuestion.ageRangeLower", true);
		
		getCollectionElementView().setEditable("study", false);
		//getCollectionElementView().setEditable("level", false);
		
		
		
		System.out.println("sub = "+getCollectionElementView().getSubview("configQuestion").getAllValues());
		
		
		
		System.out.println("in EditQuestion level = "+getCollectionElementView().getValue("configQuestion.level"));
		
		
		
		
		if (getCollectionElementView().getValue("configQuestion.level").equals(Level.HouseholdMember)) {
			System.out.println("edit quse show HH gender and ages");
			getCollectionElementView().getSubview("configQuestion").setHidden("gender", false);
			getCollectionElementView().getSubview("configQuestion").setHidden("ageRangeLower", false);
			getCollectionElementView().getSubview("configQuestion").setHidden("ageRangeUpper", false);
		}

		else {
			System.out.println("edit quse hide HH gender and ages a "+getCollectionElementView().getBaseModelName()+" "+getCollectionElementView().getValueString("configQuestion.level"));
			
			getCollectionElementView().getSubview("configQuestion").setHidden("gender", true);
			getCollectionElementView().getSubview("configQuestion").setHidden("ageRangeLower", true);
			getCollectionElementView().getSubview("configQuestion").setHidden("ageRangeUpper", true);
		
		}
		
		System.out.println("commented out level - now do answertype");
		
		AnswerType answerType = (AnswerType) getCollectionElementView().getValue("configQuestion.answerType");
		
		if (answerType.equals(AnswerType.IntegerRange))
			// get integer range

			{
				System.out.println("a10101");
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeLower", false);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeUpper", false);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeUpper", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("questionLOVType", true);

			}

			else if (answerType.equals(AnswerType.DecimalRange)) {
				System.out.println("a20202");
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeLower", false);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeUpper", false);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeUpper", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("questionLOVType", true);
				
			
				
				

			}

			else if (answerType.equals(AnswerType.LOV)) {
				System.out.println("a30303");
				getCollectionElementView().getSubview("configQuestion").setHidden("questionLOV", false);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeUpper", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeUpper", true);

			}

			else if (answerType.equals(AnswerType.Decimal)
					|| answerType.equals(AnswerType.Integer)
					|| answerType.equals(AnswerType.Text)) {
				System.out.println("a40404");
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("decRangeUpper", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeLower", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("intRangeUpper", true);
				getCollectionElementView().getSubview("configQuestion").setHidden("questionLOVType", true);

			}
		
	
		
		
	}

}
