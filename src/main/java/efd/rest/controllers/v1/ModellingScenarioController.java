package efd.rest.controllers.v1;

import efd.rest.domain.ModellingScenarioDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    @Author david
    @Create 16/02/2021 16:18
*/
@RestController
@RequestMapping("/api/v1/modellingscenario")
public class ModellingScenarioController {

    @GetMapping("/{title}")
    public ResponseEntity<ModellingScenarioDto>
        getModellingScenarioDtoByName(@PathVariable("title") String title){
        // TODO Impl
        return new ResponseEntity<ModellingScenarioDto>
                (ModellingScenarioDto.builder().build(), HttpStatus.OK);
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
