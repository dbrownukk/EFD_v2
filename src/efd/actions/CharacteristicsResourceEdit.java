package efd.actions;

import javax.inject.*;

// From Study

import org.openxava.actions.*;

public class CharacteristicsResourceEdit extends EditElementInCollectionAction {

	@Inject
	private String sessionTab;

	public void execute() throws Exception {

		super.execute();
		System.out.println("in charresource edit, sessiontab =  = " + sessionTab);

		if (sessionTab == "Food Stocks") {   /// Special as it uses 3 RTs
			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
		} else {

			getCollectionElementView().setDescriptionsListCondition("resourcesubtype",
					"e.resourcetype.resourcetypename = '" + sessionTab + "'");
		}
	}

}
