package efd.actions;

import org.openxava.actions.*;
import org.openxava.controller.*;

import efd.model.*;

import static org.junit.Assert.assertThat;

import javax.inject.*;

public class AssetChangeSectionAction extends ChangeSectionAction {// implements IModuleContextAction {

	@Inject
	private String efdModel;
	@Inject
	private String sessionTab;
	private ModuleContext context;

	public void execute() throws Exception {

		String rType[] = new String[20];

		/*
		 * Numbering is the order on the screen tabs - do not change without editing
		 * Study View
		 */

			rType[0] = "Land";
			rType[1] = "Livestock";
			rType[2] = "Tradeable";
			rType[3] = "Food Stocks";
			rType[4] = "Trees";
			rType[5] = "Cash";

			rType[6] = "Crops";
			rType[7] = "Livestock Sales";
			rType[8] = "Livestock Products";
			rType[9] = "Employment";
			rType[10] = "Transfers";
			rType[11] = "Wild Foods";

			int iactiveSection = getActiveSection();
			String sactiveSection = getViewObject();
			sessionTab = rType[iactiveSection];
	

		System.out.println("sessiontab  = " + sessionTab);

		super.execute();

	}

}
