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

	@Column(name = "LandArea", nullable=false )
	@NotNull
	private Integer landArea;

	@Column(name = "UnitEntered", length = 50)
	private String unitEntered;


	

	public String getLandTypeEnteredName() {
		return landTypeEnteredName;
	}

	public void setLandTypeEnteredName(String landTypeEnteredName) {
		this.landTypeEnteredName = landTypeEnteredName;
	}

	public Integer getLandArea() {
		return landArea;
	}

	public void setLandArea(Integer landArea) {
		this.landArea = landArea;
	}

	public String getUnitEntered() {
		return unitEntered;
	}

	public void setUnitEntered(String unitEntered) {
		this.unitEntered = unitEntered;
	}





}
