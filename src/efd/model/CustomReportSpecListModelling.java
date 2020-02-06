package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.validations.*;

@View(members = "study, livelihoodZone")

@View(name = "study", members = "modelType;study")
@View(name = "livelihoodZone", members = "modelType;livelihoodZone")

@Tab(
properties="household.householdNumber,household.status",
baseCondition="${household.status} = 'Validated'"
)

public class CustomReportSpecListModelling {



	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame

	@SearchAction("")
	//@SearchListCondition("${household.status}='Validated'")  ONLY for search list action
	@ReferenceView("forModelling")

	private Study study;
	
	
	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame

	@SearchAction("")
	@ReferenceView("FromReport")
	
	private LivelihoodZone livelihoodZone ;
	
	@Required
	@DefaultValueCalculator(value = EnumCalculator.class, properties = {
			@PropertyValue(name = "enumType", value = "efd.model.CustomReportSpecListModelling$ModelType"),
			@PropertyValue(name = "value", value = "ChangeScenario") })
	//@Editor(value="ValidValuesRadioButton")
	private ModelType modelType;
	public enum ModelType { ChangeScenario, CopingStrategy };
	
	
	
	

	public ModelType getModelType() {
		return modelType;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}




}
