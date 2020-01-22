package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

@View(members = "ModellingScenario[#title;author;date;study;project];PriceYieldVariations{priceYieldVariations},Description{description}")

@View(name = "study", members = "study")
@View(name = "livelihoodZone", members = "livelihoodZone")

//@Tab(properties = "title,author,date,priceYieldVariations,study,project")

@Entity

@Table(name = "ModellingScenario")
public class ModellingScenario extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		System.out.println("date = " + Dates.createCurrent());
		setDate(Dates.createCurrent());
		if ((project == null && study == null) || (project != null && study != null)) {

			throw new IllegalStateException(

					XavaResources.getString("Modelling Scenario must be associated to a Study or Project"));

		}
	}

	/*************************************************************************************************/
	@Required
	@Column(length = 45, nullable = false, unique = true)
	private String title;

	/*************************************************************************************************/
	@Required
	@Column(length = 45, nullable = false)
	private String author;
	/*************************************************************************************************/
	@ReadOnly
	@Stereotype("DATE")
	@Column(name = "Date")
	@Required
	@DefaultValueCalculator(CurrentDateCalculator.class)
	private java.util.Date date;
	/*************************************************************************************************/
	@Column(name = "Description", length = 255)
	@Stereotype("MEMO")
	private String description;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "projecttitle")
	private Project project;
	/*************************************************************************************************/
	
	//@ManyToOne
	//@NoCreate
	//@NoModify
	//@DescriptionsList(descriptionProperties = "lzname",depends="project", condition = "${project.projectid} = ?")
	//private LivelihoodZone  livelihoodZone;
	
	/*************************************************************************************************/

	
	@OneToMany(mappedBy = "modellingScenario", cascade = CascadeType.REMOVE)
	@ListProperties("resource.resourcetype.resourcetypename,resource.resourcetypename,yield,price")
	//@Size(min = 1)
	private Collection<PriceYieldVariation> priceYieldVariations;

	/*************************************************************************************************/

	public Collection<PriceYieldVariation> getPriceYieldVariations() {
		return priceYieldVariations;
	}

	public void setPriceYieldVariations(Collection<PriceYieldVariation> priceYieldVariations) {
		this.priceYieldVariations = priceYieldVariations;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}





}