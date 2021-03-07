/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.model.Asset.*;

/* Set Validated WGI to read only once status = Validated  */

public class SetWGIReadOnly extends ViewBaseAction{

	public void execute() throws Exception {

		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity();
		
		View view = getView();
		
		
		
		if(wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{
			System.out.println("set to read only in SetWGIReadOnly 3");
			view.setEditable(false);
			
			//view.displayAsDescriptionsListAndReferenceView();
			
			
			
			addActions("SetEditable.SetEditable");
			return;

		}
		System.out.println("in set read only Status = "+wgint.getStatus().toString());
		removeActions("SetEditable.SetEditable");
		
}



}
