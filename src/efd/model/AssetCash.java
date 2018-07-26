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
	private Double amount;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename}='Cash'")
	private ResourceSubType resourceSubType;

	public String getCurrencyEnteredName() {
		return currencyEnteredName;
	}

	public void setCurrencyEnteredName(String currencyEnteredName) {
		this.currencyEnteredName = currencyEnteredName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}




	
	
	
	






	
	
	
}
