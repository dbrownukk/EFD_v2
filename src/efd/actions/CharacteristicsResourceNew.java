package efd.actions;

import org.openxava.util.Messages;

import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

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


		
		System.out.println("meta = "+getMetaCollection().getName());
		System.out.println("meta L = "+getMetaCollection().getLabel());
		
	
		
		if(getMetaCollection().getName().equals("characteristicsResourceLand"))
			iactiveSection=0;
		else if(getMetaCollection().getName().equals("characteristicsResourceLivestock"))
			iactiveSection=1;
		else if(getMetaCollection().getName().equals("characteristicsResourceTradeable"))
			iactiveSection=2;
		else if(getMetaCollection().getName().equals("characteristicsResourceFoodstock"))
			iactiveSection=3;
		else if(getMetaCollection().getName().equals("characteristicsResourceTree"))
			iactiveSection=4;
		else if(getMetaCollection().getName().equals("characteristicsResourceCash"))
			iactiveSection=5;
		else if(getMetaCollection().getName().equals("characteristicsResourceCrop"))
			iactiveSection=6;
		else if(getMetaCollection().getName().equals("characteristicsResourceLivestockSales"))
			iactiveSection=7;
		else if(getMetaCollection().getName().equals("characteristicsResourceLivestockProducts"))
			iactiveSection=8;
		else if(getMetaCollection().getName().equals("characteristicsResourceEmployment"))
			iactiveSection=9;
		else if(getMetaCollection().getName().equals("characteristicsResourceTransfers"))
			iactiveSection=10;
		else if(getMetaCollection().getName().equals("characteristicsResourceWildFoods"))
			iactiveSection=11;

		System.out.println("sessiontab in new wgr 1 = "+sessionTab);
		
		sessionTab = rType[iactiveSection];

		
		System.out.println("sessiontab in new wgr 2 = "+sessionTab);
		
		
		if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs
			System.out.println("its a food stock");
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
		} else if (sessionTab == "Livestock Sales") {
				getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename = 'Livestock'");
		}else{
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename = '" + sessionTab + "'");
		}
		
		System.out.println("rst condition now = "+getCollectionElementView().getDescriptionsListCondition("resourcesubtype"));
		
		
		
		//super.execute();
		
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

}
