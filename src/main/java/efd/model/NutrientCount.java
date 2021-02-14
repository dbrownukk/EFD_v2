package efd.model;

public class NutrientCount {
	private MicroNutrient mn;
	private Double mnAmount;
	private int wgOrder;
	private int hhNumber;
	private Double averageNumberInHousehold;
	private boolean isDDI;
	
	
	

	public int getHhNumber() {
		return hhNumber;
	}




	public void setHhNumber(int hhNumber) {
		this.hhNumber = hhNumber;
	}




	public MicroNutrient getMn() {
		return mn;
	}




	public void setMn(MicroNutrient mn) {
		this.mn = mn;
	}




	public Double getMnAmount() {
		return mnAmount;
	}




	public void setMnAmount(Double mnAmount) {
		this.mnAmount = mnAmount;
	}




	public int getWgOrder() {
		return wgOrder;
	}




	public void setWgOrder(int wgOrder) {
		this.wgOrder = wgOrder;
	}




	public Double getAverageNumberInHousehold() {
		return averageNumberInHousehold;
	}




	public void setAverageNumberInHousehold(Double averageNumberInHousehold) {
		this.averageNumberInHousehold = averageNumberInHousehold;
	}




	public boolean isDDI() {
		return isDDI;
	}




	public void setDDI(boolean isDDI) {
		this.isDDI = isDDI;
	}




	public NutrientCount() {
	}
	
	
	
}