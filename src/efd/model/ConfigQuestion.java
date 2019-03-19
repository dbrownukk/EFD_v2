package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

import efd.model.Asset.*;
import efd.model.HouseholdMember.*;
import efd.validations.*;



@Views({@View(members="Question[#topic,prompt,hint;level,answerType,gender,notes;questionLOVType;Ranges[#ageRangeLower,ageRangeUpper;"
		+ "intRangeLower,intRangeUpper;decRangeLower,decRangeUpper]]")//,
//	@View(name="FromQuestionUse",members="Question[topic,prompt,answerType,hint,level]")
})

@Tab(properties="topic.topic, prompt, answerType,hint,level,gender,ageRangeLower,ageRangeUpper",defaultOrder="${topic} desc") 

@Entity

public class ConfigQuestion extends EFDIdentifiable {



	
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
	private Integer intRangeLower;
	/*************************************************************************************************/
	private Integer intRangeUpper;
	/*************************************************************************************************/
	private Double decRangeLower;
	/*************************************************************************************************/
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
	//	@Editor("ValidValuesVerticalRadioButton")
	private Level level;

	public enum Level {Study, Household, HouseholdMember }
	/*************************************************************************************************/
	
	//@Editor("ValidValuesVerticalRadioButton")
	private SexQ gender;

	public enum SexQ {
	All,Male, Female
	};

	/*************************************************************************************************/

	@Range(min = 1, max = 120)
	private Integer ageRangeLower;
	/*************************************************************************************************/
	@Range(min = 1, max = 120)
	private Integer ageRangeUpper;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestion")
	@ListProperties("study.studyName, level")
	private Collection<ConfigQuestionUse> configQuestionUse;
	
	/*************************************************************************************************/
	//@OneToMany(mappedBy = "configQuestion")
	
	//@ElementCollection
	//@ListProperties("study.studyName, level")

	//private Collection<QuestionLOV> questionLOV;
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
