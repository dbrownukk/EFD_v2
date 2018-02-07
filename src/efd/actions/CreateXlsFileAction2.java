package efd.actions;

/* Write XLS template for Community Interview */

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.view.*;
import org.openxava.web.servlets.*;

import com.openxava.naviox.model.*;

import org.openxava.tab.*;

import efd.model.*;

//public class CreateXlsFileAction2 extends CollectionElementViewBaseAction implements IForwardAction, JxlsConstants { // 1
public class CreateXlsFileAction2 extends CollectionBaseAction implements IForwardAction, JxlsConstants { // 1

	private String method;
	private int row;
	private String forwardURI = null;
	
	public void execute() throws Exception {
		
		
		//View view = getCollectionElementView();
		
		System.out.println("In spreadsheet 001"+getView().getKeyValuesWithValue());
		
		
		System.out.println("In spreadsheet 1"+getCollectionElementView().getCollectionValues().get(row));
		
		Map  m = (Map) getCollectionElementView().getCollectionValues().get(row);
		System.out.println("m1 = "+m.values());
		System.out.println("m2 = "+m.keySet());
		System.out.println("m3 = "+m.entrySet());
		System.out.println("m4 = "+m.get("wgid"));
		
		
		
		
		//System.out.println("In spreadsheet 1"+list.toString());
		JxlsWorkbook scenario = createScenario();
		System.out.println("In spreadsheet 2");
		getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, scenario); // 2
		System.out.println("In spreadsheet 3");
		setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis()); // 3
		System.out.println("In spreadsheet 4");
		/* Now to reset */
		
		//getView().setModelName("Community");
		//getView().clear();
	}

		// setControllers("Return");
		
		
		public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

		private JxlsWorkbook createScenario() throws Exception {

			int i = 1;
			int j = 2;
			int w = 30;
			int rows;
			int col;
			Map key;
			List wgl;
			
			/* Get EFD Project Details */

			/* Get WealthGroup data */
			Map  n = (Map) getCollectionElementView().getCollectionValues().get(row);
			System.out.println("n1 = "+n.values());
			System.out.println("n2 = "+n.keySet());
			System.out.println("n3 = "+n.entrySet());
			System.out.println("n4 = "+n.get("wgid"));
			
			
			String wgid = (String) n.get("wgid") ;
			System.out.println("In create scenario 1 ");

			
			System.out.println("wgid = "+wgid);
			
			WealthGroup wealthgroup = XPersistence.getManager().find(WealthGroup.class, wgid);
			System.out.println("In careetscenario 22");
			
			/* Get Community Data */
			
			
			Community community = XPersistence.getManager().find(Community.class,
					wealthgroup.getCommunity().getCommunityid());
			System.out.println("In careetscenario 222");
			Project project = XPersistence.getManager().find(Project.class, community.getProjectlz().getProjectid());
			System.out.println("In careetscenario 2222");
			/****************** Need to get charresource and types using getmanager and iterate ********************/
			
			// List wgcharacteristicsresource =  
			
			Query query = XPersistence.getManager().createQuery("select idwgresource from WGCharacteristicsResource where wgid = '"+wgid+"'");
			List chrs = query.getResultList();
					
			WGCharacteristicsResource 	wgcharacteristicsresource = XPersistence.getManager().find(WGCharacteristicsResource.class,
					chrs.get(0));	
					
			System.out.println("In careetscenario 3333" +wgcharacteristicsresource.getResourcesubtype().getResourcetypename());
			System.out.println("In careetscenario 5555" +wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype());
					
					
			ResourceType rst = XPersistence.getManager().find(ResourceType.class,wgcharacteristicsresource.getResourcesubtype().getResourcetype().getIdresourcetype() );
			System.out.println("In careetscenario 4444"+rst.getResourcetypename());
		
			System.out.println("In careetscenario 22222");
			//System.out.println("wgchr 1 = "+wgcharacteristicsresource.getResourcesubtype().getResourcesubtypeunit().toString());			
			
			
			
			
			Map[] participantKeys;

			if (getView().getSubview("wgcharacteristicsresource").getCollectionTab().hasSelected()) {
				participantKeys = getView().getSubview("wgcharacteristicsresource").getCollectionTab().getSelectedKeys();
			} else {
				participantKeys = getView().getSubview("wgcharacteristicsresource").getCollectionTab().getAllKeys();
			}


			List<WGCharacteristicsResource> wgr = getView().getSubview("wgcharacteristicsresource").getCollectionObjects();

			int num_wgr = wgr.size();
			String resourcesubtypeid;
			String resourcetypeid;

		

			/* XLS File Name */
			JxlsWorkbook scenarioWB = new JxlsWorkbook(project.getProjecttitle());
			JxlsStyle boldRStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(RIGHT);
			JxlsStyle boldTopStyle = scenarioWB.addStyle(TEXT).setBold().setAlign(LEFT);
			JxlsStyle borderStyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN,
					BORDER_THIN, BORDER_THIN);
			JxlsStyle textstyle = scenarioWB.addStyle(TEXT).setAlign(RIGHT).setCellColor(LIGHT_GREEN).setTextColor(BLACK);
			JxlsStyle datestyle = scenarioWB
					.getDefaultDateStyle(); /*
											 * seems to e a bug to stop setting
											 * other params
											 */

			/* XLS Sheets */

			JxlsSheet Interview = scenarioWB.addSheet("Interview Details");
			JxlsSheet Asset = scenarioWB.addSheet("Assets");
			JxlsSheet Crop = scenarioWB.addSheet("Crops");
			JxlsSheet LS = scenarioWB.addSheet("LS");
			JxlsSheet Emp = scenarioWB.addSheet("EMP");
			JxlsSheet Transfer = scenarioWB.addSheet("Transfers");
			JxlsSheet Wildfood = scenarioWB.addSheet("WILD FOODS");
			JxlsSheet Foodpurchase = scenarioWB.addSheet("FOOD PURCHASE");
			JxlsSheet Nonfoodpurchase = scenarioWB.addSheet("NON FOOD PURCHASE");

			
			
			
			return scenarioWB;
			/* end XLS setup */
		
		}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getForwardURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inNewWindow() {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void setForwardURI(String forwardURI) {
		this.forwardURI = forwardURI;
	}
	
	
	
	
	
}
