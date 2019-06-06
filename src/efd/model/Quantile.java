package efd.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.actions.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

import efd.actions.*;
import efd.validations.*;

@View(members = "Quantile[#name,percentage,sequence]")

@Table(name = "Quantile",
uniqueConstraints = {
		@UniqueConstraint(name = "unique_quantile", columnNames = { "customReportSpec_id","name" }) })

@Entity


public class Quantile extends Identifiable {

	@PrePersist
	public void calcNextSeq() throws Exception{
		System.out.println("in prepersist for quantile ");
		Query query = XPersistence.getManager().createQuery("select max(sequence) from Quantile where customReportSpec_id = :crsid");
		query.setParameter("crsid",getCustomReportSpec().getId());
		Integer lastNumber = (Integer) query.getSingleResult();
		Integer nextNumber = lastNumber == null?1:lastNumber+1;
		System.out.println("nextnumber = "+nextNumber);
		setSequence(nextNumber);
	}
	
	
	
	@Required
	@Column(length = 45, nullable = false)
	private String name;

	//@Required
	@Column(nullable = false)
	// @DefaultValueCalculator(ZeroIntegerCalculator.class)
	@ReadOnly
	private int sequence;

	@Required
	@Column(nullable = false)
	@Min(0)
	@Max(100)
	@DefaultValueCalculator(ZeroIntegerCalculator.class)
	private int percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	private CustomReportSpec customReportSpec;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public CustomReportSpec getCustomReportSpec() {
		return customReportSpec;
	}

	public void setCustomReportSpec(CustomReportSpec customReportSpec) {
		this.customReportSpec = customReportSpec;
	}



}
