package efd.model;

import java.math.*;

import javax.inject.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.actions.*;

@Entity



@Views({ @View(members = "Characteristics_Resources[resourcesubtype;wgresourceunit,type]"),
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


@Table(name = "wgcharacteristicsresource",uniqueConstraints = {
		@UniqueConstraint(name = "studyrst", columnNames = { "study_id", "WGResourceSubType","type" }) ,
		@UniqueConstraint(name="community_wgc",columnNames = { "WGID", "WGResourceSubType"}) 	 })



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
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "WGID")
	//@Required     Now either a WG or a Study
	@NoFrame
	private WealthGroup wealthgroup;
	// ----------------------------------------------------------------------------------------------//
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoFrame
	private Study study;
	// ----------------------------------------------------------------------------------------------//
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WGResourceSubType")
	@Required
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename")
	//condition="e.resourcetype.resourcetypename = 'Land'")
	@OnChange(OnChangeResourceType.class)  // class is not used..
	//@EditAction("CharacteristicsResource.edit")
	
	
	private ResourceSubType resourcesubtype;

	// ----------------------------------------------------------------------------------------------//
	@Column(name = "WGResourceUnit", length = 255)
	private String wgresourceunit;

	// ----------------------------------------------------------------------------------------------//
	@Stereotype("MONEY")
	@Column(name = "WGResourceAmount")
	@DefaultValueCalculator(value = ZeroBigDecimalCalculator.class)
	private BigDecimal wgresourceamount;

	// ----------------------------------------------------------------------------------------------//
	// which sessiontab used in?
	@Hidden
	private String type;
	
	// ----------------------------------------------------------------------------------------------//

	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

}