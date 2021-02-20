package efd.rest.client;

import efd.rest.dto.ModellingscenarioDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
    @Author david
    @Create 16/02/2021 19:02
*/
@Component
@ConfigurationProperties(value = "efd.host", ignoreUnknownFields = true)
public class EfdClient {
    public final String EFD_PATH_V1 = "/api/v1/";
    private String apihost;
    private final RestTemplate restTemplate;

    public EfdClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate=restTemplateBuilder.build();
    }
    public ModellingscenarioDTO getMSByTitle(String title){
        return restTemplate.getForObject(apihost+EFD_PATH_V1+"/modellingscenario/"+title,
                ModellingscenarioDTO.class);
    }


    public void setApihost(String apihost) {
        this.apihost = apihost;
    }
}
