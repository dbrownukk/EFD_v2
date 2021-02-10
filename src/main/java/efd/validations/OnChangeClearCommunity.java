package efd.validations;

import java.math.*;

import org.openxava.actions.*;

public class OnChangeClearCommunity extends OnChangePropertyBaseAction  {
	public void execute() throws Exception {
		
		String proj = (String) getNewValue();
		
		getView().clear();
		getView().setKeyEditable(true);
		getView().setEditable(true);
		//getView().reset();
		getView().setValue("projectlz.projectid", proj);
	}

}

