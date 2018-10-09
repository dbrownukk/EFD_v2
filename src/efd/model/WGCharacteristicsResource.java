package efd.model;

import java.math.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import javax.persistence.Entity;

@Entity



@Views({ @View(members = "Characteristics_Resources[resourcesubtype;wgresourceunit]"),
	@View(name = "DetailCR", members="Project")})

@Tab(rowStyles = @RowStyle(style = "row-highlight", property = "type", value = "steady"), 
properties = "wealthgroup.community.projectlz.projecttitle,"
		+ "wealthgroup.community.site.livelihoodZone.lzname,"
		+ "wealthgroup.community.site.subdistrict,"
		+ "wealthgroup.wgnameeng,"
		+ "resourcesubtype.resourcetype.resourcetypename,"
		+ "resourcesubtype.resourcetypename,"
		+ "wgresourceunit" ,
		defaultOrder="${wealthgroup.community.projectlz.projecttitle} desc"
				+ ", ${wealthgroup.community.site.livelihoodZone.lzname} desc"
				+ ", ${wealthgroup.community.site.subdistrict} desc"
				+ ", ${wealthgroup.wgnameeng} desc"	
		)

public class WGCharacteristicsResource {

	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDWGResource", length = 32, unique = true)
	private String idwgresource;
	
	// ----------------------------------------------------------------------------------------------//
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WGID")
	@Required
	@NoFrame
	private WealthGroup wealthgroup;

	// ----------------------------------------------------------------------------------------------//
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WGResourceSubType")
	@Required
	@NoCreate
	//@NoModify
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename")
	private ResourceSubType resourcesubtype;

	// ----------------------------------------------------------------------------------------------//
	@Column(name = "WGResourceUnit", length = 255)
	private String wgresourceunit;

	// ----------------------------------------------------------------------------------------------//
	@Stereotype("MONEY")
	@Column(name = "WGResourceAmount")
	private BigDecimal wgresourceamount;

	// ----------------------------------------------------------------------------------------------//
	
	public String getIdwgresource() {
		return idwgresource;
	}

	public void setIdwgresource(String idwgresource) {
		this.idwgresource = idwgresource;
	}

	public WealthGroup getWealthgroup() {
		return wealthgroup;
	}

	public void setWealthgroup(WealthGroup wealthgroup) {
		this.wealthgroup = wealthgroup;
	}

	public ResourceSubType getResourcesubtype() {
		return resourcesubtype;
	}

	public void setResourcesubtype(ResourceSubType resourcesubtype) {
		this.resourcesubtype = resourcesubtype;
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