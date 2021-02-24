package efd.model;

import javax.persistence.*;



@Embeddable

public class MonthlyDataQP{
	
	@Column(name = "Jan")
	private Integer jan;
	
	@Column(name = "Feb")
	private Integer feb;
	
	@Column(name = "Mar")
	private Integer mar;
	
	@Column(name = "Apr")
	private Integer apr;
	
	@Column(name = "May")
	private Integer may;
	
	@Column(name = "Jun")
	private Integer jun;
	
	@Column(name = "Jul")
	private Integer jul;
	
	@Column(name = "Aug")
	private Integer aug;
	
	@Column(name = "Sep")
	private Integer sep;
	
	@Column(name = "Oct")
	private Integer oct;
	
	@Column(name = "Nov")
	private Integer nov;
	
	@Column(name = "Dec")
	private Integer dec;

	public Integer getJan() {
		return jan;
	}

	public void setJan(Integer jan) {
		this.jan = jan;
	}

	public Integer getFeb() {
		return feb;
	}

	public void setFeb(Integer feb) {
		this.feb = feb;
	}

	public Integer getMar() {
		return mar;
	}

	public void setMar(Integer mar) {
		this.mar = mar;
	}

	public Integer getApr() {
		return apr;
	}

	public void setApr(Integer apr) {
		this.apr = apr;
	}

	public Integer getMay() {
		return may;
	}

	public void setMay(Integer may) {
		this.may = may;
	}

	public Integer getJun() {
		return jun;
	}

	public void setJun(Integer jun) {
		this.jun = jun;
	}

	public Integer getJul() {
		return jul;
	}

	public void setJul(Integer jul) {
		this.jul = jul;
	}

	public Integer getAug() {
		return aug;
	}

	public void setAug(Integer aug) {
		this.aug = aug;
	}

	public Integer getSep() {
		return sep;
	}

	public void setSep(Integer sep) {
		this.sep = sep;
	}

	public Integer getOct() {
		return oct;
	}

	public void setOct(Integer oct) {
		this.oct = oct;
	}

	public Integer getNov() {
		return nov;
	}

	public void setNov(Integer nov) {
		this.nov = nov;
	}

	public Integer getDec() {
		return dec;
	}

	public void setDec(Integer dec) {
		this.dec = dec;
	}


	
}
