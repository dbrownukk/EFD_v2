package efd.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.DisplaySize;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.OnChange;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;
import org.openxava.calculators.IntegerCalculator;
import org.openxava.jpa.XPersistence;
import org.openxava.util.XavaResources;

import efd.validations.OnChangeRSTSyn;
import efd.validations.OnChangeRT;
import efd.validations.RSTFilter;

@Entity

@Views({ @View(members = "resourcetype;resourcetypename;resourcesubtypeunit;"
		+ "resourcesubtypekcal;resourcesubtypesynonym;mccwFoodSource,category"),
		@View(name = "FromCategory", members = "resourcetype,resourcetypename,resourcesubtypeunit;"
				+ "resourcesubtypekcal,resourcesubtypesynonym"),
		@View(name = "SimpleSubtype", members = "resourcetypename"),
		@View(name = "FromLocalUnit", members = "resourcetype;resourcetypename,resourcesubtypeunit") })

@Tab(properties = "resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,"
		+ "resourcesubtypekcal,resourcesubtypesynonym.resourcetypename,mccwFoodSource.foodSourceName")

@Tab(name="fromNutrientsMenu",properties = "resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,"
		+ "resourcesubtypekcal,resourcesubtypesynonym.resourcetypename,mccwFoodSource.foodSourceName", 
		baseCondition="${resourcetype.availableInNutrientsMenu} = 'Y'")


@Table(name = "ResourceSubType", uniqueConstraints = @UniqueConstraint(columnNames = { "ReourceType",
		"ResourceTypeName" }))
public class ResourceSubType {

	@PreUpdate

	private void stopRTupdate() {
		System.out.println("in stopRTupdate");
		ResourceSubType rst = XPersistence.getManager().find(ResourceSubType.class, this.idresourcesubtype);

	
		if (this.resourcetype != rst.resourcetype) {
			System.out.println("Need to stop update");
			throw new javax.validation.ValidationException(
					XavaResources.getString("Cannot change Resource Type once Resource Sub Tyes exist"));
		}

	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceSubType", length = 32, unique = true)
	private String idresourcesubtype;

	@ManyToOne
	@Required
	@NoModify
	
	
	@NoCreate
	@JoinColumn(name = "ReourceType")
	@OnChange(notForViews = "FromLocalUnit", value = OnChangeRT.class)
	/* Need to handle reduced list if RST from Nutrition menu */
	/* Filter with just food items Crops, WF, Food Purchases, LSP 
	/* #511 */
	@DescriptionsList(descriptionProperties = "resourcetypename", filter=RSTFilter.class,
	condition="${availableInNutrientsMenu} like ?")

	private ResourceType resourcetype;

	@Column(name = "ResourceTypeName", length = 255)
	@DisplaySize(40)
	@Required
	private String resourcetypename;

	@ManyToOne

	@DescriptionsList(descriptionProperties = "resourcetypename", condition = "resourcesubtypesynonym is null")
	@OnChange(OnChangeRSTSyn.class)
	private ResourceSubType resourcesubtypesynonym;

	@Column(name = "ResourceSubTypeUnit", length = 20)
	@Required
	private String resourcesubtypeunit;

	@DefaultValueCalculator(IntegerCalculator.class)
	// @OnChange(value = OnChangeRSTkcal.class)
	@Column(name = "ResourceSubTypeKCal")
	private int resourcesubtypekcal;

	@ManyToMany(mappedBy = "resourceSubType")
	private Collection<Category> category;

	@ManyToMany
	@NewAction("")
	private Collection<CustomReportSpec> customReportSpecs;

	@OneToMany(mappedBy = "resourceSubType", cascade = CascadeType.REMOVE)
	private Collection<LocalUnit> localUnits;

	@ManyToOne
	@NoModify
	@NoCreate
	@ReferenceView("fromrst")
	@DescriptionsList(descriptionProperties ="foodCode,foodSourceName",showReferenceView = true)
	
	public MCCWFoodSource mccwFoodSource;



	public MCCWFoodSource getMccwFoodSource() {
		return mccwFoodSource;
	}

	public void setMccwFoodSource(MCCWFoodSource mccwFoodSource) {
		this.mccwFoodSource = mccwFoodSource;
	}

	public Collection<LocalUnit> getLocalUnits() {
		return localUnits;
	}

	public void setLocalUnits(Collection<LocalUnit> localUnits) {
		this.localUnits = localUnits;
	}

	public Collection<CustomReportSpec> getCustomReportSpecs() {
		return customReportSpecs;
	}

	public void setCustomReportSpecs(Collection<CustomReportSpec> customReportSpecs) {
		this.customReportSpecs = customReportSpecs;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	public ResourceType getResourcetype() {
		return resourcetype;
	}

	public void setResourcetype(ResourceType resourcetype) {
		this.resourcetype = resourcetype;
	}

	public String getResourcetypename() {
		return resourcetypename;
	}

	public void setResourcetypename(String resourcetypename) {
		this.resourcetypename = resourcetypename;
	}

	public ResourceSubType getResourcesubtypesynonym() {
		return resourcesubtypesynonym;
	}

	public void setResourcesubtypesynonym(ResourceSubType resourcesubtypesynonym) {
		this.resourcesubtypesynonym = resourcesubtypesynonym;
	}

	public String getResourcesubtypeunit() {
		return resourcesubtypeunit;
	}

	public void setResourcesubtypeunit(String resourcesubtypeunit) {
		this.resourcesubtypeunit = resourcesubtypeunit;
	}

	public int getResourcesubtypekcal() {
		return resourcesubtypekcal;
	}

	public void setResourcesubtypekcal(int resourcesubtypekcal) {
		this.resourcesubtypekcal = resourcesubtypekcal;
	}

	public String getIdresourcesubtype() {
		return idresourcesubtype;
	}

	public void setIdresourcesubtype(String idresourcesubtype) {
		this.idresourcesubtype = idresourcesubtype;
	}


	

}
