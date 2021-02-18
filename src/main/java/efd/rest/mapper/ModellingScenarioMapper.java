package efd.rest.mapper;

import efd.model.ModellingScenario;
import efd.rest.domain.ModellingScenarioDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*
    @Author david
    @Create 16/02/2021 15:52
*/
@Mapper
public interface ModellingScenarioMapper {

    ModellingScenarioMapper INSTANCE = Mappers.getMapper(ModellingScenarioMapper.class);

    ModellingScenarioDto modellingScenarioToModellingScenarionDto(ModellingScenario modellingScenario);
    ModellingScenarioDto modellingScenarioDtoToModellingScenario(ModellingScenarioDto modellingScenarioDto);

}
