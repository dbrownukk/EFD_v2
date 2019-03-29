package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;

import efd.actions.*;
import efd.model.ConfigQuestion.*;

@View(members = "answer,textAnswer,integerAnswer,lovAnswer,decimalAnswer,intRangeAnswer,decRangeAnswer,answerType")

@Entity

@Table(name = "ConfigAnswer")
public class ConfigAnswer extends EFDIdentifiable {

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true) // ,cascade=CascadeType.REMOVE)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "householdName")
	private Household household;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@DescriptionsList(descriptionProperties = "householdMemberName")
	private HouseholdMember householdMember;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private ConfigQuestionUse configQuestionUse;
	/*************************************************************************************************/
	@Column(length = 45, nullable = true)
	//@Required
	private String answer;

	/*************************************************************************************************/
	/*
	 * Transients to enter answer based on answer type for question
	 * 
	 * 
	 */

	@Transient
	@Depends("answerType")
	public String getDisplayAnswer() {
		AnswerType answerType2 = getConfigQuestionUse().getConfigQuestion().getAnswerType();

		if (answerType2.equals(AnswerType.Text)) {
			return answer;
		} else if (answerType2.equals(AnswerType.Decimal))
			return answer;
		else if (answerType2.equals(AnswerType.Integer))
			return answer;
		else if (answerType2.equals(AnswerType.LOV) && answer != null) {
			System.out.println("in lov");
			QuestionLOV qlov = XPersistence.getManager().find(QuestionLOV.class, answer.substring(4, 36)); // remove (id
																											// = }
			System.out.println("lov = " + qlov.getLovValue());

			return (qlov.getLovValue());

		}

		else if (answerType2.equals(AnswerType.IntegerRange))
			return answer;
		else if (answerType2.equals(AnswerType.DecimalRange))
			return answer;
		System.out.println("answertype = " + getConfigQuestionUse().getConfigQuestion().getAnswerType());
		System.out.println("answertype = " + getConfigQuestionUse().getConfigAnswer());
		return ("");

	}

	@Transient
	@Hidden
	private AnswerType answerType;

	@Transient
	@Hidden
	private String textAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	private BigDecimal decimalAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer integerAnswer;

	@Transient
	@NoCreate
	@NoModify
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true)
	@DescriptionsList(descriptionProperties = "lovValue")
	private QuestionLOV lovAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer intRangeAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	private BigDecimal decRangeAnswer;

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public Household getHousehold() {
		return household;
	}

	public void setHousehold(Household household) {
		this.household = household;
	}

	public HouseholdMember getHouseholdMember() {
		return householdMember;
	}

	public void setHouseholdMember(HouseholdMember householdMember) {
		this.householdMember = householdMember;
	}

	public ConfigQuestionUse getConfigQuestionUse() {
		return configQuestionUse;
	}

	public void setConfigQuestionUse(ConfigQuestionUse configQuestionUse) {
		this.configQuestionUse = configQuestionUse;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public String getTextAnswer() {
		return textAnswer;
	}

	public void setTextAnswer(String textAnswer) {
		this.textAnswer = textAnswer;
	}

	public BigDecimal getDecimalAnswer() {
		return decimalAnswer;
	}

	public void setDecimalAnswer(BigDecimal decimalAnswer) {
		this.decimalAnswer = decimalAnswer;
	}

	public Integer getIntegerAnswer() {
		return integerAnswer;
	}

	public void setIntegerAnswer(Integer integerAnswer) {
		this.integerAnswer = integerAnswer;
	}

	public QuestionLOV getLovAnswer() {
		return lovAnswer;
	}

	public void setLovAnswer(QuestionLOV lovAnswer) {
		this.lovAnswer = lovAnswer;
	}

	public Integer getIntRangeAnswer() {
		return intRangeAnswer;
	}

	public void setIntRangeAnswer(Integer intRangeAnswer) {
		this.intRangeAnswer = intRangeAnswer;
	}

	public BigDecimal getDecRangeAnswer() {
		return decRangeAnswer;
	}

	public void setDecRangeAnswer(BigDecimal decRangeAnswer) {
		this.decRangeAnswer = decRangeAnswer;
	}

}
