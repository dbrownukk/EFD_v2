

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

		if (studyId == null) {
			addError("Save Study before copying in Topic Questions");
			closeDialog();
			return;
		}

		System.out.println("in copy topic aa " + topicId + " " + studyId);

		// get topic questions and study
		List<ConfigQuestion> topicConfigQuestions = XPersistence.getManager()
				.createQuery("from ConfigQuestion where topic_ID = '" + topicId + "'").getResultList();
		Study study = XPersistence.getManager().find(Study.class, studyId);
		System.out.println("study after query = " + study.getStudyName());

		ConfigQuestion configQuestion = null;
		ConfigQuestionUse configQuestionUse = null;

		// list existing questions

		List<ConfigQuestionUse> currentQuestions = (List<ConfigQuestionUse>) study.getConfigQuestionUse();

		
		
		
		
		
		Iterator<ConfigQuestion> topicQuestioniterator = topicConfigQuestions.iterator();
		
		System.out.println("in copy topic 22 ");
		while (topicQuestioniterator.hasNext() && currentQuestions.size() > 0) {
			System.out.println("in copy topic 221 size = " + currentQuestions.size());
			for (int j = 0; j < currentQuestions.size(); j++) {
				
				System.out.println("in copy topic 223 " + j);
				if (topicQuestioniterator.next().getId() == currentQuestions.get(j).getConfigQuestion().getId()) {
					System.out.println("found an existing question");
					topicQuestioniterator.remove();
				}
			}

		}
		System.out.println("done removes");
		if (topicConfigQuestions.size() == 0) {
			addWarning("Questions in Topic are already in this Study");
			return;
		}

		// Add configQuestionUse for each Question to this Study

		try {
			for (int i = 0; i < topicConfigQuestions.size(); i++) {

				configQuestion = (ConfigQuestion) topicConfigQuestions.get(i);
				configQuestionUse = new ConfigQuestionUse();

				configQuestionUse.setConfigQuestion(configQuestion);
				configQuestionUse.setStudy(study);
				configQuestionUse.setLevel(configQuestion.getLevel());

				configQuestionUse.setId(null);

				// exception is not caught - needs investigating

				try {
					System.out.println("about to  commit questionUse");
					XPersistence.getManager().persist(configQuestionUse);
					getView().refreshCollections();
					getView().refresh();
				} catch (Exception e) {
					// fails if QUestion already in this Study, buts thats ok
					// addMessage("caught XPersistence ex "+e);
					System.out.println("did not commit questionUse");
					break;
				}

			}
		} catch (Exception ex) {
			System.out.println("did not commit questionUse 22");
			addError("Failed to Add Topic questions to this Study " + ex);
			closeDialog();
			throw new SystemException("Failed to create " + ex);
			// failed to add topic questions to study due to duplicates or other failure
			// addMessage("Failed to Add Topic questions to this Study " + ex);
			// closeDialog();
		}

		closeDialog();
		getView().findObject();
		getView().refreshCollections();
		getView().refresh();

		addMessage("Copied Topic Questions into this Study");

	}

}
