package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.util.*;

import efd.model.ConfigQuestion.*;
import efd.validations.*;

@Views({ @View(members = "QuestionUse[study,configQuestion]")})
//		@View(name = "FromConfigQuestion", members = "QuestionUse[#study,level]") })

@Tab(properties = "study.studyName,study.referenceYear,configQuestion.level,configQuestion.prompt,configQuestion.answerType")

@Entity

@Table(name = "ConfigQuestionUse",uniqueConstraints = @UniqueConstraint(name="configUseQuestion",columnNames = { "study_ID","configQuestion_ID"}))



public class ConfigQuestionUse extends EFDIdentifiable {
	
	/* Fails in Copy Study  - need to discuss use of Level in Study and Question Usages
	@PrePersist
	@PreUpdate
	private void validate() throws Exception {
		try {
			System.out.println("in configQuestionUse update");
			
			setLevel(getConfigQuestion().getLevel());

			

		} catch (Exception ex) {
			System.out.println("in jpa exception in configQuestion " + ex);
			return;

		}

	}
	*/
	
	

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@NoCreate
	
	@ReferenceView("FromStdOfLiving")
	//@DescriptionsList(descriptionProperties = "studyName")
	@NoModify
	@NoFrame
	@NoSearch
	@ReadOnly
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(optional = false)
	//@ReferenceView("FromQuestionUse")
	private ConfigQuestion configQuestion;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestionUse",cascade=CascadeType.REMOVE)
	//@ListProperties("answer")
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Column(nullable = true)
	//@Required
	//@Editor("ValidValuesRadioButton")
	@DisplaySize(30)
	@OnChange(value = OnChangeQuestionUseLevel.class)
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
