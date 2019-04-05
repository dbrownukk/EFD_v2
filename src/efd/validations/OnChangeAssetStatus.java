package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.hibernate.loader.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.Asset.*;

public class OnChangeAssetStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		

		Object status;
		try { // wrongly fires on startup
			status = getView().getValue("status");
			
			
			String rst = getView().getValueString("resourceSubType.idresourcesubtype");
			
			
			
			
			if ((status.equals(Asset.Status.Valid)) && (rst.isEmpty())) {
				getView().setValue("status", Status.Invalid);
				addError("Resource Subtype cannot be empty with Status of Valid");
			}

		} catch (Exception ex) {
			//System.out.println("wrong firing of onchange");
			return;
		}

	}
}
