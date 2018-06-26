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
	private Integer unitsPurchased;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;

	public String getFoodTypeTypeEnteredName() {
		return foodTypeTypeEnteredName;
	}

	public void setFoodTypeTypeEnteredName(String foodTypeTypeEnteredName) {
		this.foodTypeTypeEnteredName = foodTypeTypeEnteredName;
	}

	public Integer getUnitsPurchased() {
		return unitsPurchased;
	}

	public void setUnitsPurchased(Integer unitsPurchased) {
		this.unitsPurchased = unitsPurchased;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}


	
}
