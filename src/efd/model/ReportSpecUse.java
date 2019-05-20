package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;


@View(members = "study,customReportSpec, household")
@Entity

@Table(name = "ReportSpecUse",
uniqueConstraints = {
		@UniqueConstraint(name = "report_spec_use_study", columnNames = { "customReportSpec_id","study_ID "}) })

//@Tab(properties="study.studyName, household.householdName,household.householdNumber")

public class ReportSpecUse extends Identifiable {

	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ReadOnly
	@DescriptionsList(descriptionProperties = "specName")
	private CustomReportSpec customReportSpec;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoFrame
	@Required
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	@ReferenceView("FromStdOfLiving") // study name and ref year
	private Study study;

	@ManyToMany(cascade=CascadeType.ALL)
	@NewAction("")
	@DetailAction("")
	@NoModify
	@JoinTable(name = "ExplicitReportHHInclusion")
	@EditAction("")
	@AddAction("ReportSpecUse.add")
	@ListProperties("householdNumber,interviewers,interviewDate,status")
	private Collection<Household> household;



	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<Household> getHousehold() {
		return household;
	}

	public void setHousehold(Collection<Household> household) {
		this.household = household;
	}

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}



}
