package efd.model;

import java.util.*;

import javax.persistence.*;

import org.apache.poi.util.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;

import efd.model.ConfigQuestion.*;
import efd.model.WealthGroupInterview.*;
import efd.validations.*;

@Views({ @View(members = "specName,quantile,rType,report, Include{resourceType,category,resourceSubType}"),
		@View(name = "rst", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "rt", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "cat", members = "specName,quantile,rType,report,resourceSubType") })

@Entity

public class CustomReportSpec extends Identifiable {

	@Column(length = 45)
	private String specName;

	@Required
	@Column(nullable = false)

	private Quantile quantile;

	public enum Quantile {
		NoGrouping, Tercile, Quartile, Quintile
	}

	@Required
	@Column(nullable = false)
	//@OnChange(OnChangeRType.class)

	private RType rType;

	public enum RType {
		ResourceSubType, ResourceType, Category
	}

	@ManyToMany
	@JoinTable(name = "ReportInclusion")
	private Collection<Report> report;

	@ManyToMany
	@NewAction("")
	@JoinTable(name = "ReportSpecResourceInclusion")
	private Collection<ResourceType> resourceType;

	@ManyToMany
	@NewAction("")
	@JoinTable(name = "ReportSpecResourceInclusion")
	private Collection<ResourceSubType> resourceSubType;

	@ManyToMany
	@NewAction("")
	@JoinTable(name = "ReportSpecResourceInclusion")
	private Collection<Category> category;

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

	public Quantile getQuantile() {
		return quantile;
	}

	public void setQuantile(Quantile quantile) {
		this.quantile = quantile;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public RType getrType() {
		return rType;
	}

	public void setrType(RType rType) {
		this.rType = rType;
	}

}
