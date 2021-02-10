package efd.actions;

import java.util.*;

import javax.inject.*;

import efd.rest.domain.Project;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class OnChangeResourceType extends OnChangePropertyBaseAction {

	@Inject
	private String sessionTab;
	@Inject
	private String efdModel; // No longer used - a project can be used in OIHM or OHEA and is treated as the
								// same and can be used in both

	public void execute() throws Exception {

		Boolean isConditionSet = false;
		String resourceUnit = null;
		Boolean isInOHEA = false;

		try {
			Map allValues = getPreviousView().getAllValues();
			System.out.println("all values = " + allValues);

			if (!getPreviousView().getValueString("wgid").isEmpty() || !getPreviousView().getValueString("communityid").isEmpty()) {
				isInOHEA = true;
				System.out.println("in ohea set isInOHEA");
			}
		} catch (Exception e) {
			// Previous did not exist 
			e.printStackTrace();
		}

		System.out.println("In OnChangeRT 1 efdModel = " + efdModel);
		if (!isInOHEA) {
			System.out.println("in oihm");
			System.out.println("rstonchange sessionTab = " + sessionTab);
			if (sessionTab == "Food Stocks") { /// Special as it uses 3 RTs
				System.out.println("rstonchange foodstocks = ");
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products')");
				isConditionSet = true;
			}

			else if (sessionTab == "Livestock Sales") {
				isConditionSet = true;
				// System.out.println("in onchange prev view all vals = =
				// "+getPreviousView().getBaseModelName());
				// String studyid = getPreviousView().getValueString("id");
				// System.out.println("in onchange study = "+studyid);
				// Study study = XPersistence.getManager().find(Study.class, studyid);

				// CharacteristicsResourceNew characteristicsResourceNew = new
				// CharacteristicsResourceNew();

				// String condition = characteristicsResourceNew.setLSSCondition(study);
				// getView().setDescriptionsListCondition("resourcesubtype", condition);

				// getView().setDescriptionsListCondition("resourcesubtype",
				// "e.resourcetype.resourcetypename = 'Livestock'");
			} else if (sessionTab == "Inputs") {
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename in ( 'Non Food Purchase','Other Tradeable Goods')");
				isConditionSet = true;
			}

			else {
				System.out.println("rstonchange not foodstocks = ");
				getView().setDescriptionsListCondition("resourcesubtype",
						"e.resourcetype.resourcetypename = '" + sessionTab + "'");
				isConditionSet = true;
			}

		}

		else {
			if (!isConditionSet) {
				System.out.println("in ohea");
				getView().setDescriptionsListCondition("resourcesubtype", "e.resourcetype.resourcetypename like '%'");
			}
		}
		try {
			System.out.println("in try");
			String newRST = getNewValue().toString();

			System.out.println("rst on change = " + newRST + " " + getView().getAllValues().toString());
			System.out.println("rst in change id = " + getView().getValue("resourcesubtype.idresourcesubtype"));

			String rst = getView().getValue("resourcesubtype.idresourcesubtype").toString();

			ResourceSubType resourceSubType = XPersistence.getManager().find(ResourceSubType.class, rst);
			System.out.println("rst unit = " + resourceSubType.getResourcesubtypeunit());

			System.out.println("about to set unit");

		
			resourceUnit = resourceSubType.getResourcesubtypeunit();
			System.out.println("done set unit");
			/* For this Project/LZ is there a LocalUnit for this RST */
			
			System.out.println("about to get local unit "+resourceSubType.getResourcetypename());
			Collection<LocalUnit> localUnits = resourceSubType.getLocalUnits();
			System.out.println("adone get local unit "+localUnits.size());
			
			
			Project thisProject = null;

			System.out.println("isInOHEA = "+isInOHEA);
			
			
			if (!isInOHEA) {
				
				/* if SIte has no LZ, using COuntry only then no localUnits will apply */
				
				System.out.println("its OIHM ");
				String studyid = getPreviousView().getValueString("id");

				Study study = XPersistence.getManager().find(Study.class, studyid);
				thisProject = study.getProjectlz();
				
				Site site = study.getSite();
				if(site.getLivelihoodZone()==null)
				{
					/* No LZ thus no LocalUnits */
					getView().setValue("wgresourceunit", resourceSubType.getResourcesubtypeunit());
					
					System.out.println("NO LZ");
					
					return;
				}
				

			} else // isInOHEA, thus get Proj from Community
			{
				System.out.println("its OHEA ");
				
				String wgid = getPreviousView().getValueString("wgid");
				WealthGroup  wealthgroup = XPersistence.getManager().find(WealthGroup.class, wgid);
				thisProject = wealthgroup.getCommunity().getProjectlz();
				
			}

			for (LocalUnit localUnit : localUnits) {
				for (Project project : localUnit.getLivelihoodZone().getProject()) {
					if (project == thisProject) {

						System.out.println("in loop 1111 " + localUnit.getName());
						resourceUnit += " / " + localUnit.getName();
					}
				}
			}

			getView().setValue("wgresourceunit", resourceUnit);

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