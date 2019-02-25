package efd.validations;

import java.math.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.*;
import org.openxava.view.*;

import com.openxava.naviox.*;
import com.openxava.naviox.model.*;
import com.openxava.naviox.util.*;

import com.openxava.phone.web.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;

/*
 * 
 * Config Question set/unset hidden range fields
 * 
 */

public class OnChangeAnswerType extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		
		
		System.out.println("on change  answer type = "+getView().getValueString("answerType"));
		System.out.println("on change  model = "+getView().getModelName());
		
		if(getNewValue() == null)
			return;
		
		
		if (getNewValue().equals(efd.model.ConfigQuestion.AnswerType.IntegerRange))
		// get integer range
		
		{
			System.out.println("10101");
			getView().setHidden("intRangeLower", false);
			getView().setHidden("intRangeUpper", false);
			getView().setHidden("decRangeLower", true);
			getView().setHidden("decRangeUpper", true);
			getView().setHidden("questionLOV", true);	
			
			//getView().setHidden("configQuestion.intRangeLower", false);
			//getView().setHidden("configQuestion.intRangeUpper", false);
			//getView().setHidden("configQuestion.decRangeLower", true);
			//getView().setHidden("configQuestion.decRangeUpper", true);
			//getView().setHidden("configQuestion.questionLOV", true);	
			
			//getView().setHidden("configQuestionUse.intRangeLower", false);
			//getView().setHidden("configQuestionUse.intRangeUpper", false);
			//getView().setHidden("configQuestionUse.decRangeLower", true);
			//getView().setHidden("configQuestionUse.decRangeUpper", true);
			//getView().setHidden("configQuestionUse.questionLOV", true);	
			
		} 
		
		if (getNewValue().equals(efd.model.ConfigQuestion.AnswerType.DecimalRange))
		{
			System.out.println("20202");
			getView().setHidden("decRangeLower", false);
			getView().setHidden("decRangeUpper", false);
			getView().setHidden("intRangeLower", true);
			getView().setHidden("intRangeUpper", true);
			getView().setHidden("questionLOV", true);	
			
	
		} 
		
		if (getNewValue().equals(efd.model.ConfigQuestion.AnswerType.LOV))
		{
			System.out.println("30303");
			getView().setHidden("questionLOV", false);
			getView().setHidden("decRangeLower", true);
			getView().setHidden("decRangeUpper", true);
			getView().setHidden("intRangeLower", true);
			getView().setHidden("intRangeUpper", true);
			

			
			
			
		}

		
		if(getNewValue().equals(efd.model.ConfigQuestion.AnswerType.Decimal) ||
				getNewValue().equals(efd.model.ConfigQuestion.AnswerType.Integer) ||
				getNewValue().equals(efd.model.ConfigQuestion.AnswerType.Text) )
		{
			System.out.println("40404");
			getView().setHidden("decRangeLower", true);
			getView().setHidden("decRangeUpper", true);
			getView().setHidden("intRangeLower", true);
			getView().setHidden("intRangeUpper", true);
			getView().setHidden("questionLOV", true);	
			

			
			
			
		}
		
		System.out.println("in answertype change");
		//getView().getParent().refresh();
		
	
		
		//getView().recalculateProperties(); //  dont use - resets the values
		
	}

}
