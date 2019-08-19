package efd.validations;

import javax.inject.*;

import org.openxava.actions.*;

public class OnChangeSiteCountry extends OnChangePropertyBaseAction {

	@Inject // Set on init OIHM or OHEA - Country or LZ if OIHM
	private String efdModel;

	Boolean country = null;
	Boolean lz = null;
	Boolean district = null;

	public void execute() throws Exception {

		System.out.println("in on change site country model = " + efdModel);

		if (getView().getValue("country.description") == null && getView().getValue("livelihoodZone.lzname") == null
				&& getView().getValue("locationdistrict") == null) {
			return;
		}

		if (efdModel == "OIHM") {

			if (getView().getValue("country.description") == null
					|| getView().getValue("country.description").toString().isEmpty()) {

				country = false;
			} else
				country = true;

			if (getView().getValue("livelihoodZone.lzname") == null
					| getView().getValue("livelihoodZone.lzname").toString().isEmpty()) {

				lz = false;
			} else
				lz = true;

			if (getView().getValue("locationdistrict") == null) {
				district = false;
			} else {
				district = true;
			}

			if (!country && !lz && !district) // all empty - its new
			{
				return;
			}

			if (!country && !lz) // both empty
			{
				addError("Country or Livelihood Zone must be populated");
				getView().refresh();
			}

			else if (country && lz) // both populated
			{
				addError("Country and Livelihood Zone cannnot both be populated");
				getView().refresh();
			}

		}
	}

}
