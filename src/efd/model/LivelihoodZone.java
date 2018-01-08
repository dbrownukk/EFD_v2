package efd.model;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Views({
	 @View(members="Livelihood_Zone[lzname;country;lzzonemap]"),
	 @View(name="UpdateLZ", members="Livelihood Zone[lzname,country,lzzonemap,project,site]"),
	 @View(name="CreateLZ", members="Livelihood Zone[lzname,country,lzzonemap]"),
	 @View(name="SimpleLZ", members="lzname;country;lzzonemap")
	})


@Tab(properties="lzname,country.description,lzzonemap") // Note - cannot add Site - it is a OneToMany


@Entity

@Table(name = "LivelihoodZone",uniqueConstraints=@UniqueConstraint(columnNames={"LZName","LZCountry"}))


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
   

   
   //@OneToMany(mappedBy="livelihoodZone", cascade=CascadeType.ALL)
   //@ListProperties("locationdistrict,subdistrict,gpslocation")
   
    //private Collection<Site> site;

    
    @ManyToMany(mappedBy="livelihoodZone") 
    //@ListProperties("projecttitle,pdate")
  
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


	//public Collection<Site> getSite() {
		//return site;
	//}


	//public void setSite(Collection<Site> site) {
		//this.site = site;
	//}


	public Collection<Project> getProject() {
		return project;
	}


	public void setProject(Collection<Project> project) {
		this.project = project;
	}

	

	


    /* Get / set */
    
	

    
    
}
