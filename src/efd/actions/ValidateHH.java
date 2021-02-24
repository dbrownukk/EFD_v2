/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.jpa.*;
import org.openxava.view.*;
import org.openxava.view.View;

import java.math.*;
import java.util.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;

import efd.model.*;
import efd.model.Asset.*;
import efd.model.Transfer.*;

/* Read XLS Community Interview  spreadsheet */

public class ValidateHH extends CollectionBaseAction implements IForwardAction {

	public void execute() throws Exception {

		int row = 0;
		String forwardURI = null;

		int i = 0;
		Integer rowNumber = 0;
		Integer cellNumber = 0;
		Integer sheetNumber = 0;
		Boolean isInvalid = false;

		String[] cellArray = null;
		int arrayCount = 0;

		String sdate;
		String interviewers;
		String typeOfYear;
		String stest;
		Double dtest;
		boolean messaging = false;
		boolean landIsInvalid = false;
		boolean livestockIsInvalid = false;
		boolean tradeIsInvalid = false;
		boolean fsIsInvalid = false;
		boolean treeIsInvalid = false;
		boolean cashIsInvalid = false;
		boolean cropIsInvalid = false;
		boolean lssIsInvalid = false;
		boolean lspIsInvalid = false;
		boolean empIsInvalid = false;
		boolean transIsInvalid = false;
		boolean wfIsInvalid = false;
		boolean inpIsInvalid = false;
		double cropSum = 0.0;
		double lssSum = 0.0;
		double lspSum = 0.0;

		double inpSum = 0.0;
		double transSum = 0.0;
		double wfSum = 0.0;

		final double THRESHOLD = 0.1;

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

		System.out.println("DONE hhi = " + hhi.getHouseholdNumber());
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
		System.out.println("al list size = " + al.size());
		for (i = 0; i < al.size(); i++) {

			System.out.println("Land Validation ");
			/* Land Validations */

			if (!(al.get(i).getNumberOfUnits() >= 0)) {
				al.get(i).setStatus(Asset.Status.Invalid);
				landIsInvalid = true;
				System.out.println("Land Units not > = 0 ");
				break;
			} else if (al.get(i).getResourceSubType() == null) {
				landIsInvalid = true;
				al.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (!al.get(i).getStatus().equals(Status.Valid)) {
				landIsInvalid = true;
				al.get(i).setStatus(Asset.Status.Invalid);
				System.out.println("Land Units set Invalid ");
				break;
			}

		}

		/* Livestock Validation */
		for (i = 0; i < als.size(); i++) {
			System.out.println("Livetock Validation ");

			if (!(als.get(i).getNumberOwnedAtStart() >= 0)) {
				als.get(i).setStatus(Asset.Status.Invalid);
				livestockIsInvalid = true;
				break;
			} else if (als.get(i).getResourceSubType() == null) {
				livestockIsInvalid = true;
				als.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (als.get(i).getNumberOwnedAtStart() >= 0 && als.get(i).getPricePerUnit() < 0) {
				als.get(i).setStatus(Asset.Status.Invalid);
				livestockIsInvalid = true;
				break;
			} else if (!als.get(i).getStatus().toString().equals("Valid")) {
				// addMessage("Livestock Assets still Invalid or Unchecked");
				livestockIsInvalid = true;
				break;
			}

		}

		/* Tradeable Validation */
		for (i = 0; i < at.size(); i++) {
			System.out.println("Tradeable Validation ");

			if (at.get(i).getNumberOwned() < 0) {
				at.get(i).setStatus(Asset.Status.Invalid);
				tradeIsInvalid = true;
				break;
			} else if (at.get(i).getResourceSubType() == null) {
				tradeIsInvalid = true;
				at.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (at.get(i).getNumberOwned() >= 0 && at.get(i).getPricePerUnit() < 0) {
				at.get(i).setStatus(Asset.Status.Invalid);
				tradeIsInvalid = true;
				break;
			}

			else if (!at.get(i).getStatus().toString().equals("Valid")) {
				tradeIsInvalid = true;
				break;
			}

		}

		/* Food Stocks */
		for (i = 0; i < afs.size(); i++) {
			if (!(afs.get(i).getQuantity() >= 0)) {
				afs.get(i).setStatus(Asset.Status.Invalid);
				fsIsInvalid = true;
				break;
			} else if (afs.get(i).getResourceSubType() == null) {
				fsIsInvalid = true;
				afs.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (!afs.get(i).getStatus().toString().equals("Valid")) {
				fsIsInvalid = true;
				break;
			}

		}
		/* Trees */
		for (i = 0; i < atree.size(); i++) {

			if (!(atree.get(i).getNumberOwned() >= 0)) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;
				break;
			} else if (atree.get(i).getResourceSubType() == null) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;
				break;
			} else if (atree.get(i).getNumberOwned() >= 0 && atree.get(i).getPricePerUnit() < 0) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;
				break;
			}

			else if (!atree.get(i).getStatus().toString().equals("Valid")) {
				treeIsInvalid = true;
				break;
			}

		}

		/* Cash Validation */;
		for (i = 0; i < ac.size(); i++) {

			if (ac.get(i).getAmount().intValue() > 0 && ac.get(i).getExchangeRate().intValue() > 0) {
				ac.get(i).setStatus(Asset.Status.Valid);

			} else if (ac.get(i).getResourceSubType() == null) {
				ac.get(i).setStatus(Asset.Status.Invalid);
				cashIsInvalid = true;
				break;
			}

			else {

				System.out.println("Amount is not >0 " + ac.get(i).getAmount().intValue());
				ac.get(i).setStatus(Asset.Status.Invalid);
				cashIsInvalid = true;

			}
		}

		/* Crop Validation */

		for (i = 0; i < crop.size(); i++) {

			cropSum = crop.get(i).getPercentTradeMarket1() + crop.get(i).getPercentTradeMarket2()
					+ crop.get(i).getPercentTradeMarket3();

			if (crop.get(i).getUnitsSold() > 0 && crop.get(i).getPricePerUnit() < 0) {

				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;
				break;
			} else if (!(crop.get(i).getUnitsProduced() >= 0) || !(crop.get(i).getUnitsSold() >= 0)
					|| !(crop.get(i).getUnitsOtherUse() >= 0) || !(crop.get(i).getUnitsConsumed() >= 0)
					|| !(crop.get(i).getPercentTradeMarket1() >= 0) || !(crop.get(i).getPercentTradeMarket2() >= 0)
					|| !(crop.get(i).getPercentTradeMarket3() >= 0)) {

				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;
				break;
			} else if (Double.compare(cropSum, 0.0) != 0 && Double.compare(cropSum, 100.0) != 0) {
				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;
				break;
			}

			else if (!crop.get(i).getStatus().toString().equals("Valid")) {

				cropIsInvalid = true;
				break;
			} else if (crop.get(i).getResourceSubType() == null) {
				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;
				break;
			}
		}

		/* Livestock Sales Validation */

		for (i = 0; i < lss.size(); i++) {

			lssSum = lss.get(i).getPercentTradeMarket1() + lss.get(i).getPercentTradeMarket2()
					+ lss.get(i).getPercentTradeMarket3();

			if (lss.get(i).getUnitsSold() > 0 && lss.get(i).getPricePerUnit() < 0) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;
				break;
			} else if (lss.get(i).getResourceSubType() == null) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;
				break;
			} else if (!(lss.get(i).getUnitsSold() >= 0) || !(lss.get(i).getPercentTradeMarket1() >= 0)
					|| !(lss.get(i).getPercentTradeMarket2() >= 0) || !(lss.get(i).getPercentTradeMarket3() >= 0)) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;
				break;

			} else if (Double.compare(lssSum, 0.0) != 0 && Double.compare(lssSum, 100.0) != 0) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;
				break;
			}

			else if (!lss.get(i).getStatus().toString().equals("Valid")) {
				lssIsInvalid = true;
				break;
			}

		}

		/* Livestock Products Validation */

		for (i = 0; i < lsp.size(); i++) {

			lspSum = lsp.get(i).getPercentTradeMarket1() + lsp.get(i).getPercentTradeMarket2()
					+ lsp.get(i).getPercentTradeMarket3();
			System.out.println("tot = " + lspSum);
			if (lsp.get(i).getUnitsSold() > 0 && lsp.get(i).getPricePerUnit() < 0) {
				System.out.println("LSP 111");
				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = true;
				break;
			} else if (lsp.get(i).getResourceSubType() == null) {
				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = true;
				break;
			} else if (!(lsp.get(i).getUnitsProduced() >= 0) || !(lsp.get(i).getUnitsSold() >= 0)
					|| !(lsp.get(i).getUnitsOtherUse() >= 0) || !(lsp.get(i).getUnitsConsumed() >= 0)
					|| !(lsp.get(i).getPercentTradeMarket1() >= 0) || !(lsp.get(i).getPercentTradeMarket2() >= 0)
					|| !(lsp.get(i).getPercentTradeMarket3() >= 0)) {
				System.out.println("LSP 222");
				lspIsInvalid = true;
				lsp.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (Double.compare(lspSum, 0.0) != 0 && Double.compare(lspSum, 100.0) != 0) {
				System.out.println("LSP 333");
				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = false;
				break;
			}

			else if (!lsp.get(i).getStatus().toString().equals("Valid")) {
				System.out.println("LSP 444");
				lspIsInvalid = true;
				break;
			}

		}
	
		/* Employment Validation */

		// TODO Why is Food Payment Units Paid Work a string not a Double ??

		for (i = 0; i < emp.size(); i++) {
			if ((emp.get(i).getPeopleCount() < 0) || (emp.get(i).getUnitsWorked() < 0)
					|| (emp.get(i).getCashPaymentAmount() < 0)) {
				empIsInvalid = true;
				break;
			} else if (emp.get(i).getResourceSubType() == null) {
				emp.get(i).setStatus(Asset.Status.Invalid);
				empIsInvalid = true;
				break;
			} else if (!(emp.get(i).getStatus().toString().equals("Valid"))) {
				empIsInvalid = true;
				break;
			}

		}
	
		/* Transfers Validation */

		for (i = 0; i < tran.size(); i++) {

			transSum = tran.get(i).getPercentTradeMarket1() + tran.get(i).getPercentTradeMarket2()
					+ tran.get(i).getPercentTradeMarket3();

			if (tran.get(i).getUnitsSold() > 0 && tran.get(i).getPricePerUnit() < 0) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;
				break;
			} else if (tran.get(i).getResourceSubType() == null) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;
				break;
			} else if (!(tran.get(i).getPeopleReceiving() >= 0) || !(tran.get(i).getTimesReceived() >= 0)
					|| !(tran.get(i).getCashTransferAmount() >= 0)) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;
				break;
			} else if (tran.get(i).getTransferType() == TransferType.Cash
					&& tran.get(i).getFoodResourceSubType() != null) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (tran.get(i).getTransferType() != TransferType.Cash
					&& tran.get(i).getFoodResourceSubType() == null) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				break;
			}

			else if (!(tran.get(i).getUnitsSold() >= 0) || !(tran.get(i).getOtherUse() >= 0)
					|| !(tran.get(i).getUnitsConsumed() >= 0) || !(tran.get(i).getPercentTradeMarket1() >= 0)
					|| !(tran.get(i).getPercentTradeMarket2() >= 0) || !(tran.get(i).getPercentTradeMarket3() >= 0)) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				break;
			} else if (Double.compare(transSum, 0.0) != 0 && Double.compare(transSum, 100.0) != 0) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				break;
			}

			else if (!tran.get(i).getStatus().toString().equals("Valid")) {
				transIsInvalid = true;
				break;
			}

		}
		System.out.println("done transfers validation");
		/* Wildfood Validation */
		for (i = 0; i < wf.size(); i++) {
			wfSum = wf.get(i).getPercentTradeMarket1() + wf.get(i).getPercentTradeMarket2()
					+ wf.get(i).getPercentTradeMarket3();

			if (wf.get(i).getUnitsSold() > 0 && wf.get(i).getPricePerUnit() < 0) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
				break;
			} else if (!(wf.get(i).getUnitsProduced() >= 0) || !(wf.get(i).getUnitsSold() >= 0)
					|| !(wf.get(i).getOtherUse() >= 0) || !(wf.get(i).getUnitsConsumed() >= 0)
					|| !(wf.get(i).getPercentTradeMarket1() >= 0) || !(wf.get(i).getPercentTradeMarket2() >= 0)
					|| !(wf.get(i).getPercentTradeMarket3() >= 0)) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
				break;
			} else if (wf.get(i).getResourceSubType() == null) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
				break;
			} else if (Double.compare(wfSum, 0.0) != 0 && Double.compare(wfSum, 100.0) != 0) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
				break;
			}

			else if (!wf.get(i).getStatus().toString().equals("Valid")) {
				wfIsInvalid = true;
				break;
			}

		}
		
		/* Inputs Validation */
		for (i = 0; i < ins.size(); i++) {
			inpSum = ins.get(i).getPercentResource1() + ins.get(i).getPercentResource2()
					+ ins.get(i).getPercentResource3();

			if (ins.get(i).getUnitsPurchased() > 0 && ins.get(i).getPricePerUnit() < 0) {
				ins.get(i).setStatus(Asset.Status.Invalid);
				inpIsInvalid = true;
				break;
			} else if (ins.get(i).getResourceSubType() == null) {
				ins.get(i).setStatus(Asset.Status.Invalid);
				inpIsInvalid = true;
				break;
			} else if (!(ins.get(i).getPercentResource1() >= 0) || !(ins.get(i).getPercentResource2() >= 0)
					|| !(ins.get(i).getPercentResource2() >= 0)) {
				ins.get(i).setStatus(Asset.Status.Invalid);
				inpIsInvalid = true;
				break;
			} else if (Double.compare(inpSum, 0.0) != 0 && Double.compare(inpSum, 100.0) != 0) {
				inpIsInvalid = true;
				ins.get(i).setStatus(Asset.Status.Invalid);
				break;
			}

			else if (!ins.get(i).getStatus().toString().equals("Valid")) {
				inpIsInvalid = true;
				break;
			}

		}

		for (Iterator<HouseholdMember> iterator = householdMembers.iterator(); iterator.hasNext();) {
			HouseholdMember householdMember = iterator.next();

			if (householdMember.getMonthsAway() > 12) {
				addMessage("Household Member Months Away is greater than 12 - Valid values are between 0 and 12 ");
				isInvalid = true;
				break;
			}
		}

		if (landIsInvalid)
			addMessage("Land Assets still Invalid or Unchecked");
		if (livestockIsInvalid)
			addMessage("Livestock Assets still Invalid or Unchecked");
		if (tradeIsInvalid)
			addMessage("Tradeable Assets still Invalid or Unchecked");
		if (fsIsInvalid)
			addMessage("Foodstock Assets still Invalid or Unchecked");
		if (treeIsInvalid)
			addMessage("Tree Assets still Invalid or Unchecked");
		if (cashIsInvalid)
			addMessage("Cash Assets still Invalid or Unchecked");

		if (cropIsInvalid)
			addMessage("Crop Assets still Invalid or Unchecked");
		if (lssIsInvalid)
			addMessage("Livestock Sales still Invalid or Unchecked");
		if (lspIsInvalid)
			addMessage("Livestock Products still Invalid or Unchecked");
		if (empIsInvalid)
			addMessage("Employments still Invalid or Unchecked");
		if (transIsInvalid)
			addMessage("Transferable Assets still Invalid or Unchecked");
		if (wfIsInvalid)
			addMessage("Wildfood Assets still Invalid or Unchecked");
		if (inpIsInvalid)
			addMessage("Input Assets still Invalid or Unchecked");

		if (isInvalid || landIsInvalid || livestockIsInvalid || tradeIsInvalid || fsIsInvalid || treeIsInvalid
				|| cashIsInvalid || cropIsInvalid || lssIsInvalid || lspIsInvalid || empIsInvalid || transIsInvalid
				|| wfIsInvalid || inpIsInvalid)

		{
			System.out.println("NOT VALID");
			hhi.setStatus(WealthGroupInterview.Status.FullyParsed);
			getView().refresh();
		}

		else {
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
