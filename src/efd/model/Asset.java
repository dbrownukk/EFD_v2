package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.actions.*;
import efd.validations.*;



@MappedSuperclass

// abstract public class Asset {
public class Asset {


	@Column(name = "Status", nullable = false)
//	@OnChange(OnChangeAssetStatus.class)
	private Status status;

	public enum Status {
		Invalid, NotChecked, Valid
	}

	/* Unit should be set to Nullable in Cash asset database table */
	@DefaultValueCalculator(
			value=org.openxava.calculators.StringCalculator.class,
			properties={ @PropertyValue(name="string", value="?") }
			)
	@Column(name = "Unit", length = 50, nullable = false)
	@Required
	private String unit;

	

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
