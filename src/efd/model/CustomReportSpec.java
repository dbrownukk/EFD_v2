package efd.model;

import java.util.*;

import javax.persistence.*;
//import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@Views({ @View(members = "specName,totalQuantilePercentage,warningMessage,quantile,reportSpecUse,report, Category{category},ResourceType{resourceType},ResourceSubType{resourceSubType}"),
		@View(name = "rst", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "rt", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "cat", members = "specName,quantile,rType,report,resourceSubType") })

@Entity

/* check that only one of Cat/RT/RST is used */
//@EntityValidator(value = SingleResourceValidation.class, properties = { @PropertyValue(name = "category"),
//		@PropertyValue(name = "resourceType"), @PropertyValue(name = "resourceSubType") }) 

public class CustomReportSpec extends Identifiable {

	@Column(length = 45)
	@Required
	private String specName;

	@OneToMany(mappedBy = "customReportSpec")
	private Collection<Quantile> quantile;

	@ManyToMany
	@JoinTable(name = "ReportInclusion")
	@NewAction("ManyToMany.new")
	private Collection<Report> report;

	@ManyToMany
	@NewAction("")
	private Collection<ResourceType> resourceType;

	@ManyToMany
	@NewAction("")
	@ListProperties("resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")
	private Collection<ResourceSubType> resourceSubType;

	@ManyToMany
	@NewAction("")
	private Collection<Category> category;

	@OneToMany(mappedBy = "customReportSpec")
	@ListProperties("study.studyName,study.referenceYear")
	private Collection<ReportSpecUse> reportSpecUse;

	// ----------------------------------------------------------------------------------------------//
	// Is quantile total above or not equal to 100%
	// ----------------------------------------------------------------------------------------------//

	@Transient
	@ReadOnly
	@Column(length = 3)


	public int getTotalQuantilePercentage() {
		int result = 0;

		for (Quantile quantile : getQuantile()) {
			result += quantile.getPercentage();
		}
		return result;
	}

	
	@Depends("totalQuantilePercentage")
	@Stereotype("LABEL")
	@Transient
	@ReadOnly
	public String getWarningMessage() {
		if (getTotalQuantilePercentage() > 100) {

			return "<font color=" + "red" + ">Total Quantile Percent is greater than 100</font>";

		} else if (getTotalQuantilePercentage() < 100) {

			return "<font color=" + "orange" + ">Total Quantile Percent is less than 100</font>";
		} else if (getTotalQuantilePercentage() == 100) {
			return "<font color=" + "green" + ">Total Quantile Percent is 100</font>";
		}
		return null;
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

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public Collection<ReportSpecUse> getReportSpecUse() {
		return reportSpecUse;
	}

	public void setReportSpecUse(Collection<ReportSpecUse> reportSpecUse) {
		this.reportSpecUse = reportSpecUse;
	}

	public Collection<Quantile> getQuantile() {
		return quantile;
	}

	public void setQuantile(Collection<Quantile> quantile) {
		this.quantile = quantile;
	}

}
