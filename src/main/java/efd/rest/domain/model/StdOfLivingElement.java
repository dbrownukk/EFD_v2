package efd.rest.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.OnChange;
import org.openxava.annotations.PropertyValue;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;
import org.openxava.calculators.IntegerCalculator;
import org.openxava.calculators.ZeroBigDecimalCalculator;
import org.openxava.util.XavaResources;

import efd.validations.OnChangeSOLLevel;

@View(members = "StandardOfLivingElement[#resourcesubtype;amount,cost;level,gender;ageRangeLower,ageRangeUpper]")
@View(name="fromCommunity", members = "StandardOfLivingElement[#resourcesubtype;amount,cost;level,survival]")

@Tab(properties = "resourcetype.resourcetypename,resourcesubtype.resourcetypename;amount,cost,level")

@Entity

@Table(name = "StdOfLivingElement")

public class StdOfLivingElement extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		if ((level == StdLevel.HouseholdMember) && gender == null && (study != null)) {
			System.out.println("HHM in validate HHM with no gender");
			throw new IllegalStateException(

					XavaResources.getString("hhm_needs_gender"));

		}
		if (study == null && community == null) {
			throw new IllegalStateException(

					XavaResources.getString("Study or Community must be populated"));

		}


	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename", condition = "${resourcetype.resourcetypename} in ('Non Food Purchase','Food Purchase','Other Tradeable Goods')")
	@NoCreate

	@NoModify
	private ResourceSubType resourcesubtype;
	/*************************************************************************************************/
	@Required
	@Column(nullable = false)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@Positive
	private Double amount;
	// number of instances of the Resource sub type in whatever it’s unit of measure
	// is (e.g. 100 Litres Kerosene) would have Amount 100
	/*************************************************************************************************/
	@Column(precision = 10, scale = 3, nullable = false)
	@Required
	@Digits(integer = 10, fraction = 3)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@Positive
	private Double cost;
	/*************************************************************************************************/
	@Column(nullable = false)
	@Required
	@OnChange(notForViews="fromCommunity", value = OnChangeSOLLevel.class) // no need to get age and gender for Community STOL
	private StdLevel level;

	
	public enum StdLevel {
		Household, HouseholdMember
	}

	/*************************************************************************************************/

	// @Editor("ValidValuesRadioButton")

	private Gender gender;

	public enum Gender {
		Male, Female, Both
	}

	/*************************************************************************************************/

	@DefaultValueCalculator(value = IntegerCalculator.class, properties = @PropertyValue(name = "value", value = "0"))
	@Min(0)
	@Max(100)

	private int ageRangeLower;
	/*************************************************************************************************/

	@DefaultValueCalculator(value = IntegerCalculator.class, properties = @PropertyValue(name = "value", value = "110"))
	@Min(0)
	@Max(110)

	private int ageRangeUpper;

	/*************************************************************************************************/
	/*
	 * Need to add new attribute to support OHEA - Survival % and many to one to
	 * Community
	 * 
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Community community;

	@DefaultValueCalculator(value = org.openxava.calculators.IntegerCalculator.class, properties = {
			@PropertyValue(name = "value", value = "100") })

	@Min(0)
	@Max(100)

	private Integer survival;

	/*************************************************************************************************/

	public Study getStudy() {
		return study;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Integer getSurvival() {
		return survival;
	}

	public void setSurvival(Integer survival) {
		this.survival = survival;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}

	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public StdLevel getLevel() {
		return level;
	}

	public void setLevel(StdLevel level) {
		this.level = level;
	}

	public void setAgeRangeLower(int ageRangeLower) {
		this.ageRangeLower = ageRangeLower;
	}

	public void setAgeRangeUpper(int ageRangeUpper) {
		this.ageRangeUpper = ageRangeUpper;
	}

	public Integer getAgeRangeLower() {
		return ageRangeLower;
	}

	public void setAgeRangeLower(Integer ageRangeLower) {
		this.ageRangeLower = ageRangeLower;
	}

	public Integer getAgeRangeUpper() {
		return ageRangeUpper;
	}

	public void setAgeRangeUpper(Integer ageRangeUpper) {
		this.ageRangeUpper = ageRangeUpper;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

}