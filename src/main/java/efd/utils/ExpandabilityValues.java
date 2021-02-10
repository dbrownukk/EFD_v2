package efd.utils;

import efd.rest.domain.model.ExpandabilityRule;
import efd.rest.domain.model.Household;
import efd.rest.domain.model.WealthGroupInterview;

public final class ExpandabilityValues {

	private Household household;
	private WealthGroupInterview wealthGroupInterview;
	private ExpandabilityRule expandabilityRule;
	
	public Household getHousehold() {
		return household;
	}
	public void setHousehold(Household household) {
		this.household = household;
	}
	public WealthGroupInterview getWealthGroupInterview() {
		return wealthGroupInterview;
	}
	public void setWealthGroupInterview(WealthGroupInterview wealthGroupInterview) {
		this.wealthGroupInterview = wealthGroupInterview;
	}
	public ExpandabilityRule getExpandabilityRule() {
		return expandabilityRule;
	}
	public void setExpandabilityRule(ExpandabilityRule expandabilityRule) {
		this.expandabilityRule = expandabilityRule;
	}


	
	
	
	
}
