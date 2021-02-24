package efd.validations;

import java.math.*;

import org.apache.commons.lang.*;
import org.openxava.actions.*;

public class OnChangeRSTkcal extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		System.out.println("in OnChangeRSTKcal");
		if (getView().getValueInt("resourcesubtypekcal") == 0)
			return;

		if (getView().getValueInt("resourcesubtypekcal") > 0 && !getView().getValueString("resourcesubtypesynonym.idresourcesubtype").isEmpty()) {
			{
				addMessage(
						"A Resource Subtype acting as a synonym will assume KCal value from master, this value is set to 0");
				
				getView().setValue("resourcesubtypekcal",0);
			}
		}

	}
}
