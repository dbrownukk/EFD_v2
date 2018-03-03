package efd.model;



import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.model.*;

import efd.model.WealthGroupInterview.*;

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
	
	
	//@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
		//	optional = false)
	//@NoModify
	//@NoCreate
	//@JoinColumn(name = "AssetType")     /* Asset Type ID */
	//@DescriptionsList(descriptionProperties = "resourcetypename")
	
	@Column(name="AssetType", length=50)
	private String assetType;

	@Column(name="Status", nullable=false)
	private Status status;
	public enum Status { Invalid, NotChecked, Valid }

	
	
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	};





	
	
}
