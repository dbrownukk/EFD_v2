package efd.validations;

import org.openxava.actions.*;

public class OnChangeRT extends OnChangePropertyBaseAction {

	public void execute() throws Exception {

		String viewName = getView().getViewName();
		System.out.println("viewname in rt change = "+viewName);
		
		
		String idrst = getView().getValueString("idresourcesubtype");
		
		
		System.out.println("new val in RT onchange = "+getNewValue());
		if (getNewValue() != null && !idrst.equals("")) {
			addError("No change of Resource Type allowed");
			
			getView().reset();
		}
	}
}
