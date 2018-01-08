package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

import efd.actions.*;
import efd.validations.*;

@Entity

@Views({

		@View(members = "site," + "Interview [" + "cinterviewdate;" + "cinterviewsequence;" + "interviewers;" + "],"
				+ "Attendees[" + "civf;" + "civm;" + "civparticipants;" + "]," + "Project[projectlz];"
				+ "Wealth_group{wealthgroup}" + "Community_year_notes{communityyearnotes},"),
		@View(name="Communitynoproject",members = "site," + "Interview [" + "cinterviewdate;" + "cinterviewsequence;" + "interviewers;" + "],"
				+ "Attendees[" + "civf;" + "civm;" + "civparticipants;" + "]," 
				+ "Wealth_group{wealthgroup}" + "Community_year_notes{communityyearnotes},"),

		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm"),

		@View(name = "OriginalCommunity", members = "site;livelihoodzone;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers") })

/* Note the use of underscore in labels - mapped in i18n file */

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

	@ManyToOne(fetch = FetchType.LAZY, // This is FK to Site == Location
			optional = false)
	@ReferenceView("SimpleSite")
	@JoinColumn(name = "CLocation")
	private Site site;
	// ----------------------------------------------------------------------------------------------//

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@DescriptionsList(descriptionProperties = "projecttitle,pdate")
	@JoinColumn(name = "CProject")
	private Project projectlz;
	// ----------------------------------------------------------------------------------------------//
    @DetailAction(value="Spreadsheet.scenario")
	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
	//@CollectionView("SimpleWealthGroup")
	private Collection<WealthGroup> wealthgroup;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
	private Collection<CommunityYearNotes> communityyearnotes;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CInterviewSequence")
	@Required
	private Integer cinterviewsequence;
	// ----------------------------------------------------------------------------------------------//

	@Stereotype("DATE")
	@Column(name = "CInterviewDate")
	@Required
	private java.util.Date cinterviewdate;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "Interviewers", length = 255)
	private String interviewers;
	// ----------------------------------------------------------------------------------------------//

	@ReadOnly // Calculates total particpants as male + female - no need for
				// setters
	@Depends("civf,civm")
	@Column(name = "CIVParticipants")
	public Integer getCivparticipants() {
		if (civf == null) {
			civf = 0;
		}
		if (civm == null) {
			civm = 0;
		}

		return civf + civm;
	}
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CIVM")
	private Integer civm;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "CIVF")
	private Integer civf;
	// ----------------------------------------------------------------------------------------------//

	/* Dont autogen getters and setters as civparticipants is calulated */

	public String getCommunityid() {
		return communityid;
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

	/* get / set */

}
