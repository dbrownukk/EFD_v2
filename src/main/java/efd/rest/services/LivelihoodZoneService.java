package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:38
*/

import efd.rest.dto.LivelihoodZoneDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LivelihoodZoneService {
    List<LivelihoodZoneDTO> getAlLivelihoodZoneDtos();

    LivelihoodZoneDTO getModellingScenarioByName(String name);


}
