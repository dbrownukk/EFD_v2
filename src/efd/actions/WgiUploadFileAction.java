package efd.actions;

import java.util.*;

import javax.inject.*;

import org.apache.commons.fileupload.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.web.editors.*;

import efd.model.WealthGroupInterview.*;

/*
 * Extending existing Uploadfile 
 * 
 * Extended so that WGI and Household use same upload code
 * 
*/

public class WgiUploadFileAction extends UploadFileAction implements IChainAction {


	
	@SuppressWarnings("rawtypes")
	private List fileItems;

	@Inject
	private String newFileProperty;

	public void execute() throws Exception {
		

		super.execute();
	
		
		
		System.out.println("model = "+getView().getModelName());
		if(getView().getModelName().equals("WealthGroupInterview"))
			getView().setValue("status",efd.model.WealthGroupInterview.Status.Uploaded);
		else if 	(getView().getModelName().equals("Household"))
			getView().setValue("status",efd.model.WealthGroupInterview.Status.Uploaded);
		
		
		
		
		// Remove confusion of whether or not file upload is in database or not - no longer requires a Save
		//XPersistence.commit();
		
	}


	@Override
	public String getNextAction() throws Exception {
		
		System.out.println("In Save of ichain for file upload" );
		if(getView().getModelName().equals("WealthGroupInterview")
				|| getView().getModelName().equals("Household"))
			return "TypicalNotResetOnSave.save";
		return null;
	}

}
