package efd.rest.mapper;

import efd.model.ModellingScenario;
import efd.rest.dto.ModellingScenarioDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/*
    @Author david
    @Create 17/02/2021 15:06
*/
@Slf4j
public class ModellingScenarioMapperTest {

    ModellingScenarioMapper modellingScenarioMapper = ModellingScenarioMapper.INSTANCE;
    @Test
    public void msTomsDto() throws Exception
    {
        log.debug("============================= In Mapper Test ");
        //given
        ModellingScenario ms = new ModellingScenario();
        ms.setTitle("a new modelling scenario");
        ms.setAuthor("david");
        ms.setDescription("a modelling scenario");


        ModellingScenarioDto msDto = modellingScenarioMapper
                .modellingScenarioToModellingScenarionDto(ms);

        // then

        Assertions.assertEquals("david",msDto.getAuthor());
    }


}