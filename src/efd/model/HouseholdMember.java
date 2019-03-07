package efd.model;

import javax.persistence.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;

@View(members = "Household_Members[householdMemberName;age,yearOfBirth;gender;headofHousehold;absent;reasonForAbsence]")

@Entity


@Table(name = "HouseholdMember")
public class HouseholdMember extends EFDIdentifiable{
	
	@Required
	@Column(length=45)
	private String householdMemberName;

	/*************************************************************************************************/	

	@Required
	//@Editor("ValidValuesVerticalRadioButton")
	private YN headofHousehold;

	public enum YN {
	Yes, No	
	};
	/*************************************************************************************************/	
	@Required
	//@Editor("ValidValuesVerticalRadioButton")
	private Sex gender;

	public enum Sex {
	Male, Female, Unknown
	};
	/*************************************************************************************************/	
	@Required
	@Range(min=1,max=120)
	private int age;
	/*************************************************************************************************/	
	@Required
	@Range(min=1900,max=2050)
	private int yearOfBirth;
	/*************************************************************************************************/	
	//@Editor("ValidValuesVerticalRadioButton")
	private YN absent;
	/*************************************************************************************************/	
	@Column(length=45)
	private String reasonForAbsence;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@JoinColumn(name = "householdid")
	@Required
	@NoFrame
	private Household household;
	
	/*************************************************************************************************/
	public String getHouseholdMemberName() {
		return householdMemberName;
	}
	public void setHouseholdMemberName(String householdMemberName) {
		this.householdMemberName = householdMemberName;
	}

	public YN getHeadofHousehold() {
		return headofHousehold;
	}
	public void setHeadofHousehold(YN headofHousehold) {
		this.headofHousehold = headofHousehold;
	}
	public Sex getGender() {
		return gender;
	}
	public void setGender(Sex gender) {
		this.gender = gender;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}
	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public YN getAbsent() {
		return absent;
	}
	public void setAbsent(YN absent) {
		this.absent = absent;
	}
	public String getReasonForAbsence() {
		return reasonForAbsence;
	}
	public void setReasonForAbsence(String reasonForAbsence) {
		this.reasonForAbsence = reasonForAbsence;
	}
	public Household getHousehold() {
		return household;
	}
	public void setHousehold(Household household) {
		this.household = household;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	
	
	
	
	
	
	
	
}

