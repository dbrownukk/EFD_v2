package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

//@View(members = "Study[#studyName,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate]")

@Views({ @View(members = "Study[#projectlz;studyName,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate,notes]"
		+ ";site;Household{household};StandardOfLivingElement{stdOfLivingElement};DefaultDietItem{defaultDietItem}"
		+ "Land{characteristicsResourceLand}" 
		+ "Livestock{characteristicsResourceLivestock}" 
		+ "Tradeable{characteristicsResourceTradeable}" 
		+ "Foodstock{characteristicsResourceFoodstock}" 
		+ "Tress{characteristicsResourceTree}" 
		+ "Cash{characteristicsResourceCash}" 
		+ "Crops{characteristicsResourceCrop}"
		+ "LivestockSales{characteristicsResourceLivestockSales}"
		+ "LivestockProducts{characteristicsResourceLivestockProducts}"
		+ "Employment{characteristicsResourceEmployment}"
		+ "Transfers{characteristicsResourceTransfers}"
		+ "WildFoods{characteristicsResourceWildFoods}"
	+ "ConfigQuestionUse{configQuestionUse}"),
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
	// Assets
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Land')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceLand;
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestock;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Tradeable')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceTradeable;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Food Stocks')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	private Collection<WGCharacteristicsResource> characteristicsResourceFoodstock;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Trees')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceTree;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Cash')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceCash;
	/*************************************************************************************************/
	// Non Assets
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	// @ElementCollection //- will not work with SearchListCondition
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Crops')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceCrop;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock Sales')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestockSales;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock Products')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestockProducts;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Employment')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceEmployment;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Transfers')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceTransfers;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Wild Foods')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	private Collection<WGCharacteristicsResource> characteristicsResourceWildFoods;
	/*************************************************************************************************/
	
	@OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
	
	
	private Collection<ConfigQuestionUse> configQuestionUse;
	/*************************************************************************************************/
	
	

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

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceCrop() {
		return characteristicsResourceCrop;
	}

	public void setCharacteristicsResourceCrop(Collection<WGCharacteristicsResource> characteristicsResourceCrop) {
		this.characteristicsResourceCrop = characteristicsResourceCrop;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceTree() {
		return characteristicsResourceTree;
	}

	public void setCharacteristicsResourceTree(Collection<WGCharacteristicsResource> characteristicsResourceTree) {
		this.characteristicsResourceTree = characteristicsResourceTree;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceLand() {
		return characteristicsResourceLand;
	}

	public void setCharacteristicsResourceLand(Collection<WGCharacteristicsResource> characteristicsResourceLand) {
		this.characteristicsResourceLand = characteristicsResourceLand;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceLivestock() {
		return characteristicsResourceLivestock;
	}

	public void setCharacteristicsResourceLivestock(
			Collection<WGCharacteristicsResource> characteristicsResourceLivestock) {
		this.characteristicsResourceLivestock = characteristicsResourceLivestock;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceTradeable() {
		return characteristicsResourceTradeable;
	}

	public void setCharacteristicsResourceTradeable(
			Collection<WGCharacteristicsResource> characteristicsResourceTradeable) {
		this.characteristicsResourceTradeable = characteristicsResourceTradeable;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceFoodstock() {
		return characteristicsResourceFoodstock;
	}

	public void setCharacteristicsResourceFoodstock(
			Collection<WGCharacteristicsResource> characteristicsResourceFoodstock) {
		this.characteristicsResourceFoodstock = characteristicsResourceFoodstock;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceCash() {
		return characteristicsResourceCash;
	}

	public void setCharacteristicsResourceCash(Collection<WGCharacteristicsResource> characteristicsResourceCash) {
		this.characteristicsResourceCash = characteristicsResourceCash;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceLivestockSales() {
		return characteristicsResourceLivestockSales;
	}

	public void setCharacteristicsResourceLivestockSales(
			Collection<WGCharacteristicsResource> characteristicsResourceLivestockSales) {
		this.characteristicsResourceLivestockSales = characteristicsResourceLivestockSales;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceLivestockProducts() {
		return characteristicsResourceLivestockProducts;
	}

	public void setCharacteristicsResourceLivestockProducts(
			Collection<WGCharacteristicsResource> characteristicsResourceLivestockProducts) {
		this.characteristicsResourceLivestockProducts = characteristicsResourceLivestockProducts;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceEmployment() {
		return characteristicsResourceEmployment;
	}

	public void setCharacteristicsResourceEmployment(
			Collection<WGCharacteristicsResource> characteristicsResourceEmployment) {
		this.characteristicsResourceEmployment = characteristicsResourceEmployment;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceTransfers() {
		return characteristicsResourceTransfers;
	}

	public void setCharacteristicsResourceTransfers(
			Collection<WGCharacteristicsResource> characteristicsResourceTransfers) {
		this.characteristicsResourceTransfers = characteristicsResourceTransfers;
	}

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceWildFoods() {
		return characteristicsResourceWildFoods;
	}

	public void setCharacteristicsResourceWildFoods(
			Collection<WGCharacteristicsResource> characteristicsResourceWildFoods) {
		this.characteristicsResourceWildFoods = characteristicsResourceWildFoods;
	}

	public Collection<ConfigQuestionUse> getConfigQuestionUse() {
		return configQuestionUse;
	}

	public void setConfigQuestionUse(Collection<ConfigQuestionUse> configQuestionUse) {
		this.configQuestionUse = configQuestionUse;
	}

	
	
	/*************************************************************************************************/

}