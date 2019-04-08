package efd.actions;

import java.math.*;
import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class EditConfigAnswer extends EditElementInCollectionAction {

	public void execute() throws Exception {

		System.out.println("in edit configAnswer");
		
		super.execute();
		
		System.out.println("in edit configAnswer done execute");
		
		getCollectionElementView().setHidden("answer", true);
		getCollectionElementView().setHidden("textAnswer", true);
		getCollectionElementView().setHidden("integerAnswer", true);
		getCollectionElementView().setHidden("decimalAnswer", true);
		getCollectionElementView().setHidden("lovAnswer", true);
		getCollectionElementView().setHidden("intRangeAnswer", true);

		getCollectionElementView().setHidden("decRangeAnswer", true);

		getCollectionElementView().setHidden("answerType", true);

		System.out.println("in edit answer 1 =  " + getView().getAllValues());
		System.out.println("in edit answer 2 = " + getCollectionElementView().getAllValues());

		String cqid = getCollectionElementView().getValueString("id");
		System.out.println("in edit answer cqid = " + cqid);

		ConfigAnswer cq = (ConfigAnswer) XPersistence.getManager()
				.createQuery("from ConfigAnswer c where c.id = :cqid ").setParameter("cqid", cqid).getSingleResult();

		System.out.println("cqid = " + cqid);

		AnswerType answerType = cq.getConfigQuestionUse().getConfigQuestion().getAnswerType();
		String answer = cq.getAnswer();

		getCollectionElementView().setValue("answerType", answerType);

		System.out.println("answer type = " + answerType);
		switch (answerType.toString()) {
		case "Text":
			getCollectionElementView().setHidden("textAnswer", false);
			getCollectionElementView().setValue("textAnswer", answer);
			break;

		case "Integer":
			if (answer == "-") {
				answer = "0";
			}
			getCollectionElementView().setHidden("integerAnswer", false);
			getCollectionElementView().setValue("integerAnswer", answer);
			break;

		case "Decimal":

			BigDecimal decanswer = null;
			getCollectionElementView().setHidden("decimalAnswer", false);

			if (answer == null) {

				decanswer = new BigDecimal(0.0);
			} else {

				decanswer = new BigDecimal(answer);
			}

			getCollectionElementView().setValue("decimalAnswer", decanswer);

			break;
		case "LOV":
			System.out.println("1111"+answer);
			getCollectionElementView().setHidden("lovAnswer", false);
			
			System.out.println("22222"+answer);
			
			String questionLOVType = cq.getConfigQuestionUse().getConfigQuestion().getQuestionLOVType().getId();
			
			String condition = " ";
			condition = "questionLOVType_id = "+"'"+questionLOVType+"'";
			getCollectionElementView().setDescriptionsListCondition("lovAnswer", condition);
			System.out.println("condition = "+condition);
			
			/*
			if (answer != null) {
				System.out.println("3333 answer = "+questionLOVType);
				//QuestionLOV qlov = XPersistence.getManager().find(QuestionLOV.class, answer.substring(4, 36));
				QuestionLOV qlov = XPersistence.getManager().find(QuestionLOV.class, questionLOVType);
				
				System.out.println("lov in edit = " + qlov.toString());
				
				
				
				getCollectionElementView().setValue("lovAnswer", qlov);
				System.out.println("lov in edit done set ");
			}
			else
			{
				System.out.println("4444");
				getCollectionElementView().setValue("lovAnswer", null);
				System.out.println("5555");
			}
*/
			break;

		case "IntegerRange":
			getCollectionElementView().setHidden("intRangeAnswer", false);

			break;

		case "DecimalRange":
			getCollectionElementView().setHidden("decRangeAnswer", false);

			break;

		}// end switch

	}

}
