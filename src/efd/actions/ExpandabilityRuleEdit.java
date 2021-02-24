/* DRB 29/10/2020


 * Create edit Expandability Rule, determine if for OIHM or OHEA and save in a transient
 * 
 * Contains calc averages - also used in OnChange RST in ExpandabilityRule
 * 
 * DRB 21/11/2020 Average is based on all WGs or HHs not just those with a foodstuff
 * 
 */

package efd.actions;

import java.util.Collection;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Stream;

import org.apache.poi.util.Units;
import org.openxava.actions.EditElementInCollectionAction;
import org.openxava.jpa.XPersistence;

import efd.actions.ParseHHSpreadsheet.QandA;
import efd.model.Community;
import efd.model.Crop;
import efd.model.Employment;
import efd.model.Household;
import efd.model.LivestockProducts;
import efd.model.LivestockSales;
import efd.model.ResourceSubType;
import efd.model.Study;
import efd.model.Transfer;
import efd.model.WealthGroup;
import efd.model.WealthGroupInterview;
import efd.model.WealthGroupInterview.Status;
import efd.model.WildFood;

public class ExpandabilityRuleEdit extends EditElementInCollectionAction {
	static int PRODUCED = 0;
	static int SOLD = 1;
	static int AVGOFPERCENTCONSUMED = 2;
	static int TOTAL = 3;

	public void execute() throws Exception {

		Object id = "";
		Community community = null;
		Study study = null;

		double[] averages = { 0.0, 0.0, 0.0 };

		Map<?, ?> rstMap = null;
		String rstString = "";
		ResourceSubType rst = null;

		System.out.println("Edit Expandability Rule");

		super.execute();

		rstMap = getCollectionElementView().getAllValues();
		rstString = getCollectionElementView().getValueString("appliedResourceSubType.idresourcesubtype");

		System.out.println("rstall = " + rstString.toString());

		rst = XPersistence.getManager().find(ResourceSubType.class, rstString);

		System.out.println("rst now = " + rst.getResourcetypename());

		final ResourceSubType strRST = rst;

		
		
		if ((id = getPreviousView().getValue("communityid")) != null) {

			System.out.println("EXPandability -for OHEA");
			community = XPersistence.getManager().find(Community.class, id.toString());
			System.out.println("Community = " + community.getSite().getSubdistrict());

			getCollectionElementView().setValue("modelType", "OHEA");
			getCollectionElementView().setHidden("modelType", true);

			averages = calcAverages(community, strRST);

		} else if ((id = getPreviousView().getValue("id")) != null) {
			System.out.println("EXPandability -for OIHM");
			study = XPersistence.getManager().find(Study.class, id.toString());
			System.out.println("Study  = " + study.getStudyName());
			// getView().setValue("modelType", "OIHM");
			getCollectionElementView().setValue("modelType", "OIHM");
			getCollectionElementView().setHidden("modelType", true);

			averages = calcAverages(study, strRST);
		}
		// System.out.println("set parentID " + id.toString());
		//getCollectionElementView().setValue("parentId", id.toString());
		//getCollectionElementView().setHidden("parentId", true);

		getCollectionElementView().setValue("averageUnitsProduced", averages[0]);
		getCollectionElementView().setValue("averageUnitsSold", averages[1]);
		getCollectionElementView().setValue("averagePerCentConsumedRepresents", averages[2]);

	}

	public static double[] calcAverages(Community community, final ResourceSubType strRST) {

		boolean isOHEA = true;
		double[] sumUnits = { 0.0, 0.0, 0.0, 0.0 };

		int count[] = { 0, 0, 0, 0 };

		double[] wgiAverages = { 0.0, 0.0, 0.0, 0.0 };

		/* for requirements need totally KCAL income */

		
		
		int countForAverages = 0;
		for (WealthGroup wg : community.getWealthgroup()) {
			countForAverages += wg.getWealthGroupInterview().stream()
			.filter(p -> p.getStatus() == Status.Validated).count();
			}
		
		
		
		
		for (WealthGroup wg : community.getWealthgroup()) {
			

			for (WealthGroupInterview wgi : wg.getWealthGroupInterview()) {

				if (wgi.getStatus() != Status.Validated) {
					continue;
				}

				/* Only 1 of these will apply from LSS/WF/Crop etc, */
				OptionalDouble[] units = { OptionalDouble.empty(), OptionalDouble.empty(), OptionalDouble.empty(),
						OptionalDouble.empty() };
				Collection<Crop> crop = wgi.getCrop();
				Collection<WildFood> wildFood = wgi.getWildFood();
				Collection<LivestockSales> livestockSales = wgi.getLivestockSales();
				Collection<LivestockProducts> livestockProducts = wgi.getLivestockProducts();
				Collection<Transfer> transfer = wgi.getTransfer();
				Collection<Employment> employment = wgi.getEmployment();

				units = calcAvgUnits(strRST, crop, wildFood, livestockSales, livestockProducts, transfer, employment,
						isOHEA);

				if (units[PRODUCED].isPresent()) {
					

					count[PRODUCED]++;
					sumUnits[PRODUCED] += units[PRODUCED].getAsDouble();
				}
				if (units[SOLD].isPresent()) {
				

					count[SOLD]++;
					sumUnits[SOLD] += units[SOLD].getAsDouble();
				}
				if (units[AVGOFPERCENTCONSUMED].isPresent()) {
					

					count[AVGOFPERCENTCONSUMED]++;
					sumUnits[AVGOFPERCENTCONSUMED] += units[AVGOFPERCENTCONSUMED].getAsDouble();
				}
				if (units[TOTAL].isPresent()) {
					

					count[TOTAL]++;
					sumUnits[TOTAL] += units[TOTAL].getAsDouble();
					System.out.println("sumunits total KCAL = " + sumUnits[TOTAL]);
				}

			}
		}

		for (int i = 0; i < 2; i++) {
			if (count[i] > 0) {
				wgiAverages[i] = sumUnits[i] / count[i];
				wgiAverages[i] = sumUnits[i] / countForAverages;
			} else {
				wgiAverages[i] = 0;
			}

		}

		/* % consumed of this RST of total kcal consumed */
		wgiAverages[AVGOFPERCENTCONSUMED] = sumUnits[AVGOFPERCENTCONSUMED] / sumUnits[TOTAL] *100;

		return wgiAverages;

	}

	public static double[] calcAverages(Study study, final ResourceSubType strRST) {

		boolean isOHEA = false;
		double[] sumUnits = { 0.0, 0.0, 0.0, 0.0 };

		int[] count = { 0, 0, 0, 0 };
		double[] hhAverages = { 0.0, 0.0, 0.0, 0.0 };

		int countForAverages = 0;
	
		countForAverages = (int) study.getHousehold().stream().filter(p -> p.getStatus() == Status.Validated).count();
		
		
		


		for (Household hh : study.getHousehold()) {
		

			if (hh.getStatus() != Status.Validated) {
				continue;
			}

			OptionalDouble[] units = { OptionalDouble.empty(), OptionalDouble.empty(), OptionalDouble.empty(),
					OptionalDouble.empty() };

			Collection<Crop> crop = hh.getCrop();
			Collection<WildFood> wildFood = hh.getWildFood();
			Collection<LivestockSales> livestockSales = hh.getLivestockSales();
			Collection<LivestockProducts> livestockProducts = hh.getLivestockProducts();
			Collection<Transfer> transfer = hh.getTransfer();
			Collection<Employment> employment = hh.getEmployment();

			units = calcAvgUnits(strRST, crop, wildFood, livestockSales, livestockProducts, transfer, employment,
					isOHEA);

			
	

			if (units[PRODUCED].isPresent()) {
				
				count[PRODUCED]++;
				sumUnits[PRODUCED] += units[PRODUCED].getAsDouble();

			}
			if (units[SOLD].isPresent()) {
				
				count[SOLD]++;
				sumUnits[SOLD] += units[SOLD].getAsDouble();
			}
			if (units[AVGOFPERCENTCONSUMED].isPresent()) {
			
				count[AVGOFPERCENTCONSUMED]++;
				sumUnits[AVGOFPERCENTCONSUMED] += units[AVGOFPERCENTCONSUMED].getAsDouble();
				
			}
			if (units[TOTAL].isPresent()) {
			
				count[TOTAL]++;
				sumUnits[TOTAL] += units[TOTAL].getAsDouble();
				
			}

		}

		for (int i = 0; i < 2; i++) {
			if (count[i] > 0) {
				// hhAverages[i] = sumUnits[i] / count[i];
				hhAverages[i] = sumUnits[i] / countForAverages;
			} else {
				hhAverages[i] = 0;
			}

		}

		
		hhAverages[AVGOFPERCENTCONSUMED] = sumUnits[AVGOFPERCENTCONSUMED] / sumUnits[TOTAL] * 100;

		
		return hhAverages;

	}

	private static OptionalDouble[] calcAvgUnits(final ResourceSubType strRST, Collection<Crop> crop,
			Collection<WildFood> wildFood, Collection<LivestockSales> livestockSales,
			Collection<LivestockProducts> livestockProducts, Collection<Transfer> transfer,
			Collection<Employment> employment, boolean isOHEA) {

		OptionalDouble consumed;
		String type = "food";
		OptionalDouble[] units = { OptionalDouble.empty(), OptionalDouble.empty(), OptionalDouble.empty(),
				OptionalDouble.empty() };

		/* Need total KCal for this WGI/HH */

		/* Get total Food income and use to find Average percent consumed represents */
		Double totalKcal = ModellingReports.calcCropIncome(crop, type, false, false, isOHEA)
				+ ModellingReports.calcLSP(livestockProducts, type, false, false, isOHEA)
				+ ModellingReports.calcLSS(livestockSales, type, isOHEA)
				+ ModellingReports.calcTransIncome(transfer, type, false, false, isOHEA)
				+ ModellingReports.calcWFIncome(wildFood, type, false, false, isOHEA)
				+ ModellingReports.calcEmpIncome(employment, type, false, false, isOHEA);

		units[TOTAL] = OptionalDouble.of(totalKcal);

		switch (strRST.getResourcetype().getResourcetypename()) {
		case "Crops":
			

			units[PRODUCED] = crop.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsProduced() > 0).mapToDouble(p -> p.getUnitsProduced()).average();
			units[SOLD] = crop.stream().filter(p -> p.getResourceSubType() == strRST).filter(p -> p.getUnitsSold() > 0)
					.mapToDouble(p -> p.getUnitsSold()).average();

			consumed = crop.stream().filter(p -> p.getResourceSubType() == strRST)
					.mapToDouble(p -> p.getUnitsConsumed()).average();
			if (consumed.isPresent()) {
				units[AVGOFPERCENTCONSUMED] = OptionalDouble
						.of(ModellingReports.findRSTKcal(strRST) * consumed.getAsDouble());
			}
			break;
		case "Wild Foods":
			

			units[PRODUCED] = wildFood.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsProduced() > 0).mapToDouble(p -> p.getUnitsProduced()).average();
			units[SOLD] = wildFood.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsSold() > 0).mapToDouble(p -> p.getUnitsSold()).average();
			consumed = wildFood.stream().filter(p -> p.getResourceSubType() == strRST)
					.mapToDouble(p -> p.getUnitsConsumed()).average();
			if (consumed.isPresent()) {
				units[AVGOFPERCENTCONSUMED] = OptionalDouble
						.of(ModellingReports.findRSTKcal(strRST) * consumed.getAsDouble());
			}
			break;
		case "Livestock Sales":
			units[PRODUCED] = livestockSales.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsAtStartofYear() > 0).mapToDouble(p -> p.getUnitsAtStartofYear()).average();
			units[SOLD] = livestockSales.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsSold() > 0).mapToDouble(p -> p.getUnitsSold()).average();

			/* Note no food consumed from a LSS */

			break;
		case "Livestock Products":
			units[PRODUCED] = livestockProducts.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsProduced() > 0).mapToDouble(p -> p.getUnitsProduced()).average();
			units[SOLD] = livestockProducts.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsSold() > 0).mapToDouble(p -> p.getUnitsSold()).average();
			consumed = livestockProducts.stream().filter(p -> p.getResourceSubType() == strRST)
					.mapToDouble(p -> p.getUnitsConsumed()).average();
			if (consumed.isPresent()) {
				units[AVGOFPERCENTCONSUMED] = OptionalDouble
						.of(ModellingReports.findRSTKcal(strRST) * consumed.getAsDouble());
			}
			break;
		case "Transfer":
			units[PRODUCED] = transfer.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsTransferred() > 0).mapToDouble(p -> p.getUnitsTransferred()).average();
			units[SOLD] = transfer.stream().filter(p -> p.getResourceSubType() == strRST)
					.filter(p -> p.getUnitsSold() > 0).mapToDouble(p -> p.getUnitsSold()).average();
			consumed = transfer.stream().filter(p -> p.getResourceSubType() == strRST)
					.mapToDouble(p -> p.getUnitsConsumed()).average();
			if (consumed.isPresent()) {
				units[AVGOFPERCENTCONSUMED] = OptionalDouble
						.of(ModellingReports.findRSTKcal(strRST) * consumed.getAsDouble());
			}
			break;

		}
		return units;
	}
}
