package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:38
*/

import efd.rest.dto.ModellingScenarioDto;

import java.util.List;


public interface ModellingScenarioService {
    List<ModellingScenarioDto> getAllModellingScenarios();

    ModellingScenarioDto getModellingScenarioByTitle(String title);


}
