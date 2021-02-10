package efd.tests;

import java.text.*;
import java.util.*;

import org.openxava.tests.ModuleTestBase;

public class CustomReportSpecTest extends ModuleTestBase {

	public CustomReportSpecTest(String testName) {
		super(testName, "IDAPS_Livelihoods_QA", "CustomReportSpec");

	}

	public void testCreateUpdateDeleteProject() throws Exception {

		execute("CRUD.new");
		System.out.println("done New");
		setValue("specName", "JUNIT Test CRS OIHM");

		execute("TypicalNotResetOnSave.save");

		System.out.println("done save");
		waitAJAX();
		assertMessage("Report Spec created successfully"); // message on screen on success
		assertNoErrors();

		assertValue("specName", "JUNIT Test CRS OIHM");
		execute("CRUD.delete");

	}

}
