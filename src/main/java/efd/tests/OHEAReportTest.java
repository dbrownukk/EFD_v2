package efd.tests;

import efd.rest.domain.Project;
import org.openxava.jpa.XPersistence;
import org.openxava.tests.ModuleTestBase;

import javax.persistence.Query;

public class OHEAReportTest extends ModuleTestBase{

	public OHEAReportTest(String testName) {
		super(testName, "IDAPS_Livelihoods_QA", "OHEAReport");

	}

	public void testReport() throws Exception {

	
	
		Query qProject = XPersistence.getManager().createQuery("from Project where projecttitle = 'NIMFRU'");
		Project project = (Project) qProject.getSingleResult();
		
		System.out.println("proj = "+project.getProjectid()+" "+project.getProjecttitle());
		
		setValue("project.projectid", project.getProjectid());
		
		
		
		printHtml();
		
		System.out.println("exists = "+existsAction("LivelihoodZone.Report"));
		execute("LivelihoodZone.Report", "row=0");
		execute("LivelihoodZone.Report","row=1");
		

		System.out.println("done report dialog execute");
		
		assertMessage("Enter Custom Report Spec Name to Run"); // message on screen on success
		assertNoErrors();

		
		

	}

}