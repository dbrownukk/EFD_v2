package efd.model;

import java.math.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.lang3.*;
import org.hibernate.annotations.GenericGenerator;
import org.hsqldb.persist.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

//import com.sun.corba.se.spi.orbutil.fsm.Guard.*;

import efd.validations.*;

@Views({ @View(members = "Wealth_Group[# wgnameeng,wgnamelocal;this.community;wgorder,wgwives;wghhsize,wgpercent;wgpercenttotal];wgcharacteristicsresource"),
 	@View(name = "FromCommunity",members = "Wealth_Group[# wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent];wgcharacteristicsresource"),
	@View(name = "SimpleWealthGroup", members = "Wealth_Group[# wgnamelocal,wgnameeng;wgorder,wgwives;wghhsize,wgpercent];wgcharacteristicsresource"),
	@View(name = "SimpleCommunity", members = "cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
	@View(name = "OriginalCommunity", members = "site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers"),
	@View(name = "SimpleWG", members = ";cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers")
	 })


@Tab(editors = "List, Cards", properties = "community.site.subdistrict,wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent+")

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
	@DescriptionsList(descriptionProperties = "site.locationdistrict", forViews="FromCommunity")
	@ReferenceViews({
	@ReferenceView(forViews="DEFAULT",value="FromWGCommunity")
	})
	private Community community;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "wealthgroup")
	//@ElementCollection
	//@ListProperties("resourcesubtype.resourcetype.resourcetypename,resourcesubtype.resourcetypename,resourcesubtype.resourcesubtypeunit")
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
	//@OnChange(OnChangeWgpercenttotal.class)
	@Min(value = 1)
	@Max(value = 100)

	private int wgpercent;
	// ----------------------------------------------------------------------------------------------//

	@OneToMany(mappedBy = "wealthgroup")
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



	

}
