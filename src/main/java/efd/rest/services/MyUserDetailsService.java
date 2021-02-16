package efd.rest.services;
/*
    @Author david
    @Create 11/02/2021 09:24

    Populate UserDetails for Restful URL API security
    See MyUserDetails
*/

import efd.model.EfdUser;
import efd.rest.domain.MyUserDetails;
import efd.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        System.out.println("In Load UserByUserName = " + name);


        Optional<EfdUser> efdUser = userRepository.findByName((name));
        efdUser.orElseThrow(() -> new UsernameNotFoundException("Username " + name + "not found "));
        MyUserDetails myUserDetails = efdUser.map(MyUserDetails::new).get();

        System.out.println("myuserdetail user = " + myUserDetails.getUsername());
        System.out.println("myuserdetail password = " + myUserDetails.getPassword());



        return myUserDetails;

    }

}
