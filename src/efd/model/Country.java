package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.hibernate.annotations.GenericGenerator;


@Entity

@Table(name="Country")


@Tab ( editors ="List, Cards", rowStyles=@RowStyle(style="highlight", property="type", value="steady")
	,properties="isocountrycode, description, currency, currencySymbol") // removes graph option

@Views({
@View(members= "description, isocountrycode,currency,currencySymbol"),  
@View(name="FullCountry",members= "idcountry,isocountrycode,currency,currencySymbol"),
@View(name="SimpleCurrency",members="description, currency"),
@View(name="SimpleCountry",members="description"),
@View(name="SimpleCurrencynoDescription",members="isocountrycode,currency")
})

public class Country {

	@Id
	@Hidden // The property is not shown to the user. It's an internal
			// identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												// (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDCountry", length = 32, unique = true)
	private String idcountry;

	@Column(name = "ISOCountryCode", length = 3, unique = true)
	@Required
	private String isocountrycode;


	@Column(name = "CountryName", length = 45, unique = true)
	@Required
	private String description;

	/* Add Currency and Currency Symbol */
	/* Feb 2018 */
	
	
	@Column(name="Currency",length=3, nullable=false)
	@Required
	private String currency;
	
	@Column(name="CurrencySymbol",length=1)
	private String currencySymbol;
	
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}





}
