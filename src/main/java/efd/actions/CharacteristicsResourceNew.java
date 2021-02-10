package efd.actions;

import javax.inject.*;

import org.apache.commons.lang.StringUtils;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class CharacteristicsResourceNew extends CreateNewElementInCollectionAction implements INavigationAction {

	@Inject
	private String sessionTab;

	public void execute() throws Exception {

		String rType[] = new String[20];
		int iactiveSection = 0;

		System.out.println("in edit char resource");
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

		System.out.println("meta = " + getMetaCollection().getName());
		System.out.println("meta L = " + getMetaCollection().getLabel());

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

		System.out.println("sessiontab in new wgr 1 = " + sessionTab);

		sessionTab = rType[iactiveSection];

		System.out.println("sessiontab in new wgr 2 = " + sessionTab);

		if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs
			System.out.println("its a food stock");
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
		} else if (sessionTab == "Livestock Sales") {


			String studyid = getParentView().getValueString("id");
			Study study = XPersistence.getManager().find(Study.class, studyid);

			String condition = setLSSCondition(study);		/* condition set to return LS from this Study in a , delim string */	
			
			
			System.out.println("ls sales condition =  "+condition);
			if(condition.equals("e.resourcetypename in ()"))
			{
				addError("No Livestock to sell, add Livestock resource to Study");
				closeDialog();
				return;
			}
			
			
			
			
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					condition);
			
			/* Any Livestock to display? */
			
			
			
			
			
			
			
			
		//			"e.resourcetype.resourcetypename = 'Livestock Sales'");
		} else if (sessionTab == "Inputs") {
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ( 'Non Food Purchase','Other Tradeable Goods')");
		} else {
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename = '" + sessionTab + "'");
		}

		System.out.println(
				"rst condition now = " + getCollectionElementView().getDescriptionsListCondition("resourcesubtype"));

		// super.execute();

		getCollectionElementView().setHidden("type", true);

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

	public String setLSSCondition(Study study)
	{
		String condition = "";
		
		/*
		 * Issue #295 LSS should only be from existing Livestock
		 * 
		 * this need to get existing list of LS for this Study
		 * 
		 * FOR DISCUSSION as Livestock Sale is of RT Lvestock 
		 */

		//String studyid = getParentView().getValueString("id");
		//Study study = XPersistence.getManager().find(Study.class, studyid);

		//System.out.println("in new cres study = " + study.getStudyName() + " " + study.getReferenceYear());
		//System.out.println("in new cres studyid = " + study.getId());

		String lov = "";

		for (WGCharacteristicsResource wgCharacteristicsResource : study
				.getCharacteristicsResourceLivestockSales()) {
			
		
			
			if (wgCharacteristicsResource.getType().equals("Livestock")) {
				System.out.println("subtype = "+wgCharacteristicsResource.getResourcesubtype().getResourcetypename());
				lov = lov + "'" + wgCharacteristicsResource.getResourcesubtype().getResourcetypename() + "',";
			}
		}
		lov = StringUtils.chop(lov);
		System.out.println("lov = " + lov);
		
		condition = "e.resourcetypename in ("+lov+")";
		System.out.println("condition = "+condition);
		
		return condition;
		
		
	}
	
}
