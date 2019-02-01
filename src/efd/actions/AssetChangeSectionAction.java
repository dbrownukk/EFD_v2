package efd.actions;

import org.openxava.actions.*;
import org.openxava.controller.*;

import efd.model.*;

import static org.junit.Assert.assertThat;

import javax.inject.*;

public class AssetChangeSectionAction extends ChangeSectionAction {// implements IModuleContextAction {


	@Inject
	private String sessionTab;
	private ModuleContext context;
	public void execute() throws Exception {

		String rType[] = new String[20];
		
		/* Numbering is the order on the screen tabs - do not change without editing Study View */
		
		rType[3] = "Land";
		rType[4] = "Livestock";
		rType[5] = "Tradeable";
		rType[6] = "Food Stocks";
		rType[7] = "Trees";
		rType[8] = "Cash";
		
		rType[9] = "Crops";
		rType[10] = "Livestock Sales";
		rType[11] = "Livestock Products";
		rType[12] = "Employment";
		rType[13] = "Transfers";
		rType[14] = "Wild Foods";
	


		//super.execute();

		
		
		int iactiveSection = getActiveSection();
		String sactiveSection = getViewObject();
		
		//System.out.println("activeSection =" + sactiveSection);		
		
		//if(sactiveSection.("3"))
		//	System.out.println("sactivesession is null");
		
		//if(sactiveSection.isEmpty()) {      // Creats an exception!
		//	System.out.println("dave view object empty aa =" + sactiveSection+" "+iactiveSection);	
		//}
			
		
		
		System.out.println("dave view object bb =" + sactiveSection+" "+iactiveSection);		
		
		sessionTab = rType[iactiveSection];
		
		System.out.println("dave in change section  = " + iactiveSection+" "+sessionTab);
		
		
		super.execute();
		
	}



}
