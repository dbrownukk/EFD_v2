package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable

@Table(name = "LiveStockUse")

public class LiveStockUse extends Asset{

	@Column(name = "LSName", length = 50)
	private String lsName;

	@Column(name = "LocalUnit", length = 45)
	private String localUnit;
	
	@Column(name = "QuantityProduced", nullable=false)
	@NotNull
	private Integer quantityProduced;
	
	@Column(name = "QuantitySold")
	private Integer quantitySold;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;
	
	@Column(name = "OtherUse", length = 255)
	private String otherUse;
	
	@Column(name="LSIncomeType")
	private Integer lsIncomeType;
	
	@Column(name="LSIncomeType2")
	private String lsIncomeType2;



	public String getLsName() {
		return lsName;
	}

	public void setLsName(String lsName) {
		this.lsName = lsName;
	}

	public String getLocalUnit() {
		return localUnit;
	}

	public void setLocalUnit(String localUnit) {
		this.localUnit = localUnit;
	}

	public Integer getQuantityProduced() {
		return quantityProduced;
	}

	public void setQuantityProduced(Integer quantityProduced) {
		this.quantityProduced = quantityProduced;
	}

	public Integer getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Integer quantitySold) {
		this.quantitySold = quantitySold;
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

	public Integer getLsIncomeType() {
		return lsIncomeType;
	}

	public void setLsIncomeType(Integer lsIncomeType) {
		this.lsIncomeType = lsIncomeType;
	}

	public String getLsIncomeType2() {
		return lsIncomeType2;
	}

	public void setLsIncomeType2(String lsIncomeType2) {
		this.lsIncomeType2 = lsIncomeType2;
	}
	
	
	
	
	
	
	
	
	
	
}
