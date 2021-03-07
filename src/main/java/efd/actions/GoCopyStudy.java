
/* IOX Dialog to enter new name of Study  */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

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
