/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.model.Asset.*;

/* Read XLS Community Interview  spreadsheet */

public class SetHHEditable extends ViewBaseAction implements IAvailableAction {

	public void execute() throws Exception {

		
		Household wgint = (Household) getView().getEntity();
		View view = getView();
		if(wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{
		
			
			wgint.setStatus(efd.model.WealthGroupInterview.Status.FullyParsed);
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
