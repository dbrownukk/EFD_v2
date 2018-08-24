package efd.tests;

import org.openxava.tests.*;

 
public class ProjectTest extends ModuleTestBase { // Must extend from ModuleTestBase
 
    public ProjectTest(String testProj) {
        super(testProj, "EFD_v2", 
                "Project"); 
    }
 
    // The test methods must start with 'test'
    public void testProjectCreateReadUpdateDelete() throws Exception {
       
    login("admin","admin");

	
	execute("CRUD.new");
	setValue("projecttitle","JUNIT Projecttitle");
	setValue("pdate","10/13/17 10:54 AM");
	
	execute("CRUD.save");
	
    }
    
}
