package reasyst.efd;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Entity 



@Views({
	@View(members="Project [projecttitle;pdate]"+"livelihoodzones"),
	 @View(name="SimpleProject", members="projecttitle;pdate")
	})

@Tab ( editors ="List, Cards", properties="projecttitle,pdate") // removes graph option

@Table(name="Project")
public class Project {
	
    @Id
    @Hidden // The property is not shown to the user. It's an internal identifier
    @GeneratedValue(generator="system-uuid") // Universally Unique Identifier (1)
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name="ProjectID",length=32,unique=true)
    private String projectid;
 
    @Column(name="ProjectTitle",length=255,unique=true) @Required
    private String projecttitle;
    

    @Stereotype("DATETIME")
    @Column(name="PDate") @Required
    private java.util.Date pdate;
    
    
    @NewAction("")
    @ManyToMany(mappedBy="projects") @CollectionView("SimpleLZ") 
    private Collection<LivelihoodZone> livelihoodzones; 
    
    


	public Collection<LivelihoodZone> getLivelihoodzones() {
		return livelihoodzones;
	}

	public void setLivelihoodzones(Collection<LivelihoodZone> livelihoodzones) {
		this.livelihoodzones = livelihoodzones;
	}

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


    
    
    
    
}
