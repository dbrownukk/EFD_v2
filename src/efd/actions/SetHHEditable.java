package efd.actions;

/*
 * 
 * Set HH to editable to re-edit a Validated HH
 */

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.model.Asset.*;



public class SetHHEditable extends ViewBaseAction implements IAvailableAction {

	public void execute() throws Exception {

		
		Household hhint = (Household) getView().getEntity();
		View view = getView();
		if(hhint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{
		
			
			hhint.setStatus(efd.model.WealthGroupInterview.Status.FullyParsed);
			view.refresh();
			view.setEditable(true);
			removeActions("SetEditable.SetEditableHH");		
			return;
		}
		
		
}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return true;
	}


}
