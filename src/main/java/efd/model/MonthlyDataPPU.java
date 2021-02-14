package efd.model;

import java.math.*;

import javax.persistence.*;



@Embeddable



public class MonthlyDataPPU{
	
	@Column(name = "Jan",precision=10, scale=2)
	
	private BigDecimal jan;
	
	@Column(name = "Feb",precision=10, scale=2)
	
	private BigDecimal feb;
	
	@Column(name = "Mar",precision=10, scale=2)
	
	private BigDecimal mar;
	
	@Column(name = "Apr",precision=10, scale=2)
	
	private BigDecimal apr;
	
	@Column(name = "May",precision=10, scale=2)
	
	private BigDecimal may;
	
	@Column(name = "Jun",precision=10, scale=2)
	
	private BigDecimal jun;
	
	@Column(name = "Jul",precision=10, scale=2)
	
	private BigDecimal jul;
	
	@Column(name = "Aug",precision=10, scale=2)
	
	private BigDecimal aug;
	
	@Column(name = "Sep",precision=10, scale=2)
	
	private BigDecimal sep;
	
	@Column(name = "Oct",precision=10, scale=2)
	
	private BigDecimal oct;
	
	@Column(name = "Nov",precision=10, scale=2)
	
	private BigDecimal nov;
	
	@Column(name = "Dec",precision=10, scale=2)
	
	private BigDecimal dec;

	public BigDecimal getJan() {
		return jan;
	}

	public void setJan(BigDecimal jan) {
		this.jan = jan;
	}

	public BigDecimal getFeb() {
		return feb;
	}

	public void setFeb(BigDecimal feb) {
		this.feb = feb;
	}

	public BigDecimal getMar() {
		return mar;
	}

	public void setMar(BigDecimal mar) {
		this.mar = mar;
	}

	public BigDecimal getApr() {
		return apr;
	}

	public void setApr(BigDecimal apr) {
		this.apr = apr;
	}

	public BigDecimal getMay() {
		return may;
	}

	public void setMay(BigDecimal may) {
		this.may = may;
	}

	public BigDecimal getJun() {
		return jun;
	}

	public void setJun(BigDecimal jun) {
		this.jun = jun;
	}

	public BigDecimal getJul() {
		return jul;
	}

	public void setJul(BigDecimal jul) {
		this.jul = jul;
	}

	public BigDecimal getAug() {
		return aug;
	}

	public void setAug(BigDecimal aug) {
		this.aug = aug;
	}

	public BigDecimal getSep() {
		return sep;
	}

	public void setSep(BigDecimal sep) {
		this.sep = sep;
	}

	public BigDecimal getOct() {
		return oct;
	}

	public void setOct(BigDecimal oct) {
		this.oct = oct;
	}

	public BigDecimal getNov() {
		return nov;
	}

	public void setNov(BigDecimal nov) {
		this.nov = nov;
	}

	public BigDecimal getDec() {
		return dec;
	}

	public void setDec(BigDecimal dec) {
		this.dec = dec;
	}


	
	
	
}
