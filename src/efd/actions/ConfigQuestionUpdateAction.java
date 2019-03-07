package efd.actions;

import java.util.*;

import javax.ejb.*;
import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.validators.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

/**
 * @author Javier Paniza
 */

public class ConfigQuestionUpdateAction extends UpdateAction  {
	
	@Inject
	private String studyid;
	@Inject
	private String configuseid;
	@Inject
	private String questionid;
	
	public void execute() throws Exception {	
		
		String level = getView().getValue("level").toString();
		
		//System.out.println("study id in update "+getView().getValueString("study.id"));
		System.out.println("id in update "+getView().getValueString("id"));
		if (!configuseid.isEmpty()) {
			System.out.println("in configuseid SET  = " + configuseid);
			ConfigQuestionUse configQuestionUse = XPersistence.getManager().find(ConfigQuestionUse.class, configuseid);

			if (level.equals("Household"))
				configQuestionUse.setLevel(Level.Household); // set configQuestionUse Level to be same as Question level
			else if (level.equals("Household Member"))
				configQuestionUse.setLevel(Level.HouseholdMember);
			else if (level.equals("Study"))
				configQuestionUse.setLevel(Level.Study);

			XPersistence.getManager().persist(configQuestionUse);
		}
		
		super.execute();
		
	}
	
}
