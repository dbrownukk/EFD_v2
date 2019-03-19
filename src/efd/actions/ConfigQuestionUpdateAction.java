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
 * create a dummy answer for study questions
 */

public class ConfigQuestionUpdateAction extends UpdateAction {

	@Inject
	private String studyid;
	@Inject
	private String configuseid;
	@Inject
	private String questionid;

	public void execute() throws Exception {


		
		System.out.println("in update action configQuestion ");
		if (getView().getValue("level").equals(Level.Study)) {
			
			
			
			String thisquestionid =  getView().getValueString("id");
		
			String thisstudyid = getPreviousView().getValueString("study.id");
			
			String thisconfigquestionuseid = getPreviousView().getValueString("id");
	
			
			System.out.println("id 1 = "+thisquestionid);
			System.out.println("id 2 = "+thisstudyid);
			System.out.println("id 3 = "+thisconfigquestionuseid);
			
			ConfigQuestionUse configQuestionUse = (ConfigQuestionUse) XPersistence.getManager().find(ConfigQuestionUse.class, thisconfigquestionuseid); 
			
			if(configQuestionUse.getConfigAnswer().isEmpty())
			{
				ConfigAnswer answer = new ConfigAnswer();
				answer.setAnswer("-");
				answer.setConfigQuestionUse(configQuestionUse);
				answer.setStudy(configQuestionUse.getStudy());
				XPersistence.getManager().persist(answer);

				
			}
		}

		super.execute();

	}

}
