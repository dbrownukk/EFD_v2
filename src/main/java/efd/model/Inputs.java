package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Embeddable

@Table(name = "inputs")

public class Inputs extends Asset {

	@Column(name = "ItemPurchased", length = 50)
	private String itemPurchased;
	
	@Column(name = "UnitsPurchased", length = 6)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double unitsPurchased;

	@Column(name = "PricePerUnit", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double pricePerUnit;

	@Column(name = "Resource1UsedFor", length = 50)
	private String resource1UsedFor;
	@Column(name = "PercentResource1", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentResource1;

	@Column(name = "Resource2UsedFor", length = 50)
	private String resource2UsedFor;
	@Column(name = "PercentResource2", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentResource2;
	
	@Column(name = "Resource3UsedFor", length = 50)
	private String resource3UsedFor;
	@Column(name = "PercentResource3", precision = 10, scale = 2)
	@Digits(integer = 10, fraction = 2)
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double percentResource3;
	
	@ManyToOne

	@JoinColumn(name = "ResourceSubType")
	@DescriptionsList(descriptionProperties = "resourcetypename,resourcesubtypeunit", condition = "${resourcetype.resourcetypename} in  ('Non Food Purchase','Other Tradeable Goods')")
	private ResourceSubType resourceSubType;


	
	
	

	public String getItemPurchased() {
		return itemPurchased;
	}

	public void setItemPurchased(String itemPurchased) {
		this.itemPurchased = itemPurchased;
	}

	public Double getUnitsPurchased() {
		return unitsPurchased;
	}

	public void setUnitsPurchased(Double unitsPurchased) {
		this.unitsPurchased = unitsPurchased;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getResource1UsedFor() {
		return resource1UsedFor;
	}

	public void setResource1UsedFor(String resource1UsedFor) {
		this.resource1UsedFor = resource1UsedFor;
	}

	public Double getPercentResource1() {
		return percentResource1;
	}

	public void setPercentResource1(Double percentResource1) {
		this.percentResource1 = percentResource1;
	}

	public String getResource2UsedFor() {
		return resource2UsedFor;
	}

	public void setResource2UsedFor(String resource2UsedFor) {
		this.resource2UsedFor = resource2UsedFor;
	}

	public Double getPercentResource2() {
		return percentResource2;
	}

	public void setPercentResource2(Double percentResource2) {
		this.percentResource2 = percentResource2;
	}

	public String getResource3UsedFor() {
		return resource3UsedFor;
	}

	public void setResource3UsedFor(String resource3UsedFor) {
		this.resource3UsedFor = resource3UsedFor;
	}

	public Double getPercentResource3() {
		return percentResource3;
	}

	public void setPercentResource3(Double percentResource3) {
		this.percentResource3 = percentResource3;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	
	
	
	
}
