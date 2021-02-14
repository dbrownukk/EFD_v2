package efd.validations;

import org.openxava.actions.*;

import efd.model.Asset.*;

public class OnChangeAssetStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		

		Object status;
		try { // wrongly fires on startup
			
			//System.out.println("In asset status onchange ");
			
			status = getView().getValue("status");
			
			
			
			String rst = getView().getValueString("resourceSubType.idresourcesubtype");
			
			
			if(status.toString().isEmpty())
			{
				System.out.println("status is empty");
				getView().setValue("status", Status.Invalid);
			}
			
			if ((status.equals(Status.Valid)) && (rst.isEmpty())) {
				getView().setValue("status", Status.Invalid);
				addError("Resource Subtype cannot be empty with Status of Valid");
			}

		} catch (Exception ex) {
			//System.out.println("wrong firing of onchange");
			return;
		}

	}
}
