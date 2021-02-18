package efd.rest.client;

import efd.rest.domain.ModellingScenarioDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/*
    @Author david
    @Create 16/02/2021 19:17
*/
@SpringBootTest
class EfdClientTest {

    @Autowired
    EfdClient client;

    @Test
    void getMSByTitle() {
        ModellingScenarioDto dto = client.getMSByTitle("Malawi north");
        assertNotNull(dto);
    }
}