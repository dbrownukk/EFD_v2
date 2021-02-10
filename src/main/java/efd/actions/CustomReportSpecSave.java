package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;

public class CustomReportSpecSave extends SaveAction {


	public void execute() throws Exception {	
		
		System.out.println("in CRS save 1");
		
		
		
		System.out.println("in CRS save 2");
		
		// check that no more than RT, TST or Category are used
		
	
			Object rst = getView().getValue("resourceSubType");
			Object rt = getView().getValue("resourceType");
			Object cat = getView().getValue("category");
			
			System.out.println("in CRS save 3 = "+rt+" "+rst+" "+cat);
			
			
			System.out.println("in CRS persist / update "+rt+" "+rst+" "+cat);
			if ((rst != null && cat != null) ||
					(rst != null && rt != null) ||
					(cat !=null && rt !=null) ||
					(cat !=null && rt !=null && rst !=null) )
				
					
			{
				System.out.println("RS,RT and Cat not null");
				addError("Only one from Resource Type, Resource Sub Type or Category can be used");
			}


			
			super.execute();	
		
		
		
		
		
		

	}

}
