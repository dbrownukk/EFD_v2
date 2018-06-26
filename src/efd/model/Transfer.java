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
	private Integer peopleReceiving;
	
	@Column(name = "TimesReceived")
	private Integer timesReceived;
	
	@Column(name = "CashTransferAmount" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal cashTransferAmount;
	
	@Column(name = "TransferFoodOtherType", length = 50)
	private String transferFoodOtherType;
	
	@Column(name = "UnitesTransferred")
	private Integer unitsTransferred;
	
	@Column(name = "UnitsSold")
	private Integer unitsSold;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;
	
	@Column(name = "OtherUse", length = 255)
	private String otherUse;
	
	@Column(name = "UnitsConsumed")
	private Integer unitsconsumed;
	
	@Column(name = "Market1", length = 50)
	private String market1;
	@Column(name = "PercentTradeMarket1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal percentTradeMarket1;
	
	@Column(name = "Market2", length = 50)
	private String market2;
	@Column(name = "PercentTradeMarket2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal percentTradeMarket2;
	
	@Column(name = "Market3", length = 50)
	private String market3;
	@Column(name = "PercentTradeMarket3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal percentTradeMarket3;
	
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
	public Integer getPeopleReceiving() {
		return peopleReceiving;
	}
	public void setPeopleReceiving(Integer peopleReceiving) {
		this.peopleReceiving = peopleReceiving;
	}
	public Integer getTimesReceived() {
		return timesReceived;
	}
	public void setTimesReceived(Integer timesReceived) {
		this.timesReceived = timesReceived;
	}
	public BigDecimal getCashTransferAmount() {
		return cashTransferAmount;
	}
	public void setCashTransferAmount(BigDecimal cashTransferAmount) {
		this.cashTransferAmount = cashTransferAmount;
	}
	public String getTransferFoodOtherType() {
		return transferFoodOtherType;
	}
	public void setTransferFoodOtherType(String transferFoodOtherType) {
		this.transferFoodOtherType = transferFoodOtherType;
	}
	public Integer getUnitsTransferred() {
		return unitsTransferred;
	}
	public void setUnitsTransferred(Integer unitsTransferred) {
		this.unitsTransferred = unitsTransferred;
	}
	public Integer getUnitsSold() {
		return unitsSold;
	}
	public void setUnitsSold(Integer unitsSold) {
		this.unitsSold = unitsSold;
	}
	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public String getOtherUse() {
		return otherUse;
	}
	public void setOtherUse(String otherUse) {
		this.otherUse = otherUse;
	}
	public Integer getUnitsconsumed() {
		return unitsconsumed;
	}
	public void setUnitsconsumed(Integer unitsconsumed) {
		this.unitsconsumed = unitsconsumed;
	}
	public String getMarket1() {
		return market1;
	}
	public void setMarket1(String market1) {
		this.market1 = market1;
	}
	public BigDecimal getPercentTradeMarket1() {
		return percentTradeMarket1;
	}
	public void setPercentTradeMarket1(BigDecimal percentTradeMarket1) {
		this.percentTradeMarket1 = percentTradeMarket1;
	}
	public String getMarket2() {
		return market2;
	}
	public void setMarket2(String market2) {
		this.market2 = market2;
	}
	public BigDecimal getPercentTradeMarket2() {
		return percentTradeMarket2;
	}
	public void setPercentTradeMarket2(BigDecimal percentTradeMarket2) {
		this.percentTradeMarket2 = percentTradeMarket2;
	}
	public String getMarket3() {
		return market3;
	}
	public void setMarket3(String market3) {
		this.market3 = market3;
	}
	public BigDecimal getPercentTradeMarket3() {
		return percentTradeMarket3;
	}
	public void setPercentTradeMarket3(BigDecimal percentTradeMarket3) {
		this.percentTradeMarket3 = percentTradeMarket3;
	}
	
	
}
