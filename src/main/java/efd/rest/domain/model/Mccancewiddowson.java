package efd.rest.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.openxava.annotations.DisplaySize;
import org.openxava.annotations.View;

/**
 * The persistent class for the mccancewiddowsons database table.
 * Complex column names from MCcane spreadsheet..
 */

@View(name = "fromRST", members = "Food[foodCode,description]"
		+ ",RDA[rda,rdaUnit]"
		+ ";Nutrients[#aOACfibre,betacarotene,biotin,calcium"
		+ ";carbohydrate,chloride,copper,energykJkJ"
		+ ";fat,folate,iodine,iron"
		+ ";magnesium,manganese,niacin,niacinequivalent"
		+ ";nSP,pantothenate,phosphorus,potassium"
		+ ";protein,riboflavin,selenium,sodium"
		+ ";starch,thiamin,totalnitrogen,totalsugars"
		+ ";tryptophan60,vitaminB12,vitaminB6,vitaminC"
		+ ";vitaminE,water,zinc]"
)

@Entity
@Table(name = "mccancewiddowsons")
@NamedQuery(name = "Mccancewiddowson.findAll", query = "SELECT m FROM Mccancewiddowson m")
public class Mccancewiddowson {

	@Id
	@DisplaySize(20)
	
	
	@Column(name = "FoodCode", length = 50)
	private String foodCode;
	
	
	@DisplaySize(50)
	private String description;
	
	@DisplaySize(6)
	@Column(name = "AOACFibre")
	private String aoacfibre;
	
	@DisplaySize(6)
	@Column(name = "BetaCarotene")
	private String betacarotene;
	

	@DisplaySize(6)
	@Column(name = "Biotin")
	private String biotin;
	

	@DisplaySize(6)
	@Column(name = "Calcium")
	private String calcium;
	

	@DisplaySize(6)
	@Column(name = "Carbohydrate")
	private String carbohydrate;


	@DisplaySize(6)
	@Column(name = "Chloride")
	private String chloride;
	

	@DisplaySize(6)
	@Column(name = "Copper")
	private String copper;


	@DisplaySize(6)
	@Column(name = "Energy")
	private String energy;

	@DisplaySize(6)
	@Column(name = "Fat")
	private String fat;


	
	@DisplaySize(6)
	@Column(name = "Folate")
	private String folate;


	@DisplaySize(50)
	@Column(name = "FoodName")
	private String foodName;
	
	@DisplaySize(6)
	@Column(name = "Iodine")
	private String iodine;


	@DisplaySize(6)
	@Column(name = "Iron")
	private String iron;


	@DisplaySize(6)
	@Column(name = "Magnesium")
	private String magnesium;


	@DisplaySize(6)
	@Column(name = "Manganese")
	private String manganese;
	

	@DisplaySize(6)
	@Column(name = "Niacin")
	private String niacin;

	
	@DisplaySize(6)
	@Column(name = "NiacinEquivalent")
	private String niacinequivalent;

	@DisplaySize(6)
	@Column(name = "NSP")
	private String nsp;

	@DisplaySize(6)
	@Column(name = "Pantothenate")
	private String pantothenate;

	@DisplaySize(6)
	@Column(name = "Phosphorus")
	private String phosphorus;

	@DisplaySize(6)
	@Column(name = "Potassium")
	private String potassium;


	@DisplaySize(6)
	@Column(name = "Protein")
	private String protein;


	@DisplaySize(6)
	@Column(name = "Riboflavin")
	private String riboflavin;


	@DisplaySize(6)
	@Column(name = "Selenium")
	private String selenium;


	@DisplaySize(6)
	@Column(name = "Sodium")
	private String sodium;


	@DisplaySize(6)
	@Column(name = "Starch")
	private String starch;


	@DisplaySize(6)
	@Column(name = "Thiamin")
	private String thiamin;


	@DisplaySize(6)
	@Column(name = "Nitrogen")
	private String nitrogen;

	
	@DisplaySize(6)
	@Column(name = "Sugars")
	private String sugars;

	@DisplaySize(6)
	@Column(name = "Tryptophan60")
	private String tryptophan60;

	@DisplaySize(6)
	@Column(name = "VitaminB12")
	private String vitaminb12;

	
	
	@DisplaySize(6)
	@Column(name = "VitaminB6")
	private String vitaminb6;

	@DisplaySize(6)
	@Column(name = "VitaminC")
	private String vitaminc;
	
	@DisplaySize(6)
	@Column(name = "VitaminE")
	private String vitamine;

	@DisplaySize(6)
	@Column(name = "Water")
	private String water;

	@DisplaySize(6)
	@Column(name = "Zinc")
	private String zinc;
	
	@DisplaySize(6)
	@Column(name="RDA")
	private BigDecimal rda;
	@DisplaySize(6)
	@Column(name="RDAUnit")
	private String rdaUnit;
	
	
	public String getFoodCode() {
		return foodCode;
	}
	public void setFoodCode(String foodCode) {
		this.foodCode = foodCode;
	}

	public String getBetacarotene() {
		return betacarotene;
	}
	public void setBetacarotene(String betacarotene) {
		this.betacarotene = betacarotene;
	}
	public String getBiotin() {
		return biotin;
	}
	public void setBiotin(String biotin) {
		this.biotin = biotin;
	}
	public String getCalcium() {
		return calcium;
	}
	public void setCalcium(String calcium) {
		this.calcium = calcium;
	}
	public String getCarbohydrate() {
		return carbohydrate;
	}
	public void setCarbohydrate(String carbohydrate) {
		this.carbohydrate = carbohydrate;
	}
	public String getChloride() {
		return chloride;
	}
	public void setChloride(String chloride) {
		this.chloride = chloride;
	}
	public String getCopper() {
		return copper;
	}
	public void setCopper(String copper) {
		this.copper = copper;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFat() {
		return fat;
	}
	public void setFat(String fat) {
		this.fat = fat;
	}
	public String getFolate() {
		return folate;
	}
	public void setFolate(String folate) {
		this.folate = folate;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getIodine() {
		return iodine;
	}
	public void setIodine(String iodine) {
		this.iodine = iodine;
	}
	public String getIron() {
		return iron;
	}
	public void setIron(String iron) {
		this.iron = iron;
	}
	public String getMagnesium() {
		return magnesium;
	}
	public void setMagnesium(String magnesium) {
		this.magnesium = magnesium;
	}
	public String getManganese() {
		return manganese;
	}
	public void setManganese(String manganese) {
		this.manganese = manganese;
	}
	public String getNiacin() {
		return niacin;
	}
	public void setNiacin(String niacin) {
		this.niacin = niacin;
	}
	public String getNiacinequivalent() {
		return niacinequivalent;
	}
	public void setNiacinequivalent(String niacinequivalent) {
		this.niacinequivalent = niacinequivalent;
	}


	public String getPantothenate() {
		return pantothenate;
	}
	public void setPantothenate(String pantothenate) {
		this.pantothenate = pantothenate;
	}
	public String getPhosphorus() {
		return phosphorus;
	}
	public void setPhosphorus(String phosphorus) {
		this.phosphorus = phosphorus;
	}
	public String getPotassium() {
		return potassium;
	}
	public void setPotassium(String potassium) {
		this.potassium = potassium;
	}
	public String getProtein() {
		return protein;
	}
	public void setProtein(String protein) {
		this.protein = protein;
	}
	public String getRiboflavin() {
		return riboflavin;
	}
	public void setRiboflavin(String riboflavin) {
		this.riboflavin = riboflavin;
	}
	public String getSelenium() {
		return selenium;
	}
	public void setSelenium(String selenium) {
		this.selenium = selenium;
	}
	public String getSodium() {
		return sodium;
	}
	public void setSodium(String sodium) {
		this.sodium = sodium;
	}
	public String getStarch() {
		return starch;
	}
	public void setStarch(String starch) {
		this.starch = starch;
	}
	public String getThiamin() {
		return thiamin;
	}
	public void setThiamin(String thiamin) {
		this.thiamin = thiamin;
	}

	public String getTryptophan60() {
		return tryptophan60;
	}
	public void setTryptophan60(String tryptophan60) {
		this.tryptophan60 = tryptophan60;
	}

	public String getWater() {
		return water;
	}
	public void setWater(String water) {
		this.water = water;
	}
	public String getZinc() {
		return zinc;
	}
	public void setZinc(String zinc) {
		this.zinc = zinc;
	}
	public BigDecimal getRda() {
		return rda;
	}
	public void setRda(BigDecimal rda) {
		this.rda = rda;
	}
	public String getRdaUnit() {
		return rdaUnit;
	}
	public void setRdaUnit(String rdaUnit) {
		this.rdaUnit = rdaUnit;
	}
	public String getAoacfibre() {
		return aoacfibre;
	}
	public void setAoacfibre(String aoacfibre) {
		this.aoacfibre = aoacfibre;
	}
	public String getEnergy() {
		return energy;
	}
	public void setEnergy(String energy) {
		this.energy = energy;
	}
	public String getNsp() {
		return nsp;
	}
	public void setNsp(String nsp) {
		this.nsp = nsp;
	}
	public String getNitrogen() {
		return nitrogen;
	}
	public void setNitrogen(String nitrogen) {
		this.nitrogen = nitrogen;
	}
	public String getSugars() {
		return sugars;
	}
	public void setSugars(String sugars) {
		this.sugars = sugars;
	}

	
	

}