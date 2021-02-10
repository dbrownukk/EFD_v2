package efd.rest.domain.model;

import efd.validations.OnChangeRSTExpandability;
import org.openxava.annotations.*;
import org.openxava.calculators.ZeroIntegerCalculator;
import org.openxava.calculators.ZeroLongCalculator;
import org.openxava.jpa.XPersistence;
import org.openxava.util.XavaResources;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.stream.Collectors;

@View(members = "ExpandabilityRule[#ruleName];Ruleset[studyRuleSet;communityRuleSet];ResourceSubType[appliedResourceSubType;sequence;expandabilityIncreaseLimit;expandabilityLimit]")
@View(name = "fromCommunity", members = "ExpandabilityRule[#ruleName];ResourceSubType[#appliedResourceSubType;sequence,averageUnitsProduced;expandabilityIncreaseLimit,averageUnitsSold;expandabilityLimit,averagePerCentConsumedRepresents],modelType")
@View(name = "fromStudy", members = "ExpandabilityRule[#ruleName];ResourceSubType[#appliedResourceSubType;sequence,averageUnitsProduced;expandabilityIncreaseLimit,averageUnitsSold;expandabilityLimit,averagePerCentConsumedRepresents,modelType]")

@Tab(properties = "ruleName,studyRuleSet.studyName,communityRuleSet.site.locationdistrict,communityRuleSet.site.subdistrict,appliedResourceSubType.resourcetypename,sequence,expandabilityIncreaseLimit,expandabilityLimit", defaultOrder = "sequence")

@Entity

//@Table(name = "ExpandabilityRule", uniqueConstraints = {
//		@UniqueConstraint(name = "expandabilityRuleStudy", columnNames = { "ruleName", "studyRuleSet_ID" }),
//		@UniqueConstraint(name = "expandabilityRuleCommunity", columnNames = { "ruleName", "communityRuleSet_CID" }),
//		@UniqueConstraint(name = "expandabilitySequenceStudy", columnNames = { "studyRuleSet_ID", "sequence" }),
//		@UniqueConstraint(name = "expandabilitySequenceCommunity", columnNames = { "communityRuleSet_CID",
//				"sequence" }) })
public class ExpandabilityRule extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		int duplicateCount= 0;
		
	
		
		if ((communityRuleSet == null && studyRuleSet == null) || (communityRuleSet != null && studyRuleSet != null)) {

			throw new IllegalStateException(

					XavaResources.getString("Expandability Rule must be associated with a Study or a Community"));

		}
		Collection<ExpandabilityRule> expandabilityRule = null;
		/* Only allow a synonym if from another ResourceType */
		if (getCommunityRuleSet() != null) {
			Community community = XPersistence.getManager().find(Community.class,
					getCommunityRuleSet().getCommunityid());
			expandabilityRule = community.getExpandabilityRule();

		}

		if (getStudyRuleSet() != null) {
			Study study = XPersistence.getManager().find(Study.class, getStudyRuleSet().getId());
			expandabilityRule = study.getExpandabilityRule();
		}

		if (getAppliedResourceSubType().getResourcesubtypesynonym() != null) {
			/* Does this match any other rst syns in this exprule */

			for (ExpandabilityRule expRule : expandabilityRule) {
				System.out.println("in syn check ");
				System.out.println("new rst = " + getAppliedResourceSubType().getResourcesubtypesynonym());
				System.out.println("exp rst = " + expRule.getAppliedResourceSubType());
				if (getAppliedResourceSubType().getResourcesubtypesynonym() == expRule.getAppliedResourceSubType()) {
					throw new IllegalStateException(

							XavaResources.getString(
									"Expandability Rule with Resource Subtype synonym must be from different Resource Type "
											+ getAppliedResourceSubType().getResourcesubtypesynonym().getResourcetypename().toString()));

				}
			}

		} else {
			
			System.out.println("for duplicate check this.rst = "+this.getAppliedResourceSubType().getResourcetypename());
			if(this.communityRuleSet != null) {
				// check for duplicates in Community rule set
				
				duplicateCount = (int) this.getCommunityRuleSet().getExpandabilityRule().stream().filter(p -> p.getAppliedResourceSubType() == this.getAppliedResourceSubType()).count();
			}
			else if(this.studyRuleSet!=null)
			{
				
				System.out.println("in stuy rule set ");
				for (ExpandabilityRule exprule : this.getStudyRuleSet().getExpandabilityRule().stream().collect(Collectors.toList())) {
					System.out.println("exprule = "+exprule.getAppliedResourceSubType().getResourcetypename());
				}
				
				duplicateCount = (int) this.getStudyRuleSet().getExpandabilityRule().stream().filter(p -> p.getAppliedResourceSubType() == this.getAppliedResourceSubType()).count();
			}
			if (duplicateCount>1 ) {
				throw new IllegalStateException(

						XavaResources.getString(
								"Expandability Rule duplicate Resource Subtype "
										+ getAppliedResourceSubType().getResourcetypename().toString()));

			}
			


		}

	}

	/*************************************************************************************************/
	@Required
	@Column(length = 45, nullable = false)
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
	@DescriptionsList(descriptionProperties = "projectlz.projecttitle,site.locationdistrict,site.subdistrict")
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

	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename, resourcetypename", condition = "${resourcetype.resourcetypename}"
			+ " not in  ('Cash','Livestock Sales') ")
	@OnChange(OnChangeRSTExpandability.class)

	private ResourceSubType appliedResourceSubType;
	/*************************************************************************************************/
	@PositiveOrZero
//	@ReadOnly
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private int expandabilityIncreaseLimit;
	/*************************************************************************************************/
	@PositiveOrZero
	// @ReadOnly
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	// @DefaultValueCalculator(value = IntegerCalculator.class, properties =
	// @PropertyValue(name = "value", value = "100"))
	private int expandabilityLimit;
	/*************************************************************************************************/

	@Transient
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double expandabilityDI;
	@Transient
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double expandabilityCostOfDeficitPurchase;
	@Transient
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	private Double expandabilityGain;
	@DefaultValueCalculator(value = ZeroLongCalculator.class)
	@Transient
	private Double expandabilityTotalGain;

	/*
	 * #450 Average units produced. Average units Sold Average % of food
	 * requirements the Units Consumed represents.
	 * 
	 * Note - uses OnChangeRSTExpandability and ExpandabilityRuleEdit/New
	 * 
	 */

	/*
	 * TODO
	 * 
	 * Study and 3rd average
	 * 
	 *
	 */

	@Transient
	// @Hidden
	public String getParentId() {// community or study id
		if (getCommunityRuleSet() == null) {
			return (getStudyRuleSet().getId());
		} else {
			return (getCommunityRuleSet().getCommunityid());
		}

	}

	@Transient
	@ReadOnly
	@Depends("appliedResourceSubType")
	private Double averageUnitsProduced;

	@Transient
	@ReadOnly
	@Depends("appliedResourceSubType")

	private Double averageUnitsSold;

	@Transient
	@ReadOnly
	@Depends("appliedResourceSubType")
	private Double averagePerCentConsumedRepresents;

	@Hidden
	@Transient
	private String modelType; // OIHM or OHEA

	public Double getAverageUnitsProduced() {
		return averageUnitsProduced;
	}

	public void setAverageUnitsProduced(Double averageUnitsProduced) {
		this.averageUnitsProduced = averageUnitsProduced;
	}

	public Double getAverageUnitsSold() {
		return averageUnitsSold;
	}

	public void setAverageUnitsSold(Double averageUnitsSold) {
		this.averageUnitsSold = averageUnitsSold;
	}

	public Double getAveragePerCentConsumedRepresents() {
		return averagePerCentConsumedRepresents;
	}

	public void setAveragePerCentConsumedRepresents(Double averagePerCentConsumedRepresents) {
		this.averagePerCentConsumedRepresents = averagePerCentConsumedRepresents;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public Double getExpandabilityTotalGain() {
		return expandabilityTotalGain == null ? 0.0 : expandabilityTotalGain;
	}

	public void setExpandabilityTotalGain(Double expandabilityTotalGain) {
		this.expandabilityTotalGain = expandabilityTotalGain;
	}

	public Double getExpandabilityGain() {
		return expandabilityGain == null ? 0.0 : expandabilityGain;
	}

	public void setExpandabilityGain(Double expandabilityGain) {
		this.expandabilityGain = expandabilityGain;
	}

	public Double getExpandabilityCostOfDeficitPurchase() {
		return expandabilityCostOfDeficitPurchase == null ? 0.0 : expandabilityCostOfDeficitPurchase;
	}

	public void setExpandabilityCostOfDeficitPurchase(Double expandabilityCostOfDeficitPurchase) {
		this.expandabilityCostOfDeficitPurchase = expandabilityCostOfDeficitPurchase;
	}

	public Double getExpandabilityDI() {
		return expandabilityDI == null ? 0.0 : expandabilityDI;
	}

	public void setExpandabilityDI(Double expandabilityDI) {
		this.expandabilityDI = expandabilityDI;
	}

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