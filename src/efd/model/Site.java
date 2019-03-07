package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

import com.openxava.naviox.model.*;

import efd.validations.*;
import efd.actions.*;

@Views({ @View(members = "Site[#livelihoodZone;country;locationdistrict;subdistrict;gpslocation]"),
//	@Views({ @View(members = "Site[locationdistrict;subdistrict;gpslocation]"),
		@View(name = "SimpleSite", members = "locationdistrict;subdistrict;gpslocation;livelihoodZone"),
//		@View(name = "SimpleSite", members = "locationdistrict"),
		@View(name = "LZSite", members = "locationdistrict;subdistrict;gpslocation"),
		@View(name = "FromWealthGroup", members = "locationdistrict,subdistrict,livelihoodZone;"),
		@View(name = "FromStudy", members = "locationdistrict,subdistrict,gpslocation;country, livelihoodZone"),
		@View(name = "NewlineSite", members = "locationdistrictsubdistrict;gpslocation;livelihoodZone;") })

@Entity

@Tab(editors = "List", rowStyles = @RowStyle(style = "row-highlight", property = "type", value = "steady"), properties = "livelihoodZone.lzname,country.description,locationdistrict,subdistrict,gpslocation"
// , properties = "livelihoodZone.project.projecttitle,livelihoodZone.lzname,locationdistrict,subdistrict,gpslocation"  // cannot show project as there maybe more than 1 for this LZ / Site combo
		, defaultOrder = "${livelihoodZone.lzname} asc,${locationdistrict} asc,${subdistrict} asc")

@EntityValidator(value = OIHMCountryLZ.class, properties = { @PropertyValue(name = "country"),
		@PropertyValue(name = "livelihoodZone"), @PropertyValue(name = "model") }) // Cannot use Inject in validation
																					// code

@Table(name = "Site", uniqueConstraints = {
		@UniqueConstraint(name = "uk_lz_district_sub", columnNames = { "lz", "LocationDistrict", "SubDistrict" }) })

public class Site {

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "LocationID", length = 32, unique = true)
	private String locationid;

	@Column(name = "LocationDistrict", length = 25)
	@Required
	// @OnChange(value = OnChangeSiteCountry.class)
	private String locationdistrict;

	@Column(name = "SubDistrict", length = 25)

	private String subdistrict;

	@Column(name = "GPSLocation", length = 25)

	private String gpslocation;

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true, cascade = CascadeType.REMOVE)
	// @SearchAction(notForViews="DEFAULT", value="LivelihoodZone.filteredsearch")
	@SearchAction(value = "LivelihoodZone.filteredsearch")

	@ReferenceView("SimpleLZnomap")
	@NoFrame
	// @NoCreate
	// @NoModify
	@JoinColumn(name = "LZ")
	// @OnChange(value = OnChangeSiteCountry.class)
	private LivelihoodZone livelihoodZone;

	@OneToMany(mappedBy = "site", cascade = CascadeType.REMOVE)
	@ListProperties("cinterviewdate,cinterviewsequence,civf,civm")
	private Collection<Community> community;

	// OIHM LZ is now optional Site must now be for a Country or LZ
	// Need validation code

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@NoFrame
	// @SearchAction(value="LivelihoodZone.filteredsearch")
	@ReferenceView("SimpleCountry")
	// @OnChange(value = OnChangeSiteCountry.class)
	private Country country;

	@OneToMany(mappedBy = "site", cascade = CascadeType.REMOVE)
	// @ListProperties("cinterviewdate,cinterviewsequence,civf,civm")
	private Collection<Study> study;

	@Hidden
	@Transient
	private String model;

	public String getModel() {
		String userName = Users.getCurrent();
		User user = User.find(userName);
		String r = null;

		if (user.hasRole("oihm_user"))
			r = "OIHM";
		else if (user.hasRole("ohea_user"))
			r = "OHEA";
		return r;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

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

	public Collection<Study> getStudy() {
		return study;
	}

	public void setStudy(Collection<Study> study) {
		this.study = study;
	}

}
