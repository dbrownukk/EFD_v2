package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;



@Embeddable

@Table(name = "Crop")

public class Crop extends Asset{

	/* Note fk to resourcetype is in supertype Asset */

	@Column(name = "CropName", length = 50)
	private String cropName;

	@Column(name = "LocalUnit", length = 45)
	private String localUnit;
	

	
	@Column(name = "QuantityProduced", nullable=false)
	private Integer quantityProduced;
	
	@Column(name = "QuantitySold")
	private Integer quantitySold;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;
	
	@Column(name = "OtherUse", length = 255)
	private String otherUse;

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
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

    //@AsEmbedded
    //private MonthlyDataQP qp;


	
}
