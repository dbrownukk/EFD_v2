package efd.actions;


import org.openxava.actions.*;


public class RemoteMysqlDump extends ViewBaseAction implements IForwardAction{

	String mysqlphpdump = "http://localhost:8082/exp2.php";
	
	public void execute() throws Exception {
		
	}

	@Override
	public String getForwardURI() {
		// TODO Auto-generated method stub
		return mysqlphpdump;
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return false;
	}
}
