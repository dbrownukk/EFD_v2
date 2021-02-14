package efd.rest.controllers.v1;

import efd.model.EfdRole;
import efd.rest.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
    @Author david
    @Create 12/02/2021 16:37
*/
@RestController
public class UserController {

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping("/api/v1/roles")
    @ResponseBody
    List<EfdRole> all() {
        return roleRepository.findAll();
    }

}
