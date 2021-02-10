package efd.rest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
    @Author david
    @Create 07/02/2021 17:09
*/
@Data
@Entity
public class localProject {
    @Id
    private String projectid;
    private String projecttitle;
}
