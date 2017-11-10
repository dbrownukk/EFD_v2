package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Entity



@Views({
	@View(members="resourcetypename;resourcesubtypesynonym;resourcesubtypeunit;resourcesubtypekcal"),
	 @View(name="SimpleSubtype", members="resourcetypename")
	})

public class ResourceSubType {

	@Id
	@Hidden // The property is not shown to the user. It's an internal identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceSubType", length = 32, unique = true)
	private String idresourcesubtype;
	
	@ManyToOne(fetch = FetchType.LAZY, 
			optional = false)
	@Required
	@JoinColumn(name = "ReourceType")
	@DescriptionsList(descriptionProperties="resourcetypename")
	private ResourceType resourcetype;
	
	
	@ManyToOne
    //	@Column(name = "ResourceSubTypeSynonym")  
	//@ReferenceView("SimpleSubtype")
	@DescriptionsList(descriptionProperties="resourcetypename")
	private  ResourceSubType resourcesubtypesynonym;
	
	@Column(name = "ResourceTypeName", length=255)  // Should be ResourceSubTypeName?
	@Required
	private String resourcetypename;
	
	@Column(name = "ResourceSubTypeUnit")  // ?? Should be a string ??
	private BigDecimal resourcesubtypeunit;
	
	@Column(name = "ResourceSubTypeKCal")  // ?? Should be a string ?
	private int resourcesubtypekcal;

	public String getIdresourcesubtype() {
		return idresourcesubtype;
	}

	public void setIdresourcesubtype(String idresourcesubtype) {
		this.idresourcesubtype = idresourcesubtype;
	}

	public ResourceType getResourcetype() {
		return resourcetype;
	}

	public void setResourcetype(ResourceType resourcetype) {
		this.resourcetype = resourcetype;
	}

	public ResourceSubType getResourcesubtypesynonym() {
		return resourcesubtypesynonym;
	}

	public void setResourcesubtypesynonym(ResourceSubType resourcesubtypesynonym) {
		this.resourcesubtypesynonym = resourcesubtypesynonym;
	}

	public String getResourcetypename() {
		return resourcetypename;
	}

	public void setResourcetypename(String resourcetypename) {
		this.resourcetypename = resourcetypename;
	}

	public BigDecimal getResourcesubtypeunit() {
		return resourcesubtypeunit;
	}

	public void setResourcesubtypeunit(BigDecimal resourcesubtypeunit) {
		this.resourcesubtypeunit = resourcesubtypeunit;
	}

	public int getResourcesubtypekcal() {
		return resourcesubtypekcal;
	}

	public void setResourcesubtypekcal(int resourcesubtypekcal) {
		this.resourcesubtypekcal = resourcesubtypekcal;
	}


	
	
}
