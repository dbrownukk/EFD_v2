package efd.actions;

import org.openxava.actions.*;

public class NewQuestion extends ViewBaseAction implements IChangeModeAction, IModelAction {

	private String modelName; 
	private boolean restoreModel = false; 
	
	public void execute() throws Exception {

		System.out.println("in new Question");
		
		
		if (restoreModel) getView().setModelName(modelName); 
		getView().setKeyEditable(true);
		getView().setEditable(true);
		getView().reset();
		if (getView().hasSections()) getView().setActiveSection(0);
	}
	
	public String getNextMode() {
		return IChangeModeAction.DETAIL;
	}

	public void setModel(String modelName) { 
		this.modelName = modelName;		
	}

	public boolean isRestoreModel() {
		return restoreModel;
	}

	public void setRestoreModel(boolean restoreModel) {
		this.restoreModel = restoreModel;
	}
	
}
