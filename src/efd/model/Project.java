package efd.model;

import java.text.*;
import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.time.*;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.annotations.NewAction;
import org.openxava.validators.*;
import org.openxava.actions.*;


@Entity

	//@Views({ @View(members = "Project[projecttitle,pdate,projectid];livelihoodZone;community"),
	@Views({ @View(members = "Project[projecttitle,pdate];livelihoodZone"),

		@View(name = "SimpleProject", members = "projecttitle,pdate,Project.Spreadsheet()"),
		@View(name = "NewlineProject", members = "projecttitle;pdate") })

@Tab(properties = "projecttitle;pdate", editors = "List, Cards") // removes
																	// graph
																	// option

@Table(name = "Project")
public class Project {

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// // (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ProjectID", length = 32, unique = true)
	private String projectid;

	@Column(name = "ProjectTitle", length = 255, unique = true)
	@Required
	private String projecttitle;

	//@Stereotype("DATE")
	//@Column(name = "PDate")
	//@Required
	//private Date pdate;
	
	  @Stereotype("DATETIME")
	    @Column(name="PDate") @Required
	    private java.util.Date pdate;
	
	//@NewAction("ManyToMany.new")
	@NewAction("LivelihoodZone.new LZ")
	
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "projectlz", joinColumns = @JoinColumn(name = "Project", referencedColumnName = "ProjectID", nullable = false), inverseJoinColumns = @JoinColumn(name = "LZ", referencedColumnName = "LZID", nullable = false))
	@ListProperties("lzname,country.description,lzzonemap")
	private Collection<LivelihoodZone> livelihoodZone;

	@OneToMany(mappedBy = "projectlz")
	@CollectionView("Communitynoproject")
	private Collection<Community> community;

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getProjecttitle() {
		return projecttitle;
	}

	public void setProjecttitle(String projecttitle) {
		this.projecttitle = projecttitle;
	}

	public Date getPdate() {
		return pdate;
	}

	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}

	public Collection<LivelihoodZone> getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(Collection<LivelihoodZone> livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public Collection<Community> getCommunity() {
		return community;
	}

	public void setCommunity(Collection<Community> community) {
		this.community = community;
	}



}
