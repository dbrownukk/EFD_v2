package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

/*
@Views({
@View(members= "WildFood[#wildfoodname]")
//@View(name="FullCountry",members= "idcountry,isocountrycode,currency,currencySymbol"),
})
*/
//@Tab(editors = "List, Cards", properties = "wildfoodname")

@Embeddable

@Table(name = "WildFood")

public class WildFood extends Asset{


	@Column(name = "WildFoodName", length = 50)
	private String wildFoodName;

	@Column(name = "LocalUnit", length = 45)
	private String localUnit;

	@Column(name = "QuantityProduced", length = 6)
	@NotNull
	@Min(value = 0)
	private Integer quantityProduced;

	@Column(name = "QuantitySold", length = 6)
	private Integer quantitySold;
	
	
	@Column(name = "PricePerUnit",precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;

	@Column(name = "OtherUse", length = 255)
	private String otherUse;

	public String getWildFoodName() {
		return wildFoodName;
	}

	public void setWildFoodName(String wildFoodName) {
		this.wildFoodName = wildFoodName;
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






}