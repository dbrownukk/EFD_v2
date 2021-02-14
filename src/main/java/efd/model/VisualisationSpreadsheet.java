/* Visualisation Spreadsheet - Used after download of Modelling/OHEA/OIHM Reports 
 * to show graphs
 * 
 * Download option on Run Report Dialogs
 * 
 * DRB 7/5/2020
 * 
 */

package efd.model;

import javax.persistence.Column;

import org.hibernate.validator.constraints.UniqueElements;
import org.openxava.annotations.Required;
import org.openxava.annotations.Stereotype;
import org.openxava.annotations.View;

@View(members = "name,visualisationSpreadsheet")

//  @Entity



public class VisualisationSpreadsheet extends EFDIdentifiable {

	@Required 
	@UniqueElements
	@Column(length = 32, name = "Name", nullable=false)
	private String name;
	
	@Stereotype("FILES")
	@Column(length = 32, name = "VisualisationSpreadsheet")
	private String visualisationSpreadsheet;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisualisationSpreadsheet() {
		return visualisationSpreadsheet;
	}

	public void setVisualisationSpreadsheet(String visualisationSpreadsheet) {
		this.visualisationSpreadsheet = visualisationSpreadsheet;
	}
	
}
