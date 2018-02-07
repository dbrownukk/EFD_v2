package efd.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class NewLZ extends CreateNewElementInManyToManyCollectionAction {

	public void execute() throws Exception {

		/* If Projectid is null then return */
		// System.out.println("in save before check ");
		
		if (Is.empty(getView().getValue("projectid"))) {
			addError("Save Project before creating a new Livelihood Zone");
			return;
		}

		/*
		 * Now exec standard CreateNewElementInManyToManyCollectionAction
		 * System.out.println("in save "); String collectionModel =
		 * getCollectionElementView().getModelName(); String collection =
		 * getCollectionElementView().getMemberName(); showDialog();
		 * getView().setModelName(collectionModel);
		 * getView().setMemberName(collection);
		 * setControllers("ManyToManyNewElement", "Dialog");
		 * 
		 * old way of doing this
		 */

		
		//getView().setViewName("SimpleLZ");
		
		super.execute();

	}

}
