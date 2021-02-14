package efd.actions;

import java.math.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

public class EditConfigAnswer extends EditElementInCollectionAction {

	public void execute() throws Exception {

		System.out.println("in edit configAnswer");

		super.execute();

		System.out.println("in edit configAnswer done execute");

		// DB fields 
		getCollectionElementView().setHidden("answer", true);
		getCollectionElementView().setHidden("answerLowerRange", true);
		getCollectionElementView().setHidden("answerUpperRange", true);
		
		
		// Transients
		getCollectionElementView().setHidden("textAnswer", true);
		getCollectionElementView().setHidden("integerAnswer", true);
		getCollectionElementView().setHidden("decimalAnswer", true);
		getCollectionElementView().setHidden("lovAnswer", true);
		getCollectionElementView().setHidden("intRangeAnswer", true);
		getCollectionElementView().setHidden("decRangeAnswer", true);
		getCollectionElementView().setHidden("answerType", true);
		getCollectionElementView().setHidden("answerDecLower", true);
		getCollectionElementView().setHidden("answerDecUpper", true);
		getCollectionElementView().setHidden("answerIntLower", true);
		getCollectionElementView().setHidden("answerIntUpper", true);
		
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

			if (answer == null || answer.isEmpty()) {

				decanswer = new BigDecimal(0.0);
			} else {

				decanswer = new BigDecimal(answer);
			}

			getCollectionElementView().setValue("decimalAnswer", decanswer);

			break;
		case "LOV":
			System.out.println("its an LOV answer" + answer);
			getCollectionElementView().setHidden("lovAnswer", false);

			String questionLOVType = cq.getConfigQuestionUse().getConfigQuestion().getQuestionLOVType().getId();

			String condition = " ";
			condition = "questionLOVType_id = " + "'" + questionLOVType + "'";
			getCollectionElementView().setDescriptionsListCondition("lovAnswer", condition);

			break;

		case "IntegerRange":
			Integer answerIntLower = new Integer(cq.getAnswerLowerRange()==null?"0":cq.getAnswerLowerRange());
			Integer answerIntUpper = new Integer(cq.getAnswerUpperRange()==null?"0":cq.getAnswerUpperRange());
			
			getCollectionElementView().setHidden("answerIntLower", false);
			getCollectionElementView().setHidden("answerIntUpper", false);

			getCollectionElementView().setValue("answerIntLower", answerIntLower);
			getCollectionElementView().setValue("answerIntUpper", answerIntUpper);
	

			break;

		case "DecimalRange":
			
			BigDecimal answerDecLower = new BigDecimal(cq.getAnswerLowerRange()==null?"0.0":cq.getAnswerLowerRange());
			BigDecimal answerDecUpper = new BigDecimal(cq.getAnswerUpperRange()==null?"0.0":cq.getAnswerUpperRange());
			
			getCollectionElementView().setHidden("answerDecLower", false);
			getCollectionElementView().setHidden("answerDecUpper", false);

			getCollectionElementView().setValue("answerDecLower", answerDecLower);
			getCollectionElementView().setValue("answerDecUpper", answerDecUpper);
			
			
		

			break;

		}// end switch

	}

}
