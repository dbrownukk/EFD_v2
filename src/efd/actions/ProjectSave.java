package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;

public class ProjectSave extends SaveAction {

	@Inject
	private String xava_view;

	public void execute() throws Exception {

		
		System.out.println("In Poject Save view = "+xava_view);
		
		
		
		super.execute();
		

	}

}
