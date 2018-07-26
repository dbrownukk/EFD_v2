package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.Status;
import efd.model.WealthGroupInterview.*;



@Embeddable


@Table(name = "assetfoodstock")

public class AssetFoodStock extends Asset {

	@Column(name = "FoodTypeEnteredName", length = 50)
	private String foodTypeEnteredName;
	

	@Column(name = "Quantity" )
	private Double quantity;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename} like 'Food%'")
	private ResourceSubType resourceSubType;


	public String getFoodTypeEnteredName() {
		return foodTypeEnteredName;
	}


	public void setFoodTypeEnteredName(String foodTypeEnteredName) {
		this.foodTypeEnteredName = foodTypeEnteredName;
	}


	public Double getQuantity() {
		return quantity;
	}


	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}


	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}


	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	
	

    
	
	

}
