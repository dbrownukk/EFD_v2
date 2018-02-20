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

import com.sun.corba.se.spi.orbutil.fsm.Guard.*;

import efd.validations.*;




// @Tab(editors = "List, Cards", properties = "community.site.subdistrict,wgnameeng,wgnamelocal;wgorder,wgwives;wghhsize,wgpercent+")

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
private Integer WGInterviewNumber;

@Column(name = "WGInterviewers",nullable=false)	
@Required
private String WGInterviewers;

@Column(name = "WGIntervieweesCount",nullable=false)	
@Required
private Integer WGIntervieweesCount;

@Column(name = "WGFemaleIVees")	
private Integer WGFemaleIVees;

@Column(name = "WGMaleIVees")	
private Integer WGMaleIVees;

@Column(name = "WGAverageNumberInHH")	
private Integer WGAverageNumberInHH;

@Column(name = "WGYearType")	
private String WGYearType;
/* ENUM SET('Good', 'Bad', 'Normal') */ 

@Column(name = "WGInterviewDate")	
@Stereotype("DATE")
private java.util.Date WGInterviewDate;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "WGID")
@Required
@NoFrame
private WealthGroup wealthgroup;



public String getWgiid() {
	return wgiid;
}

public void setWgiid(String wgiid) {
	this.wgiid = wgiid;
}

public Integer getWGInterviewNumber() {
	return WGInterviewNumber;
}

public void setWGInterviewNumber(Integer wGInterviewNumber) {
	WGInterviewNumber = wGInterviewNumber;
}

public String getWGInterviewers() {
	return WGInterviewers;
}

public void setWGInterviewers(String wGInterviewers) {
	WGInterviewers = wGInterviewers;
}

public Integer getWGIntervieweesCount() {
	return WGIntervieweesCount;
}

public void setWGIntervieweesCount(Integer wGIntervieweesCount) {
	WGIntervieweesCount = wGIntervieweesCount;
}

public Integer getWGFemaleIVees() {
	return WGFemaleIVees;
}

public void setWGFemaleIVees(Integer wGFemaleIVees) {
	WGFemaleIVees = wGFemaleIVees;
}

public Integer getWGMaleIVees() {
	return WGMaleIVees;
}

public void setWGMaleIVees(Integer wGMaleIVees) {
	WGMaleIVees = wGMaleIVees;
}

public Integer getWGAverageNumberInHH() {
	return WGAverageNumberInHH;
}

public void setWGAverageNumberInHH(Integer wGAverageNumberInHH) {
	WGAverageNumberInHH = wGAverageNumberInHH;
}

public String getWGYearType() {
	return WGYearType;
}

public void setWGYearType(String wGYearType) {
	WGYearType = wGYearType;
}

public java.util.Date getWGInterviewDate() {
	return WGInterviewDate;
}

public void setWGInterviewDate(java.util.Date wGInterviewDate) {
	WGInterviewDate = wGInterviewDate;
}

public WealthGroup getWealthgroup() {
	return wealthgroup;
}

public void setWealthgroup(WealthGroup wealthgroup) {
	this.wealthgroup = wealthgroup;
}

// ----------------------------------------------------------------------------------------------//




}