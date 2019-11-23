
/* Run OIHM Reports2 
 * 
 *
 * Current codes 20/5/19
 * DRB
 * 
 * Note that Issue 145 has DI calc
 * 
223 HH Disposable Income (DI): sorted by ascending DI 223
224 HH DI with Standard of Living Costs: sorted by ascending DI 224
225 HH Cash Income by source type: sorted by ascending DI 225
226 HH Food Income by source type: sorted by ascending DI 226
227 HH Land Assets: sorted by ascending DI 227
228 HH Livestock Assets: sorted by ascending DI 228
237 Report HH Members characteristics 237
236 Calculate Adult Equivalent KCal requirement for a Household 236
238 Report Population by Age and Sex 238
 *
 * Get all HH for this study into hh array
 * 
 * Filter hh on Custom Report filters 
 * 
 * Calculate DI etc.
 * 
 * */

package efd.actions;

import java.text.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.*;
import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.tab.*;
import org.openxava.tab.Tab;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import com.sun.xml.internal.bind.v2.*;

import efd.actions.OHEAReports.*;
import efd.actions.OIHMReports.*;
import efd.model.*;
import efd.model.HouseholdMember.*;
import efd.model.StdOfLivingElement.*;
import efd.model.Transfer.*;
import efd.model.WealthGroupInterview.*;
import net.sf.jasperreports.charts.type.*;

public class OHEAReports extends TabBaseAction implements IForwardAction, JxlsConstants {

	static Double RC = 2200.0 * 365; // NOTE that this is from OHEA. In OIHM we can be more accurate as we know age
										// and sex of HH Members
	private static DecimalFormat df2 = new DecimalFormat("#.00");
	static final int NUMBER_OF_REPORTS = 15;
	private Community community = null;
	private LivelihoodZone livelihoodZone = null;
	private Project project = null;
	// private CustomReportSpec customReportSpec = null;

	private List<Report> reportList;
	private List<Site> selectedSites = new ArrayList<>();

	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	List<WGI> uniqueWealthgroupInterview;
	List<WGI> uniqueCommunity;

	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;

	ArrayList<WGI> wgi = new ArrayList<>();
	ArrayList<Site> sites = new ArrayList<>();
	ArrayList<WGI> wgiSelected = new ArrayList<>();
	ArrayList<WealthGroupInterview> wgiList = new ArrayList<>();
	ArrayList<QuantHousehold> quanthh = new ArrayList<>();
	ArrayList<WealthGroup> displayWealthgroup = new ArrayList<>();; // Note that assuming 1:1 WG:WGI for the

	List<WGI> orderedQuantSeq = null;
	List<WealthGroup> orderedWealthgroups;

	Map<Integer, Double> quantAvg = null;

	private String forwardURI = null;

	JxlsStyle boldRStyle = null;
	JxlsStyle boldLStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
	JxlsStyle textStyleLeft = null;
	JxlsStyle dateStyle = null;
	JxlsStyle numberStyle = null;
	JxlsStyle f1Style = null;
	JxlsStyle textStyleBlue = null;

	CellStyle style = null;
	CellStyle vstyle = null;
	CellStyle datestyle = null;
	CellStyle title = null;
	CellStyle cnumberStyle = null;

	int errno = 0;
	Boolean isQuantile = false;
	Boolean isDisplayWealthgroupDone = false;
	Boolean isSelectedSites = false;
	String currency2;

	int numCommunities = 0;

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

		Map allValues = getPreviousView().getAllValues();

		String lzid = getView().getValueString("livelihoodZone.lzid");
		livelihoodZone = XPersistence.getManager().find(LivelihoodZone.class, lzid);

		String projectid = getPreviousView().getValueString("projectid");
		project = XPersistence.getManager().find(Project.class, projectid);

		Object communityId = null; // getPreviousView().getValue("communityid");

		Tab targetTab = getView().getSubview("livelihoodZone.site").getCollectionTab();

		Map[] selectedOnes = targetTab.getSelectedKeys();

		System.out.println("selected keys a = " + selectedOnes.length);
		WealthGroupInterview wealthGroupInterview = null;
		Site site = null;

		if (selectedOnes.length == 0) {
			addError("Choose at least one Community / Site");
			return;
		} else if (selectedOnes.length != 0) {
			isSelectedSites = true; // One or more Site selected in dialog
			for (int i = 0; i < selectedOnes.length; i++) {
				Map<?, ?> key = selectedOnes[i];
				System.out.println("key = " + key.toString());
				String subKey = key.toString().substring(12, 44);
				System.out.println("subkey = " + subKey.toString());
				site = XPersistence.getManager().find(Site.class, subKey);
				System.out.println("site = " + site.toString());

				// add to array for Sites/Communities
				Site s = new Site();
				s = site;
				sites.add(s);
				System.out.println("no of sites = " + sites.size());
				// From Site get Communities/Sites then get WG and WGI

				try {

					for (Community community2 : site.getCommunity()) {
						countValidated = 0;
						System.out.println("community = " + community2.getSite().getLocationdistrict() + " "
								+ community2.getSite().getSubdistrict());
						System.out.println("nos of wgs = " + community2.getWealthgroup().size());

						Iterator<WealthGroup> wgIterator = community2.getWealthgroup().iterator();
						while (wgIterator.hasNext()) {
							System.out.println("wgIterator ");
							WealthGroup wgNext = wgIterator.next();
							System.out.println("wgNext " + wgNext.getWealthGroupInterview().size());
							Iterator<WealthGroupInterview> wgiIterator = wgNext.getWealthGroupInterview().iterator();
							while (wgiIterator.hasNext()) {
								WealthGroupInterview wgiNext = wgiIterator.next();
								System.out.println("wgiNext = " + wgiNext.getWgIntervieweesCount());
								if (wgiNext.getStatus() == Status.Validated) {
									WealthGroupInterview w = new WealthGroupInterview();
									w = wgiNext;

									wgiList.add(w);

									WGI e = new WGI();
									e.wealthgroupInterview = wgiNext;
									e.wealthgroup = wgNext;
									e.site = community2.getSite();
									e.community = community2;
									isSelectedSites = true;
									wgiSelected.add(e);
									countValidated++;

								}
							}

						}
						System.out.println("countValidated = " + countValidated);
						if (countValidated < 3) // Community needs at least 3 Validated WGs
						{
							wgi.removeIf(p -> p.community == community2);
							wgiList.removeIf(p -> p.getWealthgroup().getCommunity() == community2);
							System.out.println("removed community " + community2.getSite().getLocationdistrict() + " "
									+ community2.getSite().getSubdistrict());
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					break;
				}

			}

		}

		// wgiSelected populates wgis array
		for (WGI wgi : wgiSelected) {
			System.out.println("wgiselected  = " + wgi.site.getLocationdistrict() + " " + wgi.site.getSubdistrict()
					+ " " + wgi.getWealthgroup().getWgnameeng());

		}

		for (WealthGroupInterview wgiList2 : wgiList) {
			System.out.println("wgiList2 = " + wgiList2.getWealthgroup().getWgnameeng());
		}

		System.out.println("no of wgi = " + wgiSelected.size());
		System.out.println("specid = " + specID);
		CustomReportSpecOHEA customReportSpecOHEA = XPersistence.getManager().find(CustomReportSpecOHEA.class, specID);
		System.out.println("specname = " + customReportSpecOHEA.getSpecName());

		// community = XPersistence.getManager().find(Community.class, communityId);

		// defaultDietItems = (List<DefaultDietItem>) community.getDefaultDietItem();

		/* Populate WHO table */

		// int ddiTotPercent = 0;

		// for (DefaultDietItem defaultDietItem : defaultDietItems) {
		// ddiTotPercent += defaultDietItem.getPercentage();
		// }
		// if (ddiTotPercent != 100 || ddiTotPercent != 0) {
		// addError("Default Diet Total Percentage for this Study is not 100%");
		// closeDialog();
		// return;
		// }

		errno = 50;

		// Populate WGI array wgis - use dialog selected list if enter

		populateWGIArray(wgiList);
		System.out.println("wgi size 1 = " + wgi.size());
		uniqueCommunity = wgi.stream().filter(distinctByKey(WGI::getCommunity)).collect(Collectors.toList());
		System.out.println("wgi size 2= " + wgi.size());
		// uniqueWealthgroupInterview = wgi.stream().filter(distinctByKey(p ->
		// p.getWealthgroupInterview().getWgiid()))
		// .sorted(Comparator.comparing(WGI::getWgiDI)).collect(Collectors.toList());

		errno = 51;
		// Filter according to Catalog/RT/RST/HH

		if (filterWGI(customReportSpecOHEA) == 0) {
			addError("No Wealthgroups meet criteria, change Report Spec");
			closeDialog();
			return;
		}

		System.out.println("wgi size 3= " + wgi.size());
		errno = 52;
		// Calculate DI

		// calculateDI(); // uses hh filtered array based on CRS definition
		System.out.println("done calc DI");
		// calculateAE(); // Calculate the Adult equivalent
		System.out.println("done calc AE");

		// if (uniqueWealthgroupInterview.size() == 0) {
		// addError("No Wealthgroups meet criteria, change Report Spec");
		// closeDialog();
		// return;
		// }

		errno = 54;
		// Run reports
		try {
			System.out.println("in try to run reports crs = " + customReportSpecOHEA.getSpecName());
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

		// uniqueWealthgroup = wg.stream().filter(distinctByKey(p ->
		// p.getWealthgroup().getWgorder()))
		// .sorted(Comparator.comparing(WGI::getWgDI)).collect(Collectors.toList());

		// for (HH hh2 : hh) {
		for (WGI wg2 : uniqueWealthgroupInterview) {

			// wg2.wgiDI = wealthgroupInterviewDI(wg2.wealthgroupInterview);
		}

		// If QUantile then need to calc which quantile each unique HH is in

	}

	/******************************************************************************************************************************************/
	private int filterWGI(CustomReportSpecOHEA customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from wg populate array
		// Filter out reportspecuse non included wg if exist and for same Community

		System.out.println("filter 000");

		if (customReportSpec.getCategory().size() > 0) // Apply Category Filter
		{
			System.out.println("in Cat filter");
			List<WGI> wgCAT = wgi.stream().filter(p -> p.getCategory() != null).collect(Collectors.toList());

			for (Category category : customReportSpec.getCategory()) {
				for (WGI wg2 : wgCAT) {
					if (wg2.getResourceSubType().getCategory() == category) {

						wg2.setDelete(false);
					} else {

						wg2.setDelete(true);
					}
				}

			}

		}

		if (customReportSpec.getResourceType().size() > 0) // Apply RST Filter

		{
			System.out.println("in RT filter");
			List<WGI> wgRT = wgi.stream().filter(p -> p.getResourceType() != null).collect(Collectors.toList());
			System.out.println("After RT size  wg2 = " + wgRT.size());

			for (WGI wg2 : wgRT) {

				for (ResourceType resourceType : customReportSpec.getResourceType()) {

					if (resourceType != wg2.getResourceType()) {

						wg2.setDelete(true);
					}

					else {

						wg2.setDelete(false);
						break;
					}

				}
			}

		}

		System.out.println("After RT wgi = " + wgi.size());
		if (customReportSpec.getResourceSubType().isEmpty()) // Apply RST Filter
		{
			System.out.println("in RST filter");
			List<WGI> wgRST = wgi.stream().filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

			for (WGI wg2 : wgRST) {

				for (ResourceSubType resourceSubType : customReportSpec.getResourceSubType()) {
					if (resourceSubType.getIdresourcesubtype() != wg2.getResourceSubType().getIdresourcesubtype()) {

						wg2.setDelete(true);
					} else {

						wg2.setDelete(false);
						break;
					}
				}

			}

		}

		try {
			wgi.removeIf(n -> n.getDelete() == true);
		} catch (Exception e) {
			System.out.println("nothing filtered to remove");

		}

//		wgi.stream().forEach(p -> System.out.println("wgi post filter = "+p.community.getSite().getSubdistrict()+
//				" "+p.wealthgroup.getWgnameeng()+
//				" "+p.getLand().getResourceSubType().getResourcetypename()));

		return wgi.size();

	}

	/******************************************************************************************************************************************/
	private void populateWGIArray(List<WealthGroupInterview> wealthgroupsInterviews) {
		/*
		 * create array of all Validated wealthgroups for this commmunity
		 * 
		 * they can then be filtered
		 * 
		 */
		System.out.println("drb in populateArray begin size = " + wealthgroupsInterviews.size());

		for (WealthGroupInterview wealthgroupInterview : wealthgroupsInterviews) {
			System.out.println("in pop wgi array ");
			populateWGIfromWealthgroupInterview(wealthgroupInterview);
			System.out.println("back from  pop wgi array ");

		}

		System.out.println("end populateArray =" + wgi.size());

		for (WGI wgi2 : wgi) {
			for (AssetLand assetLand : wgi2.getWealthgroupInterview().getAssetLand()) {
				System.out.println("post populate assetland = " + assetLand.getResourceSubType().getResourcetypename()
						+ " " + assetLand.getNumberOfUnits());
			}
		}

		System.out.println(Arrays.toString(wgi.toArray()));
		// System.out.println("WGI Array = " + wgi.toString());

	}

	/******************************************************************************************************************************************/

	private void populateWGIfromWealthgroupInterview(WealthGroupInterview wealthGroupInterview) {

		// System.out.println("in populateHHfromHosuehold, hosuehold =
		// "+household.getHouseholdName()+" answer =
		// "+answer.getConfigQuestionUse().getConfigQuestion().getPrompt());;

		WealthGroupInterview wgi2;

		System.out.println("assetland size = " + wealthGroupInterview.getAssetLand().size());
		System.out.println("assetfs size = " + wealthGroupInterview.getAssetFoodStock().size());
		System.out.println("transfer ass size = " + wealthGroupInterview.getTransfer().size());

		for (AssetLand asset : wealthGroupInterview.getAssetLand()) {

			System.out.println("assetland = " + asset.getResourceSubType().getResourcetypename());
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
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
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
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
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

		for (WGI wgi3 : wgi) {
			// System.out.println(" wgi 3 Land =
			// "+wgi3.getLand().getResourceSubType().getResourcetypename());
			System.out.println(" wgi 3 RST = " + wgi3.getResourceSubType().getResourcetypename());
			System.out.println(
					" wgi 3 Site = " + wgi3.getSite().getLocationdistrict() + " " + wgi3.getSite().getSubdistrict());
			System.out.println(" wgi 3 LZ = " + wgi3.getLivelihoodZone().getLzname());
			System.out.println(" wgi 3 Proj = " + wgi3.getProject().getProjecttitle());

		}

		System.out.println("return from array pop");
	}

	/******************************************************************************************************************************************/

	private void addToWGIArray(WealthGroupInterview wealthGroupInterview, Collection<Category> category,
			ResourceType resourceType, ResourceSubType resourceSubType, String type, AssetLand land,
			AssetFoodStock foodstock, AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable,
			AssetTree tree, Crop crop, Employment employment, Inputs inputs, LivestockProducts livestockproducts,
			LivestockSales livestocksales, Transfer transfer, WildFood wildfood) {

		WGI e = new WGI();

		System.out.println("in addtowgiarray " + wealthGroupInterview);

		e.wealthgroupInterview = wealthGroupInterview;
		e.site = wealthGroupInterview.getWealthgroup().getCommunity().getSite();
		e.community = wealthGroupInterview.getWealthgroup().getCommunity();
		e.wealthgroup = wealthGroupInterview.getWealthgroup();
		e.project = project;
		e.livelihoodZone = livelihoodZone;

		// e.wgiNumber = 0;
		e.category = category;
		e.resourceType = resourceType;
		e.resourceSubType = resourceSubType;

		e.type = type;
		e.land = land;
		e.employment = employment;
		e.foodstock = foodstock;
		e.inputs = inputs;
		e.livestock = livestock;
		e.livestockproducts = livestockproducts;
		e.livestocksales = livestocksales;
		e.tradeable = tradeable;
		e.transfer = transfer;
		e.tree = tree;
		e.wildfood = wildfood;
		e.crop = crop;
		e.cash = cash;

		wgi.add(e);

	}

	/******************************************************************************************************************************************/

	private JxlsWorkbook createReport(CustomReportSpecOHEA customReportSpec) throws Exception {
		System.out.println("In Run OHEA Reports create xls 000");

		String filename = customReportSpec.getSpecName() + Calendar.getInstance().getTime();
		System.out.println("In Run OHEA Reports create xls 111");
		reportWB = new JxlsWorkbook(filename);
		System.out.println("In Run OHEA Reports create xls 222");

		setStyles();
		System.out.println("In Run OHEA Reports create xls 333");
		createHeaderPage(customReportSpec); // populates reportList
		System.out.println("In Run OHEA Reports create xls  444");
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
		System.out.println("wgi before run rep = " + wgi.size());
		for (Report report : reportList) {
			System.out.println("code = " + report.getCode());
			int reportCode = report.getCode();
			System.out.println("pre switch 001");
			ireportNumber++; // keep track of number of reports = sheet on output spreadsheet

			switch (reportCode) {
			case 366:
				System.out.println("report 366");
				createDIreport(ireportNumber, report);
				break;
			case 367:
				System.out.println("report 367");
				// createDIAfterSOLreport(ireportNumber, report);
				break;
			case 368:
				System.out.println("report 368");
				// createIncomereport(ireportNumber, report, "cash");
				break;
			case 369:
				System.out.println("report 369");
				// createIncomereport(ireportNumber, report, "food");
				break;

			case 370:
				System.out.println("report 370");
				createLandAssetreport(ireportNumber, report);
				break;

			case 371:
				System.out.println("report 371");
				// createLivestockAssetreport(ireportNumber, report);
				break;
			case 372:
				System.out.println("report 372");
				// TODO ???
				break;
			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/

	private void createDIreport(int isheet, Report report) {

		int row = 1;

		System.out.println("in 366 report ");
		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		populateFirstThreeColumns(isheet, 1);

		// wgi is array of all data
		// need an array of valid WGI now left
		// ordered by DI

		// NOTE OHEA ordered by wg Number order not DI

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);
		System.out.println("DI non quantile ");
		// reportWB.getSheet(isheet).setValue(1, row, "Wealthgroup Number", textStyle);
		// reportWB.getSheet(isheet).setValue(2, row, "Disposable Income", textStyle);
		row++;
		errno = 3665;
		/*
		 * for (WGI wgi2 : uniqueWealthgroupInterview) {
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, wgi2.wgiNumber, textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, wgi2.wgiDI, textStyle); row++;
		 * 
		 * }
		 */
		errno = 3666;
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
		Double hhSOLC = 0.0;

		// NOTE No STOL yet
		// DRB TODO

		System.out.println("In SOL Report 267");

		// uniqeHousehold = number of households in array that are relevant
		/*
		 * for (WGI wgi3 : uniqueWealthgroupInterview ) { errno = 101; hhSOLC = 0.0;
		 * 
		 * for (StdOfLivingElement stdOfLivingElement :
		 * wgi3.getHousehold().getStudy().getStdOfLivingElement()) { errno = 102;
		 * 
		 * if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) { errno = 103;
		 * hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount()); }
		 * else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {
		 * errno = 104; for (HouseholdMember householdMember :
		 * hh3.getHousehold().getHouseholdMember()) {
		 * System.out.println("hhmember in SOLC = " + hh3.getHhNumber()); hhSOLC +=
		 * calcHhmSolc(hh3, stdOfLivingElement); errno = 105; }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * errno = 106; hh3.setHhSOLC(hhSOLC);
		 * 
		 * }
		 * 
		 * 
		 * 
		 * errno = 108; reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);
		 * reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, "Standard of Living Requirement",
		 * textStyle); reportWB.getSheet(isheet).setValue(3, row, "Disposable Income",
		 * textStyle); row++; for (HH hh2 : uniqueHousehold) {
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDI(), textStyle);
		 * 
		 * row++; }
		 */
		errno = 109;
	}

	/******************************************************************************************************************************************/

	private void createIncomereport(int isheet, Report report, String type) {
		/*
		 * int row = 1; // String type = "food" or "cash" errno = 2261;
		 * System.out.println("in income report 226/7"); Double cropIncome = 0.0; Double
		 * empIncome = 0.0; Double lsIncome = 0.0; Double trIncome = 0.0; Double
		 * wfIncome = 0.0; int col = 0;
		 * 
		 * String initType = StringUtils.capitalize(type);
		 * 
		 * System.out.println("in  income report");
		 * reportWB.getSheet(isheet).setColumnWidths(1, 25, 25, 25, 25, 25, 25, 25);
		 * errno = 2262;
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		 * 
		 * reportWB.getSheet(isheet).setValue(col++, row, "Crop " + initType +
		 * " Income", textStyle); reportWB.getSheet(isheet).setValue(col++, row,
		 * "Employment " + initType + " Income", textStyle);
		 * reportWB.getSheet(isheet).setValue(col++, row, "Livestock " + initType +
		 * " Income", textStyle); reportWB.getSheet(isheet).setValue(col++, row,
		 * "Transfer " + initType + " Income", textStyle);
		 * reportWB.getSheet(isheet).setValue(col++, row, "Wildfood " + initType +
		 * " Income", textStyle); errno = 2263; row = 2; System.out.println("in " + type
		 * + " income report done heading");
		 * 
		 * errno = 2265; for (WGI wgi2 : uniqueWealthgroupInterview) {
		 * 
		 * cropIncome = calcCropIncome(wgi2, type); empIncome = calcEmpIncome(wgi2,
		 * type); lsIncome = calcLSIncome(wgi2, type); trIncome = calcTransIncome(wgi2,
		 * type); wfIncome = calcWFIncome(wgi2, type);
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, wgi2.wgiNumber, textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, cropIncome, textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, empIncome, textStyle);
		 * reportWB.getSheet(isheet).setValue(4, row, lsIncome, textStyle);
		 * reportWB.getSheet(isheet).setValue(5, row, trIncome, textStyle);
		 * reportWB.getSheet(isheet).setValue(6, row, wfIncome, textStyle); row++;
		 * 
		 * }
		 * 
		 */
	}

	/******************************************************************************************************************************************/
	private void createLandAssetreport(int isheet, Report report) {

		int col = 4;
		int datarow = 10;
		int avgrow = 6;
		double totalLand = 0.0;

		Map<ResourceSubType, Double> landTot = new HashMap<>();

		System.out.println("In SOL Report 370 dr - Land Assets");

		ArrayList<WGISub> wgil = new ArrayList<>();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 20, 20, 20, 20, 20, 20, 20);
		errno = 2271;

		System.out.println("uc = " + uniqueCommunity.size());
		int startRow = 4;
		populateFirstThreeColumns(isheet, startRow);

		errno = 2272;
		/* need to create matrix of wgi id against land assets */

		/* How many land assets for this set of filtered community / wealthgroups */

		errno = 2273;

		// Populate hhLand array for matrix
		Double total = 0.0;
		String comm;

		numCommunities = uniqueCommunity.size(); // Used for averages
		List<Double> averageTotal = new ArrayList<Double>() {
			{
				add(0.0);
				add(0.0);
				add(0.0);

			}
		};

		double thisAverageTotal = 0.0;

		// Need all Land RST types that remain in wgi array
		// wgiLandRST has array of unique LAND RST

		List<WGI> wgiLandRST = wgi.stream().filter(p -> p.getLand() != null)
				.filter(distinctByKey(p -> p.getLand().getResourceSubType())).collect(Collectors.toList());

		System.out.println("wgiLandRST drb = " + wgiLandRST.size());
		/***************************************************************************************************************************/

		/***************************************************************************************************************************/

		/***************************************************************************************************************************/
		List<WealthGroup> orderedWealthgroups2;

		for (WGI wgi3 : wgiLandRST) {
			String communityID = wgi3.getCommunity().getCommunityid();
			reportWB.getSheet(isheet).setValue(col, 1, "Asset Category", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 2, "Land", textStyle);
			reportWB.getSheet(isheet).setValue(col, 3, "Asset Type", boldTopStyle);
			reportWB.getSheet(isheet).setValue(col, 4, wgi3.getLand().getResourceSubType().getResourcetypename(),
					textStyle);

			/* Now work through Communities and Wealthgroups for this Land RST */

			System.out.println("unique community count = " + uniqueCommunity.size());

			for (WGI wgi2 : uniqueCommunity) {

				System.out.println("this community  = " + wgi2.getSite().getLocationdistrict() + " "
						+ wgi2.getSite().getSubdistrict());

				orderedWealthgroups2 = wgi2.getCommunity().getWealthgroup().stream()
						.filter(p -> p.getCommunity().getCommunityid() == wgi2.getCommunity().getCommunityid())
						.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

				Iterator<WealthGroup> owgiter = orderedWealthgroups2.iterator();

				int wgcount = 0;
				while (owgiter.hasNext()) {
					WealthGroup owgit = owgiter.next();
					List<WealthGroupInterview> wealthGroupInterview = owgit.getWealthGroupInterview();
					for (int i = 0; i < wealthGroupInterview.size(); i++) {
						for (AssetLand assetLand : wealthGroupInterview.get(i).getAssetLand()) {
							System.out.println("test assetLand " + assetLand.getResourceSubType().getResourcetypename()
									+ " " + wgi3.getResourceSubType().getResourcetypename());

							if (assetLand.getResourceSubType() == wgi3.getResourceSubType()) {
								System.out.println(
										"match - add to total " + wgi3.getResourceSubType().getResourcetypename() + " "
												+ owgit.getWgnameeng() + " " + assetLand.getNumberOfUnits());
								totalLand += assetLand.getNumberOfUnits();
								
							}

						}

						reportWB.getSheet(isheet).setValue(col, datarow, totalLand, textStyle);

						Double at1 = averageTotal.get(wgcount);
						at1 += totalLand;
						System.out.println("avgtot i = " + at1 + " " + wgcount);

						averageTotal.set(wgcount, at1); // keep running total for average calc
						System.out.println("average total = " + averageTotal.get(i));

						wgcount++;

						totalLand = 0.0;
						i++;
						datarow++;
					}

				}

			}

			// print averages

			System.out.println("uc size = "+uniqueCommunity.size());
			for (int j = 0; j < 3; j++) {
				System.out.println("at get i "+averageTotal.get(j)+" "+j);
				System.out.println("avg = " +averageTotal.get(j) / uniqueCommunity.size());
				reportWB.getSheet(isheet).setValue(col, avgrow + j, averageTotal.get(j) / uniqueCommunity.size(),
						textStyle);

			}

			datarow = 10;
			col++;
			averageTotal.set(0, 0.0);
			averageTotal.set(1, 0.0);
			averageTotal.set(2, 0.0);

		}
		/***************************************************************************************************************************/

		/***************************************************************************************************************************/

	}

	/******************************************************************************************************************************************/

	private void populateFirstThreeColumns(int isheet, int startRow) {
		int row = startRow + 6;
		int wgrow;
		int averagesRow = startRow + 2;
		int countWGs = 0;

		/* Print Communities */
		String comm;

		reportWB.getSheet(isheet).setValue(1, startRow, "LZ", boldTopStyle);
		reportWB.getSheet(isheet).setValue(2, startRow, "Community", boldTopStyle);
		reportWB.getSheet(isheet).setValue(3, startRow, "Wealthgroup", boldTopStyle);

		// Place Averages Here
		reportWB.getSheet(isheet).setValue(2, averagesRow, "Averages", boldTopStyle);

		reportWB.getSheet(isheet).setValue(3, averagesRow, "Poor", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 1, "Middle", textStyle);
		reportWB.getSheet(isheet).setValue(3, averagesRow + 2, "Better Off", textStyle);

		reportWB.getSheet(isheet).setValue(1, row, livelihoodZone.getLzname(), textStyle);

		System.out.println("In pop first three cols, nos of communities =  " + uniqueCommunity.size());

		for (WGI wgi2 : uniqueCommunity) {
			comm = wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict();
			reportWB.getSheet(isheet).setValue(2, row, wgi2.getCommunity().getSite().getLocationdistrict() + " "
					+ wgi2.getCommunity().getSite().getSubdistrict(), textStyle);

			/* Print Wealthgroups within Community */
			/* Order by WG Order */

			wgi2.getCommunity().getWealthgroup();

			String communityID = wgi2.getCommunity().getCommunityid();
			orderedWealthgroups = wgi2.getCommunity().getWealthgroup().stream()
					.filter(p -> p.getCommunity().getCommunityid() == communityID)
					.sorted(Comparator.comparing(WealthGroup::getWgorder)).collect(Collectors.toList());

			wgrow = row;
			System.out.println("no of wgs  = " + orderedWealthgroups.size());
			for (WealthGroup wealthGroup : orderedWealthgroups) {

				for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {

					if (wealthGroupInterview.getStatus() == Status.Validated) {
						reportWB.getSheet(isheet).setValue(3, wgrow, wealthGroup.getWgnameeng(), textStyle);
						wgrow++;
					}
				}

				if (!isDisplayWealthgroupDone) // store list of displayed WGs - only do once
				{

					for (WealthGroupInterview wealthGroupInterview : wealthGroup.getWealthGroupInterview()) {

						if (wealthGroupInterview.getStatus() == Status.Validated) {
							displayWealthgroup.add(wealthGroup);

							System.out.println("set display wg " + wealthGroup.getWgnameeng());
							countWGs++;
						}
					}
				}
			}
			System.out.println("done populate displayWGs size = " + displayWealthgroup.size());
			isDisplayWealthgroupDone = true;

			row = wgrow;
		}

		// Display WG details

		for (int l = 0; l < displayWealthgroup.size(); l++) {
			System.out.println("display wg = " + displayWealthgroup.get(l).getWealthGroupInterview().size());

		}

	}

	/******************************************************************************************************************************************/

	private void createLivestockAssetreport(int isheet, Report report) {
		int row = 5;
		int col = 3;
		Double hhSOLC = 0.0;
		Map<ResourceSubType, Double> lsTot = new HashMap<>();

		System.out.println("In SOL Report 228 - Livestock Assets");

		ArrayList<WGISub> wgil = new ArrayList<>();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20);
		errno = 2281;

		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, 4, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 4, "Quantile %", textStyle);
		} else {
			reportWB.getSheet(isheet).setValue(1, 4, "Household Number", textStyle);
		}

		errno = 2282;
		/* need to create matrix of hh id against land assets */

		/* How many land assets for this set of filtered households */

		errno = 2283;

		// Populate hhLand array for matrix
		for (WGI wgi3 : uniqueWealthgroupInterview) {
			// for (AssetLiveStock assetLS :
			// wgi3.getWealthgroupInterview().getAssetLiveStock()) {
			// WGISub wgiLS = new WGISub();
//
//				wgiLS.setAssetLivestock(assetLS);
//				wgiLS.setWgiid(wgi3.getWgiNumber());
//				wgiLS.setAssetRST(assetLS.getResourceSubType());
//				wgiLS.setAssetValue(assetLS.getNumberOwnedAtStart());
//				wgiLS.setAssetName(assetLS.getResourceSubType().getResourcetypename());
//
//				wgiLS.setWgiDI(wgi3.getWgiDI());
//
//				wgil.add(wgiLS);
//
//				if (lsTot.containsKey(assetLS.getResourceSubType())) {
//					Double total = lsTot.get(assetLS.getResourceSubType());
//					total += assetLS.getNumberOwnedAtStart();
//					lsTot.remove(assetLS.getResourceSubType());
//					lsTot.put(assetLS.getResourceSubType(), total);
//
//				} else // create new
//				{
//					lsTot.put(assetLS.getResourceSubType(), assetLS.getNumberOwnedAtStart());
//				}
//
//			}
		}
		/*
		 * for (HHSub ls : hhl) { System.out.println("ls" +
		 * ls.getAssetRST().getResourcetypename());
		 * 
		 * }
		 * 
		 * List<HHSub> uniqueLS = hhl.stream().filter(distinctByKey(l ->
		 * l.getAssetRST())).collect(Collectors.toList());
		 * 
		 * // Populate the relevant column value for the RST for (HHSub ls : uniqueLS) {
		 * 
		 * ls.setColumn(col);
		 * 
		 * Iterator<HHSub> hhlIterator = hhl.iterator(); while (hhlIterator.hasNext()) {
		 * 
		 * HHSub currentHHLS = hhlIterator.next(); if (currentHHLS.getAssetRST() ==
		 * ls.getAssetRST()) { currentHHLS.setColumn(ls.getColumn()); } }
		 * 
		 * col++;
		 * 
		 * }
		 * 
		 * errno = 2284;
		 * 
		 * for (HHSub hhl2 : uniqueLS) {
		 * 
		 * // Heading reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 1,
		 * "Asset Category", textStyle);
		 * reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 2, "Livestock",
		 * textStyle); reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 3,
		 * "Asset Type", textStyle);
		 * reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 4,
		 * hhl2.getAssetRST().getResourcetypename(), textStyle);
		 * 
		 * // HH data
		 * 
		 * }
		 * 
		 * errno = 2285; // sort into DI order hhl = (ArrayList<HHSub>)
		 * hhl.stream().sorted(Comparator.comparing(HHSub::getHhDI)).collect(Collectors.
		 * toList()); int i; int currentHHid = 0; errno = 2286; for (HHSub hhl3 : hhl) {
		 * i = 0; if (currentHHid != hhl3.getHhid() && currentHHid != 0) { row++; } if
		 * (!isQuantile) {
		 * 
		 * currentHHid = hhl3.getHhid(); reportWB.getSheet(isheet).setValue(1, row,
		 * hhl3.getHhid(), textStyle);
		 * reportWB.getSheet(isheet).setColumnWidths(hhl3.getColumn(), 20);
		 * reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row,
		 * fillDouble(hhl3.getAssetValue()), textStyle); } }
		 */
		errno = 2287;
		/*
		 * if (isQuantile) { System.out.println("number quant = " + numberOfQuantiles);
		 * 
		 * Comparator<HHSub> sortByQuantSeq = Comparator.comparing(HHSub::getQuantSeq);
		 * Comparator<HHSub> sortByRST = Comparator.comparing(HHSub::getAssetName);
		 * 
		 * System.out.println("done sort 1");
		 * 
		 * for (HHSub hhsr : hhl) System.out.println("fdilter params = " +
		 * hhsr.getAssetName() + " " + hhsr.getQuantSeq());
		 * 
		 * List<HHSub> hhllist =
		 * hhl.stream().sorted(sortByQuantSeq.thenComparing(sortByRST))
		 * .collect(Collectors.toList());
		 * 
		 * System.out.println("done sort 2");
		 * 
		 * // print Quant name, seq/% row = 5; errno = 2288; for (Quantile qqpr :
		 * quantiles) { reportWB.getSheet(isheet).setValue(1, row, qqpr.getName(),
		 * textStyle); reportWB.getSheet(isheet).setValue(2, row, qqpr.getPercentage(),
		 * textStyle); row++; } errno = 2289;
		 * 
		 * row = 5;
		 */
		// for (HHSub hh : hhllist) {
		// System.out.println("hhllist = " + hh.getQuantSeq() + " " +
		// hh.getAssetRST().getResourcetypename() + " "
		// + hh.getColumn());
		// }

		// Quantile check with many RSTs
		/*
		 * errno = 22810;
		 * 
		 * for (Quantile q : quantiles) {
		 * 
		 * List<HHSub> q1 = hhllist.stream().filter(p -> p.quantSeq == q.getSequence())
		 * .collect(Collectors.toList());
		 * 
		 * for (i = 0; i < uniqueLS.size(); i++) { ResourceSubType rst =
		 * uniqueLS.get(i).assetRST; List<HHSub> q2 = q1.stream().filter(p ->
		 * p.getAssetRST() == rst).collect(Collectors.toList());
		 * 
		 * for (HHSub hhs : q2) {
		 * 
		 * Map<ResourceSubType, Double> collect = q2.stream().collect(Collectors
		 * .groupingBy(HHSub::getAssetRST,
		 * Collectors.averagingDouble(HHSub::getAssetValue)));
		 * 
		 * // System.out.println("q2 = " + hhs.getQuantSeq() + " " + " " + //
		 * hhs.getAssetName() + " " // + hhs.getAssetValue() +
		 * " double from group by = " + // collect.get(hhs.getAssetRST())); Double val =
		 * fillDouble(collect.get(hhs.getAssetRST()));
		 * reportWB.getSheet(isheet).setColumnWidths(hhs.getColumn(), 20);
		 * reportWB.getSheet(isheet).setValue(hhs.getColumn(), hhs.getQuantSeq() + 5,
		 * val, textStyle);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * errno = 22811;
		 * 
		 * }
		 * 
		 * errno = 22812;
		 */
	}

	/******************************************************************************************************************************************/

	private void createPopulationreport(int isheet, Report report) {
		/*
		 * int row = 1; System.out.println("Population Report");
		 * 
		 * int ageMaleGroup[] = new int[20]; int ageFemaleGroup[] = new int[20]; int
		 * numMales = 0; int numFemales = 0;
		 * 
		 * for (HH hh2 : uniqueHousehold) { for (HouseholdMember hhm :
		 * hh2.getHousehold().getHouseholdMember()) {
		 * 
		 * if (hhm.getGender().equals(Sex.Male)) { ageMaleGroup[(int)
		 * Math.ceil(hhm.getAge() / 5)]++; hh2.setNumMales(++numMales);
		 * 
		 * } else if (hhm.getGender().equals(Sex.Female)) { ageFemaleGroup[(int)
		 * Math.ceil(hhm.getAge() / 5)]++; hh2.setNumFemales(++numFemales); } } numMales
		 * = 0; numFemales = 0; }
		 * 
		 * if (!isQuantile) { reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20);
		 * reportWB.getSheet(isheet).setValue(1, row, "Age Range", textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, "Total Males", textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, "Total Females", textStyle);
		 * reportWB.getSheet(isheet).setValue(1, row + 1, "0 - 4", textStyle);
		 * 
		 * int lower = 5; int upper = 9; row = 3; // Print 5 - 9, 10 - 14 groups for
		 * (int i = 2; i < 21; i++) { reportWB.getSheet(isheet).setValue(1, row, lower +
		 * " - " + upper, textStyle); lower += 5; upper += 5; row++; }
		 * 
		 * int i = 0; row = 2; for (int j : ageMaleGroup) {
		 * reportWB.getSheet(isheet).setValue(2, row, j, textStyle); row++; } row = 2;
		 * for (int j : ageFemaleGroup) { reportWB.getSheet(isheet).setValue(3, row, j,
		 * textStyle); row++; } } else { // Quantile
		 * 
		 * orderedQuantSeq =
		 * uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
		 * .collect(Collectors.toList());
		 * 
		 * Map<Integer, Double> quantAvgMales = orderedQuantSeq.stream().collect(
		 * Collectors.groupingBy(HH::getQuantSeq, TreeMap::new,
		 * Collectors.averagingDouble(HH::getNumMales))); Map<Integer, Double>
		 * quantAvgFemales = orderedQuantSeq.stream().collect(Collectors
		 * .groupingBy(HH::getQuantSeq, TreeMap::new,
		 * Collectors.averagingDouble(HH::getNumFemales)));
		 * 
		 * int numberOfObs = 21 + 1; // age groups - add 1 for approximation calc
		 * 
		 * reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20, 20);
		 * reportWB.getSheet(isheet).setValue(1, 1, "Quantile", textStyle);
		 * reportWB.getSheet(isheet).setValue(2, 1, "Quantile %", textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, "Avg Male", textStyle);
		 * reportWB.getSheet(isheet).setValue(4, row, "Avg Female", textStyle);
		 * 
		 * row = 2;
		 * 
		 * for (Quantile q : quantiles) {
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, q.getName(), textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, q.getPercentage(), textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row,
		 * fillDouble(quantAvgMales.get(q.getSequence())), textStyle);
		 * reportWB.getSheet(isheet).setValue(4, row,
		 * fillDouble(quantAvgFemales.get(q.getSequence())), textStyle); row++;
		 * 
		 * }
		 * 
		 * }
		 */
	}

	/******************************************************************************************************************************************/

	private void createAdultEquivalentreport(int isheet, Report report) {
		/*
		 * int row = 1;
		 * 
		 * reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);
		 * 
		 * if (isQuantile) {
		 * 
		 * reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 30);
		 * 
		 * orderedQuantSeq =
		 * uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
		 * .collect(Collectors.toList());
		 * 
		 * quantAvg = orderedQuantSeq.stream().collect(
		 * Collectors.groupingBy(HH::getQuantSeq, TreeMap::new,
		 * Collectors.averagingDouble(HH::getHhAE)));
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
		 * reportWB.getSheet(isheet).setValue(3, row, "Adult Daily Equivalent KCal",
		 * textStyle); row++;
		 * 
		 * for (Quantile quantile : quantiles) { reportWB.getSheet(isheet).setValue(1,
		 * row, quantile.getName(), textStyle); reportWB.getSheet(isheet).setValue(2,
		 * row, quantile.getPercentage(), textStyle); //
		 * reportWB.getSheet(isheet).setValue(3, row, quantile.getSequence(), //
		 * textStyle); reportWB.getSheet(isheet).setValue(3, row,
		 * fillDouble(quantAvg.get(quantile.getSequence())), textStyle); row++; }
		 * 
		 * } else { reportWB.getSheet(isheet).setColumnWidths(1, 20, 30);
		 * System.out.println("DI non quantile "); reportWB.getSheet(isheet).setValue(1,
		 * row, "Household Number", textStyle); reportWB.getSheet(isheet).setValue(2,
		 * row, "Adult Daily Equivalent KCal", textStyle); row++; for (HH hh2 :
		 * uniqueHousehold) {
		 * 
		 * reportWB.getSheet(isheet).setValue(1, row, hh2.hhNumber, textStyle);
		 * reportWB.getSheet(isheet).setValue(2, row, hh2.hhAE, textStyle); row++; } }
		 */
	}

	/******************************************************************************************************************************************/

	// handle if Synonym is used and return base Kcal value
	private int findRSTKcal(ResourceSubType rst) {

		if (rst.getResourcesubtypesynonym() != null) {
			System.out.println("its a synonym");
			try {
				System.out.println("syn kcal = " + rst.getResourcesubtypesynonym().getResourcesubtypekcal());
				return (rst.getResourcesubtypesynonym().getResourcesubtypekcal());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				addMessage("Cannot get Synonym KCal value");
				return (0);
			}

		}
		System.out.println("its not a synonym");
		return (rst.getResourcesubtypekcal());
	}

	/******************************************************************************************************************************************/

	private Double calcWFIncome(WGI wgi2, String type) {
		Double wfTot = 0.0;

		/* What about food payments? */

		// for (WildFood wf : wgi2.getWealthgroupInterview().getWildFood()) {

		// if (type == "cash") {
		// wfTot += wf.getUnitsSold() * wf.getPricePerUnit();
		// } else if (type == "food") {
		// // wfTot += wf.getUnitsConsumed() *
		// // wf.getResourceSubType().getResourcesubtypekcal();
		// wfTot += wf.getUnitsConsumed() * findRSTKcal(wf.getResourceSubType());
		// }
		// }

		return wfTot;
	}

	/******************************************************************************************************************************************/

	private Double calcTransIncome(WGI wgi2, String type) {
		Double trTot = 0.0;

		/* Handle transfer types food/cash/other */

		// for (Transfer tr : wgi2.getWealthgroupInterview().getTransfer()) {
		// if (type == "cash") {
		// if (tr.getTransferType().equals(TransferType.Food)) {
		// trTot += tr.getUnitsSold() * tr.getPricePerUnit() * tr.getPeopleReceiving() *
		// tr.getTimesReceived();
		// } else // Cash or Other
		// {
		// trTot += tr.getPeopleReceiving() * tr.getTimesReceived() *
		// tr.getCashTransferAmount();
		// }
		// } else if (type == "food" && tr.getTransferType().equals(TransferType.Food)
		// && tr.getFoodResourceSubType() != null) {
		// trTot += tr.getUnitsConsumed() * findRSTKcal(tr.getFoodResourceSubType()) *
		// tr.getPeopleReceiving()
		// * tr.getTimesReceived();
//
//			}
//
//		}
		return trTot;
	}

	/******************************************************************************************************************************************/

	private Double calcLSIncome(WGI wgi2, String type) { // LSS and LSP sales
		Double lsTot = 0.0;
		// if (type == "cash") {
		// for (LivestockSales ls : wgi2.getWealthgroupInterview().getLivestockSales())
		// {
		// lsTot += ls.getUnitsSold() * ls.getPricePerUnit();
		// }
//
//			for (LivestockProducts lsp : wgi2.getWealthgroupInterview().getLivestockProducts()) {
//				lsTot += lsp.getUnitsSold() * lsp.getPricePerUnit();
//			}
//		} else if (type == "food") {
//			for (LivestockProducts lsp : wgi2.getWealthgroupInterview().getLivestockProducts()) {
//				lsTot += lsp.getUnitsConsumed() * findRSTKcal(lsp.getResourceSubType());
//			}
//		}
//
		return lsTot;

	}

	/******************************************************************************************************************************************/

	private Double calcEmpIncome(WGI wgi2, String type) {
		Double empTot = 0.0;

		/* What about food payments? for cash */
		// if (wgi2.getWealthgroupInterview().getEmployment().size() == 0) {
//
//			return (0.0);
//		}
//		try {
//			System.out.println("type = " + type);
//
//			for (Employment emp : wgi2.getWealthgroupInterview().getEmployment()) {
//				if (type == "cash") {
//
//					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * emp.getCashPaymentAmount();
//
//				} else if (type == "food" && emp.getFoodResourceSubType() != null) {
//
//					empTot += emp.getPeopleCount() * emp.getUnitsWorked() * findRSTKcal(emp.getFoodResourceSubType());
//				} else
//					empTot = 0.0;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			errno = 991;
//		}

		return empTot;

	}

	/******************************************************************************************************************************************/
	private Double calcCropIncome(WGI wgi2, String type) {

		Double cropTot = 0.0;

		System.out.println("in calcCropIncome type =" + type);
		// for (Crop crop : wgi2.getWealthgroupInterview().getCrop()) {
		// if (type == "cash") {
//
//				cropTot += crop.getUnitsSold() * crop.getPricePerUnit();
//				System.out.println("in crop cash tot");
//			} else if (type == "food") {
//				System.out.println("get rst syn kcal");
//				cropTot += crop.getUnitsConsumed() * findRSTKcal(crop.getResourceSubType());
//				System.out.println(
//						"crop new calc tot  = " + crop.getUnitsConsumed() * findRSTKcal(crop.getResourceSubType()));
//				System.out.println("crop total = " + cropTot);
//				System.out.println("rst = " + crop.getCropType());
//				System.out.println("rst consumed = " + crop.getUnitsConsumed());
//				System.out.println("rst name = " + crop.getResourceSubType().getResourcetypename());
//				System.out.println("rst KCAL = " + findRSTKcal(crop.getResourceSubType()));
//			}
//		}

		return cropTot;
	}

	/******************************************************************************************************************************************/

	private Double calcHhmSolc(WGI wgi3, StdOfLivingElement stdL) {
		/*
		 * int lowerAgeSOL = 0; int upperAgeSOL = 0; Gender genderSOL = null;
		 * 
		 * lowerAgeSOL = stdL.getAgeRangeLower(); upperAgeSOL = stdL.getAgeRangeUpper();
		 * genderSOL = stdL.getGender();
		 * 
		 * errno = 1201; for (HouseholdMember hhm :
		 * hh3.getHousehold().getHouseholdMember()) { if (genderSOL.equals(Gender.Both)
		 * && (hhm.getAge() >= lowerAgeSOL) && (hhm.getAge() <= upperAgeSOL)) { return
		 * (stdL.getCost() * stdL.getAmount()); } errno = 1202; if
		 * (genderSOL.equals(Gender.Male) && hhm.getGender().equals(Sex.Male) &&
		 * (hhm.getAge() >= lowerAgeSOL) && (hhm.getAge() <= upperAgeSOL)) { return
		 * (stdL.getCost() * stdL.getAmount());
		 * 
		 * } errno = 1203; if (genderSOL.equals(Gender.Female) &&
		 * hhm.getGender().equals(Sex.Female) && (hhm.getAge() >= lowerAgeSOL) &&
		 * (hhm.getAge() <= upperAgeSOL)) { return (stdL.getCost() * stdL.getAmount());
		 * 
		 * } errno = 1204; } errno = 1205;
		 */
		return (0.0);
	}

	/******************************************************************************************************************************************/

	private Double wealthgroupInterviewDI(WealthGroupInterview wealthGroupInterview) {

		/*
		 * Disposable Income (DI) = Total Income (TI) - Cost of covering Shortfall (SF)
		 * in Required Calories (RC) from Own Production (OP).
		 * 
		 * TI (Total Income) = Sum (Units Sold * Price per Unit) for all Crops, Wild
		 * Foods and Livestock Products
		 * 
		 * Sum (Units Worked No. of People Working Cash Payment) for all Employments Sum
		 * (Transfer Amount No. of People Receiving No. of Times Received) for all Cash
		 * Transfers
		 * 
		 * OP = Sum (Units Consumed * KCal per Unit) for all Crops, Wild Foods and
		 * Livestock Products
		 * 
		 * Sum (KCal per Unit of Food Type * No. of Units Paid) for all Employment paid
		 * in Food Sum (KCal per Unit of Food Type Transferred * No. of Units Consumed)
		 * for all Food Transfers
		 * 
		 * Required KCalaries = 2100 x 365 No. of People in Household (that was from
		 * OHEA - can be more accurate in OIHM
		 * 
		 * 
		 * 
		 */

		Double cropTI = 0.0;
		Double wildfoodsTI = 0.0;
		Double lspTI = 0.0;
		Double employmentTI = 0.0;
		Double transfersTI = 0.0;

		Double cropOP = 0.0;
		Double wildfoodsOP = 0.0;
		Double lspOP = 0.0;
		Double employmentOP = 0.0;
		Double transfersOP = 0.0;

		int requiredCalories = 0;

		System.out.println("In HH DI calc ");

		List<WGI> thisHH = wgi.stream().filter(d -> d.wealthgroupInterview == wealthGroupInterview)
				.collect(Collectors.toList());

		List<WGI> cropList = thisHH.stream().filter(d -> d.type == "Crop").collect(Collectors.toList());

		List<WGI> wildfoodList = thisHH.stream().filter(d -> d.type == "Wildfood").collect(Collectors.toList());

		List<WGI> livestockproductList = thisHH.stream().filter(d -> d.type == "LivestockProduct")
				.collect(Collectors.toList());

		List<WGI> employmentList = thisHH.stream().filter(d -> d.type == "Employment").collect(Collectors.toList());

		List<WGI> transferList = thisHH.stream().filter(d -> d.type == "Transfer").collect(Collectors.toList());

		try {
			if (cropList.size() > 0) {

				for (WGI icrop : cropList) {

					// Crop crop = icrop.getCrop();

					cropTI += (icrop.getCrop().getUnitsSold().doubleValue()
							* icrop.getCrop().getPricePerUnit().doubleValue());

					cropOP += (icrop.getCrop().getUnitsConsumed().doubleValue()
							* Double.valueOf(icrop.getCrop().getResourceSubType().getResourcesubtypekcal()));

				}
			}

			if (wildfoodList.size() > 0) {
				for (WGI iwildfood : wildfoodList) {

					wildfoodsTI += (iwildfood.getWildfood().getUnitsSold().doubleValue()
							* iwildfood.getWildfood().getPricePerUnit().doubleValue());

					wildfoodsOP += (iwildfood.getWildfood().getUnitsConsumed().doubleValue()
							* Double.valueOf(iwildfood.getWildfood().getResourceSubType().getResourcesubtypekcal()));
				}
			}
			if (livestockproductList.size() > 0) {
				for (WGI ilivestockproduct : livestockproductList) {

					lspTI += (ilivestockproduct.getLivestockproducts().getUnitsSold()
							* ilivestockproduct.getLivestockproducts().getPricePerUnit());
					lspOP += (ilivestockproduct.getLivestockproducts().getUnitsConsumed().doubleValue()
							* Double.valueOf(ilivestockproduct.getLivestockproducts().getResourceSubType()
									.getResourcesubtypekcal()));
				}
			}
			if (employmentList.size() > 0) {
				for (WGI iemployment : employmentList) {

					employmentTI += (iemployment.getEmployment().getUnitsWorked()
							* iemployment.getEmployment().getCashPaymentAmount()
							* iemployment.getEmployment().getPeopleCount());

					/* handle null resource type */

					int empresourcekcal = 0;
					ResourceSubType resourcesubtypekcal = iemployment.getEmployment().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						empresourcekcal = 0;
					else
						empresourcekcal = iemployment.getEmployment().getFoodResourceSubType().getResourcesubtypekcal();

					employmentOP += iemployment.getEmployment().getUnitsWorked() * empresourcekcal;
				}
			}
			if (transferList.size() > 0) {
				for (WGI itransfer : transferList) {

					if (itransfer.getTransfer().getTransferType().toString().equals("Cash"))
						transfersTI += (itransfer.getTransfer().getCashTransferAmount()
								* itransfer.getTransfer().getPeopleReceiving()
								* itransfer.getTransfer().getTimesReceived());
					System.out.println("in DI transfer calc " + transfersTI);
					Double transresourcekcal = 0.0;
					ResourceSubType resourcesubtypekcal = itransfer.getTransfer().getFoodResourceSubType();
					if (resourcesubtypekcal == null)
						transresourcekcal = 0.0;
					else
						transresourcekcal = (double) itransfer.getTransfer().getFoodResourceSubType()
								.getResourcesubtypekcal();

					if (itransfer.getTransfer().getTransferType().toString().equals("Food"))
						transfersOP += (transresourcekcal * itransfer.getTransfer().getUnitsConsumed());
				}
			}
			System.out.println("done DI transfer calc " + transfersTI);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			addError("Error in DI Calculation " + e);
		}

		// FIX for members
		// requiredCalories = household.getHouseholdMember().size() * RC; // Unique
		// Households after filter

		int age;
		Sex gender;
		int monthsAway = 0;
		int energyNeed = 0;

		WHOEnergyRequirements whoEnergy;
		return 0.0;

		// TODO
	}
	/*
	 * for (HouseholdMember hm : household.getHouseholdMember()) { age =
	 * hm.getAge(); gender = hm.getGender(); monthsAway = hm.getMonthsAway();
	 * whoEnergy = WHOEnergyRequirements.findByAge(age);
	 * 
	 * if (gender == Sex.Female) { energyNeed = whoEnergy.getFemale(); } else if
	 * (gender == Sex.Male) { energyNeed = whoEnergy.getMale(); }
	 * 
	 * requiredCalories += energyNeed * 365 * (12 - monthsAway) / 12;
	 * 
	 * }
	 * 
	 * System.out.println("requiredCalories = " + requiredCalories);
	 * 
	 * Double totalIncome = cropTI + wildfoodsTI + lspTI + employmentTI +
	 * transfersTI;
	 * 
	 * System.out.println("cropTI = " + cropTI); System.out.println("wfTI = " +
	 * wildfoodsTI); System.out.println("lspTI = " + lspTI);
	 * System.out.println("empTI = " + employmentTI);
	 * System.out.println("transfTI = " + transfersTI);
	 * 
	 * Double output = cropOP + wildfoodsOP + lspOP + employmentOP + transfersOP;
	 * 
	 * Double shortFall = requiredCalories - output;
	 * 
	 * System.out.println("totalIncome = " + totalIncome);
	 * System.out.println("output = " + output);
	 * 
	 * // Now it gets more complex , but not difficult
	 * 
	 * // Diet // Diet Value = Sum (KCal per KG * Percentage of the Food type in
	 * default diet
	 * 
	 * Double dietValue = 0.0;
	 * 
	 * for (DefaultDietItem defaultDietItem : defaultDietItems) { dietValue +=
	 * (defaultDietItem.getResourcesubtype().getResourcesubtypekcal()
	 * defaultDietItem.getPercentage() / 100); }
	 * 
	 * // Diet Amount Purchased DA = Shortfall / Diet Value in KGs Double
	 * dietAmountPurchased = 0.0;
	 * 
	 * dietAmountPurchased = shortFall / dietValue;
	 * 
	 * // Cost of Shortfall = Unit Price * Diet Amount Purchased // How many KGs in
	 * % of diet is needed // i.e. 20% of KGs in Diet item 1 +80% diet item 2
	 * 
	 * Double costOfShortfall = 0.0;
	 * 
	 * for (DefaultDietItem defaultDietItem : defaultDietItems) { costOfShortfall +=
	 * ((dietAmountPurchased * defaultDietItem.getPercentage() / 100)
	 * defaultDietItem.getUnitPrice().doubleValue()); }
	 * 
	 * // Disposable Income = Total Income - Cost of Shortfall Double
	 * disposableIncome = 0.0;
	 * 
	 * disposableIncome = totalIncome - costOfShortfall;
	 * 
	 * return Double.parseDouble(df2.format(disposableIncome));
	 * 
	 * }
	 * 
	 * /
	 ******************************************************************************************************************************************/

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

		System.out.println("In Create Header Page 000");

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
		if (customReportSpec.getReport().size() > 0)
			reportList = (List<Report>) customReportSpec.getReport();
		else
			reportList = XPersistence.getManager().createQuery("from Report").getResultList();

		reportList.sort(Comparator.comparingInt(Report::getCode));

		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyleLeft);
			sheet[i - 3] = reportWB.addSheet(report.getName());

			setSheetStyle(sheet[i - 3]);

			i++;
		}

		i = STARTROW;

		int col = 3;
		// Selected HH
		errno = 1101;
		String siteName;
		String wgName;

		if (isSelectedSites) {
			sheet[0].setValue(col, i, "Selected Communities in Report = " + sites.size(), boldTopStyle);
			// sheet[0].setValue(col + 1, i, "Wealthgroups in Report = ", boldTopStyle);
			i++;

			for (Site site : sites) {
				siteName = site.getLocationdistrict() + " " + site.getSubdistrict();
				sheet[0].setValue(col, i, siteName, textStyle);
				// for (Community community2 : site.getCommunity()) { // WGs not needed on
				// header page
				// for (WealthGroup wealthGroup : community2.getWealthgroup()) {
				// wgName = wealthGroup.getWgnameeng() + " " + wealthGroup.getWgnamelocal();
				// sheet[0].setValue(col + 1, i, wgName, textStyle);
				// i++;
				// }
				// }

				i++;

			}
		}

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

	private void setStyles() {
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldLStyle = reportWB.addStyle(TEXT).setBold().setAlign(LEFT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleLeft = reportWB.addStyle(TEXT).setAlign(LEFT);
		textStyleBlue = reportWB.addStyle(TEXT).setAlign(LEFT).setTextColor(BLUE);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(LEFT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT)
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setCellColor(BLUE);
		f1Style = reportWB.addStyle("0.0");
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

	/*
	 * Using generic would be better instead of each asset class. Needs a revisit
	 * sometime
	 */
	public static class WGI {

		private Project project;
		private LivelihoodZone livelihoodZone;
		private Site site;
		private Community community;
		private WealthGroupInterview wealthgroupInterview;
		private WealthGroup wealthgroup;
		private int wgiNumber;
		private Double wgiDI;
		private Double wgiAE;
		private Double wgiSOLC;
		private Collection<Category> category;
		private ResourceType resourceType;
		private ResourceSubType resourceSubType;
		private ConfigAnswer answer;
		private Boolean delete;
		private String type;
		private AssetLand land;
		private AssetFoodStock foodstock;
		private AssetCash cash;
		private AssetLiveStock livestock;
		private AssetTradeable tradeable;
		private AssetTree tree;
		private Crop crop;
		private Employment employment;
		private Inputs inputs;
		private LivestockProducts livestockproducts;
		private LivestockSales livestocksales;
		private Transfer transfer;
		private WildFood wildfood;
		private Quantile quantile;
		private Double avgDI;
		private int quantSeq;
		private Double cropIncome;
		private Double empIncome;
		private Double lsIncome;
		private Double transIncome;
		private Double wfIncome;

		public WealthGroupInterview getWealthgroupInterview() {
			return wealthgroupInterview;
		}

		public void setWealthgroupInterview(WealthGroupInterview wealthgroupInterview) {
			this.wealthgroupInterview = wealthgroupInterview;
		}

		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}

		public LivelihoodZone getLivelihoodZone() {
			return livelihoodZone;
		}

		public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
			this.livelihoodZone = livelihoodZone;
		}

		public WealthGroup getWealthgroup() {
			return wealthgroup;
		}

		public void setWealthgroup(WealthGroup wealthgroup) {
			this.wealthgroup = wealthgroup;
		}

		public Community getCommunity() {
			return community;
		}

		public void setCommunity(Community community) {
			this.community = community;
		}

		public Site getSite() {
			return site;
		}

		public void setSite(Site site) {
			this.site = site;
		}

		private int numMales;
		private int numFemales;

		public int getNumMales() {
			return numMales;
		}

		public void setNumMales(int numMales) {
			this.numMales = numMales;
		}

		public int getNumFemales() {
			return numFemales;
		}

		public void setNumFemales(int numFemales) {
			this.numFemales = numFemales;
		}

		public Double getEmpIncome() {
			return empIncome;
		}

		public void setEmpIncome(Double empIncome) {
			this.empIncome = empIncome;
		}

		public Double getLsIncome() {
			return lsIncome;
		}

		public void setLsIncome(Double lsIncome) {
			this.lsIncome = lsIncome;
		}

		public Double getTransIncome() {
			return transIncome;
		}

		public void setTransIncome(Double transIncome) {
			this.transIncome = transIncome;
		}

		public Double getWfIncome() {
			return wfIncome;
		}

		public void setWfIncome(Double wfIncome) {
			this.wfIncome = wfIncome;
		}

		public Double getCropIncome() {
			return cropIncome;
		}

		public void setCropIncome(Double cropIncome) {
			this.cropIncome = cropIncome;
		}

		public int getQuantSeq() {
			return quantSeq;
		}

		public void setQuantSeq(int quantSeq) {
			this.quantSeq = quantSeq;
		}

		public Double getAvgDI() {
			return avgDI;
		}

		public void setAvgDI(Double avgDI) {
			this.avgDI = avgDI;
		}

		public Quantile getQuantile() {
			return quantile;
		}

		public void setQuantile(Quantile quantile) {
			this.quantile = quantile;
		}

		public int getWgiNumber() {
			return wgiNumber;
		}

		public void setWgiNumber(int wgiNumber) {
			this.wgiNumber = wgiNumber;
		}

		public Double getWgiDI() {
			return wgiDI;
		}

		public void setWgiDI(Double wgiDI) {
			this.wgiDI = wgiDI;
		}

		public Double getWgiAE() {
			return wgiAE;
		}

		public void setWgiAE(Double wgiAE) {
			this.wgiAE = wgiAE;
		}

		public Double getWgiSOLC() {
			return wgiSOLC;
		}

		public void setWgiSOLC(Double wgiSOLC) {
			this.wgiSOLC = wgiSOLC;
		}

		public AssetFoodStock getFoodstock() {
			return foodstock;
		}

		public void setFoodstock(AssetFoodStock foodstock) {
			this.foodstock = foodstock;
		}

		public AssetCash getCash() {
			return cash;
		}

		public void setCash(AssetCash cash) {
			this.cash = cash;
		}

		public AssetLiveStock getLivestock() {
			return livestock;
		}

		public void setLivestock(AssetLiveStock livestock) {
			this.livestock = livestock;
		}

		public AssetTradeable getTradeable() {
			return tradeable;
		}

		public void setTradeable(AssetTradeable tradeable) {
			this.tradeable = tradeable;
		}

		public AssetTree getTree() {
			return tree;
		}

		public void setTree(AssetTree tree) {
			this.tree = tree;
		}

		public Crop getCrop() {
			return crop;
		}

		public void setCrop(Crop crop) {
			this.crop = crop;
		}

		public Employment getEmployment() {
			return employment;
		}

		public void setEmployment(Employment employment) {
			this.employment = employment;
		}

		public Inputs getInputs() {
			return inputs;
		}

		public void setInputs(Inputs inputs) {
			this.inputs = inputs;
		}

		public LivestockProducts getLivestockproducts() {
			return livestockproducts;
		}

		public void setLivestockproducts(LivestockProducts livestockproducts) {
			this.livestockproducts = livestockproducts;
		}

		public LivestockSales getLivestocksales() {
			return livestocksales;
		}

		public void setLivestocksales(LivestockSales livestocksales) {
			this.livestocksales = livestocksales;
		}

		public Transfer getTransfer() {
			return transfer;
		}

		public void setTransfer(Transfer transfer) {
			this.transfer = transfer;
		}

		public WildFood getWildfood() {
			return wildfood;
		}

		public void setWildfood(WildFood wildfood) {
			this.wildfood = wildfood;
		}

		public Collection<Category> getCategory() {
			return category;
		}

		public void setCategory(Collection<Category> category) {
			this.category = category;
		}

		public ResourceType getResourceType() {
			return resourceType;
		}

		public void setResourceType(ResourceType resourceType) {
			this.resourceType = resourceType;
		}

		public ResourceSubType getResourceSubType() {
			return resourceSubType;
		}

		public void setResourceSubType(ResourceSubType resourceSubType) {
			this.resourceSubType = resourceSubType;
		}

		public ConfigAnswer getAnswer() {
			return answer;
		}

		public void setAnswer(ConfigAnswer answer) {
			this.answer = answer;
		}

		public Boolean getDelete() {
			return delete;
		}

		public void setDelete(Boolean delete) {
			this.delete = delete;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public AssetLand getLand() {
			return land;
		}

		public void setLand(AssetLand land) {
			this.land = land;
		}

	}

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