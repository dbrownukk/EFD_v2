package efd.tests;

import org.openxava.tests.ModuleTestBase;

public class CustomReportSpecOHEATest extends ModuleTestBase {

	public CustomReportSpecOHEATest(String testName) {
		super(testName, "IDAPS_Livelihoods_QA", "CustomReportSpecOHEA");

	}

	public void testCreateUpdateDeleteProject() throws Exception {

		execute("CRUD.new");
		System.out.println("done New");
		setValue("specName", "JUNIT Test CRS OHEA");

		execute("TypicalNotResetOnSave.save");

		System.out.println("done save");
		waitAJAX();
		assertMessage("Custom report spec OHEA created successfully"); // message on screen on success
		assertNoErrors();

		assertValue("specName", "JUNIT Test CRS OHEA");
		execute("CRUD.delete");

	}

}
