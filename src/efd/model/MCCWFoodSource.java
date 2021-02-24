package efd.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.openxava.annotations.AddAction;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;

/**
 * The persistent class for the mccancewiddowsons food sources
 */

@View(members = "foodCode;foodSourceName;resourceSubType;microNutrientLevel")
@View(name = "Simple", members = "foodCode;foodSourceName")
@View(name = "fromrst", members = "foodCode;foodSourceName;microNutrientLevel")

@Entity
@Table(name = "mccwfoodsource")

@Tab(properties = "foodCode;foodSourceName,description")

public class MCCWFoodSource {

	@Id
	@Column(length = 32)
	private String foodCode;

	@Required
	@Column(name = "FoodSourceName", length = 100)
	private String foodSourceName;

	private String description;

	@OneToMany(mappedBy = "mccwFoodSource")
	@AddAction("")
	@ListProperties("resourcetypename")
	private Collection<ResourceSubType> resourceSubType;

	@OneToMany(mappedBy = "mccwFoodSource")
	//@NoModify
	@AddAction("")
	@OrderBy("microNutrient.name")
	@ListProperties("microNutrient.name, mnLevel, microNutrient.rda,microNutrient.rdaUnit")
	private Collection<MicroNutrientLevel> microNutrientLevel;

	public String getFoodCode() {
		return foodCode;
	}

	public void setFoodCode(String foodCode) {
		this.foodCode = foodCode;
	}

	public String getFoodSourceName() {
		return foodSourceName;
	}

	public void setFoodSourceName(String foodSourceName) {
		this.foodSourceName = foodSourceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<ResourceSubType> getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(Collection<ResourceSubType> resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public Collection<MicroNutrientLevel> getMicroNutrientLevel() {
		return microNutrientLevel;
	}

	public void setMicroNutrientLevel(Collection<MicroNutrientLevel> microNutrientLevel) {
		this.microNutrientLevel = microNutrientLevel;
	}


	
	

}