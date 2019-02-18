package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class SetEfdModel extends BaseAction {

	@Inject
	private String efdModel;

	public void execute() throws Exception {
	setEfdModel("OHEA");
	}

	public String getEfdModel() {
		return efdModel;
	}

	public void setEfdModel(String efdModel) {
		this.efdModel = efdModel;
	}



}
