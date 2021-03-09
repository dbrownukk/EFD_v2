package efd.rest.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by david on 03/03/2021.
 */
@Data
public class ProjectDTO {
    private String projectid;
    private String projecttitle;
    private Date pdate;
}
