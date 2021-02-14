package efd.model;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;
import org.openxava.model.*;

@View(members = "Quantile[#name,percentage,sequence]")

//@Table(name = "Quantile",
//uniqueConstraints = {
//		@UniqueConstraint(name = "unique_quantile", columnNames = { "customReportSpec_id","name" }),
//		@UniqueConstraint(name ="unique_sequence",columnNames= {"customReportSpec_id","sequence"})
//		})


@Tab(properties="name,sequence, percentage+")
@Entity


public class Quantile extends Identifiable {

	@PrePersist
	public void calcNextSeq() throws Exception{
		System.out.println("in prepersist for quantile ");
		Query query = XPersistence.getManager().createQuery("select count(*) from Quantile where customReportSpec_id = :crsid");
		System.out.println("in prepersist for quantile  11");
		query.setParameter("crsid",getCustomReportSpec().getId());
		System.out.println("in prepersist for quantile 22");
		long lastNumber = (Long) query.getSingleResult();
		System.out.println("in prepersist for quantile 33");
		System.out.println("in prepersist for quantile ");
		Integer nextNumber;
		if (lastNumber == 0)
			nextNumber = 1;
		else
			nextNumber = (int) (lastNumber+1);
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
