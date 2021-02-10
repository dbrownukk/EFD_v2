package efd.actions;

import org.openxava.actions.*;

public class RSUnewmanytomany extends CreateNewElementInManyToManyCollectionAction implements INavigationAction{

	public void execute() throws Exception {

		/* get custom report spec if called from CSR module */
		
		
		
		System.out.println("in new RSU ");
	
		String crsid = getView().getValueString("id");
		
		System.out.println("CRS id = "+	getView().getValueString("id"));
		
	
		
		super.execute();
		System.out.println("done exec , now set CRS");
		getCollectionElementView().setValue("customReportSpec.id", crsid);	
		getCollectionElementView().setHidden("customReportSpec.specName", true);


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
