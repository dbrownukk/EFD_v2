/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.view.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.model.Asset.*;

/* Read XLS Community Interview  spreadsheet */

public class ShowWGIEditButton extends ViewBaseAction {

	public void execute() throws Exception {
		
		WealthGroupInterview wgint = (WealthGroupInterview) getView().getEntity();
		View view = getView();
		/*
		if(wgint.getStatus() == (efd.model.WealthGroupInterview.Status.Validated))
		{System.out.println("Show WGI Edit button  ");
			addActions("SetEditable.SetEditable");
			return;
		}
		removeActions("SetEditable.SetEditable");
		*/
}


}
