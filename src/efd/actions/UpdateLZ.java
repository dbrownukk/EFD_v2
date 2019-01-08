package efd.actions;


import org.openxava.actions.*;


public class UpdateLZ extends NewAction {

	public void execute() throws Exception {
System.out.println("in updateLZ");
		getView().setViewName("UpdateLZ");
		super.execute();
	}

}