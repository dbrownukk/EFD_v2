package efd.rest.api.v1.mapper;

import efd.model.Project;
import efd.rest.api.v1.model.ProjectDTO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-16T09:04:50+0000",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_171 (Oracle Corporation)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDTO projectToProjectDTO(Project project) {
        if ( project == null ) {
            return null;
        }

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setProjectid( project.getProjectid() );
        projectDTO.setProjecttitle( project.getProjecttitle() );
        projectDTO.setPdate( project.getPdate() );

        return projectDTO;
    }
}
