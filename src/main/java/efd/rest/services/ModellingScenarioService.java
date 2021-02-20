package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:38
*/

import efd.rest.dto.ModellingscenarioDTO;

import java.util.List;


public interface ModellingScenarioService {
    List<ModellingscenarioDTO> getAllModellingScenarios();

    ModellingscenarioDTO getModellingScenarioByTitle(String title);


}
