package efd.actions;

import org.openxava.actions.*;

public class NewQuestionUse extends CreateNewElementInCollectionAction {

	

	public void execute() throws Exception {

		super.execute();
/*
		System.out.println("in new Question "+getPreviousView().getAllValues());
		System.out.println("in new Question2 "+getView().getAllValues());
		System.out.println("in new Question21 "+getCollectionElementView().getAllValues());
		
		
		
		System.out.println("in new Question3 "+getView().getValue("studyName"));
		System.out.println("in new Question4 "+getPreviousView().getValue("studyName"));
		getCollectionElementView().setEditable("studyName", false);
		getView().setEditable("referenceYear", false);
		getView().setEditable("study", false);
*/
		getCollectionElementView().setValue("study.studyName", getPreviousView().getValue("studyName"));
		getCollectionElementView().setValue("study.referenceYear", getPreviousView().getValue("referenceYear"));
		System.out.println("in new Question22 "+getCollectionElementView().getAllValues());
		
	
	}
}
