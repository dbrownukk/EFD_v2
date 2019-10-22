package efd.model;

import java.util.*;



import javax.persistence.*;
//import javax.validation.constraints.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@Views({ @View(members = "specName,warningMessage,Report{report},Category{category},ResourceType{resourceType},ResourceSubType{resourceSubType}"),
		@View(name = "rst", members = "specName,rType,report,resourceSubType"),
		@View(name = "rt", members = "specName,rType,report,resourceSubType"),
		@View(name = "cat", members = "specName,,Type,report,resourceSubType") })

@Entity

/* Unique constraints need to go in intersection tables for manytomany */



public class CustomReportSpecOHEA extends Identifiable {

	@Column(length = 45)
	@Required

	//@Pattern(regexp = "[a-z-A-Z]*", message = "Spec Name has invalid characters")
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Spec Name has invalid characters")
	
	private String specName;


	@ManyToMany
	//@JoinTable(name = "ReportInclusion")
	@NewAction("")
	@Column(unique=true)
	@NoModify
	@SearchListCondition("${method} = 0") // OHEA
	private Collection<Report> report;

	@ManyToMany
	@NewAction("")
	@Column(unique=true)
	private Collection<ResourceType> resourceType;

	@ManyToMany
	@NewAction("")
	@ListProperties("resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")
	@Column(unique=true)
	private Collection<ResourceSubType> resourceSubType;

	@ManyToMany
	@NewAction("")
	@Column(unique=true)
	private Collection<Category> category;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public Collection<Report> getReport() {
		return report;
	}

	public void setReport(Collection<Report> report) {
		this.report = report;
	}

	public Collection<ResourceType> getResourceType() {
		return resourceType;
	}

	public void setResourceType(Collection<ResourceType> resourceType) {
		this.resourceType = resourceType;
	}

	public Collection<ResourceSubType> getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(Collection<ResourceSubType> resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	


}
