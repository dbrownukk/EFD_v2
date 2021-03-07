package efd.validations;

/*
 * 
 * DRB 29/11/19
 * 
 * For assetLand set units to Project default area measurement 
 */

import org.openxava.calculators.*;

public class UnitCalculator implements ICalculator {

	private String string;

	public Object calculate() throws Exception {

		return string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
