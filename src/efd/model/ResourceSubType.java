package efd.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Entity



@Views({
	 @View(members="resourcetype;resourcetypename;resourcesubtypeunit;resourcesubtypekcal;resourcesubtypesynonym"),
	 @View(name="SimpleSubtype", members="resourcetypename")
	})


@Tab(properties="resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")



@Table(name = "ResourceSubType",uniqueConstraints=@UniqueConstraint(columnNames={"ReourceType","ResourceTypeName"}))
public class ResourceSubType {

	@Id	
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceSubType", length = 32, unique = true)
	private String idresourcesubtype;
	
	@ManyToOne     
	@Required
	@JoinColumn(name = "ReourceType")
	@DescriptionsList(descriptionProperties="resourcetypename")
	private ResourceType resourcetype;
	
	
	@Column(name = "ResourceTypeName", length=255, unique=true )  // ? ResosurceSubTypeName ?
	@Required
	private String resourcetypename;
	
	@ManyToOne
    //@Column(name = "ResourceSubTypeSynonym")  // Cannot define Column name in ManyToOne JPA 	
	@DescriptionsList(descriptionProperties="resourcetypename")
	private  ResourceSubType resourcesubtypesynonym;
	
	@Column(name = "ResourceSubTypeUnit", length=20)  
	private String resourcesubtypeunit;
	
	@Column(name = "ResourceSubTypeKCal")  
	private int resourcesubtypekcal;



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
