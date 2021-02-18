package efd.rest.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import efd.rest.domain.ModellingScenarioDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    @Author david
    @Create 16/02/2021 16:36
*/
//@WebMvcTest(ModellingScenarioController.class)
@SpringBootTest
class ModellingScenarioControllerTest extends BaseIT{

    private static final String TITLE = "MicroNutrients";


    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getModellingScenarioDtoByName() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/modellingscenario/"+TITLE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @SneakyThrows
    @Test
    void saveNewModellingScenario() {
        ModellingScenarioDto modellingScenarioDto = ModellingScenarioDto.builder().build();
        String modellingScenarioToJson = objectMapper.writeValueAsString(modellingScenarioDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/modellingscenario/")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(modellingScenarioToJson))
                .andExpect(status().isCreated());


    }

    @SneakyThrows
    @Test
    void updateModellingScenario() {
        ModellingScenarioDto modellingScenarioDto = ModellingScenarioDto.builder().build();
        String modellingScenarioToJson = objectMapper.writeValueAsString(modellingScenarioDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/modellingscenario/"+TITLE)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(modellingScenarioToJson))
                .andExpect(status().isCreated());
    }
}