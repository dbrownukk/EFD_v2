package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.actions.*;
import efd.validations.*;

import efd.model.*;



@MappedSuperclass


public class Asset {


	@Column(name = "Status", nullable = false)
	@OnChange(OnChangeAssetStatus.class)
	@DisplaySize(15)
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}

	/* Unit should be set to Nullable in Cash asset database table */
	@DefaultValueCalculator(
			value=efd.validations.UnitCalculator.class,
			properties={ @PropertyValue(name="string", value="?") }
			)
	@Column(name = "Unit", length = 50, nullable = false)
	@Required
	//@OnChange(OnChangeUnit.class)
	private String unit;
	
	
	// Due to a Hibernate ddl generation bug am using localunit and localunit multiplier here instead of efd.model.LocalUnit
	
	@Hidden
	@Column(name = "LocalUnit", length = 32, nullable = true)
	private String localUnit;
	
	@Hidden
	@Column(name = "LocalUnitMultiplier", nullable = true)
	private Double localUnitMultiplier;
	


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
	


	


}
