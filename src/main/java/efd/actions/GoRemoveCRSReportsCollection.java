package efd.actions;
/* Remove Custom Report SPec and ensure some reports are always included in Spec - 366 and 372 for OHEA
DRB 18/12/2020
 */

import efd.rest.domain.model.Report;
import org.openxava.actions.RemoveSelectedInCollectionAction;
import org.openxava.jpa.XPersistence;
import org.openxava.model.MapFacade;

import java.util.List;
import java.util.Map;

public class GoRemoveCRSReportsCollection extends RemoveSelectedInCollectionAction {

    private String model;
    private int row;
    /**
     * iMethod 0 for OHEA 1 for OIHM
     */
    private String iMethod;  // passed in from controller

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getiMethod() {
        return iMethod;
    }

    public void setiMethod(String iMethod) {
        this.iMethod = iMethod;
    }

    public void execute() throws Exception {
        System.out.println("In CRSOHEA remove");
        //Get mandatory reports
        List<Report> mandatoryReports;
        mandatoryReports = XPersistence.getManager()
                .createQuery(" from Report where isMandatory = 'Y' and method =" + getiMethod())
                .getResultList();

        Map[] selectedOnes = getSelectedKeys();
        boolean process = true;
        for (Map selectedOne : selectedOnes) {
            Report selectedReportForDeletion = (Report) MapFacade.findEntity("Report", selectedOne);
            for (Report mandatoryReport : mandatoryReports) {
                if (mandatoryReport == selectedReportForDeletion) {
                    addError("Cannot delete a Mandatory Report " + mandatoryReport.getName());
                    System.out.println("cannot delete this rep ");
                    process = false;

                }
            }
            ;
        }
        ;

        if (process) {
            super.execute();
            System.out.println("In CRSOHEA done execute");
        }


    }

}




