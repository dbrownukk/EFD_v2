package efd.rest.api.v1.mapper;


import efd.rest.domain.Project;
import efd.rest.api.v1.model.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by jt on 9/25/17.
 */
@Mapper
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);



    ProjectDTO projectToProjectDTO(Project project);
}
