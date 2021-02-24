package efd.model;

import com.openxava.naviox.model.Role;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity

@Table(name = "efdusers")
public class EfdUser {
    @Id
    @Column(name = "name")
    private String name;



    @Column(name = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    @ManyToMany
    @JoinTable( // Though we use default names because a JPA bug generating schema
            name="OXUSERS_OXROLES",
            joinColumns=
            @JoinColumn(name="OXUSERS_NAME", referencedColumnName="NAME"),
            inverseJoinColumns=
            @JoinColumn(name="ROLES_NAME", referencedColumnName="NAME")
    )
    private Collection<Role> roles;

    public String getName() {
        return this.name;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }
}
