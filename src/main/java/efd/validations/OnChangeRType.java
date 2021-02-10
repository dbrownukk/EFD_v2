package efd.validations;

import java.util.*;

import org.openxava.actions.*;

public class OnChangeRType extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		
		Object newRType = getNewValue();
		
		Map keyValues = getView().getKeyValues();
		
		
		System.out.println("in rst new type 1");
		if(newRType == null)   // its a new record
			return;
		System.out.println("in rst new type 2");
	//	if (newRType.equals(RType.ResourceType))

	//		getView().setViewName("rt");

	//	else if (newRType.equals(RType.ResourceSubType))

	//		getView().setViewName("rst");

	//	else
	//		getView().setViewName("cat");
		
		
		
		System.out.println("in rst new type 3");

		getView().findObject();
		
		
	//	getView().refresh();
		
	}

}
