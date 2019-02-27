
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.beanutils.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;

public class CopyStudy extends ViewBaseAction {

	public void execute() throws Exception {

		Object studyId = getPreviousView().getValue("id");

		// get topic questions and study

		Study study = XPersistence.getManager().find(Study.class, studyId);
		System.out.println("study after query = " + study.getStudyName());
		System.out.println("NEW study  = " + getView().getValueString("studyName"));
		String newStudyName = getView().getValueString("studyName");

		// Add configQuestionUse for each Question to this Study

		try {
			Study newStudy = new Study();

			System.out.println("about to copy bean");
			// newStudy = (Study) BeanUtils.cloneBean(study); clone and copy properties fail
			// due to multiple refs to charactericsresources
			System.out.println("done  copy bean");
			newStudy.setStudyName(newStudyName);
			newStudy.setProjectlz(study.getProjectlz());
			newStudy.setReferenceYear(study.getReferenceYear());
			newStudy.setStartDate(study.getStartDate());
			newStudy.setEndDate(study.getEndDate());

			newStudy.setNotes(study.getNotes());
			newStudy.setAltCurrency(study.getAltCurrency());
			newStudy.setAltExchangeRate(study.getAltExchangeRate());

			newStudy.setDescription(study.getDescription());

			// newStudy.setId(null);
			newStudy.setSite(study.getSite());

			System.out.println("new site = " + newStudy.getSite().getLocationdistrict());

			XPersistence.getManager().persist(newStudy);

			System.out.println("new study id = " + newStudy.getId());
			System.out.println("old study id = " + study.getId());
			String oldStudyId = study.getId().toString();
			String newStudyId = newStudy.getId().toString();
			// now do Characteristcs/assets etc

			List<WGCharacteristicsResource> charResources = XPersistence.getManager()
					.createQuery("from WGCharacteristicsResource where study_ID = '" + oldStudyId + "'")
					.getResultList();

			System.out.println("res found =  = " + charResources.size());

			WGCharacteristicsResource newCharResource = null;

			for (int i = 0; i < charResources.size(); i++) {
				newCharResource = new WGCharacteristicsResource();
				newCharResource.setResourcesubtype(charResources.get(i).getResourcesubtype());

				newCharResource.setStudy(newStudy);
				newCharResource.setWgresourceunit(charResources.get(i).getWgresourceunit());

				System.out.println(
						"new char res = " + newCharResource.getResourcesubtype().getIdresourcesubtype() + " " + i);

				newCharResource.setIdwgresource(null);
				XPersistence.getManager().persist(newCharResource);
			}

		} catch (Exception ex) {

			// failed to add topic questions to study due to duplicates or other failure
			addMessage("Failed to create Study " + ex);
			closeDialog();
		}
		closeDialog();

	}

}
