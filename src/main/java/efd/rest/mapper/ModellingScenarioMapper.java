package efd.rest.mapper;

import efd.model.ModellingScenario;
import efd.rest.domain.ModellingScenarioDto;
import org.mapstruct.Mapper;

/*
    @Author david
    @Create 16/02/2021 15:52
*/
@Mapper
public interface ModellingScenarioMapper {

ModellingScenarioDto modellingScenarioToModellingScenarionDto(ModellingScenario ms);
ModellingScenario modellingScenarioDtoToModellingScenarion(ModellingScenarioDto msDto);

}
