
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

		Map allValues2 = getView().getAllValues();
		
		Object wgid = null;
		try {
			wgid = getView().getSubview("wealthgroup").getValue("wgid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (wgid == null) {
			return;
		}
		System.out.println("in LandUnitAction 33");
		Area areaMeasurement = null;
		try {
			WealthGroup wgi = XPersistence.getManager().find(WealthGroup.class, wgid.toString());
			areaMeasurement = wgi.getCommunity().getProjectlz().getAreaMeasurement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<AssetLand> collectionObjects = getView().getSubview("assetLand").getCollectionObjects();

		for (AssetLand al : collectionObjects) {
			al.setUnit(areaMeasurement.toString());
			
		}
		System.out.println("in LandUnitAction 44");
		//getView().findObject();
	
		System.out.println("done LandUnitAction to set Units");
		
		// getView().getParent().getSubview("assetLand").setValue("assetLand.unit",
		// areaMeasurement.toString());
		// getView().getParent().getSubview("assetLand").setEditable("assetLand.unit",
		// false);

	}
}
