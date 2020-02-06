package efd.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.util.*;

import efd.validations.*;

@View(members = "ExpandabilityRule[#ruleName];Ruleset[studyRuleSet;communityRuleSet];ResourceSubType[appliedResourceSubType;sequence;expandabilityIncreaseLimit;expandabilityLimit]")

@Tab(properties = "ruleName,studyRuleSet.studyName,communityRuleSet.site.locationdistrict,appliedResourceSubType.resourcetypename,sequence,expandabilityIncreaseLimit,expandabilityLimit")

@Entity

@Table(name = "ExpandabilityRule")
public class ExpandabilityRule extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		if ((communityRuleSet == null && studyRuleSet == null) || (communityRuleSet != null && studyRuleSet != null)) {

			throw new IllegalStateException(

					XavaResources.getString("Expandability Rule must be associated with a Study or a Community"));

		}
	}

	/*************************************************************************************************/
	@Required
	@Column(length = 45, nullable = false, unique = true)
	private String ruleName;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "studyName,referenceYear")
	private Study studyRuleSet;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@NoCreate
	@NoModify
	@DescriptionsList(descriptionProperties = "site.locationdistrict,site.subdistrict")
	private Community communityRuleSet;
	/*************************************************************************************************/
	@Required
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	@Positive
	private int sequence;
	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoCreate
	@NoModify
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename, resourcetypename", condition = "${resourcetype.resourcetypename} != 'Cash'")
	@OnChange(OnChangeRSTExpandability.class)
	private ResourceSubType appliedResourceSubType;
	/*************************************************************************************************/
	@PositiveOrZero
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private int expandabilityIncreaseLimit;
	/*************************************************************************************************/
	@PositiveOrZero
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	//@DefaultValueCalculator(value = IntegerCalculator.class, properties = @PropertyValue(name = "value", value = "100"))
	private int expandabilityLimit;
	/*************************************************************************************************/
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Study getStudyRuleSet() {
		return studyRuleSet;
	}

	public void setStudyRuleSet(Study studyRuleSet) {
		this.studyRuleSet = studyRuleSet;
	}

	public Community getCommunityRuleSet() {
		return communityRuleSet;
	}

	public void setCommunityRuleSet(Community communityRuleSet) {
		this.communityRuleSet = communityRuleSet;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public ResourceSubType getAppliedResourceSubType() {
		return appliedResourceSubType;
	}

	public void setAppliedResourceSubType(ResourceSubType appliedResourceSubType) {
		this.appliedResourceSubType = appliedResourceSubType;
	}

	public int getExpandabilityIncreaseLimit() {
		return expandabilityIncreaseLimit;
	}

	public void setExpandabilityIncreaseLimit(int expandabilityIncreaseLimit) {
		this.expandabilityIncreaseLimit = expandabilityIncreaseLimit;
	}

	public int getExpandabilityLimit() {
		return expandabilityLimit;
	}

	public void setExpandabilityLimit(int expandabilityLimit) {
		this.expandabilityLimit = expandabilityLimit;
	}

}