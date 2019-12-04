package efd.validations;

/* set assetLand to be Project Unit */ 

import java.util.*;

import org.openxava.actions.*;
import org.openxava.jpa.*;
import org.openxava.tab.impl.*;
import org.openxava.view.*;

import efd.model.*;
import efd.model.Project.*;

public class OnChangeUnit extends OnChangePropertyBaseAction {

	public void execute() throws Exception {

		String title = getView().getMemberName(); // returns tab name
		
		//Map allValues2 = getView().getAllValues();
		//System.out.println("all vals 2 = "+allValues2.toString());
		

		if (title.equals("assetLand")) { // check if unit area measurement is set. If not set to Project Area
											// measurement

			//getView().addValidValue("unit", "Acre", "Acre");
			//getView().addValidValue("unit", "Hectare", "Hectare");
			Object wgid = null;
			try {
				Map allValues = getView().getParent().getSubview("wealthgroup").getAllValues();
				System.out.println("all vals = "+allValues.toString());
				 wgid = getView().getParent().getSubview("wealthgroup").getValue("wgid");
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//String unit = getView().getValueString("unit");
			
			System.out.println("in set unit project measure = "+areaMeasurement.toString());
			
			
			List<View> landSections = getView().getSectionView(0).getSections();   //setValue("assetLand.unit", areaMeasurement.toString());
			landSections.get(0).setValue("unit", areaMeasurement.toString());
			
			
			getView().setValue("unit", areaMeasurement.toString());
			
			//getView().setEditable("unit", false);
			


		}

	}

}