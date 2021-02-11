package efd.rest.controllers.v1;

import efd.rest.api.v1.model.ProjectDTO;
import efd.rest.services.ProjectService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProjectControllerTest extends BaseIT{

    public static final String NAME = "Jim";

    @Mock
    ProjectService projectService;

    @InjectMocks
    ProjectController projectController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp()  {


        //mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }




    @Test
    public void testListProjects() throws Exception {
        ProjectDTO project1 = new ProjectDTO();
        project1.setProjectid("11");
        project1.setProjecttitle(NAME);

        ProjectDTO project2 = new ProjectDTO();
        project2.setProjectid("22");
        project2.setProjecttitle("Bob");

        List<ProjectDTO> projects = Arrays.asList(project1, project2);

        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/v1/projects/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects", hasSize(2)));
    }

    @Test
    public void testGetByNameCategories() throws Exception {
        ProjectDTO project1 = new ProjectDTO();
        project1.setProjecttitle("1");
        project1.setProjecttitle(NAME);

        when(projectService.getProjectByName(anyString())).thenReturn(project1);

        mockMvc.perform(get("/api/v1/project/Jim")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME)));
    }
}