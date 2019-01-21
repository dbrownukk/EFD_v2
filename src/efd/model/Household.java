package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@View(members = "Household_Members[householdName;householdNumber,notes];householdMember")


@Entity

@Table(name = "Household")
public class Household extends EFDIdentifiable {

	@Required
	@Column(length = 45)
	private String householdName;
	/*************************************************************************************************/
	@Required
	private int householdNumber;

	/*************************************************************************************************/

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoFrame
	// @JoinColumn(name = "studyid")
	// @DescriptionsList(descriptionProperties = "site.locationdistrict",
	// forViews="FromCommunity")
	// @ReferenceViews({
	// @ReferenceView(forViews="DEFAULT",value="FromWGCommunity")
	// })
	private Study study;
	/*************************************************************************************************/

	@OneToMany(mappedBy = "household" , cascade=CascadeType.REMOVE)
	// @RowAction("Spreadsheet.Template Spreadsheet")
	// @CollectionView("FromCommunity")
	// @ListProperties("wgnameeng,wgnamelocal,wgorder,wgwives,wghhsize,wgpercent+")
	private List<HouseholdMember> householdMember;

	/*************************************************************************************************/
	public String getHouseholdName() {
		return householdName;
	}
	public void setHouseholdName(String householdName) {
		this.householdName = householdName;
	}
	public int getHouseholdNumber() {
		return householdNumber;
	}
	public void setHouseholdNumber(int householdNumber) {
		this.householdNumber = householdNumber;
	}
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	public List<HouseholdMember> getHouseholdMember() {
		return householdMember;
	}
	public void setHouseholdMember(List<HouseholdMember> householdMember) {
		this.householdMember = householdMember;
	}




}
