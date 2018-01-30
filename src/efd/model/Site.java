package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

@Views({ @View(members = "Site[livelihoodZone,locationdistrict,subdistrict,gpslocation]"),
		@View(name = "SimpleSite", members = "locationdistrict;subdistrict;gpslocation;livelihoodZone;"),
		@View(name = "LZSite", members = "locationdistrict;subdistrict;gpslocation"),
		@View(name = "FromWealthGroup", members = ";locationdistrict,subdistrict,livelihoodZone;"),
		@View(name = "NewlineSite", members = "locationdistrictsubdistrict;gpslocation;livelihoodZone;") })

@Entity

@Tab(editors = "List, Cards", rowStyles = @RowStyle(style = "row-highlight", property = "type", value = "steady"), properties = "livelihoodZone.lzname,locationdistrict,subdistrict,gpslocation", defaultOrder = "${livelihoodZone.lzname} asc,${locationdistrict} asc,${subdistrict} asc")
//@Tab(properties = "livelihoodZone.lzname,locationdistrict,subdistrict,gpslocation")



@Table(name = "Site", uniqueConstraints = {
		@UniqueConstraint(name = "uk_lz_district_sub", columnNames = { "LZ", "LocationDistrict", "SubDistrict" }) })

public class Site {

	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "LocationID", length = 32, unique = true)
	private String locationid;

	@Column(name = "LocationDistrict", length = 25)
	@Required
	private String locationdistrict;

	@Column(name = "SubDistrict", length = 25)
	private String subdistrict;

	@Column(name = "GPSLocation", length = 25)
	private String gpslocation;

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@JoinColumn(name = "LZ")
	@DescriptionsList(descriptionProperties = "lzname")
	private LivelihoodZone livelihoodZone;

	@OneToMany(mappedBy = "site", cascade = CascadeType.REMOVE)
	@ListProperties("cinterviewdate,cinterviewsequence,civf,civm")
	private Collection<Community> community;

	public String getLocationid() {
		return locationid;
	}

	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}

	public String getLocationdistrict() {
		return locationdistrict;
	}

	public void setLocationdistrict(String locationdistrict) {
		this.locationdistrict = locationdistrict;
	}

	public String getSubdistrict() {
		return subdistrict;
	}

	public void setSubdistrict(String subdistrict) {
		this.subdistrict = subdistrict;
	}

	public String getGpslocation() {
		return gpslocation;
	}

	public void setGpslocation(String gpslocation) {
		this.gpslocation = gpslocation;
	}

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public Collection<Community> getCommunity() {
		return community;
	}

	public void setCommunity(Collection<Community> community) {
		this.community = community;
	}

}
