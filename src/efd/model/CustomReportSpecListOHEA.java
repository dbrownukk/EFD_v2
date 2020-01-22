package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.validations.*;

@View(members = "customReportSpec;livelihoodZone;")

//@Tab(
//properties="household.householdNumber,household.status",
//baseCondition="${household.status} = 'Validated'"
//)

public class CustomReportSpecListOHEA {

	@ManyToOne
	@NoCreate
	@NoModify
    @Required
	@DescriptionsList(descriptionProperties = "specName")
	private CustomReportSpecOHEA customReportSpec;

	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame

	@SearchAction("")
	@ReferenceView("FromReport")
	
	private LivelihoodZone livelihoodZone ;



	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public CustomReportSpecOHEA getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpecOHEA customReportSpec) {
		this.customReportSpec = customReportSpec;
	}

}
