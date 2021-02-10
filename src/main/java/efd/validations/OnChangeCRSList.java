package efd.validations;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.Tab;
import org.openxava.util.XavaException;

import efd.actions.GoOHEAReports;
import efd.rest.domain.model.*;

public class OnChangeCRSList extends OnChangePropertyBaseAction {

	@Override
	public void execute() throws Exception {
		String condition = "";
		boolean isStudy = false;
		boolean isFromMenu = false;
		Tab tab = null;
		/*
		 * Check if any HH / WG restrictions are set in Category, RST, RT or
		 * Questions/Answers
		 * 
		 */

		System.out.println("In OnchangeCRSList");

	

		try {
			isStudy = getPreviousView().getModelName().equals("Study");  // is this from Study or Menu
		} catch (EmptyStackException e1) {
			// TODO Auto-generated catch block
			if (getView().getModelName().equals("CustomReportSpecList")) {
				if(getView().getValue("studyToSelect.id").equals(""))
				{
					addError("Select a Study before selecting a Report Specification");
					return;
				}
				
				isStudy = true;
				isFromMenu = true;
			}
		}

		if (isStudy) { // OIHM

			ArrayList<Household> hhList = new ArrayList<Household>();
			Object studyid;
			if (!isFromMenu) {
				studyid = getPreviousView().getValue("id");
			} else {
				studyid = getView().getValue("studyToSelect.id");
			}
			Study study = XPersistence.getManager().find(Study.class, studyid.toString());
			tab = getView().getSubview("study.household").getCollectionTab();
			

			String crsid = getView().getValueString("customReportSpec.id");
			CustomReportSpec crs = XPersistence.getManager().find(CustomReportSpec.class, crsid);

			if (crs.getConfigAnswer().size() > 0 || crs.getResourceSubType().size() > 0
					|| crs.getResourceType().size() > 0 || crs.getCategory().size() > 0) {

				/* Need to filter out HH that do not have RSTs in play */
				Collection<ResourceSubType> rstsInCRS = getRSTsForCRSOHEA(crs.getCategory(), crs.getResourceType(),
						crs.getResourceSubType());

				rstsInCRS = rstsInCRS.stream().distinct().collect(Collectors.toList());

				System.out.println("in Study crs of rsts = " + rstsInCRS.size());

				// rstsInCRS.stream().forEach(p ->
				// System.out.println("rstinCRS = "+p.getResourcetype().getResourcetypename()+"
				// "+p.getResourcetypename())
				// );
				String filterHH = "";
				for (Household household : study.getHousehold()) {
					for (ResourceSubType resourceSubType : household.getRSTsForHH()) {

						rstsInCRS.stream().filter(p -> p.equals(resourceSubType)).forEach(p -> {
							System.out.println(
									"match for " + p.getResourcetypename() + " " + household.getHouseholdNumber());
							hhList.add(household);
						});
					}
				}

				List<String> collect = hhList.stream().distinct().map(p -> p.getId()).collect(Collectors.toList());
				if (collect.size() == 0) {
					addWarning("No Households match Custom Report Specification Criteria");
					condition = "${study.id} = ? and ${status} = '0'";
					tab.setBaseCondition(condition);
				} else {
					for (int j = 0; j < collect.size(); j++) {
						filterHH += "'" + collect.get(j) + "',";
					}
					filterHH = StringUtils.chop(filterHH);

					condition = "${study.id} = ? and ${Id} in (" + filterHH + ") and ${status} = '4'";

					tab.setBaseCondition(condition); // Validated
					addWarning("Households filtered by Custom Report Specification ");
				}
				getView().refreshCollections();

				getView().getSubview("study.household").collectionDeselectAll();

			} else // No OIHM filtering of HHs
			{
				System.out.println();
				tab = getView().getSubview("study.household").getCollectionTab();

				System.out.println("tab base cond = " + tab.getBaseCondition());

				tab.setBaseCondition("${study.id} = ? and ${status} = '4'"); // Validated
				getView().refreshCollections();

			}
		} else { // OHEA

			String crsid = getView().getValueString("customReportSpec.id");
			CustomReportSpecOHEA crs = XPersistence.getManager().find(CustomReportSpecOHEA.class, crsid);

			if (!crs.getCategory().isEmpty() || !crs.getResourceType().isEmpty()
					|| !crs.getResourceSubType().isEmpty()) {
				addWarning(
						"Sites filtered by Custom Report Specification Category, Resource Type or Resource Sub Type  ");
			}

			tab = getView().getSubview("livelihoodZone.site").getCollectionTab();

			/*
			 * If crs has RSTs listed then filter out Sites that do not have the filtered in
			 * RSTs
			 */

			Collection<ResourceSubType> rsTsForCRSOHEA = getRSTsForCRSOHEA(crs.getCategory(), crs.getResourceType(),
					crs.getResourceSubType());

			Object lzid = getView().getValue("livelihoodZone.lzid");

			Object projectid;
			try { // called from menu system
				projectid = getPreviousView().getValue("project.projectid");
			} catch (XavaException e) {
				// TODO Auto-generated catch block
				// System.out.println("called from community not from menu");
				projectid = getPreviousView().getValue("projectid");
			}

			LivelihoodZone livelihoodZone = XPersistence.getManager().find(LivelihoodZone.class, lzid.toString());

			/* Get list of sites with at least 1 Valid WGI and add to condition */

			String sitesInProject = GoOHEAReports.isLZinProject(projectid, livelihoodZone);
			sitesInProject = StringUtils.chop(sitesInProject);

			if (crs.getResourceSubType().size() > 0 || crs.getResourceType().size() > 0
					|| crs.getCategory().size() > 0) {

				/* Does WGI have any of RSTs in filter */

				List<Site> remainingSites = new ArrayList<>();
				List<Site> listSites = XPersistence.getManager()
						.createQuery("from Site where locationid in (" + sitesInProject + ")").getResultList();

				for (ResourceSubType resourceSubType : rsTsForCRSOHEA) {
					for (Site site : listSites) {
						for (Community community : site.getCommunity()) {
							for (WealthGroup wealthGroup : community.getWealthgroup()) {
								for (WealthGroupInterview wealthGroupInterview : wealthGroup
										.getWealthGroupInterview()) {
									if (wealthGroupInterview.getRSTsForWGI().stream()
											.anyMatch(p -> p == resourceSubType)) {
										remainingSites.add(site);
									}

								}
							}
						}
					}
				}

				String strListOfSites = "";
				for (int j = 0; j < remainingSites.size(); j++) {
					strListOfSites += "'" + remainingSites.get(j).getLocationid() + "',";
				}
				condition = "";

				if (strListOfSites.length() < 1) {
					addWarning("No Sites match Custom Report Specification Criteria");
					tab.setBaseCondition(condition);
				} else {
					strListOfSites = StringUtils.chop(strListOfSites);

					System.out.println("remaining sites after filter = " + remainingSites.toString());

					condition = "${livelihoodZone.lzid} = ? and ${locationid} in (" + strListOfSites + ")";

					tab.setBaseCondition(condition);
					getView().getSubview("livelihoodZone.site").collectionDeselectAll();

				}
			} else // No filtering in Customreportspec
			{

				condition = "${livelihoodZone.lzid} = ? and ${locationid} in (" + sitesInProject + ")";

				System.out.println("unfiltered condition = " + condition);
				tab.setBaseCondition(condition);

			}

		}

		// Tab endTab = getView().getSubview("livelihoodZone.site").getCollectionTab();
		// System.out.println("condition = " + endTab.getBaseCondition());

		// getView().findObject();
		getView().refreshCollections();

		// getView().refreshDescriptionsLists();
		// getView().refresh();
		System.out.println("refreshed view ");
	}

	private void getRSTsForCRSOHEA(CustomReportSpec crs) {
		// TODO Auto-generated method stub

	}

	/*
	 * 
	 * List all ResourceSubTypes used in customreportspec
	 * (crop,lsp,lss,transfers,emp,wildfood)
	 * 
	 * 
	 */
	@Transient

	// private Collection<ResourceSubType> getRSTsForCRSOHEA(CustomReportSpecOHEA
	// crs) {
	Collection<ResourceSubType> getRSTsForCRSOHEA(Collection<Category> categorys,
			Collection<ResourceType> resourceTypes, Collection<ResourceSubType> resourceSubTypes) {

		ArrayList<Category> catArray = new ArrayList<>(categorys);
		ArrayList<ResourceType> rtArray = new ArrayList<>(resourceTypes);
		ArrayList<ResourceSubType> rstArray = new ArrayList<>(resourceSubTypes);

		ArrayList<ResourceSubType> collect = new ArrayList<>();
		Collection<ResourceSubType> catCollection = null;
		Collection<ResourceSubType> rtCollection = null;
		Collection<ResourceSubType> rstCollection = null;

		for (Category category : catArray) {
			catCollection = category.getResourceSubType();

		}
		if (catCollection != null)
			collect.addAll(catCollection);

		for (ResourceType resourceType : rtArray) {
			rtCollection = resourceType.getResourcesubtype();
		}

		if (rtCollection != null)
			collect.addAll(rtCollection);

		for (ResourceSubType resourceSubType : rstArray) {
			collect.add(resourceSubType);
		}

		List<ResourceSubType> collect2 = collect.stream().distinct().filter(p -> p != null)
				.collect(Collectors.toList());

		// collect2.stream().forEach(p -> System.out.println("rst in wgi = " +
		// p.getResourcetypename()));

		return (collect2);

	}

}
