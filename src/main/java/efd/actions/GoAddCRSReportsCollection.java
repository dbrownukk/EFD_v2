package efd.actions;
/* Save Custom Report SPec and ensure some reports are always included in Spec - 366 and 372 for OHEA
DRB 18/12/2020
 */

import efd.model.CustomReportSpec;
import efd.model.CustomReportSpecOHEA;
import efd.model.Report;
import org.openxava.actions.GoAddElementsToCollectionAction;
import org.openxava.jpa.XPersistence;
import org.openxava.model.MapFacade;

import java.util.List;
import java.util.Map;

public class GoAddCRSReportsCollection extends GoAddElementsToCollectionAction {

    private String iMethod;  // passed in from controller
    private CustomReportSpecOHEA crs;
    private int repnum;

    public String getiMethod() {
        return iMethod;
    }

    public void setiMethod(String iMethod) {
        this.iMethod = iMethod;
    }

    public void execute() throws Exception {



        super.execute();


        Map keyValues = getPreviousView().getKeyValues();
        if (keyValues.size() == 0) {


            addError("Save Custom Report Specification before adding Reports");
            closeDialog();

        }

        List<Report> reports;
        reports = XPersistence.getManager()
                .createQuery(" from Report where isMandatory = 'Y' and method =" + getiMethod())
                .getResultList();



        if (getiMethod().equals("0")) {
            CustomReportSpecOHEA crs
                    = (CustomReportSpecOHEA) MapFacade.findEntity(getModelName(), getView().getKeyValues());
            for (Report report : reports) {
                addMissingReport(crs, report.getCode());
            }
        } else if (getiMethod().equals("1")) {
            CustomReportSpec crs
                    = (CustomReportSpec) MapFacade.findEntity(getModelName(), getView().getKeyValues());
            for (Report report : reports) {
                addMissingReport(crs, report.getCode());
            }
        }


    }

    public static void addMissingReport(CustomReportSpecOHEA crs, int repnum) {

        if (crs.getReport().stream().noneMatch(p -> p.getCode() == repnum)) {
            Report reportToAdd = Report.findByCode(repnum);
            crs.getReport().add(reportToAdd);
        }
    }

    public static void addMissingReport(CustomReportSpec crs, int repnum) {
        if (crs.getReport().stream().noneMatch(p -> p.getCode() == repnum)) {
            Report reportToAdd = Report.findByCode(repnum);
            crs.getReport().add(reportToAdd);
        }
    }

    @Override
    public String getNextController() {
        String nextController = super.getNextController();
        return nextController;
    }




}
