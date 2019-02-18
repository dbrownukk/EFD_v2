package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.model.ConfigQuestion.*;

@Views({ @View(members = "QuestionUse[study;level;configQuestion]")})
//		@View(name = "FromConfigQuestion", members = "QuestionUse[#study,level]") })

//@Tab(properties = "study.studyName,study.referenceYear,configQuestion.prompt")

@Entity

@Table(name = "ConfigQuestionUse",uniqueConstraints = @UniqueConstraint(name="configUseQuestion",columnNames = { "study_ID","configQuestion_ID"}))



public class ConfigQuestionUse extends EFDIdentifiable {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	//@NoFrame
	private ConfigQuestion configQuestion;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestionUse") // , cascade=CascadeType.REMOVE)
	//@ListProperties("answer")
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Column(nullable = false)
	@Required
	@Editor("ValidValuesRadioButton")
	private Level level;

	/*************************************************************************************************/
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public ConfigQuestion getConfigQuestion() {
		return configQuestion;
	}

	public void setConfigQuestion(ConfigQuestion configQuestion) {
		this.configQuestion = configQuestion;
	}

	public Collection<ConfigAnswer> getConfigAnswer() {
		return configAnswer;
	}

	public void setConfigAnswer(Collection<ConfigAnswer> configAnswer) {
		this.configAnswer = configAnswer;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
