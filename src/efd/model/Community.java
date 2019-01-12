package efd.model;

import java.math.*;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;

import efd.actions.*;
import efd.validations.*;

@Entity

@Views({

		@View(members = "Community[Project[projectlz];site,Interview [cinterviewdate;cinterviewsequence;interviewers;],"
				+ "Attendees[" + "civf;" + "civm;" + "civparticipants;" + "]," + "notes;wgpercenttotal,warningMessage]"
				+ "Wealth_group{wealthgroup}" + "Community_year_notes{communityyearnotes},"),
		@View(name = "Communitynoproject", members = "site," + "Interview [" + "cinterviewdate;" + "cinterviewsequence;"
				+ "interviewers;" + "]," + "Attendees[" + "civf;" + "civm;" + "civparticipants;" + "],"
				+ "Wealth_group{wealthgroup}" + "Community_year_notes{communityyearnotes},"),

		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm"),
		@View(name = "FromWGCommunity", members = "projectlz, site"),

		@View(name = "OriginalCommunity", members = "site;livelihoodzone;cinterviewdate,cinterviewsequence,civf,civm,interviewers") })

/* Note the use of underscore in labels - mapped in i18n file */

@Tab(properties = "projectlz.projecttitle,site.livelihoodZone.lzname,cinterviewsequence,site.locationdistrict,site.subdistrict,cinterviewdate,interviewers,civm,civf")

@Table(name = "Community")
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
	@RowAction("Spreadsheet.Template Spreadsheet")
	@CollectionView("FromCommunity")
	@ListProperties("wgnameeng,wgnamelocal,wgorder,wgwives,wghhsize,wgpercent+")
	private Collection<WealthGroup> wealthgroup;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
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
	@NoModify
	
	private Project projectlz;	
	// ----------------------------------------------------------------------------------------------//
	

	@ManyToOne  //(fetch = FetchType.LAZY, optional = false)
	@SearchAction("Community.filteredSitesearch")
	// need a new LZ create check @AddAction("LivelihoodZone.add LZ")
	@NoFrame(forViews = "FromWGCommunity")
	//@ReferenceViews({ @ReferenceView(forViews = "DEFAULT", value = "SimpleSite"),
		//	@ReferenceView(forViews = "FromWGCommunity", value = "FromWealthGroup") })
	
	@ReferenceView("SimpleSite")
	@JoinColumn(name = "CLocation")
	private Site site;

	// ----------------------------------------------------------------------------------------------//
	

	// ----------------------------------------------------------------------------------------------//
	// Is total above 100%
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
			//System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "red" + ">Total Wealthgroup Percent is greater than 100</font>";

		} else if (getWgpercenttotal() < 100) {
			//System.out.println("in stereotype" + getWgpercenttotal());
			return "<font color=" + "orange" + ">Total Wealthgroup Percent is less than 100</font>";
		} else if (getWgpercenttotal() == 100) {
			return "<font color=" + "green" + ">Total Wealthgroup Percent is 100</font>";
		}
		return null;
	}


	@ReadOnly
	@Column(name = "CIVparticipants")
	@Calculation("civm+civf")
	private Integer civparticipants;
	// ----------------------------------------------------------------------------------------------//
	
	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;
	// ----------------------------------------------------------------------------------------------//


	/* Dont autogen getters and setters as civparticipants is calulated */

	public String getCommunityid() {
		return communityid;
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

	/* get / set */

}
