package efd.rest.services;
/*
    @Author david
    @Create 11/02/2021 09:24

    Populate UserDetails for Restful URL API security
    See MyUserDetails
*/

import efd.model.EfdUser;
import efd.rest.domain.EfdUserDetails;
import efd.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class EfdUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {


        System.out.println("In Load UserByUserName = " + name);


        Optional<EfdUser> efdUser = userRepository.findByName((name));
        System.out.println("done findbyname name now = "+efdUser.get().getName());


        efdUser.orElseThrow(() -> new UsernameNotFoundException(" THROW Username " + name + "not found "));
        EfdUserDetails myUserDetails = efdUser.map(EfdUserDetails::new).get();

       System.out.println("myuserdetail user = " + myUserDetails.getUsername());
       System.out.println("myuserdetail password = " + myUserDetails.getPassword());
       System.out.println("myuserdetail role = " + myUserDetails.getAuthorities().toString());

        return myUserDetails;

    }

}
