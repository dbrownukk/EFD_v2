package reasyst.efd;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Views({
	 @View(members="Site [locationdistrict,subdistrict,gpslocation,livelihoodzone];"),
	 @View(name="SimpleSite", members="locationdistrict,subdistrict,gpslocation,livelihoodzone;"),
	 @View(name="NewlineSite", members="locationdistrict;subdistrict;gpslocation;livelihoodzone;")
	})



@Entity 

@Table(name="Site")

public class Site {

	@Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="LocationID",length=32,unique=true)
    private String locationid;

    @Column(name="LocationDistrict",length=25) @Required
    private String locationdistrict;

    @Column(name="GPSLocation",length=25)
    private String gpslocation;
    
    
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@JoinColumn(name="LZ")
	@DescriptionsList(descriptionProperties="lzname")
    private  LivelihoodZone livelihoodzone;
    
    @Column(name="SubDistrict",length=25)
    private String subdistrict;
    

	







	public String getLocationid() {
		return locationid;
	}


	public void setLocationid(String locationid) {
		this.locationid = locationid;
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




	public String getGpslocation() {
		return gpslocation;
	}


	public void setGpslocation(String gpslocation) {
		this.gpslocation = gpslocation;
	}

    



}
