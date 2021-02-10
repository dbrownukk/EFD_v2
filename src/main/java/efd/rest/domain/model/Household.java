package efd.rest.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.Positive;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;

import efd.rest.domain.model.ConfigQuestion.Level;
import efd.rest.domain.model.WealthGroupInterview.*;
import efd.validations.*;

@View(members = "Household[#study,householdName,householdNumber;interviewers, interviewDate,status;spreadsheet];configAnswer;HouseholdMember{householdMember},"
		+ "Assets{Land{assetLand};LiveStock{assetLiveStock};Tradeable{assetTradeable}" + ";FoodStock{assetFoodStock}"
		+ ";Trees{assetTree};Cash{assetCash}};Crops{crop}"
		+ ";LiveStockSales{livestockSales};LiveStockProducts{livestockProducts};Employment{employment}"
		+ ";Transfer{transfer};WildFood{wildFood};Inputs{inputs}")

//@Tab(filter=ValidHouseholds.class, properties = "study.studyName,study.referenceYear, householdNumber, householdName, status",
//baseCondition="${status}=?")

@Tab(properties = "study.studyName,study.referenceYear, householdNumber, householdName, status")

@Table(name = "Household", uniqueConstraints = {
		@UniqueConstraint(name = "unique_household_number", columnNames = { "study_id", "householdNumber" }),
		@UniqueConstraint(name = "unique_household_name", columnNames = { "study_id", "householdName" }) })

@Entity

public class Household extends EFDIdentifiable {

	@PrePersist

	private void validate() throws Exception {

		int lastNumber = 0;
		String studyid = getStudy().getId();

		// HouseholdName is set to empty string which is not unique. Mysql - null is
		// unique

		if (getHouseholdName().isEmpty()) {
			setHouseholdName(null);
		}
		try {
			TypedQuery<Integer> ilast = XPersistence.getManager()
					.createQuery("select max(householdNumber) from Household where study_id = :studyid", Integer.class);
			ilast.setParameter("studyid", studyid);

			lastNumber = ilast.getSingleResult();

		} catch (Exception ex) {
			System.out.println("exception = " + ex.toString());
			if (lastNumber == 0)
				setHouseholdNumber(1);
			return;
		}

		/*
		 * Add HH Questions
		 */
		updateQuestions();

		/*
		 * #462 If HH Number entered then use that - need to check if number is entered
		 * in Spreadsheet parsing
		 */
		if (getHouseholdNumber() > 0) {
			return;
		}
		if (lastNumber > 0)

		{
			setHouseholdNumber(lastNumber + 1);
		} else {
			setHouseholdNumber(1);
		}
		return;

	}

	private void updateQuestions() {
		for (ConfigQuestionUse configQuestionUse : this.getStudy().getConfigQuestionUse()) {

			if (configQuestionUse.getConfigQuestion().getLevel().equals(Level.Household)) {

				ConfigAnswer ans = new ConfigAnswer();
				ans.setAnswerType(configQuestionUse.getConfigQuestion().getAnswerType());
				ans.setAnswer("");
				ans.setConfigQuestionUse(configQuestionUse);
				ans.setHousehold(this);
				ans.setStudy(configQuestionUse.getStudy());

				XPersistence.getManager().persist(ans);

			}

		}
	}

	@PreUpdate
	private void validate_hhName() throws Exception {

		try {
			if (getHouseholdName().isEmpty())
				setHouseholdName(null);
		} catch (Exception ex) {
			// already null
		}

	}

	@PostUpdate
	/* Check if all questions are included */
	/*
	 * will only update if there are no questions. Will not fix is a question is
	 * missing
	 */
	private void validate_questions() throws Exception {
		int currentQuestionCount = this.getConfigAnswer().size();
		int countHHQuestions = this.getStudy().getConfigQuestionUse().size();

		if (this.getConfigAnswer().isEmpty()) {
			updateQuestions();
		}
	}

	private void validate_hhNumberupdate() throws Exception {

		System.out.println("hhnum = " + getHouseholdNumber());

	}

	@Column(length = 45)
	@DefaultValueCalculator(NullCalculator.class)
	private String householdName;
	/*************************************************************************************************/
	// @Required
	@Positive
	private int householdNumber;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoFrame
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "household", cascade = CascadeType.REMOVE)
	// @SaveAction("HouseholdMember.save")
	private Collection<HouseholdMember> householdMember;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "household", cascade = CascadeType.REMOVE)
	// @ElementCollection
	@NoCreate
	@EditOnly
	@AddAction("")
	// @ListProperties("configQuestionUse.configQuestion.prompt,answer")
	@EditAction("ConfigAnswer.edit")
	@SaveAction("ConfigAnswer.save")
	@ListProperties("configQuestionUse.configQuestion.prompt,configQuestionUse.configQuestion.answerType,displayAnswer")
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Stereotype("FILE")
	@Column(length = 32, name = "spreadsheet")
	private String spreadsheet;

	/*************************************************************************************************/
	@Stereotype("DATE")
	@Column(name = "InterviewDate")
	private Date interviewDate;
	/*************************************************************************************************/
	@Column(name = "Interviewers", length = 45)
	private String interviewers;
	/*************************************************************************************************/

	/* Collections of resource elements */

	@ElementCollection
	// Workaround from Javier
	// https://sourceforge.net/p/openxava/discussion/419690/thread/b6535530de/

	@ListProperties("status,resourceSubType.resourcetypename,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit")
	private Collection<AssetLiveStock> assetLiveStock;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, landTypeEnteredName,unit,numberOfUnits")
	private Collection<AssetLand> assetLand;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,tradeableTypeEnteredName,unit,numberOwned,pricePerUnit")
	private Collection<AssetTradeable> assetTradeable;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,foodTypeEnteredName,unit,quantity")
	private Collection<AssetFoodStock> assetFoodStock;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename,treeTypeEnteredName,unit,numberOwned,pricePerUnit")
	private Collection<AssetTree> assetTree;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,currencyEnteredName,currencyEnteredAmount,exchangeRate,amount")
	private Collection<AssetCash> assetCash;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,cropType,unit,unitsProduced, unitsSold,unitsOtherUse,unitsConsumed,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<Crop> crop;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, livestockType,unit,unitsAtStartofYear, unitsSold,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<LivestockSales> livestockSales;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, livestockType,livestockProduct,unit,unitsProduced, unitsSold,unitsOtherUse,unitsConsumed,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<LivestockProducts> livestockProducts;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,employmentName,peopleCount,unitsWorked,unit,cashPaymentAmount,foodResourceSubType.resourcetypename,foodPaymentFoodType,foodPaymentUnit,foodPaymentUnitsPaidWork"
			+ ",workLocation1,percentWorkLocation1,workLocation2,percentWorkLocation2,workLocation3,percentWorkLocation3")
	private Collection<Employment> employment;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, isOfficial,source,transferType,peopleReceiving,timesReceived,cashTransferAmount,foodResourceSubType.resourcetypename,transferFoodOtherType,"
			+ " unit, unitsTransferred,unitsSold,otherUse,unitsConsumed,pricePerUnit"
			+ ";market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<Transfer> transfer;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename,wildFoodName,unit,unitsProduced,unitsSold,otherUse,unitsConsumed,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<WildFood> wildFood;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, itemPurchased, unit, unitsPurchased, pricePerUnit,resource1UsedFor,percentResource1,resource2UsedFor,percentResource2,resource3UsedFor,percentResource3")
	private Collection<Inputs> inputs;

	@DisplaySize(25)
	@Required
	@OnChange(value = OnChangeSetHHStatus.class)
	private Status status;

	@Transient
	private Double ddiKCalValue;
	@Transient
	private Double ddiPriceValue;
	@Transient
	private Double totalIncome;
	@Transient
	private Double totalIncomeAfterChangeScenario;
	@Transient
	private Double totalOutput;
	@Transient
	private Double totalOutputAfterChangeScenario;
	@Transient
	private Double dIAfterChangeScenario;
	@Transient
	private Double dI;
	@Transient
	private Double SOLC;

	@Transient
	private ExpandabilityRule expandabilityRule;

	@Transient
	public int getHHMCount() {
		return getHouseholdMember().size();
	}

	public Double getSOLC() {
		return SOLC;
	}

	public void setSOLC(Double sOLC) {
		SOLC = sOLC;
	}

	public ExpandabilityRule getExpandabilityRule() {
		return expandabilityRule;
	}

	public void setExpandabilityRule(ExpandabilityRule expandabilityRule) {
		this.expandabilityRule = expandabilityRule;
	}

	public Double getdI() {
		return dI == null ? 0.0 : dI;
	}

	public void setdI(Double dI) {
		this.dI = dI;
	}

	public Double getdIAfterChangeScenario() {
		return dIAfterChangeScenario == null ? 0.0 : dIAfterChangeScenario;
	}

	public void setdIAfterChangeScenario(Double dIAfterChangeScenario) {
		this.dIAfterChangeScenario = dIAfterChangeScenario;
	}

	public Double getDdiKCalValue() {
		return ddiKCalValue == null ? 0.0 : ddiKCalValue;
	}

	public void setDdiKCalValue(Double ddiKCalValue) {
		this.ddiKCalValue = ddiKCalValue;
	}

	public Double getDdiPriceValue() {
		return ddiPriceValue == null ? 0.0 : ddiPriceValue;
	}

	public void setDdiPriceValue(Double ddiPriceValue) {
		this.ddiPriceValue = ddiPriceValue;
	}

	public Double getTotalIncomeAfterChangeScenario() {
		return totalIncomeAfterChangeScenario == null ? 0.0 : totalIncomeAfterChangeScenario;
	}

	public void setTotalIncomeAfterChangeScenario(Double totalIncomeAfterChangeScenario) {
		this.totalIncomeAfterChangeScenario = totalIncomeAfterChangeScenario;
	}

	public Double getTotalOutputAfterChangeScenario() {
		return totalOutputAfterChangeScenario == null ? 0.0 : totalOutputAfterChangeScenario;
	}

	public void setTotalOutputAfterChangeScenario(Double totalOutputAfterChangeScenario) {
		this.totalOutputAfterChangeScenario = totalOutputAfterChangeScenario;
	}

	public Double getTotalIncome() {
		return totalIncome == null ? 0.0 : totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Double getTotalOutput() {
		return totalOutput == null ? 0.0 : totalOutput;
	}

	public void setTotalOutput(Double totalOutput) {
		this.totalOutput = totalOutput;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getHouseholdName() {
		return householdName;
	}

	public void setHouseholdName(String householdName) {
		this.householdName = householdName;
	}

	public int getHouseholdNumber() {
		return householdNumber;
	}

	public void setHouseholdNumber(int householdNumber) {

		this.householdNumber = householdNumber;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Collection<HouseholdMember> getHouseholdMember() {
		return householdMember;
	}

	public void setHouseholdMember(List<HouseholdMember> householdMember) {
		this.householdMember = householdMember;
	}

	public Collection<ConfigAnswer> getConfigAnswer() {
		return configAnswer;
	}

	public void setConfigAnswer(Collection<ConfigAnswer> configAnswer) {
		this.configAnswer = configAnswer;
	}

	public String getSpreadsheet() {
		return spreadsheet;
	}

	public void setSpreadsheet(String spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	public void setHouseholdMember(Collection<HouseholdMember> householdMember) {
		this.householdMember = householdMember;
	}

	public Collection<AssetLiveStock> getAssetLiveStock() {
		return assetLiveStock;
	}

	public void setAssetLiveStock(Collection<AssetLiveStock> assetLiveStock) {
		this.assetLiveStock = assetLiveStock;
	}

	public Collection<AssetLand> getAssetLand() {
		return assetLand;
	}

	public void setAssetLand(Collection<AssetLand> assetLand) {
		this.assetLand = assetLand;
	}

	public Collection<AssetTradeable> getAssetTradeable() {
		return assetTradeable;
	}

	public void setAssetTradeable(Collection<AssetTradeable> assetTradeable) {
		this.assetTradeable = assetTradeable;
	}

	public Collection<AssetFoodStock> getAssetFoodStock() {
		return assetFoodStock;
	}

	public void setAssetFoodStock(Collection<AssetFoodStock> assetFoodStock) {
		this.assetFoodStock = assetFoodStock;
	}

	public Collection<AssetTree> getAssetTree() {
		return assetTree;
	}

	public void setAssetTree(Collection<AssetTree> assetTree) {
		this.assetTree = assetTree;
	}

	public Collection<AssetCash> getAssetCash() {
		return assetCash;
	}

	public void setAssetCash(Collection<AssetCash> assetCash) {
		this.assetCash = assetCash;
	}

	public Collection<Crop> getCrop() {
		return crop;
	}

	public void setCrop(Collection<Crop> crop) {
		this.crop = crop;
	}

	public Collection<LivestockSales> getLivestockSales() {
		return livestockSales;
	}

	public void setLivestockSales(Collection<LivestockSales> livestockSales) {
		this.livestockSales = livestockSales;
	}

	public Collection<LivestockProducts> getLivestockProducts() {
		return livestockProducts;
	}

	public void setLivestockProducts(Collection<LivestockProducts> livestockProducts) {
		this.livestockProducts = livestockProducts;
	}

	public Collection<Employment> getEmployment() {
		return employment;
	}

	public void setEmployment(Collection<Employment> employment) {
		this.employment = employment;
	}

	public Collection<Transfer> getTransfer() {
		return transfer;
	}

	public void setTransfer(Collection<Transfer> transfer) {
		this.transfer = transfer;
	}

	public Collection<WildFood> getWildFood() {
		return wildFood;
	}

	public void setWildFood(Collection<WildFood> wildFood) {
		this.wildFood = wildFood;
	}

	public Collection<Inputs> getInputs() {
		return inputs;
	}

	public void setInputs(Collection<Inputs> inputs) {
		this.inputs = inputs;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewers() {
		return interviewers;
	}

	public void setInterviewers(String interviewers) {
		this.interviewers = interviewers;
	}
	

	/*
	 * 
	 * List all ResourceSubTypes used in Modelling (crop,lsp,lss,transfers,emp,wildfood)
	 * in this WealthgroupInterview 
	 * 
	 */
	@Transient
	public Collection<ResourceSubType> getRSTsForHH() {
		List<ResourceSubType> collect = getCrop().stream().map(p -> p.getResourceSubType())
				.collect((Collectors.toList()));
		
		
		collect.addAll(getEmployment().stream().map(p -> p.getFoodResourceSubType())
		.collect((Collectors.toList())));
		
		collect.addAll(getEmployment().stream().map(p -> p.getResourceSubType())
		.collect((Collectors.toList())));
		
		collect.addAll(getLivestockProducts().stream().map(p -> p.getResourceSubType())
				.collect((Collectors.toList())));
		
		collect.addAll(getLivestockSales().stream().map(p -> p.getResourceSubType())
				.collect((Collectors.toList())));		
		
		collect.addAll(getTransfer().stream().map(p -> p.getFoodResourceSubType())
				.collect((Collectors.toList())));
		
		collect.addAll(getTransfer().stream().map(p -> p.getResourceSubType())
				.collect((Collectors.toList())));
		
		collect.addAll(getWildFood().stream().map(p -> p.getResourceSubType())
				.collect((Collectors.toList())));
		
		List<ResourceSubType> collect2 = collect.stream()
		.distinct()
		.filter(p -> p != null)
		.collect(Collectors.toList());
		
		
		return(collect2);
		
		
		
	}	
	

}
