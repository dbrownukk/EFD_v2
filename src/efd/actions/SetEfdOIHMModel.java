package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class SetEfdOIHMModel extends BaseAction {

	@Inject
	private String efdModel;

	public void execute() throws Exception {
	setEfdModel("OIHM");
	}

	public String getEfdModel() {
		return efdModel;
	}

	public void setEfdModel(String efdModel) {
		this.efdModel = efdModel;
	}



}
