package efd.model;



import javax.persistence.*;

import org.openxava.annotations.*;

@MappedSuperclass

//abstract public class Asset {
	public class Asset {
	
	//@ManyToOne(optional=false)
	@ManyToOne
	@JoinColumn(name="ResourceType")
	//@Required 
	
	private ResourceType resourceType;

	@Column(name = "Status", nullable = false)
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}
	
	
	

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}



}
