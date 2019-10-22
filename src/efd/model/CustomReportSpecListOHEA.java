package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;


import efd.validations.*;

@View(members = "customReportSpec,wealthgroup")

//@Tab(
//properties="household.householdNumber,household.status",
//baseCondition="${household.status} = 'Validated'"
//)

public class CustomReportSpecListOHEA {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "specName")
	//@OnChange(OnChangeCRSList.class)  //if crs has a restriction filter on HH then disable choice of HH 
	private CustomReportSpecOHEA customReportSpec;

	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame
	
	
	@SearchAction("")
	//@SearchListCondition("${household.status}='Validated'")  ONLY for search list action
	//@ReferenceView("SimpleWealthGroup")
	
	private WealthGroup wealthgroup;





	public WealthGroup getWealthgroup() {
		return wealthgroup;
	}

	public void setWealthgroup(WealthGroup wealthgroup) {
		this.wealthgroup = wealthgroup;
	}

	public CustomReportSpecOHEA getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpecOHEA customReportSpec) {
		this.customReportSpec = customReportSpec;
	}



}
