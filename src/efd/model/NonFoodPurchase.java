package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable


@Table(name = "nonfoodpurchase")

public class NonFoodPurchase extends Asset{


	@Column(name = "ItemPurchased", length = 50, nullable=false)
	@Required
	private String itemPurchased;
	
	@Column(name = "UnitsPurchased", nullable=false )
	@Required
	@NotNull
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsPurchased;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double pricePerUnit;

	@ManyToOne
	
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename} in ('Non Food Purchase','Other Tradeable Goods')")
	private ResourceSubType resourceSubType;
	

	public String getItemPurchased() {
		return itemPurchased;
	}

	public void setItemPurchased(String itemPurchased) {
		this.itemPurchased = itemPurchased;
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
