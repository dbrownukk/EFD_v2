package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "livestocksale")

public class LivestockSales extends Asset {

	@Column(name = "LivestockType", length = 50)
	@DisplaySize(20)
	private String livestockType;

	
	@Column(name = "UnitsAtStartofYear", nullable = false)
	private Integer unitsAtStartofYear;

	@Column(name = "UnitsSold")
	private Integer unitsSold;

	@Column(name = "PricePerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal pricePerUnit;

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
	
	public String getLivestockType() {
		return livestockType;
	}
	public void setLivestockType(String livestockType) {
		this.livestockType = livestockType;
	}
	public Integer getUnitsAtStartofYear() {
		return unitsAtStartofYear;
	}
	public void setUnitsAtStartofYear(Integer unitsAtStartofYear) {
		this.unitsAtStartofYear = unitsAtStartofYear;
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
