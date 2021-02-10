package efd.rest.domain.model;

import efd.validations.ZeroDoubleCalculator;
import org.openxava.annotations.*;
import org.openxava.model.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.Positive;



@View(members = "#name;multipleOfStandardMeasure;resourceSubType;livelihoodZone")

@Tab(properties = "name,livelihoodZone.lzname,resourceSubType.resourcetype.resourcetypename,"
		+ "resourceSubType.resourcetypename,multipleOfStandardMeasure,resourceSubType.resourcesubtypeunit")



//@Table(
//uniqueConstraints = {
//		@UniqueConstraint(name = "unique_localunit", columnNames = { "name","resourceSubType_IDResourceSubType","livelihoodZone_LZID" })
//		})



@Entity
public class LocalUnit extends Identifiable {

	@Required
	@Column(length = 45, nullable = false)
	private String name;

	@DefaultValueCalculator(ZeroDoubleCalculator.class)
	@Positive
	private Double multipleOfStandardMeasure;

	@Required
	@ManyToOne
	@NoCreate
	@ReferenceView("FromLocalUnit")
	private LivelihoodZone livelihoodZone;

	@Required
	@ManyToOne
	@NoCreate
	@ReferenceView("FromLocalUnit")
	@SearchListCondition(value = "${resourcetype.resourcetypename} in ('Wild Foods', 'Crops', 'Livestock Products', 'Food Purchase','Land','Non Food Purchase','Other Tradeable Goods') ")
	private ResourceSubType resourceSubType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Double getMultipleOfStandardMeasure() {
		return multipleOfStandardMeasure;
	}

	public void setMultipleOfStandardMeasure(Double multipleOfStandardMeasure) {
		this.multipleOfStandardMeasure = multipleOfStandardMeasure;
	}

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public ResourceSubType getResourceSubType() {
		return resourceSubType;
	}

	public void setResourceSubType(ResourceSubType resourceSubType) {
		this.resourceSubType = resourceSubType;
	}

}
