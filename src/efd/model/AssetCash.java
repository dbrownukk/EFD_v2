package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "assetcash")

public class AssetCash extends Asset{


	@Column(name = "Currency", length = 3, nullable=false)
	@Required
	private String currencyEnteredName;
	
	@Column(name = "Amount" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal amount;

	public String getCurrencyEnteredName() {
		return currencyEnteredName;
	}

	public void setCurrencyEnteredName(String currencyEnteredName) {
		this.currencyEnteredName = currencyEnteredName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	
	
	
	






	
	
	
}
