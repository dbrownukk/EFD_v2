package efd.validations;

import org.openxava.calculators.*;

/**
 * @author Javier Paniza - adapted for Double David Brown
 */
public class ZeroDoubleCalculator implements ICalculator {
	
	private final static Double ZERO = new Double(0);
	

	public Object calculate() throws Exception {
		return ZERO;
	}

}
