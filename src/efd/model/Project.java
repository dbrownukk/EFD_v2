package efd.model;

import java.math.*;
import java.text.*;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.apache.commons.lang.time.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.*;
import org.openxava.annotations.*;
import org.openxava.annotations.NewAction;
import org.openxava.annotations.Tab;
import org.openxava.calculators.*;
import org.openxava.validators.*;
import org.openxava.actions.*;

import org.openxava.jpa.*;
import org.openxava.model.*;
import org.openxava.util.jxls.*;
import org.openxava.web.servlets.*;

import efd.actions.*;

import org.openxava.tab.*;

@Entity

	@Views({ @View(members = "Project[projecttitle,pdate,altCurrency,altExchangeRate;notes];livelihoodZone"),
		@View(name="Proj",members = "Project[projecttitle,pdate,altCurrency,altExchangeRate];livelihoodZone"),
		@View(name="NewLZ", members = "Project[projecttitle,pdate];livelihoodZone;community"),
		@View(name = "SimpleProject", members = "projecttitle,pdate"),
		@View(name = "NewlineProject", members = "projecttitle;pdate") })




@Tab(properties = "projecttitle,pdate,altCurrency.description,altExchangeRate") 
//@Tab(properties = "projecttitle,pdate,altCurrency.description,altExchangeRate", editors = "List, Cards") 

@Table(name = "Project")
public class Project {
	

	
	
	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") // Universally Unique Identifier
												
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ProjectID", length = 32, unique = true)
	private String projectid;
	
	/***********************************************************************************************/
	@Column(name = "ProjectTitle", length = 255, unique = true)
	//@OnChange(value = LoadRemoteToOrg.class)
	@Required	
	private String projecttitle;
	
	/***********************************************************************************************/
	@Stereotype("DATE")
	    @Column(name="PDate") @Required
	    private java.util.Date pdate;
	
	/***********************************************************************************************/	
	@ManyToOne(fetch = FetchType.LAZY, // The reference is loaded on demand
	optional = true)
	@NoModify
	@NoCreate
	@DescriptionsList(descriptionProperties="currency" )
	private Country altCurrency;
	
	/***********************************************************************************************/
	@Column(precision=10, scale=5)
	@Digits(integer=10,fraction=5)
	@DefaultValueCalculator(ZeroBigDecimalCalculator.class)

	private BigDecimal altExchangeRate;
	
	/***********************************************************************************************/
	//@NewActions({
	//@NewAction(forViews="DEFAULT", value="LivelihoodZone.new LZ"), /* Check projectid is not empty */ 
	//})
	@AddAction("LivelihoodZone.add") /* Remember Bug for NewAction and ManyToMany */ 
	//@AddAction("LivelihoodZone.add")
	@NewAction("ManyToMany.new") 
	@ManyToMany
	@JoinTable(name = "projectlz", 
	joinColumns = @JoinColumn(name = "Project", referencedColumnName = "ProjectID", nullable = false), 
	inverseJoinColumns = @JoinColumn(name = "LZ", referencedColumnName = "LZID", nullable = false),
	uniqueConstraints=
            @UniqueConstraint(columnNames = { "Project", "LZ" }))
	@ListProperties("lzname,country.description,country.currency,lzzonemap")
	@CollectionView("SimpleLZ") 
	private Collection<LivelihoodZone> livelihoodZone;

	/***********************************************************************************************/
	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;
	
	
	
	/* removed until save to web file is fixed */
	/*
	 * 
	 * 
	 
	@Stereotype("FILE")
	
	@Column(length = 32, name = "StageFile")
	private String stageFile;
	*/
	/*  dont forget setters,,,,,,,,
	@Version
	private Integer version;
	*/





	/***********************************************************************************************/

	public String getProjectid() {
		return projectid;
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
		return altExchangeRate;
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
