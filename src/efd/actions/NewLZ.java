package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class NewLZ extends CreateNewElementInManyToManyCollectionAction {

	public void execute() throws Exception {

		/* If Projectid is null then return */
		// System.out.println("in save before check ");
		if(getView().getValueString("projectid").isEmpty()){
			 throw new IllegalStateException(
	                  XavaResources.getString( 
	                           "Site must be in context of a Project"));
		}

		
		super.execute();
		getView().setViewName("SimpleLZ");
		
		

	}

}
