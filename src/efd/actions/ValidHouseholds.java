package efd.actions;

import org.openxava.filters.*;

public class ValidHouseholds extends BaseContextFilter{

	@Override
	public Object filter(Object o) throws FilterException {
		String status = getString("status");
		
		System.out.println("in filter status = "+status);
		
		
		return null;
	}

	


	
	
	
}
