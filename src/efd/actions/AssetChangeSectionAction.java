package efd.actions;

import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;

import efd.model.*;



// public class AssetChangeSectionAction extends ChangeSectionAction implements IModuleContextAction{
//public class AssetChangeSectionAction extends OnChangePropertyBaseAction {
	public class AssetChangeSectionAction extends ChangeSectionAction  {



	public void execute() throws Exception {
	
		int irst = 0;
		ResourceType rt = null;
		String rType[] = new String[9];
		
		rType[0] = "Livestock";
		rType[1] = "Land";
		rType[2] = "Crops";
		rType[3] = "Livestock Products";
		rType[4] = "Employment";
		rType[5] = "Transfers";
		rType[6] = "Wild foods";
		rType[7] = "Tradeable";
		rType[8] = "Food Stocks";
		
		
		
		super.execute();
		
		String asection = getRequest().getParameter("xava_activeSection");
		
		
		int activeSection = this.getActiveSection();
		
		System.out.println("in change section"+activeSection+asection);
		
		//getView().setDescriptionsListCondition("resourceSubType","resourcetype.resourcetypename = 'Crops'");
	
	/*
	  rt = (ResourceType) XPersistence.getManager().createQuery("from ResourceType where ResourceTypeName = '"+rType[activeSection]+"'").getSingleResult();
	  System.out.println("in condition for sub resource type");
	  //String condition = "${resourcetype} = '"+rt.getIdresourcetype()+"'";
	  String condition = "${resourcetype} = 'fred'";
	  getView().setDescriptionsListCondition("resourceSubType", condition);    // we modify the condition of the combo 'state'
	 
	 getView not valid here
	 
	*/
	
	
	}

}
