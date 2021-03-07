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

public class WgiUploadFileAction extends LoadAttachedFileAction implements IChainAction {
//public class WgiUploadFileAction extends UploadFileAction { // implements IChainAction {

	@SuppressWarnings("rawtypes")
	private List fileItems;

	@Inject
	private String newFileProperty;

	public void execute() throws Exception {

		System.out.println("in wgi upload");

		super.execute();

		System.out.println("model = " + getView().getModelName());
		if (getView().getModelName().equals("WealthGroupInterview")) {
			getView().setValue("status", efd.model.WealthGroupInterview.Status.Uploaded);
			System.out.println("in wgi upload 11");
			

		} else if (getView().getModelName().equals("Household")) {
			getView().setValue("status", efd.model.WealthGroupInterview.Status.Uploaded);
			System.out.println("in wgi upload 22");
			
		}
	}

	@Override
	public String getNextAction() throws Exception {
		// TODO Auto-generated method stub
		return "TypicalNotResetOnSave.save";
	} 

	/*
	 * @Override public String getNextAction() throws Exception {
	 * 
	 * System.out.println("In Save of ichain for file upload" );
	 * if(getView().getModelName().equals("WealthGroupInterview") ||
	 * getView().getModelName().equals("Household")) return
	 * "TypicalNotResetOnSave.save"; return null; }
	 */
}