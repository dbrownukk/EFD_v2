package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable


@Table(name = "employment")




public class Employment extends Asset {

	/* Note fk to resourcetype is in supertype Asset */

	@Column(name = "EmploymentName", length = 50)
	private String employmentName;

	@Column(name = "PeopleCount", nullable = false)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double peopleCount;

	@Column(name = "UnitsWorked")
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	@Positive
	private Double unitsWorked;
	
	@Column(name = "CashPaymentAmount", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double cashPaymentAmount;
	
	@ManyToOne
	@JoinColumn(name = "FoodResourceSubType")

	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename} in  ('Food Purchase','Wild Foods','Crops','Livestock Products')")
	private ResourceSubType foodResourceSubType;

	@Column(name = "FoodPaymentFoodType", length=50)
	private String foodPaymentFoodType;

	@Column(name = "FoodPaymentUnit", length=50)
	private String foodPaymentUnit;
	
	@Column(name = "FoodPaymentUnitsPaidWork", precision = 10, scale = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double foodPaymentUnitsPaidWork;
	
	@Column(name = "WorkLocation1", length = 50)
	private String workLocation1;
	
	@Column(name = "PercentWorkLocation1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentWorkLocation1;
	
	@Column(name = "WorkLocation2", length = 50)
	private String workLocation2;
	@Column(name = "PercentWorkLocation2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentWorkLocation2;
	
	@Column(name = "WorkLocation3", length = 50)
	private String workLocation3;
	@Column(name = "PercentWorkLocation3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentWorkLocation3;
	
	
	@ManyToOne

	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename} like '%Employment%'")
	private ResourceSubType resourceSubType;
	
	public String getEmploymentName() {
		return employmentName;
	}
	public void setEmploymentName(String employmentName) {
		this.employmentName = employmentName;
	}
	public Double getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(Double peopleCount) {
		this.peopleCount = peopleCount;
	}
	public Double getUnitsWorked() {
		return unitsWorked;
	}
	public void setUnitsWorked(Double unitsWorked) {
		this.unitsWorked = unitsWorked;
	}
	public Double getCashPaymentAmount() {
		return cashPaymentAmount;
	}
	public void setCashPaymentAmount(Double cashPaymentAmount) {
		this.cashPaymentAmount = cashPaymentAmount;
	}
	public String getFoodPaymentFoodType() {
		return foodPaymentFoodType;
	}
	public void setFoodPaymentFoodType(String foodPaymentFoodType) {
		this.foodPaymentFoodType = foodPaymentFoodType;
	}
	public String getFoodPaymentUnit() {
		return foodPaymentUnit;
	}
	public void setFoodPaymentUnit(String foodPaymentUnit) {
		this.foodPaymentUnit = foodPaymentUnit;
	}

	public Double getFoodPaymentUnitsPaidWork() {
		return foodPaymentUnitsPaidWork;
	}
	public void setFoodPaymentUnitsPaidWork(Double foodPaymentUnitsPaidWork) {
		this.foodPaymentUnitsPaidWork = foodPaymentUnitsPaidWork;
	}
	public String getWorkLocation1() {
		return workLocation1;
	}
	public void setWorkLocation1(String workLocation1) {
		this.workLocation1 = workLocation1;
	}
	public Double getPercentWorkLocation1() {
		return percentWorkLocation1;
	}
	public void setPercentWorkLocation1(Double percentWorkLocation1) {
		this.percentWorkLocation1 = percentWorkLocation1;
	}
	public String getWorkLocation2() {
		return workLocation2;
	}
	public void setWorkLocation2(String workLocation2) {
		this.workLocation2 = workLocation2;
	}
	public Double getPercentWorkLocation2() {
		return percentWorkLocation2;
	}
	public void setPercentWorkLocation2(Double percentWorkLocation2) {
		this.percentWorkLocation2 = percentWorkLocation2;
	}
	public String getWorkLocation3() {
		return workLocation3;
	}
	public void setWorkLocation3(String workLocation3) {
		this.workLocation3 = workLocation3;
	}
	public Double getPercentWorkLocation3() {
		return percentWorkLocation3;
	}
	public void setPercentWorkLocation3(Double percentWorkLocation3) {
		this.percentWorkLocation3 = percentWorkLocation3;
	}
	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}
	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}
	public ResourceSubType getFoodResourceSubType() {
		return foodResourceSubType;
	}
	public void setFoodResourceSubType(ResourceSubType foodResourceSubType) {
		this.foodResourceSubType = foodResourceSubType;
	}
	
	
	
	
}