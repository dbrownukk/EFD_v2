package efd.actions;

import org.openxava.actions.*;

import efd.model.*;

public class EditStdOfLivingElement extends EditElementInCollectionAction  {

	public void execute() throws Exception {

		System.out.println("in edit StdOfLivingElement");
		super.execute();
		
		if (getCollectionElementView().getValue("level").equals(StdOfLivingElement.StdLevel.HouseholdMember)) {
			System.out.println("show HH gender and ages b = "+getCollectionElementView().getValue("level"));
			getCollectionElementView().setHidden("gender", false);
			getCollectionElementView().setHidden("ageRangeLower", false);
			getCollectionElementView().setHidden("ageRangeUpper", false);

		}

		else {
			System.out.println("hide HH gender and ages");
			getCollectionElementView().setHidden("gender", true);
			getCollectionElementView().setHidden("ageRangeLower", true);
			getCollectionElementView().setHidden("ageRangeUpper", true);

		}
		
		
	}

}
