package efd.model;

import java.math.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.hsqldb.persist.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.validations.*;

@Views({ @View(members = "Wealth_Group[# wgnameeng,wgnamelocal;community;wgorder,wgwives;wghhsize,wgpercent,wgpercenttotal];wgcharacteristicsresource"),
	 	@View(name = "FromCommunity",members = "Wealth_Group[# wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent,wgpercenttotal];wgcharacteristicsresource"),
		@View(name = "SimpleWealthGroup", members = "Wealth_Group[# wgnamelocal,wgnameeng;wgorder,wgwives;wghhsize,wgpercent];wgcharacteristicsresource"),
		@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
		@View(name = "OriginalCommunity", members = "site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers"),
		@View(name = "SimpleWealthGroup", members = "community") })

@Tab(editors = "List, Cards", properties = "wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent+")

@Entity

@Table(name = "WealthGroup")
public class WealthGroup {
	// ----------------------------------------------------------------------------------------------//
	@PreUpdate // Just before updating the database
	private void validate() throws Exception {
		//System.out.println("in preupdate");
		//System.out.println((getWgpercenttotal()  ));
		//System.out.println("in preupdate 2");
		//if (Integer.parseInt(getWgpercenttotal()) > 100) { // The validation
															// logic
			// The validation exception from Bean Validation
		//throw new javax.validation.ValidationException(
				//	XavaResources.getString("Total_Wealthgroups_Percentage_greater_than_100"));
	
	
	}

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
	@DescriptionsList(descriptionProperties = "site.locationdistrict", forViews="FromCommunity")
	@ReferenceViews({
	@ReferenceView(forViews="DEFAULT",value="FromWGCommunity")
	})
	private Community community;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "wealthgroup", cascade=CascadeType.REMOVE)
	//@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,resourcesubtype.resourcesubtypeunit")
	@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,wgresourceunit")
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
	@Digits(integer = 3, fraction = 2)
	private BigDecimal wgwives;
	// ----------------------------------------------------------------------------------------------//

	@Column(name = "WGHHSize")
	@Digits(integer = 3, fraction = 2)
	private BigDecimal wghhsize;
	// ----------------------------------------------------------------------------------------------//
	@Transient
	private int ptotal;
	
	
	
	@Column(name = "WGPercent")
	@Min(value = 1)
	@Max(value = 100)
	// @PropertyValidator(Valid100PerCent.class)
	// @PropertyValidator(value=Valid100PerCent.class,
	// properties=@PropertyValue(name="communityid",
	// value="community.getCommunityid()"))
	private int wgpercent;
	// ----------------------------------------------------------------------------------------------//
	// @Transient
	// @Hidden
	// public BigDecimal wgtotalpercent;

	@Transient
	@ReadOnly
	// @Hidden
	// @Max(100)
	@Depends("wgpercent")
	public String getWgpercenttotal() {
		EntityManager em = XPersistence.getManager();

		Query q = em.createNativeQuery("select sum(wgpercent) from wealthgroup where communityid = ?1");
		//System.out.println("in get 33");
		//System.out.println(community.getCommunityid());
		q.setParameter(1, community.getCommunityid());
		//System.out.println("wgpercenttotal in getter");
		//System.out.println(q.getSingleResult().toString());
		// wgtotalpercent = (BigDecimal) q.getSingleResult();
		//System.out.println("before return in getter");
		// System.out.println(wgtotalpercent.toPlainString());
		String result = q.getSingleResult().toString();
		// em.close();
		return result;
	}

	/*
	 * public void setWgpercenttotal(String wgpercenttotal) {
	 * System.out.println("in wgpercenttotal setter");
	 * System.out.println(wgpercenttotal); this.wgpercenttotal = wgpercenttotal;
	 * }
	 */

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

	public int getPtotal() {
		return ptotal;
	}

	public void setPtotal(int ptotal) {
		this.ptotal = ptotal;
	}

	public int getWgpercent() {
		return wgpercent;
	}

	public void setWgpercent(int wgpercent) {
		this.wgpercent = wgpercent;
	}



	

}
