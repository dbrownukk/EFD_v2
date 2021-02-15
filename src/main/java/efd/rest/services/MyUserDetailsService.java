package efd.rest.services;
/*
    @Author david
    @Create 11/02/2021 09:24

    Populate UserDetails for Restful URL API security
    See MyUserDetails
*/

import efd.model.EfdRole;
import efd.model.EfdUser;
import efd.rest.domain.MyUserDetails;
import efd.rest.repositories.RoleRepository;
import efd.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.chop;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        System.out.println("In Load UserByUserName = " + name);


        Optional<EfdUser> efdUser = userRepository.findByName((name));
        efdUser.orElseThrow(() -> new UsernameNotFoundException("Username " + name + "not found "));
        MyUserDetails myUserDetails = efdUser.map(MyUserDetails::new).get();

        System.out.println("myuserdetail user = " + myUserDetails.getUsername());
        System.out.println("myuserdetail password = " + myUserDetails.getPassword());
        System.out.println("myuserdetail = Auth  " + myUserDetails.getAuthorities().toString());


        List<EfdRole> allRoles =
                roleRepository.findAll();

        List<EfdRole> efdRoles = allRoles.stream()
                .distinct()
                .filter(p -> p.getOxusersName().equals(myUserDetails.getUsername()))
                .collect(Collectors.toList());


        String roles = "";
        for (EfdRole efdRole : efdRoles) {
            System.out.println("role = " + efdRole.toString());
            roles += "\"" + efdRole.getRolesName() + "\",";
        }
        ;

        roles = chop(roles);

        myUserDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(roles)));

        return myUserDetails;

    }
}
