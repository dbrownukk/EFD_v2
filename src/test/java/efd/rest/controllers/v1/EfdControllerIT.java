package efd.rest.controllers.v1;

import efd.rest.repositories.RoleRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    @Author david
    @Create 17/02/2021 12:08
*/
@SpringBootTest
public class EfdControllerIT extends BaseIT{


    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void listRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/roles")
                .with(httpBasic("david", "d2k2000")))
                .andExpect(status().isOk());
    }



}



