package efd.rest.services;
/*
    @Author david
    @Create 17/02/2021 13:40
*/

import efd.rest.dto.ModellingScenarioDto;
import efd.rest.mapper.ModellingScenarioMapper;
import efd.rest.repositories.ModellingScenarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModellingScenarioServiceImpl implements ModellingScenarioService {

    private final ModellingScenarioMapper modellingScenarioMapper;
    private final ModellingScenarioRepository modellingScenarioRepository;

    public ModellingScenarioServiceImpl(ModellingScenarioMapper modellingScenarioMapper, ModellingScenarioRepository modellingScenarioRepository) {
        this.modellingScenarioMapper = modellingScenarioMapper;
        this.modellingScenarioRepository = modellingScenarioRepository;
    }

    @Override
    @Transactional
    public List<ModellingScenarioDto> getAllModellingScenarios() {
        return modellingScenarioRepository.findAll()
                .stream()
                .map(modellingScenarioMapper::modellingScenarioToModellingScenarionDto)
                .collect(Collectors.toList());


    }

    @Override
    public ModellingScenarioDto getModellingScenarioByTitle(String title) {
        log.debug("============= In Service Implementation title = " + title);

        return modellingScenarioMapper.modellingScenarioToModellingScenarionDto(
                modellingScenarioRepository.findByTitle(title));


    }


}
