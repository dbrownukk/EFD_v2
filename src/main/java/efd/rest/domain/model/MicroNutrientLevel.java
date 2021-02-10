package efd.rest.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.openxava.annotations.NoFrame;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;
import org.openxava.model.Identifiable;

/*
 * Values for a micronutrient for a food stuff
 * DRB 12/06/2020
 */

@View(members = "mccwFoodSource;microNutrient[#microNutrient,mnLevel]")

@Tab(properties="microNutrient.name,mccwFoodSource.foodCode,mccwFoodSource.foodSourceName"
		+ ",mnLevel,microNutrient.rda",  defaultOrder="${microNutrient.name}" )

@Entity

public class MicroNutrientLevel extends Identifiable{

	@Column(length=15)
	private String mnLevel;

	
	@ManyToOne
	//@NoCreate
	//@NoModify
	//@ReadOnly
	//@DescriptionsList(descriptionProperties = "name,rda,rdaUnit",showReferenceView=true)
	
	@ReferenceView("frommnl")
	@NoFrame
	
	private MicroNutrient microNutrient;

	
	@ManyToOne
	//@NoCreate
	//@NoModify
	//@ReadOnly
	//@DescriptionsList(descriptionProperties = "foodCode,foodSourceName")
	@ReferenceView("Simple")
	private MCCWFoodSource mccwFoodSource;


	public String getMnLevel() {
		return mnLevel;
	}


	public void setMnLevel(String mnLevel) {
		this.mnLevel = mnLevel;
	}




	public MicroNutrient getMicroNutrient() {
		return microNutrient;
	}


	public void setMicroNutrient(MicroNutrient microNutrient) {
		this.microNutrient = microNutrient;
	}


	public MCCWFoodSource getMccwFoodSource() {
		return mccwFoodSource;
	}


	public void setMccwFoodSource(MCCWFoodSource mccwFoodSource) {
		this.mccwFoodSource = mccwFoodSource;
	}




}
