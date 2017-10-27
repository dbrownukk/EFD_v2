package efd.model;
import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;


@Entity 



@Views({
	//@View(members="Project[projecttitle,pdate]"),
	@View(name="SimpleProject", members="projecttitle,pdate,Project.Spreadsheet()"),
	@View(name="NewlineProject", members="projecttitle;pdate")
	})

@Tab ( properties="projecttitle;pdate",editors ="List, Cards") // removes graph option

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
    
    
    
    @NewAction("ManyToMany.new")
    @ManyToMany
    @JoinTable(name="projectlz",
    		joinColumns=@JoinColumn(name="Project", referencedColumnName="ProjectID"),
    	      inverseJoinColumns=@JoinColumn(name="LZ", referencedColumnName="LZID"))
    @ListProperties("lzname;country,lzzonemap")
    private Collection<LivelihoodZone> livelihoodZone;

    

    /*
    @NewAction("ManyToMany.new")
    @OneToMany(mappedBy="project")
    @ListProperties("this.projectLZ.livelihoodZone.lzname")
    private Collection<ProjectLZ> projectLZ;
    */

    /* getters/setters */
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
    

    
	


    
    
        
    
}
