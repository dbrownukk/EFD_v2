package efd.rest.domain.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.validations.*;

@View(members = "study, livelihoodZone; visreports")

@View(name = "study", members = "modelType;study; visreports")
@View(name = "livelihoodZone", members = "modelType;livelihoodZone;visreports")

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
	//@DescriptionsList(descriptionProperties = "household.householdName")

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
			@PropertyValue(name = "enumType", value = "efd.rest.domain.model.CustomReportSpecListModelling$ModelType"),
			@PropertyValue(name = "value", value = "0") })
	@Editor(value="ValidValuesRadioButton")
	@OnChange(OnChangeModelType.class)
	private ModelType modelType;
	public enum ModelType { ChangeScenario, CopingStrategy };
	
	
	@OneToMany
	@NewAction("")
	@EditAction("")
	@NoCreate
	@NoModify
	@RemoveAction("")
	@ReadOnly
	@DetailAction("")
	
	
	@Condition("${code}<7")
	@DetailAction("")
	@ListProperties(value = "name,visualisationSpreadsheet")
	private Collection<Report> visreports;
	
	
	

	public Collection<Report> getVisreports() {
		return visreports;
	}

	public void setVisreports(Collection<Report> visreports) {
		this.visreports = visreports;
	}

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
