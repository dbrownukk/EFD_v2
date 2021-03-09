package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:40
*/

import efd.rest.dto.LivelihoodZoneDTO;
import efd.rest.mapper.LivelihoodZoneMapper;
import efd.rest.repositories.LivelihoodZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LivelihoodZoneServiceImpl implements LivelihoodZoneService {

    private final LivelihoodZoneMapper livelihoodZoneMapper;
    private final LivelihoodZoneRepository livelihoodZoneRepository;

    public LivelihoodZoneServiceImpl(LivelihoodZoneMapper livelihoodZoneMapper, LivelihoodZoneRepository livelihoodZoneRepository) {
        this.livelihoodZoneMapper = livelihoodZoneMapper;
        this.livelihoodZoneRepository = livelihoodZoneRepository;
    }


    @Override
    public List<LivelihoodZoneDTO> getAlLivelihoodZoneDtos() {
        return livelihoodZoneRepository.findAll()
                .stream()
                .map(livelihoodZoneMapper::lzTolzDto)
                .collect(Collectors.toList());
    }

    @Override
    public LivelihoodZoneDTO getModellingScenarioByName(String name) {
        return livelihoodZoneMapper.lzTolzDto(
                livelihoodZoneRepository.findByLzname(name));
    }
}
