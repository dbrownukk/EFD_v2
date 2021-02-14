package efd.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
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

}
