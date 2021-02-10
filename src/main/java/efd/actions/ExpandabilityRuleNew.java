/* DRB 29/10/2020
 * Create new Expandability Rule, determine if for OIHM or OHEA and save in a transient
 * 
 */

package efd.actions;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class ExpandabilityRuleNew extends CreateNewElementInCollectionAction implements INavigationAction {

	public void execute() throws Exception {

		Object id = "";
		Community community = null;
		Study study = null;

		System.out.println("Create a new Expandability Rule");

		super.execute();

		if ((id = getPreviousView().getValue("communityid")) != null) {

			
			System.out.println("EXPandability -for OHEA");
			community = XPersistence.getManager().find(Community.class, id.toString());
			
			
			getCollectionElementView().setValue("modelType", "OHEA");
			getCollectionElementView().setHidden("modelType", true);
		}
		else if ((id = getPreviousView().getValue("id")) != null)
		{
			System.out.println("EXPandability -for OIHM");
			study = XPersistence.getManager().find(Study.class, id.toString());
			
			//getView().setValue("modelType", "OIHM");
			getCollectionElementView().setValue("modelType", "OIHM");
			getCollectionElementView().setHidden("modelType", true);
		}
		
	//	getCollectionElementView().setValue("parentId", id.toString());
	//	getCollectionElementView().setHidden("parentId", true);

	}

	@Override
	public String[] getNextControllers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomView() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
