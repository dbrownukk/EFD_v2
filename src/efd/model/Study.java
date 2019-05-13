package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import efd.actions.*;
import efd.model.ConfigQuestion.*;

//@View(members = "Study[#studyName,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate]")

@Views({ @View(members = "Study[studyName,projectlz,referenceYear;startDate,endDate, description;altCurrency,altExchangeRate,notes]"
		+ ";warningMessage,"
		+ ";Site{site};StandardOfLivingElement{stdOfLivingElement};DefaultDietItem{defaultDietItem};"
		+ ";CharacteristicAssets/Resources{Land{characteristicsResourceLand}"
		+ "Livestock{characteristicsResourceLivestock}" + "Tradeable{characteristicsResourceTradeable}"
		+ "Foodstock{characteristicsResourceFoodstock}" + "Trees{characteristicsResourceTree}"
		+ "Cash{characteristicsResourceCash}" + "Crops{characteristicsResourceCrop}"
		+ "LivestockSales{characteristicsResourceLivestockSales}"
		+ "LivestockProducts{characteristicsResourceLivestockProducts}"
		+ "Employment{characteristicsResourceEmployment}" + "Transfers{characteristicsResourceTransfers}"
		+ "WildFoods{characteristicsResourceWildFoods},Inputs{characteristicsResourceInputs}},Questions{configQuestionUse},StudyQuestionAnswers{configAnswer}"),
		// + "ConfigQuestionUse{configQuestionUse}"),
		@View(name = "FromStdOfLiving", members = "studyName,referenceYear"),
		@View(name = "StudyInterview", members = "studyName,referenceYear,spreadsheets"),
		@View(name = "FromQuestionUse", members = "Study[#studyName,topic,referenceYear,startDate,endDate;description,altCurrency,altExchangeRate]") })


//@Tabs({
//@Tab(name="StudyInterview", properties = "studyName,referenceYear, household.householdName")
//})

@Entity

@Table(name = "Study",

		uniqueConstraints = {
				@UniqueConstraint(name = "studyrefyear", columnNames = { "studyName", "referenceYear" }) })

public class Study extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {
		try {
			System.out.println("tot = " + getTotalDietPercentage());
			if (getTotalDietPercentage() > 100) {
				throw new IllegalStateException(

						XavaResources.getString("diet_greater_100"));

			}

		} catch (Exception ex) {
			System.out.println("in jpa exception in study " + ex);
			return;

		}

	}

	/*************************************************************************************************/
	@Required
	@Column(length = 45)
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
	@Range(min=1960,max=2050)
	
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
	@ManyToOne //(fetch = FetchType.LAZY, // The reference is loaded on demand
			// optional = false)
	// @Required NOT required in OIHM a Study can just be part of a Project without
	// LZ and Site, but may have an LZ and Site
	@DescriptionsList(descriptionProperties = "projecttitle,pdate") //,  showReferenceView=true)
	@JoinColumn(name = "CProject")
	// @OnChange(OnChangeClearCommunity.class)
	@Required
	@ReferenceView("SimpleProject")
	//@NoCreate
	//@NoModify
	private Project projectlz;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study" , cascade=CascadeType.REMOVE)
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,amount,cost,level")
	@EditAction("StdOfLivingElement.edit")
	private Collection<StdOfLivingElement> stdOfLivingElement;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study",  cascade=CascadeType.REMOVE)
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,percentage+,unitPrice")
	private Collection<DefaultDietItem> defaultDietItem;

	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true) // cascade = CascadeType.REMOVE)
	@SearchAction("Community.filteredSitesearch")
	// @NoFrame(forViews = "FromWGCommunity")
	@ReferenceView("FromStudy")
	@JoinColumn(name = "CLocation")
	private Site site;

	/*************************************************************************************************/
	@OneToMany(mappedBy = "study" , cascade=CascadeType.REMOVE)
	private Collection<Household> household;
	/*************************************************************************************************/
	// Assets
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study")
	// @ElementCollection // Not usable with EditAction
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Land')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@NewAction("CharacteristicsResource.new")
	@AddAction("")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceLand;
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study")
	//@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock')"
	//		+ "AND ${this.id} = ${study.id}")
	@Condition("${type} = 'Livestock' AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@NewAction("CharacteristicsResource.new")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestock;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Other Tradeable Goods')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@NewAction("CharacteristicsResource.new")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceTradeable;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	//@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename in ('Crops','Wild Foods','Livestock Products') ${this.id} = ${study.id}")
	//@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Food Stocks')"  
	//		+ "AND ${this.id} = ${study.id}")
	@Condition("${type} = 'Food Stocks' AND ${this.id} = ${study.id}")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	private Collection<WGCharacteristicsResource> characteristicsResourceFoodstock;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Trees')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceTree;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Cash')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceCash;

	
	/*************************************************************************************************/
	// Non Assets
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study")
	// @ElementCollection //- will not work with SearchListCondition
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Crops')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceCrop;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	//@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock Sales')"
	//		+ "AND ${this.id} = ${study.id}")
	@Condition("${type} = 'Livestock Sales' AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestockSales;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Livestock Products')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@EditAction("CharacteristicsResource.edit")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceLivestockProducts;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Employment')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceEmployment;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Transfers')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceTransfers;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "study")
	@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Wild Foods')"
			+ "AND ${this.id} = ${study.id}")
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceWildFoods;
	
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study")
	//@Condition("${resourcesubtype.resourcetype.idresourcetype} = (SELECT r.idresourcetype from ResourceType r where r.resourcetypename = 'Non Food Purchase')"
	//		+ "AND ${this.id} = ${study.id}")
	
	@Condition("${type} = 'Inputs' AND ${this.id} = ${study.id}")
	
	@ListProperties("resourcesubtype.resourcetypename,wgresourceunit")
	@EditAction("CharacteristicsResource.edit")
	@AddAction("")
	@NewAction("CharacteristicsResource.new")
	@SaveAction("CharacteristicsResource.save")
	private Collection<WGCharacteristicsResource> characteristicsResourceInputs;
	/*************************************************************************************************/

	@OneToMany(mappedBy = "study" ,cascade=CascadeType.REMOVE)
	@XOrderBy("configQuestion.level asc")
	@NewAction("ConfigQuestionUse.new")
	@EditAction("ConfigQuestionUse.edit")
	@SaveAction("ConfigQuestionUse.save")
	@ListProperties("configQuestion.prompt,configQuestion.hint,configQuestion.gender,"
			+ "configQuestion.level,configQuestion.answerType,configQuestion.ageRangeLower,configQuestion.ageRangeUpper")

	private Collection<ConfigQuestionUse> configQuestionUse;

	/*************************************************************************************************/
	/*
	 * Copy from a Topic a list of Questions to use - fails if Questions already
	 * exist
	 */


	public Integer totalDietPercentage() {
		Integer result = new Integer("0");
		for (DefaultDietItem detail : getDefaultDietItem())
			result = result + detail.getPercentage();

		return result;
	}

	/*************************************************************************************************/
	/*
	 * get default diet % running total
	 */

	public Integer getTotalDietPercentage() {
		Integer result = new Integer("0");
		for (DefaultDietItem defaultDietItem : getDefaultDietItem()) { // We iterate through all details
			result = result + defaultDietItem.getPercentage(); // Adding up the amount
		}
		return result;
	}

	@Depends("totalDietPercentage")
	@Stereotype("LABEL") // @Stereotype("HTML_TEXT")
	@Transient
	@ReadOnly
	public String getWarningMessage() {
		if (getTotalDietPercentage() != 100) {
			return "<font color=" + "red" + ">Total Diet Items Percent is not equal to 100</font>";
		}
		return null;
	}


	/*************************************************************************************************/
	@OneToMany(mappedBy = "study", cascade=CascadeType.REMOVE)
	//@NoCreate
	//@AddAction("")
	@EditAction("ConfigAnswer.edit")
	@ListProperties("configQuestionUse.configQuestion.prompt,configQuestionUse.configQuestion.answerType,displayAnswer")
	@Condition("${configQuestionUse.configQuestion.level} = 'Study' AND ${study.id} =${this.id}")   // Note use of JPA syntax not sql
	
	@SaveAction("ConfigAnswer.save")
	@EditOnly
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/

	@OneToMany(mappedBy="study")
	private Collection <ReportSpecUse> reportSpecUse;
	
	
	/*************************************************************************************************/

	
	
	
	
	
	
	
	
	public String getStudyName() {
		return studyName;
	}





	public Collection<ReportSpecUse> getReportSpecUse() {
		return reportSpecUse;
	}

	public void setReportSpecUse(Collection<ReportSpecUse> reportSpecUse) {
		this.reportSpecUse = reportSpecUse;
	}

	public Collection<ConfigAnswer> getConfigAnswer() {
		return configAnswer;
	}

	public void setConfigAnswer(Collection<ConfigAnswer> configAnswer) {
		this.configAnswer = configAnswer;
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

	public Collection<WGCharacteristicsResource> getCharacteristicsResourceInputs() {
		return characteristicsResourceInputs;
	}

	public void setCharacteristicsResourceInputs(Collection<WGCharacteristicsResource> characteristicsResourceInputs) {
		this.characteristicsResourceInputs = characteristicsResourceInputs;
	}



	
	
	
	/*************************************************************************************************/

}
