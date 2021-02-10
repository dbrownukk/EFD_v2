package efd.validations;

import org.openxava.actions.*;

public class OnChangeQuestionUseLevel extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		if (getNewValue() == null)
		{
		System.out.println("questionleveluse null onchange = null"+getNewValue());
			return;
		}
		System.out.println("questionleveluse onchange = null"+getNewValue());
		
		

	}
}
