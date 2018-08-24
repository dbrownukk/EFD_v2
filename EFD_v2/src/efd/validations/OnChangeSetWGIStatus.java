package efd.validations;

import java.math.*;

import org.openxava.actions.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;

/* After upload of spreadsheet in WG Interview set status to Uploaded */
/* will also need to set status after validate to stop further uploads of spreadsheet */ 

public class OnChangeSetWGIStatus extends OnChangePropertyBaseAction  {
	public void execute() throws Exception {
		
		System.out.println("In change for ss");
		String spreadsheet = getChangedProperty();
		System.out.println("In change" +spreadsheet);

		
		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity();
		System.out.println("In change" + wgint.getWgAverageNumberInHH() );
		wgint.setStatus(Status.Uploaded);
	}

}

