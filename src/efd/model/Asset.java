package efd.model;



import javax.persistence.*;

import org.openxava.annotations.*;

@MappedSuperclass

//abstract public class Asset {
	public class Asset {
	
	@SearchAction("WealthGroupInterview.resourceSubTypeFilter")
	@AddAction("WealthGroupInterview.newResourceSubType")
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="ResourceSubType")
	//@DescriptionsList(descriptionProperties="resourcetypename")
	//@DescriptionsList(showReferenceView=true)

	@ReferenceView("SimpleSubtype")
	private ResourceSubType resourceSubType;

	@Column(name = "Status", nullable = false)
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	



}
