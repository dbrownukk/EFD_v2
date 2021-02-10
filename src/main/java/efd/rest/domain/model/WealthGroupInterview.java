package efd.rest.domain.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import efd.validations.*;

@Views({ @View(members = "Wealth_Group_Interview[#wealthgroup" + ";wgInterviewNumber" + ",wgInterviewers"
		+ ",wgIntervieweesCount" + ";wgFemaleIVees" + ",wgMaleIVees" + ",wgAverageNumberInHH" + ";wgYearType"
		+ ",wgInterviewDate, status ;spreadsheet]" + "Assets{" + ";Land{assetLand}" + ";LiveStock{assetLiveStock}"
		+ ";Tradeable{assetTradeable}" + ";FoodStock{assetFoodStock}" + ";Trees{assetTree}" + ";Cash{assetCash}" + "}"
		+ ";Crops{crop}" + ";LiveStockSales{livestockSales}" + ";LiveStockProducts{livestockProducts}"
		+ ";Employment{employment}" + ";Transfer{transfer}" + ";WildFood{wildFood}" + ";FoodPurchase{foodPurchase}"
		+ ";NonFoodPurchase{nonFoodPurchase}"),
		@View(name = "ReadOnly", members = "Wealth_Group_Interview[# wealthgroup" + ";wgInterviewNumber"
				+ ",wgInterviewers" + ",wgIntervieweesCount" + ";wgFemaleIVees" + ",wgMaleIVees"
				+ ",wgAverageNumberInHH" + ";wgYearType" + ",wgInterviewDate," + "notes ;spreadsheet" + ",status]"
				+ "Assets{" + ";Land{assetLand}" + ";LiveStock{assetLiveStock}" + ";Tradeable{assetTradeable}"
				+ ";FoodStock{assetFoodStock}" + ";Trees{assetTree}" + ";Cash{assetCash}" + "}" + ";Crops{crop}"
				+ ";LiveStockSales{livestockSales}" + ";LiveStockProducts{livestockProducts}"
				+ ";Employment{employment}" + ";Transfer{transfer}" + ";WildFood{wildFood}"
				+ ";FoodPurchase{foodPurchase}" + ";NonFoodPurchase{nonFoodPurchase}"),
		@View(name = "wg", members = "wealthgroup") })

@Tabs({ @Tab(editors = "List, Detail", properties = "wealthgroup.community.projectlz.projecttitle,wealthgroup.community.site.livelihoodZone.lzname, "
		+ "wealthgroup.community.site.locationdistrict, wealthgroup.community.site.subdistrict, wealthgroup.wgnameeng,status,wgInterviewNumber,"
		+ "wgInterviewers,wgIntervieweesCount,wgFemaleIVees,"
		+ "wgMaleIVees,wgAverageNumberInHH,wgYearType,wgInterviewDate,wealthGroup.wgorder", defaultOrder = "${wealthgroup.community.projectlz.projecttitle}"
				+ ",${wealthgroup.community.site.livelihoodZone.lzname}"
				+ ",${wealthgroup.community.site.locationdistrict}" + ",${wealthgroup.community.site.subdistrict}"
				+ ",${wealthgroup.wgnameeng}"),
		@Tab(name = "IncomeData", editors = "List, Detail", properties = "wealthgroup.community.projectlz.projecttitle,wealthgroup.community.site.livelihoodZone.lzname, "
				+ "wealthgroup.community.site.locationdistrict, wealthgroup.community.site.subdistrict, wealthgroup.wgnameeng,status,wgInterviewNumber,"
				+ "wgInterviewers,wgIntervieweesCount,wgFemaleIVees,"
				+ "wgMaleIVees,wgAverageNumberInHH,wgYearType,wgInterviewDate")
		// baseCondition="from Organization e, in (e.users) u where u.name = ?")
})

@Entity

// @Table(name = "WealthGroupInterview")

public class WealthGroupInterview {

	/*
	 * Do not allow updates when set to Validated - unless an Admin
	 */

	/*
	 * @PreUpdate private void verifyNotValidated() {
	 * 
	 * User user = User.find(Users.getCurrent());
	 * 
	 * if(user.hasRole("admin")) { System.out.println("User is an admin"); return; }
	 * 
	 * if (status == WealthGroupInterview.Status.Validated) { Messages errors = new
	 * Messages(); errors.add("Cannot update Validated Wealthgroup"); throw new
	 * org.openxava.validators.ValidationException(errors); } }
	 */

	// ----------------------------------------------------------------------------------------------//

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "WGIID", length = 32, unique = true, nullable = false)
	private String wgiid;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGInterviewNumber", nullable = false)
	@Required
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer wgInterviewNumber;

	@Column(name = "WGInterviewers", nullable = false)
	@Required
	private String wgInterviewers;

	@Depends("wgFemaleIVees, wgMaleIVees")
	public Integer getWgIntervieweesCount() {
		return (wgFemaleIVees + wgMaleIVees);
	}

	@Column(name = "WGFemaleIVees")
	@PositiveOrZero
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer wgFemaleIVees;

	@Column(name = "WGMaleIVees")
	@PositiveOrZero
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer wgMaleIVees;

	@Column(name = "WGAverageNumberInHH")
	// @Positive temp change while fixing data
	@Required
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer wgAverageNumberInHH;

	@Column(name = "WGYearType")
	private String wgYearType;
	/* ENUM SET('Good', 'Bad', 'Normal') */

	@Column(name = "WGInterviewDate")
	@Stereotype("DATE")
	private Date wgInterviewDate;

	@OnChange(value = OnChangeSetWGIStatus.class)
	@Column(name = "WGIStatus")
	@Required // removes the blank
	private Status status;

	public enum Status {
		Generated, Uploaded, PartParsed, FullyParsed, Validated
	};

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WGID")
	// @Required
	@NoFrame
	@NoModify
	@ReadOnly
	@NoCreate
	@SearchKey

	@DescriptionsList(descriptionProperties = "community.site.livelihoodZone.lzname,community.site.locationdistrict,community.site.subdistrict,wgnameeng")
	private WealthGroup wealthgroup;

	@Stereotype("FILE")
	@Column(length = 32, name = "WGISpreadsheet")
	// @OnChange(OnChangeFileUpload.class) // set status to uploaded after file
	// upload and spreadsheet not null -- ONLY 1 OnChange per view..
	private String spreadsheet;

	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;

	/* Collections of resource elements */

	@ElementCollection
	// Workaround from Javier
	// https://sourceforge.net/p/openxava/discussion/419690/thread/b6535530de/

	@ListProperties("status,resourceSubType.resourcetypename,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit")

	// @ListsProperties({
	// @ListProperties(forViews="ReadOnly",
	// value="status,resourceSubType.resourcetypename,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit"),
	// @ListProperties(forViews="DEFAULT",value="status,resourceSubType.resourcetypename,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit")
	// })
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

	// @CollectionTable(uniqueConstraints= {
	// @UniqueConstraint(name = "unique_type", columnNames = {
	// "WealthGroupInterview_WGIID", "ResourceSubType"}) })

	@ListProperties("status, resourceSubType.resourcetypename,employmentName,peopleCount,unitsWorked,unit,cashPaymentAmount,foodResourceSubType.resourcetypename,foodPaymentFoodType,foodPaymentUnit,foodPaymentUnitsPaidWork"
			+ ",workLocation1,percentWorkLocation1,workLocation2,percentWorkLocation2,workLocation3,percentWorkLocation3")
	private Collection<Employment> employment;

	@ElementCollection
	// @ListProperties("status,resourceSubType.resourcetypename, isOfficial,
	// source,peopleReceiving,timesReceived,cashTransferAmount,foodResourceSubType.resourcetypename,transferFoodOtherType,"
	// + " unit, unitsTransferred,unitsSold,otherUse,unitsConsumed,pricePerUnit"
	// +
	// ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	@ListProperties("status,resourceSubType.resourcetypename, isOfficial,source,transferType,peopleReceiving,timesReceived,cashTransferAmount,foodResourceSubType.resourcetypename,transferFoodOtherType,"
			+ " unit, unitsTransferred,unitsSold,otherUse,unitsConsumed,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<Transfer> transfer;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename,wildFoodName,unit,unitsProduced,unitsSold,otherUse,unitsConsumed,pricePerUnit"
			+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
	private Collection<WildFood> wildFood;

	@ElementCollection
	@ListProperties("status, resourceSubType.resourcetypename,foodTypeTypeEnteredName,unit,unitsPurchased,pricePerUnit")
	private Collection<FoodPurchase> foodPurchase;

	@ElementCollection
	@ListProperties("status,resourceSubType.resourcetypename,itemPurchased,unit,unitsPurchased,pricePerUnit")
	private Collection<NonFoodPurchase> nonFoodPurchase;

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
	private Collection<ExpandabilityRule> expandabilityRule;

	@Transient
	private double wgiShortFall;

	public double getWgiShortFall() {
		return wgiShortFall;
	}

	public void setWgiShortFall(double wgiShortFall) {
		this.wgiShortFall = wgiShortFall;
	}

	public Collection<ExpandabilityRule> getExpandabilityRule() {
		return expandabilityRule;
	}

	public void setExpandabilityRule(Collection<ExpandabilityRule> expandabilityRule) {
		this.expandabilityRule = expandabilityRule;
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

	public Double getTotalIncome() {
		return totalIncome == null ? 0.0 : totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Double getTotalIncomeAfterChangeScenario() {
		return totalIncomeAfterChangeScenario == null ? 0.0 : totalIncomeAfterChangeScenario;
	}

	public void setTotalIncomeAfterChangeScenario(Double totalIncomeAfterChangeScenario) {
		this.totalIncomeAfterChangeScenario = totalIncomeAfterChangeScenario;
	}

	public Double getTotalOutput() {
		return totalOutput == null ? 0.0 : totalOutput;
	}

	public void setTotalOutput(Double totalOutput) {
		this.totalOutput = totalOutput;
	}

	public Double getTotalOutputAfterChangeScenario() {
		return totalOutputAfterChangeScenario == null ? 0.0 : totalOutputAfterChangeScenario;
	}

	public void setTotalOutputAfterChangeScenario(Double totalOutputAfterChangeScenario) {
		this.totalOutputAfterChangeScenario = totalOutputAfterChangeScenario;
	}

	public Double getdIAfterChangeScenario() {
		return dIAfterChangeScenario == null ? 0.0 : dIAfterChangeScenario;
	}

	public void setdIAfterChangeScenario(Double dIAfterChangeScenario) {
		this.dIAfterChangeScenario = dIAfterChangeScenario;
	}

	public Double getdI() {
		return dI == null ? 0.0 : dI;
	}

	public void setdI(Double dI) {
		this.dI = dI;
	}

	public String getWgiid() {
		return wgiid;
	}

	public void setWgiid(String wgiid) {
		this.wgiid = wgiid;
	}

	public Integer getWgInterviewNumber() {
		return wgInterviewNumber;
	}

	public void setWgInterviewNumber(Integer wgInterviewNumber) {
		this.wgInterviewNumber = wgInterviewNumber;
	}

	public String getWgInterviewers() {
		return wgInterviewers;
	}

	public void setWgInterviewers(String wgInterviewers) {
		this.wgInterviewers = wgInterviewers;
	}

	public Integer getWgFemaleIVees() {
		return wgFemaleIVees;
	}

	public void setWgFemaleIVees(Integer wgFemaleIVees) {
		this.wgFemaleIVees = wgFemaleIVees;
	}

	public Integer getWgMaleIVees() {
		return wgMaleIVees;
	}

	public void setWgMaleIVees(Integer wgMaleIVees) {
		this.wgMaleIVees = wgMaleIVees;
	}

	public Integer getWgAverageNumberInHH() {
		return wgAverageNumberInHH == null ? 0 : wgAverageNumberInHH;
	}

	public void setWgAverageNumberInHH(Integer wgAverageNumberInHH) {
		this.wgAverageNumberInHH = wgAverageNumberInHH;
	}

	public String getWgYearType() {
		return wgYearType;
	}

	public void setWgYearType(String wgYearType) {
		this.wgYearType = wgYearType;
	}

	public Date getWgInterviewDate() {
		return wgInterviewDate;
	}

	public void setWgInterviewDate(Date wgInterviewDate) {
		this.wgInterviewDate = wgInterviewDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WealthGroup getWealthgroup() {
		return wealthgroup;
	}

	public void setWealthgroup(WealthGroup wealthgroup) {
		this.wealthgroup = wealthgroup;
	}

	public String getSpreadsheet() {
		return spreadsheet == null ? "" : spreadsheet;
	}

	public void setSpreadsheet(String spreadsheet) {
		this.spreadsheet = spreadsheet;
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

	public Collection<FoodPurchase> getFoodPurchase() {
		return foodPurchase;
	}

	public void setFoodPurchase(Collection<FoodPurchase> foodPurchase) {
		this.foodPurchase = foodPurchase;
	}

	public Collection<NonFoodPurchase> getNonFoodPurchase() {
		return nonFoodPurchase;
	}

	public void setNonFoodPurchase(Collection<NonFoodPurchase> nonFoodPurchase) {
		this.nonFoodPurchase = nonFoodPurchase;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	/*
	 * 
	 * List all ResourceSubTypes used in Modelling (crop,lsp,lss,transfers,emp,wildfood)
	 * in this WealthgroupInterview 
	 * 
	 */
	@Transient
	public Collection<ResourceSubType> getRSTsForWGI() {
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
		
		
		//collect2.stream().forEach(p ->
		//System.out.println("rst in wgi = "+p.getResourcetypename())
		//);
		
		return(collect2);
		
		
		
	}	
	
	

}