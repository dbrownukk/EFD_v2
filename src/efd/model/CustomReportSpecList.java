package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;


import efd.validations.*;

@View(members = "customReportSpec,study")

//@Tab(
//properties="household.householdNumber",
//baseCondition="${household.status} = 1"
//)

public class CustomReportSpecList {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "specName")
	@OnChange(OnChangeCRSList.class)  //if crs has a restriction filter on HH then disable choice of HH 
	private CustomReportSpec customReportSpec;

	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame
	@SearchAction("")
	
	@ReferenceView("households")
	
	private Study study;

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}

}
