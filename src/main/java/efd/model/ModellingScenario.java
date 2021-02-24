package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import efd.validations.OnChangeProjectinModellingScenario;

@View(members = "LivelihoodsModelling[#title;author;date;study;project,livelihoodZone];"
		+ "PriceYieldVariations{priceYieldVariations},FoodSubstitution{foodSubstitution},Description{description}")

@View(name = "study", members = "study")
@View(name = "livelihoodZone", members = "livelihoodZone")

@Tab(properties = "modelType,project.projecttitle,study.studyName, title,author,date,,description", defaultOrder = "${study.studyName} asc,${project.projecttitle} asc")

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

		if (getProject() != null && getLivelihoodZone() == null) {
			throw new IllegalStateException(

					XavaResources.getString("Modelling Scenario Project must have an associate Livelihood Zone"));
		}

		if (getStudy() != null) {
			setModelType("IHM");
		} else if (getProject() != null) {
			setModelType("HEA");
		}

		/*
		 * Only allow unique RST across a Price Yield Variation and a Food Substitution
		 */
		/* May change in future but simplifies modelling reports */

		// if( getPriceYieldVariations().isEmpty() || getFoodSubstitution().isEmpty()) {
		// return;
		// }

		if (getPriceYieldVariations() != null) {

			for (PriceYieldVariation priceYieldVariation : getPriceYieldVariations()) {
				if (getFoodSubstitution().stream().filter(p -> p.getCurrentFood() == priceYieldVariation.getResource())
						.findAny().isPresent()) {
					throw new IllegalStateException(

							XavaResources.getString(
									"Food Resource cannot be in Price / Yield Variation and Food Substitution list"));
				}
				if (getFoodSubstitution().stream()
						.filter(p -> p.getSubstitutionFood() == priceYieldVariation.getResource()).findAny()
						.isPresent()) {
					throw new IllegalStateException(

							XavaResources.getString(
									"Food Resource cannot be in Price / Yield Variation and Food Substitution list"));
				}
			}
		}

	}

	@PostUpdate
	private void validatePostUpdate() throws Exception {
		if (getProject() != null && getLivelihoodZone() == null) {
			throw new IllegalStateException(

					XavaResources.getString("Modelling Scenario Project must have an associate Livelihood Zone"));
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
	@SearchKey
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@SearchKey
	@DescriptionsList(descriptionProperties = "projecttitle")
	@OnChange(OnChangeProjectinModellingScenario.class)
	private Project project;
	/*************************************************************************************************/

	@ManyToOne
	@NoCreate
	@NoModify
	// @DescriptionsList(descriptionProperties =
	// "lzname",depends="project",condition = "${Project.projectid} = ?")
	// @DescriptionsList(descriptionProperties = "lzname",depends="project")
	@SearchAction(value = "LivelihoodZone.filteredLZforModelling")

	@ReferenceView("SimpleLZnomap")
	@NoFrame

	private LivelihoodZone livelihoodZone;

	/*************************************************************************************************/

	@OneToMany(mappedBy = "modellingScenario", cascade = CascadeType.REMOVE)
	@ListProperties("resource.resourcetype.resourcetypename,resource.resourcetypename,yield,price")
	// @Size(min = 1)
	@ListAction(value = "PriceYieldVariation.generateExcel")
	private Collection<PriceYieldVariation> priceYieldVariations;

	/*************************************************************************************************/

	// @Transient - cannot be transient if need to filter in list
	// @SearchKey

	@OneToMany(mappedBy = "modellingScenario", cascade = CascadeType.REMOVE)
	@ListProperties("currentFood.resourcetypename,substitutionFood.resourcetypename")
	private Collection<FoodSubstitution> foodSubstitution;

	private String modelType;

	public Collection<FoodSubstitution> getFoodSubstitution() {
		return foodSubstitution;
	}

	public void setFoodSubstitution(Collection<FoodSubstitution> foodSubstitution) {
		this.foodSubstitution = foodSubstitution;
	}

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

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