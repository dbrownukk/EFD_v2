package efd.tests;

import org.openxava.tests.*;



public class StudyTest extends ModuleTestBase{

	public StudyTest(String studyTest) {
		super(studyTest, "EFD_HM", "Study");
		
	}
	
	public void testCreateUpdateDeleteStudy() throws Exception{
		
		login("admin", "admin2018");
		assertNoErrors();
/*
		execute("CRUD.new");
		
		setValue("studyName", "JUNIT Test Study Name");

		setValue("altExchangeRate", "1.1");
		setValue("description","JUNIT Study Description");
		setValue("startDate","12/03/2019");
		setValue("endDate","12/03/2019");
		setValue("referenceYear","2018");
		setValue("projectlz","402881c06852858e016852862e330000");
		
		
		execute("CRUD.save");
		assertNoErrors();
		*/
		
		
		
	}
	
	

}
