/**
 * 
 */
package efd.actions;

import java.util.Map;

import javax.swing.text.View;

import org.openxava.actions.BaseAction;
import org.openxava.actions.ViewBaseAction;

/**
 * @author David
 * Add or remove Search button for LZ
 *
 */
public class InitModellingScenario extends ViewBaseAction {

	
	public void execute() throws Exception {
	
		
		
		Map allValues = getView().getAllValues();
		
		boolean containsKey = allValues.containsKey("project");
		if(!containsKey) {
			return;
		}
		
		
		
		
	
		String projectid = getView().getValueString("project.projectid");
		
		if (projectid.isEmpty()) {
			getView().setHidden("livelihoodZone", true);
			//removeActions("LivelihoodZone.filteredLZforModelling");
			System.out.println("remove action");
		}
		else
		{
			getView().setHidden("livelihoodZone", false);
			//addActions("LivelihoodZone.filteredLZforModelling");
			System.out.println("add action");
		}
		
	}

}
