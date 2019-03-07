package efd.actions;

import org.openxava.actions.*;

import efd.model.*;

/* Ensure Level of ConfigQuestionUse is same as Config Question Level */

public class ConfigQuestionUseSave extends SaveAction {

	public void execute() throws Exception {
		
		
		System.out.println("in configQuestionUse Save");
		getView().setValue("level", getView().getValue("configQuestion.level"));
		super.execute();
		}
		
	

}
