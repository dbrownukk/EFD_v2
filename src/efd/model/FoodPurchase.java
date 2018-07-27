package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "foodpurchase")

public class FoodPurchase extends Asset{


	@Column(name = "FoodTypeEnteredName", length = 50, nullable=false)
	@Required
	private String foodTypeTypeEnteredName;
	
	@Column(name = "UnitsPurchased", nullable=false )
	@Required
	@NotNull
	private Double unitsPurchased;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private Double pricePerUnit;

	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename} in ('Crops','Wild Foods','Livestock Products','Food Purchase')")
	private ResourceSubType resourceSubType;
	
	public String getFoodTypeTypeEnteredName() {
		return foodTypeTypeEnteredName;
	}

	public void setFoodTypeTypeEnteredName(String foodTypeTypeEnteredName) {
		this.foodTypeTypeEnteredName = foodTypeTypeEnteredName;
	}

	public Double getUnitsPurchased() {
		return unitsPurchased;
	}

	public void setUnitsPurchased(Double unitsPurchased) {
		this.unitsPurchased = unitsPurchased;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}



	
}
