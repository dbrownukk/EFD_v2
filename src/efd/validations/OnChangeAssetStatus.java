package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;

import efd.model.*;

public class OnChangeAssetStatus extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		
		//if(getNewValue().equals(Asset.Status.Valid) && getView().getValue("resourcesubtype.resourcetypename").toString().isEmpty())
			System.out.println("in asset change");
			
			//addError("Resource or Asset cannot be Valid with a blank Name");
			
			
	}
}
