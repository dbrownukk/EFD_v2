package efd.model;

import java.io.*;

import javax.persistence.*;

import org.openxava.annotations.*;

// @View(name="SimpleLZ",members="livelihoodZone.lzname")
@Embeddable
public class ProjectLZId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="LZID")
	private String livelihoodZone;
	
	@Column(name="ProjectID")
	private String project;
}
