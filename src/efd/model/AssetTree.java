package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

@Embeddable


@Table(name = "assettree")

public class AssetTree extends Asset{


	@Column(name = "TreeTypeEnteredName", length = 50, nullable=false)
	@Required
	private String treeTypeEnteredName;
	
	@Column(name = "NumberOwned", nullable=false )
	@Required
	@NotNull
	private Integer numberOwned;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private BigDecimal pricePerUnit;

	public String getTreeTypeEnteredName() {
		return treeTypeEnteredName;
	}

	public void setTreeTypeEnteredName(String treeTypeEnteredName) {
		this.treeTypeEnteredName = treeTypeEnteredName;
	}

	public Integer getNumberOwned() {
		return numberOwned;
	}

	public void setNumberOwned(Integer numberOwned) {
		this.numberOwned = numberOwned;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	
	
	






	
	
	
}
