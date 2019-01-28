package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import efd.model.ConfigQuestion.*;

@View(members = "study;configQuestion,level")


@Entity

@Table(name = "ConfigQuestionUse")
public class ConfigQuestionUse extends EFDIdentifiable {



	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Required
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private ConfigQuestion configQuestion;
	/*************************************************************************************************/
	@OneToMany(mappedBy = "configQuestionUse", cascade=CascadeType.REMOVE)
	private Collection<ConfigAnswer> configAnswer;
	/*************************************************************************************************/
	@Column(nullable=false)
	@Required
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
