package efd.actions;


import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.tab.*;


public class UpdateLZ extends NewAction {

	public void execute() throws Exception {
System.out.println("in updateLZ");
		getView().setViewName("UpdateLZ");
		super.execute();
	}

}