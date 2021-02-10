
/* IOX Dialog to enter new name of Study  */

package efd.actions;

import org.openxava.actions.*;

public class GoCopyStudy extends ViewBaseAction {

	public void execute() throws Exception {

		// get Topic List
		
		
		System.out.println("in dialog call");
		showDialog();
		getView().setTitle("Enter New Study Name");

		getView().setModelName("StudyList");

		setControllers("CopyStudy", "Dialog");

		
	}

}
