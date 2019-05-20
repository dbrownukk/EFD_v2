package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

public class CustomReportSpecList {

	@ManyToOne
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "specName")

	private CustomReportSpec customReportSpec;

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}



}
