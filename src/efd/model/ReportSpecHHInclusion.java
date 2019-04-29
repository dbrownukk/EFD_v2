package efd.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;

@Entity

public class ReportSpecHHInclusion extends Identifiable {

	@ManyToOne
	@Required
	private ReportSpecUse reportSpecUse;
	
	
	@ManyToOne
	@Required
	//@NewAction("")
	private Household household;


	public ReportSpecUse getReportSpecUse() {
		return reportSpecUse;
	}


	public void setReportSpecUse(ReportSpecUse reportSpecUse) {
		this.reportSpecUse = reportSpecUse;
	}


	public Household getHousehold() {
		return household;
	}


	public void setHousehold(Household household) {
		this.household = household;
	}
	
	
	
}
