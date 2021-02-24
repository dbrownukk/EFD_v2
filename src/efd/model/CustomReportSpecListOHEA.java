package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.validations.*;

@View(members = "customReportSpec;livelihoodZone;visreports")

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
	@OnChange(OnChangeCRSList.class)  //if crs has a restriction filter on WG then disable choice of WG 
	private CustomReportSpecOHEA customReportSpec;

	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame

	@SearchAction("")
	@ReferenceView("FromReport")

	private LivelihoodZone livelihoodZone;

	@OneToMany
	@NewAction("")
	@EditAction("")
	@NoCreate
	@NoModify
	@RemoveAction("")
	@ReadOnly
	@Condition("${code}<10")
	@DetailAction("")
	@ListProperties(value = "name,visualisationSpreadsheet")
	private Collection<Report> visreports;



	public Collection<Report> getVisreports() {
		return visreports;
	}

	public void setVisreports(Collection<Report> visreports) {
		this.visreports = visreports;
	}

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
