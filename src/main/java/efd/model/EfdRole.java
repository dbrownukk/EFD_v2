package efd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "efdroles")
public class EfdRole {
    @Id
    @Column(name = "OXUSERS_NAME")
    private String oxusersName;

    @Column(name = "ROLES_NAME")
    private String rolesName;

}
