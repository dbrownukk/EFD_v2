package efd.actions;
/* Save Custom Report SPec and ensure some reports are always included in Spec - 366 and 372 for OHEA
DRB 18/12/2020
 */

import efd.model.CustomReportSpecOHEA;
import efd.model.Report;
import org.hibernate.type.BooleanType;
import org.hibernate.type.YesNoType;
import org.openxava.actions.SaveAction;
import org.openxava.jpa.XPersistence;
import org.openxava.model.MapFacade;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class SaveCustomReportSpecOHEA extends SaveAction {
    public void execute() throws Exception {

        System.out.println("In SaveCustomReportSpecOHEA");
        super.execute();

        Map keyValues = getView().getKeyValues();
        System.out.println("key vals = ");
        CustomReportSpecOHEA crs
                = (CustomReportSpecOHEA) MapFacade.findEntity(getModelName(), getView().getKeyValues());

        System.out.println("crs = " + crs.getSpecName());
        int repnum;

        List<Report> reports;
        reports = XPersistence.getManager()
                .createQuery(" from Report where isMandatory = 'Y' and method = 0 ").getResultList();
//                .setParameter("isMandatory", "T").getResultList();
        System.out.println("no of mandatory reps = "+reports.size());
        for (Report report : reports) {
            addMissingReport(crs,report.getCode());
        }
        ;


        getView().findObject();
        getView().refresh();
        getView().refreshCollections();
    }

    private void addMissingReport(CustomReportSpecOHEA crs, int repnum) {
        if (crs.getReport().stream().noneMatch(p -> p.getCode() == repnum)) {
            Report reportToAdd = Report.findByCode(repnum);
            crs.getReport().add(reportToAdd);
        }
    }

}
