
/* Run OHEA Reports2 
* June 2020 Add ability to handle less than or more than 3 validated WGIs
* Still have WGOrder of 1 2 3 - where there are more than 1 in a group then should average out
 * 
 *
 *
 * Get all HH for this study into hh array
 * 
 * Filter hh on Custom Report filters 
 * 
 * Calculate DI etc.
 * 
 * Various duplication due to need to make reoprts individually runnable as part of a reportgroup 
 * 
 * 
 * */

package efd.actions;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openxava.actions.IForwardAction;
import org.openxava.jpa.XPersistence;
import org.openxava.tab.Tab;
import org.openxava.util.jxls.JxlsConstants;
import org.openxava.util.jxls.JxlsSheet;
import org.openxava.util.jxls.JxlsStyle;
import org.openxava.util.jxls.JxlsWorkbook;
import org.openxava.web.servlets.ReportXLSServlet;

import efd.model.AssetCash;
import efd.model.AssetFoodStock;
import efd.model.AssetLand;
import efd.model.AssetLiveStock;
import efd.model.AssetTradeable;
import efd.model.AssetTree;
import efd.model.Category;
import efd.model.Community;
import efd.model.Crop;
import efd.model.CustomReportSpecOHEA;
import efd.model.DefaultDietItem;
import efd.model.Employment;
import efd.model.Household;
import efd.model.HouseholdMember.Sex;
import efd.model.Inputs;
import efd.model.LivelihoodZone;
import efd.model.LivestockProducts;
import efd.model.LivestockSales;
import efd.model.MCCWFoodSource;
import efd.model.MicroNutrient;
import efd.model.MicroNutrientLevel;
import efd.model.NutrientCount;
import efd.model.Project;
import efd.model.Project.Area;
import efd.model.Quantile;
import efd.model.Report;
import efd.model.ResourceSubType;
import efd.model.ResourceType;
import efd.model.Site;
import efd.model.StdOfLivingElement;
import efd.model.StdOfLivingElement.StdLevel;
import efd.model.Transfer;
import efd.model.Transfer.TransferType;
import efd.model.WealthGroup;
import efd.model.WealthGroupInterview;
import efd.model.WealthGroupInterview.Status;
import efd.model.WildFood;
import efd.utils.Efdutils;
import efd.utils.WGI;

public class OHEAReports extends BaseReporting implements IForwardAction, JxlsConstants {

	static Double RC = 2100.0 * 365; // NOTE that this is from OHEA. In OIHM we can be more accurate as we know age
										// and sex of HH Members

	
	static final int NUMBEROFAVERAGES = 30;
	static final int MAXNUMBERVALIDATEDWGIS = 30;
	final int CROP = 0;
	final int EMP = 1;
	final int LS = 2;
	final int TR = 3;
	final int WF = 4;

	private LivelihoodZone livelihoodZone = null;
	private Project project = null;
	// private CustomReportSpec customReportSpec = null;

	private List<Report> reportList;

	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	List<WGI> uniqueWealthgroupInterview;
	List<WGI> uniqueCommunity;
	List<WGI> allCommunity;
	int[] countWGInEachWGOrder = { 0, 0, 0 };
	List<WGI> uniqueWG;

	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;

	ArrayList<WGI> wgi = new ArrayList<>();
	ArrayList<Site> sites = new ArrayList<>();
	ArrayList<WGI> wgiSelected = new ArrayList<>();
	ArrayList<WealthGroupInterview> wgiList = new ArrayList<>();
	ArrayList<QuantHousehold> quanthh = new ArrayList<>();
	ArrayList<WealthGroup> displayWealthgroup = new ArrayList<>(); // Note that assuming 1:1 WG:WGI for the

	List<WGI> orderedQuantSeq = null;
	List<WealthGroup> orderedWealthgroups;
	List<WealthGroup> allOrderedWealthgroups;
	ArrayList<WealthGroup> allwgs = new ArrayList<WealthGroup>();
	ArrayList<WealthGroupInterview> allwgis = new ArrayList<WealthGroupInterview>();
	ArrayList<NutrientCount> overallNutrientCount = new ArrayList<NutrientCount>();
	ArrayList<NutrientCount> totalWGNutrientCount = new ArrayList<NutrientCount>();
	List<MicroNutrient> nutrients = XPersistence.getManager().createQuery("from MicroNutrient").getResultList();

	OptionalDouble[] wghhSize = new OptionalDouble[3];
	Double[] allHHAverage = { 0.0, 0.0, 0.0 };

	Map<Integer, Double> quantAvg = null;

	private String forwardURI = null;

	int errno = 0;
	Boolean isQuantile = false;
	Boolean isDisplayWealthgroupDone = false;
	Boolean isSelectedSites = false;
	Boolean isCustomFilter = false;
	String currency2;
	int[] wgCount = { 0, 0, 0 };
	double[] hhSize = { 0.0, 0.0, 0.0 };

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	int numCommunities = 0;

	// ArrayList<ArrayList<Double>> averageTotal = new
	// ArrayList<>(NUMBEROFAVERAGES);

	Double[][] averageTotal = new Double[MAXNUMBERVALIDATEDWGIS][NUMBEROFAVERAGES];

	
	private WealthGroup wealthgroup;

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run OHEA Reports ");
		int countValidated = 0;
		String specID = getView().getValueString("customReportSpec.id");
		if (specID.equals("")) {
			addError("Choose a Report Spec");
			return;
		}
		CustomReportSpecOHEA customReportSpecOHEA = XPersistence.getManager().find(CustomReportSpecOHEA.class, specID);

		Map allValues = getPreviousView().getAllValues();

		String lzid = getView().getValueString("livelihoodZone.lzid");
		livelihoodZone = XPersistence.getManager().find(LivelihoodZone.class, lzid);

		String projectid = getPreviousView().getValueString("projectid");
		if (projectid.isEmpty()) {
			projectid = getPreviousView().getValueString("project.projectid");
		}

		project = XPersistence.getManager().find(Project.class, projectid);

		Object communityId = null; // getPreviousView().getValue("communityid");

		Tab targetTab = getView().getSubview("livelihoodZone.site").getCollectionTab();

		Map[] selectedOnes = targetTab.getSelectedKeys();

		Map[] allLZKeys = targetTab.getAllKeys();

		WealthGroupInterview wealthGroupInterview = null;
		Site site = null;

		/*
		 * #535 If custom report filters - category etc, then use all LZs - NO LONGER
		 * #535
		 */

		if (!customReportSpecOHEA.getCategory().isEmpty() || !customReportSpecOHEA.getResourceSubType().isEmpty()
				|| !customReportSpecOHEA.getResourceType().isEmpty()) {
			isCustomFilter = true;
		}

		if (selectedOnes.length == 0) {
			addError("Choose at least one Community / Site");
			return;
		} else if (selectedOnes.length != 0) {
			isSelectedSites = true; // One or more Site selected in dialog
			for (int i = 0; i < selectedOnes.length; i++) {
				Map<?, ?> key = selectedOnes[i];

				String subKey = key.toString().substring(12, 44);

				site = XPersistence.getManager().find(Site.class, subKey);

				// add to array for Sites/Communities
				Site s = new Site();
				s = site;
				sites.add(s);

				// From Site get Communities/Sites then get WG and WGI

				try {

					for (Community community2 : site.getCommunity()) {

						/*
						 * Is this for current Project?
						 */

						countValidated = 0;

						if (!community2.getProjectlz().equals(project)) {
							Efdutils.em("Wrong Project");
							break;
						}

						Iterator<WealthGroup> wgIterator = community2.getWealthgroup().iterator();
						while (wgIterator.hasNext()) {

							WealthGroup wgNext = wgIterator.next();

							Iterator<WealthGroupInterview> wgiIterator = wgNext.getWealthGroupInterview().iterator();
							while (wgiIterator.hasNext()) {
								WealthGroupInterview wgiNext = wgiIterator.next();

								if (wgiNext.getStatus() == Status.Validated) {
									WealthGroupInterview w = new WealthGroupInterview();
									w = wgiNext;

									wgiList.add(w);

									WGI e = new WGI();
									e.setWealthgroupInterview(wgiNext);
									e.setWealthgroup(wgNext);
									e.setSite(community2.getSite());
									e.setCommunity(community2);
									isSelectedSites = true;
									wgiSelected.add(e);
									countValidated++;

								}
							}

						}
						if (countValidated < 1) // Community needs at least 1 Validated WGs - NOLONGER- needs at least 1
						{
							// addError("Number of Validated Wealthgroup Interviews is less than 3");
							wgi.removeIf(p -> p.getCommunity() == community2);
							wgiList.removeIf(p -> p.getWealthgroup().getCommunity() == community2);

						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					break;
				}

			}

		}

		errno = 50;

		// Populate WGI array wgis - use dialog selected list if enter

		populateWGIArray(wgiList); // Populates wgi arraylist

		Map<String, List<WGI>> collect = wgi.stream()
				.collect(Collectors.groupingBy(c -> c.getWealthgroup().getWgnameeng()));

		uniqueCommunity = wgi.stream().filter(distinctByKey(WGI::getCommunity)).collect(Collectors.toList());
		uniqueWG = wgi.stream().filter(distinctByKey(WGI::getWealthgroup)).collect(Collectors.toList());

		countWGInEachWGOrder[0] = (int) wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 1).count();
		countWGInEachWGOrder[1] = (int) wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 2).count();
		countWGInEachWGOrder[2] = (int) wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 3).count();

		allHHAverage[0] = wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 1)
				.mapToDouble(p -> p.getWgAverageNumberInHH()).average().orElse(0.0);
		allHHAverage[1] = wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 2)
				.mapToDouble(p -> p.getWgAverageNumberInHH()).average().orElse(0.0);
		allHHAverage[2] = wgiList.stream().filter(p -> p.getWealthgroup().getWgorder() == 3)
				.mapToDouble(p -> p.getWgAverageNumberInHH()).average().orElse(0.0);

		for (int k = 0; k < uniqueWG.size(); k++) {
			WealthGroup wg = new WealthGroup();
			wg = uniqueWG.get(k).getWealthgroup();
			allwgs.add(wg);
			WealthGroupInterview wgil = new WealthGroupInterview();
			wgil = uniqueWG.get(k).getWealthgroupInterview();
			allwgis.add(wgil);
		}

		/* Handle report spec with Filters - Category, RT, RST */

		if (filterWGI(customReportSpecOHEA) == 0) {
			addError(
					"No Communities meet criteria of at least 1 Validated Wealthgroup Interviews, change Report Spec or Validate Wealthgroup Interviews.");
			closeDialog();
			return;
		}

		errno = 51;
		// Filter according to Catalog/RT/RST/HH

		// Any Communities with DDI not adding up to 100 or stil at 0?

		Iterator<WealthGroupInterview> wgiIter = wgiList.iterator();

		while (wgiIter.hasNext()) {

			WealthGroupInterview wgiNext = wgiIter.next();

			if (wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 100
					&& wgiNext.getWealthgroup().getCommunity().getDdipercenttotal() != 0) {
				addError("Default Diet Total Percentage for a chosen Community is not 100% or 0%");
				closeDialog();
				return;
			}

		}

		errno = 52;
		// Calculate DI

		calculateDI(); // uses wgi filtered array based on CRS definition
		System.out.println("done calc DI");
		// calculateAE(); // Calculate the Adult equivalent
		System.out.println("done calc AE");

		errno = 54;
		// Run reports
		try {

			JxlsWorkbook report = createReport(customReportSpecOHEA);
			errno = 55;
			getRequest().getSession().setAttribute(ReportXLSServlet.SESSION_XLS_REPORT, report);
			errno = 56;
			setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
			errno = 57;
		} catch (Exception e) {
			addError(e + " Errno = " + errno);
			closeDialog();
			return;
		}

		closeDialog();

		addMessage("Created OHEA Report for Community using Spec " + customReportSpecOHEA.getSpecName());

	}

	/******************************************************************************************************************************************/

	private void calculateAE() {

		// for (HH hh2 : hh) {

		for (WGI wg2 : uniqueWealthgroupInterview) {

			// wg2.wgiAE = wealthgroupInterviewAE(wg2.wealthgroupInterview);

		}

	}

	/******************************************************************************************************************************************/

	private void calculateDI() {

		// Need to calculate DI for each WG

		double wgiDI = 0.0;
		for (WealthGroupInterview wgi2 : wgiList) {

			wgiDI = ModellingReports.wealthgroupInterviewDI(wgi2, false);

			wgi2.getWealthgroup().setDefaultDI(wgiDI);

		}

	}

	/******************************************************************************************************************************************/
	private int filterWGI(CustomReportSpecOHEA customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from wg populate array
		// Filter out reportspecuse non included wg if exist and for same Community

		System.out.println("filterWGI");

		/* if custom filter then set everything to delete = true */
		/* if no custom filter set everything to delete = false */

		if (isCustomFilter) {
			wgi.stream().forEach(p -> {
				filterOutRST(p, true);
			});
		} else {
			wgi.stream().forEach(p -> {
				p.setDelete(false);
				filterOutRST(p, false);
			});
		}

		if (customReportSpec.getCategory().size() > 0) // Apply Category Filter
		{

			List<WGI> wgiCAT = wgi.stream().filter(p -> p.getCategory() != null).collect(Collectors.toList());

			for (Category category : customReportSpec.getCategory()) {
				for (WGI wgi2 : wgiCAT) {

					if (category.getResourceSubType().stream().anyMatch(p -> p == wgi2.getResourceSubType())) {

						wgi2.setDelete(false);
						setAssetFilteredOutFalse(wgi2);

					} else {

						filterOutRST(wgi2, true);
					}
				}

			}

		}

		if (customReportSpec.getResourceType().size() > 0) // Apply RST Filter

		{
			System.out.println("in RT filter");
			List<WGI> wgRT = wgi.stream().filter(p -> p.getResourceType() != null).collect(Collectors.toList());

			for (WGI wg2 : wgRT) {
				if (BooleanUtils.isFalse(wg2.getDelete())) {
					filterOutRST(wg2, false);
					/* keep as do not delete regardless as set above */
					continue;
				}
				for (ResourceType resourceType : customReportSpec.getResourceType()) {

					if (resourceType == wg2.getResourceType()) {

						setAssetFilteredOutFalse(wg2); // Used to filter in reports

						wg2.setDelete(false);
					}

					else {

						filterOutRST(wg2, true);
					}

				}
			}

		}

		if (!customReportSpec.getResourceSubType().isEmpty()) // Apply RST Filter
		{

			List<WGI> wgRST = wgi.stream().filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

			for (WGI wg2 : wgRST) {
				if (BooleanUtils.isFalse(wg2.getDelete())) {
					/* keep as do not delete regardless as set above */
					filterOutRST(wg2, false);
					continue;
				}

				for (ResourceSubType resourceSubType : customReportSpec.getResourceSubType()) {

					if (resourceSubType.getIdresourcesubtype() == wg2.getResourceSubType().getIdresourcesubtype()) {

						setAssetFilteredOutFalse(wg2);

						wg2.setDelete(false);
					}

					else {

						filterOutRST(wg2, true);
					}
				}

			}

		}

		System.out.println("Before wgi delete = " + wgi.size());
		try {
			wgi.removeIf(n -> n.getDelete() == true);
		} catch (Exception e) {
			System.out.println("nothing filtered to remove");

		}

		System.out.println("After wgi delete and return val is = " + wgi.size());
		return wgi.size();

	}

	/******************************************************************************************************************************************/
	/*
	 * Set isFilteredOut flag in Asset if not included - default is false in Assets
	 */

	private void setAssetFilteredOutFalse(WGI wgiFilter) {

		if (wgiFilter.getCrop() != null)
			wgiFilter.getCrop().setIsFilteredOut(false);

		if (wgiFilter.getEmployment() != null)
			wgiFilter.getEmployment().setIsFilteredOut(false);

		if (wgiFilter.getLivestockproducts() != null)
			wgiFilter.getLivestockproducts().setIsFilteredOut(false);

		if (wgiFilter.getLivestocksales() != null)
			wgiFilter.getLivestocksales().setIsFilteredOut(false);

		if (wgiFilter.getTransfer() != null)
			wgiFilter.getTransfer().setIsFilteredOut(false);

		if (wgiFilter.getWildfood() != null)
			wgiFilter.getWildfood().setIsFilteredOut(false);
	}

	/******************************************************************************************************************************************/
	/*
	 * Set isFilteredOut flag in Asset if not included - default is false in Assets
	 * - set to True here
	 */
	private void filterOutRST(WGI wgiFilter, boolean trueorfalse) {

		// wgiFilter.setDelete(trueorfalse);
		/* Need to set household assets to filtered out before crop,emp,lsp,lss,tr,wf */

		List<Crop> cropSetFilteredOut = wgiFilter.getWealthgroupInterview().getCrop().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Crop> listIteratorcropList = cropSetFilteredOut.listIterator();
		while (listIteratorcropList.hasNext()) {
			listIteratorcropList.next().setIsFilteredOut(trueorfalse);

		}

		List<WildFood> wfSetFilteredOut = wgiFilter.getWealthgroupInterview().getWildFood().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<WildFood> listIteratorwfList = wfSetFilteredOut.listIterator();
		while (listIteratorwfList.hasNext()) {
			listIteratorwfList.next().setIsFilteredOut(trueorfalse);

		}

		List<Employment> empSetFilteredOut = wgiFilter.getWealthgroupInterview().getEmployment().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Employment> listIteratorempList = empSetFilteredOut.listIterator();
		while (listIteratorempList.hasNext()) {
			listIteratorempList.next().setIsFilteredOut(trueorfalse);

		}

		// Food payment
		List<Employment> empFoodSetFilteredOut = wgiFilter.getWealthgroupInterview().getEmployment().stream()
				.filter(p -> p.getFoodResourceSubType() == wgiFilter.getResourceSubType())
				.filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

		ListIterator<Employment> listIteratorempFoodList = empFoodSetFilteredOut.listIterator();
		while (listIteratorempFoodList.hasNext()) {
			listIteratorempFoodList.next().setIsFilteredOut(trueorfalse);

		}

		List<LivestockProducts> lspSetFilteredOut = wgiFilter.getWealthgroupInterview().getLivestockProducts().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<LivestockProducts> listIteratorlspList = lspSetFilteredOut.listIterator();
		while (listIteratorlspList.hasNext()) {
			listIteratorlspList.next().setIsFilteredOut(trueorfalse);

		}
		List<LivestockSales> lssSetFilteredOut = wgiFilter.getWealthgroupInterview().getLivestockSales().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<LivestockSales> listIteratorlssList = lssSetFilteredOut.listIterator();
		while (listIteratorlssList.hasNext()) {
			listIteratorlssList.next().setIsFilteredOut(trueorfalse);

		}
		List<Transfer> trSetFilteredOut = wgiFilter.getWealthgroupInterview().getTransfer().stream()
				.filter(p -> p.getResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Transfer> listIteratortrList = trSetFilteredOut.listIterator();
		while (listIteratortrList.hasNext()) {
			listIteratortrList.next().setIsFilteredOut(trueorfalse);

		}
		// Food transfer
		List<Transfer> trFoodSetFilteredOut = wgiFilter.getWealthgroupInterview().getTransfer().stream()
				.filter(p -> p.getFoodResourceSubType() == wgiFilter.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Transfer> listIteratortrFoodList = trFoodSetFilteredOut.listIterator();
		while (listIteratortrFoodList.hasNext()) {
			listIteratortrFoodList.next().setIsFilteredOut(trueorfalse);

		}

	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/
	private void populateWGIArray(List<WealthGroupInterview> wealthgroupsInterviews) {

		for (WealthGroupInterview wealthgroupInterview : wealthgroupsInterviews) {

			populateWGIfromWealthgroupInterview(wealthgroupInterview);

		}

		Map<WealthGroup, List<WGI>> collectWGIonExit = wgi.stream()
				.collect(Collectors.groupingBy(c -> c.getWealthgroup()));

	}

	/******************************************************************************************************************************************/

	private WealthGroupInterview AverageWGI(List<WealthGroupInterview> wgiGroupForAveraging) {

		System.out.println("in averaging wgi func");

		WealthGroupInterview avgwgiReturned = new WealthGroupInterview();
		WealthGroupInterview avgwgi1 = new WealthGroupInterview();
		WealthGroupInterview avgwgi2 = new WealthGroupInterview();

		/*
		 * set most to same as first wgi then update average numbers
		 */
		avgwgiReturned = wgiGroupForAveraging.get(0);
		avgwgi1 = wgiGroupForAveraging.get(0);
		avgwgi2 = wgiGroupForAveraging.get(1);

		WealthGroup wealthgroup2 = avgwgi1.getWealthgroup();
		wealthgroup2.setWgnameeng(wealthgroup2.getWgnameeng() + " **");

		// Wealthgroup and WealthgroupInterview
		avgwgiReturned.setWgAverageNumberInHH(
				(int) wgiGroupForAveraging.stream().mapToInt(r -> r.getWgAverageNumberInHH()).average().getAsDouble());

		avgwgiReturned.setdI(wgiGroupForAveraging.stream().mapToDouble(r -> r.getdI()).average().getAsDouble());

		return avgwgiReturned;
	}

	/******************************************************************************************************************************************/

	private void populateWGIfromWealthgroupInterview(WealthGroupInterview wealthGroupInterview) {

		for (AssetLand asset : wealthGroupInterview.getAssetLand()) {

			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory(); // List of Categories that
																						// include this RST
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Land", asset, null, null,
					null, null, null, null, null, null, null, null, null, null);

		}
		for (AssetFoodStock asset : wealthGroupInterview.getAssetFoodStock()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Foodstock", null, asset, null,
					null, null, null, null, null, null, null, null, null, null);
		}
		for (AssetCash asset : wealthGroupInterview.getAssetCash()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = null;
			ResourceType resourceType = null;
			try {
				category = asset.getResourceSubType().getCategory();
				resourceType = asset.getResourceSubType().getResourcetype();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Cash", null, null, asset,
					null, null, null, null, null, null, null, null, null, null);

		}
		for (AssetLiveStock asset : wealthGroupInterview.getAssetLiveStock()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Livestock", null, null, null,
					asset, null, null, null, null, null, null, null, null, null);
		}
		for (AssetTradeable asset : wealthGroupInterview.getAssetTradeable()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Tradeable", null, null, null,
					null, asset, null, null, null, null, null, null, null, null);
		}
		for (AssetTree asset : wealthGroupInterview.getAssetTree()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Tree", null, null, null, null,
					null, asset, null, null, null, null, null, null, null);
		}
		for (Crop asset : wealthGroupInterview.getCrop()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Crop", null, null, null, null,
					null, null, asset, null, null, null, null, null, null);
		}
		for (Employment asset : wealthGroupInterview.getEmployment()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Employment", null, null, null,
					null, null, null, null, asset, null, null, null, null, null);
		}

		for (LivestockProducts asset : wealthGroupInterview.getLivestockProducts()) {
			ResourceSubType resourceSubType = null;
			Collection<Category> category = null;
			ResourceType resourceType = null;
			try {
				resourceSubType = asset.getResourceSubType();
				category = asset.getResourceSubType().getCategory();
				resourceType = asset.getResourceSubType().getResourcetype();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "LivestockProduct", null, null,
					null, null, null, null, null, null, null, asset, null, null, null);
		}
		for (LivestockSales asset : wealthGroupInterview.getLivestockSales()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "LivestockSale", null, null,
					null, null, null, null, null, null, null, null, asset, null, null);
		}
		for (Transfer asset : wealthGroupInterview.getTransfer()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Transfer", null, null, null,
					null, null, null, null, null, null, null, null, asset, null);
		}
		for (WildFood asset : wealthGroupInterview.getWildFood()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addToWGIArray(wealthGroupInterview, category, resourceType, resourceSubType, "Wildfood", null, null, null,
					null, null, null, null, null, null, null, null, null, asset);
		}

	}

	/******************************************************************************************************************************************/

	private void addToWGIArray(WealthGroupInterview wealthGroupInterview, Collection<Category> category,
			ResourceType resourceType, ResourceSubType resourceSubType, String type, AssetLand land,
			AssetFoodStock foodstock, AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable,
			AssetTree tree, Crop crop, Employment employment, Inputs inputs, LivestockProducts livestockproducts,
			LivestockSales livestocksales, Transfer transfer, WildFood wildfood) {

		WGI e = new WGI();

		e.setWealthgroupInterview(wealthGroupInterview);
		e.setSite(wealthGroupInterview.getWealthgroup().getCommunity().getSite());
		e.setCommunity(wealthGroupInterview.getWealthgroup().getCommunity());
		e.setWealthgroup(wealthGroupInterview.getWealthgroup());
		e.setProject(project);
		e.setLivelihoodZone(livelihoodZone);
		e.setWgiNumber(wealthGroupInterview.getWealthgroup().getWgorder());

		// e.wgiNumber = 0;
		e.setCategory(category);
		e.setResourceType(resourceType);
		e.setResourceSubType(resourceSubType);

		e.setType(type);
		e.setLand(land);
		e.setEmployment(employment);
		e.setFoodstock(foodstock);
		e.setInputs(inputs);
		e.setLivestock(livestock);
		e.setLivestockproducts(livestockproducts);
		e.setLivestocksales(livestocksales);
		e.setTradeable(tradeable);
		e.setTransfer(transfer);
		e.setTree(tree);
		e.setWildfood(wildfood);
		e.setCrop(crop);
		e.setCash(cash);

		wgi.add(e);

	}

	/******************************************************************************************************************************************/

	private JxlsWorkbook createReport(CustomReportSpecOHEA customReportSpec) throws Exception {
		System.out.println("In Run OHEA Reports create xls 000");

		String filename = customReportSpec.getSpecName() + Calendar.getInstance().getTime();

		reportWB = new JxlsWorkbook(filename);

		setStyles(reportWB);

		createHeaderPage(customReportSpec); // populates reportList

		int ireportNumber = 0; // should be equal to the sheet number in workbook

		/*
		 * HH are included if they are Validated
		 * 
		 * If Custom Report Spec (CRS) has Config Answers then only HH with answers to
		 * that question and answer are included
		 * 
		 * Order by HH DI size
		 * 
		 * Include all resources unless CRS has Category/RT or RST rows which then form
		 * the filter
		 * 
		 */

		for (Report report : reportList) {

			int reportCode = report.getCode();

			ireportNumber++; // keep track of number of reports = sheet on output spreadsheet

			switch (reportCode) {
			case 366:
				System.out.println("report 366");
				createDIreport(1, report);
				break;
			case 367:
				System.out.println("report 367");
				createDIAfterSOLreport(2, report);
				break;
			case 368:
				System.out.println("report 368");
				createIncomereport(3, report, "cash");
				break;
			case 369:
				System.out.println("report 369");
				createIncomereport(4, report, "food");
				break;

			case 370:
				System.out.println("report 370");
				createAssetreport(5, report, "land");
				break;

			case 371:
				System.out.println("report 371");
				createAssetreport(6, report, "livestock");
				break;
			case 373:
				System.out.println("report 373 Food Micronutrients");
				createFoodNutrientreport(7, report);
				break;
			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/
	private void createDIreport(int isheet, Report report) {

		int row = 7;
		int i = 0;
		int WGOrder = 0;
		int WGGroup = 0;

		int wgorder = 0;
		int numberOfCommunites = uniqueCommunity.size();

		System.out.println("in 366 report ");

		averageReset();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 15, 15, 20);

		populateFirstThreeColumns(isheet, 1);

		reportWB.getSheet(isheet).setValue(4, 1, "Family Group Size", boldTopStyle);

		reportWB.getSheet(isheet).setValue(5, 1, "Disposable Income", boldTopStyle);

		// NOTE OHEA ordered by wg Number order not DI

		for (WGI wgi2 : uniqueCommunity) {

			OptionalDouble[] wghhSize = new OptionalDouble[3];
			OptionalDouble[] wgDI = new OptionalDouble[3];

			ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

			// NEED TO FILTER OUT NON VALIDATED WGI
			// multiple predicate

			orderedWealthgroups = calcOrderedWealthGroup(wgi2); // sets

			for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
					.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
				listWealthgroup.add(wgi4.getWealthgroup());
			}

			Stream<WealthGroup> wgStream = listWealthgroup.stream();

			/* find any wgs and group by wgorder and get avg */

			if (wgStream.filter(p -> p.getWgorder() == 1).findAny().isPresent()) {

				WGOrder = 0;

				wghhSize[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
						.mapToDouble(p -> p.getDefaultDI()).average();

				reportWB.getSheet(isheet).setValue(4, row, wghhSize[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][0] = wghhSize[WGOrder].getAsDouble() + averageTotal[WGOrder][0];

				reportWB.getSheet(isheet).setValue(5, row, wgDI[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][1] = wgDI[WGOrder].getAsDouble() + averageTotal[WGOrder][1];

				wgCount[0]++;

			}
			row++;
			wgStream = listWealthgroup.stream();
			if (wgStream.filter(p -> p.getWgorder() == 2).findAny().isPresent()) {

				WGOrder = 1;

				wghhSize[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
						.mapToDouble(p -> p.getDefaultDI()).average();

				reportWB.getSheet(isheet).setValue(4, row, wghhSize[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][0] = wghhSize[WGOrder].getAsDouble() + averageTotal[WGOrder][0];

				reportWB.getSheet(isheet).setValue(5, row, wgDI[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][1] = wgDI[WGOrder].getAsDouble() + averageTotal[WGOrder][1];

				wgCount[1]++;
			}
			row++;
			wgStream = listWealthgroup.stream();
			if (wgStream.filter(p -> p.getWgorder() == 3).findAny().isPresent()) {

				WGOrder = 2;

				wghhSize[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[WGOrder] = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
						.mapToDouble(p -> p.getDefaultDI()).average();

				reportWB.getSheet(isheet).setValue(4, row, wghhSize[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][0] = wghhSize[WGOrder].getAsDouble() + averageTotal[WGOrder][0];

				reportWB.getSheet(isheet).setValue(5, row, wgDI[WGOrder].getAsDouble(), numberStyle);
				averageTotal[WGOrder][1] = wgDI[WGOrder].getAsDouble() + averageTotal[WGOrder][1];

				wgCount[2]++;
			}

			/* if not selected sites due to filter of Category etc need to calc averages */

			row++;
		}
		errno = 3666;
		row = 3;

		// Now need to handle less than 3 wealthgroups in some or all of the Communities
		// grouped by wgorder

		OptionalDouble[] averageHH = new OptionalDouble[3];
		OptionalDouble[] averageDI = new OptionalDouble[3];

		for (int j = 0; j < 3; j++) {

			if (isSelectedSites) {
				averageHH[j] = calcAvgWGHH(j + 1);
				averageDI[j] = calcAvgWGDI(j + 1);
			} else {

				if (wgCount[j] == 0) {
					wgCount[j] = 1;
				}
				averageHH[j] = OptionalDouble.of(averageTotal[j][0] / wgCount[j]);
				averageDI[j] = OptionalDouble.of(averageTotal[j][1] / wgCount[j]);

			}
		}

		for (i = 0; i < 3; i++) {

			reportWB.getSheet(isheet).setValue(4, row, averageHH[i].orElse(0.0), numberStyle);
			reportWB.getSheet(isheet).setValue(5, row, averageDI[i].orElse(0.0), numberStyle);

			row++;

		}

		System.out.println("done create di report");
		errno = 3667;
	}

	/******************************************************************************************************************************************/

	private List<WealthGroup> calcOrderedWealthGroup(WGI wgi2) {

		ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

		for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
				.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
			listWealthgroup.add(wgi4.getWealthgroup());

		}

		return (listWealthgroup.stream().sorted(Comparator.comparing(WealthGroup::getWgorder))
				.collect(Collectors.toList()));

		// Note now includes any averaged WGIs where WGOrder is the grouping value

	}

	/******************************************************************************************************************************************/
	private void averageReset() {

		int l = 0;
		for (int i = 0; i < 3; i++) {

			for (l = 0; l < NUMBEROFAVERAGES; l++) {
				averageTotal[i][l] = 0.0;
			}

		}
	}

	/******************************************************************************************************************************************/

	private Double fillDouble(Double val) {
		if (val == null || val.isNaN())
			val = 0.0;
		val = Double.parseDouble(df2.format(val));
		return val;
	}

	/******************************************************************************************************************************************/

	private void createDIAfterSOLreport(int isheet, Report report) {
		int row = 1;
		int i = 0;

		/*
		 * KCAl Req / STOL averages are always same for each WG as they belong to
		 * Community
		 */

		double hhSize = 0.0;
		int[] numberOfWGs = { 0, 0, 0 };
		Double[] totalKCAL = { 0.0, 0.0, 0.0 };
		Double[] totalSTOLI = { 0.0, 0.0, 0.0 };
		Double[] totalSTOLS = { 0.0, 0.0, 0.0 };
		int[] wgCounter = { 0, 0, 0 };

		boolean[] wgExist = new boolean[] { false, false, false };

		double[] wgSOL = new double[2];

		errno = 3668;

		OptionalDouble[] wghhSize = new OptionalDouble[3];
		OptionalDouble[] wgDI = new OptionalDouble[3];
		OptionalDouble[] wgSolSurv = new OptionalDouble[3];
		OptionalDouble[] wgSolIncl = new OptionalDouble[3];

		averageReset();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 15, 20, 20, 15, 22, 20);

		populateFirstThreeColumns(isheet, 1);

		reportWB.getSheet(isheet).setValue(4, row, "Disposable Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(5, row, "Family Group Size", boldTopStyle);
		reportWB.getSheet(isheet).setValue(6, row, "KCal Requirement", boldTopStyle);
		reportWB.getSheet(isheet).setValue(7, row, "Social Inclusion SToL", boldTopStyle);
		reportWB.getSheet(isheet).setValue(8, row, "Survival SToL", boldTopStyle);

		row = 7;
		for (WGI wgi2 : uniqueCommunity) {
			wgExist[0] = false;
			wgExist[1] = false;
			wgExist[2] = false;
			ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

			for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
					.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
				listWealthgroup.add(wgi4.getWealthgroup());
			}

			Stream<WealthGroup> wgStream = listWealthgroup.stream();

			/* find any wgs and group by wgorder and get avg */

			if (wgStream.filter(p -> p.getWgorder() == 1).findAny().isPresent()) {

				wgCounter[0] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 1).count();
				wgExist[0] = true;
				wghhSize[0] = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[0] = listWealthgroup.stream().filter(p -> p.getWgorder() == 1).mapToDouble(p -> p.getDefaultDI())
						.average();

				hhSize = wghhSize[0].getAsDouble();
				List<WealthGroup> thiswg = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
						.collect(Collectors.toList());

				wgSOL = calcSOLC(hhSize, thiswg);

				wgSolIncl[0] = OptionalDouble.of(wgSOL[0]);
				wgSolSurv[0] = OptionalDouble.of(wgSOL[1]);

			}
			wgStream = listWealthgroup.stream();
			if (wgStream.filter(p -> p.getWgorder() == 2).findAny().isPresent()) {
				wgCounter[1] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 2).count();
				wgExist[1] = true;
				wghhSize[1] = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[1] = listWealthgroup.stream().filter(p -> p.getWgorder() == 2).mapToDouble(p -> p.getDefaultDI())
						.average();
				hhSize = wghhSize[1].getAsDouble();
				List<WealthGroup> thiswg = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
						.collect(Collectors.toList());

				wgSOL = calcSOLC(hhSize, thiswg);
				wgSolIncl[1] = OptionalDouble.of(wgSOL[0]);
				wgSolSurv[1] = OptionalDouble.of(wgSOL[1]);

			}
			wgStream = listWealthgroup.stream();
			if (wgStream.filter(p -> p.getWgorder() == 3).findAny().isPresent()) {
				wgCounter[2] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 3).count();
				wgExist[2] = true;
				wghhSize[2] = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
						.mapToInt(p -> p.getWealthGroupInterview().get(0).getWgAverageNumberInHH()).average();

				wgDI[2] = listWealthgroup.stream().filter(p -> p.getWgorder() == 3).mapToDouble(p -> p.getDefaultDI())
						.average();
				hhSize = wghhSize[2].getAsDouble();
				List<WealthGroup> thiswg = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
						.collect(Collectors.toList());

				wgSOL = calcSOLC(hhSize, thiswg);
				wgSolIncl[2] = OptionalDouble.of(wgSOL[0]);
				wgSolSurv[2] = OptionalDouble.of(wgSOL[1]);

			}
			errno = 3669;
			/* need to handle more than 3 wgs being defined */
			/* remove any with Ordered = 0 */

			// Print results

			for (int k = 0; k < 3; k++) {
				if (!wgExist[k]) {
					row++;
					continue;
				}
				if (wgDI[k] != null) {
					reportWB.getSheet(isheet).setValue(4, row, wgDI[k].getAsDouble(), numberStyle);

					averageTotal[k][0] = wgDI[k].getAsDouble() + averageTotal[k][0];
				}

				if (wghhSize[k] != null) {
					reportWB.getSheet(isheet).setValue(5, row, wghhSize[k].getAsDouble(), numberStyle);
					averageTotal[k][1] = wghhSize[k].getAsDouble() + averageTotal[k][1];

					reportWB.getSheet(isheet).setValue(6, row, wghhSize[k].getAsDouble() * RC, numberStyle);
					averageTotal[k][2] = (wghhSize[k].getAsDouble() * RC) + averageTotal[k][2];

				}

				if (wgSolIncl[k] != null) {

					reportWB.getSheet(isheet).setValue(7, row, wgSolIncl[k].getAsDouble(), numberStyle);

					averageTotal[k][3] = wgSolIncl[k].getAsDouble() + averageTotal[k][3];
				}
				if (wgSolSurv[k] != null) {
					reportWB.getSheet(isheet).setValue(8, row, wgSolSurv[k].getAsDouble(), numberStyle);

					averageTotal[k][4] = wgSolSurv[k].getAsDouble() + averageTotal[k][4];
				}
				row++;

			}

		}
		errno = 3670;
		// Print Averages
		row = 3;

		for (int j = 0; j < 3; j++) {
			errno = 3670_1;
			double di;
			double hh;
			double stol;
			double sstol;

			/*
			 * Need to handle all sites that have filter member in operation - disregard
			 * isSelected
			 */

			if (wgCounter[j] == 0)
				wgCounter[j] = 1;

			if (isSelectedSites) {
				di = calcAvgWGDI(j + 1).orElse(0.0);
				hh = calcAvgWGHH(j + 1).orElse(0.0);
				stol = averageTotal[j][3] / wgCounter[j];
				sstol = averageTotal[j][4] / wgCounter[j];
			} else {
				if (wgCounter[j] != 0) {
					di = averageTotal[j][0] / wgCounter[j];
					hh = averageTotal[j][1] / wgCounter[j];
					stol = averageTotal[j][3] / wgCounter[j];
					sstol = averageTotal[j][4] / wgCounter[j];
				} else {
					di = 0.0;
					hh = 0.0;
					stol = 0.0;
					sstol = 0.0;
				}
			}

			errno = 3670_2;
			double kcal = RC * hh;

			List<WealthGroup> wglist = calcWGList(j + 1);
			errno = 3670_3;
			double[] solc = calcSOLC(hh, wglist);
			errno = 3670_4;

			reportWB.getSheet(isheet).setValue(4, row, di, numberStyle);
			reportWB.getSheet(isheet).setValue(5, row, hh, numberStyle);
			reportWB.getSheet(isheet).setValue(6, row, kcal, numberStyle);
			// if (wgCounter[j] > 0) {
			// reportWB.getSheet(isheet).setValue(7, row, solc[0] / wgCounter[j],
			// numberStyle);
			// reportWB.getSheet(isheet).setValue(8, row, solc[1] / wgCounter[j],
			// numberStyle);
			// } else {
			reportWB.getSheet(isheet).setValue(7, row, stol, numberStyle);
			reportWB.getSheet(isheet).setValue(8, row, sstol, numberStyle);
			// }
			row++;
		}

		for (int col = 3; col < 5; col++) {
			row = 3;
			for (i = 0; i < 3; i++) {
				if (averageTotal[i] == null) {
					continue;
				}
				// reportWB.getSheet(isheet).setValue(4 + col, row, averageTotal[i][col] /
				// wgCount[i], numberStyle);
				row++;
			}
		}
		errno = 3671;
	}

	private List<WealthGroup> calcWGList(int wgOrder) {
		List<WealthGroup> wglist = allwgs.stream().filter(p -> p.getWgorder() == wgOrder).collect(Collectors.toList());
		return wglist;
	}

	private List<WealthGroupInterview> calcWGIList(int wgOrder) {
		List<WealthGroupInterview> wgilist = allwgis.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
				.collect(Collectors.toList());
		return wgilist;
	}

	private OptionalDouble calcAvgWGHH(int wgOrder) {

		long count = uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
				.mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).count();

		double sum = uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
				.mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).sum();

		return uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
				.mapToDouble(p -> p.getWealthgroupInterview().getWgAverageNumberInHH()).average();

	}

	private OptionalDouble calcAvgWGDI(int wgOrder) {
		return uniqueWG.stream().filter(p -> p.getWealthgroup().getWgorder() == wgOrder)
				.mapToDouble(p -> p.getWealthgroup().getDefaultDI()).average();
	}

	/******************************************************************************************************************************************/

	private double[] calcSOLC(double hhSize, List<WealthGroup> thiswg) {

		double[] wgSOL = new double[2];

		if (thiswg.size() == 0) {
			wgSOL[0] = 0.0;
			wgSOL[1] = 0.0;
			return wgSOL;
		}

		if (thiswg.get(0).getCommunity().getStdOfLivingElement().isEmpty()) {
			wgSOL[0] = 0.0;
			wgSOL[1] = 0.0;
		} else {
			for (StdOfLivingElement stdOfLivingElement : thiswg.get(0).getCommunity().getStdOfLivingElement()) {
				errno = 44371;

				errno = 44372;
				if (stdOfLivingElement.getLevel() == StdLevel.Household) {
					wgSOL[0] += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());
					wgSOL[1] += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
							* stdOfLivingElement.getSurvival() / 100;
					errno = 44373;

					errno = 44374;
				} else if (stdOfLivingElement.getLevel() == StdLevel.HouseholdMember) {
					errno = 44375;
					wgSOL[0] += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount() * hhSize;
					wgSOL[1] += stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()
							* (stdOfLivingElement.getSurvival() / 100) * hhSize;
					errno = 44376;
				}

			}
		}
		errno = 44377;
		return wgSOL;
	}

	/******************************************************************************************************************************************/

	private void createIncomereport(int isheet, Report report, String type) {

		int col = 4;

		int startRow = 1;
		int[] wgCounter = { 0, 0, 0 };

		Double[] income = new Double[5];
		Double[] income1 = new Double[5];

		// boolean[] wgExist = new boolean[] { false, false, false };
		int row = 7;

		// String type = "food" or "cash"
		System.out.println("in income report 368/369");

		averageReset();
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 30, 30, 30, 30, 30);
		populateFirstThreeColumns(isheet, startRow);

		String initType = StringUtils.capitalize(type);

		reportWB.getSheet(isheet).setValue(col++, startRow, "Crop " + initType + " Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(col++, startRow, "Employment " + initType + " Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(col++, startRow, "Livestock " + initType + " Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(col++, startRow, "Transfer " + initType + " Income", boldTopStyle);
		reportWB.getSheet(isheet).setValue(col++, startRow, "Wildfood " + initType + " Income", boldTopStyle);

		row = 7;

		errno = 2265;

		for (WGI wgi2 : uniqueCommunity) {

			OptionalDouble[] wghhSize = new OptionalDouble[3];
			OptionalDouble[] wgDI = new OptionalDouble[3];

			for (int k = 0; k < 5; k++) {
				income[k] = 0.0;
			}

			ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

			// NEED TO FILTER OUT NON VALIDATED WGI
			// multiple predicate

			orderedWealthgroups = calcOrderedWealthGroup(wgi2); // sets

			/* Handle filtered WGs */

			if (orderedWealthgroups.size() == 0) {
				row = row + 3;
				continue;

			}

			for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
					.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
				listWealthgroup.add(wgi4.getWealthgroup());
			}

			/* find any wgs and group by wgorder and get avg */
			errno = 2266;
			if (listWealthgroup.stream().filter(p -> p.getWgorder() == 1).findAny().isPresent()) {
				for (int k = 0; k < 5; k++) {
					income1[k] = 0.0;
				}
				wgCounter[0] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 1).count();
				int count = 0;

				for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
						.collect(Collectors.toList())) {
					List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

					// cropIncome+=calcCropIncome(wealthGroupInterview, type);
					income1 = calcIncome(wealthGroupInterview, type);
					for (int l = 0; l < 5; l++) {
						income[l] += income1[l]; // running total
					}

					count++;
				}
				if (count == 0)// If no assets found - prevent divide by 0
					count = 1;

				for (int k = 0; k < 5; k++) // Calc average
				{

					averageTotal[0][k] += income[k];

					income[k] = income[k] / count;
					reportWB.getSheet(isheet).setValue(4 + k, row, income[k], numberStyle);
				}

			}
			row++;
			for (int k = 0; k < 5; k++) {
				income[k] = 0.0;
			}
			errno = 2267;
			if (listWealthgroup.stream().filter(p -> p.getWgorder() == 2).findAny().isPresent()) {
				for (int k = 0; k < 5; k++) {
					income1[k] = 0.0;
				}
				/* #536 - need all wg count for average calc - += added */
				wgCounter[1] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 2).count();
				int count = 0;

				for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
						.collect(Collectors.toList())) {
					List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

					// cropIncome+=calcCropIncome(wealthGroupInterview, type);
					income1 = calcIncome(wealthGroupInterview, type);
					for (int l = 0; l < 5; l++) {
						income[l] += income1[l]; // running total
					}

					count++;
				}
				if (count == 0)// If no assets found - prevnt divide by 0
					count = 1;

				for (int k = 0; k < 5; k++) // Calc average
				{

					averageTotal[1][k] += income[k];
					income[k] = income[k] / count;
					reportWB.getSheet(isheet).setValue(4 + k, row, income[k], numberStyle);
				}

			}
			row++;
			errno = 2268;
			for (int k = 0; k < 5; k++) {
				income[k] = 0.0;
			}
			if (listWealthgroup.stream().filter(p -> p.getWgorder() == 3).findAny().isPresent()) {
				for (int k = 0; k < 5; k++) {
					income1[k] = 0.0;
				}
				wgCounter[2] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 3).count();
				int count = 0;

				for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
						.collect(Collectors.toList())) {
					List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

					// cropIncome+=calcCropIncome(wealthGroupInterview, type);
					income1 = calcIncome(wealthGroupInterview, type);
					for (int l = 0; l < 5; l++) {
						income[l] += income1[l]; // running total
					}

					count++;
				}
				if (count == 0)// If no assets found - prevnt divide by 0
					count = 1;

				for (int k = 0; k < 5; k++) // Calc average
				{
					averageTotal[2][k] += income[k];

					income[k] = income[k] / count;
					reportWB.getSheet(isheet).setValue(4 + k, row, income[k], numberStyle);
				}

			}
			row++;
			errno = 2269;

		}

		errno = 3000;
		row = 3;
		/* Print averages at top of worksheet */

		for (int k = 0; k < 3; k++) { // for each wgorder

			if (wgCounter[k] == 0) {
				wgCounter[k] = 1;
			}

			List<WealthGroupInterview> wgiList = calcWGIList(k + 1); // List of WGi for wgorder = k
			// No - need to handle filters
			// Double[] incomelist = calcIncome(wgiList, type); // income array for wgilist
			// where wgorder = k

			for (int m = 0; m < 5; m++) {

				// reportWB.getSheet(isheet).setValue(m + 4, row, incomelist[m] / wgCounter[k],
				// numberStyle);
				reportWB.getSheet(isheet).setValue(m + 4, row, averageTotal[k][m] / wgCounter[k], numberStyle);
			}

			row++;
		}

	}

	/******************************************************************************************************************************************/

	private Double[] calcIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double[] income = new Double[5];

		income[CROP] = calcCropIncome(wealthGroupInterview, type);
		income[EMP] = calcEmpIncome(wealthGroupInterview, type);
		income[LS] = calcLSIncome(wealthGroupInterview, type);
		income[TR] = calcTransIncome(wealthGroupInterview, type);
		income[WF] = calcWFIncome(wealthGroupInterview, type);

		return (income);
	}

	/******************************************************************************************************************************************/
	private void createAssetreport(int isheet, Report report, String type) { // type = land or livestock

		int col = 4;
		int datarow = 10;
		int avgrow = 6;
		OptionalDouble totalLand = OptionalDouble.of(0.0);
		Double asset = 0.0;
		int assetTypeCounter = 0;
		int startRow = 4;
		int count = 0;
		int row = 0;
		int[] wgCounter = { 0, 0, 0 };

		System.out.println("In Asset rep");

		averageReset();
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 20, 20, 20, 20, 20, 20);
		populateFirstThreeColumns(isheet, startRow);

		if (type == "land") {
			reportWB.getSheet(isheet).setValue(1, 2, "Unit of Measure", boldTopStyle);

			// get Default Area in use

			Area areaMeasurement = project.getAreaMeasurement();

			if (areaMeasurement == null) {
				areaMeasurement = Area.Acre;
			}

			reportWB.getSheet(isheet).setValue(2, 2, areaMeasurement.toString(), textStyle);
		}
		errno = 2273;

		// Populate hhLand array for matrix

		numCommunities = uniqueCommunity.size(); // Used for averages

		double thisAverageTotal = 0.0;

		// Need all Land RST types that remain in wgi array
		// wgiLandRST has array of unique LAND RST

		List<WGI> wgiRST = null;

		if (type == "land") {

			wgiRST = wgi.stream().filter(p -> p.getLand() != null)
					.filter(distinctByKey(p -> p.getLand().getResourceSubType())).collect(Collectors.toList());
		} else if (type == "livestock") {

			wgiRST = wgi.stream().filter(p -> p.getLivestock() != null)
					.filter(distinctByKey(p -> p.getLivestock().getResourceSubType())).collect(Collectors.toList());
		}
		errno = 2274;
		List<WealthGroup> orderedWealthgroups2;

		for (WGI wgi3 : wgiRST) { // Get all Asset RSTs
			reportWB.getSheet(isheet).setColumnWidths(col, 30);
			String communityID = wgi3.getCommunity().getCommunityid();
			reportWB.getSheet(isheet).setValue(col, 1, "Asset Category", boldTopStyle);
			if (type == "land") {
				reportWB.getSheet(isheet).setValue(col, 2, "Land", textStyle);
				reportWB.getSheet(isheet).setValue(col, 4, wgi3.getLand().getResourceSubType().getResourcetypename(),
						textStyle);
			} else if (type == "livestock") {
				reportWB.getSheet(isheet).setValue(col, 2, "Livestock", textStyle);
				reportWB.getSheet(isheet).setValue(col, 4,
						wgi3.getLivestock().getResourceSubType().getResourcetypename(), textStyle);
			}
			reportWB.getSheet(isheet).setValue(col, 3, "Asset Type", boldTopStyle);

			wgCounter = new int[] { 0, 0, 0 };

			for (WGI wgi2 : uniqueCommunity) {

				/* Need to average out any WGs with more than 1 WG in WG Order (1,2,3) */

				/*************************************************************************************************/
				/*************************************************************************************************/
				ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();
				for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
						.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
					listWealthgroup.add(wgi4.getWealthgroup());
				}

				/* find any wgs and group by wgorder and get avg */
				errno = 2275;
				if (listWealthgroup.stream().filter(p -> p.getWgorder() == 1).findAny().isPresent()) {
					wgCounter[0] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 1).count();
					for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
							.collect(Collectors.toList())) {
						List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

						Optional<WealthGroupInterview> wgi1 = wealthGroupInterview.stream().findFirst();

						if (wgi1.isPresent()) {

							if (type == "land") {
								for (AssetLand assetLand : wgi1.get().getAssetLand().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLand.getNumberOfUnits();

									count++;
								}
							} else if (type == "livestock") {
								for (AssetLiveStock assetLivestock : wgi1.get().getAssetLiveStock().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLivestock.getNumberOwnedAtStart();

									count++;
								}

							}

						}

					}

					if (count == 0) {
						count = 1;
					}
					// asset = asset / count;
					reportWB.getSheet(isheet).setValue(col, datarow, asset / count, numberStyle);

					averageTotal[0][assetTypeCounter] = asset + averageTotal[0][assetTypeCounter];

				}
				datarow++;
				count = 0;
				asset = 0.0;
				errno = 22751;
				if (listWealthgroup.stream().filter(p -> p.getWgorder() == 2).findAny().isPresent()) {
					wgCounter[1] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 2).count();
					for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
							.collect(Collectors.toList())) {
						List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

						Optional<WealthGroupInterview> wgi1 = wealthGroupInterview.stream().findFirst();

						if (wgi1.isPresent()) {

							if (type == "land") {
								for (AssetLand assetLand : wgi1.get().getAssetLand().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLand.getNumberOfUnits();

									count++;
								}
							} else if (type == "livestock") {
								for (AssetLiveStock assetLivestock : wgi1.get().getAssetLiveStock().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLivestock.getNumberOwnedAtStart();

									count++;
								}

							}

						}

					}

					if (count == 0) {
						count = 1;
					}
					// asset = asset / count;
					reportWB.getSheet(isheet).setValue(col, datarow, asset / count, numberStyle);

					averageTotal[1][assetTypeCounter] = asset + averageTotal[1][assetTypeCounter];

				}
				datarow++;
				count = 0;
				asset = 0.0;
				errno = 22752;
				if (listWealthgroup.stream().filter(p -> p.getWgorder() == 3).findAny().isPresent()) {

					wgCounter[2] += (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 3).count();
					for (WealthGroup wealthGroup2 : listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
							.collect(Collectors.toList())) {
						List<WealthGroupInterview> wealthGroupInterview = wealthGroup2.getWealthGroupInterview();

						Optional<WealthGroupInterview> wgi1 = wealthGroupInterview.stream().findFirst();

						if (wgi1.isPresent()) {

							if (type == "land") {
								for (AssetLand assetLand : wgi1.get().getAssetLand().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLand.getNumberOfUnits();

									count++;
								}
							} else if (type == "livestock") {
								for (AssetLiveStock assetLivestock : wgi1.get().getAssetLiveStock().stream()
										.filter(p -> p.getResourceSubType() == wgi3.getResourceSubType())
										.collect(Collectors.toList())) {

									asset += assetLivestock.getNumberOwnedAtStart();

									count++;
								}

							}

						}

					}

					if (count == 0) {
						count = 1;
					}
					// asset = asset / count;
					reportWB.getSheet(isheet).setValue(col, datarow, asset / count, numberStyle);

					averageTotal[2][assetTypeCounter] = asset + averageTotal[2][assetTypeCounter];

				}
				datarow++;
				count = 0;
				asset = 0.0;
				errno = 2276;
			}
			// print averages

			errno = 2277;

			for (int j = 0; j < 3; j++) {

				errno = 22773;
				if (wgCounter[j] == 0)
					wgCounter[j] = 1;

				reportWB.getSheet(isheet).setValue(col, avgrow + j, averageTotal[j][assetTypeCounter] / wgCounter[j],
						numberStyle);
				errno = 22775;
			}
			errno = 22776;
			datarow = 10;
			col++;
			assetTypeCounter++;

		}
		errno = 2278;

	}

	/******************************************************************************************************************************************/

	private void populateFirstThreeColumns(int isheet, int startRow) {
		int row = startRow + 6;
		int wgrow;
		int averagesRow = startRow + 2;
		int countWGs = 0;
		System.out.println("In Popfirst rep");
		/* Print Communities */
		String comm;
		boolean isSkipNeeded = false;
		int[] countWG = { 0, 0, 0 };
		String[] wgName = { "Poor", "Middle", "Better Off" };

		reportWB.getSheet(isheet).setValue(1, startRow, "LZ", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, startRow, "Community", boldTopStyle);
		reportWB.getSheet(isheet).setValue(3, startRow, "Wealthgroup", boldTopStyle);

		// Place Averages Here
		reportWB.getSheet(isheet).setValue(2, averagesRow, "Averages", boldTopStyle);

		reportWB.getSheet(isheet).setValue(3, averagesRow, "Poor", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 1, "Middle", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 2, "Better Off", textStyle);

		reportWB.getSheet(isheet).setValue(1, row, livelihoodZone.getLzname(), textStyle);

		for (WGI wgi2 : uniqueCommunity) {
			comm = wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict();
			reportWB.getSheet(isheet).setValue(2, row, wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict(), textStyle);

			/* Print Wealthgroups within Community */
			/* Order by WG Order */

			wgi2.getCommunity().getWealthgroup();

			String communityID = wgi2.getCommunity().getCommunityid();
			// orderedWealthgroups = calcOrderedWealthGroup(wgi2);

			wgrow = row;

			for (int j = 0; j < 3; j++) {

				List<WealthGroup> wgspop = calcWGS(wgi2, j + 1);

				if (wgspop.size() == 0) {
					// print a blank row
					reportWB.getSheet(isheet).setValue(3, wgrow, "", textStyle);

				} else if (wgspop.size() > 1) {
					// print an averaged row with an extra *
					reportWB.getSheet(isheet).setValue(3, wgrow, wgName[j] + " (" + wgspop.get(0).getWgorder() + ")*",
							textStyle);

				} else {
					reportWB.getSheet(isheet).setValue(3, wgrow,
							wgspop.get(0).getWgnameeng() + " (" + wgspop.get(0).getWgorder() + ") ", textStyle);
				}
				wgrow++;
			}

			isDisplayWealthgroupDone = true;

			row = wgrow;

		}

	}

	private List<WealthGroup> calcWGS(WGI wgi2, int wgOrder) {
		return wgi2.getCommunity().getWealthgroup().stream().filter(p -> p.getWgorder() == wgOrder)
				.collect(Collectors.toList());
	}

	/******************************************************************************************************************************************/
	/* Report on micronutrients RDA % per Community WG */
	/* Populate nutrient array and use in report */

	private void createFoodNutrientreport(int isheet, Report report) {
		int col = 4;
		int row = 5;
		errno = 4373;
		int startRow = 4;
		int[] totalWGCount = { 0, 0, 0 };
		ArrayList<WealthGroup> listWealthgroup = new ArrayList<>();

		OptionalDouble[] wgDI = new OptionalDouble[3];
		int[] wgCounter = { 0, 0, 0 };

		System.out.println("In Food Nutrient rep 373");

		averageReset();
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 20, 20, 20, 20, 20, 20);
		populateFirstThreeColumns(isheet, startRow);
		errno = 4373_0;
		/* Get list of Micronutrients */

		nutrients.sort(Comparator.comparing(MicroNutrient::getName));

		reportWB.getSheet(isheet).setValue(col++, startRow, "KCal Requirement", boldTopStyle);

		for (int j = 0; j < nutrients.size(); j++) {
			reportWB.getSheet(isheet).setColumnWidths(col, 20);
			reportWB.getSheet(isheet).setValue(col++, startRow, nutrients.get(j).getName(), boldTopStyle);
		}
		row = 10;
		int nrow = 10;
		errno = 4373_1;

		for (WGI wgi2 : uniqueCommunity) {

			for (WGI wgi4 : wgi.stream().filter(p -> p.getCommunity() == wgi2.getCommunity())
					.filter(distinctByKey(w -> w.getWealthgroup())).collect(Collectors.toList())) {
				listWealthgroup.add(wgi4.getWealthgroup());
			}

			/* add DDI nutrients to nutrients array */
			defaultDietItems = (List<DefaultDietItem>) wgi2.getCommunity().getDefaultDietItem();
			errno = 4373_2;
			for (DefaultDietItem defaultDietItem : defaultDietItems) {
				/* how many calories needed of this RST */
				double calsNeeded = defaultDietItem.getCalsNeededofThisDDI();
				/*
				 * TODO Is calsneeded ever set?
				 */
				double rstKCAL = defaultDietItem.getResourcesubtype().getResourcesubtypekcal();

				double kgNeeded = calsNeeded / rstKCAL;

				/* Kgs needed getcals/rst kcal */

				errno = 4373_3;

				if (defaultDietItem.getResourcesubtype().getMccwFoodSource() != null) {

					for (MicroNutrientLevel mnl : defaultDietItem.getResourcesubtype().getMccwFoodSource()
							.getMicroNutrientLevel()) {
						/* Kgs * mn level in this RST */

						/* switch from negative to add amount made up in DDI */
						double mnLevel = NumberUtils.toDouble(mnl.getMnLevel()) * kgNeeded * -1.0;
						errno = 4373_4;

						/* Same for all WG as DDI is property of Community */
						for (int k = 1; k < 4; k++) {

							/* Note that wealthgroup and avgnumberinH not set in the NutrientCount array */

							NutrientCount thismn = new NutrientCount();
							thismn.setMn(mnl.getMicroNutrient());
							thismn.setMnAmount(mnLevel);

							thismn.setWgOrder(k);

							thismn.setDDI(true);

							overallNutrientCount.add(thismn);
							totalWGNutrientCount.add(thismn);

						}

					}

				}
			}

			/* find any wgs and group by wgorder and get avg */
			errno = 4374;
			col = 4;

			wgCounter[0] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 1).count();
			wgCounter[1] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 2).count();
			wgCounter[2] = (int) listWealthgroup.stream().filter(p -> p.getWgorder() == 3).count();

			List<WealthGroup> wg1 = listWealthgroup.stream().filter(p -> p.getWgorder() == 1)
					.collect(Collectors.toList());
			List<WealthGroup> wg2 = listWealthgroup.stream().filter(p -> p.getWgorder() == 2)
					.collect(Collectors.toList());
			List<WealthGroup> wg3 = listWealthgroup.stream().filter(p -> p.getWgorder() == 3)
					.collect(Collectors.toList());

			/* Note that this calc prints hhSize on spreadsheet */
			calcWGNutrients(isheet, col, row++, wg1, 1);
			calcWGNutrients(isheet, col, row++, wg2, 2);
			calcWGNutrients(isheet, col, row++, wg3, 3);

			/* print nutrients array for LZ and WGs */
			/* let convert all to kgs as that is the consumption unit */
			for (int k = 0; k < 3; k++) {
				for (int j = 0; j < nutrients.size(); j++) {
					JxlsStyle thisStyle;
					if (wgCounter[k] == 0) {
						break;
					}
					MicroNutrient microNutrient = nutrients.get(j);

					double totalNutrient = calcWGNutrient(microNutrient, k + 1);
					/* totalNutrient is in KGs * RDA unit */

					/* Data bug avgHHsize could be 0 - 0 is no longer allowed */

					if (hhSize[k] == 0.0) {
						hhSize[k] = 1;
					}
					double mnYearRDA = microNutrient.getRda() * hhSize[k] * 365;

					mnYearRDA = calcMNYearRDA(microNutrient, mnYearRDA);

					// divide by wgCounter to get average - normally will be 1.

					// daily RDA held in Nutrients table

					Double nutrientPerCent = 100 * totalNutrient / mnYearRDA;
					/*
					 * if (nutrientPerCent < 80.0) { reportWB.getSheet(isheet).setValue(5 + j, nrow,
					 * 100 * totalNutrient / mnYearRDA, numberRedStyle); } else if (nutrientPerCent
					 * < 100.0) { reportWB.getSheet(isheet).setValue(5 + j, nrow, 100 *
					 * totalNutrient / mnYearRDA, numberYellowStyle);
					 * 
					 * } else { reportWB.getSheet(isheet).setValue(5 + j, nrow, 100 * totalNutrient
					 * / mnYearRDA, numberGreenStyle);
					 * 
					 * }
					 */
					// reportWB.getSheet(isheet).setValue(5 + j, nrow, (100 * totalNutrient /
					// mnYearRDA)/wgCounter[k]+" "+totalNutrient+" , "+mnYearRDA+" , "+hhSize[k],
					// numberStyle);

					if (nutrientPerCent.isNaN()) {
						nutrientPerCent = 0.0;
					}
					reportWB.getSheet(isheet).setValue(5 + j, nrow, nutrientPerCent / wgCounter[k], numberStyle);

				}
				nrow++;
			}

			errno = 4375;

			totalWGNutrientCount.clear();
			listWealthgroup.clear();
		}

		/* Print averages */
		// total count in each group = countWGInEachWGOrder[0/1/2]

		errno = 4376;
		for (int wgrow = 0; wgrow < 3; wgrow++) {

			/* first print kcal average */
			double hh = calcAvgWGHH(wgrow + 1).orElse(0.0);

			double kcal = RC * hh;
			reportWB.getSheet(isheet).setValue(4, wgrow + 6, kcal, numberStyle);

			for (int j = 0; j < nutrients.size(); j++) {

				MicroNutrient microNutrient = nutrients.get(j);

				double totalNutrient = calcTotalNutrient(microNutrient, wgrow + 1);
				double mnYearRDA = nutrients.get(j).getRda() * allHHAverage[wgrow] * 365;
				mnYearRDA = calcMNYearRDA(microNutrient, mnYearRDA);

				Double percentOutput = (100 * totalNutrient / mnYearRDA) / countWGInEachWGOrder[wgrow];

				;
				if (percentOutput.isNaN()) {
					percentOutput = 0.0;
				}

				reportWB.getSheet(isheet).setValue(5 + j, wgrow + 6, percentOutput, numberStyle);

			}
			errno = 4377;
		}
		errno = 4378;
	}

	/******************************************************************************************************************************************/
	/* Convert to Kgs */
	public static double calcMNYearRDA(MicroNutrient microNutrient, double mnYearRDA) {
		if (microNutrient.getRdaUnit() == "�g") {

			mnYearRDA = mnYearRDA / 1000000000; // in kgs
		} else if (microNutrient.getRdaUnit() == "mg") {

			mnYearRDA = mnYearRDA / 1000000; // in kgs
		} else if (microNutrient.getRdaUnit() == "g") {

			mnYearRDA = mnYearRDA / 1000; // in kgs
		}
		return mnYearRDA;
	}

	/******************************************************************************************************************************************/
	/* For WG Overall array */
	private double calcTotalNutrient(MicroNutrient nutrient, int wgOrder) {
		return overallNutrientCount.stream().filter(p -> p.getWgOrder() == wgOrder).filter(p -> p.getMn() == nutrient)
				.mapToDouble(p -> p.getMnAmount()).sum();
	}

	/******************************************************************************************************************************************/
	/* For totalNutriebt array */
	private double calcWGNutrient(MicroNutrient microNutrient, int wgOrder) {
		double totalNutrient = totalWGNutrientCount.stream().filter(p -> p.getWgOrder() == wgOrder)
				.filter(p -> p.getMn() == microNutrient).mapToDouble(p -> p.getMnAmount()).sum();

		return totalNutrient;
	}

	/******************************************************************************************************************************************/

	private void calcWGNutrients(int isheet, int col, int row, List<WealthGroup> wg, int wgOrder) {

		int numberOfWGs = wg.size();
		double houseavgsize = 0.0;
		MCCWFoodSource thisMccwFoodSource;
		errno = 45000;
		if (wg.stream().filter(p -> p.getWgorder() == wgOrder).findAny().isPresent()) {

			for (int j = 0; j < wg.size(); j++) {

				for (WealthGroupInterview wgi6 : wg.get(j).getWealthGroupInterview()) {
					houseavgsize += wgi6.getWgAverageNumberInHH();

				}
			}
			hhSize[wgOrder - 1] = houseavgsize / wg.size();

			reportWB.getSheet(isheet).setValue(col++, row, hhSize[wgOrder - 1] * RC, numberStyle);

			/*
			 * Calc KCALS Consumed by crops/lsp/emp/transfer/wf for each wgi Then need to
			 * average across equal wgorders
			 * 
			 * Add to array nutrientCount
			 * 
			 * Aug 2020 Nutrients need to use RST Synonym
			 * 
			 */
			errno = 45010;

			for (int k = 0; k < wg.size(); k++) {
				for (WealthGroupInterview wgimnut : wg.get(k).getWealthGroupInterview()) {
					// if (wgimnut.getCrop().size() > 0) {

					for (Crop crop2 : wgimnut.getCrop()) {

						thisMccwFoodSource = calcMccWincludeSyn(crop2.getResourceSubType());

						if (thisMccwFoodSource != null) {

							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
								populateNutrientSource(wgOrder, hhSize[wgOrder - 1], crop2.getUnitsConsumed(), mn);
							}
						}
					}
					errno = 45020;
					for (LivestockProducts lsp : wgimnut.getLivestockProducts()) {

						thisMccwFoodSource = calcMccWincludeSyn(lsp.getResourceSubType());

						if (thisMccwFoodSource != null) {

							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {

								populateNutrientSource(wgOrder, hhSize[wgOrder - 1], lsp.getUnitsConsumed(), mn);

							}
						}
					}
					errno = 45030;
					for (Transfer tr : wgimnut.getTransfer()) {

						thisMccwFoodSource = calcMccWincludeSyn(tr.getResourceSubType());

						if (thisMccwFoodSource != null) {

							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
								populateNutrientSource(wgOrder, hhSize[wgOrder - 1], tr.getUnitsConsumed(), mn);

							}
						}
					}
					errno = 45040;
					for (Employment emp : wgimnut.getEmployment()) {

						thisMccwFoodSource = calcMccWincludeSyn(emp.getResourceSubType());

						if (thisMccwFoodSource != null) {

							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
								populateNutrientSource(wgOrder, hhSize[wgOrder - 1],
										emp.getPeopleCount() * emp.getFoodPaymentUnitsPaidWork(), mn);

							}
						}
					}
					errno = 45050;
					for (WildFood wf : wgimnut.getWildFood()) {

						thisMccwFoodSource = calcMccWincludeSyn(wf.getResourceSubType());

						if (thisMccwFoodSource != null) {

							for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
								populateNutrientSource(wgOrder, hhSize[wgOrder - 1], wf.getUnitsConsumed(), mn);

							}
						}
					}

					// }
				}

			}

		}

		/* need to work out average if more than one WG in this wgorder */

		/* Now to consolidate nutrients by WG */

		ArrayList<NutrientCount> dummyWGCount = (ArrayList<NutrientCount>) totalWGNutrientCount.clone();
		/* Dont forget number in wgorder */

		/* Do not delete the DDI components marked as isDDI true */

		Predicate<NutrientCount> equalWGOrder = i -> i.getWgOrder() == wgOrder;
		Predicate<NutrientCount> isaDDI = p -> p.isDDI() == false;

		// totalWGNutrientCount.removeIf(p -> p.getWgOrder() == wgOrder);

		totalWGNutrientCount.removeIf(equalWGOrder.and(isaDDI));

		for (int j = 0; j < nutrients.size(); j++) {
			MicroNutrient mn = nutrients.get(j);
			double sumOfMn = dummyWGCount.stream().filter(p -> p.getWgOrder() == wgOrder).filter(p -> p.getMn() == mn)
					.mapToDouble(p -> p.getMnAmount()).sum();

			if (sumOfMn != 0.0) {
				NutrientCount thismn = new NutrientCount();
				thismn.setMn(mn);
				thismn.setWgOrder(wgOrder);

				// thismn.setMnAmount(sumOfMn / wg.size()); // AVerage is set here for all
				// nutrients in array for a
				thismn.setMnAmount(sumOfMn); // WG
				thismn.setAverageNumberInHousehold(hhSize[3 - 1]);
				totalWGNutrientCount.add(thismn);
			}
		}

		dummyWGCount.clear();
		errno = 45500;

	}

	/******************************************************************************************************************************************/
	/*
	 * Is identified RST a Synonym? If so return the Micronutrient level of the base
	 * RST
	 */

	public static MCCWFoodSource calcMccWincludeSyn(ResourceSubType rst) {
		MCCWFoodSource thisMccwFoodSource;

		if (rst.getResourcesubtypesynonym() != null) {
			thisMccwFoodSource = rst.getResourcesubtypesynonym().getMccwFoodSource();
		} else {
			thisMccwFoodSource = rst.getMccwFoodSource();
		}

		return thisMccwFoodSource;
	}

	/******************************************************************************************************************************************/

	private void populateNutrientSource(int wgOrder, double avgInHH, double used, MicroNutrientLevel mn) {

		errno = 440010;
		if (NumberUtils.toDouble(mn.getMnLevel()) != 0.0) {

			NutrientCount thismn = new NutrientCount();
			thismn.setMn(mn.getMicroNutrient());

			double mnLevel = NumberUtils.toDouble(mn.getMnLevel());

			thismn.setWgOrder(wgOrder);
			thismn.setMnAmount(mnLevel * used * 10); // x10 as mnLevel is per 100g - convert to kg
			thismn.setAverageNumberInHousehold(avgInHH);
			thismn.setDDI(false);
			overallNutrientCount.add(thismn);
			totalWGNutrientCount.add(thismn);

		}
	}

	/******************************************************************************************************************************************/

	private double calcavgMNForArray(int wgOrder, ArrayList<NutrientCount> dummyWGNutrientCount, MicroNutrient mn) {

		List<NutrientCount> collect = dummyWGNutrientCount.stream().filter(p -> p.getMn() == mn)
				.collect(Collectors.toList());

		return dummyWGNutrientCount.stream().filter(p -> p.getMn() == mn).filter(p -> p.getWgOrder() == wgOrder)
				.mapToDouble(p -> p.getMnAmount()).sum();
	}

	/******************************************************************************************************************************************/

	// handle if Synonym is used and return base Kcal value
	private int findRSTKcal(ResourceSubType rst) {

		if (rst.getResourcesubtypesynonym() != null) {

			try {

				return (rst.getResourcesubtypesynonym().getResourcesubtypekcal());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addMessage("Cannot get Synonym KCal value");
				return (0);
			}

		}

		return (rst.getResourcesubtypekcal());
	}

	/******************************************************************************************************************************************/

	private Double calcWFIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double wfTot = 0.0;

		/* What about food payments? */

		for (int i = 0; i < wealthGroupInterview.size(); i++) {

			for (WildFood wf : wealthGroupInterview.get(i).getWildFood()) {
				if (wf.getIsFilteredOut() == true) {
					continue;
				}
				if (type == "cash") {

					wfTot += wf.getUnitsSold() * wf.getPricePerUnit();

				} else if (type == "food") {

					// wfTot += wf.getUnitsConsumed() *
					// wf.getResourceSubType().getResourcesubtypekcal();
					wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType());

				}
			}

		}

		return wfTot;
	}

	/******************************************************************************************************************************************/

	private Double calcTransIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double trTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Transfer tr : wealthGroupInterview.get(i).getTransfer()) {
				if (tr.getTransferType() == null)
					break;

				if (tr.getIsFilteredOut() == true) {
					continue;
				}

				if (type == "cash") {

					if (tr.getTransferType().equals(TransferType.Food)) {
						trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving()
								* tr.getTimesReceived();
					} else {
						trTot += tr.getPeopleReceiving() * tr.getTimesReceived() * tr.getCashTransferAmount();
					}

				} else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
						&& tr.getFoodResourceSubType() != null) {

					trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) * tr.getPeopleReceiving()
							* tr.getTimesReceived();

				}
			}

		}

		return trTot;
	}

	/******************************************************************************************************************************************/

	private Double calcLSIncome(List<WealthGroupInterview> wealthGroupInterview, String type) { // LSS and LSP sales
		Double lsTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {

			if (type == "cash") {

				for (LivestockSales ls : wealthGroupInterview.get(i).getLivestockSales()) {

					if (ls.getIsFilteredOut() == true) {
						continue;
					}

					lsTot += ls.getUnitsSold() * ls.getPricePerUnit();
				}

				for (LivestockProducts lsp : wealthGroupInterview.get(i).getLivestockProducts()) {

					if (lsp.getIsFilteredOut() == true) {
						continue;
					}
					lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit();
				}

			}

			else if (type == "food") {
				for (LivestockProducts lsp : wealthGroupInterview.get(i).getLivestockProducts()) {
					if (lsp.getIsFilteredOut() == true) {
						continue;
					}

					lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType());
				}
			} else {
				lsTot = 0.0;
			}
		}
		return lsTot;

	}

	/******************************************************************************************************************************************/

	private Double calcEmpIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {
		Double empTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Employment emp : wealthGroupInterview.get(i).getEmployment()) {

				if (emp.getIsFilteredOut() == true) {
					continue;
				}

				if (type == "cash") {

					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();

				} else if (type == "food" && emp.getFoodResourceSubType() != null) {

					empTot += (int) (emp.getFoodPaymentUnitsPaidWork() * emp.getPeopleCount()) * emp.getUnitsWorked()
							* findRSTKcal(emp.getFoodResourceSubType());
				} else {
					empTot = 0.0;
				}
			}
		}

		return empTot;

	}

	/******************************************************************************************************************************************/
	private Double calcCropIncome(List<WealthGroupInterview> wealthGroupInterview, String type) {

		Double cropTot = 0.0;

		for (int i = 0; i < wealthGroupInterview.size(); i++) {
			for (Crop crop : wealthGroupInterview.get(i).getCrop()) {

				if (crop.getIsFilteredOut() == true) {
					continue;
				}

				if (type == "cash") {

					cropTot += crop.getUnitsSold() * crop.getPricePerUnit();

				} else if (type == "food") {

					cropTot += crop.getUnitsConsumed() * findRSTKcal(crop.getResourceSubType());
				}
			}
		}

		return cropTot;
	}

	/******************************************************************************************************************************************/

	private Double wealthgroupInterviewAE(WealthGroupInterview wealthGroupInterview) {

		System.out.println("in wealthgroupInterview Adult Equivalent calc = ");

		Double totAE = 0.0;
		Double ageReq = 0.0;
		int age = 0;
		Sex gender;

		// TODO
		/*
		 * for (HouseholdMember hhm : household.getHouseholdMember()) { age =
		 * hhm.getAge(); gender = hhm.getGender();
		 * 
		 * WHOEnergyRequirements whoEnergy = WHOEnergyRequirements.findByAge(age);
		 * 
		 * // System.out.println("whoEnergy = " + whoEnergey.getFemale() + " " + //
		 * whoEnergey.getMale() + " " + gender); if (gender == Sex.Female) { totAE +=
		 * whoEnergy.getFemale(); } else if (gender == Sex.Male) { totAE +=
		 * whoEnergy.getMale(); }
		 * 
		 * }
		 */
		/* AE = TE / 2600 */

		totAE = totAE / 2600;
		// System.out.println("done householdAE calc");
		return Double.parseDouble(df2.format(totAE));

	}

	/******************************************************************************************************************************************/

	private void createHeaderPage(CustomReportSpecOHEA customReportSpec) {
		final int STARTROW = 6;
		int i = STARTROW; // used for row number

		System.out.println("In Create Header Page");

		sheet[0] = reportWB.addSheet("Custom Report Spec");
		setSheetStyle(sheet[0]);
		sheet[0].setColumnWidths(1, 40, 80, 50, 50, 50, 50, 25, 25, 50, 50);

		sheet[0].setValue(1, 1, "Date:", boldRStyle);
		sheet[0].setValue(2, 1, new Date(), dateStyle);
		sheet[0].setValue(1, 2, "Spec Name:", boldRStyle);
		sheet[0].setValue(2, 2, customReportSpec.getSpecName(), textStyleLeft);
		sheet[0].setValue(1, 3, "Livelihood Zone:", boldRStyle);
		sheet[0].setValue(2, 3, project.getProjecttitle() + " / " + livelihoodZone.getLzname(), textStyleLeft);
		// study.getReferenceYear(), textStyle);
		sheet[0].setValue(2, STARTROW, "Reports", boldLStyle);
		/* get list of reports and create tabbed bsheets for each */

		/* If no reports in custom report spec then use all reports */
		i++;

		/*
		 * #561 Always output every spreadsheet sheet. If not requested in custom report
		 * spec then leave blank
		 */
		// if (customReportSpec.getReport().size() > 0)
		// {

		Set<Report> reportSet = customReportSpec.getReport();

		reportList = reportSet.stream().collect(Collectors.toList());

		// }
		// else {
		// Only used in this func
		List<Report> fullReportList = XPersistence.getManager()
				.createQuery("from Report where Method = 'OHEA' and code >10").getResultList();
		// }

		// Sort the lists of reports
		fullReportList.sort(Comparator.comparingInt(Report::getCode));
		reportList.sort(Comparator.comparingInt(Report::getCode));

		// List of reps on front sheet
		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyleLeft);
			i++;
		}
		// Create sheets, some maybe blank
		i = STARTROW;
		for (Report report : fullReportList) {
			sheet[i - 3] = reportWB.addSheet(report.getCode() + " " + report.getName());
			setSheetStyle(sheet[i - 3]);
			i++;
		}

		i = STARTROW;

		int col = 3;
		// Selected HH
		errno = 1101;
		String siteName;
		String wgName;

		Comparator<Site> compareBy = Comparator.comparing(Site::getLocationdistrict)
				.thenComparing(Site::getSubdistrict);
		sites.sort(compareBy);

		// if (isSelectedSites) {
		sheet[0].setValue(col, i, "Selected Communities in Report = " + sites.size(), boldTopStyle);
		// sheet[0].setValue(col + 1, i, "Wealthgroups in Report = ", boldTopStyle);
		i++;

		for (Site site : sites) {
			siteName = site.getLocationdistrict() + " " + site.getSubdistrict();
			sheet[0].setValue(col, i, siteName, textStyle);

			i++;

		}
		// }

		i = STARTROW;

		col++;
		errno = 1102;

		sheet[0].setValue(col, i, "Categories Included", boldTopStyle);
		i++;
		for (Category category : customReportSpec.getCategory()) {
			sheet[0].setValue(col, i, category.getCategoryName(), textStyle);
			i++;
		}

		i = STARTROW;
		col++;
		errno = 1103;

		sheet[0].setValue(col, i, "Resources Included", boldTopStyle);
		i++;
		for (ResourceType rt : customReportSpec.getResourceType()) {
			sheet[0].setValue(col, i, rt.getResourcetypename(), textStyle);
			i++;
		}

		i = STARTROW;
		col++;

		errno = 1104;

		sheet[0].setValue(col, i, "Resource Subtypes Included", boldTopStyle);
		i++;
		for (ResourceSubType rst : customReportSpec.getResourceSubType()) {
			sheet[0].setValue(col, i, rst.getResourcetypename(), textStyle);
			i++;
		}

		String currency = livelihoodZone.getCountry().getCurrency();

		/*
		 * Cross Projects so unsure what currency should be set to - assume Project
		 * country currency
		 */
		/*
		 * TODO
		 */
		/*
		 * try { if (StringUtils.isEmpty(getProjectlz().getAltCurrency().getCurrency()))
		 * { currency = study.getSite().getLivelihoodZone().getCountry().getCurrency();
		 * } else { currency = study.getProjectlz().getAltCurrency().getCurrency(); } }
		 * catch (Exception e1) { // TODO Auto-generated catch block //
		 * e1.printStackTrace(); }
		 * 
		 * errno = 1107;
		 * 
		 */
		sheet[0].setValue(1, 4, "Reporting Currency:", boldTopStyle);
		sheet[0].setValue(2, 4, currency, textStyleLeft);

		errno = 1108;
	}

	/******************************************************************************************************************************************/

	private void setSheetStyle(JxlsSheet sheet) {
		sheet.setColumnWidths(1, 20, 60);

	}

	/******************************************************************************************************************************************/

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}


	/******************************************************************************************************************************************/

	@Override
	public String getForwardURI() {
		return forwardURI;
	}

	/******************************************************************************************************************************************/

	@Override
	public boolean inNewWindow() {
		if (forwardURI == null)
			return false;
		return true;
	}

	/******************************************************************************************************************************************/

	public void setForwardURI(String forwardURI) {
		this.forwardURI = forwardURI;
	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

	public static class WGISub {
		private int wgiid;
		private Community community;
		private WealthGroupInterview wealthGroupInterview;
		private WealthGroup wealthGroup;
		private AssetLand assetLand;
		private AssetLiveStock assetLivestock;
		private ResourceSubType assetRST;
		private String assetName;
		private Double assetValue;
		private Double wgiDI;
		private int column;

		public Community getCommunity() {
			return community;
		}

		public void setCommunity(Community community) {
			this.community = community;
		}

		public WealthGroupInterview getWealthGroupInterview() {
			return wealthGroupInterview;
		}

		public void setWealthGroupInterview(WealthGroupInterview wealthGroupInterview) {
			this.wealthGroupInterview = wealthGroupInterview;
		}

		public WealthGroup getWealthGroup() {
			return wealthGroup;
		}

		public void setWealthGroup(WealthGroup wealthGroup) {
			this.wealthGroup = wealthGroup;
		}

		public String getAssetName() {
			return assetName;
		}

		public void setAssetName(String assetName) {
			this.assetName = assetName;
		}

		public int getWgiid() {
			return wgiid;
		}

		public void setWgiid(int wgiid) {
			this.wgiid = wgiid;
		}

		public AssetLand getAssetLand() {
			return assetLand;
		}

		public void setAssetLand(AssetLand assetLand) {
			this.assetLand = assetLand;
		}

		public AssetLiveStock getAssetLivestock() {
			return assetLivestock;
		}

		public void setAssetLivestock(AssetLiveStock assetLivestock) {
			this.assetLivestock = assetLivestock;
		}

		public ResourceSubType getAssetRST() {
			return assetRST;
		}

		public void setAssetRST(ResourceSubType assetRST) {
			this.assetRST = assetRST;
		}

		public Double getAssetValue() {
			return assetValue;
		}

		public void setAssetValue(Double assetValue) {
			this.assetValue = assetValue;
		}

		public Double getWgiDI() {
			return wgiDI;
		}

		public void setWgiDI(Double wgiDI) {
			this.wgiDI = wgiDI;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

	}

	public class QuantHousehold {

		private Quantile quantile;

		private Household household;

		private Double averageDI;

		public Double getAverageDI() {
			return averageDI;
		}

		public void setAverageDI(Double averageDI) {
			this.averageDI = averageDI;
		}

		public Quantile getQuantile() {
			return quantile;
		}

		public void setQuantile(Quantile quantile) {
			this.quantile = quantile;
		}

		public Household getHousehold() {
			return household;
		}

		public void setHousehold(Household household) {
			this.household = household;
		}

	}

}