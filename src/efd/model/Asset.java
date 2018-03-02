package efd.model;



import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.model.*;

@MappedSuperclass


abstract public class Asset {

	//@Id
	//@Hidden 
	//@GeneratedValue(generator = "system-uuid") 
	//@GenericGenerator(name = "system-uuid", strategy = "uuid")
	//@Column(name = "ID", length = 32, unique = true)
	//private String id;
	
	//@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
		//	optional = false)

	//@JoinColumn(name = "WGIID")     /* Wealthgroup Interview ID */
	//@DescriptionsList
	//private WealthGroupInterview wealthgroupinterview;
	
	
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@JoinColumn(name = "AssetType")     /* Asset Type ID */
	@DescriptionsList(descriptionProperties = "resourcetypename")
	private ResourceSubType resourcesubtype;




	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}


	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}


	
	
}
