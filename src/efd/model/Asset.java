package efd.model;



import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@MappedSuperclass


public class Asset {

	@Id
	@Hidden 
	@GeneratedValue(generator = "system-uuid") 
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", length = 32, unique = true)
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)

	@JoinColumn(name = "WGIID")     /* Wealthgroup Interview ID */
	//@DescriptionsList
	private WealthGroupInterview wealthgroupinterview;
	
	
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@JoinColumn(name = "AssetType")     /* Asset Type ID */
	//@DescriptionsList
	private ResourceSubType resourcesubtype;




	public WealthGroupInterview getWealthgroupinterview() {
		return wealthgroupinterview;
	}


	public void setWealthgroupinterview(WealthGroupInterview wealthgroupinterview) {
		this.wealthgroupinterview = wealthgroupinterview;
	}


	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}


	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}
	
	
	
}
