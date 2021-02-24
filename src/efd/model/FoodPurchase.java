package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable


@Table(name = "foodpurchase")

public class FoodPurchase extends Asset{


	@Column(name = "FoodTypeEnteredName", length = 50, nullable=true)
	//@Required
	private String foodTypeTypeEnteredName;
	
	@Column(name = "UnitsPurchased", nullable=false )
	//@Required
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsPurchased;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double pricePerUnit;

	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	
	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename} in ('Crops','Wild Foods','Livestock Products','Food Purchase','Food Stocks')")
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
