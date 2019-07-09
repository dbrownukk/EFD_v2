package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;

public class CharacteristicsResourceSave extends SaveElementInCollectionAction {

	@Inject
	private String sessionTab;

	public void execute() throws Exception {

		// set the resource type in characteristic resources for use in spreadsheet create
		
		
		
		getCollectionElementView().setValue("type", sessionTab);
		
		super.execute();
		

	}

}
