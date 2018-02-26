package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Entity

@Table(name="WildFoods")



public class WildFood extends Asset{

@Column(name="WildFoodName", length=50)
private String wildfoodname;

@Column(name="LocalUnit", length=45)
private String localunit;
	
@Column(name="QuantityProduced", length=6)
@NotNull
@Min(value=0)
private Integer quantityproduced;
	
@Column(name="QuantitySold", length=6)
private Integer quantitsold;

@Stereotype("MONEY")
@Column(name="PricePerUnit" )
private BigDecimal priceperunit;

@Column(name="OtherUse", length=255)
private String otheruse;

public String getWildfoodname() {
	return wildfoodname;
}

public void setWildfoodname(String wildfoodname) {
	this.wildfoodname = wildfoodname;
}

public String getLocalunit() {
	return localunit;
}

public void setLocalunit(String localunit) {
	this.localunit = localunit;
}

public Integer getQuantityproduced() {
	return quantityproduced;
}

public void setQuantityproduced(Integer quantityproduced) {
	this.quantityproduced = quantityproduced;
}

public Integer getQuantitsold() {
	return quantitsold;
}

public void setQuantitsold(Integer quantitsold) {
	this.quantitsold = quantitsold;
}

public BigDecimal getPriceperunit() {
	return priceperunit;
}

public void setPriceperunit(BigDecimal priceperunit) {
	this.priceperunit = priceperunit;
}

public String getOtheruse() {
	return otheruse;
}

public void setOtheruse(String otheruse) {
	this.otheruse = otheruse;
}




}
