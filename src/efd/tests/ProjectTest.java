package efd.tests;

import java.text.*;
import java.util.*;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.*;

import org.openxava.tests.*;


public class ProjectTest extends ModuleTestBase{

	public ProjectTest(String testName) {
		super(testName, "EFD_HM", "Project");
		
	}
	
	public void testCreateUpdateDeleteProject() throws Exception{
		
		Date date = new Date();
		
		login("admin", "admin2018");
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
