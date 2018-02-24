package efd.model;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;



@Views({ @View(members="Livelihood_Zone[lzname;country,site]"),
		@View(name = "UpdateLZ", members = "Livelihood Zone[lzname,country,project,site]"),
		@View(name = "CreateLZ", members = "Livelihood Zone[lzname;country;lzzonemap]"),
		@View(name = "SimpleLZ", members = "lzname;country") })

@Tab(properties = "lzname,country.description,country.isocountrycode,country.currency")
											

@Entity

@Table(name = "LivelihoodZone", uniqueConstraints = @UniqueConstraint(columnNames = { "LZName", "LZCountry" }))

public class LivelihoodZone {


	
	
	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "LZID", length = 32, unique = true)
	private String lzid;

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@NoModify
	@NoCreate
	@Required
	@JoinColumn(name = "LZCountry")
	@NoFrame//(forViews="SimpleLZ")
	
	@ReferenceViews({
	@ReferenceView(forViews="SimpleLZ",value="SimpleCountry"),
	@ReferenceView(forViews="DEFAULT",value="FullCountry")
	})
	
	@DescriptionsList
	//@DescriptionsList(showReferenceView=true,descriptionProperties="description", forViews="DEFAULT")
	private Country country;
	
	@Column(name = "LZName", length = 255)
	@Required
	private String lzname;

	@Column(name = "LZZoneMap")
	// private String lzzonemap;
	@Stereotype("IMAGE")
	private byte[] lzzonemap;

	@OneToMany(mappedBy = "livelihoodZone")
	@ListProperties("locationdistrict,subdistrict,gpslocation")
	@CollectionView("LZSite")
	private Collection<Site> site;

	@ManyToMany(mappedBy = "livelihoodZone")
	// @ListProperties("projecttitle,pdate")

	private Collection<Project> project;

	public String getLzid() {
		return lzid;
	}

	public void setLzid(String lzid) {
		this.lzid = lzid;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getLzname() {
		return lzname;
	}

	public void setLzname(String lzname) {
		this.lzname = lzname;
	}

	public byte[] getLzzonemap() {
		return lzzonemap;
	}

	public void setLzzonemap(byte[] lzzonemap) {
		this.lzzonemap = lzzonemap;
	}

	public Collection<Site> getSite() {
		return site;
	}

	public void setSite(Collection<Site> site) {
		this.site = site;
	}

	public Collection<Project> getProject() {
		return project;
	}

	public void setProject(Collection<Project> project) {
		this.project = project;
	}



	/* Get / set */

}
