package efd.actions;

import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class OnChangeResourceType extends OnChangePropertyBaseAction {

	@Inject
	private String sessionTab;
	@Inject
	private String efdModel;

	public void execute() throws Exception {

		System.out.println("In OnChangeRT 1 efdModel = " + efdModel);
		if (efdModel.equals("OIHM")) {
			System.out.println("rstonchange sessionTab = " + sessionTab);
			if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs
				System.out.println("rstonchange foodstocks = ");
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
			}

			else if (sessionTab == "Livestock Sales") {
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename = 'Livestock'");
			}
			else if (sessionTab == "Inputs") {
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename in ( 'Non Food Purchase','Other Tradeable Goods')");
			}

			else {
				System.out.println("rstonchange not foodstocks = ");
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename = '" + sessionTab + "'");
			}

		}

		else if (efdModel.equals("OHEA"))
			getView().setDescriptionsListCondition("resourcesubtype", "e.resourcetype.resourcetypename like '%'");

		System.out.println("done oihm ohea if = ");

		try {
			String newRST = getNewValue().toString();

			System.out.println("rst on change = " + newRST + " " + getView().getAllValues().toString());
			System.out.println("rst in change id = " + getView().getValue("resourcesubtype.idresourcesubtype"));

			String rst = getView().getValue("resourcesubtype.idresourcesubtype").toString();

			ResourceSubType resourceSubType = XPersistence.getManager().find(ResourceSubType.class, rst);
			System.out.println("rst unit = " + resourceSubType.getResourcesubtypeunit());

			System.out.println("set unit");
			getView().setValue("wgresourceunit", resourceSubType.getResourcesubtypeunit());

		} catch (Exception ex) {
			System.out.println("catch - this is a new asset, thus onchange not needed to fire and no new value");
		}

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