package efd.actions;

import org.openxava.actions.*;
import org.openxava.jpa.*;

import efd.model.*;
import efd.model.ConfigQuestion.*;

/* 
 * Create dummy answer on creation of question usage at Question Study level
 * 
 */

public class ConfigQuestionUseSave extends SaveElementInCollectionAction {

	public void execute() throws Exception {
		// setResetAfter(false);

		if (getCollectionElementView().getValue("configQuestion.level").equals(Level.Study)) {

			String studyid = getView().getValueString("id");
			String cqid = getCollectionElementView().getValueString("configQuestion.id");

			System.out.println("in Study if 2");

			super.execute();

			ConfigQuestionUse cqu = (ConfigQuestionUse) XPersistence.getManager().createQuery(
					"select c from ConfigQuestionUse c where c.configQuestion.id = :cqid and c.study.id = :studyid",
					ConfigQuestionUse.class).setParameter("cqid", cqid).setParameter("studyid", studyid)
					.getSingleResult();

			System.out.println("cqu = " + cqu.getId());

			System.out.println("ids = " + studyid + " a " + cqid + " b");

			if (cqu.getConfigAnswer().isEmpty()) {
				ConfigAnswer answer = new ConfigAnswer();
				answer.setAnswer(null);
				answer.setConfigQuestionUse(cqu);
				answer.setStudy(cqu.getStudy());
				XPersistence.getManager().persist(answer);
			}
		} else
			super.execute();

		getView().findObject();
		// super.executeAction("CRUD.refresh");

	}

}
