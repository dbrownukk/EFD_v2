package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.*;



@Embeddable


@Table(name = "Asset_LiveStock")

public class AssetLiveStock extends Asset{


	@Column(name = "LiveStockTypeEnteredName", length = 50)
	private String liveStockTypeEnteredName;

	@Column(name = "NumberOwned", nullable=false )
	@NotNull
	private Integer numberOwned;
	
	@Column(name = "PricePerUnit" )
	private BigDecimal pricePerUnit;
	
	@Column(name = "UnitEntered", length = 50)
	private String unitEntered;

	public String getLiveStockTypeEnteredName() {
		return liveStockTypeEnteredName;
	}

	public void setLiveStockTypeEnteredName(String liveStockTypeEnteredName) {
		this.liveStockTypeEnteredName = liveStockTypeEnteredName;
	}

	public Integer getNumberOwned() {
		return numberOwned;
	}

	public void setNumberOwned(Integer numberOwned) {
		this.numberOwned = numberOwned;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getUnitEntered() {
		return unitEntered;
	}

	public void setUnitEntered(String unitEntered) {
		this.unitEntered = unitEntered;
	}



	
	
	
}
