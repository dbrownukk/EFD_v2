package efd.model;



import javax.persistence.*;

import org.openxava.annotations.*;

import efd.actions.*;

@MappedSuperclass

//abstract public class Asset {
	public class Asset {
	

	
	@ManyToOne
	
	@JoinColumn(name="ResourceSubType")

	@DescriptionsList(descriptionProperties="resourcetype.resourcetypename,resourcetypename")
	private ResourceSubType resourceSubType;
	
	

	@Column(name = "Status", nullable = false)
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}

	/* Unit should be set to Nullable in Cash asset database table */
	@Column(name = "Unit", length = 50, nullable=false)
	@Required
	private String unit;



	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	



}
