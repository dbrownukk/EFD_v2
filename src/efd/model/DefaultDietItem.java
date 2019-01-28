package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;


@View(members = "DefaultDietItem[#resourcesubtype;percentage,unitPrice]")
@Tab(properties="study.studyName,resourcesubtype.resourcetypename;percentage,unitPrice")


@Entity

@Table(name = "DefaultDietItem")
public class DefaultDietItem extends EFDIdentifiable {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename")
	private ResourceSubType resourcesubtype;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Study study;
	/*************************************************************************************************/
	@Column(nullable=false)
	@Required
	@Range(min=0,max=100)
	private Integer percentage;
	/*************************************************************************************************/
	@Stereotype("Money")
	@Column(nullable=false)
	@Required
	@Digits(integer = 10, fraction = 2)
	private Double unitPrice;
	/*************************************************************************************************/
	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}
	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	
	
}