package efd.utils;

import efd.rest.domain.model.*;

public class QuantHousehold {
	private Quantile quantile;
	private Household household;
	private Double averageDI;

	public QuantHousehold() {
	}

	public Quantile getQuantile() {
		return quantile;
	}

	public void setQuantile(Quantile quantile) {
		this.quantile = quantile;
	}

	public Household getHousehold() {
		return household;
	}

	public void setHousehold(Household household) {
		this.household = household;
	}

	public Double getAverageDI() {
		return averageDI;
	}

	public void setAverageDI(Double averageDI) {
		this.averageDI = averageDI;
	}
}