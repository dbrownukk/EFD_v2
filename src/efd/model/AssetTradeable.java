package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable


@Table(name = "Asset_Tradeable")

public class AssetTradeable extends Asset{



	@Column(name = "TradeableTypeEnteredName", length = 50)
	private String tradeableTypeEnteredName;

	@Column(name = "Quantity", nullable=false )
	@NotNull
	private Integer quantity;


	public String getTradeableTypeEnteredName() {
		return tradeableTypeEnteredName;
	}

	public void setTradeableTypeEnteredName(String tradeableTypeEnteredName) {
		this.tradeableTypeEnteredName = tradeableTypeEnteredName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
