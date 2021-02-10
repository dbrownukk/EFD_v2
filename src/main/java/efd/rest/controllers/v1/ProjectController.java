package efd.rest.controllers.v1;

import efd.rest.api.v1.model.ProjectListDTO;
import efd.rest.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jt on 9/26/17.
 */
@RestController
@RequestMapping("/api/v1/projects/")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<ProjectListDTO> getallProjects(){

        return new ResponseEntity<ProjectListDTO>(
                new ProjectListDTO(projectService.getAllProjects()), HttpStatus.OK);
    }


}
