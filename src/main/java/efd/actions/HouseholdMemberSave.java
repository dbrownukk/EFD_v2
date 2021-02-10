package efd.actions;

// From Study

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.*;
import efd.rest.domain.model.ConfigQuestion.*;

public class HouseholdMemberSave extends SaveElementInCollectionAction {

	public void execute() throws Exception {

		// set the resource type in characteristic resources for use in spreadsheet
		// create

		System.out.println("in HHM Save");

		String hhid = getPreviousView().getValueString("id");
		Household hh = XPersistence.getManager().find(Household.class, hhid);

		int nextNumberInHH = hh.getHouseholdMember().size() + 1;

		System.out.println("hhm prepersist num = " + nextNumberInHH);

		getView().setValue("householdMember.householdMemberNumber", "HHM" + nextNumberInHH);

		String hhmNumber = "HHM" + nextNumberInHH;

		System.out.println("pre " + hhmNumber + " " + hhid);
		super.execute();
		System.out.println("done  " + hhmNumber + " " + hhid);

		HouseholdMember newhhm = (HouseholdMember) XPersistence.getManager()
				.createQuery("from HouseholdMember c where c.household.id = :hhid "
						+ "and c.householdMemberNumber = :hhmNumber")
				.setParameter("hhid", hhid).setParameter("hhmNumber", hhmNumber).getSingleResult();

		System.out.println("found hh member = " + newhhm.getHouseholdMemberName());

		hh = XPersistence.getManager().find(Household.class, hhid);
		// Create answer records

		for (ConfigQuestionUse configQuestionUse : hh.getStudy().getConfigQuestionUse()) {
			System.out.println("1 question  = " + configQuestionUse.getConfigQuestion().getPrompt());
			System.out.println("1 question  = " + configQuestionUse.getConfigQuestion().getLevel());

			if (configQuestionUse.getConfigQuestion().getLevel().equals(Level.HouseholdMember)) {
				System.out.println("1");
				ConfigAnswer ans = new ConfigAnswer();
				ans.setAnswerType(configQuestionUse.getConfigQuestion().getAnswerType());
				ans.setConfigQuestionUse(configQuestionUse);
				ans.setHouseholdMember(newhhm);
				ans.setStudy(configQuestionUse.getStudy());

				System.out.println("2");
				XPersistence.getManager().persist(ans);
				System.out.println("3");

			}

		}

	}

}
