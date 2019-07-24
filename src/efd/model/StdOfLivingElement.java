package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.omg.CORBA.DynAnyPackage.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

import efd.model.ConfigQuestion.*;
import efd.model.HouseholdMember.*;
import efd.validations.*;

@View(members = "StandardOfLivingElement[#resourcesubtype;amount,cost;level,gender;ageRangeLower,ageRangeUpper]")
@Tab(properties = "resourcetype.resourcetypename,resourcesubtype.resourcetypename;amount,cost,level")

@Entity

@Table(name = "StdOfLivingElement")

public class StdOfLivingElement extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		if ((level == StdLevel.HouseholdMember) && gender == null) {
			System.out.println("HHM in validate HHM with no gender");
			throw new IllegalStateException(

					XavaResources.getString("hhm_needs_gender"));

		}

	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	// @NoFrame
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename", condition = "${resourcetype.resourcetypename} = 'Non Food Purchase'")
	@NoCreate
	
	@NoModify
	private ResourceSubType resourcesubtype;
	/*************************************************************************************************/
	@Required
	@Column(nullable = false)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@Min(1)
	private Double amount;
	// number of instances of the Resource sub type in whatever it’s unit of measure
	// is (e.g. 100 Litres Kerosene) would have Amount 100
	/*************************************************************************************************/
	@Column(precision = 10, scale = 3, nullable = false)
	@Required
	@Digits(integer = 10, fraction = 3)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@Min(1)
	private Double cost;
	/*************************************************************************************************/
	@Column(nullable = false)
	@Required
	@OnChange(value = OnChangeSOLLevel.class)
	private StdLevel level;
	
	public enum StdLevel {
		Household, HouseholdMember
	}
	
	
	/*************************************************************************************************/

	//@Editor("ValidValuesRadioButton")
	
	private Gender gender;

	public enum Gender {
		Male, Female, Both
	}

	/*************************************************************************************************/
	
	
	@DefaultValueCalculator(value=IntegerCalculator.class, properties=@PropertyValue(name="value", value="0"))
	@Min(0)
	@Max(100)
	
	private int ageRangeLower;
	/*************************************************************************************************/
	
	
	@DefaultValueCalculator(value=IntegerCalculator.class, properties=@PropertyValue(name="value", value="110"))
	@Min(0)
	@Max(110)

	private int ageRangeUpper;

	/*************************************************************************************************/
	public Study getStudy() {
		return study;
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