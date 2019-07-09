package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class SetEfdOHEAModel extends BaseAction {

	@Inject
	private String efdModel;

	public void execute() throws Exception {
	setEfdModel("OHEA");
	
	System.out.println("efdModel = "+efdModel);
	}

	public String getEfdModel() {
		return efdModel;
	}

	public void setEfdModel(String efdModel) {
		this.efdModel = efdModel;
	}



}
