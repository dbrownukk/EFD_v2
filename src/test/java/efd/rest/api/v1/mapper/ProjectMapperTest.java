package efd.rest.api.v1.mapper;


import efd.model.Project;
import efd.rest.api.v1.model.ProjectDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProjectMapperTest {

    public static final String NAME = "Joe";
    public static final String ID = "1" ;

    ProjectMapper projectMapper = ProjectMapper.INSTANCE;

    @Test
    public void categoryToCategoryDTO() throws Exception {

        //given
        Project project = new Project();
        project.setProjecttitle(NAME);
        project.setProjectid(ID);

        //when
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);

        //then
        assertEquals(ID, projectDTO.getProjectid());
        assertEquals(NAME, projectDTO.getProjecttitle());
    }

}