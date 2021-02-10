package efd.validations;

import java.util.Map;

import org.openxava.actions.*;
import org.openxava.tab.Tab;
import org.openxava.view.*;

public class OnChangeStudyToSelect extends OnChangePropertyBaseAction {

	@Override
	public void execute() throws Exception {
		
		
		
		
		Map allValues = getView().getAllValues();
		System.out.println("allvals = " + allValues);
	
		String studyid = getView().getValueString("studyToSelect.id");
		System.out.println("study id = "+studyid);
		
		getView().setValue("study.id", studyid);

		Tab tab = getView().getSubview("study.household").getCollectionTab();
		tab.setBaseCondition(tab.getBaseCondition() + " and ${status} = '4'"); // Validated
		getView().refreshCollections();
		
	}

}
