/* 
 * Call Dialog to run modelling reports . 
 * 
 *  For modelling need to determine if OHEA - Community/LZ or OIHM Study / HH  
 */

package efd.actions;

import org.openxava.actions.*;

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
