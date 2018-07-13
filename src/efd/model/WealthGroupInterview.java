package efd.model;

import java.util.*;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import efd.validations.*;
import efd.model.*;

@Views({
@View(members= "Wealth_Group_Interview[# wealthgroup"
		+ ";wgInterviewNumber"
		+ ",wgInterviewers"
		+ ",wgIntervieweesCount"
		+ ";wgFemaleIVees"
		+ ",wgMaleIVees"
		+ ",wgAverageNumberInHH"
		+ ";wgYearType"
		+ ",wgInterviewDate"
		+ ";spreadsheet"
		+ ",status]"
		+  "Assets{"
		+ ";Land{assetLand}"
		+ ";LiveStock{assetLiveStock}"
		+ ";Tradeable{assetTradeable}"
		+ ";FoodStock{assetFoodStock}"
		+ ";Trees{assetTree}"
		+ ";Cash{assetCash}"
		+ "}"
		+ ";Crops{crop}"
		+ ";LiveStockSales{livestockSales}"
		+ ";LiveStockProducts{livestockProducts}"
		+ ";Employment{employment}"
		+ ";Transfer{transfer}"
		+ ";WildFood{wildFood}"
		+ ";FoodPurchase{foodPurchase}"
		+ ";NonFoodPurchase{nonFoodPurchase}"
		),  
@View(name="wg",members= "wealthgroup")
})


@Tab(editors = "List, Detail", properties = "wealthgroup.community.projectlz.projecttitle,wealthgroup.community.site.livelihoodZone.lzname, "
		+ "wealthgroup.community.site.locationdistrict, wealthgroup.community.site.subdistrict, wealthgroup.wgnameeng,status,wgInterviewNumber,"
		+ "wgInterviewers,wgIntervieweesCount,wgFemaleIVees,"
		+ "wgMaleIVees,wgAverageNumberInHH,wgYearType,wgInterviewDate", 
		defaultOrder="${wealthgroup.community.projectlz.projecttitle}"
				+ ",${wealthgroup.community.site.livelihoodZone.lzname}"
				+ ",${wealthgroup.community.site.locationdistrict}"
				+ ",${wealthgroup.community.site.subdistrict}"
				+ ",${wealthgroup.wgnameeng}")

@Entity

//@Table(name = "WealthGroupInterview")

public class WealthGroupInterview {
	
	
	
	// ----------------------------------------------------------------------------------------------//

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "WGIID", length = 32, unique = true,nullable=false)
	private String wgiid;
	// ----------------------------------------------------------------------------------------------//



@Column(name = "WGInterviewNumber",nullable=false)	
@Required
private Integer wgInterviewNumber;

@Column(name = "WGInterviewers",nullable=false)	
@Required
private String wgInterviewers;

@Column(name = "WGIntervieweesCount",nullable=false)	
@Required
private Integer wgIntervieweesCount;

@Column(name = "WGFemaleIVees")	
private Integer wgFemaleIVees;

@Column(name = "WGMaleIVees")	
private Integer wgMaleIVees;

@Column(name = "WGAverageNumberInHH")	
private Integer wgAverageNumberInHH;

@Column(name = "WGYearType")	
private String wgYearType;
/* ENUM SET('Good', 'Bad', 'Normal') */ 

@Column(name = "WGInterviewDate")	
@Stereotype("DATE")
private Date wgInterviewDate;

//@Editor("ValidValuesRadioButton")
//@ReadOnly
@Column(name="WGIStatus")
private Status status;
public enum Status { Generated, Uploaded, Parsed, Validated };

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "WGID")
//@Required
@NoFrame
@NoModify
@ReadOnly
@NoCreate
@DescriptionsList(descriptionProperties="community.site.livelihoodZone.lzname,community.site.locationdistrict,community.site.subdistrict,wgnameeng")
private WealthGroup wealthgroup;


@Stereotype("FILE")
// @OnChange(OnChangeSetWGIStatus.class)
@Column(length=32,name="WGISpreadsheet")
private String spreadsheet;





/*  Collections of resource elements */

@ElementCollection     
@ListProperties("status,resourceSubType,liveStockTypeEnteredName,unit,numberOwnedAtStart,pricePerUnit")
@XOrderBy("status desc") 
@OrderBy("Status desc")
private Collection<AssetLiveStock> assetLiveStock;

@ElementCollection     
@ListProperties("status,resourceSubType,landTypeEnteredName,unit,numberOfUnits")
private Collection<AssetLand> assetLand;

@ElementCollection     
@ListProperties("status,resourceSubType,tradeableTypeEnteredName,unit,numberOwned,pricePerUnit")
private Collection<AssetTradeable> assetTradeable;

@ElementCollection     
@ListProperties("status,resourceSubType,foodTypeEnteredName,unit,quantity")
private Collection<AssetFoodStock> assetFoodStock;

@ElementCollection     
@ListProperties("status,resourceSubType,treeTypeEnteredName,unit,numberOwned,pricePerUnit")
private Collection<AssetTree> assetTree;

@ElementCollection     
@ListProperties("status,resourceSubType,currencyEnteredName,amount")
private Collection<AssetCash> assetCash;


@ElementCollection      
@ListProperties("status,resourceSubType,cropType,unit,unitsProduced, unitsSold,pricePerUnit,unitsConsumed,unitsOtherUse"
		+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
private Collection<Crop> crop;

@ElementCollection     
@ListProperties("status,resourceSubType,livestockType,unit,unitsAtStartofYear, unitsSold,pricePerUnit"
		+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
private Collection<LivestockSales> livestockSales;

@ElementCollection      
@ListProperties("status,resourceSubType,livestockType,livestockProduct,unit,unitsProduced, unitsSold,pricePerUnit,unitsConsumed,unitsOtherUse"
		+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
private Collection<LivestockProducts> livestockProducts;


@ElementCollection     
@ListProperties("status,resourceSubType,employmentName,peopleCount,unitsWorked,unit,cashPaymentAmount,foodPaymentFoodType,foodPaymentUnit"
		+ ",workLocation1,percentWorkLocation1,workLocation2,percentWorkLocation2,workLocation3,percentWorkLocation3")
private Collection<Employment> employment;

@ElementCollection      
@ListProperties("status,resourceSubType,isOfficial,source,transferType,peopleReceiving,timesReceived,cashTransferAmount,transferFoodOtherType,"
		+ "unit, unitsTransferred,unitsSold,pricePerUnit,otherUse,unitsConsumed"
		+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
private Collection<Transfer> transfer;

@ElementCollection     
@ListProperties("status,resourceSubType,wildFoodName,unit,unitsProduced,unitsSold,pricePerUnit,unitsConsumed,otherUse"
		+ ",market1,percentTradeMarket1,market2,percentTradeMarket2,market3,percentTradeMarket3")
private Collection<WildFood> wildFood;

@ElementCollection     
@ListProperties("status,resourceSubType,foodTypeTypeEnteredName,unit,unitsPurchased,pricePerUnit")
private Collection<FoodPurchase> foodPurchase;

@ElementCollection     
@ListProperties("status,resourceSubType,itemPurchased,unit,unitsPurchased,pricePerUnit")
private Collection<NonFoodPurchase> nonFoodPurchase;

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

public Integer getWgIntervieweesCount() {
	return wgIntervieweesCount;
}

public void setWgIntervieweesCount(Integer wgIntervieweesCount) {
	this.wgIntervieweesCount = wgIntervieweesCount;
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
	return wgAverageNumberInHH;
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
	return spreadsheet;
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



}