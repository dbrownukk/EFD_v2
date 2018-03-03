package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable

@Table(name = "Employment")

public class Employment extends Asset {

	/* Note fk to resourcetype is in supertype Asset */

	@Column(name = "EmploymentName", length = 50)
	private String employmentName;


	@Column(name = "PeopleCount", nullable = false)
	private Integer peopleCount;

	@Column(name = "Duration")
	private Integer duration;
	
	@Column(name = "WorkUnit ", length = 45)
	private String workUnit;
	
	@Column(name = "Frequency ", length = 45)
	private String frequency;
	
	@Column(name = "CashPaymentPerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal cashPaymentPerUnit;

	@Column(name = "FoodPaymentPerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal foodPaymentPerUnit;

	public String getEmploymentName() {
		return employmentName;
	}

	public void setEmploymentName(String employmentName) {
		this.employmentName = employmentName;
	}

	public Integer getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getCashPaymentPerUnit() {
		return cashPaymentPerUnit;
	}

	public void setCashPaymentPerUnit(BigDecimal cashPaymentPerUnit) {
		this.cashPaymentPerUnit = cashPaymentPerUnit;
	}

	public BigDecimal getFoodPaymentPerUnit() {
		return foodPaymentPerUnit;
	}

	public void setFoodPaymentPerUnit(BigDecimal foodPaymentPerUnit) {
		this.foodPaymentPerUnit = foodPaymentPerUnit;
	}

	
	
}