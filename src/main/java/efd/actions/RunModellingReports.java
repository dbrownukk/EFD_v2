/**
 * @author david 25 Feb 2021
 * <p>
 * Called from ModellingScenario dialog and then calls ModelingReport with params
 * so that report can also be called from API
 * <p>
 * Note that ModellingReports is no longer an action, just a normal class
 */
package efd.actions;

import org.openxava.actions.IForwardAction;
import org.openxava.jpa.XPersistence;
import org.openxava.tab.Tab;

import efd.model.CustomReportSpecListModelling;
import efd.model.ModellingScenario;
import lombok.Getter;
import lombok.Setter;


public @Setter
@Getter
class RunModellingReports extends BaseReporting implements IForwardAction {


    public static final String STUDY_HOUSEHOLD = "study.household";
    public static final String LIVELIHOOD_ZONE_SITE = "livelihoodZone.site";
    private String forwardURI;

    @Override
    public void execute() throws Exception {

        ModellingReports modellingReports = new ModellingReports();
        String modellingScenarioId = getPreviousView().getValueString("id");
        CustomReportSpecListModelling.ModelType model = (CustomReportSpecListModelling.ModelType) getView().getValue("modelType");
        ModellingScenario modellingScenario = XPersistence.getManager().find(ModellingScenario.class, modellingScenarioId);

		modellingReports.setTargetTab(getTab());
        

        //OIHM
        if (modellingScenario.getStudy() != null) {
        	
			Tab collectionTab = getView().getSubview(STUDY_HOUSEHOLD).getCollectionTab();

			modellingReports.setTargetTab(collectionTab);

            //OHEA
        } else if (modellingScenario.getProject() != null) {
            modellingReports.setTargetTab(getView().getSubview(LIVELIHOOD_ZONE_SITE).getCollectionTab());
        } else {
            addError("No Project or Study in Modelling Scenario");
            return;
        }

        if(!modellingReports.getTargetTab().hasSelected())
        {
            addError("Select at least one Household or Village");
            return;
        }

		modellingReports.setSession(getRequest().getSession());

		System.out.println("session in run rep = " + modellingReports.getSession());

        modellingReports.setModellingScenarioId(modellingScenarioId);

        modellingReports.setModelType(model); //  ChangeScenario, CopingStrategy

        modellingReports.execute();
        setForwardURI("/xava/report.xls?time=" + System.currentTimeMillis());
        closeDialog();
    }

    /******************************************************************************************************************************************/

    @Override
    public String getForwardURI() {
        return forwardURI;
    }

    /******************************************************************************************************************************************/

    public void setForwardURI(String forwardURI) {
        this.forwardURI = forwardURI;
    }

    /******************************************************************************************************************************************/

    @Override
    public boolean inNewWindow() {
        if (forwardURI == null)
            return false;
        return true;
    }

}
