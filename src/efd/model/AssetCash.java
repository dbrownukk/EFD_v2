package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable

@Table(name = "assetcash")

public class AssetCash extends Asset {

	@Column(name = "Currency", length = 3, nullable = false)
	// @Required Bug in elementcollection
	@DisplaySize(10)
	private String currencyEnteredName;

	@Column(name = "Amount")
	@Depends("currencyEnteredAmount,exchangeRate")
	public BigDecimal getAmount() {
		return currencyEnteredAmount.multiply(exchangeRate);
	}

	@DisplaySize(value = 10)
	@Positive
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@Column(name = "CurrencyEnteredAmount")
	private BigDecimal currencyEnteredAmount;

	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties = "resourcetypename,resourcesubtypeunit", condition = "${resourcetype.resourcetypename}='Cash'")
	private ResourceSubType resourceSubType;

	@Column(precision = 10, scale = 5)
	@Digits(integer = 10, fraction = 5)
	@Positive
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)

	private BigDecimal exchangeRate;

	public BigDecimal getExchangeRate() {
		return exchangeRate == null ? BigDecimal.ZERO : exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyEnteredName() {
		return currencyEnteredName;
	}

	public void setCurrencyEnteredName(String currencyEnteredName) {
		this.currencyEnteredName = currencyEnteredName;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public BigDecimal getCurrencyEnteredAmount() {
		return currencyEnteredAmount;
	}

	public void setCurrencyEnteredAmount(BigDecimal currencyEnteredAmount) {
		this.currencyEnteredAmount = currencyEnteredAmount;
	}

}
