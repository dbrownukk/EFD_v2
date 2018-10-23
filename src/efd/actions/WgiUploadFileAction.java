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
 * Extending existing Uploadfile does not work - thus copying code...
 * 
public class WgiUploadFileAction extends UploadFileAction {

	
	public void execute() throws Exception {
		
	System.out.println("in wgi uploadfile");	
	execute();
	}
	}
*/

public class WgiUploadFileAction extends ViewBaseAction implements INavigationAction, IProcessLoadedFileAction {

	@SuppressWarnings("rawtypes")
	private List fileItems;

	@Inject
	private String newFileProperty;

	public void execute() throws Exception {
		Iterator<?> it = getFileItems().iterator();
		while (it.hasNext()) {
			FileItem fi = (FileItem) it.next();
			if (!Is.emptyString(fi.getName())) {
				AttachedFile file = new AttachedFile();
				file.setName(fi.getName());
				file.setData(fi.get());
				FilePersistorFactory.getInstance().save(file);
				getPreviousView().setValue(getNewFileProperty(), file.getId());
			
				break;
			}
		}
		closeDialog();
		System.out.println("model = "+getView().getModelName());
		if(getView().getModelName().equals("WealthGroupInterview"))
			getView().setValue("status",efd.model.WealthGroupInterview.Status.Uploaded);
	}

	@SuppressWarnings("rawtypes")
	public List getFileItems() {
		return fileItems;
	}

	@SuppressWarnings("rawtypes")
	public void setFileItems(List fileItems) {
		this.fileItems = fileItems;
	}

	public String[] getNextControllers() throws Exception {
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() throws Exception {
		return PREVIOUS_VIEW;
	}

	public String getNewFileProperty() {
		return newFileProperty;
	}

	public void setNewFileProperty(String newFileProperty) {
		this.newFileProperty = newFileProperty;
	}
}
