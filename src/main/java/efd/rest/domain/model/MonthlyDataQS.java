package efd.rest.domain.model;

import javax.persistence.*;



@Embeddable



public class MonthlyDataQS{
	
	@Column(name = "Jan")
	private int jan;
	
	@Column(name = "Feb")
	private int feb;
	
	@Column(name = "Mar")
	private int mar;
	
	@Column(name = "Apr")
	private int apr;
	
	@Column(name = "May")
	private int may;
	
	@Column(name = "Jun")
	private int jun;
	
	@Column(name = "Jul")
	private int jul;
	
	@Column(name = "Aug")
	private int aug;
	
	@Column(name = "Sep")
	private int sep;
	
	@Column(name = "Oct")
	private int oct;
	
	@Column(name = "Nov")
	private int nov;
	
	@Column(name = "Dec")
	private int dec;

	public int getJan() {
		return jan;
	}

	public void setJan(int jan) {
		this.jan = jan;
	}

	public int getFeb() {
		return feb;
	}

	public void setFeb(int feb) {
		this.feb = feb;
	}

	public int getMar() {
		return mar;
	}

	public void setMar(int mar) {
		this.mar = mar;
	}

	public int getApr() {
		return apr;
	}

	public void setApr(int apr) {
		this.apr = apr;
	}

	public int getMay() {
		return may;
	}

	public void setMay(int may) {
		this.may = may;
	}

	public int getJun() {
		return jun;
	}

	public void setJun(int jun) {
		this.jun = jun;
	}

	public int getJul() {
		return jul;
	}

	public void setJul(int jul) {
		this.jul = jul;
	}

	public int getAug() {
		return aug;
	}

	public void setAug(int aug) {
		this.aug = aug;
	}

	public int getSep() {
		return sep;
	}

	public void setSep(int sep) {
		this.sep = sep;
	}

	public int getOct() {
		return oct;
	}

	public void setOct(int oct) {
		this.oct = oct;
	}

	public int getNov() {
		return nov;
	}

	public void setNov(int nov) {
		this.nov = nov;
	}

	public int getDec() {
		return dec;
	}

	public void setDec(int dec) {
		this.dec = dec;
	}
	
	

     

	
	
	
	
	
}
