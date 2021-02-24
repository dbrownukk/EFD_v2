package efd.validations;

import org.openxava.actions.OnChangePropertyBaseAction;
import org.openxava.annotations.AddAction;

public class OnChangeProjectinModellingScenario extends OnChangePropertyBaseAction{

	@Override
	public void execute() throws Exception {

		System.out.println("in on change for project in mod scenario");
		
		System.out.println("new proj val = "+getNewValue());
		System.out.println("get val from view = "+getView().getValueString("project.projectid"));
		
		if( getNewValue()==null) {
			
			getView().setHidden("livelihoodZone", true);
			//removeActions("LivelihoodZone.filteredLZforModelling");
		}
		else
		{
			getView().setHidden("livelihoodZone", false);
			//addActions("LivelihoodZone.filteredLZforModelling");
		}
		
	}
	



}
