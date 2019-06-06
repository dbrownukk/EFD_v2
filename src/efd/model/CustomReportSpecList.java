package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@View(members = "customReportSpec,study")

public class CustomReportSpecList {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "specName")

	private CustomReportSpec customReportSpec;

	@ManyToOne
	@NoCreate
	@NoModify
	// @DescriptionsList(descriptionProperties = "studyName,referenceYear")
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
