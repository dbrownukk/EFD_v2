package efd.validations;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.meta.*;

import efd.model.*;

public class OnChangeCRSList extends OnChangePropertyBaseAction {

	@Override
	public void execute() throws Exception {

		String studyid = getView().getValueString("study.id");
		String crsid = getView().getValueString("customReportSpec.id");

		System.out.println("study id =" + studyid);
		System.out.println("crs id =" + crsid);

		CustomReportSpec crs = XPersistence.getManager().find(CustomReportSpec.class, crsid);

		System.out.println("crs name =" + crs.getSpecName());

		/*
		 * Check if any HH restrictions are set in Category, RST, RT or
		 * Questions/Answers
		 * 
		 */

		if (crs.getConfigAnswer().size() > 0 || crs.getResourceSubType().size() > 0 || crs.getResourceType().size() > 0
				|| crs.getCategory().size() > 0) {
			System.out.println("in deselect");

			getView().getSubview("study.household").collectionDeselectAll();
			addWarning(
					"Selected Report Spec has Household inclusion rules. No explicit Households can be chosen below");

		} else {
			System.out.println("in select");
		}

	}

}
