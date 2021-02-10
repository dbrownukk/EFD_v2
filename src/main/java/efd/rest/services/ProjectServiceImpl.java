package efd.rest.services;

import efd.rest.api.v1.mapper.ProjectMapper;
import efd.rest.api.v1.model.ProjectDTO;
import efd.rest.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by jt on 9/26/17.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectMapper projectMapper, ProjectRepository projectRepository) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        final List<ProjectDTO> collect = projectRepository.findAll()
                .stream()
                .map(projectMapper::projectToProjectDTO)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public ProjectDTO getProjectByName(String name) {
        return null;
    }
}
