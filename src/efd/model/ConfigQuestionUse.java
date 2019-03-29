package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import efd.model.ConfigQuestion.*;
import efd.validations.*;

@Views({ @View(members = "QuestionUse[study,configQuestion]") })
//		@View(name = "FromConfigQuestion", members = "QuestionUse[#study,level]") })

@Tab(properties = "study.studyName,study.referenceYear,configQuestion.level,configQuestion.prompt,configQuestion.answerType", defaultOrder = "${configQuestion.level} asc")

@Entity

@Table(name = "ConfigQuestionUse", uniqueConstraints = @UniqueConstraint(name = "configUseQuestion", columnNames = {
		"study_ID", "configQuestion_ID" }))

public class ConfigQuestionUse extends EFDIdentifiable {

	/*
	 * If config Question Level is Study then create a dummy answer Update answer
	 * create is handled in configQuestion
	 */

	/*
	@PrePersist

	private void createAnswer() throws Exception {
		try {
			System.out.println("in configQuestionUse postPersist");

			System.out.println("postpersist level =  " + getConfigQuestion().getLevel());

			if (getConfigQuestion().getLevel().equals(Level.Study)) {
				//XPersistence.getManager();
				System.out.println("its a Study in post persist");
				ConfigAnswer answer = new ConfigAnswer();
				answer.setAnswer("-");
				answer.setStudy(getStudy());
				XPersistence.commit();

			}

		} catch (Exception ex) {
			System.out.println("in jpa exception in configQuestionuse postpersist " + ex);
			return;

		}

	}
	*/

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@NoCreate

	@ReferenceView("FromStdOfLiving")
	// @DescriptionsList(descriptionProperties = "studyName")
	@NoModify
	@NoFrame
	@NoSearch
	@ReadOnly
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(optional = false)
	//@SearchListCondition("${configQuestion.id} != ${id}")
	private ConfigQuestion configQuestion;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestionUse", cascade = CascadeType.REMOVE)
	// @ListProperties("answer")
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Column(nullable = true)
	// @Required
	// @Editor("ValidValuesRadioButton")
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
