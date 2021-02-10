package efd.rest.services;


import efd.rest.domain.Project;
import efd.rest.api.v1.mapper.ProjectMapper;
import efd.rest.api.v1.model.ProjectDTO;
import efd.rest.repositories.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    public static final String ID = "222";
    public static final String NAME = "Jimmy";
    ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        projectService = new ProjectServiceImpl(ProjectMapper.INSTANCE, projectRepository);

    }

    @Test
    public void getAllProjects() throws Exception {

        //given
        List<Project> projects = Arrays.asList(new Project(), new Project(), new Project());

        when(projectRepository.findAll()).thenReturn(projects);

        //when
        List<ProjectDTO> projectDTOS = projectService.getAllProjects();

        //then
        assertEquals(3, projectDTOS.size());

    }



}