package efd.rest.services;

import efd.model.ModellingScenario;
import efd.rest.mapper.ModellingScenarioMapper;
import efd.rest.repositories.ModellingScenarioRepository;
import lombok.val;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/*
    @Author david
    @Create 17/02/2021 13:43
*/
@SpringBootTest
class ModellingScenarioServiceTest {
    ModellingScenarioService modellingScenarioService;

    @Mock
    ModellingScenarioRepository modellingScenarioRepository;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);

        modellingScenarioService =
                new ModellingScenarioServiceImpl(ModellingScenarioMapper.INSTANCE,
                        modellingScenarioRepository);
    }

    @Test
    void getAllModellingScenarios() throws Exception{
        //given
        List<ModellingScenario> modellingScenarios= Arrays.asList(new ModellingScenario(),new ModellingScenario(),new ModellingScenario());
        when(modellingScenarioRepository.findAll()).thenReturn(modellingScenarios);

        //when
        val msDTOS = modellingScenarioService.getAllModellingScenarios();

        //then
        Assertions.assertEquals(3, msDTOS.size());

    }


}