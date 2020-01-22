package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;


import efd.validations.*;

@View(members = "study, livelihoodZone")

@View(name = "study", members = "study")
@View(name = "livelihoodZone", members = "livelihoodZone")

@Tab(
properties="household.householdNumber,household.status",
baseCondition="${household.status} = 'Validated'"
)

public class CustomReportSpecListModelling {

	//@ManyToOne
	//@NoCreate
	//@NoModify
	//@DescriptionsList(descriptionProperties = "specName")
	//@OnChange(OnChangeCRSList.class)  //if crs has a restriction filter on HH then disable choice of HH 
	//private CustomReportSpecModelling customReportSpec;

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
