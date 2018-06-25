package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.*;



@Embeddable


@Table(name = "Asset_Land")

public class AssetLand extends Asset{
	
	@Column(name = "LandTypeEnteredName", length = 50)
	private String landTypeEnteredName;

	@Column(name = "NumberofUnits", nullable=false )
	@NotNull
	private Integer numberOfUnits;	

	public String getLandTypeEnteredName() {
		return landTypeEnteredName;
	}

	public void setLandTypeEnteredName(String landTypeEnteredName) {
		this.landTypeEnteredName = landTypeEnteredName;
	}

	public Integer getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(Integer numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}








}
