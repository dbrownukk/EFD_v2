package efd.actions;

import org.openxava.actions.*;

public class EFDSaveAction extends SaveAction {

	public void execute() throws Exception {
		System.out.println("In MySave");
		super.execute();
		if (!getErrors().contains()) {
			setNextMode(LIST);
		}
	}

}
