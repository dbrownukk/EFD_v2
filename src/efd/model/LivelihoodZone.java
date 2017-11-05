package efd.model;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Views({
	 @View(members="Livelihood Zone[lzname,country,lzzonemap,project,site]"),	
	 @View(name="SimpleLZ", members="lzname;country;lzzonemap")
	})



@Entity

//@Tab ( editors ="List, Cards", properties="lzname,country") // DO NOT USE - Causes problems with Add

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
   
    

   @OneToMany(mappedBy="livelihoodZone", cascade=CascadeType.REMOVE)
   @ListProperties("locationdistrict,subdistrict,gpslocation")
    private Collection<Site> site;

    
    @ManyToMany(mappedBy="livelihoodZone",cascade=CascadeType.REMOVE) 
    @ListProperties("projecttitle,pdate")
    private Collection<Project> project;


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


	public Collection<Project> getProject() {
		return project;
	}


	public void setProject(Collection<Project> project) {
		this.project = project;
	}

	

	


    /* Get / set */
    
	

    
    
}
