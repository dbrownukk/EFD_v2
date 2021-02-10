package efd.utils;


import efd.rest.domain.model.*;

public class WGISub {
	private int wgiid;
	private Community community;
	private WealthGroupInterview wealthGroupInterview;
	private WealthGroup wealthGroup;
	private AssetLand assetLand;
	private AssetLiveStock assetLivestock;
	private ResourceSubType assetRST;
	private String assetName;
	private Double assetValue;
	private Double wgiDI;
	private int column;



	public int getWgiid() {
		return wgiid;
	}

	public void setWgiid(int wgiid) {
		this.wgiid = wgiid;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public WealthGroupInterview getWealthGroupInterview() {
		return wealthGroupInterview;
	}

	public void setWealthGroupInterview(WealthGroupInterview wealthGroupInterview) {
		this.wealthGroupInterview = wealthGroupInterview;
	}

	public WealthGroup getWealthGroup() {
		return wealthGroup;
	}

	public void setWealthGroup(WealthGroup wealthGroup) {
		this.wealthGroup = wealthGroup;
	}

	public AssetLand getAssetLand() {
		return assetLand;
	}

	public void setAssetLand(AssetLand assetLand) {
		this.assetLand = assetLand;
	}

	public AssetLiveStock getAssetLivestock() {
		return assetLivestock;
	}

	public void setAssetLivestock(AssetLiveStock assetLivestock) {
		this.assetLivestock = assetLivestock;
	}

	public ResourceSubType getAssetRST() {
		return assetRST;
	}

	public void setAssetRST(ResourceSubType assetRST) {
		this.assetRST = assetRST;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Double getAssetValue() {
		return assetValue;
	}

	public void setAssetValue(Double assetValue) {
		this.assetValue = assetValue;
	}

	public Double getWgiDI() {
		return wgiDI;
	}

	public void setWgiDI(Double wgiDI) {
		this.wgiDI = wgiDI;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}