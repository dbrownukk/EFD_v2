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
	//@Required
	@NotNull
	private Double numberOwned;
	
	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	private Double pricePerUnit;
	
	@ManyToOne
	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename", condition="${resourcetype.resourcetypename} like '%Trees%'")
	private ResourceSubType resourceSubType;

	public String getTreeTypeEnteredName() {
		return treeTypeEnteredName;
	}

	public void setTreeTypeEnteredName(String treeTypeEnteredName) {
		this.treeTypeEnteredName = treeTypeEnteredName;
	}

	public Double getNumberOwned() {
		return numberOwned;
	}

	public void setNumberOwned(Double numberOwned) {
		this.numberOwned = numberOwned;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}


	
	
	
	






	
	
	
}
