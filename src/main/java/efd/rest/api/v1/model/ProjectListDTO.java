package efd.rest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by jt on 9/26/17.
 */
@Data
@AllArgsConstructor
public class ProjectListDTO {

    List<ProjectDTO> projects;

}
