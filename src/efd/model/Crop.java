package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "Crop")

public class Crop extends Asset {

	/* Note fk to resourcetype is in supertype Asset */

	@Column(name = "CropName", length = 50)
	@DisplaySize(20)
	private String cropName;

	@Column(name = "LocalUnit", length = 45)
	@DisplaySize(20)
	private String localUnit;

	@Column(name = "QuantityProduced", nullable = false)
	private Integer quantityProduced;

	@Column(name = "QuantitySold")
	private Integer quantitySold;

	@Column(name = "PricePerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal pricePerUnit;

	@Column(name = "OtherUse", length = 255)
	@DisplaySize(10)
	private String otherUse;

	@Column(name = "JanQP")
	private Integer janQP;
	
	@Column(name = "FebQP")
	private Integer febQP;
	
	@Column(name = "MarQP")
	private Integer marQP;
	
	@Column(name = "AprQP")
	private Integer aprQP;
	
	@Column(name = "MayQP")
	private Integer mayQP;
	
	@Column(name = "JunQP")
	private Integer junQP;
	
	@Column(name = "JulQP")
	private Integer julQP;
	
	@Column(name = "AugQP")
	private Integer augQP;
	
	@Column(name = "SepQP")
	private Integer sepQP;
	
	@Column(name = "OctQP")
	private Integer octQP;
	
	@Column(name = "NovQP")
	private Integer novQP;
	
	@Column(name = "DecQP")
	private Integer decQP;

	
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

	public Integer getJanQP() {
		return janQP;
	}

	public void setJanQP(Integer janQP) {
		this.janQP = janQP;
	}

	public Integer getFebQP() {
		return febQP;
	}

	public void setFebQP(Integer febQP) {
		this.febQP = febQP;
	}

	public Integer getMarQP() {
		return marQP;
	}

	public void setMarQP(Integer marQP) {
		this.marQP = marQP;
	}

	public Integer getAprQP() {
		return aprQP;
	}

	public void setAprQP(Integer aprQP) {
		this.aprQP = aprQP;
	}

	public Integer getMayQP() {
		return mayQP;
	}

	public void setMayQP(Integer mayQP) {
		this.mayQP = mayQP;
	}

	public Integer getJunQP() {
		return junQP;
	}

	public void setJunQP(Integer junQP) {
		this.junQP = junQP;
	}

	public Integer getJulQP() {
		return julQP;
	}

	public void setJulQP(Integer julQP) {
		this.julQP = julQP;
	}

	public Integer getAugQP() {
		return augQP;
	}

	public void setAugQP(Integer augQP) {
		this.augQP = augQP;
	}

	public Integer getSepQP() {
		return sepQP;
	}

	public void setSepQP(Integer sepQP) {
		this.sepQP = sepQP;
	}

	public Integer getOctQP() {
		return octQP;
	}

	public void setOctQP(Integer octQP) {
		this.octQP = octQP;
	}

	public Integer getNovQP() {
		return novQP;
	}

	public void setNovQP(Integer novQP) {
		this.novQP = novQP;
	}

	public Integer getDecQP() {
		return decQP;
	}

	public void setDecQP(Integer decQP) {
		this.decQP = decQP;
	}



}
