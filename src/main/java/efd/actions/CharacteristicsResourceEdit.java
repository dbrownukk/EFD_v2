package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class CharacteristicsResourceEdit extends EditElementInCollectionAction {

	@Inject
	private String sessionTab;

	public void execute() throws Exception {
		String rType[] = new String[20];
		int iactiveSection = 0;

		super.execute();
		System.out.println("in charresource edit 23, sessiontab =  = " + sessionTab);

		System.out.println("in char edit active section = " + getCollectionElementView().getActiveSection());
		// SEEMS CORRECT

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

		if (getMetaCollection().getName().equals("characteristicsResourceLand"))
			iactiveSection = 0;
		else if (getMetaCollection().getName().equals("characteristicsResourceLivestock"))
			iactiveSection = 1;
		else if (getMetaCollection().getName().equals("characteristicsResourceTradeable"))
			iactiveSection = 2;
		else if (getMetaCollection().getName().equals("characteristicsResourceFoodstock"))
			iactiveSection = 3;
		else if (getMetaCollection().getName().equals("characteristicsResourceTree"))
			iactiveSection = 4;
		else if (getMetaCollection().getName().equals("characteristicsResourceCash"))
			iactiveSection = 5;
		else if (getMetaCollection().getName().equals("characteristicsResourceCrop"))
			iactiveSection = 6;
		else if (getMetaCollection().getName().equals("characteristicsResourceLivestockSales"))
			iactiveSection = 7;
		else if (getMetaCollection().getName().equals("characteristicsResourceLivestockProducts"))
			iactiveSection = 8;
		else if (getMetaCollection().getName().equals("characteristicsResourceEmployment"))
			iactiveSection = 9;
		else if (getMetaCollection().getName().equals("characteristicsResourceTransfers"))
			iactiveSection = 10;
		else if (getMetaCollection().getName().equals("characteristicsResourceWildFoods"))
			iactiveSection = 11;
		else if (getMetaCollection().getName().equals("characteristicsResourceInputs"))
			iactiveSection = 12;

		sessionTab = rType[iactiveSection];

		getCollectionElementView().setHidden("type", true);

		if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs
			System.out.println("its a food stock");
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
		} else if (sessionTab == "Livestock Sales") {
			String studyid = getParentView().getValueString("id");
			Study study = XPersistence.getManager().find(Study.class, studyid);

			CharacteristicsResourceNew characteristicsResourceNew = new CharacteristicsResourceNew();

			String condition = characteristicsResourceNew.setLSSCondition(study);
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype", condition);
			// getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
			// "e.resourcetype.resourcetypename = 'Livestock Sales'");
		} else if (sessionTab == "Inputs") {
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ( 'Non Food Purchase','Other Tradeable Goods')");
		} else {
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename = '" + sessionTab + "'");
		}
	}

}
