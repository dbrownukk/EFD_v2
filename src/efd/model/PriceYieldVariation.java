package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;


@View(members = "PriceYieldVariation[#resource;yield;price]")

@Tab(properties = "resource.resourcetype.resourcetypename,resource.resourcetypename,yield,price")

@Entity

@Table(name = "PriceYieldVariation")
public class PriceYieldVariation extends EFDIdentifiable {



	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoCreate
	@NoModify
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename")
	private ResourceSubType resource;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@DescriptionsList(descriptionProperties = "title,author,date")
	private ModellingScenario modellingScenario;
	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	@PositiveOrZero
	private int yield;
	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	@PositiveOrZero
	private int price;
	/*************************************************************************************************/
	public ResourceSubType getResource() {
		return resource;
	}
	public void setResource(ResourceSubType resource) {
		this.resource = resource;
	}
	public ModellingScenario getModellingScenario() {
		return modellingScenario;
	}
	public void setModellingScenario(ModellingScenario modellingScenario) {
		this.modellingScenario = modellingScenario;
	}
	public int getYield() {
		return yield;
	}
	public void setYield(int yield) {
		this.yield = yield;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}


}