/* 
 * Call Dialog to run modelling reports . 
 * 
 *  For modelling need to determine if OHEA - Community/LZ or OIHM Study / HH  
 */

package efd.actions;

import java.util.*;
import java.util.stream.*;

import javax.inject.*;

import org.apache.commons.lang3.*;
import org.openxava.actions.*;
import org.openxava.annotations.AddActions;
import org.openxava.jpa.*;
import org.openxava.tab.*;

import efd.model.*;
import efd.model.WealthGroupInterview.*;
import efd.utils.*;

public class GoReporting extends ViewBaseAction {


	public void execute() throws Exception {

		
		showDialog();
		getView().setTitle("Reporting");
		getView().setModel("Reporting");
		setControllers("Reporting","Dialog");
		addActions("Reporting", "Dialog.cancel");
		showNewView();
	}


}
