package efd.model;

import java.math.*;
import java.text.*;
import java.util.*;

import javax.inject.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.lang.time.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.*;
import org.openxava.annotations.*;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.Tab;
import org.openxava.calculators.*;
import org.openxava.validators.*;
import org.openxava.actions.*;

import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import com.openxava.naviox.model.*;

import efd.actions.*;
import efd.model.Asset.*;
import efd.validations.*;

import org.openxava.tab.*;

@Entity

@Views({ @View(members = "Project[#projecttitle,pdate;"
		+ "altCurrency,altExchangeRate,areaMeasurement;donor,funder];Notes[notes];livelihoodZone;"),
		@View(name = "Proj", members = "Project[projecttitle,pdate,altCurrency,altExchangeRate];livelihoodZone"),
		@View(name = "NewLZ", members = "Project[projecttitle,pdate];livelihoodZone;community"),
		@View(name = "SimpleProject", members = "projecttitle,pdate"),
		@View(name = "StudyProject", members = "Project[#projecttitle,pdate;altCurrency,altExchangeRate;donor,funder,notes]"),
		@View(name = "NewlineProject", members = "projecttitle;pdate") })

// model is OHEA or OIHM
// No longer required - Projects cross OHEA and OIHM 
//@Tab(filter = ProjectModelFilter.class, properties = "projecttitle,pdate,altCurrency.description,altExchangeRate", baseCondition = "${model} = ?")
@Tab(properties = "projecttitle,pdate,altCurrency.description,altExchangeRate")


@Table(name = "Project")


public class Project {


	@PrePersist
	@PreUpdate
	private void checkExchangeRate() {

		/* in Project persist/update check if ALt currency used - needs an alt exc rate  */
				
				
		if(this.getAltCurrency()!=null && this.getAltExchangeRate() == null)
		{
			System.out.println("Need to stop update");
		throw new javax.validation.ValidationException(
				XavaResources.getString("If Alternate Currency set, enter an Exchange rate to use"));
		}

	}
	

	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ProjectID", length = 32, unique = true)
	private String projectid;

	/***********************************************************************************************/
	@Column(name = "ProjectTitle", length = 255, unique = true)
	// @OnChange(value = LoadRemoteToOrg.class)
	@Required
	@SearchKey
	private String projecttitle;

	/***********************************************************************************************/
	@Stereotype("DATE")
	@Column(name = "PDate")
	@Required
	private java.util.Date pdate;

	/***********************************************************************************************/
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
			optional = true)
	@NoModify
	@NoCreate
	@DescriptionsList(descriptionProperties = "currency")
	private Country altCurrency;

	/***********************************************************************************************/
	@Column(precision = 10, scale = 5)
	@Digits(integer = 10, fraction = 5)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)

	private BigDecimal altExchangeRate;

	/***********************************************************************************************/
	// @NewActions({
	// @NewAction(forViews="DEFAULT", value="LivelihoodZone.new LZ"), /* Check
	// projectid is not empty */
	// })
	
	
	//@AddAction("LivelihoodZone.add") /* Remember Bug for NewAction and ManyToMany */
	
	
	@NewAction("ManyToMany.new")
	@ManyToMany
	@JoinTable(name = "projectlz", joinColumns = @JoinColumn(name = "Project", referencedColumnName = "ProjectID", nullable = false), inverseJoinColumns = @JoinColumn(name = "LZ", referencedColumnName = "LZID", nullable = false), uniqueConstraints = @UniqueConstraint(columnNames = {
			"Project", "LZ" }))
	@ListProperties("lzname,country.description,country.currency,lzzonemap")
	@CollectionView("SimpleLZ")
	@RowAction("LivelihoodZone.Report")
	private Collection<LivelihoodZone> livelihoodZone;
	/***********************************************************************************************/

	
	
	/***********************************************************************************************/
	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;

	
	/*
	 * 
	 * Versioning fails with ManyToMany - raising bug 
	 * 
	 * Removed all custom actions and views and fails
	 * 
	 * Create proj, save, add LZ and get error - another user has updated this record
	 * 
	 */
	//@Version
	//private Integer version;

	@DefaultValueCalculator(StringCalculator.class)
	private String donor;

	private String funder;

	// OHEA or OIHM
	@Hidden
	private String model;

	// indicate if model -s OHEA or OIHM

	// Calculated property
	@Hidden
	public String getTheModel() {
		String userName = Users.getCurrent();
		User user = User.find(userName);
		String r = null;

		if (user.hasRole("oihm_user"))
			r = "OIHM";
		else if (user.hasRole("ohea_user"))
			r = "OHEA";
		return r;
	}

	/***********************************************************************************************/
	@Column(name = "Area", nullable = true)
	@Required
	private Area areaMeasurement;

	public enum Area {
		Acre, Hectare
	}
	/***********************************************************************************************/

	
	
	public String getProjectid() {
		return projectid;
	}



	public Area getAreaMeasurement() {
		return areaMeasurement;
	}



	public void setAreaMeasurement(Area areaMeasurement) {
		this.areaMeasurement = areaMeasurement;
	}



	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}



	public String getDonor() {
		return donor;
	}

	public void setDonor(String donor) {
		this.donor = donor;
	}

	public String getFunder() {
		return funder;
	}

	public void setFunder(String funder) {
		this.funder = funder;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getProjecttitle() {
		return projecttitle;
	}

	public void setProjecttitle(String projecttitle) {
		this.projecttitle = projecttitle;
	}

	public java.util.Date getPdate() {
		return pdate;
	}

	public void setPdate(java.util.Date pdate) {
		this.pdate = pdate;
	}

	public Collection<LivelihoodZone> getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(Collection<LivelihoodZone> livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public Country getAltCurrency() {
		return altCurrency;
	}

	public void setAltCurrency(Country altCurrency) {
		this.altCurrency = altCurrency;
	}

	public BigDecimal getAltExchangeRate() {
		return altExchangeRate  == null?BigDecimal.ZERO:altExchangeRate;
	}

	public void setAltExchangeRate(BigDecimal altExchangeRate) {
		this.altExchangeRate = altExchangeRate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
