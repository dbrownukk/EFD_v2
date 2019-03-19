package efd.actions;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

/* Ensure Level of ConfigQuestionUse is same as Config Question Level */

public class ConfigQuestionUseSave extends SaveAction {

	
	public void execute() throws Exception {
		//setResetAfter(false);
		super.execute();
		XPersistence.commit();
		System.out.println("in configQuestionUse Save"+getView().getAllValues());
		

		

		if (getView().getValue("level") == "Study") {

			Object cquid = getView().getValue("configQuestionUse.id").toString();

			System.out.println("cquid = " + cquid);

		
			
		}
		return;
	}



}
