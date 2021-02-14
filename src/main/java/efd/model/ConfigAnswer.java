package efd.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.PositiveOrZero;

import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.Depends;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;
import org.openxava.calculators.ZeroBigDecimalCalculator;
import org.openxava.calculators.ZeroIntegerCalculator;

import efd.model.ConfigQuestion.AnswerType;

@Views({ @View(members = "answer,textAnswer,integerAnswer,lovAnswer,decimalAnswer,intRangeAnswer,decRangeAnswer,answerType, answerDecLower, answerDecUpper,"
		+ "answerIntLower,answerIntUpper, answerLowerRange,answerUpperRange"),
		@View(name = "fromCRS", members = "answer,configQuestionUse"), })

@Tab(properties = "configQuestionUse.configQuestion.prompt,configQuestionUse.configQuestion.level, displayAnswer ,answerLowerRange, answerUpperRange")

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
	// @Required

	private String answer;

	@Column(length = 45, nullable = true)
	private String answerLowerRange;

	@Column(length = 45, nullable = true)
	private String answerUpperRange;

	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomReportSpec customReportSpec;

	/*************************************************************************************************/

	/*
	 * Transients to enter answer based on answer type for question
	 * 
	 * 
	 */

	
	
	@Transient

	@Depends("answerType")

	public String getDisplayAnswer() {

		System.out.println("In getDisplayAnswer");
		AnswerType answerType2 = getConfigQuestionUse().getConfigQuestion().getAnswerType();

		if (answerType2.equals(AnswerType.Text)) {
			return answer;
		} else if (answerType2.equals(AnswerType.Decimal))
			return answer;
		else if (answerType2.equals(AnswerType.Integer))
			return answer;
		else if (answerType2.equals(AnswerType.LOV)) {
			System.out.println("in lov answr = " + answer);
			if (answer.isEmpty() || answer == null) {
				return (" ");
			}
			//try {
			//return(answer);
			return (getLovAnswer().getLovValue());
			
			//} catch (Exception ex) {
				//System.out.println("Error in ConfigAnswer set LOV Answer" + ex);
				//return("0");
			//}
			
		}

		else if (answerType2.equals(AnswerType.IntegerRange)) {
			if (getAnswerLowerRange() == null) {
				setAnswerLowerRange("0");
			}
			if (getAnswerUpperRange() == null) {
				setAnswerUpperRange("0");
			}
			return getAnswerLowerRange() + " - " + getAnswerUpperRange();
		} else if (answerType2.equals(AnswerType.DecimalRange)) {
			if (getAnswerLowerRange() == null) {
				setAnswerLowerRange("0.0");
			}
			if (getAnswerUpperRange() == null) {
				setAnswerUpperRange("0.0");
			}

			return getAnswerLowerRange() + " - " + getAnswerUpperRange();
		} else
			return "";

	}

	@Transient
	@Hidden
	@PositiveOrZero
	private BigDecimal answerDecLower;
	@Transient
	@Hidden
	@PositiveOrZero
	private BigDecimal answerDecUpper;

	@Transient
	@Hidden
	@PositiveOrZero
	private Integer answerIntLower;
	@PositiveOrZero
	@Transient
	@Hidden
	private Integer answerIntUpper;

	@Transient
	@Hidden
	private AnswerType answerType;

	@Transient
	@Hidden

	private String textAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	@PositiveOrZero
	private BigDecimal decimalAnswer;

	@Transient
	@Hidden
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	@PositiveOrZero
	private Integer integerAnswer;

	@ManyToOne
	// @Transient
	@DescriptionsList(descriptionProperties = "lovValue")
	@NoCreate
	@NoModify
	private QuestionLOV lovAnswer;

	@Transient
	@Hidden
	@PositiveOrZero
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private Integer intRangeAnswer;

	@Transient
	@Hidden
	@PositiveOrZero
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)
	private BigDecimal decRangeAnswer;

	public BigDecimal getAnswerDecLower() {
		return answerDecLower;
	}

	public void setAnswerDecLower(BigDecimal answerDecLower) {
		this.answerDecLower = answerDecLower;
	}

	public BigDecimal getAnswerDecUpper() {
		return answerDecUpper;
	}

	public void setAnswerDecUpper(BigDecimal answerDecUpper) {
		this.answerDecUpper = answerDecUpper;
	}

	public Integer getAnswerIntLower() {
		return answerIntLower;
	}

	public void setAnswerIntLower(Integer answerIntLower) {
		this.answerIntLower = answerIntLower;
	}

	public Integer getAnswerIntUpper() {
		return answerIntUpper;
	}

	public void setAnswerIntUpper(Integer answerIntUpper) {
		this.answerIntUpper = answerIntUpper;
	}

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

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
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

	public String getAnswerLowerRange() {
		return answerLowerRange;
	}

	public void setAnswerLowerRange(String answerLowerRange) {
		this.answerLowerRange = answerLowerRange;
	}

	public String getAnswerUpperRange() {
		return answerUpperRange;
	}

	public void setAnswerUpperRange(String answerUpperRange) {
		this.answerUpperRange = answerUpperRange;
	}

}
