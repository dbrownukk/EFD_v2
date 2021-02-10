package efd.actions;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;

public class HHActionSelect extends OnSelectElementBaseAction {

	@Override
	public void execute() throws Exception {

		System.out.println("view All Values = " + getView().getAllValues());

		String studyid = getPreviousView().getValueString("id");
		String crsid = getView().getValueString("customReportSpec.id");

		System.out.println("HHAction crsid = " + crsid);
		System.out.println("studyid = " + studyid);

		
		if (crsid.isEmpty()) {
			System.out.println("no crsid");
			
			Map hhallValues = getView().getSubview("study.household").getAllValues();
			System.out.println("HH All Values = " + hhallValues);
		//	getView().getSubview("study.household").collectionDeselectAll();
			
			addWarning(
					"No Report Spec selected");
			
		
			return;
		}
		
		CustomReportSpec crs = XPersistence.getManager().find(CustomReportSpec.class, crsid);

		System.out.println("crs =  = " + crs.getSpecName());

		if (crs.getConfigAnswer().size() > 0 || crs.getResourceSubType().size() > 0 || crs.getResourceType().size() > 0
				|| crs.getCategory().size() > 0) {

			System.out.println("in deselect");
			// getView().collectionDeselectAll();
			
		//	getView().getSubview("study.household").collectionDeselectAll();
			
			addWarning(
					"Selected Report Spec has Household inclusion rules. No explicit Households can be chosen below");

		}

	}
}
