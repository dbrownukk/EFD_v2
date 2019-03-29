package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

/*
@Views({
@View(members= "WildFood[#wildfoodname]")
//@View(name="FullCountry",members= "idcountry,isocountrycode,currency,currencySymbol"),
})
*/
//@Tab(editors = "List, Cards", properties = "wildfoodname")

@Embeddable

@Table(name = "wildfood")

public class WildFood extends Asset{


	@Column(name = "WildFoodName", length = 50)
	private String wildFoodName;
	
	
	//@Column(name = "WildfoodProduct", length = 50)
	//@DisplaySize(20)
	//private String wildfoodProduct;

	@Column(name = "UnitsProduced", length = 6)
	@NotNull
	@Min(value = 0)
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsProduced;

	@Column(name = "UnitsSold", length = 6)
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsSold;
	
	
	@Column(name = "PricePerUnit",precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double pricePerUnit;

	@Depends("unitsProduced,unitsSold,otherUse")
	@Column(name = "UnitsConsumed")
	public Double getUnitsConsumed(){
		return(unitsProduced-unitsSold-otherUse);
	};
	
	
	//@Column(name = "UnitsConsumed")
	//private Double unitsConsumed;
	
	@Column(name = "OtherUse", length = 255)
	//@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double otherUse;

	@Column(name = "Market1", length = 50)
	private String market1;
	@Column(name = "PercentTradeMarket1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentTradeMarket1;
	
	@Column(name = "Market2", length = 50)
	private String market2;
	@Column(name = "PercentTradeMarket2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentTradeMarket2;
	
	@Column(name = "Market3", length = 50)
	private String market3;
	@Column(name = "PercentTradeMarket3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentTradeMarket3;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	
	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename} like '%Wild Food%'")
	private ResourceSubType resourceSubType;
	
	public String getWildFoodName() {
		return wildFoodName;
	}
	public void setWildFoodName(String wildFoodName) {
		this.wildFoodName = wildFoodName;
	}
	public Double getUnitsProduced() {
		return unitsProduced;
	}
	public void setUnitsProduced(Double unitsProduced) {
		this.unitsProduced = unitsProduced;
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
