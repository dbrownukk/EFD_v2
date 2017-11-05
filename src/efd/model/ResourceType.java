package efd.model;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Entity
public class ResourceType {

	@Id
	@Hidden // The property is not shown to the user. It's an internal identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceType", length = 32, unique = true)
	private String idresourcetype;
	

	
	@Column(name = "ResourceTypeName", length=255)  // ?? Should be a string ??
	@Required
	private String resourcetypename;
	



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
	

	
	
	
}
