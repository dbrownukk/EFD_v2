package efd.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;

@Embeddable

@Table(name = "transfer")

public class Transfer extends Asset {

	@Column(name = "Official", columnDefinition="boolean default false")
	@org.openxava.annotations.DisplaySize(15)
	// @DefaultValueCalculator(value = FalseCalculator.class)
	@org.openxava.annotations.Editor(value = "BooleanYesNoCombo")
	private Boolean isOfficial;

	@Column(name = "Source", length = 50)
	private String source;

	@Column(name = "TransferType", length = 50, nullable = false)
	@OrderColumn
	@org.openxava.annotations.Required
	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.EnumCalculator.class, properties = {
			@org.openxava.annotations.PropertyValue(name = "enumType", value = "efd.model.Transfer$TransferType"),
			@org.openxava.annotations.PropertyValue(name = "value", value = "Food") })
	private TransferType transferType;

	public enum TransferType {
		Food, Cash, Other
	}

	@Column(name = "PeopleReceiving")
	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	private Double peopleReceiving;

	@Column(name = "TimesReceived")
	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	private Double timesReceived;

	@Column(name = "CashTransferAmount", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	private Double cashTransferAmount;

	@ManyToOne
	@JoinColumn(name = "FoodResourceSubType")
	@org.openxava.annotations.DescriptionsList(descriptionProperties = "resourcetypename,resourcesubtypeunit", condition = "${resourcetype.resourcetypename} in  ('Food Purchase','Wild Foods','Crops','Livestock','Livestock Products','Other Tradeable Goods')")
	private ResourceSubType foodResourceSubType;

	@Column(name = "TransferFoodOtherType", length = 50)
	private String transferFoodOtherType;

	@Column(name = "UnitesTransferred")
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsTransferred;

	@Column(name = "UnitsSold")
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsSold;

	@Column(name = "PricePerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	private Double pricePerUnit;

	@Column(name = "OtherUse", length = 255)
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double otherUse;

	@org.openxava.annotations.Depends("unitsTransferred,unitsSold,otherUse")
	@Column(name = "UnitsConsumed")
	public Double getUnitsConsumed() {
		return (getUnitsTransferred() - getUnitsSold() - getOtherUse());
	};
	// @Column(name = "UnitsConsumed")
	// private Double unitsConsumed;

	@Column(name = "Market1", length = 50)
	private String market1;

	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	@Column(name = "PercentTradeMarket1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket1;

	@Column(name = "Market2", length = 50)
	private String market2;

	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	@Column(name = "PercentTradeMarket2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket2;

	@Column(name = "Market3", length = 50)
	private String market3;

	@org.openxava.annotations.DefaultValueCalculator(value = org.openxava.calculators.ZeroLongCalculator.class)
	@Column(name = "PercentTradeMarket3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double percentTradeMarket3;

	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@org.openxava.annotations.DescriptionsList(descriptionProperties = "resourcetypename,resourcesubtypeunit", condition = "${resourcetype.resourcetypename} like '%Transfer%'")
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

	public TransferType getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferType transferType) {
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

	public ResourceSubType getFoodResourceSubType() {
		return foodResourceSubType;
	}

	public void setFoodResourceSubType(ResourceSubType foodResourceSubType) {
		this.foodResourceSubType = foodResourceSubType;
	}

	public String getTransferFoodOtherType() {
		return transferFoodOtherType;
	}

	public void setTransferFoodOtherType(String transferFoodOtherType) {
		this.transferFoodOtherType = transferFoodOtherType;
	}

	public Double getUnitsTransferred() {
		return unitsTransferred == null ? 0.0 :unitsTransferred;
	}

	public void setUnitsTransferred(Double unitsTransferred) {
		this.unitsTransferred = unitsTransferred;
	}

	public Double getUnitsSold() {
		return unitsSold == null ? 0.0 :unitsSold;
	}

	public void setUnitsSold(Double unitsSold) {
		this.unitsSold  = unitsSold;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Double getOtherUse() {
		return otherUse == null ? 0.0 :otherUse;
	}

	public void setOtherUse(Double otherUse) {
		this.otherUse = otherUse;
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
