package efd.model;

import java.math.*;

/* Asset Other Tradeable */


import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable


@Table(name = "Asset_Tradeable")

public class AssetTradeable extends Asset{



	@Column(name = "TradeableTypeEnteredName", length = 50)
	private String tradeableTypeEnteredName;

	@Column(name = "NumberOwned", nullable=false )
	@NotNull
	private Integer numberOwned;

	@Column(name = "PricePerUnit" )
	private BigDecimal pricePerUnit;

	public String getTradeableTypeEnteredName() {
		return tradeableTypeEnteredName;
	}

	public void setTradeableTypeEnteredName(String tradeableTypeEnteredName) {
		this.tradeableTypeEnteredName = tradeableTypeEnteredName;
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




	
	
}
