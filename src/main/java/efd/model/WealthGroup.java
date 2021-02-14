package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Range;
import org.openxava.annotations.*;
import org.openxava.calculators.*;

@Views({ @View(members = "Wealth_Group[# wgnameeng,wgnamelocal;this.community;wgorder;wgwives,wghhsize,wgpercent;wgpercenttotal];wgcharacteristicsresource"),
		@View(name = "FromCommunity", members = "Wealth_Group[# wgnameeng,wgnamelocal;wgorder;wgwives,wghhsize,wgpercent];wgcharacteristicsresource"),
		@View(name = "SimpleWealthGroup", members = "Wealth_Group[# wgnamelocal,wgnameeng;wgorder;wgwives,wghhsize,wgpercent];wgcharacteristicsresource"),
		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
		@View(name = "OriginalCommunity", members = "site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers"),
		@View(name = "SimpleWG", members = ";cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers"),
		@View(name = "ReportWG", members = "wealthGroupInterview") })

@Tab(editors = "List, Cards", properties = "community.site.subdistrict,wgnameeng,wgnamelocal;wgorder;wghhsize,wgpercent+")

@Entity

@Table(name = "WealthGroup")
public class WealthGroup {
	// ----------------------------------------------------------------------------------------------//

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "WealthGroupID", length = 32, unique = true)
	private String wgid;
	// ----------------------------------------------------------------------------------------------//

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@NoFrame
	@JoinColumn(name = "CommunityID")
	@DescriptionsList(descriptionProperties = "site.locationdistrict", forViews = "FromCommunity")
	@ReferenceViews({ @ReferenceView(forViews = "DEFAULT", value = "FromWGCommunity") })
	private Community community;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "wealthgroup", cascade = CascadeType.ALL)
	@AddAction("")
	// @ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,resourcesubtype.resourcesubtypeunit")
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,wgresourceunit")

	private List<WGCharacteristicsResource> wgcharacteristicsresource;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGName_Local", length = 255)
	private String wgnamelocal;
	// ----------------------------------------------------------------------------------------------//

	@Required
	@Column(name = "WGName_Eng", length = 255)
	private String wgnameeng;
	// ----------------------------------------------------------------------------------------------//

	@Required
	@Range(min=1,max=3)
	@Column(name = "WGOrder")
	private int wgorder;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGWives")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal wgwives;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGHHSize")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal wghhsize;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGPercent")
	// @OnChange(OnChangeWgpercenttotal.class)
	@Min(value = 1, message="Percent must be greater than 0")
	@Max(value = 100,message="Percent maximum is 100")
	@DefaultValueCalculator(ZeroIntegerCalculator.class)

	private int wgpercent;
	// ----------------------------------------------------------------------------------------------//
	@Transient
	private double defaultDI;
	@Transient
	private double kcalReq;
	@Transient
	private double socialinclusionStol;
	@Transient
	private double survivalStol;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "wealthgroup", cascade = CascadeType.ALL)
	private List<WealthGroupInterview> wealthGroupInterview;

	public String getWgid() {
		return wgid;
	}

	public void setWgid(String wgid) {
		this.wgid = wgid;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public List<WGCharacteristicsResource> getWgcharacteristicsresource() {
		return wgcharacteristicsresource;
	}

	public void setWgcharacteristicsresource(List<WGCharacteristicsResource> wgcharacteristicsresource) {
		this.wgcharacteristicsresource = wgcharacteristicsresource;
	}

	public String getWgnamelocal() {
		return wgnamelocal;
	}

	public void setWgnamelocal(String wgnamelocal) {
		this.wgnamelocal = wgnamelocal;
	}

	public String getWgnameeng() {
		return wgnameeng;
	}

	public void setWgnameeng(String wgnameeng) {
		this.wgnameeng = wgnameeng;
	}

	public int getWgorder() {
		return wgorder;
	}

	public void setWgorder(int wgorder) {
		this.wgorder = wgorder;
	}

	public BigDecimal getWgwives() {
		return wgwives;
	}

	public void setWgwives(BigDecimal wgwives) {
		this.wgwives = wgwives;
	}

	public BigDecimal getWghhsize() {
		return wghhsize;
	}

	public void setWghhsize(BigDecimal wghhsize) {
		this.wghhsize = wghhsize;
	}

	public int getWgpercent() {
		return wgpercent;
	}

	public void setWgpercent(int wgpercent) {
		this.wgpercent = wgpercent;
	}



	public double getKcalReq() {
		return kcalReq;
	}

	public void setKcalReq(double kcalReq) {
		this.kcalReq = kcalReq;
	}

	public double getSocialinclusionStol() {
		return socialinclusionStol;
	}

	public void setSocialinclusionStol(double socialinclusionStol) {
		this.socialinclusionStol = socialinclusionStol;
	}



	public double getSurvivalStol() {
		return survivalStol;
	}

	public void setSurvivalStol(double survivalStol) {
		this.survivalStol = survivalStol;
	}

	public List<WealthGroupInterview> getWealthGroupInterview() {
		return wealthGroupInterview;
	}

	public void setWealthGroupInterview(List<WealthGroupInterview> wealthGroupInterview) {
		this.wealthGroupInterview = wealthGroupInterview;
	}

	public double getDefaultDI() {
		return defaultDI;
	}

	public void setDefaultDI(double defaultDI) {
		this.defaultDI = defaultDI;
	}



	
	
}
