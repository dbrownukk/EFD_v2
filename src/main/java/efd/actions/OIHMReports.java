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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.openxava.actions.IForwardAction;
import org.openxava.actions.TabBaseAction;
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
import efd.model.ConfigAnswer;
import efd.model.Crop;
import efd.model.CustomReportSpec;
import efd.model.DefaultDietItem;
import efd.model.Employment;
import efd.model.Household;
import efd.model.HouseholdMember;
import efd.model.HouseholdMember.Sex;
import efd.model.Inputs;
import efd.model.LivelihoodZone;
import efd.model.LivestockProducts;
import efd.model.LivestockSales;
import efd.model.MCCWFoodSource;
import efd.model.MicroNutrient;
import efd.model.MicroNutrientLevel;
import efd.model.ModellingScenario;
import efd.model.NutrientCount;
import efd.model.Project.Area;
import efd.model.Quantile;
import efd.model.Report;
import efd.model.ResourceSubType;
import efd.model.ResourceType;
import efd.model.StdOfLivingElement;
import efd.model.StdOfLivingElement.StdLevel;
import efd.model.Study;
import efd.model.Transfer;
import efd.model.WHOEnergyRequirements;
import efd.model.WealthGroupInterview.Status;
import efd.model.WildFood;
import efd.utils.HH;

public class OIHMReports extends TabBaseAction implements IForwardAction, JxlsConstants {

	static final int NUMBER_OF_REPORTS = 15;
	static final String FOOD = "food";
	static final String CASH = "cash";
	static Double RC = 2200.0 * 365; // NOTE that this is from OHEA. In OIHM we can be more accurate as we know age
	// and sex of HH Members
	private static DecimalFormat df2 = new DecimalFormat("#.00");
	List<HH> uniqueHousehold;
	// private CustomReportSpec customReportSpec = null;
	List<Quantile> quantiles;
	int numberOfQuantiles = 0;
	JxlsSheet[] sheet = new JxlsSheet[NUMBER_OF_REPORTS];
	JxlsWorkbook reportWB;
	ArrayList<HH> hh = new ArrayList<>();
	ArrayList<HH> hhSelected = new ArrayList<>();
	ArrayList<QuantHousehold> quanthh = new ArrayList<>();
	List<MicroNutrient> nutrients = XPersistence.getManager().createQuery("from MicroNutrient").getResultList();
	ArrayList<NutrientCount> totalHHNutrientCount = new ArrayList<NutrientCount>();
	List<HH> orderedQuantSeq = null;
	Map<Integer, Double> quantAvg = null;
	JxlsStyle boldRStyle = null;
	JxlsStyle boldTopStyle = null;
	JxlsStyle borderStyle = null;
	JxlsStyle textStyle = null;
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
	Boolean isSelectedHouseholds = false;
	String currency2;
	Boolean isCustomFilter = false;
	private Study study = null;
	private List<Report> reportList;
	private List<Household> selectedHouseholds = new ArrayList<Household>();
	private List<DefaultDietItem> defaultDietItems; // At Study not Household level
	private String forwardURI = null;
	private ModellingScenario modellingScenario=null;

	/******************************************************************************************************************************************/

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/******************************************************************************************************************************************/
	@Override
	public void execute() throws Exception {

		System.out.println("In Run OIHM Reports ");

		String specID = getView().getValueString("customReportSpec.id");
		if (specID.equals("")) {
			addError("Choose a Report Spec");
			return;
		}

		Object studyId;
		try {
			studyId = getPreviousView().getValue("id"); // if from Study mod
		} catch (EmptyStackException e1) {
			// TODO Auto-generated catch block
			studyId = getView().getValue("studyToSelect.id"); // if from menu
		}

		if (studyId == null) {
			addError("Save Study before running reports");
			closeDialog();
			return;
		}

		Tab targetTab = getView().getSubview("study.household").getCollectionTab();

		Map[] selectedOnes = targetTab.getSelectedKeys();

		Boolean isValid = false; // Has at least one Valid HH been selected
		if (selectedOnes.length > 0) {
			isSelectedHouseholds = true; // One or more HH selected in dialog
			for (int i = 0; i < selectedOnes.length; i++) {
				Map<?, ?> key = selectedOnes[i];

				Map<?, ?> membersNames = getView().getSubview("study.household").getMembersNames();

				String subKey = key.toString().substring(4, 36);

				// Map househ = MapFacade.getValues("Household", sub, membersNames);

				Household singleHHSelected = XPersistence.getManager().find(Household.class, subKey);
				if (singleHHSelected.getStatus() == Status.Validated) {
					isValid = true;
				}

				HH e = new HH();
				e.setHousehold(singleHHSelected);
				hhSelected.add(e);

				selectedHouseholds.add(singleHHSelected);
			}

		} else {
			addError("No Validated Households selected in this Study");

			return;
		}

		study = XPersistence.getManager().find(Study.class, studyId);
		CustomReportSpec customReportSpec = XPersistence.getManager().find(CustomReportSpec.class, specID);

		/*
		 * #535 If custom report spec has category etc. then use all households
		 * regardless of selection if there are valid HH
		 */

		if (!customReportSpec.getCategory().isEmpty() || !customReportSpec.getQuantile().isEmpty()
				|| !customReportSpec.getResourceSubType().isEmpty() || !customReportSpec.getResourceType().isEmpty()
				|| !customReportSpec.getConfigAnswer().isEmpty()) {

			isCustomFilter = true;

		}

		if (isValid == false) {

			addError("No Validated Households selected in this Study 1");

			return;
		}

		defaultDietItems = (List<DefaultDietItem>) study.getDefaultDietItem();

		nutrients.sort(Comparator.comparing(MicroNutrient::getName));

		/* Populate WHO table */

		/* Only create reports if there are Validated Households */

		List<Household> households = XPersistence.getManager()
				.createQuery("from Household where study_id = :study and status = :status ")
				.setParameter("study", study.getId()).setParameter("status", Status.Validated).getResultList();

		/******************************************************************************************************************************************/
		/* Pre Report Run Validations */

		uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber()))
				.sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());

		if (households.size() == 0) {

			addError("No Validated Households in this Study");
			closeDialog();
			return;
		}

		int ddiTotPercent = 0;

		for (DefaultDietItem defaultDietItem : defaultDietItems) {
			ddiTotPercent += defaultDietItem.getPercentage();
		}
		if (ddiTotPercent != 100) {
			addError("Default Diet Total Percentage for this Study is not 100%");
			closeDialog();
			return;
		}

		// Quantiles
		if (customReportSpec.getQuantile().size() > 0 && customReportSpec.getTotalQuantilePercentage() != 100) {
			addError("Quantile for this Report Spec is set but does not total 100%");
			closeDialog();

			return;
		} else if (customReportSpec.getTotalQuantilePercentage() == 100) {
			isQuantile = true;
			quantiles = (List<Quantile>) customReportSpec.getQuantile();

			quantiles = quantiles.stream().sorted(Comparator.comparing(Quantile::getSequence))
					.collect(Collectors.toList());
			// PUT Back 31/10 DRB
			uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber()))
					.sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());

			numberOfQuantiles = customReportSpec.getQuantile().size();
		}

		if (isQuantile && households.size() < 20)
			addWarning("Quantiles being used on Study with less than 20 Households");

		errno = 50;

		// Populate HH array hh - use dialog selected list if entered, otherwise use all

		populateHHArray(selectedHouseholds);

		errno = 51;
		// Filter according to Catalog/RT/RST/HH
		filterHH(customReportSpec);

		errno = 52;
		// Calculate DI
		em("about to do calc DI unique hh size = " + hh.size());
		calculateDI(); // uses hh filtered array based on CRS definition
		// System.out.println("done calc DI");
		calculateAE(); // Calculate the Adult equivalent
		// System.out.println("done calc AE");
		if (uniqueHousehold.size() == 0) {
			addError("No Households meet criteria, change Report Spec");
			closeDialog();
			return;
		}

		errno = 54;
		// Run reports
		try {

			JxlsWorkbook report = createReport(customReportSpec);
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

		addMessage("Created OIHM Report for Study " + study.getStudyName() + " using Spec "
				+ customReportSpec.getSpecName());

	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

	private void calculateAE() {

		for (HH hh2 : uniqueHousehold) {

			hh2.setHhAE(householdAE(hh2.getHousehold()));

		}

	}

	/******************************************************************************************************************************************/

	private void calculateDI() {

		// Put back drb 31/10/19
		uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber())) // Cannot sort
				// by DI at this
				// point, but
				// need unique
				// list of HH
				.collect(Collectors.toList());

		// for (HH hh2 : hh) {

		for (HH hh2 : uniqueHousehold) {

			hh2.setHhDI(ModellingReports.householdDI(hh2.getHousehold(), false, defaultDietItems,modellingScenario));

		}
		uniqueHousehold = hh.stream().filter(distinctByKey(p -> p.getHousehold().getHouseholdNumber())) // Now sort
				.sorted(Comparator.comparing(HH::getHhDI)).collect(Collectors.toList());

		// If QUantile then need to calc which quantile each unique HH is in
		final int[] qpoint = new int[200];
		if (isQuantile) {
			em("in calc DI 444");
			// unique HH are in order of DI set in order of Disposable Income
			int numberInQuantileDataset = uniqueHousehold.size();

			// ith observation = q (n + 1)
			// q = quantile % /100
			// n = number of observations
			// https://www.statisticshowto.datasciencecentral.com/quantile-definition-find-easy-steps/

			boolean isFirstRun = true;
			int currentPercentage = 0;
			for (Quantile qq : quantiles) {

				Double qVal = (double) ((qq.getPercentage()) * (1 + numberInQuantileDataset));

				// qVal is the position at which the percentage is valid (round down to nearest
				// int)

				currentPercentage += qq.getPercentage(); // get current + previous quantile percentage
				qpoint[qq.getSequence()] += currentPercentage * (numberInQuantileDataset + 1) / 100;

				if (qq.getSequence() > 1) {

					isFirstRun = false;
				}

				int i = 1;
				for (HH unhh : uniqueHousehold) {
					if (isFirstRun) {

						if (i <= qpoint[qq.getSequence()]) {

							QuantHousehold qqhh = new QuantHousehold();
							qqhh.setHousehold(unhh.getHousehold());
							qqhh.setQuantile(qq);
							quanthh.add(qqhh);
							unhh.setQuantSeq(qq.getSequence());
						}
					} else if (!isFirstRun) {

						if (i <= qpoint[qq.getSequence()] && i > qpoint[qq.getSequence() - 1]) {

							QuantHousehold qqhh = new QuantHousehold();
							qqhh.setHousehold(unhh.getHousehold());
							qqhh.setQuantile(qq);
							quanthh.add(qqhh);
							unhh.setQuantSeq(qq.getSequence());

						}

					}
					i++;
				}

			}
		}

	}

	/******************************************************************************************************************************************/
	private void filterHH(CustomReportSpec customReportSpec) {
		// Filter out if not in Category, not in RT or not in RT from hh populate array
		// Filter out reportspecuse non included HH if exist and for same Study

		/*
		 * Remove RSU from CRS if (customReportSpec.getReportSpecUse().size() > 0) //
		 * Apply Report Spec Usage Filter - HH must be Validated {
		 *
		 * for (ReportSpecUse reportSpecUse : customReportSpec.getReportSpecUse()) {
		 *
		 * for (Household household : reportSpecUse.getHousehold()) {
		 *
		 * for (HH hh2 : hh) {
		 *
		 * if (hh2.getHousehold() != household) {
		 * System.out.println("a RSU to delete "); hh2.setDelete(true);
		 *
		 * // Only if this rsu hh is for current study is this rsu used
		 *
		 * } else if (household.getStudy() == study) {
		 * System.out.println("a  RSU to save "); hh2.setDelete(false); break; } }
		 *
		 * } } }
		 */

		/* if custom filter then set everything to delete = true */
		/* if no custom filter set everything to delete = false */

		if (isCustomFilter) {
			System.out.println("custom filter set all to be deleted");
			hh.stream().forEach(p -> {
				p.setDelete(true);
				filterOutRST(p, true);
			});
		} else {
			System.out.println("NO custom filter set all to be not deleted");
			hh.stream().forEach(p -> {
				p.setDelete(false);
				filterOutRST(p, false);
			});
		}

		if (customReportSpec.getCategory().size() > 0) // Apply Category Filter
		{

			List<HH> hhCAT = hh.stream().filter(p -> p.getCategory() != null).collect(Collectors.toList());

			for (Category category : customReportSpec.getCategory()) {
				for (HH hh2 : hhCAT) {

					if (category.getResourceSubType().stream().anyMatch(p -> p == hh2.getResourceSubType())) {

						hh2.setDelete(false);
						setAssetFilteredOutFalse(hh2);
						filterOutRST(hh2, false);

					} else
						filterOutRST(hh2, true);
				}

			}

		}

		if (customReportSpec.getResourceType().size() > 0) // Apply RST Filter

		{

			List<HH> hhRT = hh.stream().filter(p -> p.getResourceType() != null).collect(Collectors.toList());

			for (HH hh2 : hhRT) {
				if (BooleanUtils.isFalse(hh2.getDelete())) {
					/* keep as do not delete regardless as set above */
					filterOutRST(hh2, false);
					continue;
				}

				for (ResourceType resourceType : customReportSpec.getResourceType()) {

					if (resourceType == hh2.getResourceType()) {

						hh2.setDelete(false);
						// setAssetFilteredOutFalse(hh2);
						filterOutRST(hh2, false);
					} else {
						// System.out.println("In RT rt and hh2 rt TRUE MATCH
						// "+resourceType.getResourcetypename()+"
						// "+hh2.getResourceType().getResourcetypename());
						filterOutRST(hh2, true);
					}

				}
			}

		}

		// System.out.println("After RT hh = " + hh.size());
		if (customReportSpec.getResourceSubType().size() > 0) // Apply RST Filter
		{

			List<HH> hhRST = hh.stream().filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

			for (HH hh2 : hhRST) {
				if (BooleanUtils.isFalse(hh2.getDelete())) {
					/* keep as do not delete regardless as set above */
					filterOutRST(hh2, false);
					continue;
				}
				for (ResourceSubType resourceSubType : customReportSpec.getResourceSubType()) {
					if (resourceSubType.getIdresourcesubtype() == hh2.getResourceSubType().getIdresourcesubtype()) {

						hh2.setDelete(false);
						// setAssetFilteredOutFalse(hh2);
						filterOutRST(hh2, false);
					} else {

						filterOutRST(hh2, true);
					}
				}

			}

		}

		if (customReportSpec.getConfigAnswer().size() > 0) // Apply Q and A Filter
		{

			for (HH hh2 : hh) {

				for (ConfigAnswer configAnswer : customReportSpec.getConfigAnswer())

					if (configAnswer != hh2.getAnswer()) {

						hh2.setDelete(true);
					} else {

						hh2.setDelete(false);
						break;
					}
			}

		}

		if (hh.stream().filter(p -> p.getDelete() == true && isSelectedHouseholds == true).findAny().isPresent()) {
			hh.removeIf(n -> n.getDelete() == true);
		}

	}

	/******************************************************************************************************************************************/
	/*
	 * Set isFilteredOut flag in Asset if not included - default is false in Assets
	 */
	private void setAssetFilteredOutFalse(HH hh2) {
		if (hh2.getCrop() != null)
			hh2.getCrop().setIsFilteredOut(false);

		if (hh2.getEmployment() != null)
			hh2.getEmployment().setIsFilteredOut(false);

		if (hh2.getLivestockproducts() != null)
			hh2.getLivestockproducts().setIsFilteredOut(false);

		if (hh2.getLivestocksales() != null)
			hh2.getLivestocksales().setIsFilteredOut(false);

		if (hh2.getTransfer() != null)
			hh2.getTransfer().setIsFilteredOut(false);

		if (hh2.getWildfood() != null)
			hh2.getWildfood().setIsFilteredOut(false);
	}

	/******************************************************************************************************************************************/
	/*
	 * Set isFilteredOut flag in Asset if not included - default is false in Assets
	 * - set to True here
	 */
	private void filterOutRST(HH hh2, boolean trueOrfalse) {

		// hh2.setDelete(trueOrfalse);
		/* Need to set household assets to filtered out before crop,emp,lsp,lss,tr,wf */

		List<Crop> cropSetFilteredOut = hh2.getHousehold().getCrop().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Crop> listIteratorcropList = cropSetFilteredOut.listIterator();
		while (listIteratorcropList.hasNext()) {
			listIteratorcropList.next().setIsFilteredOut(trueOrfalse);

		}

		List<WildFood> wfSetFilteredOut = hh2.getHousehold().getWildFood().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<WildFood> listIteratorwfList = wfSetFilteredOut.listIterator();
		while (listIteratorwfList.hasNext()) {
			listIteratorwfList.next().setIsFilteredOut(trueOrfalse);

		}

		List<Employment> empSetFilteredOut = hh2.getHousehold().getEmployment().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Employment> listIteratorempList = empSetFilteredOut.listIterator();
		while (listIteratorempList.hasNext()) {
			listIteratorempList.next().setIsFilteredOut(trueOrfalse);

		}
		// Food payment
		List<Employment> empFoodSetFilteredOut = hh2.getHousehold().getEmployment().stream()
				.filter(p -> p.getFoodResourceSubType() == hh2.getResourceSubType())
				.filter(p -> p.getResourceSubType() != null).collect(Collectors.toList());

		ListIterator<Employment> listIteratorempFoodList = empFoodSetFilteredOut.listIterator();
		while (listIteratorempFoodList.hasNext()) {
			listIteratorempFoodList.next().setIsFilteredOut(trueOrfalse);

		}

		List<LivestockProducts> lspSetFilteredOut = hh2.getHousehold().getLivestockProducts().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<LivestockProducts> listIteratorlspList = lspSetFilteredOut.listIterator();
		while (listIteratorlspList.hasNext()) {
			listIteratorlspList.next().setIsFilteredOut(trueOrfalse);

		}
		List<LivestockSales> lssSetFilteredOut = hh2.getHousehold().getLivestockSales().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<LivestockSales> listIteratorlssList = lssSetFilteredOut.listIterator();
		while (listIteratorlssList.hasNext()) {
			listIteratorlssList.next().setIsFilteredOut(trueOrfalse);

		}
		List<Transfer> trSetFilteredOut = hh2.getHousehold().getTransfer().stream()
				.filter(p -> p.getResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Transfer> listIteratortrList = trSetFilteredOut.listIterator();
		while (listIteratortrList.hasNext()) {
			listIteratortrList.next().setIsFilteredOut(trueOrfalse);

		}

		// Food transfer
		List<Transfer> trFoodSetFilteredOut = hh2.getHousehold().getTransfer().stream()
				.filter(p -> p.getFoodResourceSubType() == hh2.getResourceSubType()).collect(Collectors.toList());

		ListIterator<Transfer> listIteratortrFoodList = trFoodSetFilteredOut.listIterator();
		while (listIteratortrFoodList.hasNext()) {
			listIteratortrFoodList.next().setIsFilteredOut(trueOrfalse);

		}

	}

	/******************************************************************************************************************************************/
	private void populateHHArray(List<Household> households) {
		/*
		 * create array of all Validated households for this study
		 *
		 * they can then be filtered
		 *
		 */

		ConfigAnswer answer = null;

		for (Household household : households) {
			populateHHfromHousehold(household, answer);

			for (ConfigAnswer configAnswer : household.getConfigAnswer()) {
				addTohhArray(household, null, null, null, configAnswer, "Answer", null, null, null, null, null, null,
						null, null, null, null, null, null, null);

			}

		}

	}

	/******************************************************************************************************************************************/

	private void populateHHfromHousehold(Household household, ConfigAnswer answer) {

		for (AssetLand asset : household.getAssetLand()) {

			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory(); // List of Categories that
			// include this RST
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Land", asset, null, null, null,
					null, null, null, null, null, null, null, null, null);

		}
		for (AssetFoodStock asset : household.getAssetFoodStock()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Foodstock", null, asset, null,
					null, null, null, null, null, null, null, null, null, null);
		}
		for (AssetCash asset : household.getAssetCash()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Cash", null, null, asset, null,
					null, null, null, null, null, null, null, null, null);

		}
		for (AssetLiveStock asset : household.getAssetLiveStock()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Livestock", null, null, null,
					asset, null, null, null, null, null, null, null, null, null);
		}
		for (AssetTradeable asset : household.getAssetTradeable()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Tradeable", null, null, null,
					null, asset, null, null, null, null, null, null, null, null);
		}
		for (AssetTree asset : household.getAssetTree()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Tree", null, null, null, null,
					null, asset, null, null, null, null, null, null, null);
		}
		for (Crop asset : household.getCrop()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Crop", null, null, null, null,
					null, null, asset, null, null, null, null, null, null);
		}
		for (Employment asset : household.getEmployment()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Employment", null, null, null,
					null, null, null, null, asset, null, null, null, null, null);
		}
		for (Inputs asset : household.getInputs()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Input", null, null, null, null,
					null, null, null, null, asset, null, null, null, null);
		}
		for (LivestockProducts asset : household.getLivestockProducts()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "LivestockProduct", null, null,
					null, null, null, null, null, null, null, asset, null, null, null);
		}
		for (LivestockSales asset : household.getLivestockSales()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "LivestockSale", null, null, null,
					null, null, null, null, null, null, null, asset, null, null);
		}
		for (Transfer asset : household.getTransfer()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Transfer", null, null, null, null,
					null, null, null, null, null, null, null, asset, null);
		}
		for (WildFood asset : household.getWildFood()) {
			ResourceSubType resourceSubType = asset.getResourceSubType();
			Collection<Category> category = asset.getResourceSubType().getCategory();
			ResourceType resourceType = asset.getResourceSubType().getResourcetype();
			addTohhArray(household, category, resourceType, resourceSubType, answer, "Wildfood", null, null, null, null,
					null, null, null, null, null, null, null, null, asset);
		}

	}

	/******************************************************************************************************************************************/

	private void addTohhArray(Household household, Collection<Category> category, ResourceType resourceType,
			ResourceSubType resourceSubType, ConfigAnswer answer, String type, AssetLand land, AssetFoodStock foodstock,
			AssetCash cash, AssetLiveStock livestock, AssetTradeable tradeable, AssetTree tree, Crop crop,
			Employment employment, Inputs inputs, LivestockProducts livestockproducts, LivestockSales livestocksales,
			Transfer transfer, WildFood wildfood) {

		HH e = new HH();
		e.setHousehold(household);
		e.setHhNumber(household.getHouseholdNumber());
		e.setCategory(category);
		e.setResourceType(resourceType);
		e.setResourceSubType(resourceSubType);
		e.setAnswer(answer);
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
		e.setDelete(false);

		hh.add(e);

	}

	/******************************************************************************************************************************************/

	private JxlsWorkbook createReport(CustomReportSpec customReportSpec) throws Exception {
		System.out.println("In Run OIHM Reports create xls ");

		String filename = customReportSpec.getSpecName() + Calendar.getInstance().getTime();

		reportWB = new JxlsWorkbook(filename);
		setStyles();

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
			case 223:
				System.out.println("report 223");
				createDIreport(1, report);
				break;
			case 224:
				System.out.println("report 224");
				createDIAfterSOLreport(2, report);
				break;
			case 225:
				System.out.println("report 225");
				createIncomereport(3, report, "cash");
				break;
			case 226:
				System.out.println("report 226");
				createIncomereport(4, report, "food");
				break;

			case 227:
				System.out.println("report 227");
				createLandAssetreport(5, report);
				break;

			case 228:
				System.out.println("report 228");
				createLivestockAssetreport(6, report);
				break;

			case 236:
				System.out.println("report 236");
				createAdultEquivalentreport(7, report);
				break;

			case 237:
				System.out.println("report 237");
				createHHMemberreport(8, report);
				break;

			case 238:
				System.out.println("report 238");
				createPopulationreport(9, report);
				break;
			case 239:
				System.out.println("report 239");
				createNutrientreport(10, report);
				break;

			}
		}

		return reportWB;
	}

	/******************************************************************************************************************************************/

	private void createDIreport(int isheet, Report report) {

		int row = 1;

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		if (isQuantile) {

			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

			orderedQuantSeq = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
					.collect(Collectors.toList());

			quantAvg = orderedQuantSeq.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getHhDI)));

			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Disposable Income", textStyle);
			row++;

			for (Quantile quantile : quantiles) {
				Double avgDI = quantAvg.get(quantile.getSequence());
				avgDI = fillDouble(avgDI);
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				// reportWB.getSheet(isheet).setValue(3, row, quantile.getSequence(),
				// textStyle);
				reportWB.getSheet(isheet).setValue(3, row, avgDI, textStyle);
				row++;
			}

		} else {
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);

			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Disposable Income", textStyle);
			row++;
			for (HH hh2 : uniqueHousehold) {

				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.getHhDI(), textStyle);
				row++;
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
		Double hhSOLC = 0.0;

		// uniqeHousehold = number of households in array that are relevant

		for (HH hh3 : uniqueHousehold) {
			errno = 101;
			hhSOLC = 0.0;
			for (StdOfLivingElement stdOfLivingElement : hh3.getHousehold().getStudy().getStdOfLivingElement()) {
				errno = 102;

				if (stdOfLivingElement.getLevel().equals(StdLevel.Household)) {
					errno = 103;
					hhSOLC += (stdOfLivingElement.getCost() * stdOfLivingElement.getAmount());
				} else if (stdOfLivingElement.getLevel().equals(StdLevel.HouseholdMember)) {
					errno = 104;
					for (HouseholdMember householdMember : hh3.getHousehold().getHouseholdMember()) {

						hhSOLC += ModellingReports.calcHhmSolc(householdMember, stdOfLivingElement); // Moved to
						// ModellingReport
						// hhSOLC += calcHhmSolc(hh3, stdOfLivingElement);
						errno = 105;
					}

				}

			}
			errno = 106;
			hh3.setHhSOLC(hhSOLC);

		}

		if (isQuantile) {
			errno = 107;
			Double totSTOL = 0.0;

			List<HH> orderedQuantSOL = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
					.collect(Collectors.toList());

			Map<Integer, Double> quantAvgSOL = orderedQuantSOL.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getHhSOLC)));

			reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30, 30);

			List<HH> orderedHH = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getHhSOLC))
					.collect(Collectors.toList());

			for (HH hh5 : orderedHH) {
				totSTOL += hh5.getHhSOLC();
			}

			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Standard of Living Requirement", textStyle);
			reportWB.getSheet(isheet).setValue(4, row, "Disposable Income", textStyle);
			row++;

			for (Quantile quantile : quantiles) {
				Double avgDI = quantAvg.get(quantile.getSequence()); // gets avg DI
				avgDI = fillDouble(avgDI);
				Double avgSOL = quantAvgSOL.get(quantile.getSequence());
				avgSOL = fillDouble(avgSOL);

				Double quantileAmount = quantile.getPercentage() / 100.0 * totSTOL;
				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, avgSOL, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, avgDI, textStyle);
				row++;
			}
		} else {
			errno = 108;
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 30, 30);
			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Standard of Living Requirement", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Disposable Income", textStyle);
			row++;
			for (HH hh2 : uniqueHousehold) {

				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.getHhSOLC(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, hh2.getHhDI(), textStyle);

				row++;
			}
		}
		errno = 109;
	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

	private void createIncomereport(int isheet, Report report, String type) {
		int row = 1;
		// String type = "food" or "cash"
		errno = 2261;

		Double cropIncome = 0.0;
		Double empIncome = 0.0;
		Double lsIncome = 0.0;
		Double trIncome = 0.0;
		Double wfIncome = 0.0;
		int col = 0;

		String initType = StringUtils.capitalize(type);

		reportWB.getSheet(isheet).setColumnWidths(1, 25, 25, 25, 25, 25, 25, 25);
		errno = 2262;
		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, row, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Quantile %", textStyle);
		} else {

			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		}

		if (isQuantile)
			col = 3;
		else
			col = 2;

		List<HH> orderedQuantIncome = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
				.collect(Collectors.toList());

		reportWB.getSheet(isheet).setValue(col++, row, "Crop " + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Employment " + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Livestock " + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Transfer " + initType + " Income", textStyle);
		reportWB.getSheet(isheet).setValue(col++, row, "Wildfood " + initType + " Income", textStyle);
		errno = 2263;
		row = 2;

		/*
		 * if Quantile get Incomes and set income in hh array for use in average stream
		 * calculation using sequence group by
		 */

		if (isQuantile) {
			errno = 2264;
			for (HH hh3 : uniqueHousehold) {

				cropIncome = ModellingReports.calcCropIncome(hh3.getHousehold().getCrop(), type, false, false, false, modellingScenario);
				/* change to Modelling funcs drb 30/11/2020 */
				empIncome = ModellingReports.calcEmpIncome(hh3.getHousehold().getEmployment(), type, false, false,
						false,modellingScenario );

				// lsIncome = calcLSIncome(hh3, type);

				lsIncome = ModellingReports.calcLSP(hh3.getHousehold().getLivestockProducts(), type, false, false,
						false,modellingScenario);
				lsIncome += ModellingReports.calcLSS(hh3.getHousehold().getLivestockSales(), type, false,modellingScenario );

				// trIncome = calcTransIncome(hh3, type);
				trIncome = ModellingReports.calcTransIncome(hh3.getHousehold().getTransfer(), type, false, false,
						false,modellingScenario );
				wfIncome = ModellingReports.calcWFIncome(hh3.getHousehold().getWildFood(), type, false, false, false,modellingScenario );

				hh3.setCropIncome(cropIncome);
				hh3.setEmpIncome(empIncome);
				hh3.setLsIncome(lsIncome);
				hh3.setTransIncome(trIncome);
				hh3.setWfIncome(wfIncome);

			}

			Map<Integer, Double> quantAvgCrop = orderedQuantIncome.stream().collect(Collectors
					.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getCropIncome)));
			errno = 22647;

			Map<Integer, Double> quantAvgEmp = orderedQuantIncome.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getEmpIncome)));
			errno = 22648;
			Map<Integer, Double> quantAvgLS = orderedQuantIncome.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getLsIncome)));
			errno = 22649;
			Map<Integer, Double> quantAvgTrans = orderedQuantIncome.stream().collect(Collectors
					.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getTransIncome)));
			errno = 226410;
			Map<Integer, Double> quantAvgWF = orderedQuantIncome.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getWfIncome)));
			errno = 226411;

			Double avgCrop = 0.0;
			Double avgEmp = 0.0;
			Double avgLS = 0.0;
			Double avgTrans = 0.0;
			Double avgWF = 0.0;

			for (Quantile quantile : quantiles) {

				avgCrop = quantAvgCrop.get(quantile.getSequence());
				if (avgCrop == null)
					avgCrop = 0.0;

				avgEmp = quantAvgEmp.get(quantile.getSequence());
				if (avgEmp == null)
					avgEmp = 0.0;

				avgLS = quantAvgLS.get(quantile.getSequence());
				if (avgLS == null)
					avgLS = 0.0;

				avgTrans = quantAvgTrans.get(quantile.getSequence());
				if (avgTrans == null)
					avgTrans = 0.0;

				avgWF = quantAvgWF.get(quantile.getSequence());
				if (avgWF == null)
					avgWF = 0.0;

				avgCrop = Double.parseDouble(df2.format(avgCrop));
				avgEmp = Double.parseDouble(df2.format(avgEmp));
				avgLS = Double.parseDouble(df2.format(avgLS));
				avgTrans = Double.parseDouble(df2.format(avgTrans));
				avgWF = Double.parseDouble(df2.format(avgWF));

				reportWB.getSheet(isheet).setValue(1, row, quantile.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantile.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, avgCrop, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, avgEmp, textStyle);
				reportWB.getSheet(isheet).setValue(5, row, avgLS, textStyle);
				reportWB.getSheet(isheet).setValue(6, row, avgTrans, textStyle);
				reportWB.getSheet(isheet).setValue(7, row, avgWF, textStyle);
				row++;
			}
		} else {
			errno = 2265;
			for (HH hh2 : uniqueHousehold) {

				cropIncome = ModellingReports.calcCropIncome(hh2.getHousehold().getCrop(), type, false, false, false,modellingScenario );
				empIncome = ModellingReports.calcEmpIncome(hh2.getHousehold().getEmployment(), type, false, false,
						false,modellingScenario );

				// lsIncome = calcLSIncome(hh2, type);

				lsIncome = ModellingReports.calcLSP(hh2.getHousehold().getLivestockProducts(), type, false, false,
						false,modellingScenario );
				lsIncome += ModellingReports.calcLSS(hh2.getHousehold().getLivestockSales(), type, false,modellingScenario );

				trIncome = ModellingReports.calcTransIncome(hh2.getHousehold().getTransfer(), type, false, false,
						false,modellingScenario );
				wfIncome = ModellingReports.calcWFIncome(hh2.getHousehold().getWildFood(), type, false, false, false,modellingScenario );

				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, cropIncome, textStyle);
				reportWB.getSheet(isheet).setValue(3, row, empIncome, textStyle);
				reportWB.getSheet(isheet).setValue(4, row, lsIncome, textStyle);
				reportWB.getSheet(isheet).setValue(5, row, trIncome, textStyle);
				reportWB.getSheet(isheet).setValue(6, row, wfIncome, textStyle);
				row++;

			}
		}
		errno = 2266;

	}

	/**
	 * @param isheet
	 * @param report
	 */
	private void createLandAssetreport(int isheet, Report report) {
		int row = 5;
		int col = 3;
		Map<ResourceSubType, Double> landTot = new HashMap<>();

		ArrayList<HHSub> hhl = new ArrayList<>();

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20, 20, 20, 20, 20, 20, 20);
		errno = 2271;

		reportWB.getSheet(isheet).setValue(1, 2, "Unit of Measure", boldTopStyle);

		// get Default Area in use

		
		Area areaMeasurement = study.getProjectlz().getAreaMeasurement();

		if (areaMeasurement == null) {
			areaMeasurement = Area.Acre;
		}

		reportWB.getSheet(isheet).setValue(2, 2, areaMeasurement.toString(), textStyle);

		if (isQuantile) {
			reportWB.getSheet(isheet).setValue(1, 4, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 4, "Quantile %", textStyle);
		} else {
			reportWB.getSheet(isheet).setValue(1, 4, "Household Number", textStyle);
		}
		errno = 2272;
		/* need to create matrix of hh id against land assets */

		/* How many land assets for this set of filtered households */

		errno = 2273;

		// Populate hhLand array for matrix
		Double total = 0.0;
		for (HH hh3 : uniqueHousehold) {
			for (AssetLand assetLand : hh3.getHousehold().getAssetLand()) {
				HHSub hhLand = new HHSub();

				hhLand.setAssetLand(assetLand);
				hhLand.setHhid(hh3.getHhNumber());
				hhLand.setAssetRST(assetLand.getResourceSubType());
				hhLand.setAssetName(assetLand.getResourceSubType().getResourcetypename());
				hhLand.setAssetValue(assetLand.getNumberOfUnits());
				hhLand.setHhDI(hh3.getHhDI());
				hhLand.setQuantSeq(hh3.getQuantSeq());
				hhLand.setQuantile(hh3.getQuantile());
				hhl.add(hhLand);

				total = 0.0;

				if (landTot.containsKey(assetLand.getResourceSubType())) {
					total = landTot.get(assetLand.getResourceSubType());
					total += assetLand.getNumberOfUnits();
					landTot.remove(assetLand.getResourceSubType());
					landTot.put(assetLand.getResourceSubType(), total);

				} else // create new
				{
					landTot.put(assetLand.getResourceSubType(), assetLand.getNumberOfUnits());
				}
				// hhLand.setQuantSeq(hh3.getQuantSeq());
				// hhLand.setQuantile(hh3.getQuantile());

			}

		}

		// get distinct list of RST
		List<HHSub> uniqueLand = hhl.stream().filter(distinctByKey(l -> l.getAssetRST())).collect(Collectors.toList());

		// Populate the relevant column value for the RST
		for (HHSub land : uniqueLand) {

			land.setColumn(col);

			Iterator<HHSub> hhlIterator = hhl.iterator();
			while (hhlIterator.hasNext()) {

				HHSub currentHHLand = hhlIterator.next();
				if (currentHHLand.getAssetRST() == land.getAssetRST()) {
					currentHHLand.setColumn(land.getColumn());
				}
			}

			col++;
		}

		errno = 2274;

		for (HHSub hhl2 : uniqueLand) {

			// Heading
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 1, "Asset Category", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 2, "Land", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 3, "Asset Type", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 4, hhl2.getAssetRST().getResourcetypename(),
					textStyle);

		}
		// sort into DI order

		int i;
		hhl = (ArrayList<HHSub>) hhl.stream().sorted(Comparator.comparing(HHSub::getHhDI)).collect(Collectors.toList());

		int currentHHid = 0;
		for (HHSub hhl3 : hhl) {
			i = 0;
			if (currentHHid != hhl3.getHhid() && currentHHid != 0) {
				row++;
			}

			if (!isQuantile) {

				currentHHid = hhl3.getHhid();
				reportWB.getSheet(isheet).setValue(1, row, hhl3.getHhid(), textStyle);
				reportWB.getSheet(isheet).setColumnWidths(hhl3.getColumn(), 20);
				reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row, fillDouble(hhl3.getAssetValue()), textStyle);
			}
		}
		errno = 2275;
		// Quantiles are set

		if (isQuantile) {

			Comparator<HHSub> sortByQuantSeq = Comparator.comparing(HHSub::getQuantSeq);
			Comparator<HHSub> sortByRST = Comparator.comparing(HHSub::getAssetName);

			List<HHSub> hhllist = hhl.stream().sorted(sortByQuantSeq.thenComparing(sortByRST))
					.collect(Collectors.toList());

			// print Quant name, seq/%
			row = 5;
			errno = 2276;
			for (Quantile qqpr : quantiles) {
				reportWB.getSheet(isheet).setValue(1, row, qqpr.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, qqpr.getPercentage(), textStyle);

				row++;
			}
			errno = 2277;

			row = 5;

			// Quantile check with many RSTs
			errno = 2278;

			for (Quantile q : quantiles) {

				List<HHSub> q1 = hhllist.stream().filter(p -> p.quantSeq == q.getSequence())
						.collect(Collectors.toList());

				for (i = 0; i < uniqueLand.size(); i++) {
					ResourceSubType rst = uniqueLand.get(i).assetRST;
					List<HHSub> q2 = q1.stream().filter(p -> p.getAssetRST() == rst).collect(Collectors.toList());

					for (HHSub hhs : q2) {

						Map<ResourceSubType, Double> collect = q2.stream().collect(Collectors
								.groupingBy(HHSub::getAssetRST, Collectors.averagingDouble(HHSub::getAssetValue)));

						Double val = fillDouble(collect.get(hhs.getAssetRST()));
						reportWB.getSheet(isheet).setColumnWidths(hhs.getColumn(), 20);
						reportWB.getSheet(isheet).setValue(hhs.getColumn(), hhs.getQuantSeq() + 5, val, textStyle);

					}

				}

			}

			errno = 2279;

		}
	}

	/******************************************************************************************************************************************/

	private void createLivestockAssetreport(int isheet, Report report) {
		int row = 5;
		int col = 3;
		Double hhSOLC = 0.0;
		Map<ResourceSubType, Double> lsTot = new HashMap<>();

		ArrayList<HHSub> hhl = new ArrayList<>();

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
		for (HH hh3 : uniqueHousehold) {
			for (AssetLiveStock assetLS : hh3.getHousehold().getAssetLiveStock()) {
				HHSub hhLS = new HHSub();

				hhLS.setAssetLivestock(assetLS);
				hhLS.setHhid(hh3.getHhNumber());
				hhLS.setAssetRST(assetLS.getResourceSubType());
				hhLS.setAssetValue(assetLS.getNumberOwnedAtStart());
				hhLS.setAssetName(assetLS.getResourceSubType().getResourcetypename());

				hhLS.setHhDI(hh3.getHhDI());
				hhLS.setQuantSeq(hh3.getQuantSeq());
				hhLS.setQuantile(hh3.getQuantile());
				hhl.add(hhLS);

				if (lsTot.containsKey(assetLS.getResourceSubType())) {
					Double total = lsTot.get(assetLS.getResourceSubType());
					total += assetLS.getNumberOwnedAtStart();
					lsTot.remove(assetLS.getResourceSubType());
					lsTot.put(assetLS.getResourceSubType(), total);

				} else // create new
				{
					lsTot.put(assetLS.getResourceSubType(), assetLS.getNumberOwnedAtStart());
				}

			}
		}

		List<HHSub> uniqueLS = hhl.stream().filter(distinctByKey(l -> l.getAssetRST())).collect(Collectors.toList());

		// Populate the relevant column value for the RST
		for (HHSub ls : uniqueLS) {

			ls.setColumn(col);

			Iterator<HHSub> hhlIterator = hhl.iterator();
			while (hhlIterator.hasNext()) {

				HHSub currentHHLS = hhlIterator.next();
				if (currentHHLS.getAssetRST() == ls.getAssetRST()) {
					currentHHLS.setColumn(ls.getColumn());
				}
			}

			col++;

		}

		errno = 2284;

		for (HHSub hhl2 : uniqueLS) {

			// Heading
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 1, "Asset Category", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 2, "Livestock", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 3, "Asset Type", textStyle);
			reportWB.getSheet(isheet).setValue(hhl2.getColumn(), 4, hhl2.getAssetRST().getResourcetypename(),
					textStyle);

			// HH data

		}
		errno = 2285;
		// sort into DI order
		hhl = (ArrayList<HHSub>) hhl.stream().sorted(Comparator.comparing(HHSub::getHhDI)).collect(Collectors.toList());
		int i;
		int currentHHid = 0;
		errno = 2286;
		for (HHSub hhl3 : hhl) {
			i = 0;
			if (currentHHid != hhl3.getHhid() && currentHHid != 0) {
				row++;
			}
			if (!isQuantile) {

				currentHHid = hhl3.getHhid();
				reportWB.getSheet(isheet).setValue(1, row, hhl3.getHhid(), textStyle);
				reportWB.getSheet(isheet).setColumnWidths(hhl3.getColumn(), 20);
				reportWB.getSheet(isheet).setValue(hhl3.getColumn(), row, fillDouble(hhl3.getAssetValue()), textStyle);
			}
		}

		errno = 2287;
		if (isQuantile) {

			Comparator<HHSub> sortByQuantSeq = Comparator.comparing(HHSub::getQuantSeq);
			Comparator<HHSub> sortByRST = Comparator.comparing(HHSub::getAssetName);

			List<HHSub> hhllist = hhl.stream().sorted(sortByQuantSeq.thenComparing(sortByRST))
					.collect(Collectors.toList());

			// print Quant name, seq/%
			row = 5;
			errno = 2288;
			for (Quantile qqpr : quantiles) {
				reportWB.getSheet(isheet).setValue(1, row, qqpr.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, qqpr.getPercentage(), textStyle);
				row++;
			}
			errno = 2289;

			row = 5;

			// Quantile check with many RSTs
			errno = 22810;

			for (Quantile q : quantiles) {

				List<HHSub> q1 = hhllist.stream().filter(p -> p.quantSeq == q.getSequence())
						.collect(Collectors.toList());

				for (i = 0; i < uniqueLS.size(); i++) {
					ResourceSubType rst = uniqueLS.get(i).assetRST;
					List<HHSub> q2 = q1.stream().filter(p -> p.getAssetRST() == rst).collect(Collectors.toList());

					for (HHSub hhs : q2) {

						Map<ResourceSubType, Double> collect = q2.stream().collect(Collectors
								.groupingBy(HHSub::getAssetRST, Collectors.averagingDouble(HHSub::getAssetValue)));

						Double val = fillDouble(collect.get(hhs.getAssetRST()));
						reportWB.getSheet(isheet).setColumnWidths(hhs.getColumn(), 20);
						reportWB.getSheet(isheet).setValue(hhs.getColumn(), hhs.getQuantSeq() + 5, val, textStyle);

					}

				}

			}

			errno = 22811;

		}

		errno = 22812;

	}

	/******************************************************************************************************************************************/

	private void createHHMemberreport(int isheet, Report report) {
		int row = 1;
		int col = 10;
		errno = 2371;
		System.out.println("In HHMember report 237");
		reportWB.getSheet(isheet).setColumnWidths(1, 15, 10, 10, 10, 15, 10, 15, 15, 15, 15, 15, 15);
		reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
		reportWB.getSheet(isheet).setValue(2, row, "Member", textStyle);
		reportWB.getSheet(isheet).setValue(3, row, "Age", textStyle);
		reportWB.getSheet(isheet).setValue(4, row, "Year of Birth", textStyle);
		reportWB.getSheet(isheet).setValue(5, row, "Sex", textStyle);
		reportWB.getSheet(isheet).setValue(6, row, "Head of Household", textStyle);
		reportWB.getSheet(isheet).setValue(7, row, "Absent", textStyle);
		reportWB.getSheet(isheet).setValue(8, row, "Reason for Absence", textStyle);
		reportWB.getSheet(isheet).setValue(9, row, "Months Absent", textStyle);
		reportWB.getSheet(isheet).setValue(10, row, "Notes", textStyle);

		/* Need any extra questions/answers that are defined for this Study */
		/*
		 * Question header obtained from just one household as all questions same for
		 * hh/hhm in a study
		 */
		errno = 2372;

		Optional<HH> findFirstHH = uniqueHousehold.stream().findFirst();

		for (HouseholdMember member : findFirstHH.get().getHousehold().getHouseholdMember()) {

			for (ConfigAnswer answer : member.getConfigAnswer()) {

				String prompt = answer.getConfigQuestionUse().getConfigQuestion().getPrompt();

				reportWB.getSheet(isheet).setValue(col, row, prompt, textStyle);
				reportWB.getSheet(isheet).setColumnWidths(col, 30);
				col++;

			}
			break;
		}
		errno = 2373;

		for (HH hh2 : uniqueHousehold) {

			Collection<HouseholdMember> householdMember = hh2.getHousehold().getHouseholdMember();
			List<HouseholdMember> hhm = householdMember.stream()
					.sorted(Comparator.comparing(HouseholdMember::getHouseholdMemberNumber))
					.collect(Collectors.toList());
			for (HouseholdMember hh3 : hhm) {
				row++;
				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh3.getHouseholdMemberNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, hh3.getAge(), textStyle);
				reportWB.getSheet(isheet).setValue(4, row, hh3.getYearOfBirth(), textStyle);
				reportWB.getSheet(isheet).setValue(5, row, hh3.getGender(), textStyle);
				reportWB.getSheet(isheet).setValue(6, row, hh3.getHeadofHousehold(), textStyle);
				reportWB.getSheet(isheet).setValue(7, row, hh3.getAbsent(), textStyle);
				reportWB.getSheet(isheet).setValue(8, row, hh3.getReasonForAbsence(), textStyle);
				reportWB.getSheet(isheet).setValue(9, row, hh3.getMonthsAway(), textStyle);
				reportWB.getSheet(isheet).setValue(10, row, hh3.getNotes(), textStyle);

				col = 10;
				// Answers
				for (ConfigAnswer configAnswer : hh3.getConfigAnswer()) {
					reportWB.getSheet(isheet).setColumnWidths(col, 35);
					reportWB.getSheet(isheet).setValue(col, row, configAnswer.getAnswer(), textStyle);
					col++;
				}

			}

		}

		errno = 2374;

	}

	/******************************************************************************************************************************************/

	private void createPopulationreport(int isheet, Report report) {
		int row = 1;
		System.out.println("Population Report");

		int ageMaleGroup[] = new int[20];
		int ageFemaleGroup[] = new int[20];
		int numMales = 0;
		int numFemales = 0;

		for (HH hh2 : uniqueHousehold) {
			for (HouseholdMember hhm : hh2.getHousehold().getHouseholdMember()) {

				if (hhm.getGender().equals(Sex.Male)) {
					ageMaleGroup[(int) Math.ceil(hhm.getAge() / 5)]++;
					hh2.setNumMales(++numMales);

				} else if (hhm.getGender().equals(Sex.Female)) {
					ageFemaleGroup[(int) Math.ceil(hhm.getAge() / 5)]++;
					hh2.setNumFemales(++numFemales);
				}
			}
			numMales = 0;
			numFemales = 0;
		}

		if (!isQuantile) {
			reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20);
			reportWB.getSheet(isheet).setValue(1, row, "Age Range", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Total Males", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Total Females", textStyle);
			reportWB.getSheet(isheet).setValue(1, row + 1, "0 - 4", textStyle);

			int lower = 5;
			int upper = 9;
			row = 3;
			// Print 5 - 9, 10 - 14 groups
			for (int i = 2; i < 21; i++) {
				reportWB.getSheet(isheet).setValue(1, row, lower + " - " + upper, textStyle);
				lower += 5;
				upper += 5;
				row++;
			}

			int i = 0;
			row = 2;
			for (int j : ageMaleGroup) {
				reportWB.getSheet(isheet).setValue(2, row, j, textStyle);
				row++;
			}
			row = 2;
			for (int j : ageFemaleGroup) {
				reportWB.getSheet(isheet).setValue(3, row, j, textStyle);
				row++;
			}
		} else { // Quantile

			orderedQuantSeq = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
					.collect(Collectors.toList());

			Map<Integer, Double> quantAvgMales = orderedQuantSeq.stream().collect(
					Collectors.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getNumMales)));
			Map<Integer, Double> quantAvgFemales = orderedQuantSeq.stream().collect(Collectors
					.groupingBy(HH::getQuantSeq, TreeMap::new, Collectors.averagingDouble(HH::getNumFemales)));

			int numberOfObs = 21 + 1; // age groups - add 1 for approximation calc

			reportWB.getSheet(isheet).setColumnWidths(1, 10, 20, 20, 20);
			reportWB.getSheet(isheet).setValue(1, 1, "Quantile", textStyle);
			reportWB.getSheet(isheet).setValue(2, 1, "Quantile %", textStyle);
			reportWB.getSheet(isheet).setValue(3, row, "Avg Male", textStyle);
			reportWB.getSheet(isheet).setValue(4, row, "Avg Female", textStyle);

			row = 2;

			for (Quantile q : quantiles) {

				reportWB.getSheet(isheet).setValue(1, row, q.getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, q.getPercentage(), textStyle);
				reportWB.getSheet(isheet).setValue(3, row, fillDouble(quantAvgMales.get(q.getSequence())), textStyle);
				reportWB.getSheet(isheet).setValue(4, row, fillDouble(quantAvgFemales.get(q.getSequence())), textStyle);
				row++;

			}

		}

	}

	/******************************************************************************************************************************************/

	private void createAdultEquivalentreport(int isheet, Report report) {

		int row = 1;

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		if (isQuantile) {
			/*
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
			 */
		} else {
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 30);

			reportWB.getSheet(isheet).setValue(1, row, "Household Number", textStyle);
			reportWB.getSheet(isheet).setValue(2, row, "Adult Daily Equivalent KCal", textStyle);
			row++;
			for (HH hh2 : uniqueHousehold) {

				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, hh2.getHhAE(), textStyle);
				row++;
			}
		}
	}

	/******************************************************************************************************************************************/

	private void createNutrientreport(int isheet, Report report) {

		int row = 2;
		int col = 2;
		MCCWFoodSource thisMccwFoodSource;
		MicroNutrient microNutrient;

		reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);

		// hh is array of all data
		// need an array of valid HH now left
		// ordered by DI

		/*
		 * ARray is used to add each micronutrient in a food product to list for each HH
		 */

		if (isQuantile) {

			/* Headings */
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20, 20);
			reportWB.getSheet(isheet).setValue(1, 1, "Quantile", boldTopStyle);
			reportWB.getSheet(isheet).setValue(2, 1, "Quantile %", boldTopStyle);

			col = 3;
			for (int j = 0; j < nutrients.size(); j++) {

				microNutrient = nutrients.get(j);

				reportWB.getSheet(isheet).setColumnWidths(col, 20);
				reportWB.getSheet(isheet).setValue(col, 1, nutrients.get(j).getName(), boldTopStyle);

				col++;
			}

			/* Calculation */

			/* populate totalHHNutrientCount */
			totalHHNutrientCount = calcOIHMNutrient(uniqueHousehold);

			orderedQuantSeq = uniqueHousehold.stream().sorted(Comparator.comparing(HH::getQuantSeq))
					.collect(Collectors.toList());

			/* get list of HH in a Quant */
			row = 2;
			for (int k = 0; k < quantiles.size(); k++) {
				final int m = k + 1;
				List<Integer> listHH = orderedQuantSeq.stream().filter(p -> p.getQuantSeq() == m)
						.map(p -> p.getHhNumber()).collect(Collectors.toList());

				reportWB.getSheet(isheet).setValue(1, row, quantiles.get(k).getName(), textStyle);
				reportWB.getSheet(isheet).setValue(2, row, quantiles.get(k).getPercentage(), textStyle);

				/* Now have list of HH for this Quant */
				/* Now get average amount of micronutrient */
				col = 3;

				/* Get average HHM Count in this quantile */
				OptionalDouble averageHHNumberInThisQuantile = totalHHNutrientCount.stream()
						.filter(p -> listHH.contains(p.getHhNumber())).mapToDouble(p -> p.getAverageNumberInHousehold())
						.average();

				for (int j = 0; j < nutrients.size(); j++) {
					final int q = j;
					microNutrient = nutrients.get(j);

					OptionalDouble averageForNutrient = totalHHNutrientCount.stream()
							.filter(p -> listHH.contains(p.getHhNumber())).filter(p -> p.getMn() == nutrients.get(q))
							.mapToDouble(p -> p.getMnAmount()).average();

					double mnYearRDA = microNutrient.getRda() * averageHHNumberInThisQuantile.getAsDouble() * 365;

					reportWB.getSheet(isheet).setValue(col++, row, 100 * (averageForNutrient.orElse(0) / mnYearRDA),
							numberStyle);
				}
				row++;
			}

		} else {
			row = 2;
			reportWB.getSheet(isheet).setColumnWidths(1, 20, 20);

			reportWB.getSheet(isheet).setValue(1, 1, "Household Number", textStyle);

			for (HH hh2 : uniqueHousehold) {
				reportWB.getSheet(isheet).setValue(1, row, hh2.getHhNumber(), textStyle);
				row++;
			}

			/* populate totalHHNutrientCount */
			totalHHNutrientCount = calcOIHMNutrient(uniqueHousehold);

			col = 2;

			for (int j = 0; j < nutrients.size(); j++) {
				final int k = j;
				microNutrient = nutrients.get(j);
				row = 2;
				reportWB.getSheet(isheet).setColumnWidths(col, 20);
				reportWB.getSheet(isheet).setValue(col, 1, nutrients.get(j).getName(), boldTopStyle);

				for (HH hh3 : uniqueHousehold) {
					int hhmCount = hh3.getHousehold().getHHMCount();

					double mnYearRDA = microNutrient.getRda() * hhmCount * 365;

					mnYearRDA = OHEAReports.calcMNYearRDA(microNutrient, mnYearRDA); // Calc g,mg divisor

					double mnSum = totalHHNutrientCount.stream().filter(p -> p.getMn() == nutrients.get(k))
							.filter(p -> p.getHhNumber() == hh3.getHhNumber()).mapToDouble(p -> p.getMnAmount()).sum();

					


					Double mnSumPercent = 100 * (mnSum / mnYearRDA);
					if (mnSumPercent.isNaN())
						mnSumPercent = 0.0;


					reportWB.getSheet(isheet).setValue(col, row, mnSumPercent, numberStyle);


					row++;
				}
				col++;
			}

		}

	}

	/******************************************************************************************************************************************/
	/* populate array with hh and nutrient values - used when calculatng totals */
	private ArrayList<NutrientCount> calcOIHMNutrient(List<HH> thisUniqueHousehold) {

		ArrayList<NutrientCount> thisTotalHHNutrientCount = new ArrayList<NutrientCount>();

		MCCWFoodSource thisMccwFoodSource;
		for (HH hh2 : thisUniqueHousehold) {

			defaultDietItems = (List<DefaultDietItem>) hh2.getHousehold().getStudy().getDefaultDietItem();



			/* Calc DDI effect */

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



							NutrientCount thismn = new NutrientCount();
							thismn.setMn(mnl.getMicroNutrient());
							thismn.setMnAmount(mnLevel);

							thismn.setHhNumber(hh2.getHhNumber());
							// thismn.setWgOrder(k);

							thismn.setDDI(true);

							thisTotalHHNutrientCount.add(thismn);


					}

				}
			}



			if (hh2.getHousehold().getCrop().size() > 0) {

				for (Crop crop2 : hh2.getHousehold().getCrop()) {

					thisMccwFoodSource = OHEAReports.calcMccWincludeSyn(crop2.getResourceSubType());

					if (thisMccwFoodSource != null) {

						for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
							thisTotalHHNutrientCount.add(populateNutrientSource(hh2.getHhNumber(),
									hh2.getHousehold().getHHMCount(), crop2.getUnitsConsumed(), mn));
						}
					}
				}
			}
			if (hh2.getHousehold().getLivestockProducts().size() > 0) {
				errno = 45020;

				for (LivestockProducts lsp : hh2.getHousehold().getLivestockProducts()) {

					thisMccwFoodSource = OHEAReports.calcMccWincludeSyn(lsp.getResourceSubType());

					if (thisMccwFoodSource != null) {

						for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {

							thisTotalHHNutrientCount.add(populateNutrientSource(hh2.getHhNumber(),
									hh2.getHousehold().getHHMCount(), lsp.getUnitsConsumed(), mn));

						}
					}
				}
			}
			if (hh2.getHousehold().getTransfer().size() > 0) {
				errno = 45030;
				for (Transfer tr : hh2.getHousehold().getTransfer()) {

					thisMccwFoodSource = OHEAReports.calcMccWincludeSyn(tr.getResourceSubType());

					if (thisMccwFoodSource != null) {

						for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
							thisTotalHHNutrientCount.add(populateNutrientSource(hh2.getHhNumber(),
									hh2.getHousehold().getHHMCount(), tr.getUnitsConsumed(), mn));

						}
					}
				}
			}
			errno = 45040;
			if (hh2.getHousehold().getEmployment().size() > 0) {
				for (Employment emp : hh2.getHousehold().getEmployment()) {

					thisMccwFoodSource = OHEAReports.calcMccWincludeSyn(emp.getResourceSubType());

					if (thisMccwFoodSource != null) {

						for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
							thisTotalHHNutrientCount
									.add(populateNutrientSource(hh2.getHhNumber(), hh2.getHousehold().getHHMCount(),
											emp.getPeopleCount() * emp.getFoodPaymentUnitsPaidWork(), mn));

						}
					}
				}
			}
			errno = 45050;
			if (hh2.getHousehold().getWildFood().size() > 0) {
				for (WildFood wf : hh2.getHousehold().getWildFood()) {

					thisMccwFoodSource = OHEAReports.calcMccWincludeSyn(wf.getResourceSubType());

					if (thisMccwFoodSource != null) {

						for (MicroNutrientLevel mn : thisMccwFoodSource.getMicroNutrientLevel()) {
							thisTotalHHNutrientCount.add(populateNutrientSource(hh2.getHhNumber(),
									hh2.getHousehold().getHHMCount(), wf.getUnitsConsumed(), mn));

						}
					}
				}
			}

		}

		errno = 45052;
		return (thisTotalHHNutrientCount);
	}

	/******************************************************************************************************************************************/
	/* Populate nutrients array for this HH with nutrient count */
	private NutrientCount populateNutrientSource(int hhNumber, double avgInHH, double used, MicroNutrientLevel mn) {

		NutrientCount thismn = new NutrientCount();
		errno = 440010;
		if (NumberUtils.toDouble(mn.getMnLevel()) != 0.0) {

			thismn.setMn(mn.getMicroNutrient());

			double mnLevel = NumberUtils.toDouble(mn.getMnLevel());

			// thismn.setWgOrder(wgOrder);
			thismn.setHhNumber(hhNumber);
			thismn.setMnAmount(mnLevel * used * 10); // x10 as mnLevel is per 100g - convert to kg
			thismn.setAverageNumberInHousehold(avgInHH);
			thismn.setDDI(false);

			// totalWGNutrientCount.add(thismn);

		}
		return (thismn);
	}

	/******************************************************************************************************************************************/

	private void em(String message) {
		System.out.println(message);
		return;
	}

	/******************************************************************************************************************************************/

	private Double householdAE(Household household) {

		Double totAE = 0.0;

		int age = 0;
		Sex gender;

		for (HouseholdMember hhm : household.getHouseholdMember()) {
			age = hhm.getAge();
			gender = hhm.getGender();

			WHOEnergyRequirements whoEnergy = WHOEnergyRequirements.findByAge(age);

			if (gender == Sex.Female) {
				totAE += whoEnergy.getFemale();
			} else if (gender == Sex.Male) {
				totAE += whoEnergy.getMale();
			}

		}

		/* AE = TE / 2600 */

		totAE = totAE / 2600;

		return Double.parseDouble(df2.format(totAE));

	}

	/******************************************************************************************************************************************/

	private void createHeaderPage(CustomReportSpec customReportSpec) {
		final int STARTROW = 6;
		int i = STARTROW; // used for row number

		// June 25 - change to report details across page

		sheet[0] = reportWB.addSheet("Custom Report Spec");
		setSheetStyle(sheet[0]);
		sheet[0].setColumnWidths(1, 40, 50, 50, 50, 50, 50, 25, 25, 50, 50);

		sheet[0].setValue(1, 1, "Date:", textStyle);
		sheet[0].setValue(2, 1, new Date());
		sheet[0].setValue(1, 2, "Spec Name:", textStyle);
		sheet[0].setValue(2, 2, customReportSpec.getSpecName(), textStyle);
		sheet[0].setValue(1, 3, "Study:", textStyle);
		sheet[0].setValue(2, 3, study.getStudyName() + " " + study.getReferenceYear(), textStyle);
		sheet[0].setValue(2, STARTROW, "Reports", boldTopStyle);
		/* get list of reports and create tabbed bsheets for each */

		/* If no reports in custom report spec then use all reports */
		i++;

		/*
		 * #561 Always output every spreadsheet sheet. If not requested in custom report
		 * spec then leave blank
		 */
		// if (customReportSpec.getReport().size() > 0) {
		Set<Report> reportSet;
		reportSet = customReportSpec.getReport();
		reportList = reportSet.stream().collect(Collectors.toList());
		// } else {
		List<Report> fullReportList = XPersistence.getManager()
				.createQuery("from Report where method = 1 and code > 10").getResultList();
		// }
		fullReportList.sort(Comparator.comparing(Report::getCode));
		reportList.sort(Comparator.comparing(Report::getCode));
		for (Report report : reportList) {
			sheet[0].setValue(2, i, report.getName(), textStyle);
			i++;
		}
		i = STARTROW;
		for (Report report : fullReportList) {
			sheet[i - 3] = reportWB.addSheet(report.getName());
			setSheetStyle(sheet[i - 3]);
			i++;
		}

		i = STARTROW;

		int col = 3;
		// Selected HH
		errno = 1101;
		String hhName;
		if (isSelectedHouseholds) {
			sheet[0].setValue(col, i, "Selected Households in Report = " + hhSelected.size(), boldTopStyle);
			i++;
			for (HH hh2 : hhSelected) {
				if ((hhName = hh2.getHousehold().getHouseholdName()) == null)
					hhName = "-";
				sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyle);
				i++;
			}
		} else {
			sheet[0].setValue(col, i, "Report Spec Households in Report = " + uniqueHousehold.size(), boldTopStyle);
			i++;
			for (HH hh2 : uniqueHousehold) {
				if ((hhName = hh2.getHousehold().getHouseholdName()) == null)
					hhName = "-";
				sheet[0].setValue(col, i, hh2.getHousehold().getHouseholdNumber() + " - " + hhName, textStyle);
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

		i = STARTROW;
		col++;
		errno = 1105;

		sheet[0].setValue(col, i, "Quantiles Included", boldTopStyle);
		sheet[0].setValue(col + 1, i, "Quantile Percent", boldTopStyle);

		try {
			i++;
			for (Quantile q : quantiles) {

				sheet[0].setValue(col, i, q.getName(), textStyle);
				sheet[0].setValue(col + 1, i, q.getPercentage(), textStyle);
				i++;
			}
		} catch (Exception e) {

			System.out.println("No quantile");
		}

		/*
		 * Q & A
		 */

		i = STARTROW;
		col++;
		col++;
		errno = 1105;

		sheet[0].setValue(col, i, "Household Questions Included", boldTopStyle);
		sheet[0].setValue(col + 1, i, "Answer", boldTopStyle);

		try {
			i++;
			for (ConfigAnswer ans : customReportSpec.getConfigAnswer()) {

				String prompt = ans.getConfigQuestionUse().getConfigQuestion().getPrompt();
				String answer = ans.getAnswer();

				sheet[0].setValue(col, i, prompt, textStyle);
				sheet[0].setValue(col + 1, i, answer, textStyle);
				i++;
			}
		} catch (Exception e) {

			System.out.println("No config answer");
		}

		/*
		 *
		 * get Currency
		 */

		String currency = "UNKNOWN";

		Optional<LivelihoodZone> findFirstLZ = hh.get(0).getHousehold().getStudy().getProjectlz().getLivelihoodZone()
				.stream().findFirst();

		try {
			if (study.getProjectlz().getAltCurrency() == null) {
				currency = findFirstLZ.get().getCountry().getCurrency();

			} else {
				currency = study.getProjectlz().getAltCurrency().getCurrency();

			}
		} catch (Exception e1) {

		}

		errno = 1107;

		errno = 1107_3;
		sheet[0].setValue(1, 4, "Reporting Currency:", boldTopStyle);
		sheet[0].setValue(2, 4, currency, textStyle);

		errno = 1108;
	}

	/******************************************************************************************************************************************/

	private void setSheetStyle(JxlsSheet sheet) {
		sheet.setColumnWidths(1, 20, 60);

	}

	/******************************************************************************************************************************************/

	private void setStyles() {
		reportWB.setDateFormat("dd/MM/yyyy");
		boldRStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT);
		boldTopStyle = reportWB.addStyle(TEXT).setBold().setAlign(RIGHT).setWrap(true);
		borderStyle = reportWB.addStyle(TEXT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		textStyle = reportWB.addStyle(TEXT).setAlign(RIGHT);
		textStyleBlue = reportWB.addStyle(TEXT).setAlign(LEFT).setTextColor(BLUE);

		reportWB.setDateFormat("dd/MM/yyyy");

		dateStyle = reportWB.addStyle(reportWB.getDateFormat())
				.setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN, BORDER_THIN).setAlign(RIGHT);

		numberStyle = reportWB.addStyle(FLOAT).setAlign(RIGHT).setBorders(BORDER_THIN, BORDER_THIN, BORDER_THIN,
				BORDER_THIN);
		f1Style = reportWB.addStyle("0.0");
	}

	/******************************************************************************************************************************************/

	@Override
	public String getForwardURI() {
		return forwardURI;
	}

	/******************************************************************************************************************************************/

	public void setForwardURI(String forwardURI) {
		this.forwardURI = forwardURI;
	}

	/******************************************************************************************************************************************/

	@Override
	public boolean inNewWindow() {
		if (forwardURI == null)
			return false;
		return true;
	}

	/******************************************************************************************************************************************/

	/******************************************************************************************************************************************/

	public static class HHSub {
		private int hhid;
		private AssetLand assetLand;
		private AssetLiveStock assetLivestock;
		private ResourceSubType assetRST;
		private String assetName;
		private Double assetValue;
		private Double hhDI;
		private int column;
		private int quantSeq;
		private Quantile quantile;

		public Quantile getQuantile() {
			return quantile;
		}

		public void setQuantile(Quantile quantile) {
			this.quantile = quantile;
		}

		public String getAssetName() {
			return assetName;
		}

		public void setAssetName(String assetName) {
			this.assetName = assetName;
		}

		public int getQuantSeq() {
			return quantSeq;
		}

		public void setQuantSeq(int quantSeq) {
			this.quantSeq = quantSeq;
		}

		public int getHhid() {
			return hhid;
		}

		public void setHhid(int hhid) {
			this.hhid = hhid;
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

		public Double getHhDI() {
			return hhDI;
		}

		public void setHhDI(Double hhDI) {
			this.hhDI = hhDI;
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