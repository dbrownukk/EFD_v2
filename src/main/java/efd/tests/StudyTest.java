package efd.tests;

import java.util.List;

import javax.persistence.Query;

import org.openxava.jpa.XPersistence;
import org.openxava.tests.ModuleTestBase;

import efd.model.Project;

public class StudyTest extends ModuleTestBase {

	public StudyTest(String testName) {
		super(testName, "IDAPS_Livelihoods_QA", "Study");

	}

	public void testCreateUpdateDeleteStudy() throws Exception {

		execute("CRUD.new");

		setValue("studyName", "JUNIT Test Study Name");

		System.out.println("set name done");

		setValue("description", "JUNIT Study Description");
		setValue("startDate", "12/03/2019");
		setValue("endDate", "12/03/2019");
		setValue("referenceYear", "2018");
		System.out.println("about to  assign project");
		Query query = XPersistence.getManager().createQuery("from Project");
		query.setMaxResults(1);
		List<Project> resultList = query.getResultList();
		
		setValue("projectlz.projectid", resultList.get(0).getProjectid());
		System.out.println("done assign project");
		execute("TypicalNotResetOnSave.save");
		// assertMessage("Study created successfully"); // message on screen on success

		assertValue("studyName", "JUNIT Test Study Name");
		execute("CRUD.delete");

		assertNoErrors();

	}

}
