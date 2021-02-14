package efd.validations;

/* Validate that mandatory reports are in Custom Report Spec - See Reports - isMandatory */
/* DRB 21/12/2020 */

import efd.model.Report;
import org.openxava.util.Messages;
import org.openxava.validators.IValidator;

import java.util.Collection;

public class MandatoryOHEAReportsInCRS implements IValidator {

    private Collection<Report> report;
    @Override
    public void validate(Messages errors) throws Exception {
        System.out.println("in MandatoryOHEAReports CRS");
        System.out.println("reports  = "+report.toString());
    }
}
