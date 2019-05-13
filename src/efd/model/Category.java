package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@View(members = "Category[#categoryName,study,resourceSubType]")
@Tab(properties = "categoryName, study.studyName")

@Entity

@Table(name = "Category")
public class Category extends Identifiable {

	@Column(length = 45, nullable = false)
	@Required
	private String categoryName;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	@NoFrame
	private Study study;

	/*************************************************************************************************/
	@ManyToMany
	// @NewAction("")
	@CollectionView("FromCategory")
	@ListProperties("resourcetype.resourcetypename,resourcetypename")
	@JoinTable(name = "categoryInclusion")
	private Collection<ResourceSubType> resourceSubType;
	/*************************************************************************************************/

	@ManyToMany
	@NewAction("")
	private Collection<CustomReportSpec> customReportSpecs;

	/*************************************************************************************************/

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<ResourceSubType> getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(Collection<ResourceSubType> resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public Collection<CustomReportSpec> getCustomReportSpecs() {
		return customReportSpecs;
	}

	public void setCustomReportSpecs(Collection<CustomReportSpec> customReportSpecs) {
		this.customReportSpecs = customReportSpecs;
	}

}
