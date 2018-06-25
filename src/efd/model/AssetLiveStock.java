package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;




//@Entity
@Embeddable


@Table(name = "Asset_LiveStock")

public class AssetLiveStock extends Asset{


	@Column(name = "LiveStockTypeEnteredName", length = 50, nullable=false)
	@Required
	private String liveStockTypeEnteredName;
	
	@Column(name = "NumberOwnedAtStart", nullable=false )
	@Required
	@NotNull
	private Integer numberOwnedAtStart;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;

	public String getLiveStockTypeEnteredName() {
		return liveStockTypeEnteredName;
	}

	public void setLiveStockTypeEnteredName(String liveStockTypeEnteredName) {
		this.liveStockTypeEnteredName = liveStockTypeEnteredName;
	}



	public Integer getNumberOwnedAtStart() {
		return numberOwnedAtStart;
	}

	public void setNumberOwnedAtStart(Integer numberOwnedAtStart) {
		this.numberOwnedAtStart = numberOwnedAtStart;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	

	
	
}
