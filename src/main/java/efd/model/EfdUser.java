package efd.model;

import com.openxava.naviox.model.Role;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "efdusers")
public class EfdUser {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private boolean active;

    @Column(name = "authenticateWithLDAP")
    private boolean authenticateWithLdap;

    @Column(name = "birthDate")
    private java.sql.Timestamp birthDate;

    @Column(name = "creationDate")
    private java.sql.Timestamp creationDate;

    @Column(name = "email")
    private String email;

    @Column(name = "failedLoginAttempts")
    private Integer failedLoginAttempts;

    @Column(name = "familyName")
    private String familyName;

    @Column(name = "forceChangePassword")
    private boolean forceChangePassword;

    @Column(name = "givenName")
    private String givenName;

    @Column(name = "jobTitle")
    private String jobTitle;

    @Column(name = "lastLoginDate")
    private java.sql.Timestamp lastLoginDate;

    @Column(name = "lastPasswordChangeDate")
    private java.sql.Timestamp lastPasswordChangeDate;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "nickName")
    private String nickName;

    @Column(name = "password")
    private String password;

    @Column(name = "passwordRecoveringCode")
    private String passwordRecoveringCode;

    @Column(name = "passwordRecoveringDate")
    private java.sql.Timestamp passwordRecoveringDate;

    @Column(name = "privacyPolicyAcceptanceDate")
    private java.sql.Timestamp privacyPolicyAcceptanceDate;

    @Column(name = "recentPassword1")
    private String recentPassword1;

    @Column(name = "recentPassword2")
    private String recentPassword2;

    @Column(name = "recentPassword3")
    private String recentPassword3;

    @Column(name = "recentPassword4")
    private String recentPassword4;

    @Column(name = "allowedIP")
    private String allowedIp;

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

    public boolean isActive() {
        return this.active;
    }

    public boolean isAuthenticateWithLdap() {
        return this.authenticateWithLdap;
    }

    public Timestamp getBirthDate() {
        return this.birthDate;
    }

    public Timestamp getCreationDate() {
        return this.creationDate;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getFailedLoginAttempts() {
        return this.failedLoginAttempts;
    }

    public String getFamilyName() {
        return this.familyName;
    }

    public boolean isForceChangePassword() {
        return this.forceChangePassword;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Timestamp getLastLoginDate() {
        return this.lastLoginDate;
    }

    public Timestamp getLastPasswordChangeDate() {
        return this.lastPasswordChangeDate;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordRecoveringCode() {
        return this.passwordRecoveringCode;
    }

    public Timestamp getPasswordRecoveringDate() {
        return this.passwordRecoveringDate;
    }

    public Timestamp getPrivacyPolicyAcceptanceDate() {
        return this.privacyPolicyAcceptanceDate;
    }

    public String getRecentPassword1() {
        return this.recentPassword1;
    }

    public String getRecentPassword2() {
        return this.recentPassword2;
    }

    public String getRecentPassword3() {
        return this.recentPassword3;
    }

    public String getRecentPassword4() {
        return this.recentPassword4;
    }

    public String getAllowedIp() {
        return this.allowedIp;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }
}
