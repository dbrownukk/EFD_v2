package efd.model;

import java.util.*;



import javax.persistence.*;
//import javax.validation.constraints.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.model.*;

@Views({ @View(members = "specName,totalQuantilePercentage,warningMessage,Report{report},Quantile{quantile},Category{category},ResourceType{resourceType},ResourceSubType{resourceSubType},ConfigAnswer{configAnswer}"),
		@View(name = "rst", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "rt", members = "specName,quantile,rType,report,resourceSubType"),
		@View(name = "cat", members = "specName,quantile,rType,report,resourceSubType") })

@Entity

/* Unique constraints need to go in intersection tables for manytomany */



public class CustomReportSpec extends Identifiable {

	@Column(length = 45)
	@Required

	//@Pattern(regexp = "[a-z-A-Z]*", message = "Spec Name has invalid characters")
	@Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Spec Name has invalid characters")
	
	private String specName;

	@ListProperties("name,sequence, percentage+")
	@AddAction("")
	@OneToMany(mappedBy = "customReportSpec")
	private Collection<Quantile> quantile;

	@ManyToMany
	@JoinTable(name = "ReportInclusion")
	@NewAction("")
	@Column(unique=true)
	@NoModify
	@SearchListCondition("${method} = 1") // OIHM
	private Collection<Report> report;

	@ManyToMany
	@NewAction("")
	@Column(unique=true)
	private Collection<ResourceType> resourceType;

	@ManyToMany
	@NewAction("")
	@ListProperties("resourcetype.resourcetypename,resourcetypename,resourcesubtypeunit,resourcesubtypekcal,resourcesubtypesynonym.resourcetypename")
	@Column(unique=true)
	private Collection<ResourceSubType> resourceSubType;

	@ManyToMany
	@NewAction("")
	@Column(unique=true)
	private Collection<Category> category;

	//@OneToMany(mappedBy = "customReportSpec")
	//@NewAction("ReportSpecUse.new")
	//@EditAction("ReportSpecUse.edit")
	//@RemoveSelectedAction("ReportSpecUse.remove")
	//@ListProperties("study.studyName,study.referenceYear")
	//private Collection<ReportSpecUse> reportSpecUse;
	
	
	
	/*
	 * replacing with onetomany configAnswer
	 * Select HH where answer matches answer for question 
	 * 
	@ManyToMany
	@JoinTable(name = "HouseholdInclusionRule")
	@NewAction("")
	
	@ListProperties("prompt,level,answerType")
	private Collection<ConfigQuestion> configQuestion;
	
	*/
	
	@OneToMany(mappedBy = "customReportSpec")
	@NewAction("")
	//@AddAction("CustomReportSpec.add") 
	@CollectionView("fromCRS")
	@SearchListCondition(value="${configQuestionUse.configQuestion.level} = 1")  // Household q and a only
	@ListProperties("configQuestionUse.configQuestion.prompt,answer")
	private Collection<ConfigAnswer> configAnswer;

	

	// ----------------------------------------------------------------------------------------------//
	// Is quantile total above or not equal to 100%
	// ----------------------------------------------------------------------------------------------//

	@Transient
	@ReadOnly
	@Column(length = 3)


	public int getTotalQuantilePercentage() {
		int result = 0;

		for (Quantile quantile : getQuantile()) {
			result += quantile.getPercentage();
		}
		return result;
	}

	
	@Depends("totalQuantilePercentage")
	@Stereotype("LABEL")
	@Transient
	@ReadOnly
	public String getWarningMessage() {
		if (getTotalQuantilePercentage() > 100) {

			return "<font color=" + "red" + ">Total Quantile Percent is greater than 100</font>";

		} else if (getTotalQuantilePercentage() < 100) {

			return "<font color=" + "orange" + ">Total Quantile Percent is less than 100</font>";
		} else if (getTotalQuantilePercentage() == 100) {
			return "<font color=" + "green" + ">Total Quantile Percent is 100</font>";
		}
		return null;
	}
	


	public Collection<Report> getReport() {
		return report;
	}

	public void setReport(Collection<Report> report) {
		this.report = report;
	}

	public Collection<ResourceType> getResourceType() {
		return resourceType;
	}

	public void setResourceType(Collection<ResourceType> resourceType) {
		this.resourceType = resourceType;
	}

	public Collection<ResourceSubType> getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(Collection<ResourceSubType> resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

	public Collection<Category> getCategory() {
		return category;
	}

	public void setCategory(Collection<Category> category) {

		this.category = category;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}



	public Collection<Quantile> getQuantile() {
		return quantile;
	}

	public void setQuantile(Collection<Quantile> quantile) {
		this.quantile = quantile;
	}


	public Collection<ConfigAnswer> getConfigAnswer() {
		return configAnswer;
	}


	public void setConfigAnswer(Collection<ConfigAnswer> configAnswer) {
		this.configAnswer = configAnswer;
	}




}
