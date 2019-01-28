package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@View(members = "configQuestionUse.study;household,householdMember,answer")


@Entity

@Table(name = "ConfigAnswer")
public class ConfigAnswer extends EFDIdentifiable {



	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study study;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
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
	@Column(length=45,nullable=false)
	@Required
	private String answer;
	/*************************************************************************************************/
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




}
