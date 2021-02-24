package efd.actions;

/*
 * Filter LZs based on Poject for Modelling Scenario
 * DRB
 * 16/4/2020
 * 
 */

import java.util.*;

import javax.inject.*;
import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class FilteredLZforModelling extends ReferenceSearchAction{

	public void execute() throws Exception {

		String projectid = getView().getValueString("project.projectid");
		System.out.println("projectid = " + projectid);

		if (projectid.isEmpty()) {
			addError("Enter Project before selecting Livelihood Zone");
			closeDialog();
		} else {
			Query query = XPersistence.getManager()
					.createQuery("select lz.lzid from LivelihoodZone lz join lz.project pr " + " where pr.projectid = '"
							+ projectid + "'");

			List lzs = query.getResultList();

			String condition = "";
			for (Object lz : lzs) {
				condition += "'" + lz + "',";
				System.out.println("proj lz = " + lz);

			}

			condition = condition.substring(0, condition.length() - 1);
			System.out.println("condition = " + condition);
			super.execute();
			getTab().setBaseCondition("${lzid} in (" + condition + ")");
		}
	}

}
