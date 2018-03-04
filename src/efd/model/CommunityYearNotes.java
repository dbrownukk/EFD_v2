package efd.model;

import java.time.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;

import efd.actions.*;
import efd.validations.*;


@View(members="year;notes")

@Entity



@Table(name = "CommunityYearNotes")
public class CommunityYearNotes {
	//----------------------------------------------------------------------------------------------//
	@Id
	@Hidden // The property is not shown to the user. It's an internal identifier
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier (1)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "IDCommunityYearNotes", length = 32, unique = true)
	private String communityid;
	//----------------------------------------------------------------------------------------------//
	@ManyToOne(fetch=FetchType.LAZY, // The reference is loaded on demand
	        optional=false)
	@JoinColumn(name="CommunityID")	
	@Required
    private Community community;	
	//----------------------------------------------------------------------------------------------//

	@Column(name="Year", nullable=false, length=4)
	//@Min(value=2001)
	//@Max(value=2020)
	//@Range(min=2001,max=2020)
	@Required
	private int year;
	
	
	//----------------------------------------------------------------------------------------------//
	@Stereotype("MEMO")
	@Column(name="Notes",length=1023,nullable=false)
	@Required
	private String notes;
	//----------------------------------------------------------------------------------------------//
	// get / set 
	//----------------------------------------------------------------------------------------------//
	public String getCommunityid() {
		return communityid;
	}
	public void setCommunityid(String communityid) {
		this.communityid = communityid;
	}
	public Community getCommunity() {
		return community;
	}
	public void setCommunity(Community community) {
		this.community = community;
	}




	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/* get / set */

	
	

}
