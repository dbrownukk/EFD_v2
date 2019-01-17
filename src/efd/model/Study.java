package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.validations.*;

@View(members = "Study[#projectlz;studyName,startDate,endDate,referenceYear;description,altCurrency,altExchangeRate]")

@Entity

@Table(name = "Study")

public class Study extends Core {

	/*************************************************************************************************/
	@Required
	private String studyName;
	/*************************************************************************************************/
	@Stereotype("DATE")
	@Column(name = "startDate")
	private java.util.Date startDate;
	/*************************************************************************************************/
	@Stereotype("DATE")
	@Column(name = "endDate")
	private java.util.Date endDate;
	/*************************************************************************************************/
	@Required
	private Integer referenceYear;
	/*************************************************************************************************/
	@Column(length = 45)
	private String description;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true)
	@NoModify
	@NoCreate
	@DescriptionsList(descriptionProperties = "currency")
	private Country altCurrency;
	/*************************************************************************************************/
	@Column(precision = 10, scale = 5)
	@Digits(integer = 10, fraction = 5)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	private BigDecimal altExchangeRate;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@Required
	//@DescriptionsList(descriptionProperties = "projecttitle,pdate")
	//@JoinColumn(name = "CProject")
	//@OnChange(OnChangeClearCommunity.class)
	//@ReferenceView("SimpleProject")
	//@NoCreate
	//@NoModify
	private Project projectlz;
	/*************************************************************************************************/
	@ManyToOne // (fetch = FetchType.LAZY, optional = false)
	//@SearchAction("Community.filteredSitesearch")
	//@NoFrame(forViews = "FromWGCommunity")
	//@ReferenceView("SimpleSite")
	//@JoinColumn(name = "CLocation")
	private Site site;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")//,cascade = CascadeType.ALL)
//@RowAction("Spreadsheet.Template Spreadsheet")
//@CollectionView("FromCommunity")
//@ListProperties("wgnameeng,wgnamelocal,wgorder,wgwives,wghhsize,wgpercent+")
	private Collection<Household> household;

	/*************************************************************************************************/

	public Collection<Household> getHousehold() {
		return household;
	}

	public void setHousehold(Collection<Household> household) {
		this.household = household;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public Integer getReferenceYear() {
		return referenceYear;
	}

	public void setReferenceYear(Integer referenceYear) {
		this.referenceYear = referenceYear;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Country getAltCurrency() {
		return altCurrency;
	}

	public void setAltCurrency(Country altCurrency) {
		this.altCurrency = altCurrency;
	}

	public BigDecimal getAltExchangeRate() {
		return altExchangeRate;
	}

	public void setAltExchangeRate(BigDecimal altExchangeRate) {
		this.altExchangeRate = altExchangeRate;
	}

	public Project getProjectlz() {
		return projectlz;
	}

	public void setProjectlz(Project projectlz) {
		this.projectlz = projectlz;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
