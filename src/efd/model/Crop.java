package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "crop")

public class Crop extends Asset {

	@Column(name = "CropType", length = 50)
	@DisplaySize(20)
	private String cropType;

	
	@Column(name = "UnitsProduced", nullable = false)
	private Double unitsProduced;

	@Column(name = "UnitsSold")
	private Double unitsSold;

	@Column(name = "PricePerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private Double pricePerUnit;
	
	@Column(name = "UnitsConsumed")
	private Double unitsConsumed;

	@Column(name = "UnitsOtherUse", length = 255)
	@DisplaySize(10)
	private Double unitsOtherUse;

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
	public String getCropType() {
		return cropType;
	}
	public void setCropType(String cropType) {
		this.cropType = cropType;
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

	public Double getUnitsConsumed() {
		return unitsConsumed;
	}
	public void setUnitsConsumed(Double unitsConsumed) {
		this.unitsConsumed = unitsConsumed;
	}

	public Double getUnitsOtherUse() {
		return unitsOtherUse;
	}
	public void setUnitsOtherUse(Double unitsOtherUse) {
		this.unitsOtherUse = unitsOtherUse;
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
	

	



}
