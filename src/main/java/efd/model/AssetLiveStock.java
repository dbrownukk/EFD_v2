package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;



@Views({
	@View(name="ReadOnly", members = "status")
//	,resourceSubType,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit")
})
//@Entity
@Embeddable


@Table(name = "assetliveStock")

public class AssetLiveStock extends Asset{


	@Column(name = "LiveStockTypeEnteredName", length = 50, nullable=true)
	//@Required
	private String liveStockTypeEnteredName;
	
	@Column(name = "NumberOwnedAtStart", nullable=false )
	//@Required
	@NotNull
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double numberOwnedAtStart;

	@Column(name = "PricePerUnit" ,precision=10, scale=2)
	@Digits(integer=10,fraction=2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double pricePerUnit;
	
	@ManyToOne

	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties="resourcetypename,resourcesubtypeunit", condition="${resourcetype.resourcetypename}='Livestock'")
	private ResourceSubType resourceSubType;

	public String getLiveStockTypeEnteredName() {
		return liveStockTypeEnteredName;
	}

	public void setLiveStockTypeEnteredName(String liveStockTypeEnteredName) {
		this.liveStockTypeEnteredName = liveStockTypeEnteredName;
	}





	public Double getNumberOwnedAtStart() {
		return numberOwnedAtStart;
	}

	public void setNumberOwnedAtStart(Double numberOwnedAtStart) {
		this.numberOwnedAtStart = numberOwnedAtStart;
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
