package efd.actions;

import java.math.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;
import javax.swing.event.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.impl.*;
import org.openxava.util.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

/* 
 * Create dummy answer on creation of question usage at Question Study level
 * 
 */

public class ConfigAnswerSave extends SaveElementInCollectionAction {

	public void execute() throws Exception {

		ConfigAnswer ca = null;
		String answerid = "";
		System.out.println("in configQuestion Save " + getCollectionElementView().getAllValues());

		AnswerType answerType = (AnswerType) getCollectionElementView().getValue("answerType");
		System.out.println("answertype in answer save = " + answerType);
		
		
		 answerid = getCollectionElementView().getValueString("id");
		 System.out.println("answer id = "+answerid);
		 
		 // Another possible route ... 
		 // Invoice invoice = (Invoice) MapFacade.findEntity(getModelName(), getView().getKeyValues());
		 
		 ca = XPersistence.getManager().find(ConfigAnswer.class, answerid);
		 System.out.println("configanswer = "+ca.getAnswerType());

		switch (answerType.toString()) {
		case "Text":
			getCollectionElementView().setValue("answer", getCollectionElementView().getValue("textAnswer").toString());
			break;

		case "Integer":
			getCollectionElementView().setValue("answer", getCollectionElementView().getValue("integerAnswer").toString());
			break;

		case "Decimal":
			getCollectionElementView().setValue("answer", getCollectionElementView().getValue("decimalAnswer").toString());
			break;
		case "LOV":
			System.out.println("lov answer = "+getCollectionElementView().getValue("lovAnswer"));
			getCollectionElementView().setValue("answer", getCollectionElementView().getValueString("lovAnswer"));
			break;

		case "IntegerRange":
			// validation
			 Integer enteredIntLower = (Integer) getCollectionElementView().getValue("answerIntLower");
			 Integer enteredIntUpper = (Integer) getCollectionElementView().getValue("answerIntUpper");
			 
	 
			 Integer intRangeLower = ca.getConfigQuestionUse().getConfigQuestion().getIntRangeLower();
			 Integer intRangeUpper = ca.getConfigQuestionUse().getConfigQuestion().getIntRangeUpper();
			 
			 if(enteredIntLower < intRangeLower || enteredIntUpper > intRangeUpper)
			 {
			 	throw new javax.validation.ValidationException(
			            XavaResources.getString(
			                "invalid_range",intRangeLower,intRangeUpper));
			 
			 }	
			 	


			 
			 getCollectionElementView().setValue("answerLowerRange",enteredIntLower.toString());
			 getCollectionElementView().setValue("answerUpperRange",enteredIntUpper.toString());
			
			
			break;

		case "DecimalRange":
			
			// validation
			 BigDecimal enteredDecLower = (BigDecimal) getCollectionElementView().getValue("answerDecLower");
			 BigDecimal enteredDecUpper = (BigDecimal) getCollectionElementView().getValue("answerDecUpper");

			 
			 Double decRangeLower = ca.getConfigQuestionUse().getConfigQuestion().getDecRangeLower();
			 Double decRangeUpper = ca.getConfigQuestionUse().getConfigQuestion().getDecRangeUpper();
			 
			 
			 if(Double.compare(enteredDecLower.doubleValue(),decRangeLower) <0 || Double.compare(enteredDecLower.doubleValue(),decRangeUpper) >0
					 || Double.compare(enteredDecUpper.doubleValue(),decRangeUpper) >0
					 || Double.compare(enteredDecUpper.doubleValue(),decRangeLower) <0
			 		 || Double.compare(enteredDecLower.doubleValue(), enteredDecUpper.doubleValue()) > 0)
		 {
			 	throw new javax.validation.ValidationException(
			            XavaResources.getString(
			                "invalid_range",decRangeLower,decRangeUpper));
			 
			 }	
			 	

			 getCollectionElementView().setValue("answerLowerRange",enteredDecLower.toString());
			 getCollectionElementView().setValue("answerUpperRange",enteredDecUpper.toString());
			
			break;

		}

		super.execute();


	}

}
