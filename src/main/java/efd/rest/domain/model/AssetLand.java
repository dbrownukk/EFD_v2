package efd.rest.domain.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable

@Table(name = "assetland")

public class AssetLand extends Asset {

	@PrePersist

	private void preSetUnit() { // set as default Acre 

		System.out.println("in prepersist assetland ");
		this.setUnit("Acre");
		System.out.println("done prepersist assetland ");
	}

	@Column(name = "LandTypeEnteredName", length = 50)
	private String landTypeEnteredName;

	@Column(name = "NumberofUnits", nullable = false)
	@NotNull
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double numberOfUnits;

	@ManyToOne(fetch = FetchType.LAZY)

	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties = "resourcetypename,resourcesubtypeunit", condition = "${resourcetype.resourcetypename}='Land'")

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
