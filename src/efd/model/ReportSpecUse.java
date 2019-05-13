package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;


@Entity


//@View(members = "study,customReportSpec,Household")

public class ReportSpecUse extends Identifiable {
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@DescriptionsList(descriptionProperties = "specName")
	private CustomReportSpec customReportSpec;
	
	

	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoFrame
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	@ReferenceView("FromStdOfLiving")   // study name and ref year
	private Study study;

	@ManyToMany
	@Required
	@NewAction("")
	@JoinTable(name = "reportSpecHHInclusion")
	private Collection<Household> Household;
	

	@ManyToOne
	@Required
	

	
	@NoModify
	//@DescriptionsList(descriptionProperties = "configQuestion.prompt,configAnswer")
	@ReferenceView("fromRSU")
	private ConfigQuestionUse configQuestionUse;


	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}


	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}


	public Study getStudy() {
		return study;
	}


	public void setStudy(Study study) {
		this.study = study;
	}


	public Collection<Household> getHousehold() {
		return Household;
	}


	public void setHousehold(Collection<Household> household) {
		Household = household;
	}


	public ConfigQuestionUse getConfigQuestionUse() {
		return configQuestionUse;
	}


	public void setConfigQuestionUse(ConfigQuestionUse configQuestionUse) {
		this.configQuestionUse = configQuestionUse;
	}
	
	
	
	




	
	
	
	
	
}
