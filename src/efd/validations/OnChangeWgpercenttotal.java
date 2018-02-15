package efd.validations;

import org.openxava.actions.*;

public class OnChangeWgpercenttotal extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		Integer value = Integer.valueOf((String) getNewValue());
		if (value > 100) {
			System.out.println("Newvalue = " + value);
			addMessage("Total Wealthgroup Percentages are greater than 100 percent");
		} else if (value < 100) {
			addMessage("Total Wealthgroup Percentages are less than 100 percent");
		}
	}
}
