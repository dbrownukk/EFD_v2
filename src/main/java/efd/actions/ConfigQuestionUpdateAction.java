package efd.actions;

import org.openxava.jpa.*;

import efd.rest.domain.model.*;
import efd.rest.domain.model.ConfigQuestion.*;

/**
 * create a dummy answer for study questions
 */

public class ConfigQuestionUpdateAction extends UpdateAction {



	public void execute() throws Exception {



		System.out.println("in update action configQuestion ");
		if (getView().getValue("level").equals(Level.Study)) {

			String thisquestionid = getView().getValueString("id");

			String thisstudyid = getPreviousView().getValueString("study.id");

			String thisconfigquestionuseid = getPreviousView().getValueString("id");



			try {
				ConfigQuestionUse configQuestionUse = (ConfigQuestionUse) XPersistence.getManager()
						.find(ConfigQuestionUse.class, thisconfigquestionuseid);

				if (configQuestionUse.getConfigAnswer().isEmpty()) {
					ConfigAnswer answer = new ConfigAnswer();
					
					answer.setAnswer("-");
					
					answer.setConfigQuestionUse(configQuestionUse);
					
					answer.setStudy(configQuestionUse.getStudy());
					
					XPersistence.getManager().persist(answer);
				
				}

			} catch (Exception ex) {
				// No CQU yet
				System.out.println("exception in configquestionupdateaction "+ex);
			}

		}

		super.execute();

	}

}
