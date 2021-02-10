package efd.actions;

import org.openxava.actions.*;

/* Set default to List after a Detail Save */

public class EFDSaveAction extends SaveAction {

	public void execute() throws Exception {
		
		super.execute();
		
		getTab().setBaseCondition(null);
		/* reset after setting in Community Site Reference */ 
		
		if (!getErrors().contains()) {
			setNextMode(LIST);
		}
		
	}

}
