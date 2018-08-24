package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.*;



@Embeddable


@Table(name = "assetland")

public class AssetLand extends Asset{
	
	@Column(name = "LandTypeEnteredName", length = 50)
	private String landTypeEnteredName;

	@Column(name = "NumberofUnits", nullable=false )
	@NotNull
	private Double numberOfUnits;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename}='Land'")
	private ResourceSubType resourceSubType;

	public String getLandTypeEnteredName() {
		return landTypeEnteredName;
	}

	public void setLandTypeEnteredName(String landTypeEnteredName) {
		this.landTypeEnteredName = landTypeEnteredName;
	}

	public Double getNumberOfUnits() {
		return numberOfUnits;
	}

	public void setNumberOfUnits(Double numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}	










}
