package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;

public class OnChangeResourceType extends OnChangePropertyBaseAction {

	@Inject
	private String sessionTab;
	@Inject
	private String efdModel;

	public void execute() throws Exception {

		System.out.println("In OnChangeRT 1 efdModel = " + efdModel);
		if (efdModel.equals("OIHM")) {

			if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs

				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
			} else {

				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename = '" + sessionTab + "'");
			}

		}

		else if (efdModel.equals("OHEA"))
			getView().setDescriptionsListCondition("resourcesubtype", "e.resourcetype.resourcetypename like '%'");

	}

	public String getSessionTab() {
		return sessionTab;
	}

	public void setSessionTab(String sessionTab) {
		this.sessionTab = sessionTab;
	}

	public String getEfdModel() {
		return efdModel;
	}

	public void setEfdModel(String efdModel) {
		this.efdModel = efdModel;
	}
}