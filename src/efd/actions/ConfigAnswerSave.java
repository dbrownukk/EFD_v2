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
			 Integer irangeValue = (Integer) getCollectionElementView().getValue("intRangeAnswer");
			 System.out.println("int range = "+irangeValue);
			 
			 answerid = getCollectionElementView().getValueString("id");
			 System.out.println("answer id = "+answerid);
			 
			 // Another possible route ... 
			 // Invoice invoice = (Invoice) MapFacade.findEntity(getModelName(), getView().getKeyValues());
			 
			 ca = XPersistence.getManager().find(ConfigAnswer.class, answerid);
			 
			 Integer intRangeLower = ca.getConfigQuestionUse().getConfigQuestion().getIntRangeLower();
			 Integer intRangeUpper = ca.getConfigQuestionUse().getConfigQuestion().getIntRangeUpper();
			 
			 if(irangeValue < intRangeLower || irangeValue > intRangeUpper)
			 {
			 	throw new javax.validation.ValidationException(
			            XavaResources.getString(
			                "invalid_range",intRangeLower,intRangeUpper));
			 
			 }	
			 	

			getCollectionElementView().setValue("answer", getCollectionElementView().getValue("intRangeAnswer").toString()); 

			
			break;

		case "DecimalRange":
			
			// validation
			 BigDecimal drangeValue = (BigDecimal) getCollectionElementView().getValue("decRangeAnswer");
			 System.out.println("dec range = "+drangeValue);
			 
			 answerid = getCollectionElementView().getValueString("id");
			 System.out.println("answer id = "+answerid);
		
			 
			 ca = XPersistence.getManager().find(ConfigAnswer.class, answerid);
			 
			 Double decRangeLower = ca.getConfigQuestionUse().getConfigQuestion().getDecRangeLower();
			 Double decRangeUpper = ca.getConfigQuestionUse().getConfigQuestion().getDecRangeUpper();
			 
		//	 if(drangeValue.doubleValue() < decRangeLower || drangeValue.doubleValue() > decRangeUpper)
		
			 Double doubledRangeValue = drangeValue.doubleValue();
			 
			 
			 if(doubledRangeValue.compareTo(decRangeLower) < 0 || (doubledRangeValue.compareTo(decRangeUpper)) > 0 )
			 {
			 	throw new javax.validation.ValidationException(
			            XavaResources.getString(
			                "invalid_range",decRangeLower,decRangeUpper));
			 
			 }	
			 	

			
			
			
			getCollectionElementView().setValue("answer", getCollectionElementView().getValue("decRangeAnswer").toString());

			break;

		}

		super.execute();

	//	getView().findObject();

	}

}
