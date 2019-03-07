
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class CopyTopic extends ViewBaseAction {

	public void execute() throws Exception {


		String topicId = getView().getValueString("reusableConfigTopic.id");
		Object studyId = getPreviousView().getValue("id");

		// get topic questions and study
		List configQuestions = XPersistence.getManager()
				.createQuery("from ConfigQuestion where topic_ID = '" + topicId + "'").getResultList();
		Study study = XPersistence.getManager().find(Study.class, studyId);
		System.out.println("study after query = " + study.getStudyName());

		ConfigQuestion configQuestion = null;
		ConfigQuestionUse configQuestionUse = null;

		// Add configQuestionUse for each Question to this Study

		try {
			for (int i = 0; i < configQuestions.size(); i++) {

				configQuestion = (ConfigQuestion) configQuestions.get(i);
				configQuestionUse = new ConfigQuestionUse();

				
				configQuestionUse.setConfigQuestion(configQuestion);
				configQuestionUse.setStudy(study);
				configQuestionUse.setLevel(configQuestion.getLevel());
				
				configQuestionUse.setId(null);
				
				// exception is not caught - needs investigating
				
				try
				{
				XPersistence.getManager().persist(configQuestionUse);
				getView().refreshCollections();
				getView().refresh();
				}
				catch(RollbackException e) {
					addMessage("caught XPersistence ex "+e);
				}

			}
		} catch (Exception ex) {
			closeDialog();
			throw new SystemException("Failed to create "+ex);
			// failed to add topic questions to study due to duplicates or other failure
			//addMessage("Failed to Add Topic questions to this Study " + ex);
			//closeDialog();
		}
		getView().refreshCollections();
		getView().refresh();
		closeDialog();

	}

}
