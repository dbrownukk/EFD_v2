package efd.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.validators.*;



public class UpdateAction extends UpdateReferenceBaseAction  {
	
	
	
	public void execute() throws Exception {		
		try {					
			// Update
			/*
			System.out.println("in update prev view = "+ getPreviousView().getBaseModelName());
			System.out.println("id configQuestionUse = "+ getPreviousView().getValueString("id"));
			System.out.println("id configQuestion = "+ getView().getValueString("id"));
			
			System.out.println("what is use use Level "+getPreviousView().getValueString("level"));
			System.out.println("what is question Level "+getView().getValueString("level"));
			
			getPreviousView().setValue("level", getView().getValue("level"));
			
			
			System.out.println("what is use use Level after set "+getPreviousView().getValueString("level"));
			
			//getPreviousView().setValue("level", getView().getValue("level"));	
			//Map keyp = getPreviousView().getKeyValues();
			//MapFacade.setValues(getPreviousView().getBaseModelName(), keyp, getValuesToSave());
			
			//getPreviousView().updateModelFromView();
			//super.commit();
			*/
			super.executeAction("TypicalNotResetOnSave.save");
			super.commit();
			
			
			//getPreviousView().setValue("level", getView().getValue("level"));		
			
			
			Map key = getView().getKeyValues();			
			MapFacade.setValues(getView().getModelName(), key, getValuesToSave());
			//System.out.println("key = "+key.getClass().toString());
			returnsToPreviousViewUpdatingReferenceView(key);
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}
		catch (ObjectNotFoundException ex) {
			addError("no_modify_no_exists");
		}
	}
	
}
