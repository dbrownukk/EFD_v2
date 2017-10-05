package reasyst.efd;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.hibernate.annotations.GenericGenerator;

/* @View(members="[isocountrycode,description]") */ 

@Entity

/*
@Table(name="Country")
*/

public class Country {

	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDCountry", length = 32, unique = true)
	private String idcountry;

	@Column(name = "ISOCountryCode", length = 2, unique = true)
	@Required
	private String isocountrycode;

	@Column(name = "CountryName", length = 42, unique = true)
	@Required
	private String countryname;

	public String getIdcountry() {
		return idcountry;
	}

	public void setIdcountry(String idcountry) {
		this.idcountry = idcountry;
	}

	public String getIsocountrycode() {
		return isocountrycode;
	}

	public void setIsocountrycode(String isocountrycode) {
		this.isocountrycode = isocountrycode;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}



}
