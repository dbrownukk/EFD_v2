package efd.model;

import javax.persistence.*;

import org.openxava.annotations.*;


@Entity


@Table(name = "Household")
public class Household extends Core{
	
	@Required
	@Column(length=45)
	private String householdName;
	/*************************************************************************************************/	

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	//@NoFrame
	@JoinColumn(name = "studyid")
	//@DescriptionsList(descriptionProperties = "site.locationdistrict", forViews="FromCommunity")
	//@ReferenceViews({
	//@ReferenceView(forViews="DEFAULT",value="FromWGCommunity")
	//})
	private Study study;
	/*************************************************************************************************/
	public String getHouseholdName() {
		return householdName;
	}
	public void setHouseholdName(String householdName) {
		this.householdName = householdName;
	}
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}	

	
	
	
	
}
