package efd.model;
import java.util.*; 


import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import com.openxava.naviox.model.*;


@Views({
	 @View(members="resourcetypename;resourcesubtype"),
	 @View(name="SimpleSubtype", members="resourcetypename")
	})


@Entity
public class ResourceType {
	
	//----------------------------------------------------------------------------------------------//
	@Id
	@Hidden // The property is not shown to the user. It's an internal identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceType", length = 32, unique = true)
	private String idresourcetype;
	//----------------------------------------------------------------------------------------------//
	@Column(name = "ResourceTypeName", length=255, unique=true)  // ?? Should be a string ??
	@Required
	@DisplaySize(50)
	private String resourcetypename;
	//----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy="resourcetype") //,cascade=CascadeType.REMOVE)
	@ListProperties("resourcetypename;resourcesubtypesynonym;resourcesubtypeunit;resourcesubtypekcal")
	private Collection<ResourceSubType> resourcesubtype;
	//----------------------------------------------------------------------------------------------//

	@ManyToMany
	@NewAction("")
	private Collection<CustomReportSpec> customReportSpecs;
	
	@Hidden
	private String availableInNutrientsMenu;
	
	
	
	
	public String getAvailableInNutrientsMenu() {
		return availableInNutrientsMenu;
	}

	public void setAvailableInNutrientsMenu(String availableInNutrientsMenu) {
		this.availableInNutrientsMenu = availableInNutrientsMenu;
	}

	public String getIdresourcetype() {
		return idresourcetype;
	}

	public void setIdresourcetype(String idresourcetype) {
		this.idresourcetype = idresourcetype;
	}

	public String getResourcetypename() {
		return resourcetypename;
	}

	public void setResourcetypename(String resourcetypename) {
		this.resourcetypename = resourcetypename;
	}

	public Collection<ResourceSubType> getResourcesubtype() {
		return resourcesubtype;
	}

	public void setResourcesubtype(Collection<ResourceSubType> resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}

	public Collection<CustomReportSpec> getCustomReportSpecs() {
		return customReportSpecs;
	}

	public void setCustomReportSpecs(Collection<CustomReportSpec> customReportSpecs) {
		this.customReportSpecs = customReportSpecs;
	}


	
}
