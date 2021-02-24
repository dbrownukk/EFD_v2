package efd.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
//import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.ws.rs.DELETE;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.UniqueElements;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;
import org.openxava.model.*;

import static java.util.Collections.*;

@Views({ @View(members = "specName,Report{report},Category{category},ResourceType{resourceType},ResourceSubType{resourceSubType}"),
		@View(name = "rst", members = "specName,rType,report,resourceSubType"),
		@View(name = "rt", members = "specName,rType,report,resourceSubType"),
		@View(name = "cat", members = "specName,,Type,report,resourceSubType") })

@Entity
//@Tab(defaultOrder = "${report.code}")
/* Unique constraints need to go in intersection tables for manytomany */

public class CustomReportSpecOHEA extends Identifiable {

	@Column(length = 45)
	@Required
	@NotNull

	// @Pattern(regexp = "[a-z-A-Z]*", message = "Spec Name has invalid characters")
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Spec Name has invalid characters")

	private String specName;

	@ManyToMany
	// @JoinTable(name = "ReportInclusion")
	@RemoveSelectedAction("")
	@ListAction("CustomReportSpecOHEA.goRemoveReports")
	@NewAction("")
	@Column(unique = true)
	@NoModify
	@SearchListCondition("${method} = 0 and ${code}>10 ") // OHEA
	@AddAction("CustomReportSpecOHEA.goAddReports")
	@ListProperties("name,code,method,isMandatory")
	private @UniqueElements Set<Report> report;

	@ManyToMany
	@NewAction("")
	@Column(unique = true)
	private Collection<ResourceType> resourceType;

	@ManyToMany
	@NewAction("")
	@ListProperties("resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")
	@Column(unique = true)
	private Collection<ResourceSubType> resourceSubType;

	@ManyToMany
	@NewAction("")
	@Column(unique = true)
	private Collection<Category> category;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	// Order the Report Set
	public Set<Report> getReport() {
		List<Report> repToOrder = null;
 
		if (CollectionUtils.emptyIfNull(this.report).isEmpty()) {
			// Add mandatory reps for OHEA (Method = 0)
			repToOrder = XPersistence.getManager().createQuery("from Report where isMandatory = 'Y' and Method = 0 ")
					.getResultList();
		}

		else {
			repToOrder = this.report.stream().collect(Collectors.toList());
		}

		repToOrder.sort(Comparator.comparing(Report::getCode));
		report = new LinkedHashSet<>(repToOrder);

		return report;
	}

	public void setReport(Set<Report> report) {
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
