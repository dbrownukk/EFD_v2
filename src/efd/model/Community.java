package efd.model;

import java.math.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.actions.*;
import efd.model.Asset.Status;
import efd.validations.*;

@Entity

@Views({

		@View(members = "Community[Project[projectlz];ReferenceYear[referenceYearStartDate,referenceYearEndDate]"
				+ ";;site,Interview [cinterviewdate;cinterviewsequence;interviewers;],"
				+ "Attendees[" + "civf;" + "civm;" + "civparticipants]"
				+ ";wgpercenttotal,warningMessage;ddipercenttotal,warningDDIMessage]" + "Wealth_group{wealthgroup}"
				+ "StandardOfLivingElement{stdOfLivingElement};"
				+ "DefaultDietItem{defaultDietItem};ExpandabilityRule{expandabilityRule};Community_year_notes{communityyearnotes},"),
		@View(name = "Communitynoproject", members = "site," + "Interview [" + "cinterviewdate;" + "cinterviewsequence;"
				+ "interviewers;" + "]," + "Attendees[" + "civf;" + "civm;" + "civparticipants;" + "],"
				+ "Wealth_group{wealthgroup}" + "Community_year_notes{communityyearnotes},"),

		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm"),
		@View(name = "FromWGCommunity", members = "projectlz, site"), @View(name = "FromReport", members = "site"),
		@View(name = "FromStdOfLiving", members = "site"),
		@View(name = "OriginalCommunity", members = "site;livelihoodzone;cinterviewdate,cinterviewsequence,civf,civm,interviewers") })
/* Note the use of underscore in labels - mapped in i18n file */

@Tab(properties = "projectlz.projecttitle,site.livelihoodZone.lzname,cinterviewsequence,site.locationdistrict,site.subdistrict,cinterviewdate,interviewers,civm,civf")

// #589 Issue fix to have unique site within Project 
@Table(name = "Community", uniqueConstraints = @UniqueConstraint(name = "uniqueSite",columnNames = { "CProject",
"CLocation" })) 




public class Community {
	// ----------------------------------------------------------------------------------------------//

	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "CID", length = 32, unique = true)
	private String communityid;
	// ----------------------------------------------------------------------------------------------//

	@Stereotype("DATE")
	@Column(name = "CInterviewDate")

	private java.util.Date cinterviewdate;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
	@RowAction(notForViews = "FromReport", value = "Spreadsheet.Template Spreadsheet") // not needed in the call to
																						// Reports
	@CollectionView("FromCommunity")
	@ListProperties(forViews = "default", value = "wgnameeng,wgnamelocal,wgorder,wgwives,wghhsize,wgpercent+")
	@ListProperties(forViews = "FromReport", value = "wgnameeng,wgnamelocal")
	@AddAction("")
	private Collection<WealthGroup> wealthgroup;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
	@AddAction("")
	private Collection<CommunityYearNotes> communityyearnotes;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CInterviewSequence")

	private Integer cinterviewsequence;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CIVM")
	private Integer civm;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CIVF")
	private Integer civf;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "Interviewers", length = 255)
	private String interviewers;

	// ----------------------------------------------------------------------------------------------//

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "projecttitle,pdate")
	@JoinColumn(name = "CProject")
	@OnChange(OnChangeClearCommunity.class)
	@ReferenceView("SimpleProject")
	@NoCreate
	// @NoModify

	private Project projectlz;
	// ----------------------------------------------------------------------------------------------//

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@SearchAction("Community.filteredSitesearch")
	// need a new LZ create check @AddAction("LivelihoodZone.add LZ")
	@NoFrame(forViews = "FromWGCommunity")
	// @ReferenceViews({ @ReferenceView(forViews = "DEFAULT", value = "SimpleSite"),
	// @ReferenceView(forViews = "FromWGCommunity", value = "FromWealthGroup") })

	@ReferenceView("SimpleSite")
	@JoinColumn(name = "CLocation")
	private Site site;

	// ----------------------------------------------------------------------------------------------//

	// ----------------------------------------------------------------------------------------------//
	// Is Wealthgroup % total != 100%
	// ----------------------------------------------------------------------------------------------//

	@Transient
	@ReadOnly
	@Column(length = 3)

	public int getWgpercenttotal() {
		int result = 0;
		for (WealthGroup wealthgroup : getWealthgroup()) {
			result += wealthgroup.getWgpercent();
		}
		return result;
	}

	@Depends("wgpercenttotal")
	@Stereotype("LABEL") // @Stereotype("HTML_TEXT")
	@Transient
	@ReadOnly
	public String getWarningMessage() {
		if (getWgpercenttotal() > 100) {
			// System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "red" + ">Total Wealthgroup Percent is greater than 100</font>";

		} else if (getWgpercenttotal() < 100) {
			// System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "orange" + ">Total Wealthgroup Percent is less than 100</font>";
		} else if (getWgpercenttotal() == 100) {
			return "<font color=" + "green" + ">Total Wealthgroup Percent is 100</font>";
		}
		return null;
	}

	// ----------------------------------------------------------------------------------------------//
	// Is DDI % total != 100%
	// ----------------------------------------------------------------------------------------------//

	@Transient
	@ReadOnly
	@Column(length = 3)
	@DefaultValueCalculator(value = ZeroIntegerCalculator.class)

	public int getDdipercenttotal() {
		int result = 0;
		for (DefaultDietItem defaultDietItems : getDefaultDietItem()) {
			result += defaultDietItems.getPercentage();
		}
		return result;
	}

	@Depends("ddipercenttotal")
	@Stereotype("LABEL") // @Stereotype("HTML_TEXT")
	@Transient
	@ReadOnly
	public String getWarningDDIMessage() {
		if (getDdipercenttotal() > 100) {
			// System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "red" + ">Total Default Diet Items Percent is greater than 100</font>";

		} else if (getDdipercenttotal() < 100) {
			// System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "orange" + ">Total Default Diet Items Percent is less than 100</font>";
		} else if (getDdipercenttotal() == 100) {
			return "<font color=" + "green" + ">Total Default Diet Items Percent is 100</font>";
		}
		return null;
	}
	// ----------------------------------------------------------------------------------------------//

	@ReadOnly
	@Column(name = "CIVparticipants")
	@Calculation("civm+civf")
	private Integer civparticipants;
	// ----------------------------------------------------------------------------------------------//

	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;
	// ----------------------------------------------------------------------------------------------//
	@OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE)
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,percentage+,unitPrice")
	private Collection<DefaultDietItem> defaultDietItem;
	// ----------------------------------------------------------------------------------------------//
	@OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE)
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,amount,cost,level,survival")
	@EditAction("StdOfLivingElement.edit")
	@CollectionView("fromCommunity")

	private Collection<StdOfLivingElement> stdOfLivingElement;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "communityRuleSet", cascade = CascadeType.REMOVE)
	@ListProperties("ruleName,appliedResourceSubType.resourcetypename,sequence,expandabilityIncreaseLimit,expandabilityLimit")
	@CollectionView("fromCommunity")
	@NewAction("ExpandabilityRule.new")
	@EditAction("ExpandabilityRule.edit")
	private Collection<ExpandabilityRule> expandabilityRule;

	/*
	 * Used in Modelling reports where WGOrder has more than 1 WG in
	 * Poor/Middle/Better Off 1/2/3
	 */
	@Transient
	public int[] getCountWGinCommunity() {
		int[] ret = { 0, 0, 0 };

		try {
		
			for (int i = 0; i < ret.length; i++) {
			
				for (WealthGroup wg : getWealthgroup()) {
					if (wg.getWgorder() == i+1) {
						for (WealthGroupInterview wgi : wg.getWealthGroupInterview()) {
							if (wgi.getStatus() == efd.model.WealthGroupInterview.Status.Validated) {
								ret[i]++;
							}
						}
					}

				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * ret[0] = (int) getWealthgroup().stream().filter(p -> p.getWgorder() ==
		 * 1).filter( p -> p.getWealthGroupInterview().get(0).getStatus() ==
		 * efd.model.WealthGroupInterview.Status.Validated) .count();
		 * 
		 * ret[1] = (int) getWealthgroup().stream().filter(p -> p.getWgorder() ==
		 * 2).filter( p -> p.getWealthGroupInterview().get(0).getStatus() ==
		 * efd.model.WealthGroupInterview.Status.Validated) .count(); ret[2] = (int)
		 * getWealthgroup().stream().filter(p -> p.getWgorder() == 3).filter( p ->
		 * p.getWealthGroupInterview().get(0).getStatus() ==
		 * efd.model.WealthGroupInterview.Status.Validated) .count();
		 * 
		 */
		return ret;
	};
	
	
	
	/* #598 */
	@Stereotype("DATE")
	@DefaultValueCalculator(value = CurrentDateCalculator.class)
	private java.util.Date referenceYearStartDate;
	
	
	@Stereotype("DATE")
	@DefaultValueCalculator(value = CurrentDateCalculator.class)
	private java.util.Date referenceYearEndDate;
	

	/* Dont autogen getters and setters as civparticipants is calculated */

	
	
	
	public Collection<ExpandabilityRule> getExpandabilityRule() {
		return expandabilityRule;
	}

	/**
	 * @return the referenceYearStartDate
	 */
	public java.util.Date getReferenceYearStartDate() {
		return referenceYearStartDate;
	}

	/**
	 * @param referenceYearStartDate the referenceYearStartDate to set
	 */
	public void setReferenceYearStartDate(java.util.Date referenceYearStartDate) {
		this.referenceYearStartDate = referenceYearStartDate;
	}

	/**
	 * @return the referenceYearEndDate
	 */
	public java.util.Date getReferenceYearEndDate() {
		return referenceYearEndDate;
	}

	/**
	 * @param referenceYearEndDate the referenceYearEndDate to set
	 */
	public void setReferenceYearEndDate(java.util.Date referenceYearEndDate) {
		this.referenceYearEndDate = referenceYearEndDate;
	}

	public void setExpandabilityRule(Collection<ExpandabilityRule> expandabilityRule) {
		this.expandabilityRule = expandabilityRule;
	}

	public String getCommunityid() {
		return communityid;
	}

	public Collection<DefaultDietItem> getDefaultDietItem() {
		return defaultDietItem;
	}

	public void setDefaultDietItem(Collection<DefaultDietItem> defaultDietItem) {
		this.defaultDietItem = defaultDietItem;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setCommunityid(String communityid) {
		this.communityid = communityid;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Project getProjectlz() {
		return projectlz;
	}

	public void setProjectlz(Project projectlz) {
		this.projectlz = projectlz;
	}

	public Integer getCinterviewsequence() {
		return cinterviewsequence;
	}

	public void setCinterviewsequence(Integer cinterviewsequence) {
		this.cinterviewsequence = cinterviewsequence;
	}

	public java.util.Date getCinterviewdate() {
		return cinterviewdate;
	}

	public void setCinterviewdate(java.util.Date cinterviewdate) {
		this.cinterviewdate = cinterviewdate;
	}

	public String getInterviewers() {
		return interviewers;
	}

	public void setInterviewers(String interviewers) {
		this.interviewers = interviewers;
	}

	public Integer getCivm() {
		return civm;
	}

	public void setCivm(Integer civm) {
		this.civm = civm;
	}

	public Integer getCivf() {
		return civf;
	}

	public void setCivf(Integer civf) {
		this.civf = civf;
	}

	public Collection<WealthGroup> getWealthgroup() {
		return wealthgroup;
	}

	public void setWealthgroup(Collection<WealthGroup> wealthgroup) {
		this.wealthgroup = wealthgroup;
	}

	public Collection<CommunityYearNotes> getCommunityyearnotes() {
		return communityyearnotes;
	}

	public void setCommunityyearnotes(Collection<CommunityYearNotes> communityyearnotes) {
		this.communityyearnotes = communityyearnotes;
	}

	public Integer getCivparticipants() {
		return civparticipants;
	}

	public void setCivparticipants(Integer civparticipants) {
		this.civparticipants = civparticipants;
	}

	public Collection<StdOfLivingElement> getStdOfLivingElement() {
		return stdOfLivingElement;
	}

	public void setStdOfLivingElement(Collection<StdOfLivingElement> stdOfLivingElement) {
		this.stdOfLivingElement = stdOfLivingElement;
	}

	/* get / set */

}
