
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import org.apache.commons.beanutils.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import efd.model.*;
import efd.model.ConfigQuestion.*;

public class CopyStudy extends ViewBaseAction {

	public void execute() throws Exception {

		System.out.println("in copy study ");

		
		
		
		
		Object studyId = getPreviousView().getValue("id");

		// get topic questions and study
		String message = null;
		Study study = XPersistence.getManager().find(Study.class, studyId);
		System.out.println("study after query = " + study.getStudyName());
		System.out.println("NEW study  = " + getView().getValueString("studyName"));
		String newStudyName = getView().getValueString("studyName");
		
		

		// Add configQuestionUse for each Question to this Study

		try {
			Study newStudy = new Study();

			newStudy.setStudyName(newStudyName);
			newStudy.setProjectlz(study.getProjectlz());
			message = "Done ProjectLZ";
			newStudy.setReferenceYear(study.getReferenceYear());
			newStudy.setStartDate(study.getStartDate());
			newStudy.setEndDate(study.getEndDate());
			message = "Done End Date";
			newStudy.setNotes(study.getNotes());
			message = "Done AltExchangeRate ";
			newStudy.setDescription(study.getDescription());
			message = "Done Description ";
			newStudy.setId(null);
			newStudy.setVersion(null);

			newStudy.setSite(study.getSite());

			message = "Done Site ";



			message = "Done print of New Study ";

			XPersistence.getManager().persist(newStudy);
			message = " Done persist New Study";
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
				System.out.println("rst to copy = "+charResources.get(i).getResourcesubtype());
				
				// edge case FoodStocks - check if type = foodstock 
				// Food Stocks is not an RST 
				
				
				
				newCharResource = (WGCharacteristicsResource) BeanUtils.cloneBean(charResources.get(i));
				
				//newCharResource.setResourcesubtype(charResources.get(i).getResourcesubtype());
				
				//newCharResource.setWgresourceunit(charResources.get(i).getWgresourceunit());
				
				newCharResource.setStudy(newStudy);
				
				System.out.println(
						"new char res = " + newCharResource.getResourcesubtype().getIdresourcesubtype() + " " + i);

				newCharResource.setIdwgresource(null);
				XPersistence.getManager().persist(newCharResource);
			}

			// Standard of Living

			// get current standard of livings for orig study

			List<StdOfLivingElement> stdels = XPersistence.getManager()
					.createQuery("from StdOfLivingElement where study_ID = '" + oldStudyId + "'").getResultList();

			StdOfLivingElement stdel = null;
			System.out.println("standard of livings = " + stdels.size());
			for (int i = 0; i < stdels.size(); i++) {
				stdel = new StdOfLivingElement();

				BeanUtils.copyProperties(stdel, stdels.get(i));
				System.out.println("done bean copy" + stdel.getResourcesubtype().getResourcetypename());
				
				stdel.setId(null);
				stdel.setStudy(newStudy);  
				stdel.setVersion(null);
				XPersistence.getManager().persist(stdel);
			}
			
			// Default Diet

			// get current Default Diet for orig study

			List<DefaultDietItem> ddiets = XPersistence.getManager()
					.createQuery("from DefaultDietItem where study_ID = '" + oldStudyId + "'").getResultList();

			DefaultDietItem ddiet = null;

			System.out.println("number of ddiets items = " + ddiets.size());
			for (int i = 0; i < ddiets.size(); i++) {
				ddiet = new DefaultDietItem();

				// BeanUtils.copyProperties(ddiet, ddiets.get(i)); // FAILS

				ddiet.setNotes(ddiets.get(i).getNotes());
				ddiet.setPercentage(ddiets.get(i).getPercentage());
				ddiet.setResourcesubtype(ddiets.get(i).getResourcesubtype());
				ddiet.setStudy(newStudy);
				ddiet.setUnitPrice(ddiets.get(i).getUnitPrice());

				System.out.println("done SET in ddiet " + ddiet.getResourcesubtype().getResourcetypename());
				//ddiet.setId(null);

				//ddiet.setVersion(null);
				System.out.println("about to persist ddiet");
				
				
				XPersistence.getManager().persist(ddiet);
				

				System.out.println("persisted ddiet");

			}
			
			// Config Questions

			// get current Questions for orig study

			List<ConfigQuestionUse> questions = XPersistence.getManager()
					.createQuery("from ConfigQuestionUse where study_ID = '" + oldStudyId + "'").getResultList();

			ConfigQuestionUse question = null;

			for (int i = 0; i < questions.size(); i++) {
				question = new ConfigQuestionUse();

				// BeanUtils.copyProperties(question, questions.get(i)); BEAN will not work with
				// sub collections
				question.setConfigQuestion(questions.get(i).getConfigQuestion());
				question.setConfigAnswer(null);
				question.setLevel(questions.get(i).getLevel());
				question.setNotes(questions.get(i).getNotes());
				question.setVersion(null);
				question.setId(null);
				question.setStudy(newStudy);
				//System.out.println("about to persist question use "+question.getLevel()+" "+question.getConfigQuestion().getId()+" "+question.getStudy().getId());
				XPersistence.getManager().persist(question);
				System.out.println("persisted question use" );
				
				if(question.getConfigQuestion().getLevel().equals(Level.Study))
						{
					System.out.println("in copy answer 1");
						ConfigAnswer answer = new ConfigAnswer();
						answer.setStudy(newStudy);
						answer.setAnswerType(question.getConfigQuestion().getAnswerType());
						System.out.println("in copy answer 2");
						answer.setConfigQuestionUse(question);
						answer.setId(null);
						answer.setVersion(null);
						System.out.println("in copy answer 3");
						XPersistence.getManager().persist(answer);
						System.out.println("in copy answer 4");
					
						}
				
			}
			
			
		} catch (Exception ex) {

			// failed to add topic questions to study due to duplicates or other failure
			closeDialog();
			addError("Failed to create Study " + ex + " " + message);
			return;

		}
		XPersistence.commit();
		closeDialog();
		addMessage("Successfully created new Study " + newStudyName);
		return;
	}

}
