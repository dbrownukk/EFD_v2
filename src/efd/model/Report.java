package efd.model;
/* List of available reports runnable from OHEA or OIHM */



import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@View(members = "name, code, method")


@Table(name = "Report")
@Entity

public class Report extends Identifiable{


	@Required
	@Column(length=100, nullable=false, unique = true)
	@DisplaySize(value = 40)
	private String name;

	@Required
	@Column(unique=true, nullable=false)
	private int code;
	
	@ManyToMany
	@JoinTable(name="ReportInclusion")
	private Collection<CustomReportSpec> customReportSpec;
	
	@Column(name = "Method")
	private Method method;

	public enum Method {
		OHEA,OIHM, MODELLING
	}

	
	

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

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



