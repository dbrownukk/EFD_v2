package efd.validations;

import org.openxava.actions.BaseAction;
import org.openxava.filters.BaseContextFilter;
import org.openxava.filters.FilterException;
import org.openxava.filters.IFilter;

import efd.utils.Efdutils;

//public class rstFiler  implements IFilter  {
@SuppressWarnings("serial")
public class RSTFilter extends BaseContextFilter {
	@Override
	public Object filter(Object o) throws FilterException {

		String nutrientMenu = getEnvironment().getValue("NUTRITIONMENU");

		

		Efdutils.em("");
		
		if (nutrientMenu == null) {
			return o;
		}
		
		if (nutrientMenu.equals("false")) {
			System.out.println("o is an array ");
			
			Object[] r = null;

			r = new Object[1];

			r[0] = "%";

			return r;
			

		}

		if (nutrientMenu.equals("true")) {

			Object[] r = null;

			r = new Object[1];

			r[0] = "Y";

			return r;

		} else {
			return o;
		}
	}

}
