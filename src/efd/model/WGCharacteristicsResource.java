package efd.model;

import java.math.*;

import javax.persistence.*;
import javax.persistence.Entity;
import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Entity
@View(members="wgresourcesubtype;wgresourceunit;wgresourceamount;resourcesubtype")
public class WGCharacteristicsResource {

	
	@Id
	@Hidden // The property is not shown to the user. It's an internal identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "WGID", length = 32, unique = true)
	private String wgid;
	
	@ManyToOne(fetch = FetchType.LAZY, // This is FK to Site == Location
			optional = false)
	//@ReferenceView("SimpleSite")
	@JoinColumn(name = "IDWGResource")
	@Required
	private ResourceSubType resourcesubtype;
	
	@Column(name = "WGResourceSubType")     // think this should be FK not 
	@Required
	private Integer wgresourcesubtype;
	
	@Column(name = "WGResourceUnit",length = 255)    
	private String wgresourceunit;
	
	@Stereotype("MONEY")
	@Column(name = "WGResourceAmount")    
	private BigDecimal wgresourceamount;

	public String getWgid() {
		return wgid;
	}

	public void setWgid(String wgid) {
		this.wgid = wgid;
	}

	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}

	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
	}

	public Integer getWgresourcesubtype() {
		return wgresourcesubtype;
	}

	public void setWgresourcesubtype(Integer wgresourcesubtype) {
		this.wgresourcesubtype = wgresourcesubtype;
	}

	public String getWgresourceunit() {
		return wgresourceunit;
	}

	public void setWgresourceunit(String wgresourceunit) {
		this.wgresourceunit = wgresourceunit;
	}

	public BigDecimal getWgresourceamount() {
		return wgresourceamount;
	}

	public void setWgresourceamount(BigDecimal wgresourceamount) {
		this.wgresourceamount = wgresourceamount;
	}
	
	
	
}
