package efd.tests;

import java.text.*;
import java.util.*;

import org.openxava.test.tests.*;
import org.openxava.tests.*;


public class ProjectTest extends CustomizeListTestBase{

	
	public ProjectTest(String testName) {
		super(testName, "Project");
		
	}
	
	public void testCreateUpdateDeleteProject() throws Exception{
		
	
		
		login("oihm", "oihm2019");
		System.out.println("done login");
		//Create
		waitAJAX();
		execute("CRUD.new");
		System.out.println("done New");
		setValue("projecttitle","JUNIT Test Project");
		System.out.println("done Title");
		setValue("pdate",getCurrentDate());
		System.out.println("done Date");
		waitAJAX();
		setValue("altCurrency","GBP");
		setValue("altExchangeRate","1.11");
		setValue("notes","JUNIT Test Project Notes");
		//waitAJAX();
		execute("TypicalNotResetOnSave.save");

		System.out.println("done save");
		waitAJAX();
		//assertMessage("Project created successfully");    // message on screen on success
		assertNoErrors();
		
		//assertValue("projectitle","JUNIT Test Project");
		
		
	}
	
	
	private String getCurrentDate() { // Current date as string in short format
	    return DateFormat.getDateInstance( // The standard way to do it with Java
	            DateFormat.SHORT).format(new Date());
	}
	

}
