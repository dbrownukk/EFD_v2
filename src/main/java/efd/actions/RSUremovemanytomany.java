package efd.actions;

import java.util.*;

import org.openxava.actions.*;

public class RSUremovemanytomany extends RemoveSelectedInCollectionAction {

	public void execute() throws Exception {

		/* delete  custom report spec if called from CSR module */

		
		
		System.out.println("in delete CRS");
		
		Map keyValues = getCollectionElementView().getKeyValues();
				
		Map allValues = getCollectionElementView().getAllValues();
		Map allValues2 = getView().getAllValues();
		
		System.out.println("all vals = "+allValues);
		System.out.println("all vals 2 = "+allValues2);
		System.out.println("key vals  = "+keyValues.toString());
		super.execute();
		System.out.println("done exec , now delete CRS");
	

	}


}
