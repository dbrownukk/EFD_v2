package efd.actions;

import java.awt.*;
import java.io.*;
import java.net.*;

import org.apache.commons.beanutils.*;
import org.openxava.actions.*;
import org.openxava.jpa.*;

import com.openxava.naviox.model.*;

import efd.model.*;
import efd.utils.EfdPreferences;

public class LoadRemoteToStage extends ViewBaseAction  {
	// public class LoadRemoteToStage extends ViewBaseAction {

	String loadremote = "load_remote.html";

	/* check efd.properties for setting of baseURL */

	String url = EfdPreferences.getBaseURL() + "load_remote.html";

	public void execute() throws Exception {
		System.out.printf("in execute url = " + url);
		/* upload.php takes the exported data file and adds to efd_remote_stage */

		//String currentOrg = Organizations.getCurrent(getRequest());
		//url = url + "?org=" + currentOrg;
		//System.out.println("Current org = " + currentOrg);
		System.out.println("URL = "+url);
		HttpURLConnection connection = null;
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
			}
			

		} catch (

				Exception ex) {
		}

		addError("Failed to load Remote Project ");
		

	}


}
