package efd.actions;

import org.openxava.util.Messages;
import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class NewLZ extends CreateNewElementInManyToManyCollectionAction implements INavigationAction{

	public void execute() throws Exception {

		/* If Projectid is null then return */
		System.out.println("in save before check ");
		System.out.println("ViewBase = "+getView().getBaseModelName());
		System.out.println("View = "+getView());
		if(getView().getValueString("projectid").isEmpty()
				&& getView().getBaseModelName().equals("Project")){
			 	System.out.println("Raise error = ");			
					 addError("Must be in Context of a saved Project");
	                  return;
		}
 
		//System.out.println("before exec View = "+getView().getValues());
		super.execute();
		getView().setViewName("CreateLZ");
		//System.out.println("View = "+getView().getValues());
		
		

	}

	@Override
	public String[] getNextControllers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomView() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
