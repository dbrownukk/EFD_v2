package efd.rest.controllers.v1;

import efd.rest.dto.LivelihoodZoneDTO;
import efd.rest.dto.LivelihoodZoneListDTO;
import efd.rest.services.LivelihoodZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    @Author david
    @Create 16/02/2021 16:18
*/


@Slf4j
@RestController
@RequestMapping("/api/v1/livelihoodzone")
public class LivelihoodZoneController {

    private final LivelihoodZoneService livelihoodZoneService;
    private String name;

    public LivelihoodZoneController(LivelihoodZoneService livelihoodZoneService) {
        this.livelihoodZoneService = livelihoodZoneService;

    }

    @GetMapping({"/{name}"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LivelihoodZoneDTO>
        getLZbyName(@PathVariable String name){
        this.name=name;


        return new ResponseEntity<LivelihoodZoneDTO>
                (livelihoodZoneService.getModellingScenarioByName(name), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<LivelihoodZoneListDTO> getListofLZs()
    {
        return new ResponseEntity<LivelihoodZoneListDTO>(
                new LivelihoodZoneListDTO(livelihoodZoneService.getAlLivelihoodZoneDtos()),
                HttpStatus.OK);
    }


}
