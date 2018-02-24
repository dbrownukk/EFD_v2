package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class AddLZ extends GoAddElementsToCollectionAction implements IChangeModuleAction { // GoAddElementsToCollectionAction
																							// {

	public void execute() throws Exception {

		
		System.out.println("Add LZ 1"+getView().getValueString("projectid"));
		System.out.println("Add LZ 2"+getView().getBaseModelName());
		if (getView().getValueString("projectid").isEmpty() && getView().getBaseModelName().equals("Project")) {
			System.out.println("Raise error = ");
			addError("Must be in Context of a saved Project");
			//wait();
			return;
		}

		super.execute();
	}

@Override
public String getNextModule() {
	// TODO Auto-generated method stub
	addError("Must be in Context of a saved Project");
	return PREVIOUS_MODULE;
}

@Override
public boolean hasReinitNextModule() {
	// TODO Auto-generated method stub
	return false;
}

}
