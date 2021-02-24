package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.view.*;
import efd.model.*;

public class ResourceSubTypeFilter extends  OnChangePropertyBaseAction   {


	public void execute() throws Exception {
		
		  /* NOT USED
		   * 
		   * 
		   * 
		   * 
		   * Note that these run the risk of being changed in ResourceType Table
		   * Current WealtgGroupInterview Tabs
		   *     
		   * 0 Livestock
		   * 1 Land
		   * 2 Crops
		   * 3 Livestock Products
		   * 4 Employment
		   * 5 Transfers
		   * 6 Wild foods
		   * 7 Tradeable
		   * 8 Food Stocks 
		   */
		
		
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
		
		
	/* Filter Resource Sub Type to correct Resource Type for WealthGroupInterview element collection */
super.executeBefore();		

		  System.out.println("in filter 1");
		  
		  irst = getView().getActiveSection();
		  
		  System.out.println("in filter 2");

		  rt = (ResourceType) XPersistence.getManager().createQuery("from ResourceType where ResourceTypeName = '"+rType[irst]+"'").getSingleResult();
		  System.out.println("in condition for sub resource type");
		  //String condition = "${resourcetype} = '"+rt.getIdresourcetype()+"'";
		  String condition = "${resourcetype} = 'fred'";
		  getView().setDescriptionsListCondition("resourceSubType", condition);    // we modify the condition of the combo 'state'
		 
	}
}
	      
	

