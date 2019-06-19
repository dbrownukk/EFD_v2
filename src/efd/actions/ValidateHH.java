/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.jpa.*;
import org.openxava.view.*;
import org.openxava.view.View;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;

import efd.model.*;
import efd.model.Asset.*;

/* Read XLS Community Interview  spreadsheet */

public class ValidateHH extends CollectionBaseAction implements IForwardAction {

	public void execute() throws Exception {

		int row = 0;
		String forwardURI = null;

		int i = 0;
		Integer rowNumber = 0;
		Integer cellNumber = 0;
		Integer sheetNumber = 0;
		Boolean isinvalid = false;

		String[] cellArray = null;
		int arrayCount = 0;

		String sdate;
		String interviewers;
		String typeOfYear;
		String stest;
		Double dtest;
		Boolean messaging = false;

		String hhid = getView().getValueString("id");

		Household hhi = XPersistence.getManager().find(Household.class, hhid);
		if (hhi.getSpreadsheet().isEmpty()) {
			addError("Upload completed Interview Spreadsheet before validating");
			return;
		} else if (hhi.getStatus().equals(WealthGroupInterview.Status.Generated)) {
			addError("Cannot Validate Interview Spreadsheet - Upload and Parse Spreadsheet first ");
			return;
		} else if (hhi.getStatus().equals(WealthGroupInterview.Status.Uploaded)) {
			addError("Cannot Validate Interview Spreadsheet - Parse Spreadsheet first");
			return;
		} else if (hhi.getStatus().equals(WealthGroupInterview.Status.Validated)) {
			addError("Cannot Validate Interview Spreadsheet Data - Already Validated");
			return;
		}

		
		
		System.out.println("DONE hhi ");
		/*************************************************************************************************/
		/*
		 * VALIDATE EACH Resource Asset type
		 */
		List<AssetLand> al = (List<AssetLand>) hhi.getAssetLand();
		List<AssetLiveStock> als = (List<AssetLiveStock>) hhi.getAssetLiveStock();
		List<AssetTradeable> at = (List<AssetTradeable>) hhi.getAssetTradeable();
		List<AssetFoodStock> afs = (List<AssetFoodStock>) hhi.getAssetFoodStock();
		List<AssetTree> atree = (List<AssetTree>) hhi.getAssetTree();
		List<AssetCash> ac = (List<AssetCash>) hhi.getAssetCash();
		List<Crop> crop = (List<Crop>) hhi.getCrop();
		List<LivestockSales> lss = (List<LivestockSales>) hhi.getLivestockSales();
		List<LivestockProducts> lsp = (List<LivestockProducts>) hhi.getLivestockProducts();
		List<Employment> emp = (List<Employment>) hhi.getEmployment();
		List<Transfer> tran = (List<Transfer>) hhi.getTransfer();
		List<WildFood> wf = (List<WildFood>) hhi.getWildFood();
		List<Inputs> ins = (List<Inputs>) hhi.getInputs();
		
		
		Collection<HouseholdMember> householdMembers = hhi.getHouseholdMember();
		
		

		System.out.println("DONE LISTS ");
		
		for (i = 0; i < al.size(); i++) {
			if (!al.get(i).getStatus().equals(Status.Valid)) {
				
				addMessage("Land Assets still Invalid or Unchecked");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE LAND ");
		for (i = 0; i < als.size(); i++) {

			if (!als.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Livestock Assets still Invalid or Unchecked");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE LS ");
		for (i = 0; i < at.size(); i++) {
	
			if (!at.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Tradeable Assets still Invalid or Unchecked");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE Trade ");
		for (i = 0; i < afs.size(); i++) {
			if (!afs.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Food Stock Assets still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE FS ");
		for (i = 0; i < atree.size(); i++) {
			if (!atree.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Tree Assets still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE TREE ");
		for (i = 0; i < ac.size(); i++) {
			if (!ac.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Cash Assets still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE CASH ");
		
		for (i = 0; i < crop.size(); i++) {
			if (!crop.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Crop Assets still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < lss.size(); i++) {
			if (!lss.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Livestock Sales still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < lsp.size(); i++) {
			if (!lsp.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Livestock Products still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < emp.size(); i++) {
			if (!emp.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Employment still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < tran.size(); i++) {
			if (!tran.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Transfers still Invalid or Unchecked");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE TRANS ");
		for (i = 0; i < wf.size(); i++) {
			if (!wf.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Wild Food still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}
		//System.out.println("DONE WF ");
		for (i = 0; i < ins.size(); i++) {
			if (!ins.get(i).getStatus().toString().equals("Valid")) {
				addMessage("Inputs still Invalid or Unchecked ");
				isinvalid = true;
				break;
			}
		}

		
		for (Iterator<HouseholdMember> iterator = householdMembers.iterator(); iterator.hasNext();) {
			HouseholdMember householdMember = iterator.next();
			
			if(householdMember.getMonthsAway() >12)
			{
			addMessage("Household Member Months Away is greater than 12 - Valid values are between 0 and 12 ");
			isinvalid = true;
			break;
			}
			if(householdMember.getAge()+)
			
			
			
		}
		
		
		//System.out.println("DONE NFP ");
		if (isinvalid)
			addMessage("Validation Complete with Invalid Assets");
		else 
		{
			hhi.setStatus(WealthGroupInterview.Status.Validated);	
			getView().setEditable(false);
			addActions("SetEditable.SetEditableHH");
			getView().refresh();
			addMessage("Validation Complete");
		}
		
		return;
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

}
