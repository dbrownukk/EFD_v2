package reasyst.efd;

import java.util.List;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

import antlr.collections.*;

@Entity 
@Views({
	 @View(name="Simple", members="country, lzname;"),
	})


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
    
    @ManyToMany
    @JoinTable(name="ProjectLZ",
    		joinColumns=@JoinColumn(name="LZ", referencedColumnName="LZID"),
    	      inverseJoinColumns=@JoinColumn(name="Project", referencedColumnName="ProjectID"))

    
    private List<Project> projects;
    
    
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getLzid() {
		return lzid;
	}

	public void setLzid(String lzid) {
		this.lzid = lzid;
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
    
    
    
}
