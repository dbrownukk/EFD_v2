package efd.model;

import com.openxava.naviox.model.*;

import efd.validations.*;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

@Entity

@Views({ @View(members = "resourcetype;resourcetypename;resourcesubtypeunit;resourcesubtypekcal;resourcesubtypesynonym,category"),
		@View(name = "FromCategory", members = "resourcetype,resourcetypename,resourcesubtypeunit;resourcesubtypekcal,resourcesubtypesynonym"),
		@View(name = "SimpleSubtype", members = "resourcetypename"),
		@View(name = "FromLocalUnit", members = "resourcetype;resourcetypename,resourcesubtypeunit") })

@Tab(properties = "resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")

@Table(name = "ResourceSubType", uniqueConstraints = @UniqueConstraint(columnNames = { "ReourceType",
		"ResourceTypeName" }))
public class ResourceSubType {

	@PreUpdate
	

	private void stopRTupdate() {
		System.out.println("in stopRTupdate");
		ResourceSubType rst = XPersistence.getManager().find(ResourceSubType.class, this.idresourcesubtype);
		
		System.out.println("found rst "+rst.resourcetype.getResourcetypename());
		
		System.out.println("rst 1 222  = "+getResourcetype().getResourcetypename()+" "+rst.getResourcetype().getResourcetypename());
		
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
	@OnChange(OnChangeRT.class)
	@DescriptionsList(descriptionProperties = "resourcetypename")
	private ResourceType resourcetype;

	@Column(name = "ResourceTypeName", length = 255)
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

	@OneToMany(mappedBy = "resourceSubType")
	private Collection<LocalUnit> localUnits;

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
