package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.validations.*;

@View(members = "customReportSpec;;")

//@Tab(
//properties="household.householdNumber,household.status",
//baseCondition="${household.status} = 'Validated'"
//)

public class CustomReportSpecListOHEA {

	@ManyToOne
	@NoCreate
	@NoModify
	
	@DescriptionsList(descriptionProperties = "specName")
	private CustomReportSpecOHEA customReportSpec;

	
	/*
	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame

	
	@SearchAction("")
	// @SearchListCondition("${household.status}='Validated'") ONLY for search list
	// action
	@ReferenceView("FromReport")

	private Community community;

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}
*/
	public CustomReportSpecOHEA getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpecOHEA customReportSpec) {
		this.customReportSpec = customReportSpec;
	}

}
