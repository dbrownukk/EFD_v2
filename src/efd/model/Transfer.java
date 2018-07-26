package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import efd.model.Asset.*;



@Embeddable


@Table(name = "transfer")

public class Transfer extends Asset{

	@Column(name = "Official", nullable = false)
	private Boolean isOfficial;

	@Column(name = "Source", length = 50)
	private String source;
	
	@Column(name = "TransferType", length = 50)
	private String transferType;

	@Column(name = "PeopleReceiving")
	private Double peopleReceiving;
	
	@Column(name = "TimesReceived")
	private Double timesReceived;
	
	@Column(name = "CashTransferAmount" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private Double cashTransferAmount;
	
	@Column(name = "TransferFoodOtherType", length = 50)
	private String transferFoodOtherType;
	
	@Column(name = "UnitesTransferred")
	private Double unitsTransferred;
	
	@Column(name = "UnitsSold")
	private Double unitsSold;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private Double pricePerUnit;
	
	@Column(name = "OtherUse", length = 255)
	private Double otherUse;
	
	@Column(name = "UnitsConsumed")
	private Double unitsConsumed;
	
	@Column(name = "Market1", length = 50)
	private String market1;
	@Column(name = "PercentTradeMarket1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket1;
	
	@Column(name = "Market2", length = 50)
	private String market2;
	@Column(name = "PercentTradeMarket2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket2;
	
	@Column(name = "Market3", length = 50)
	private String market3;
	@Column(name = "PercentTradeMarket3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket3;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename} like '%Transfer%'")
	private ResourceSubType resourceSubType;
	
	
	public Boolean getIsOfficial() {
		return isOfficial;
	}
	public void setIsOfficial(Boolean isOfficial) {
		this.isOfficial = isOfficial;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public Double getPeopleReceiving() {
		return peopleReceiving;
	}
	public void setPeopleReceiving(Double peopleReceiving) {
		this.peopleReceiving = peopleReceiving;
	}
	public Double getTimesReceived() {
		return timesReceived;
	}
	public void setTimesReceived(Double timesReceived) {
		this.timesReceived = timesReceived;
	}
	public Double getCashTransferAmount() {
		return cashTransferAmount;
	}
	public void setCashTransferAmount(Double cashTransferAmount) {
		this.cashTransferAmount = cashTransferAmount;
	}
	public String getTransferFoodOtherType() {
		return transferFoodOtherType;
	}
	public void setTransferFoodOtherType(String transferFoodOtherType) {
		this.transferFoodOtherType = transferFoodOtherType;
	}
	public Double getUnitsTransferred() {
		return unitsTransferred;
	}
	public void setUnitsTransferred(Double unitsTransferred) {
		this.unitsTransferred = unitsTransferred;
	}
	public Double getUnitsSold() {
		return unitsSold;
	}
	public void setUnitsSold(Double unitsSold) {
		this.unitsSold = unitsSold;
	}
	public Double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Double getOtherUse() {
		return otherUse;
	}
	public void setOtherUse(Double otherUse) {
		this.otherUse = otherUse;
	}
	public Double getUnitsConsumed() {
		return unitsConsumed;
	}
	public void setUnitsConsumed(Double unitsConsumed) {
		this.unitsConsumed = unitsConsumed;
	}
	public String getMarket1() {
		return market1;
	}
	public void setMarket1(String market1) {
		this.market1 = market1;
	}
	public Double getPercentTradeMarket1() {
		return percentTradeMarket1;
	}
	public void setPercentTradeMarket1(Double percentTradeMarket1) {
		this.percentTradeMarket1 = percentTradeMarket1;
	}
	public String getMarket2() {
		return market2;
	}
	public void setMarket2(String market2) {
		this.market2 = market2;
	}
	public Double getPercentTradeMarket2() {
		return percentTradeMarket2;
	}
	public void setPercentTradeMarket2(Double percentTradeMarket2) {
		this.percentTradeMarket2 = percentTradeMarket2;
	}
	public String getMarket3() {
		return market3;
	}
	public void setMarket3(String market3) {
		this.market3 = market3;
	}
	public Double getPercentTradeMarket3() {
		return percentTradeMarket3;
	}
	public void setPercentTradeMarket3(Double percentTradeMarket3) {
		this.percentTradeMarket3 = percentTradeMarket3;
	}
	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}
	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}
	
	
	
}
