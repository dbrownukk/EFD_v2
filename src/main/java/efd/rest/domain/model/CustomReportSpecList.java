package efd.rest.domain.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;


import efd.validations.*;

@View(members = "studyToSelect,customReportSpec,study;visreports")

@Tab(
properties="household.householdNumber,household.status",
baseCondition="${household.status} = 'Validated'"
)

public class CustomReportSpecList {

	@ManyToOne
	@Required
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
	//@SearchListCondition("${household.status}='Validated'")  ONLY for search list action
	@ReferenceView("households")
	
	private Study study;

	@OneToMany
	@NewAction("")
	@EditAction("")
	@NoCreate
	@NoModify
	@RemoveAction("")
	@ReadOnly
	@Condition("${code}=2")
	@DetailAction("")
	@ListProperties(value = "name,visualisationSpreadsheet")
	
	
	
	private Collection<Report> visreports;
	
	@ManyToOne
	@NoCreate
	@NoModify
	@NoFrame
	@SearchAction("")
	@Transient
	@DescriptionsList(descriptionProperties = "studyName")
	@OnChange(OnChangeStudyToSelect.class)
	private Study studyToSelect;

	
	
	
	/**
	 * @return the studyToSelect
	 */
	public Study getStudyToSelect() {
		return studyToSelect;
	}

	/**
	 * @param studyToSelect the studyToSelect to set
	 */
	public void setStudyToSelect(Study studyToSelect) {
		this.studyToSelect = studyToSelect;
	}

	public Collection<Report> getVisreports() {
		return visreports;
	}

	public void setVisreports(Collection<Report> visreports) {
		this.visreports = visreports;
	}

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
