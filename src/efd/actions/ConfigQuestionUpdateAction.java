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

			String thisquestionid = getView().getValueString("id");

			String thisstudyid = getPreviousView().getValueString("study.id");

			String thisconfigquestionuseid = getPreviousView().getValueString("id");

			System.out.println("id 1 = " + thisquestionid);
			System.out.println("id 2 = " + thisstudyid);
			System.out.println("id 3 = " + thisconfigquestionuseid);

			try {
				ConfigQuestionUse configQuestionUse = (ConfigQuestionUse) XPersistence.getManager()
						.find(ConfigQuestionUse.class, thisconfigquestionuseid);

				if (configQuestionUse.getConfigAnswer().isEmpty()) {
					ConfigAnswer answer = new ConfigAnswer();
					System.out.println("in cq update 111");
					answer.setAnswer("-");
					System.out.println("in cq update 222");
					answer.setConfigQuestionUse(configQuestionUse);
					System.out.println("in cq update 333");
					answer.setStudy(configQuestionUse.getStudy());
					System.out.println("in cq update 444");
					XPersistence.getManager().persist(answer);
					System.out.println("in cq update 555");

				}

			} catch (Exception ex) {
				// No CQU yet
			}

		}

	super.execute();

	}

}
