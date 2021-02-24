package efd.utils;

/**
* This Class has functions to upload & download large files from server.
* @author Vikrant  
*/
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
 
public class fileUploadDownload {
 
	/**
	 * @param args
	 * @throws URISyntaxException 
	 * @throws IOException 
	
	public static void main(String[] args) throws IOException, URISyntaxException {
 
		uploadFileToServer("video.mp4","http://mysite.com/upload.php");
		downloadFileFromServer("video.mp4","http://mysite.com/video.mp4");
	}
  */
	/**
	 * This function upload the large file to server with other POST values.
	 * @param filename
	 * @param targetUrl
	 * @return
	 */
	public static String uploadFileToServer(String filename, String targetUrl) {
	    String response = "error";
	    HttpURLConnection connection = null;
	    DataOutputStream outputStream = null;
 
	    String pathToOurFile = filename;
	    String urlServer = targetUrl;
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
 
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1 * 1024;
	    try {
	        FileInputStream fileInputStream = new FileInputStream(new File(
	                pathToOurFile));
 
	        URL url = new URL(urlServer);
	        connection = (HttpURLConnection) url.openConnection();
 
	        // Allow Inputs & Outputs
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setChunkedStreamingMode(1024);
	        // Enable POST method
	        connection.setRequestMethod("POST");
 
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        connection.setRequestProperty("Content-Type",
	                "multipart/form-data; boundary=" + boundary);
 
	        outputStream = new DataOutputStream(connection.getOutputStream());
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String token = "anyvalye";
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"Token\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + token.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(token + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String taskId = "anyvalue";
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + taskId.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(taskId + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String connstr = null;
	        connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
	                + pathToOurFile + "\"" + lineEnd;
 
	        System.out.println("connstr = " + connstr);
	        outputStream.writeBytes(connstr);
	        outputStream.writeBytes(lineEnd);
 
	      	bytesAvailable = fileInputStream.available();
	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        buffer = new byte[bufferSize];
	        
	        System.out.println("buffersize = "+bufferSize);
 
	        // Read file
	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	        System.out.println("Image length " + bytesAvailable + "");
	        try {
	            while (bytesRead > 0) {
	                try {
	                	
	                    outputStream.write(buffer, 0, bufferSize);
	                   
	                } catch (OutOfMemoryError e) {
	                    e.printStackTrace();
	                    response = "outofmemoryerror";
	                    return response;
	                }
	                
	                bytesAvailable = fileInputStream.available();
	               
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	              
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            response = "error";
	            return response;
	        }
	        System.out.println("upload 11");
	        outputStream.writeBytes(lineEnd);
	        System.out.println("upload 22");
	        outputStream.writeBytes(twoHyphens + boundary + twoHyphens
	                + lineEnd);
	        System.out.println("upload 33");
	        // Responses from the server (code and message)
	        int serverResponseCode = connection.getResponseCode();
	        String serverResponseMessage = connection.getResponseMessage();
	        System.out.println("Server Response Code " + " " + serverResponseCode);
	        System.out.println("Server Response Message "+ serverResponseMessage);
 
	        if (serverResponseCode == 200) {
	            response = "true";
	        }else
	        {
	        	response = "false";
	        }
	        System.out.println("upload 34");
	        fileInputStream.close();
	        System.out.println("upload 35");
	        outputStream.flush();
	        System.out.println("upload 36");
	        connection.getInputStream();
	        System.out.println("upload 37");
	        //for android InputStream is = connection.getInputStream();
	        java.io.InputStream is = connection.getInputStream();
	        System.out.println("upload 38");
			int ch;
			StringBuffer b = new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
				System.out.println("upload 40 b = "+b.toString());
			}
			System.out.println("upload 39");
			String responseString = b.toString();
			System.out.println("response string is" + responseString); //Here is the actual output
 
	        outputStream.close();
	        outputStream = null;
 
	    } catch (Exception ex) {
	        // Exception handling
	        response = "error";
	        System.out.println("Send file Exception" + ex.getMessage() + "");
	        ex.printStackTrace();
	    }
	    return response;
	}
 
	/**
	 * This function download the large files from the server
	 * @param filename
	 * @param urlString
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void downloadFileFromServer(String filename, String urlString) throws MalformedURLException, IOException
    {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
    	    URL url = new URL(urlString);
 
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(filename);
 
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
                System.out.println(count);
            }
        }
        finally
        {
            if (in != null)
                    in.close();
            if (fout != null)
                    fout.close();
        }
        System.out.println("Done");
    }
}