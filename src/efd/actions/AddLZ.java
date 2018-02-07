package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class AddLZ extends GoAddElementsToCollectionAction implements INavigationAction{ //GoAddElementsToCollectionAction {

	public void execute() throws Exception {
		
		/* If Projectid is null then return */ 
		System.out.println("in save before check ");
		System.out.println("Project ID is  ");
		System.out.println(getView().getValue("projectid"));
		if (Is.empty(getView().getValue("projectid"))) {	
			addError("Save Project before adding a new Livelihood Zone");
			getView().setViewName("NewLZ");
			return;
		}	
		else
		{
	    /* Now exec standard CreateNewElementInManyToManyCollectionAction*/
		System.out.println("in add save ");
		super.execute();
		System.out.println("done exec ");
		}
	}

}

