/* Check that all WGI resource types are set to Valid */

package efd.actions;

import org.openxava.jpa.*;
import org.openxava.model.meta.*;
import org.openxava.view.*;
import org.openxava.view.View;

import java.math.*;
import java.util.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;

import efd.model.*;
import efd.model.Asset.*;
import efd.model.Transfer.*;

/*Validate loaded spreadsheet data for HH and WGI */

public class ValidateAssets extends ViewBaseAction implements IForwardAction {

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

		boolean isHH = false;
		boolean isWGI = false;

		final double THRESHOLD = 0.1;

		Household hhi = null;
		WealthGroupInterview wgi = null;

		List<AssetLand> al = null;
		List<AssetLiveStock> als = null;
		List<AssetTradeable> at = null;
		List<AssetFoodStock> afs = null;
		List<AssetTree> atree = null;
		List<AssetCash> ac = null;
		List<Crop> crop = null;
		List<LivestockSales> lss = null;
		List<LivestockProducts> lsp = null;
		List<Employment> emp = null;
		List<Transfer> tran = null;
		List<WildFood> wf = null;
		List<FoodPurchase> fp = null;
		List<NonFoodPurchase> nfp = null;
		List<Inputs> ins = null;

		Collection<HouseholdMember> householdMembers = null;

		if (getView().getBaseModelName().equalsIgnoreCase("WealthGroupInterview")) {
			isWGI = true;

			String wgiid = getView().getValueString("wgiid");
			wgi = XPersistence.getManager().find(WealthGroupInterview.class, wgiid);
			if (wgi.getStatus().equals(WealthGroupInterview.Status.Validated)) {
				addError("No Validation Required - Already Validated");
				return;
			}

			al = (List<AssetLand>) wgi.getAssetLand();
			als = (List<AssetLiveStock>) wgi.getAssetLiveStock();
			at = (List<AssetTradeable>) wgi.getAssetTradeable();
			afs = (List<AssetFoodStock>) wgi.getAssetFoodStock();
			atree = (List<AssetTree>) wgi.getAssetTree();
			ac = (List<AssetCash>) wgi.getAssetCash();
			crop = (List<Crop>) wgi.getCrop();
			lss = (List<LivestockSales>) wgi.getLivestockSales();
			lsp = (List<LivestockProducts>) wgi.getLivestockProducts();
			emp = (List<Employment>) wgi.getEmployment();
			tran = (List<Transfer>) wgi.getTransfer();
			wf = (List<WildFood>) wgi.getWildFood();
			fp = (List<FoodPurchase>) wgi.getFoodPurchase();
			nfp = (List<NonFoodPurchase>) wgi.getNonFoodPurchase();

			if (al.size() == 0 && als.size() == 0 && at.size() == 0 && afs.size() == 0 && atree.size() == 0
					&& ac.size() == 0 && crop.size() == 0 && lss.size() == 0 && lsp.size() == 0 && emp.size() == 0
					&& tran.size() == 0 && wf.size() == 0 && fp.size() == 0 && nfp.size() == 0) {
				addError("Cannot Validate Wealthgroup, No Assets to Validate");
				return;
			}

		} else if (getView().getBaseModelName().equalsIgnoreCase("Household")) {
			isHH = true;
			String hhid = getView().getValueString("id");

			hhi = XPersistence.getManager().find(Household.class, hhid);
			// if (hhi.getSpreadsheet() == null) {
			// addError("Upload completed Interview Spreadsheet before validating");
			// return;
			// } else
			// if (hhi.getStatus().equals(WealthGroupInterview.Status.Generated)) {
			// addError("Cannot Validate Interview Spreadsheet - Upload and Parse
			// Spreadsheet first ");
			// return;
			// } else if (hhi.getStatus().equals(WealthGroupInterview.Status.Uploaded)) {
			// addError("Cannot Validate Interview Spreadsheet - Parse Spreadsheet first");
			// return;
			if (hhi.getStatus().equals(WealthGroupInterview.Status.Validated)) {
				addError("No Validation Required - Already Validated");
				return;
			}
			al = (List<AssetLand>) hhi.getAssetLand();
			als = (List<AssetLiveStock>) hhi.getAssetLiveStock();
			at = (List<AssetTradeable>) hhi.getAssetTradeable();
			afs = (List<AssetFoodStock>) hhi.getAssetFoodStock();
			atree = (List<AssetTree>) hhi.getAssetTree();
			ac = (List<AssetCash>) hhi.getAssetCash();
			crop = (List<Crop>) hhi.getCrop();
			lss = (List<LivestockSales>) hhi.getLivestockSales();
			lsp = (List<LivestockProducts>) hhi.getLivestockProducts();
			emp = (List<Employment>) hhi.getEmployment();
			tran = (List<Transfer>) hhi.getTransfer();
			wf = (List<WildFood>) hhi.getWildFood();
			ins = (List<Inputs>) hhi.getInputs();

			/* Before validate check that at least 1 asset has some data */
			if (al.size() == 0 && als.size() == 0 && at.size() == 0 && afs.size() == 0 && atree.size() == 0
					&& ac.size() == 0 && crop.size() == 0 && lss.size() == 0 && lsp.size() == 0 && emp.size() == 0
					&& tran.size() == 0 && wf.size() == 0 && ins.size() == 0) {
				addError("Cannot Validate Household, No Assets to Validate");
				return;
			}

			householdMembers = hhi.getHouseholdMember();

		}
		System.out.println("isHH = " + isHH);
		System.out.println("isWGI = " + isWGI);

		/*************************************************************************************************/
		/*
		 * VALIDATE EACH Resource Asset type
		 */

		for (i = 0; i < al.size(); i++) {

			/* Land Validations */

			if (!(al.get(i).getNumberOfUnits() >= 0)) {
				al.get(i).setStatus(Asset.Status.Invalid);
				landIsInvalid = true;

			} else if (al.get(i).getResourceSubType() == null) {
				landIsInvalid = true;
				al.get(i).setStatus(Asset.Status.Invalid);

			} else {
				al.get(i).setStatus(Asset.Status.Valid);

			}

		}

		/* Livestock Validation */
		for (i = 0; i < als.size(); i++) {

			if (!(als.get(i).getNumberOwnedAtStart() >= 0)) {
				als.get(i).setStatus(Asset.Status.Invalid);
				livestockIsInvalid = true;

			} else if (als.get(i).getResourceSubType() == null) {
				livestockIsInvalid = true;
				als.get(i).setStatus(Asset.Status.Invalid);

			} else if (als.get(i).getNumberOwnedAtStart() >= 0 && als.get(i).getPricePerUnit() < 0) {
				als.get(i).setStatus(Asset.Status.Invalid);
				livestockIsInvalid = true;

			} else {

				als.get(i).setStatus(Asset.Status.Valid);
			}

		}

		/* Tradeable Validation */
		for (i = 0; i < at.size(); i++) {

			if (at.get(i).getNumberOwned() < 0) {
				at.get(i).setStatus(Asset.Status.Invalid);
				tradeIsInvalid = true;

			} else if (at.get(i).getResourceSubType() == null) {
				tradeIsInvalid = true;
				at.get(i).setStatus(Asset.Status.Invalid);

			} else if (at.get(i).getNumberOwned() >= 0 && at.get(i).getPricePerUnit() < 0) {
				at.get(i).setStatus(Asset.Status.Invalid);
				tradeIsInvalid = true;

			}

			else {
				at.get(i).setStatus(Asset.Status.Valid);

			}

		}

		/* Food Stocks */
		for (i = 0; i < afs.size(); i++) {
			if (!(afs.get(i).getQuantity() >= 0)) {
				afs.get(i).setStatus(Asset.Status.Invalid);
				fsIsInvalid = true;

			} else if (afs.get(i).getResourceSubType() == null) {
				fsIsInvalid = true;
				afs.get(i).setStatus(Asset.Status.Invalid);

			} else {
				afs.get(i).setStatus(Asset.Status.Valid);
			}

		}
		/* Trees */
		for (i = 0; i < atree.size(); i++) {

			if (!(atree.get(i).getNumberOwned() >= 0)) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;

			} else if (atree.get(i).getResourceSubType() == null) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;

			} else if (atree.get(i).getNumberOwned() >= 0 && atree.get(i).getPricePerUnit() < 0) {
				atree.get(i).setStatus(Asset.Status.Invalid);
				treeIsInvalid = true;

			}

			else {

				atree.get(i).setStatus(Asset.Status.Valid);
			}

		}

		/* Cash Validation */;
		for (i = 0; i < ac.size(); i++) {

			if (ac.get(i).getAmount().intValue() > 0 && ac.get(i).getExchangeRate().intValue() > 0) {
				ac.get(i).setStatus(Asset.Status.Valid);

			} else if (ac.get(i).getResourceSubType() == null) {
				ac.get(i).setStatus(Asset.Status.Invalid);
				cashIsInvalid = true;

			}

			else {
				ac.get(i).setStatus(Asset.Status.Valid);
			}
		}

		/* Crop Validation */

		for (i = 0; i < crop.size(); i++) {

			cropSum = crop.get(i).getPercentTradeMarket1() + crop.get(i).getPercentTradeMarket2()
					+ crop.get(i).getPercentTradeMarket3();

			if (crop.get(i).getUnitsSold() > 0 && crop.get(i).getPricePerUnit() <= 0) {

				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;
				addWarning(crop.get(i).getResourceSubType().getResourcetypename() + " crop sold but no price per unit");

			} else if ((crop.get(i).getUnitsProduced() <= 0) || (crop.get(i).getUnitsSold() < 0)
					|| (crop.get(i).getUnitsOtherUse() < 0) || (crop.get(i).getUnitsConsumed() < 0)
					|| (crop.get(i).getPercentTradeMarket1() < 0) || (crop.get(i).getPercentTradeMarket2() < 0)
					|| (crop.get(i).getPercentTradeMarket3() < 0)) {

				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;

			} else if (Double.compare(cropSum, 0.0) != 0 && Double.compare(cropSum, 100.0) != 0) {
				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;

			}

			// else if (!crop.get(i).getStatus().toString().equals("Valid")) {

			// cropIsInvalid = true;

			// }
			else if (crop.get(i).getResourceSubType() == null) {
				crop.get(i).setStatus(Asset.Status.Invalid);
				cropIsInvalid = true;

			} else {
				crop.get(i).setStatus(Asset.Status.Valid);

			}
		}

		/* Livestock Sales Validation */

		for (i = 0; i < lss.size(); i++) {

			lssSum = lss.get(i).getPercentTradeMarket1() + lss.get(i).getPercentTradeMarket2()
					+ lss.get(i).getPercentTradeMarket3();

			if(lss.get(i).getUnitsSold() == null) {
				lss.get(i).setUnitsSold(0.0);
			}
			if(lss.get(i).getPricePerUnit() == null) {
				lss.get(i).setPricePerUnit(0.0);
			}
			
			if (lss.get(i).getUnitsSold() > 0 && lss.get(i).getPricePerUnit() <= 0) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				addWarning(lss.get(i).getResourceSubType().getResourcetypename()
						+ " livestock sold but no price per unit");
				lssIsInvalid = true;

			} else if (lss.get(i).getResourceSubType() == null) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;

			} else if (!(lss.get(i).getUnitsSold() >= 0) || !(lss.get(i).getPercentTradeMarket1() >= 0)
					|| !(lss.get(i).getPercentTradeMarket2() >= 0) || !(lss.get(i).getPercentTradeMarket3() >= 0)) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;

			} else if (Double.compare(lssSum, 0.0) != 0 && Double.compare(lssSum, 100.0) != 0) {
				lss.get(i).setStatus(Asset.Status.Invalid);
				lssIsInvalid = true;

			}

			else {
				lss.get(i).setStatus(Asset.Status.Valid);

			}

		}

		/* Livestock Products Validation */

		for (i = 0; i < lsp.size(); i++) {

			lspSum = lsp.get(i).getPercentTradeMarket1() + lsp.get(i).getPercentTradeMarket2()
					+ lsp.get(i).getPercentTradeMarket3();

			if (lsp.get(i).getUnitsSold() > 0 && lsp.get(i).getPricePerUnit() <= 0) {

				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = true;
				addWarning(lsp.get(i).getResourceSubType().getResourcetypename()
						+ " livestock product sold but no price per unit");

			} else if (lsp.get(i).getResourceSubType() == null) {

				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = true;

			} else if (!(lsp.get(i).getUnitsProduced() >= 0) || !(lsp.get(i).getUnitsSold() >= 0)
					|| !(lsp.get(i).getUnitsOtherUse() >= 0) || !(lsp.get(i).getUnitsConsumed() >= 0)
					|| !(lsp.get(i).getPercentTradeMarket1() >= 0) || !(lsp.get(i).getPercentTradeMarket2() >= 0)
					|| !(lsp.get(i).getPercentTradeMarket3() >= 0)) {

				lspIsInvalid = true;
				lsp.get(i).setStatus(Asset.Status.Invalid);

			} else if (Double.compare(lspSum, 0.0) != 0 && Double.compare(lspSum, 100.0) != 0) {

				lsp.get(i).setStatus(Asset.Status.Invalid);
				lspIsInvalid = false;

			}

			else {

				lsp.get(i).setStatus(Asset.Status.Valid);

			}

		}

		/* Employment Validation */

		// TODO Why is Food Payment Units Paid Work a string not a Double ??
		// FIXED Oct 2019

		for (i = 0; i < emp.size(); i++) {

			// legacy null check
			if (emp.get(i).getFoodPaymentUnitsPaidWork() == null) {
				emp.get(i).setFoodPaymentUnitsPaidWork(0.00);
			}

			if(emp.get(i).getCashPaymentAmount() == null) {
				emp.get(i).setCashPaymentAmount(0.0);
			}
			if(emp.get(i).getFoodPaymentUnitsPaidWork() == null) {
				emp.get(i).setFoodPaymentUnitsPaidWork(0.0);
			}
			
			if ((emp.get(i).getPeopleCount() < 0) || (emp.get(i).getUnitsWorked() < 0)
					|| (emp.get(i).getCashPaymentAmount() < 0) || (emp.get(i).getFoodPaymentUnitsPaidWork() < 0)) {
				empIsInvalid = true;
				emp.get(i).setStatus(Asset.Status.Invalid);

			} else if (emp.get(i).getResourceSubType() == null) {
				emp.get(i).setStatus(Asset.Status.Invalid);
				empIsInvalid = true;

			} else {
				emp.get(i).setStatus(Asset.Status.Valid);
			}

		}

		/* Transfers Validation */

		for (i = 0; i < tran.size(); i++) {
			aa: transSum = tran.get(i).getPercentTradeMarket1() + tran.get(i).getPercentTradeMarket2()
					+ tran.get(i).getPercentTradeMarket3();

			if (tran.get(i).getUnitsSold() > 0 && tran.get(i).getPricePerUnit() <= 0) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;
				addWarning(tran.get(i).getFoodResourceSubType().getResourcetypename()
						+ " food transfer sold but no price per unit");

			} else if (tran.get(i).getTransferType() != TransferType.Food && 
					tran.get(i).getResourceSubType() == null ) {

				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;
				addWarning(tran.get(i).getFoodResourceSubType().getResourcetypename()
						+ " Food Transfer requires a Food Type ");


			} else if (tran.get(i).getTransferType() == null) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;

			} else if (!(tran.get(i).getPeopleReceiving() >= 0) || !(tran.get(i).getTimesReceived() >= 0)) {
				tran.get(i).setStatus(Asset.Status.Invalid);
				transIsInvalid = true;

			} else if (tran.get(i).getTransferType() == TransferType.Cash
					&& tran.get(i).getFoodResourceSubType() != null) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				addWarning(" Cash Transfer does not need a Food Resource Type");


			} else if (tran.get(i).getTransferType() == TransferType.Cash
					&& tran.get(i).getCashTransferAmount() == null) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);
				addWarning(" Cash Transfer requires a cash transfer Amount");

			} else if (tran.get(i).getTransferType() != TransferType.Cash
					&& tran.get(i).getFoodResourceSubType() == null) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);

			} else if (!(tran.get(i).getUnitsSold() >= 0) || !(tran.get(i).getOtherUse() >= 0)
					|| !(tran.get(i).getUnitsConsumed() >= 0) || !(tran.get(i).getPercentTradeMarket1() >= 0)
					|| !(tran.get(i).getPercentTradeMarket2() >= 0) || !(tran.get(i).getPercentTradeMarket3() >= 0)) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);

			} else if (Double.compare(transSum, 0.0) != 0 && Double.compare(transSum, 100.0) != 0) {
				transIsInvalid = true;
				tran.get(i).setStatus(Asset.Status.Invalid);

			}

			else {
				tran.get(i).setStatus(Asset.Status.Valid);
			}

		}
		System.out.println("done transfers validation");
		/* Wildfood Validation */
		for (i = 0; i < wf.size(); i++) {
			wfSum = wf.get(i).getPercentTradeMarket1() + wf.get(i).getPercentTradeMarket2()
					+ wf.get(i).getPercentTradeMarket3();

			if (wf.get(i).getUnitsSold() > 0 && wf.get(i).getPricePerUnit() <= 0) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
				addWarning(
						wf.get(i).getResourceSubType().getResourcetypename() + " wildfood sold but no price per unit");
			} else if (!(wf.get(i).getUnitsProduced() >= 0) || !(wf.get(i).getUnitsSold() >= 0)
					|| !(wf.get(i).getOtherUse() >= 0) || !(wf.get(i).getUnitsConsumed() >= 0)
					|| !(wf.get(i).getPercentTradeMarket1() >= 0) || !(wf.get(i).getPercentTradeMarket2() >= 0)
					|| !(wf.get(i).getPercentTradeMarket3() >= 0)) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;

			} else if (wf.get(i).getResourceSubType() == null) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;
			} else if (Double.compare(wfSum, 0.0) != 0 && Double.compare(wfSum, 100.0) != 0) {
				wf.get(i).setStatus(Asset.Status.Invalid);
				wfIsInvalid = true;

			}

			else {
				wf.get(i).setStatus(Asset.Status.Valid);
			}

		}

		/* Inputs Validation */
		if (isHH) {
			for (i = 0; i < ins.size(); i++) {
				inpSum = ins.get(i).getPercentResource1() + ins.get(i).getPercentResource2()
						+ ins.get(i).getPercentResource3();

				if (ins.get(i).getUnitsPurchased() > 0 && ins.get(i).getPricePerUnit() <= 0) {
					ins.get(i).setStatus(Asset.Status.Invalid);
					inpIsInvalid = true;

				} else if (ins.get(i).getResourceSubType() == null) {
					ins.get(i).setStatus(Asset.Status.Invalid);
					inpIsInvalid = true;

				} else if (!(ins.get(i).getPercentResource1() >= 0) || !(ins.get(i).getPercentResource2() >= 0)
						|| !(ins.get(i).getPercentResource2() >= 0)) {
					ins.get(i).setStatus(Asset.Status.Invalid);
					inpIsInvalid = true;

				} else if (Double.compare(inpSum, 0.0) != 0 && Double.compare(inpSum, 100.0) != 0) {
					inpIsInvalid = true;
					ins.get(i).setStatus(Asset.Status.Invalid);

				}

				else {
					ins.get(i).setStatus(Asset.Status.Valid);

				}

			}
			for (Iterator<HouseholdMember> iterator = householdMembers.iterator(); iterator.hasNext();) {
				HouseholdMember householdMember = iterator.next();

				if (householdMember.getMonthsAway() > 12) {
					addMessage("Household Member Months Away is greater than 12 - Valid values are between 0 and 12 ");
					isInvalid = true;

				}
			}
		}

		if (landIsInvalid)
			addWarning("Land Assets still Invalid or Unchecked");
		if (livestockIsInvalid)
			addWarning("Livestock Assets still Invalid or Unchecked");
		if (tradeIsInvalid)
			addWarning("Tradeable Assets still Invalid or Unchecked");
		if (fsIsInvalid)
			addWarning("Foodstock Assets still Invalid or Unchecked");
		if (treeIsInvalid)
			addWarning("Tree Assets still Invalid or Unchecked");
		if (cashIsInvalid)
			addWarning("Cash Assets still Invalid or Unchecked");

		if (cropIsInvalid)
			addWarning("Crop Assets still Invalid or Unchecked");
		if (lssIsInvalid)
			addWarning("Livestock Sales still Invalid or Unchecked");
		if (lspIsInvalid)
			addWarning("Livestock Products still Invalid or Unchecked");
		if (empIsInvalid)
			addWarning("Employments still Invalid or Unchecked");
		if (transIsInvalid)
			addWarning("Transferable Assets still Invalid or Unchecked");
		if (wfIsInvalid)
			addWarning("Wildfood Assets still Invalid or Unchecked");
		if (inpIsInvalid)
			addWarning("Input Assets still Invalid or Unchecked");

		if (isInvalid || landIsInvalid || livestockIsInvalid || tradeIsInvalid || fsIsInvalid || treeIsInvalid
				|| cashIsInvalid || cropIsInvalid || lssIsInvalid || lspIsInvalid || empIsInvalid || transIsInvalid
				|| wfIsInvalid || inpIsInvalid)

		{
			System.out.println("NOT VALID");
			if (isHH) {
				hhi.setStatus(WealthGroupInterview.Status.FullyParsed);
			} else if (isWGI) {
				wgi.setStatus(WealthGroupInterview.Status.FullyParsed);
			}
			getView().refresh();
		}

		else {
			if (isHH) {
				hhi.setStatus(WealthGroupInterview.Status.Validated);
				getView().setEditable(false);
				addActions("SetEditable.SetEditableHH");
			} else if (isWGI) {
				wgi.setStatus(WealthGroupInterview.Status.Validated);
				getView().setEditable(false);
				addActions("SetEditable.SetEditable");
			}
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
