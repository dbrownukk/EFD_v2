package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;

public class OnChangeWgpercenttotal extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		if (getNewValue().equals(null))
				{
				System.out.print("its null");
				//getView().setValue("wgpercenttotal", "0");
				return;
				}
		BigDecimal OneHundred = new BigDecimal("100");
		BigDecimal value = (BigDecimal) getNewValue();
		if (value.compareTo(OneHundred) == 1) {
			System.out.println("Newvalue = " + value);
			addMessage("Total Wealthgroup Percentages are greater than 100 percent");
		} else if (value.compareTo(OneHundred) == 0) {
			addMessage("Total Wealthgroup Percentages are less than 100 percent");
		}
	}
}
