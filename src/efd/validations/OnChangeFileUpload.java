package efd.validations;

import java.util.*;

import javax.inject.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.web.editors.*;

import com.openxava.naviox.actions.*;

import efd.model.WealthGroupInterview.*;

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
			getView().setValue("status", efd.model.WealthGroupInterview.Status.Uploaded);
			
		}
		else
		{
			getView().setValue("status", efd.model.WealthGroupInterview.Status.Generated);
		}
		
*/		

	}

}
