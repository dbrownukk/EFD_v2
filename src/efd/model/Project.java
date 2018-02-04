package efd.model;

import java.text.*;
import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.time.*;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.Tab;
import org.openxava.validators.*;
import org.openxava.actions.*;

import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;
import org.openxava.tab.*;

@Entity

	@Views({ @View(members = "Project[projecttitle,pdate];livelihoodZone"),
		@View(name="NewLZ", members = "Project[projecttitle,pdate];livelihoodZone;community"),
		@View(name = "SimpleProject", members = "projecttitle,pdate,Project.Spreadsheet()"),
		@View(name = "NewlineProject", members = "projecttitle;pdate") })

@Tab(properties = "projecttitle;pdate", editors = "List, Cards") 

@Table(name = "Project")
public class Project {
	
	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ProjectID", length = 32, unique = true)
	private String projectid;

	@Column(name = "ProjectTitle", length = 255, unique = true)
	@Required
	private String projecttitle;
	
	  @Stereotype("DATE")
	    @Column(name="PDate") @Required
	    private java.util.Date pdate;
	

	@NewAction("LivelihoodZone.new LZ")
	//@AddAction("LivelihoodZone.add LZ") Not needed as standard code save master and detail
	
	
	@ManyToMany
	//@org.hibernate.validator.constraints   (min=1)
	@JoinTable(name = "projectlz", joinColumns = @JoinColumn(name = "Project", referencedColumnName = "ProjectID", nullable = false), inverseJoinColumns = @JoinColumn(name = "LZ", referencedColumnName = "LZID", nullable = false))
	@ListProperties("lzname,country.description,lzzonemap")
	@CollectionView("SimpleLZ")
	
	private Collection<LivelihoodZone> livelihoodZone;

	/* Lets keep it simple - Project and LZ 
	//@NewAction("")
	@OneToMany(mappedBy = "projectlz", cascade=CascadeType.ALL)
	@CollectionView("Communitynoproject")
	@OrderBy("cinterviewsequence")
	@ListProperties("cinterviewsequence,site.locationdistrict,site.subdistrict,cinterviewdate,interviewers,civparticipants,civm,civf")
	private Collection<Community> community;
	 */
	

	
	
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





}
