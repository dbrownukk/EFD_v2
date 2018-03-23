package efd.model;

import java.util.*;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

import efd.validations.*;

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
		+ ";LiveStock{assetLiveStock}"
		+ ";Land{assetLand}"
		+ ";Crops{crop}"
		+ ";LiveStockUse{liveStockUse}"
		+ ";Employment{employment}"
		+ ";Transfer{transfer}"
		+ ";WildFood{wildFood}"
		+ ";Tradeable{assetTradeable}"
		+ ";FoodStock{assetFoodStock}"
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

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,liveStockTypeEnteredName,unitEntered,numberOwned,pricePerUnit")
private Collection<AssetLiveStock> assetLiveStock;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,landTypeEnteredName,landArea,unitEntered")
private Collection<AssetLand> assetLand;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,cropName,localUnit,quantityProduced, quantitySold,pricePerUnit,otherUse"
		+ ",janQP,febQP,marQP,aprQP,mayQP,junQP"
		+ ",julQP,augQP,sepQP,octQP,novQP,decQP")
private Collection<Crop> crop;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,lsName,localUnit,quantityProduced,quantitySold,pricePerUnit,otherUse,lsIncomeType,lsIncomeType2")
private Collection<LiveStockUse> liveStockUse;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,employmentName,peopleCount,frequency, cashPaymentPerUnit, foodPaymentPerUnit")
private Collection<Employment> employment;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,transferredResourceName,localUnit,quantityReceived, quantitySold,pricePerUnit,otherUse")
private Collection<Transfer> transfer;

@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,wildFoodOrCropIndicator,foodName")
private Collection<AssetFoodStock> assetFoodStock;




@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,wildFoodName,localUnit,quantityProduced,quantitySold,pricePerUnit,otherUse")
private Collection<WildFood> wildFood;




@ElementCollection      // Note  problem with Descriptions list for resourcetype 
@ListProperties("status,tradeableTypeEnteredName,quantity")
private Collection<AssetTradeable> assetTradeable;






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

public Collection<WildFood> getWildFood() {
	return wildFood;
}

public void setWildFood(Collection<WildFood> wildFood) {
	this.wildFood = wildFood;
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

public Collection<AssetLiveStock> getAssetLiveStock() {
	return assetLiveStock;
}

public void setAssetLiveStock(Collection<AssetLiveStock> assetLiveStock) {
	this.assetLiveStock = assetLiveStock;
}

public Collection<AssetFoodStock> getAssetFoodStock() {
	return assetFoodStock;
}

public void setAssetFoodStock(Collection<AssetFoodStock> assetFoodStock) {
	this.assetFoodStock = assetFoodStock;
}

public Collection<LiveStockUse> getLiveStockUse() {
	return liveStockUse;
}

public void setLiveStockUse(Collection<LiveStockUse> liveStockUse) {
	this.liveStockUse = liveStockUse;
}

public Collection<Transfer> getTransfer() {
	return transfer;
}

public void setTransfer(Collection<Transfer> transfer) {
	this.transfer = transfer;
}

public Collection<Crop> getCrop() {
	return crop;
}

public void setCrop(Collection<Crop> crop) {
	this.crop = crop;
}

public Collection<Employment> getEmployment() {
	return employment;
}

public void setEmployment(Collection<Employment> employment) {
	this.employment = employment;
}

public String getSpreadsheet() {
	return spreadsheet;
}

public void setSpreadsheet(String spreadsheet) {
	{
	this.spreadsheet = spreadsheet;
}

}




// ----------------------------------------------------------------------------------------------//




}