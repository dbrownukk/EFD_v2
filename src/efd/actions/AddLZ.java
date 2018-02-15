package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class AddLZ extends GoAddElementsToCollectionAction implements INavigationAction{ //GoAddElementsToCollectionAction {

	public void execute() throws Exception {
		
		if(getView().getValueString("projectid").isEmpty()){
			 throw new IllegalStateException(
	                  XavaResources.getString( 
	                           "Site lookup must be in context of a Project"));
		}
		super.execute();
		
	}

}

