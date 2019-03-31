package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import efd.model.Asset.*;
import efd.model.HouseholdMember.*;
import efd.validations.*;

@Views({ @View(members = "Question[#topic,prompt,hint;level,answerType,gender,notes;questionLOVType;Ranges[#ageRangeLower,ageRangeUpper;"
		+ "intRangeLower,intRangeUpper;decRangeLower,decRangeUpper]]") ,
@View(name="FromTopic",members = "Question[#prompt,hint;level,answerType,gender,notes;questionLOVType;Ranges[#ageRangeLower,ageRangeUpper;"
		+ "intRangeLower,intRangeUpper;decRangeLower,decRangeUpper]]")
})

@Tab(properties = "topic.topic, prompt, answerType,hint,level,gender,ageRangeLower,ageRangeUpper", defaultOrder = "${topic} desc")

@Entity

public class ConfigQuestion extends EFDIdentifiable {

	@PreUpdate
	@PrePersist

	private void cqValidate() throws Exception {

		System.out.println("in cq persist / update " + getAnswerType() + " " + getQuestionLOVType());
		if (getAnswerType() == AnswerType.LOV && getQuestionLOVType() == null) {
			System.out.println("in CQ pre per upd 1");
			throw new IllegalStateException("Answer Type of LOV requires a List of Values");
		}

		if ((getAnswerType() == AnswerType.DecimalRange)
				&& (getDecRangeLower() == null || getDecRangeUpper() == null)) {
			System.out.println("in CQ pre per upd 2");
			throw new IllegalStateException("Answer Type of Decimal Range requires Range Values");
		}
		if ((getAnswerType() == AnswerType.DecimalRange ) && ((Double.compare(getDecRangeLower(),getDecRangeUpper())>0)))
		{
			System.out.println("in CQ pre per upd 2");
			throw new IllegalStateException("Answer Type of Decimal Range requires the Lower Range Value to be less than Upper Range Value");
		}
				
		

		if ((getAnswerType() == AnswerType.IntegerRange)
				&& (getIntRangeLower() == null || getIntRangeUpper() == null)) {
			System.out.println("in CQ pre per upd 3");
			throw new IllegalStateException("Answer Type of Integer Range requires Range Values");
		}

		if ((getAnswerType() == AnswerType.IntegerRange ) && (getIntRangeLower() > getIntRangeUpper()))
		{
			System.out.println("in CQ pre per upd 4");
			throw new IllegalStateException("Answer Type of Integer Range requires the Lower Range Value to be less than Upper Range Value");
		}
		
		if(getAgeRangeLower() == null)
			setAgeRangeLower(0);
		if(getAgeRangeUpper() == null)
			setAgeRangeUpper(0);
		
		if ((getLevel() == Level.HouseholdMember ) && (getAgeRangeLower() >= getAgeRangeUpper()) && 
				(getAgeRangeLower()!= 0 && getAgeRangeUpper()!=0)
				
				
				)
		{
			System.out.println("in CQ pre per upd 5");
			throw new IllegalStateException("Household Member Age Range Lower must be less than Upper Age ");
		}
		
		
		
	}

	@Required
	@Column(length = 50, nullable = false)
	private String prompt;
	/*************************************************************************************************/

	@Column(nullable = false)
	@Required
	@OnChange(value = OnChangeAnswerType.class)
	private AnswerType answerType;

	public enum AnswerType {
		Text, Decimal, Integer, LOV, IntegerRange, DecimalRange
	}

	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer intRangeLower;
	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer intRangeUpper;
	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Double decRangeLower;
	/*************************************************************************************************/
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Double decRangeUpper;
	/*************************************************************************************************/

	/*************************************************************************************************/

	@Required
	@Column(length = 45, nullable = false)
	private String hint;
	/*************************************************************************************************/

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true)
	@NoFrame
	@DescriptionsList(descriptionProperties = "topic")
	private ReusableConfigTopic topic;
	/*************************************************************************************************/
	@Column(nullable = false)
	@Required
	@OnChange(value = OnChangeQuestionLevel.class)
	// @Editor("ValidValuesVerticalRadioButton")
	//@DefaultValueCalculator(value=EnumCalculator.class,
	//		properties={@PropertyValue(name="enumType", value="efd.model.ConfigQuestion.Level"),
	//				@PropertyValue(name="value", value = "Study")
	//				})
	private Level level;

	
	public enum Level {
		Study, Household, HouseholdMember
	}

	/*************************************************************************************************/

	// @Editor("ValidValuesVerticalRadioButton")
	private SexQ gender;

	public enum SexQ {
		All, Male, Female
	};

	/*************************************************************************************************/

	@Range(min = 0, max = 120)
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer ageRangeLower;
	/*************************************************************************************************/
	@Range(min = 0, max = 120)
	//@DefaultValueCalculator(ZeroIntegerCalculator.class)
	@DefaultValueCalculator(
			 value=org.openxava.calculators.BigDecimalCalculator.class,
			 properties={ @PropertyValue(name="value", value="120") }
			)
	private Integer ageRangeUpper;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestion")
	@ListProperties("study.studyName, level")
	private Collection<ConfigQuestionUse> configQuestionUse;

	/*************************************************************************************************/
	// @OneToMany(mappedBy = "configQuestion")

	// @ElementCollection
	// @ListProperties("study.studyName, level")

	// private Collection<QuestionLOV> questionLOV;
	/*************************************************************************************************/

	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true)
	@NoFrame
	@DescriptionsList(descriptionProperties = "lovType")
	private QuestionLOVType questionLOVType;

	public QuestionLOVType getQuestionLOVType() {
		return questionLOVType;
	}

	public void setQuestionLOVType(QuestionLOVType questionLOVType) {
		this.questionLOVType = questionLOVType;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public Integer getIntRangeLower() {
		return intRangeLower;
	}

	public void setIntRangeLower(Integer intRangeLower) {
		this.intRangeLower = intRangeLower;
	}

	public Integer getIntRangeUpper() {
		return intRangeUpper;
	}

	public void setIntRangeUpper(Integer intRangeUpper) {
		this.intRangeUpper = intRangeUpper;
	}

	public Double getDecRangeLower() {
		return decRangeLower;
	}

	public void setDecRangeLower(Double decRangeLower) {
		this.decRangeLower = decRangeLower;
	}

	public Double getDecRangeUpper() {
		return decRangeUpper;
	}

	public void setDecRangeUpper(Double decRangeUpper) {
		this.decRangeUpper = decRangeUpper;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public ReusableConfigTopic getTopic() {
		return topic;
	}

	public void setTopic(ReusableConfigTopic topic) {
		this.topic = topic;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public SexQ getGender() {
		return gender;
	}

	public void setGender(SexQ gender) {
		this.gender = gender;
	}

	public Integer getAgeRangeLower() {
		return ageRangeLower;
	}

	public void setAgeRangeLower(Integer ageRangeLower) {
		this.ageRangeLower = ageRangeLower;
	}

	public Integer getAgeRangeUpper() {
		return ageRangeUpper;
	}

	public void setAgeRangeUpper(Integer ageRangeUpper) {
		this.ageRangeUpper = ageRangeUpper;
	}

	public Collection<ConfigQuestionUse> getConfigQuestionUse() {
		return configQuestionUse;
	}

	public void setConfigQuestionUse(Collection<ConfigQuestionUse> configQuestionUse) {
		this.configQuestionUse = configQuestionUse;
	}

	/*************************************************************************************************/

	/*************************************************************************************************/

}
