package efd.rest.controllers.v1;

import efd.rest.dto.ModellingScenarioDto;
import efd.rest.dto.ModellingScenarioListDto;
import efd.rest.services.ModellingScenarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    @Author david
    @Create 16/02/2021 16:18
*/
@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/api/v1/modellingscenario")
public class ModellingScenarioController {

    private final ModellingScenarioService modellingScenarioService;
    private String title;

    public ModellingScenarioController(ModellingScenarioService modellingScenarioService) {
        this.modellingScenarioService = modellingScenarioService;
    }

    @GetMapping({"/{title}"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ModellingScenarioDto>
        getModellingScenarioDtoByName(@PathVariable String title){
        this.title = title;
        log.debug("============ MS Title = "+title);

        return new ResponseEntity<ModellingScenarioDto>
                (modellingScenarioService.getModellingScenarioByTitle(title), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ModellingScenarioListDto> getListofModellingScenarios()
    {
        return new ResponseEntity<ModellingScenarioListDto>(
                new ModellingScenarioListDto(modellingScenarioService.getAllModellingScenarios()),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity
    saveNewModellingScenario(@RequestBody ModellingScenarioDto modellingScenarioDto){
        // TODO Impl
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{name}")
    public ResponseEntity updateModellingScenario(@PathVariable("name") String name,
                                                  @RequestBody ModellingScenarioDto modellingScenarioDto){
        // TODO Impl
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
