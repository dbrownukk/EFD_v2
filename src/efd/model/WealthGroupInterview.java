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

@Views({
@View(members= "Wealth_Group_Interview[# wealthgroup"
		+ ",wgInterviewNumber"
		+ ",wgInterviewers"
		+ ",wgIntervieweesCount"
		+ ";wgFemaleIVees"
		+ ",wgMaleIVees"
		+ ",wgAverageNumberInHH"
		+ ",wgYearType"
		+ ",wgInterviewDate]"
		+ ";WildFood{wildfood}"
		),  
//@View(name="FullCountry",members= "idcountry,isocountrycode,currency,currencySymbol"),
})


//@Tab(editors = "List, Cards", properties = "")

@Entity

@Table(name = "WealthGroupInterview")
public class WealthGroupInterview {
	// ----------------------------------------------------------------------------------------------//

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "WGIID", length = 32, unique = true,nullable=false)
	private String wgiid;
	// ----------------------------------------------------------------------------------------------//

@Column(name = "WGInterviewNumber",nullable=false)	
@Required
private Integer wgInterviewNumber;

@Column(name = "WGInterviewers",nullable=false)	
@Required
private String wgInterviewers;

@Column(name = "WGIntervieweesCount",nullable=false)	
@Required
private Integer wgIntervieweesCount;

@Column(name = "WGFemaleIVees")	
private Integer wgFemaleIVees;

@Column(name = "WGMaleIVees")	
private Integer wgMaleIVees;

@Column(name = "WGAverageNumberInHH")	
private Integer wgAverageNumberInHH;

@Column(name = "WGYearType")	
private String wgYearType;
/* ENUM SET('Good', 'Bad', 'Normal') */ 

@Column(name = "WGInterviewDate")	
@Stereotype("DATE")
private java.util.Date wgInterviewDate;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "WGID")
@Required
@NoFrame
@NoModify
@NoCreate
@DescriptionsList(descriptionProperties="community.site.locationdistrict,wgnameeng")
private WealthGroup wealthgroup;

@OneToMany(mappedBy = "wealthgroupinterview")
@ListProperties("asset.id,wildfoodname,localunit,quantityproduced,quantitsold,priceperunit,otheruse")
private Collection<WildFood> wildfood;





public String getWgiid() {
	return wgiid;
}

public void setWgiid(String wgiid) {
	this.wgiid = wgiid;
}

public Integer getWgInterviewNumber() {
	return wgInterviewNumber;
}

public void setWgInterviewNumber(Integer wgInterviewNumber) {
	this.wgInterviewNumber = wgInterviewNumber;
}

public String getWgInterviewers() {
	return wgInterviewers;
}

public void setWgInterviewers(String wgInterviewers) {
	this.wgInterviewers = wgInterviewers;
}

public Integer getWgIntervieweesCount() {
	return wgIntervieweesCount;
}

public void setWgIntervieweesCount(Integer wgIntervieweesCount) {
	this.wgIntervieweesCount = wgIntervieweesCount;
}

public Integer getWgFemaleIVees() {
	return wgFemaleIVees;
}

public void setWgFemaleIVees(Integer wgFemaleIVees) {
	this.wgFemaleIVees = wgFemaleIVees;
}

public Integer getWgMaleIVees() {
	return wgMaleIVees;
}

public void setWgMaleIVees(Integer wgMaleIVees) {
	this.wgMaleIVees = wgMaleIVees;
}

public Integer getWgAverageNumberInHH() {
	return wgAverageNumberInHH;
}

public void setWgAverageNumberInHH(Integer wgAverageNumberInHH) {
	this.wgAverageNumberInHH = wgAverageNumberInHH;
}

public String getWgYearType() {
	return wgYearType;
}

public void setWgYearType(String wgYearType) {
	this.wgYearType = wgYearType;
}

public java.util.Date getWgInterviewDate() {
	return wgInterviewDate;
}

public void setWgInterviewDate(java.util.Date wgInterviewDate) {
	this.wgInterviewDate = wgInterviewDate;
}

public WealthGroup getWealthgroup() {
	return wealthgroup;
}

public void setWealthgroup(WealthGroup wealthgroup) {
	this.wealthgroup = wealthgroup;
}

public Collection<WildFood> getWildfood() {
	return wildfood;
}

public void setWildfood(Collection<WildFood> wildfood) {
	this.wildfood = wildfood;
}









// ----------------------------------------------------------------------------------------------//




}