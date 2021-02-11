package efd.rest.services;
/*
    @Author david
    @Create 11/02/2021 09:24
*/

import com.openxava.naviox.model.User;
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
        Optional<User> user = userRepository.findByName((name));
        user.orElseThrow(() -> new UsernameNotFoundException("Username "+name+"not found "));
        return user.map(MyUserDetails::new).get();

    }
}
