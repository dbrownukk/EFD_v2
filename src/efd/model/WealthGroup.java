package efd.model;

import java.math.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;

@Views({ @View(members = "Wealth_Group[# wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent];wgcharacteristicsresource"),
		@View(name = "SimpleWealthGroup", members = "Wealth_Group[# wgnamelocal,wgnameeng;wgorder,wgwives;wghhsize,wgpercent];wgcharacteristicsresource"),
		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
		@View(name = "OriginalCommunity", members = "site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers"),
		@View(name = "SimpleWealthGroup", members = "community") })
/*
 * @Tab ( editors ="List, Cards", properties="locationdistrict," +
 * "community;wgnamelocal,wgnameeng;wgorder,wgwives;" +
 * "wghhsize,wgpercent+,cproject") // removes graph option
 */

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
	@JoinColumn(name = "CommunityID")
	@DescriptionsList(descriptionProperties = "site.locationdistrict")
	private Community community;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy="wealthgroup")
	private Collection<WGCharacteristicsResource> wgcharacteristicsresource;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGName_Local", length = 255)
	private String wgnamelocal;
	// ----------------------------------------------------------------------------------------------//

	@Required
	@Column(name = "WGName_Eng", length = 255)
	private String wgnameeng;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGOrder")
	private int wgorder;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGWives")
	@Digits(integer=3,fraction=2)
	private BigDecimal  wgwives;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGHHSize")
	@Digits(integer=3,fraction=2)
	private BigDecimal  wghhsize;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGPercent")
	@Min(value = 0)
	@Max(value = 100)
	// @PropertyValidator(Valid100PerCent.class)
	private int wgpercent;
	// ----------------------------------------------------------------------------------------------//

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

	public Collection<WGCharacteristicsResource> getWgcharacteristicsresource() {
		return wgcharacteristicsresource;
	}

	public void setWgcharacteristicsresource(Collection<WGCharacteristicsResource> wgcharacteristicsresource) {
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

	
	
	

	// Getters and Setters

}
