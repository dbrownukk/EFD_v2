package efd.tests;

import java.util.List;

import javax.persistence.Query;

import org.openxava.jpa.XPersistence;
import org.openxava.tests.ModuleTestBase;

import efd.model.LivelihoodZone;
import efd.model.Project;
import efd.model.Study;

public class ModellingScenarioTest extends ModuleTestBase {

	public ModellingScenarioTest(String testName) {
		super(testName, "IDAPS_Livelihoods_TESTING", "ModellingScenario");
		

	}

	public void testCreateUpdateDeleteModellingScenario() throws Exception {

		runReport();
		newProject();
		newStudy();
	

	}

	private void runReport() throws Exception {
		assertListNotEmpty();
		execute("List.viewDetail", "row=0");
		String title = getValue("title");
		assertNotNull(title);
		execute("GoModellingReport.Report");
		assertDialog();
		
		execute("ModellingReports.Run");
		
		assertError("Select at least one Community or Site");
		
		
		closeDialog();
		execute("Mode.list");
		

	}

	private void newProject() throws Exception {
		execute("CRUD.new");

		setValue("title", "JUNIT Test ModScenario");

		setValue("author", "JUNIT Author");
		setValue("date", "12/03/2019");

		Query query = XPersistence.getManager().createQuery("from Project");
		query.setMaxResults(1);
		List<Project> resultList = query.getResultList();
		// Need a Project with a LZ
		for (Project project : resultList) {
			if (project.getLivelihoodZone().size() != 0) {
				setValue("project.projectid", project.getProjectid());
				for (LivelihoodZone livelihoodZone : project.getLivelihoodZone()) {
					setValue("livelihoodZone.lzname", livelihoodZone.getLzname());
					break;
				}

				break;
			}
		}

		execute("TypicalNotResetOnSave.save");
		// assertMessage("Study created successfully"); // message on screen on success

		assertValue("title", "JUNIT Test ModScenario");
		execute("CRUD.delete");

		assertNoErrors();
	}

	private void newStudy() throws Exception {
		execute("CRUD.new");

		setValue("title", "JUNIT Test ModScenario");

		setValue("author", "JUNIT Author");
		setValue("date", "12/03/2019");

		Query query = XPersistence.getManager().createQuery("from Study");
		query.setMaxResults(1);
		List<Study> resultList = query.getResultList();

		setValue("study.id", resultList.get(0).getId());

		execute("TypicalNotResetOnSave.save");
		// assertMessage("Study created successfully"); // message on screen on success

		assertValue("title", "JUNIT Test ModScenario");
		execute("CRUD.delete");

		assertNoErrors();
	}
}
