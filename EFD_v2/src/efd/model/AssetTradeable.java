package efd.model;

import java.math.*;

/* Asset Other Tradeable */


import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable


@Table(name = "assettradeable")

public class AssetTradeable extends Asset{



	@Column(name = "TradeableTypeEnteredName", length = 50)
	private String tradeableTypeEnteredName;

	@Column(name = "NumberOwned", nullable=false )
	@NotNull
	private Double numberOwned;

	@Column(name = "PricePerUnit" )
	private Double pricePerUnit;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename} like '%Tradeable%'")
	private ResourceSubType resourceSubType;

	public String getTradeableTypeEnteredName() {
		return tradeableTypeEnteredName;
	}

	public void setTradeableTypeEnteredName(String tradeableTypeEnteredName) {
		this.tradeableTypeEnteredName = tradeableTypeEnteredName;
	}

	public Double getNumberOwned() {
		return numberOwned;
	}

	public void setNumberOwned(Double numberOwned) {
		this.numberOwned = numberOwned;
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