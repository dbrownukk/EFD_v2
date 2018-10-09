package efd.model;

import com.openxava.naviox.model.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity

@Views({ @View(members = "resourcetype;resourcetypename;resourcesubtypeunit;resourcesubtypekcal;resourcesubtypesynonym"),
		@View(name = "SimpleSubtype", members = "resourcetypename") })

@Tab(properties = "resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")

@Table(name = "ResourceSubType", uniqueConstraints = @UniqueConstraint(columnNames = { "ReourceType",
		"ResourceTypeName" }))
public class ResourceSubType {

	@PrePersist
	@PreUpdate

	private void calcKcal() {
		if (resourcesubtypesynonym != null) {
			resourcesubtypekcal = 0;
		}
		System.out.println("About to get Roles  = ");

		String userName = Users.getCurrent();
		User user = User.find(userName);

		System.out.println("Roles done = ");

		// if Role is efd_remote then this is standalone and user can create an RST but
		// must be a synonym for some other RST
		// This assumes XavaPro is used

		System.out.println("Role = " + user.hasRole("efd_remote"));
		System.out.println("Synonym = " + getResourcesubtypesynonym());

		if (user.hasRole("efd_remote") && resourcesubtypesynonym == null) {
			System.out.println("Need to stop update");
			throw new javax.validation.ValidationException(
					XavaResources.getString("Field Users must enter a SubType Synonym"));
		}

	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDResourceSubType", length = 32, unique = true)
	private String idresourcesubtype;

	@ManyToOne
	@Required
	@JoinColumn(name = "ReourceType")
	@DescriptionsList(descriptionProperties = "resourcetypename")
	private ResourceType resourcetype;

	@Column(name = "ResourceTypeName", length = 255, unique = true)
	@Required
	private String resourcetypename;

	@ManyToOne

	@DescriptionsList(descriptionProperties = "resourcetypename")
	private ResourceSubType resourcesubtypesynonym;

	@Column(name = "ResourceSubTypeUnit", length = 20)
	@Required
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
