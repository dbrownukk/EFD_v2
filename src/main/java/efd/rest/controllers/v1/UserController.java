package efd.rest.controllers.v1;

import efd.model.EfdRole;
import efd.model.EfdUser;
import efd.rest.repositories.RoleRepository;
import efd.rest.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/v1/roles")
    @ResponseBody
    List<EfdRole> roleAll() {
        return roleRepository.findAll();
    }

    @GetMapping("/api/v1/users")
    @ResponseBody
    List<EfdUser> userAll() {
        return userRepository.findAll();
    }

}
