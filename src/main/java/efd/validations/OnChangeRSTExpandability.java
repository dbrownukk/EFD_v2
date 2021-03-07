/*
 * 
 * Expandability Rule RST On Change 
 * 
 * DRB 7/1/2020
 * 
 * Set defaults and hide/unhide based on RT
 */

package efd.validations;

import java.util.Map;
import java.util.OptionalDouble;

import org.hibernate.boot.model.source.internal.hbm.AbstractEntitySourceImpl;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.actions.ExpandabilityRuleEdit;
import efd.model.*;

public class OnChangeRSTExpandability extends OnChangePropertyBaseAction {
	public void execute() throws Exception {
		ResourceSubType rst = null;
		double[] averages = { 0.0, 0.0, 0.0 };
		String parentId = "";

		System.out.println("in RST Expandability on change = ");

		Object rstid = getNewValue();
		if (rstid != null) {

			String id = getView().getValueString("id");

			ExpandabilityRule expRule = XPersistence.getManager().find(ExpandabilityRule.class, id);

			if (expRule != null) {
				parentId = expRule.getParentId();
			}

			rst = XPersistence.getManager().find(ResourceSubType.class, rstid.toString());

			String rtName = rst.getResourcetype().getResourcetypename();
			if (rtName.contains("Livestock")) {
				System.out.println("Livestock Sale ");
				// getView().setHidden("expandabilityLimit", false);
				// getView().setHidden("expandabilityIncreaseLimit", false);
				getView().setValue("expandabilityLimit", 25);
				getView().setValue("expandabilityIncreaseLimit", 200);
				getView().setEditable("expandabilityLimit", true);
				getView().setEditable("expandabilityIncreaseLimit", true);

			} else if (rtName.equals("Crops") || rtName.equals("Wild Foods") || rtName.equals("Employment")
					|| rtName.equals("Transfers")) {
				System.out.println("No need for expand values ");
				// getView().setHidden("expandabilityLimit", true);
				// getView().setHidden("expandabilityIncreaseLimit", true);
				getView().setValue("expandabilityLimit", 0);
				getView().setValue("expandabilityIncreaseLimit", 0);
			} else {
				System.out.println("Not  ");
				// getView().setHidden("expandabilityLimit", false);
				// getView().setHidden("expandabilityIncreaseLimit", false);
				getView().setValue("expandabilityLimit", 0);
				getView().setValue("expandabilityIncreaseLimit", 0);
				getView().setEditable("expandabilityLimit", true);
				getView().setEditable("expandabilityIncreaseLimit", true);
			}

			/*
			 * Calc average Units Produced / Units Sold and Average % of food requirements
			 * the Units Consumed represents
			 */
			/* #450 */

			System.out.println("calc averages in RST Expandability for Study / Community ");

			final ResourceSubType strRST = rst;

			// Map allValues = getPreviousView().getAllValues();
			// System.out.println("all vals = "+allValues);

			String modelName = getPreviousView().getModelName();
			System.out.println("model = " + modelName);

			if (modelName.equals("Community")) {
				System.out.println("this is OHEA communityid = ");

				if (parentId.isEmpty()) {
					parentId = getPreviousView().getValue("communityid").toString();
				}
				getView().setHidden("modelType", true);
				Community community = XPersistence.getManager().find(Community.class, parentId);

				averages = ExpandabilityRuleEdit.calcAverages(community, strRST);

			} else {

				if (parentId.isEmpty()) {
					parentId = getPreviousView().getValue("id").toString();
				}

				System.out.println("this is OIHM");

				Study study = XPersistence.getManager().find(Study.class, parentId);
				System.out.println("parentid = "+parentId);
				System.out.println("study = "+study.getStudyName());
				
				
				averages = ExpandabilityRuleEdit.calcAverages(study, strRST);

				getView().setHidden("modelType", true);

			}
			getView().setValue("averageUnitsProduced", averages[0]);

			getView().setValue("averageUnitsSold", averages[1]);

			getView().setValue("averagePerCentConsumedRepresents", averages[2]);

		}

		System.out.println("change rst exp exit");
	}
}
