package efd.model;

import efd.validations.OnChangeAssetStatus;
import efd.validations.UnitCalculator;
import org.openxava.annotations.*;
import org.openxava.calculators.EnumCalculator;
import org.openxava.calculators.FalseCalculator;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass

public class Asset {

	@Column(name = "Status", nullable = false)
	@OnChange(OnChangeAssetStatus.class)
	@DisplaySize(15)
	@Required
	@Editor(value = "ValidValuesCombo")//ValidValuesHorizontalRadioButton

	@DefaultValueCalculator(value = EnumCalculator.class, properties = {
			@PropertyValue(name = "enumType", value = "efd.model.Asset$Status"),
			@PropertyValue(name = "value", value = "Invalid") })
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}

	/* Unit should be set to Nullable in Cash asset database table */
	@DefaultValueCalculator(value = UnitCalculator.class, properties = {
			@PropertyValue(name = "string", value = "?") })
	@Column(name = "Unit", length = 50, nullable = false)
	// @Required
	// @OnChange(OnChangeUnit.class)
	@DisplaySize(10)
	private String unit;

	// Due to a Hibernate ddl generation bug am using localunit and localunit
	// multiplier here instead of efd.model.LocalUnit

	@Hidden
	@Column(name = "LocalUnit", length = 32, nullable = true)
	private String localUnit;

	@Hidden
	@Column(name = "LocalUnitMultiplier", nullable = true)
	private Double localUnitMultiplier;

	@Transient
	
	@DefaultValueCalculator(FalseCalculator.class)
	private Boolean isFilteredOut;
	
	
	
	public String getLocalUnit() {
		return localUnit;
	}

	public void setLocalUnit(String localUnit) {
		this.localUnit = localUnit;
	}

	public Double getLocalUnitMultiplier() {
		return localUnitMultiplier;
	}

	public void setLocalUnitMultiplier(Double localUnitMultiplier) {
		this.localUnitMultiplier = localUnitMultiplier;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}



	/**
	 * @return the isFilteredOut
	 */
	public Boolean getIsFilteredOut() {
		//return isFilteredOut==null?false:isFilteredOut;
		return isFilteredOut;
	}

	/**
	 * @param isFilteredOut the isFilteredOut to set
	 */
	public void setIsFilteredOut(Boolean isFilteredOut) {
		this.isFilteredOut = isFilteredOut;
	}


	
	

	
	
}
