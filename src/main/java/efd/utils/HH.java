package efd.utils;

import java.util.*;

import efd.rest.domain.model.*;

public class HH {
	private Household household;
	private int hhNumber;
	private Double hhDI;
	private Double hhDIAfterChangeScenario;
	private Double hhAE;
	private Double hhSOLC;
	private Double hhSOLCAfterChangeScenario;
	private Collection<Category> category;
	private ResourceType resourceType;
	private ResourceSubType resourceSubType;
	private ConfigAnswer answer;
	private Boolean delete;
	private String type;
	private AssetLand land;
	private AssetFoodStock foodstock;
	private AssetCash cash;
	private AssetLiveStock livestock;
	private AssetTradeable tradeable;
	private AssetTree tree;
	private Crop crop;
	private Employment employment;
	private Inputs inputs;
	private LivestockProducts livestockproducts;
	private LivestockSales livestocksales;
	private Transfer transfer;
	private WildFood wildfood;
	private Quantile quantile;
	private Double avgDI;
	private int quantSeq;
	private Double cropIncome;
	private Double empIncome;
	private Double lsIncome;
	private Double transIncome;
	private Double wfIncome;
	private int numMales;
	private int numFemales;

	


	public Household getHousehold() {
		return household;
	}

	public void setHousehold(Household household) {
		this.household = household;
	}

	public int getHhNumber() {
		return hhNumber;
	}

	public void setHhNumber(int hhNumber) {
		this.hhNumber = hhNumber;
	}

	public Double getHhDI() {
		return hhDI;
	}

	public void setHhDI(Double hhDI) {
		this.hhDI = hhDI;
	}

	public Double getHhDIAfterChangeScenario() {
		return hhDIAfterChangeScenario;
	}

	public void setHhDIAfterChangeScenario(Double hhDIAfterChangeScenario) {
		this.hhDIAfterChangeScenario = hhDIAfterChangeScenario;
	}

	public Double getHhAE() {
		return hhAE;
	}

	public void setHhAE(Double hhAE) {
		this.hhAE = hhAE;
	}

	public Double getHhSOLC() {
		return hhSOLC;
	}

	public void setHhSOLC(Double hhSOLC) {
		this.hhSOLC = hhSOLC;
	}



	public Double getHhSOLCAfterChangeScenario() {
		return hhSOLCAfterChangeScenario;
	}

	public void setHhSOLCAfterChangeScenario(Double hhSOLCAfterChangeScenario) {
		this.hhSOLCAfterChangeScenario = hhSOLCAfterChangeScenario;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {
		this.category = category;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public ConfigAnswer getAnswer() {
		return answer;
	}

	public void setAnswer(ConfigAnswer answer) {
		this.answer = answer;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AssetLand getLand() {
		return land;
	}

	public void setLand(AssetLand land) {
		this.land = land;
	}

	public AssetFoodStock getFoodstock() {
		return foodstock;
	}

	public void setFoodstock(AssetFoodStock foodstock) {
		this.foodstock = foodstock;
	}

	public AssetCash getCash() {
		return cash;
	}

	public void setCash(AssetCash cash) {
		this.cash = cash;
	}

	public AssetLiveStock getLivestock() {
		return livestock;
	}

	public void setLivestock(AssetLiveStock livestock) {
		this.livestock = livestock;
	}

	public AssetTradeable getTradeable() {
		return tradeable;
	}

	public void setTradeable(AssetTradeable tradeable) {
		this.tradeable = tradeable;
	}

	public AssetTree getTree() {
		return tree;
	}

	public void setTree(AssetTree tree) {
		this.tree = tree;
	}

	public Crop getCrop() {
		return crop;
	}

	public void setCrop(Crop crop) {
		this.crop = crop;
	}

	public Employment getEmployment() {
		return employment;
	}

	public void setEmployment(Employment employment) {
		this.employment = employment;
	}

	public Inputs getInputs() {
		return inputs;
	}

	public void setInputs(Inputs inputs) {
		this.inputs = inputs;
	}

	public LivestockProducts getLivestockproducts() {
		return livestockproducts;
	}

	public void setLivestockproducts(LivestockProducts livestockproducts) {
		this.livestockproducts = livestockproducts;
	}

	public LivestockSales getLivestocksales() {
		return livestocksales;
	}

	public void setLivestocksales(LivestockSales livestocksales) {
		this.livestocksales = livestocksales;
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public WildFood getWildfood() {
		return wildfood;
	}

	public void setWildfood(WildFood wildfood) {
		this.wildfood = wildfood;
	}

	public Quantile getQuantile() {
		return quantile;
	}

	public void setQuantile(Quantile quantile) {
		this.quantile = quantile;
	}

	public Double getAvgDI() {
		return avgDI;
	}

	public void setAvgDI(Double avgDI) {
		this.avgDI = avgDI;
	}

	public int getQuantSeq() {
		return quantSeq;
	}

	public void setQuantSeq(int quantSeq) {
		this.quantSeq = quantSeq;
	}

	public Double getCropIncome() {
		return cropIncome;
	}

	public void setCropIncome(Double cropIncome) {
		this.cropIncome = cropIncome;
	}

	public Double getEmpIncome() {
		return empIncome;
	}

	public void setEmpIncome(Double empIncome) {
		this.empIncome = empIncome;
	}

	public Double getLsIncome() {
		return lsIncome;
	}

	public void setLsIncome(Double lsIncome) {
		this.lsIncome = lsIncome;
	}

	public Double getTransIncome() {
		return transIncome;
	}

	public void setTransIncome(Double transIncome) {
		this.transIncome = transIncome;
	}

	public Double getWfIncome() {
		return wfIncome;
	}

	public void setWfIncome(Double wfIncome) {
		this.wfIncome = wfIncome;
	}

	public int getNumMales() {
		return numMales;
	}

	public void setNumMales(int numMales) {
		this.numMales = numMales;
	}

	public int getNumFemales() {
		return numFemales;
	}

	public void setNumFemales(int numFemales) {
		this.numFemales = numFemales;
	}

}