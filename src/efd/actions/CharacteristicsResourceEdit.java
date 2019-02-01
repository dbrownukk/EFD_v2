package efd.actions;

import javax.inject.*;


// From Study

import org.openxava.actions.*;

public class CharacteristicsResourceEdit extends EditElementInCollectionAction {

	@Inject
	private String sessionTab;
	
    public void execute() throws Exception {


    	super.execute();
    	
    	System.out.println("in charresource edit, sessiontab =  = " + sessionTab);
    	getCollectionElementView().setDescriptionsListCondition("resourcesubtype", "e.resourcetype.resourcetypename = '"+sessionTab+"'");
    	
    }
	
	
	
	
}
