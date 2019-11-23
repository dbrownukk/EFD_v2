package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class AssetChangeSectionAction extends ChangeSectionAction implements IModuleContextAction {

	@Inject
	private String efdModel;
	@Inject
	private String sessionTab;

	public void execute() throws Exception {

		String rType[] = new String[20];

		/*
		 * Numbering is the order on the screen tabs - do not change without editing
		 * Study View
		 */
		super.execute();

		rType[0] = "Land";
		rType[1] = "Livestock";
		rType[2] = "Other Tradeable Goods";
		rType[3] = "Food Stocks";
		rType[4] = "Trees";
		rType[5] = "Cash";

		rType[6] = "Crops";
		rType[7] = "Livestock Sales";
		rType[8] = "Livestock Products";
		rType[9] = "Employment";
		rType[10] = "Transfers";
		rType[11] = "Wild Foods";
		rType[12] = "Inputs";

		int iactiveSection = getActiveSection();

		sessionTab = rType[iactiveSection];

	}

}
