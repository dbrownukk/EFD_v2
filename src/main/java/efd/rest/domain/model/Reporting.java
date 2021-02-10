package efd.rest.domain.model;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import efd.rest.domain.Project;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

@View(members = "project")

public class Reporting {

	
	@Required
	private ReportMethod reportMethod;

	public enum ReportMethod {
		OHEA, OIHM
	}

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@Required
	@DescriptionsList(descriptionProperties = "projecttitle,pdate" , showReferenceView=true)
	@JoinColumn(name = "CProject")
	//@OnChange(OnChangeRefreshReporting.class)
	@ReferenceView("ReportProj")
	@NoCreate
	@NoModify

	private Project project;

	@ManyToOne
	@NoCreate
	@NoModify
	@Required
	@DescriptionsList(descriptionProperties = "specName")
	private CustomReportSpecOHEA customReportSpec;


	public CustomReportSpecOHEA getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpecOHEA customReportSpec) {
		this.customReportSpec = customReportSpec;
	}


	public ReportMethod getReportMethod() {
		return reportMethod;
	}

	public void setReportMethod(ReportMethod reportMethod) {
		this.reportMethod = reportMethod;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
