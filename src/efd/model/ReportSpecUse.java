package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;


@Entity




public class ReportSpecUse extends Identifiable {
	
	
	@ManyToOne
	private CustomReportSpec customReportSpec;
	
	
	@ManyToOne
	@ReferenceView("FromStdOfLiving")   // study name and ref year
	private Study study;

	@OneToMany(mappedBy="reportSpecUse")
	private Collection<ReportSpecHHInclusion> reportSpecHHInclusion;

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<ReportSpecHHInclusion> getReportSpecHHInclusion() {
		return reportSpecHHInclusion;
	}

	public void setReportSpecHHInclusion(Collection<ReportSpecHHInclusion> reportSpecHHInclusion) {
		this.reportSpecHHInclusion = reportSpecHHInclusion;
	}



	
	
	
	
	
}
