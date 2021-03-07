package efd.rest.controllers.v1;

/*
    @Author david
    @Create 28/02/2021 21:06
*/

import efd.rest.services.ModellingReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/modellingreports")
class ModellingReportsController {


    private final ModellingReportsService modellingReportsService;

    @GetMapping
    public String errMessage() {
        return ("Provide Modelling Scenario Title in URL to run Modelling Report");
    }

    // Run ModellingReport for a scenario
    @GetMapping({"/{title}"})
    public ResponseEntity<byte[]> // TODO Change to return XLS
    getRunReportByModScenarioTitle(@PathVariable String title) throws Exception {
        // return modellingReportsService.runModReports(title);

        System.out.println("doing headers");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=NAMEOFYOURFILE.xls");
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        //byte[] bytes = modellingReportsService.runModReports(title);

        byte[] media = modellingReportsService.runModReports(title);

      //  InputStream in = servletContext.getResourceAsStream(report);



        ResponseEntity<byte[]> responseEntity = responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);



        return responseEntity;
    }


}
