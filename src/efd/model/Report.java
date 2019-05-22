package efd.model;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;




@View(members = "name")
@Table(name = "Report")
@Entity

public class Report extends Identifiable{


	@Required
	@Column(length=100, nullable=false, unique = true)
	private String name;

	@Required
	@Column(unique=true, nullable=false)
	private int code;
	
	@ManyToMany
	@JoinTable(name="ReportInclusion")
	private Collection<CustomReportSpec> customReportSpec;
	
	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<CustomReportSpec> getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(Collection<CustomReportSpec> customReportSpec) {
		this.customReportSpec = customReportSpec;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
	
	
}



