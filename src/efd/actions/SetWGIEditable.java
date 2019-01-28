/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.model.Asset.*;

/* Read XLS Community Interview  spreadsheet */

public class SetWGIEditable extends ViewBaseAction implements IAvailableAction {

	public void execute() throws Exception {

		
		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity();
		View view = getView();
		if(wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{
		
			System.out.println("set WGI to editable in SetWGIEditable  ");
			wgint.setStatus(efd.model.WealthGroupInterview.Status.FullyParsed);
			view.refresh();
			view.setEditable(true);
			removeActions("SetEditable.SetEditable");		
			return;
		}
		
		
}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return true;
	}


}
