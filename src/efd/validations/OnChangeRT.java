package efd.validations;

import org.openxava.actions.*;

public class OnChangeRT extends OnChangePropertyBaseAction {

	public void execute() throws Exception {

		String idrst = getView().getValueString("idresourcesubtype");
		
		
		System.out.println("new val = "+getNewValue());
		if (getNewValue() != null && !idrst.equals("")) {
			addError("No change of Resource Type allowed");
			
			getView().reset();
		}
	}
}
