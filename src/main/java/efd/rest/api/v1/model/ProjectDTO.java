package efd.rest.api.v1.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by jt on 9/24/17.
 */
@Data
public class ProjectDTO {
    private String projectid;
    private String projecttitle;
    private Date pdate;
}
