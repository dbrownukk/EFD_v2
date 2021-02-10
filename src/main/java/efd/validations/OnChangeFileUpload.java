package efd.validations;

import org.openxava.actions.*;

/*
 * On spreadsheet upload change status from Generated to Uploaded 
 * 
 * Extended so that WGI and Household use same upload code
 * DRB 5/2019
*/

public class OnChangeFileUpload extends OnChangePropertyBaseAction {

		
	public void execute() throws Exception {

		System.out.println("in spreadsheet onchange");
		
/*
		if (getNewValue() != null)
		{
			// No file uploaded 
			getView().setValue("status", efd.rest.domain.model.WealthGroupInterview.Status.Uploaded);
			
		}
		else
		{
			getView().setValue("status", efd.rest.domain.model.WealthGroupInterview.Status.Generated);
		}
		
*/		

	}

}
