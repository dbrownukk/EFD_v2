package efd.model;

import java.time.*;
import java.util.*;
import java.util.function.Predicate;

import javax.persistence.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.XPersistence;

import efd.model.ConfigQuestion.Level;
import efd.validations.*;

@View(members = "Household_Members[householdMemberName;householdMemberNumber;age,yearOfBirth;gender;headofHousehold;absent,monthsAway;reasonForAbsence];configAnswer")

@Entity

@Table(name = "HouseholdMember")
public class HouseholdMember extends EFDIdentifiable {

	@PrePersist
	

	private void AddHHMNumber() throws Exception {

		System.out.println("prepersist hhmember number = "+getHouseholdMemberNumber());
		if (getHouseholdMemberNumber().isEmpty()) {
			int nextNumberInHH = getHousehold().getHouseholdMember().size() + 1;

			System.out.println("hhm prepersist num = " + nextNumberInHH);

			householdMemberNumber = "HHM" + nextNumberInHH;
		}
		updateHHMQuestions();

	}

	
	
	@PreUpdate
	private void UpdHHMNumber() throws Exception {

		if (getHouseholdMemberNumber().isEmpty()) {
			int nextNumberInHH = (int) (getHousehold().getHouseholdMember().stream()
					.filter(p -> !p.householdMemberNumber.isEmpty()).count()+1);

			System.out.println("hhm preupdate num = " + nextNumberInHH);

			householdMemberNumber = "HHM" + nextNumberInHH;
		}

	}
	
	
	private void updateHHMQuestions() {

		for (ConfigQuestionUse configQuestionUse : this.getHousehold().getStudy().getConfigQuestionUse()) {

			if (configQuestionUse.getConfigQuestion().getLevel().equals(Level.HouseholdMember)) {

				ConfigAnswer ans = new ConfigAnswer();
				ans.setAnswerType(configQuestionUse.getConfigQuestion().getAnswerType());
				ans.setAnswer("");
				ans.setConfigQuestionUse(configQuestionUse);
				ans.setHouseholdMember(this);
				ans.setStudy(configQuestionUse.getStudy());

				XPersistence.getManager().persist(ans);

			}

		}
	}

	// @Required
	@Column(length = 45)
	private String householdMemberName;

	/*************************************************************************************************/

	@ReadOnly
	// @Required
	@Column(length = 45)
	private String householdMemberNumber;

	/*************************************************************************************************/

	@Required
	@Editor("ValidValuesVerticalRadioButton")
	private YN headofHousehold;

	public enum YN {
		Yes, No
	};

	/*************************************************************************************************/
	@Required
	// @Editor("ValidValuesVerticalRadioButton")
	private Sex gender;

	public enum Sex {
		Male, Female, Unknown
	};

	/*************************************************************************************************/
	@Required
	@Range(min = 0, max = 120)
	@OnChange(OnChangeAge.class)
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer age;
	/*************************************************************************************************/
	@Required
	@ReadOnly
	@Depends("age")
	@Range(min = 1900, max = 2050)
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer yearOfBirth; // () {

	/*************************************************************************************************/
	@Editor("ValidValuesVerticalRadioButton")
	private YN absent;
	/*************************************************************************************************/
	@Column(length = 45)
	private String reasonForAbsence;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = false)
	@JoinColumn(name = "householdid")
	@Required
	@NoFrame
	private Household household;

	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private int monthsAway;

	/*************************************************************************************************/
	@OneToMany(mappedBy = "householdMember", cascade = CascadeType.REMOVE)
	@NoCreate
	@AddAction("")
	// @ListProperties("configQuestionUse.configQuestion.prompt,answer")
	@EditOnly
	@EditAction("ConfigAnswer.edit")
	@ListProperties("configQuestionUse.configQuestion.prompt,configQuestionUse.configQuestion.answerType,displayAnswer")
	private Collection<ConfigAnswer> configAnswer;

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

	public Integer getAge() {
		return age == null ? 0 : age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public int getMonthsAway() {
		return monthsAway;
	}

	public void setMonthsAway(int monthsAway) {
		this.monthsAway = monthsAway;
	}

	public Collection<ConfigAnswer> getConfigAnswer() {
		return configAnswer;
	}

	public void setConfigAnswer(Collection<ConfigAnswer> configAnswer) {
		this.configAnswer = configAnswer;
	}

	public String getHouseholdMemberNumber() {
		return householdMemberNumber;
	}

	public void setHouseholdMemberNumber(String householdMemberNumber) {
		this.householdMemberNumber = householdMemberNumber;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

}
