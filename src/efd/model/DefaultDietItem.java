package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import efd.model.ConfigQuestion.*;

@View(members = "DefaultDietItem[resourcesubtype;percentage,unitPrice]")

@Tab(properties = "resourcesubtype.resourcetypename;percentage,unitPrice")

@Entity

@Table(name = "DefaultDietItem")
public class DefaultDietItem extends EFDIdentifiable {

	// @PrePersist
	@PreUpdate
	private void validate() throws Exception {
		
		if (study != null) {
			System.out.println("tot = " + study.getTotalDietPercentage());
			if (study.getTotalDietPercentage() > 100) {
				throw new IllegalStateException(

						XavaResources.getString("diet_greater_100"));

			}
		}
		else if (community != null) {
			System.out.println("tot = "+ community.getDdipercenttotal());
			if (community.getDdipercenttotal() > 100) {
				throw new IllegalStateException(

						XavaResources.getString("diet_greater_100"));

			}
		}
		if ((study == null && community == null) || (study != null && community != null)) {
			throw new IllegalStateException(

					XavaResources.getString("Default Diet Item must belong to a Study or a Community"));

		}
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename", condition = "e.resourcetype.resourcetypename in ('Crops','Wild Foods','Livestock Products','Food Purchase','Food Stocks')")
	@NoCreate
	@NoModify
	private ResourceSubType resourcesubtype;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	// @ReferenceView("FromStdOfLiving")
	private Community community;
	/*************************************************************************************************/

	@Column(nullable = false)
	@Required
	@Range(min = 0, max = 100)
	@DefaultValueCalculator(value = ZeroIntegerCalculator.class)
	private Integer percentage;
	/*************************************************************************************************/
	// @Stereotype("Money")
	@Column(nullable = false)
	@Required
	@DefaultValueCalculator(value = ZeroBigDecimalCalculator.class)
	@Digits(integer = 10, fraction = 2)
	@Positive
	private BigDecimal unitPrice;

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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	/*************************************************************************************************/

}