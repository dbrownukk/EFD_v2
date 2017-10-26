package efd.model;

import javax.persistence.*;

@Entity
@Table(name = "ProjectLZ")
@IdClass(ProjectLZId.class)
// @Table(name = "ProjectLZ", uniqueConstraints = { @UniqueConstraint(columnNames = { "LZ", "PROJECT" }) })

public class ProjectLZ {

	@Id
	@ManyToOne
	@JoinColumn(name="LZID")
	private LivelihoodZone livelihoodZone;

	@Id
	@ManyToOne
	@JoinColumn(name = "ProjectID")
	private Project project;

	public LivelihoodZone getLivelihoodZone() {
		return livelihoodZone;
	}

	public void setLivelihoodZone(LivelihoodZone livelihoodZone) {
		this.livelihoodZone = livelihoodZone;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}



	
}
