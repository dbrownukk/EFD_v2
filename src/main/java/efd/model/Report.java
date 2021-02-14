package efd.model;
/* List of available reports runnable from OHEA or OIHM */

import org.hibernate.validator.constraints.UniqueElements;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;
import org.openxava.model.Identifiable;

import javax.persistence.*;
import java.util.Collection;

@View(members = "name, code, method,visualisationSpreadsheet")
@View(name = "FromCRS",members = "DownloadVisualisationSpreadsheet[name;visualisationSpreadsheet]")

@Table(name = "Report")
@Tab(properties="name, code, isMandatory",
		defaultOrder="${code} asc"
)
@Entity

public class Report extends Identifiable {
	@Required
	@Column(length = 100, nullable = false, unique = true)
	@DisplaySize(value = 40)
	private String name;

	@Required
	@Column(unique = true, nullable = false)
	private int code;

	@ManyToMany
	@UniqueElements
	private Collection<CustomReportSpec> customReportSpec;

	@ManyToMany
	@UniqueElements
	private Collection<CustomReportSpecOHEA> customReportSpecOHEA;

	@Column(name = "Method")
	private Method method;


	@Column(name = "isMandatory")
	private String isMandatory;

	public enum Method {
		OHEA, OIHM, MODELLINGOIHMSCENARIO, MODELLINGOHEASCENARIO, MODELLINGOIHMCOPING, MODELLINGOHEACOPING
	}

	/*
	 * Visualisation Spreadsheet - Used after download of Modelling/OHEA/OIHM
	 * Reports to show graphs
	 */
	@Stereotype("FILE")
	@Column(length = 32, name = "VisualisationSpreadsheet")
	private String visualisationSpreadsheet;




	public String getVisualisationSpreadsheet() {
		return visualisationSpreadsheet;
	}

	public void setVisualisationSpreadsheet(String visualisationSpreadsheet) {
		this.visualisationSpreadsheet = visualisationSpreadsheet;
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


	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public static Report findByCode(int code) throws NoResultException {
		System.out.println("find by code, code = "+code);
		Query query = XPersistence.getManager().createQuery(
				"from Report as o where o.code = :code");
		query.setParameter("code", code);
		final Report firstResult = (Report) query.getSingleResult();

		System.out.println("found report = "+firstResult.getName());

		return firstResult;
	}

	public Collection<CustomReportSpecOHEA> getCustomReportSpecOHEA() {
		return customReportSpecOHEA;
	}

	public void setCustomReportSpecOHEA(Collection<CustomReportSpecOHEA> customReportSpecOHEA) {
		this.customReportSpecOHEA = customReportSpecOHEA;
	}
}
