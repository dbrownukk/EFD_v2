package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;

import efd.utils.*;

public class NewHousehold extends NewAction {
	
	private Messages errors;
	private Messages messages;
	
	public void execute() throws Exception {
		
		Efdutils.em("in NewHousehold");
		//getView().getSubview("crop").setValue("unitsSold", 0.0);
		
		
		
		super.execute();
		//Map allValues = getView().getAllValues();
	
		//System.out.println("all vals in new HH = "+allValues);
		
		
	}

}
