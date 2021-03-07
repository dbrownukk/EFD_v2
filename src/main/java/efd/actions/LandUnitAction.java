
/* In Project module override standard add LZ to remove LZs already associated with Project */

package efd.actions;

import java.util.*;

import javax.persistence.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.view.*;
import efd.model.*;
import efd.model.Project.*;

public class LandUnitAction extends ViewBaseAction {

	public void execute() throws Exception {

		System.out.println("in LandUnitAction");


		Object wgid = null;
		try {
			wgid = getView().getSubview("wealthgroup").getValue("wgid");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (wgid == null) {
			return;
		}

		Area areaMeasurement = null;
		try {
			WealthGroup wgi = XPersistence.getManager().find(WealthGroup.class, wgid.toString());
			areaMeasurement = wgi.getCommunity().getProjectlz().getAreaMeasurement();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (areaMeasurement == null) { // If not set in Project during testing
			areaMeasurement = Area.Acre;
		}

		List<AssetLand> collectionObjects = getView().getSubview("assetLand").getCollectionObjects();

		for (AssetLand al : collectionObjects) {
			al.setUnit(areaMeasurement.toString());

		}

	}
}
