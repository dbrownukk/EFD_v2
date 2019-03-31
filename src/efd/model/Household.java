package efd.model;

import java.util.*;

import javax.persistence.*;

import org.apache.poi.util.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.model.WealthGroupInterview.*;
import efd.validations.*;

@View(members = "Household[#study,householdName,householdNumber;interviewers, interviewDate,notes;spreadsheet,status];configAnswer;HouseholdMember{householdMember},"
		+ "Assets{Land{assetLand};LiveStock{assetLiveStock};Tradeable{assetTradeable}" + ";FoodStock{assetFoodStock}"
		+ ";Trees{assetTree};Cash{assetCash}};Crops{crop}"
		+ ";LiveStockSales{livestockSales};LiveStockProducts{livestockProducts};Employment{employment}"
		+ ";Transfer{transfer};WildFood{wildFood};Inputs{inputs}")

@Tab(properties = "study.studyName,study.referenceYear, householdName, status")

@Table(name = "Household", uniqueConstraints = {
		@UniqueConstraint(name = "unique_household_number", columnNames = { "study_id", "householdNumber" }),
		@UniqueConstraint(name = "unique_household_name", columnNames = { "study_id", "householdName" }) })

@Entity

public class Household extends EFDIdentifiable {

	@PrePersist

	private void validate() throws Exception {
		System.out.println("calc next household number = ");
		int lastNumber = 0;
		String studyid = getStudy().getId();
		System.out.println("calc next household number, studyid =  = " + studyid);
		try {
			TypedQuery<Integer> ilast = XPersistence.getManager()
					.createQuery("select max(householdNumber) from Household where study_id = :studyid", Integer.class);
			ilast.setParameter("studyid", studyid);
			
			System.out.println("post XP select");
			lastNumber = ilast.getSingleResult();
			
			System.out.println("post XP select lastnumber = "+lastNumber);
			//lastNumber = query.getSingleResult();
		
		} catch (Exception ex) {
			System.out.println("exception = "+ex.toString());
			if (lastNumber == 0)
				setHouseholdNumber(1);
			return;
		}
		if (lastNumber > 0)
			setHouseholdNumber(lastNumber + 1);
		else
			setHouseholdNumber(1);
		return;

	}

	
	@Column(length = 45)
	@SearchKey 
	private String householdName;
	/*************************************************************************************************/
	// @Required
	private int householdNumber;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoFrame
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "household" , cascade=CascadeType.REMOVE)
	//@ElementCollection
	//@ListProperties("householdMemberName,headofHousehold, gender, age, yearOfBirth, absent, reasonForAbsence")
	private Collection<HouseholdMember> householdMember;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "household", cascade=CascadeType.REMOVE)
	//@ElementCollection
	@NoCreate
	@AddAction("")
	@ListProperties("configQuestionUse.configQuestion.prompt,answer")
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Stereotype("FILE")
	@Column(length = 32, name = "spreadsheet")
	private String spreadsheet;

	/*************************************************************************************************/
	@Stereotype("DATE")
	@Column(name = "InterviewDate")
	private java.util.Date interviewDate;
	/*************************************************************************************************/
	@Column(name = "Interviewers", length=45)
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
	@ListProperties("status, resourceSubType.resourcetypename,currencyEnteredName,amount")
	private Collection<AssetCash> assetCash;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,cropType,unit,unitsProduced, unitsSold,pricePerUnit,unitsConsumed,unitsOtherUse"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<Crop> crop;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, livestockType,unit,unitsAtStartofYear, unitsSold,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<LivestockSales> livestockSales;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, livestockType,livestockProduct,unit,unitsProduced, unitsSold,pricePerUnit,unitsConsumed,unitsOtherUse"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<LivestockProducts> livestockProducts;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,employmentName,peopleCount,unitsWorked,unit,cashPaymentAmount,foodResourceSubType.resourcetypename,foodPaymentFoodType,foodPaymentUnit,foodPaymentUnitsPaidWork"
			+ ",workLocation1,percentWorkLocation1,workLocation2,percentWorkLocation2,workLocation3,percentWorkLocation3")
	private Collection<Employment> employment;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, isOfficial,source,transferType,peopleReceiving,timesReceived,cashTransferAmount,foodResourceSubType.resourcetypename,transferFoodOtherType,"
			+ " unit, unitsTransferred,unitsSold,pricePerUnit,otherUse,unitsConsumed"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<Transfer> transfer;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename,wildFoodName,unit,unitsProduced,unitsSold,pricePerUnit,unitsConsumed,otherUse"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<WildFood> wildFood;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename, itemPurchased, unit, unitsPurchased, pricePerUnit,resource1UsedFor,percentResource1,resource2UsedFor,percentResource2,resource3UsedFor,percentResource3" )
	private Collection<Inputs> inputs;
	
	
	@DisplaySize(25)
	@OnChange(value = OnChangeSetHHStatus.class)
	private Status status;

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

	public java.util.Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(java.util.Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewers() {
		return interviewers;
	}

	public void setInterviewers(String interviewers) {
		this.interviewers = interviewers;
	}

	
	
	
}
