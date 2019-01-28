package efd.model;

import java.math.*;
import java.text.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.lang.time.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.SearchAction;
import org.openxava.annotations.Tab;
import org.openxava.calculators.*;
import org.openxava.validators.*;
import org.openxava.actions.*;

import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import com.openxava.naviox.model.*;

import efd.actions.*;
import efd.model.Asset.*;

import org.openxava.tab.*;

//@View(members = "Study[#studyName,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate]")

@Views({ @View(members = "Study[#projectlz;studyName,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate,notes]"
		+ ";site;Household{household};StandardOfLivingElement{stdOfLivingElement};DefaultDietItem{defaultDietItem}"
		+ ";CharacteristicResource{characteristicsResource}"),
		@View(name = "FromStdOfLiving", members = "studyName,referenceYear") })

@Entity

@Table(name = "Study")

public class Study extends EFDIdentifiable {

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
	@DescriptionsList(descriptionProperties = "projecttitle,pdate")
	@JoinColumn(name = "CProject")
	// @OnChange(OnChangeClearCommunity.class)
	@ReferenceView("SimpleProject")
	// @NoCreate
	// @NoModify
	private Project projectlz;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@ListProperties("study.studyName,resourcesubtype.resourcetypename,amount,cost,level,gender,ageRangeLower,ageRangeUpper")
	private Collection<StdOfLivingElement> stdOfLivingElement;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@ListProperties("study.studyName,resourcesubtype.resourcetypename,percentage,unitPrice")
	private Collection<DefaultDietItem> defaultDietItem;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@SearchAction("Community.filteredSitesearch")
	@NoFrame(forViews = "FromWGCommunity")
	@ReferenceView("SimpleSite")
	@JoinColumn(name = "CLocation")
	private Site site;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	private Collection<Household> household;
	/*************************************************************************************************/
	// @OneToMany(mappedBy = "study", cascade=CascadeType.REMOVE)
	@ElementCollection
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit,wgresourceamount")
	private Collection<WGCharacteristicsResource> characteristicsResource;

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

	public Collection<StdOfLivingElement> getStdOfLivingElement() {
		return stdOfLivingElement;
	}

	public void setStdOfLivingElement(Collection<StdOfLivingElement> stdOfLivingElement) {
		this.stdOfLivingElement = stdOfLivingElement;
	}

	public Collection<DefaultDietItem> getDefaultDietItem() {
		return defaultDietItem;
	}

	public void setDefaultDietItem(Collection<DefaultDietItem> defaultDietItem) {
		this.defaultDietItem = defaultDietItem;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Collection<Household> getHousehold() {
		return household;
	}

	public void setHousehold(Collection<Household> household) {
		this.household = household;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResource() {
		return characteristicsResource;
	}

	public void setCharacteristicsResource(Collection<WGCharacteristicsResource> characteristicsResource) {
		this.characteristicsResource = characteristicsResource;
	}

	/*************************************************************************************************/

	/*************************************************************************************************/

}
