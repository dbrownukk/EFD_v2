package efd.model;


import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Entity 

@Views({
	@View(members="Wealth Group[# community;wgnamelocal,wgnameeng;wgorder,wgwives;wghhsize,wgpercent]")
	// @View(name="SimpleCommunity", members="cinterviewdate,cinterviewsequence,civf,civm,civparticpants"),
	// @View(name="OriginalCommunity", members="site;project;cinterviewdate,cinterviewsequence,civf,civm,civparticipants,interviewers")
})

// @Tab ( editors ="List, Cards", properties="community;wgnamelocal,wgnameeng;wgorder,wgwives;wghhsize,wgpercent") // removes graph option

@Table(name="WealthGroup")
public class WealthGroup {
	
    @Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="WealthGroupID",length=32,unique=true)
    private String wgid;
 
    
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	//@ReferenceView("SimpleCommunity")
	@JoinColumn(name="CommunityID")	
	@DescriptionsList(descriptionProperties="site.locationdistrict,project.projecttitle,cinterviewdate")
    private  Community community;
    
	@Column(name="WGName_Local", length=255)
    private String wgnamelocal ;
    
    @Required
	@Column(name="WGName_Eng", length=255)
    private String wgnameeng ;
	
	@Column(name="WGOrder")
    private int wgorder ;
    
	@Column(name="WGWives")
    private int wgwives ;
    
	@Column(name="WGHHSize")
    private int wghhsize ;
	
	@Column(name="WGPercent") @Min(value=0) @Max(value=100)
	
    private int wgpercent ;

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

	public int getWgwives() {
		return wgwives;
	}

	public void setWgwives(int wgwives) {
		this.wgwives = wgwives;
	}

	public int getWghhsize() {
		return wghhsize;
	}

	public void setWghhsize(int wghhsize) {
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

