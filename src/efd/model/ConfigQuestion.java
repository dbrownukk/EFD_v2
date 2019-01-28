package efd.model;



import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;

import efd.model.Asset.*;
import efd.validations.*;

@View(members = "Question[#prompt, answerType,hint;level,gender;"
		+ "ageRangeLower,ageRangeUpper];configQuestionUse")


@Entity

public class ConfigQuestion extends EFDIdentifiable {

@Required
@Column(length = 25,nullable=false)
private String prompt;
/*************************************************************************************************/

@Column(nullable = false)
private AnswerType answerType;
public enum AnswerType { Text, Decimal, Integer, LOV, IntegerRange, DecimalRange}
/*************************************************************************************************/

@Required
@Column(length = 45,nullable=false)
private String hint;
/*************************************************************************************************/

@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
optional = false)
@Required
@NoFrame
@DescriptionsList(descriptionProperties = "topic")
private ReusableConfigTopic topic;
/*************************************************************************************************/
@Column(nullable = false)
@Required
private Level level;

public enum Level {Study, Household, HouseholdMember, All }
/*************************************************************************************************/
private Gender gender;

public enum Gender {M,F }

/*************************************************************************************************/

@Range(min=1,max=120)
private Integer ageRangeLower;
/*************************************************************************************************/
@Range(min=1,max=120)
private Integer ageRangeUpper;
/*************************************************************************************************/
@OneToMany(mappedBy = "configQuestion", cascade=CascadeType.REMOVE)
private Collection<ConfigQuestionUse> configQuestionUse;
/*************************************************************************************************/
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
public Gender getGender() {
	return gender;
}
public void setGender(Gender gender) {
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





}


	




