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

public class ValidateWGI extends CollectionBaseAction implements IForwardAction {

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

		String wgiid = getView().getValueString("wgiid");

		WealthGroupInterview wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
		if (wgi.getSpreadsheet().isEmpty()) {
			addError("Upload completed Interview Spreadsheet before validating");
			return;
		} else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Generated)) {
			addError("Cannot Validate Interview Spreadsheet - Upload and Parse Spreadsheet first ");
			return;
		} else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Uploaded)) {
			addError("Cannot Validate Interview Spreadsheet - Parse Spreadsheet first");
			return;
		} else if (wgi.getStatus().equals(efd.model.WealthGroupInterview.Status.Validated)) {
			addError("Cannot Validate Interview Spreadsheet Data - Already Validated");
			return;
		}

		System.out.println("DONE wgi ");
		/*************************************************************************************************/
		/*
		 * VALIDATE EACH Resource Asset type
		 */
		List<AssetLand> al = (List<AssetLand>) wgi.getAssetLand();
		List<AssetLiveStock> als = (List<AssetLiveStock>) wgi.getAssetLiveStock();
		List<AssetTradeable> at = (List<AssetTradeable>) wgi.getAssetTradeable();
		List<AssetFoodStock> afs = (List<AssetFoodStock>) wgi.getAssetFoodStock();
		List<AssetTree> atree = (List<AssetTree>) wgi.getAssetTree();
		List<AssetCash> ac = (List<AssetCash>) wgi.getAssetCash();
		List<Crop> crop = (List<Crop>) wgi.getCrop();
		List<LivestockSales> lss = (List<LivestockSales>) wgi.getLivestockSales();
		List<LivestockProducts> lsp = (List<LivestockProducts>) wgi.getLivestockProducts();
		List<Employment> emp = (List<Employment>) wgi.getEmployment();
		List<Transfer> tran = (List<Transfer>) wgi.getTransfer();
		List<WildFood> wf = (List<WildFood>) wgi.getWildFood();
		List<FoodPurchase> fp = (List<FoodPurchase>) wgi.getFoodPurchase();
		List<NonFoodPurchase> nfp = (List<NonFoodPurchase>) wgi.getNonFoodPurchase();

		for (i = 0; i < al.size(); i++) {
			if (al.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Land Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < als.size(); i++) {

			if (als.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Livestock Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < at.size(); i++) {
	
			if (at.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Tradeable Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < afs.size(); i++) {
			if (afs.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Food Stock Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < atree.size(); i++) {
			if (atree.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Tree Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < ac.size(); i++) {
			if (ac.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Cash Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		
		for (i = 0; i < crop.size(); i++) {
			if (crop.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Crop Assets still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < lss.size(); i++) {
			if (lss.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Livestock Sales still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < lsp.size(); i++) {
			if (lsp.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Livestock Products still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < emp.size(); i++) {
			if (emp.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Employment still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < tran.size(); i++) {
			if (tran.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Transfers still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < wf.size(); i++) {
			if (wf.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Wild Food still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		for (i = 0; i < fp.size(); i++) {
			if (fp.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Food Purchase still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		for (i = 0; i < nfp.size(); i++) {
			if (nfp.get(i).getStatus().toString().equals("Invalid")) {
				addMessage("Non Food Purchase still set to Invalid ");
				isinvalid = true;
				break;
			}
		}
		
		if (isinvalid)
			addMessage("Validation Complete with Invalid Assets");
		else 
		{
			wgi.setStatus(WealthGroupInterview.Status.Validated);
			/*
			View vw;
			vw = getCollectionElementView();
			vw.setEditable("AssetLand.status", false);
		
			*/
			removeActions("TypicalNotResetOnSave");
			
			
			
			getView().refresh();
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
