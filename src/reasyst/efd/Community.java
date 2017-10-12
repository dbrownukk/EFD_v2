package reasyst.efd;


import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Entity 



@Views({
	@View(members="Community [# cinterviewdate,cinterviewsequence;"
			+ "civf,civm,civparticipants,interviewers, site, project]"),
	 @View(name="SimpleCommunity", members="cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
	 @View(name="OriginalCommunity", members="site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers")
})

@Tab ( editors ="List, Cards", properties="site.locationdistrict,project.projecttitle,cinterviewdate,cinterviewsequence") // removes graph option

@Table(name="Community")
public class Community {
	
    @Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="CID",length=32,unique=true)
    private String communityid;
 
    
    
	@ManyToOne(fetch=FetchType.LAZY, // This is FK to Site == Location
	        optional=false)
	@ReferenceView("NewlineSite")
	@JoinColumn(name="CLocation")	
	private  Site site;
	

	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@ReferenceView("NewlineProject")
	@JoinColumn(name="CProject")	
	private  Project project;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@Column(name="CInterviewSequence") @Required
    private Integer cinterviewsequence;
	
	  @Stereotype("DATETIME")
	  @Column(name="CInterviewDate") @Required
	  private java.util.Date cinterviewdate;
	
	  @Column(name="Interviewers",length=255)
	    private String interviewers;
	  
	  @Column(name="CIVParticipants")
	    private Integer civparticipants;
	
	  @Column(name="CIVM")
	    private Integer civm;
 
	  @Column(name="CIVF")
	    private Integer civf;

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



	public Integer getCivparticipants() {
		return civparticipants;
	}

	public void setCivparticipants(Integer civparticipants) {
		this.civparticipants = civparticipants;
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
    
    
}

