package efd.actions;

/*
 * 
 * Set HH to editable to re-edit a Validated HH
 */

import efd.model.Household;
import efd.model.WealthGroupInterview;
import org.openxava.actions.IAvailableAction;
import org.openxava.actions.ViewBaseAction;
import org.openxava.view.View;



public class SetHHEditable extends ViewBaseAction implements IAvailableAction {

	public void execute() throws Exception {

		
		Household hhint = (Household) getView().getEntity();
		View view = getView();
		if(hhint.getStatus() == (WealthGroupInterview.Status.Validated))
		{
		
			
			hhint.setStatus(WealthGroupInterview.Status.FullyParsed);
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
