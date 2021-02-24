package efd.model;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
/*
 * DRB 12/6/2020 List of Micronutrients from McCane list
 * 
 * 
 */
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.OnDelete;
import org.openxava.annotations.DefaultValueCalculator;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.NoCreate;
import org.openxava.annotations.NoModify;
import org.openxava.annotations.ReadOnly;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;
import org.openxava.calculators.ZeroBigDecimalCalculator;
import org.openxava.model.Identifiable;

@View(members="name;rda;microNutrientLevel;rdaUnit")
@View(name="frommnl",members="name,rda;rdaUnit")

@Tab(properties="name,microNutrientLevel.mnLevel,rda,rdaUnit", defaultOrder="${name}" )

@Entity
public class MicroNutrient{

	
	
	@Id
	@Column(length = 45)
	
	private String name;
	
	
	@DefaultValueCalculator( ZeroBigDecimalCalculator.class)
	@PositiveOrZero
	private double rda;
	
	@Column(length = 25)
	private String rdaUnit;
	

	@OneToMany(mappedBy = "microNutrient")

	@ListProperties("mccwFoodSource.foodCode,mccwFoodSource.foodSourceName, mnLevel,microNutrient.rda,microNutrient.rdaUnit")
	
	private Collection<MicroNutrientLevel> microNutrientLevel;
	
	
	



	public Collection<MicroNutrientLevel> getMicroNutrientLevel() {
		return microNutrientLevel;
	}

	public void setMicroNutrientLevel(Collection<MicroNutrientLevel> microNutrientLevel) {
		this.microNutrientLevel = microNutrientLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public double getRda() {
		return rda;
	}

	public void setRda(double rda) {
		this.rda = rda;
	}

	public String getRdaUnit() {
		return rdaUnit;
	}

	public void setRdaUnit(String rdaUnit) {
		this.rdaUnit = rdaUnit;
	}

	

	
	
	
}
