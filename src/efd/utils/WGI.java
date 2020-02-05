package efd.utils;

import java.util.*;

import efd.model.*;

public class WGI {
	private Project project;
	private LivelihoodZone livelihoodZone;
	private Site site;
	private Community community;
	private WealthGroupInterview wealthgroupInterview;
	private WealthGroup wealthgroup;
	private int wgiNumber;
	private Double wgiDI;
	public Double wgiDIAfterChangeScenario;
	private Double wgiAE;
	private Double wgiSOLC;
	private Double wgiSOLCAfterChangeScenario;
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
	
	
	
	
	public Double getWgiDIAfterChangeScenario() {
		return wgiDIAfterChangeScenario;
	}
	public void setWgiDIAfterChangeScenario(Double wgiDIAfterChangeScenario) {
		this.wgiDIAfterChangeScenario = wgiDIAfterChangeScenario;
	}
	public Double getWgiSOLCAfterChangeScenario() {
		return wgiSOLCAfterChangeScenario;
	}
	public void setWgiSOLCAfterChangeScenario(Double wgiSOLCAfterChangeScenario) {
		this.wgiSOLCAfterChangeScenario = wgiSOLCAfterChangeScenario;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}
	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public Community getCommunity() {
		return community;
	}
	public void setCommunity(Community community) {
		this.community = community;
	}
	public WealthGroupInterview getWealthgroupInterview() {
		return wealthgroupInterview;
	}
	public void setWealthgroupInterview(WealthGroupInterview wealthgroupInterview) {
		this.wealthgroupInterview = wealthgroupInterview;
	}
	public WealthGroup getWealthgroup() {
		return wealthgroup;
	}
	public void setWealthgroup(WealthGroup wealthgroup) {
		this.wealthgroup = wealthgroup;
	}
	public int getWgiNumber() {
		return wgiNumber;
	}
	public void setWgiNumber(int wgiNumber) {
		this.wgiNumber = wgiNumber;
	}
	public Double getWgiDI() {
		return wgiDI;
	}
	public void setWgiDI(Double wgiDI) {
		this.wgiDI = wgiDI;
	}
	public Double getWgiAE() {
		return wgiAE;
	}
	public void setWgiAE(Double wgiAE) {
		this.wgiAE = wgiAE;
	}
	public Double getWgiSOLC() {
		return wgiSOLC;
	}
	public void setWgiSOLC(Double wgiSOLC) {
		this.wgiSOLC = wgiSOLC;
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