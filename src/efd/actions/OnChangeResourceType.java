package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class OnChangeResourceType extends OnChangePropertyBaseAction {


	@Inject
	private String sessionTab;
	
    public void execute() throws Exception {


    	System.out.println("in on change active sessiontab = " + sessionTab);
    	getView().setDescriptionsListCondition("resourcesubtype", "e.resourcetype.resourcetypename = '"+sessionTab+"'");
    	
    	
    	
    }
}