package efd.validations;

import java.util.Map;

import org.openxava.actions.OnChangePropertyBaseAction;
import org.openxava.tab.Tab;
import org.openxava.view.View;

public class OnChangeRefreshReporting extends OnChangePropertyBaseAction {
	public void execute() throws Exception {

		Map allValues = getView().getAllValues();
		
		System.out.println("allvals in reportrefresh col  = "+allValues);
		
		getView().refreshCollections();
		System.out.println("update collections");
		
		
		View collectionTab = getView().getSubview("livelihoodZone");
		getView().getSubview("livelihoodZone").refreshCollections();
	//	System.out.println("collectionTab = "+collectionTab.getBaseCondition());
		
		
		
		
	}
}
