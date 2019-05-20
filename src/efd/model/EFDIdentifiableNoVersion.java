package efd.model;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;

/**
 * Base class for defining entities with a UUID id. <p>
 * 
 * and Versioning for multi user locking
 */

@MappedSuperclass
public class EFDIdentifiableNoVersion {
	
	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") 

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", length = 32, unique = true)
	private String id;

	


	@Stereotype("FILES")
	@Column(length = 32, name = "Notes")
	private String notes;
	
	

	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	
	
	
}
