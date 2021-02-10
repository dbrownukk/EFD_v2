package efd.rest.services;

import efd.rest.api.v1.model.ProjectDTO;

import java.util.List;

/**
 * Created by jt on 9/26/17.
 */
public interface ProjectService {

    List<ProjectDTO> getAllProjects();

    ProjectDTO getProjectByName(String name);
}
