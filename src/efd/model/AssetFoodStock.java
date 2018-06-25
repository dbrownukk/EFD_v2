package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.Status;
import efd.model.WealthGroupInterview.*;



@Embeddable


@Table(name = "Asset_FoodStock")

public class AssetFoodStock extends Asset {

	@Column(name = "FoodTypeEnteredName", length = 50)
	private String foodTypeEnteredName;
	

	@Column(name = "Quantity" )
	private BigDecimal quantity;


	public String getFoodTypeEnteredName() {
		return foodTypeEnteredName;
	}


	public void setFoodTypeEnteredName(String foodTypeEnteredName) {
		this.foodTypeEnteredName = foodTypeEnteredName;
	}


	public BigDecimal getQuantity() {
		return quantity;
	}


	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}


    
	
	

}
