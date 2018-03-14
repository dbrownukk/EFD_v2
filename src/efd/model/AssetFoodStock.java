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

	
	/* Boolean or either / or ?? */
	

	
	
	//@Editor("ValidValuesRadioButton")
	private WildFoodOrCropIndicator wildFoodOrCropIndicator;
	public enum WildFoodOrCropIndicator { Wildfood, Crop };
	
	
	@Column(name = "FoodName", nullable=false, length=50 )
	private String foodName;



	public WildFoodOrCropIndicator getWildFoodOrCropIndicator() {
		return wildFoodOrCropIndicator;
	}


	public void setWildFoodOrCropIndicator(WildFoodOrCropIndicator wildFoodOrCropIndicator) {
		this.wildFoodOrCropIndicator = wildFoodOrCropIndicator;
	}


	public String getFoodName() {
		return foodName;
	}


	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}




	
	

}
