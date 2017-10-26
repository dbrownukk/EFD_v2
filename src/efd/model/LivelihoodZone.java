package efd.model;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

@Views({
	 @View(members="Livelihood[lzname;country,lzzonemap]"+"site"),
	 @View(name="SimpleLZ", members="lzname,country,locationdistrict")
	})



@Entity

// @Tab ( editors ="List, Cards", properties="lzname,country,lzzonemap") // removes graph option

@Table(name="LivelihoodZone")

public class LivelihoodZone {

	@Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="LZID",length=32,unique=true)
    private String lzid;
 
	
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@JoinColumn(name="LZCountry")	
	@DescriptionsList
    private  Country country;

    @Column(name="LZName",length=255) @Required
    private String lzname;
	
    @Column(name="LZZoneMap",length=250) 
    private String lzzonemap;
    
    /*
    @ManyToMany(mappedBy="livelihoodZone") 
    private Collection<Project> project;
    */

    @OneToMany(mappedBy="livelihoodZone")
    private Collection<Site> site;

    
    @OneToMany(mappedBy="livelihoodZone")
    private Collection<ProjectLZ> projectLZ;


	public String getLzid() {
		return lzid;
	}


	public void setLzid(String lzid) {
		this.lzid = lzid;
	}


	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
	}


	public String getLzname() {
		return lzname;
	}


	public void setLzname(String lzname) {
		this.lzname = lzname;
	}


	public String getLzzonemap() {
		return lzzonemap;
	}


	public void setLzzonemap(String lzzonemap) {
		this.lzzonemap = lzzonemap;
	}


	public Collection<Site> getSite() {
		return site;
	}


	public void setSite(Collection<Site> site) {
		this.site = site;
	}


	public Collection<ProjectLZ> getProjectLZ() {
		return projectLZ;
	}


	public void setProjectLZ(Collection<ProjectLZ> projectLZ) {
		this.projectLZ = projectLZ;
	}


	


    /* Get / set */
    
	

    
    
}
