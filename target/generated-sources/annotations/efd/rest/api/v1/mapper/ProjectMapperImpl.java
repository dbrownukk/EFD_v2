package efd.rest.api.v1.mapper;

import efd.rest.api.v1.model.ProjectDTO;
import efd.rest.domain.Project;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-10T12:06:49+0000",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.8 (Red Hat, Inc.)"
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
