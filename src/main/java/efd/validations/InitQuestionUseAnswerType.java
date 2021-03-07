package efd.validations;

import java.util.*;

import org.openxava.actions.*;

public class InitQuestionUseAnswerType extends InitViewAction {
	public void execute() throws Exception {



		
		try {
		
			String name = "configQuestion.answerType";
			Object value = getView().getValue(name);
			
		
			getView().setValueNotifying("configQuestion.answerType", value);
			
			
		} catch (Exception e) {
			return;
		}
		
	}

}
