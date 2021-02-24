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
public class EFDIdentifiable {
	
	@Id
	@Hidden
	@GeneratedValue(generator = "system-uuid") 

	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID", length = 32, unique = true)
	private String id;

	
	@Version
	private Integer version;

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


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	
	
}
