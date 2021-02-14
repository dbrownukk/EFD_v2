package efd.actions;

import java.awt.*;
import java.io.*;
import java.net.*;

import java.sql.*;
import java.util.*;
import java.util.List;

import efd.model.Project;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;


import efd.utils.EfdPreferences;


import java.io.IOException;

//public class LoadRemoteToOrg extends OnChangePropertyBaseAction implements IForwardAction, IJavaScriptPostAction{
public class LoadRemoteToOrg extends OnChangePropertyBaseAction implements IProcessLoadedFileAction {

	String loadremote = "load_remote.html";

	/* check efd.properties for setting of baseURL */

	String urlhtml = EfdPreferences.getBaseURL() + "load_remote.html";
	String urlupload = EfdPreferences.getBaseURL() + "upload2.php";
	String urluploaddir = EfdPreferences.getBaseURL() + "uploads/";
	String urluploadfile = EfdPreferences.getBaseURL() + "uploads/uploaded_export.sql";
	
	public void execute() throws Exception {

		System.out.println("in execute loadremotetoorgchange  ");
		/* upload.php takes the exported data file and adds to efd_remote_stage */
		Connection con = null;

		String projectid = getView().getValueString("projectid");
		Project proj = XPersistence.getManager().find(Project.class, projectid);
		//String exportFile = proj.getStageFile();
		//System.out.println("project exp file   = " + exportFile);

		
	//	String currentOrg = Organizations.getCurrent(getRequest());
		// url = url+"?org="+currentOrg;
		//System.out.println("Current org = " + currentOrg);
		
		try {
			/****************************************************************************************/

			Project eProj = XPersistence.getManager().find(Project.class, projectid);

			con = DataSourceConnectionProvider.getByComponent("Project").getConnection();
			System.out.println("Connection = " + con.getCatalog());
			//PreparedStatement ps = con
			//		.prepareStatement("select id, data from oxfiles where ID =   '" + exportFile + "'");

			//ResultSet rs = ps.executeQuery();
			//rs.next();
			//System.out.println("rs id  = " + rs.getString("id"));
			//System.out.println("Export file after rs = " + eProj.getStageFile());

			//InputStream inputStream = rs.getBinaryStream("DATA"); /* get stream data as well */
 
			/* writes to file */
			//byte[] buffer = new byte[inputStream.available()];
			//inputStream.read(buffer);
		
            //File targetFile = new File("/tmp/remote_export.sql");
			//System.out.println("wrote file");
			//OutputStream outStream = new FileOutputStream(targetFile);
			//outStream.write(buffer);
			//outStream.close();
			//fileUploadDownload.uploadFileToServer("/tmp/remote_export.sql","http://localhost/uploaded_export_drb.sql");

			//BufferedReader reader = null;
			
			//reader = new BufferedReader(new FileReader("/tmp/remote_export.sql"));
		    

		    //System.out.println("reader = "+reader.readLine());
		        
			
			
			//System.out.println("Upload file to "+urluploadfile);
			//fileUploadDownload.uploadFileToServer("/tmp/remote_export.sql",urluploadfile);
			

			
            

            /* run PHP */
			//URL urlup = new URL(url);
			
			//System.out.println("in php upload "+urlup.toString());
			//URLConnection connection2 = urlup.openConnection();
			//connection2.setDoOutput(true);
            /* run PHP */
			
			
			//System.out.println("in php upload connection "+connection.toString());
			//connection.connect();
			
			/****************************************************************************************/

			 if (Desktop.isDesktopSupported()) { 
				 Desktop.getDesktop().browse(new URI(urlhtml)); 
			 		System.out.println("NOW Create new proj");
			 }
			
			
			//importSQL(inputStream);
			System.out.println("done inputstream in ");

			// File targetFile = new File("export_drb.sql");
			
			//inputStream.close();

			//ps.close();
			/*
			//Blob blob = rs.getBlob("DATA");
			//System.out.println("Blob  File size = " + blob.length());

			//InputStream in = blob.getBinaryStream();
			//OutputStream out = new FileOutputStream("export.sql");
			//byte[] buff = blob.getBytes(1, (int) blob.length());
			//System.out.printf("Wrote File size = " + blob.length());


			//out.write(buff);
			//out.close();



		
			 * 
	
			 * 
			 * System.out.printf("Create new proj"); Project newProject = new Project();
			 * 
			 * //XPersistence.setDefaultSchema("efd_remote_stage"); Project stageProject =
			 * (Project)
			 * XPersistence.getManager().createQuery("from efd_remote_stage.Project").
			 * getSingleResult();
			 * System.out.printf("Create new proj with title "+stageProject.getProjecttitle(
			 * ));
			 * 
			 * 
			 * BeanUtils.cloneBean(stageProject);
			 * System.out.printf("Create new proj 2 proj = "+stageProject.getProjecttitle())
			 * ; newProject.setProjectid(null);
			 * XPersistence.getManager().persist(newProject);
			 * 
			 * 
			 * 
			 * //}
			 */
		} catch (

		Exception ex) {
		}

		/* now insert single project into current Org */

	}
	public static void importSQL(InputStream in) throws SQLException, IOException
	{
		  final String DRIVER = "com.mysql.jdbc.Driver";
		  final String DB_URL = "jdbc:mysql://localhost:3306/efd_remote_stage";
		  final String DB_USER = "efd_remote_stage";
		  final String DB_PASSWORD = "efd";
		  Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		
		System.out.println("Connect = "+conn.getCatalog()); 
		//System.out.println("instream ="+in.read());
	    Scanner s = new Scanner(in);
	    s.next();
	    System.out.println("Scanner s = "+s.toString());
	    //s.useDelimiter("(;(\r)?\n)|(--\n)");
	    s.useDelimiter("(;(\r)?\n)|((\r)?\n)?(--)?.*(--(\r)?\n)");
	    System.out.println("Scanner s next  = "+s.hasNext());
	    Statement st = null;
	    
	    while(s.hasNext())
	    	System.out.println(s.next());
	    
	    
	    try
	    {
	        st = conn.createStatement();
	        while (s.hasNext())
	        {
	            String line = s.next();
	            //System.out.println("Exec line"+line);
	            if (line.startsWith("/*!") && line.endsWith("*/"))
	            {
	                int i = line.indexOf(' ');
	                line = line.substring(i + 1, line.length() - " */".length());
	            }

	            if (line.trim().length() > 0)
	            {
	            	
	                st.execute(line);
	            }
	        }
	    }
	    finally
	    {
	        if (st != null) st.close();
	    }

	}

	public static void execShellCmd(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[] { "/bin/bash", "-c", cmd });
            int exitValue = process.waitFor();
            System.out.println("exit value: " + exitValue);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println("exec response: " + line);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	@Override
	public void setFileItems(List fileItems) {
		// TODO Auto-generated method stub
		System.out.println("do something after file upload");
	}
	
	
}
