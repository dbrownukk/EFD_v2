package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.omg.CORBA.DynAnyPackage.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

import efd.model.ConfigQuestion.*;
import efd.model.HouseholdMember.*;

@View(members = "StandardOfLivingElement[#resourcesubtype;amount,cost;level,gender;ageRangeLower,ageRangeUpper]")
@Tab(properties="resourcesubtype.resourcetypename;amount,cost,level,gender,ageRangeLower,ageRangeUpper")


@Entity


@Table(name = "StdOfLivingElement")



public class StdOfLivingElement extends EFDIdentifiable{

	
	@PrePersist
	@PreUpdate
	private void validate() throws Exception{

		if((level == Level.HouseholdMember) && gender == null)
		{
			System.out.println("HHM in validate HHM with no gender");
			throw new IllegalStateException(
					
							
					XavaResources.getString(  
							"hhm_needs_gender"));
							
					}
					
			
		}

	
	
	
	
	
	
	

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	//@NoFrame
	@NoCreate
	@NoModify
	@ReferenceView("FromStdOfLiving")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename",
	condition="${resourcetype.resourcetypename} = 'Non Food Purchase'")
	
	
	private ResourceSubType resourcesubtype;
	/*************************************************************************************************/
	@Required
	@Column(length=45,nullable=false)
	private String amount;
	//number of instances of the Resource sub type in whatever it’s unit of measure is (e.g. 100 Litres Kerosene) would have Amount 100  
	/*************************************************************************************************/
	@Column(precision = 10, scale = 3,nullable=false)
	@Required
	@Digits(integer = 10, fraction = 3)
	private Double cost;
	/*************************************************************************************************/
	@Column(nullable = false)
	@Required
	private Level level;
	/*************************************************************************************************/
	
	@Editor("ValidValuesRadioButton")
	private Gender gender;
	
	public enum Gender {
		Male, Female, Both
	}
	
	
	
	
	
	/*************************************************************************************************/
	@Range(min=1,max=120)
	private Integer ageRangeLower;
	/*************************************************************************************************/
	@Range(min=1,max=120)
	private Integer ageRangeUpper;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
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