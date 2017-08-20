package reasyst.efd;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;


@View(members="livelihoodzone;locationdistrict,subdistrict,gpslocation;project")
@Entity 

@Table(name="Site")

public class Site {

	@Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="LocationID",length=32,unique=true)
    private String lzid;
	
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@JoinColumn(name="LZ")
	@ReferenceView("Simple")
    private  LivelihoodZone livelihoodzone;

    @Column(name="LocationDistrict",length=255) @Required
    private String locationdistrict;
 
    
    @Column(name="SubDistrict",length=250)
    private String subdistrict;
    
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@JoinColumn(name="Project")
    private  Project project;
	

    @Column(name="GPSLocation",length=250)
    private String gpslocation;


	public String getLzid() {
		return lzid;
	}


	public void setLzid(String lzid) {
		this.lzid = lzid;
	}


	public LivelihoodZone getLivelihoodzone() {
		return livelihoodzone;
	}


	public void setLivelihoodzone(LivelihoodZone livelihoodzone) {
		this.livelihoodzone = livelihoodzone;
	}


	public String getLocationdistrict() {
		return locationdistrict;
	}


	public void setLocationdistrict(String locationdistrict) {
		this.locationdistrict = locationdistrict;
	}


	public String getSubdistrict() {
		return subdistrict;
	}


	public void setSubdistrict(String subdistrict) {
		this.subdistrict = subdistrict;
	}


	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public String getGpslocation() {
		return gpslocation;
	}


	public void setGpslocation(String gpslocation) {
		this.gpslocation = gpslocation;
	}

    



}
