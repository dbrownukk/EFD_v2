package efd.actions;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;

public class NewQuestionUse extends CreateNewElementInCollectionAction {

	public void execute() throws Exception {

		super.execute();

		// Warn if spreadsheets already uploaded as HHs into this Study

		Object studyid = getParentView().getValue("id");

		Study study = XPersistence.getManager().find(Study.class, studyid);

		System.out.println("study = " + study.getStudyName());

		for (Household household : study.getHousehold()) {
			System.out.println("hh = " + household.getHouseholdName());
			if (household.getStatus().toString() != "") {
				System.out.println("in loop 444 = "+household.getHouseholdName()+" "+household.getStatus());
				addWarning("A Household within this Study already has an uploaded Spreadsheet");
				break;
			}
		}


		getCollectionElementView().setValue("study.studyName", getPreviousView().getValue("studyName"));
		getCollectionElementView().setValue("study.referenceYear", getPreviousView().getValue("referenceYear"));
		

	}
}
