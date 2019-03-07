package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;

import efd.model.*;

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
