package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.hibernate.loader.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class OnChangeAssetStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		System.out.println("in asset change ....77");

		Object status;
		try { // wrongly fires on startup
			status = getView().getValue("status");
			System.out.println("status = "+status);
			
			String rst = getView().getValueString("resourceSubType.idresourcesubtype");
			System.out.println("rst = "+rst);
			
			
			
			if ((status.equals(Asset.Status.Valid)) && (rst.isEmpty())) {
				addError("Resource Subtype cannot be empty with Status of Valid");

				System.out.println("status and rst = " + status + rst);

			}

		} catch (Exception ex) {
			System.out.println("wrong firing of onchange");
			return;
		}

	}
}
