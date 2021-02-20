package efd.rest.mapper;

import efd.model.ModellingScenario;
import efd.rest.dto.ModellingscenarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/*
    @Author david
    @Create 16/02/2021 15:52
*/
@Mapper(componentModel = "spring")
public interface ModellingScenarioMapper {

    ModellingScenarioMapper INSTANCE = Mappers.getMapper(ModellingScenarioMapper.class);

    @Mapping(source = "project.projectid",target = "projectProjectId")
    @Mapping(source = "study.id",target = "studyId")
    ModellingscenarioDTO modellingScenarioToModellingScenarionDto(ModellingScenario modellingScenario);

    @Mapping(source = "projectProjectId",target = "project.projectid")
    @Mapping(source = "studyId",target = "study.id")
    ModellingScenario modellingScenarioDtoToModellingScenario(ModellingscenarioDTO modellingScenarioDTO);

}
