package efd.utils;

import org.openxava.calculators.*;

/**
 * @author DRB
 */
public class ZeroDoubleCalculator implements ICalculator {
	
	private final static Double ZERO = new Double(0.0);
	

	public Object calculate() throws Exception {
		
		System.out.println("returning from Zero Double default ");
		
		return ZERO;
	}

}
