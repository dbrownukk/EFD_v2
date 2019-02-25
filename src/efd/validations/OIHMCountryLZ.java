package efd.validations;

import javax.inject.*;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import efd.model.*;

public class OIHMCountryLZ implements IValidator {

	@Inject
	private String efdModel;

	private Country country;
	private LivelihoodZone livelihoodZone;
	private String model;

	@Override
	public void validate(Messages errors) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("in ent validator country lz country = ");

		// System.out.println("in ent validator efdModel = " + getModel());

		if (getModel() == "OIHM") // need to ensure LZ and Country not populated
		{
			if (country == null && livelihoodZone == null) {
				System.out.println("country and LZ null null ");
				errors.add("Country and Livelihoodzone cannot both be empty");

			}

			if ((country != null) && (livelihoodZone != null)) {
				System.out.println("both are populated - country and lz");
				errors.add("Country and Livelihoodzone cannot both be populated");
			}

		} else if (getModel() == "OHEA")
			return;

	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
