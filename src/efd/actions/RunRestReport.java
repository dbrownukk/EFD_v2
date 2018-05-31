package efd.actions;

import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.util.*;

public class RunRestReport implements IForwardAction {

	@Override
	public void setErrors(Messages errors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Messages getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessages(Messages messages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Messages getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getForwardURI() {
		// TODO Auto-generated method stub
		// Needs encryting at some point or using SSO
		String un = "j_username=run";
		String pw = "j_password=run2018";
		return "http://idaps.walker.ac.uk:8080/jasperserver/rest_v2/reports/reports/SitesByCountry.html?"+un+"&"+pw;
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}
}