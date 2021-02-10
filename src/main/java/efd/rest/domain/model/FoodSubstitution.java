/* DRB Aug 2020
 * Hold alternative food resource when running Modelling reports to compare Nutrients
 */

package efd.rest.domain.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.util.XavaResources;

@View(members = "FoodSubstitution[currentFood, substitutionFood]")

@Entity
@Table(name = "FoodSubstitution")
@Tab(properties = "currentFood.resourcetypename,substitutionFood.resourcetypename")

public class FoodSubstitution extends EFDIdentifiable {

	@PrePersist
	@PreUpdate
	private void validate() throws Exception {

		if (getCurrentFood() == getSubstitutionFood()) {
			throw new IllegalStateException(

					XavaResources.getString("Current Food and Substitution Food must be different"));

		}

		for (PriceYieldVariation priceYieldVariation2 : getModellingScenario().getPriceYieldVariations()) {
			if (priceYieldVariation2.getResource() == getCurrentFood()
					|| priceYieldVariation2.getResource() == getSubstitutionFood()) {
				throw new IllegalStateException(

						XavaResources.getString(
								"Food Resource cannot be in Price / Yield Variation and Food Substitution list"));
			}
		}

	}

	/*************************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoCreate
	@NoModify
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename", condition = "${resourcetype.resourcetypename} in  ('Food Stocks','Crops','Wild Foods','Food Purchase','Livestock Products')")
	private ResourceSubType currentFood;
	/*************************************************************************************************/

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@NoCreate
	@NoModify
	@Required
	@DescriptionsList(descriptionProperties = "resourcetype.resourcetypename,resourcetypename", condition = "${resourcetype.resourcetypename} in  ('Food Stocks','Crops','Wild Foods','Food Purchase','Livestock Products')")
	private ResourceSubType substitutionFood;
	/*************************************************************************************************/

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@DescriptionsList(descriptionProperties = "title,author,date")
	private ModellingScenario modellingScenario;

	/*************************************************************************************************/

	public ResourceSubType getCurrentFood() {
		return currentFood;
	}

	public ModellingScenario getModellingScenario() {
		return modellingScenario;
	}

	public void setModellingScenario(ModellingScenario modellingScenario) {
		this.modellingScenario = modellingScenario;
	}

	public void setCurrentFood(ResourceSubType currentFood) {
		this.currentFood = currentFood;
	}

	public ResourceSubType getSubstitutionFood() {
		return substitutionFood;
	}

	public void setSubstitutionFood(ResourceSubType substitutionFood) {
		this.substitutionFood = substitutionFood;
	}

	/*************************************************************************************************/

}