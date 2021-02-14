package efd.rest.domain;
/*
    @Author david
    @Create 11/02/2021 09:26
*/

import efd.model.EfdUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Service
public class MyUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

   // @Autowired
   // private RoleRepository roleRepository;



    public MyUserDetails() {
    }

    public MyUserDetails(EfdUser efduser) {



        System.out.println("in MyUserDetails " + efduser.getName());

        this.userName = efduser.getName();
        this.password = efduser.getPassword();
        //this.authorities = Arrays.stream(user.getRoles().toString().split(","))
        //      .map(SimpleGrantedAuthority::new)
        //    .collect(Collectors.toList());
        this.active = efduser.isActive();



        // this.getAuthorities()= Arrays.asList(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority("User")});

        //List<EfdRole> allRoles = roleRepository.findAllByOxusersName(userName);

        //List<EfdRole> efdRoles = XPersistence.getManager().createQuery("from EfdRole").getResultList();
        //for (EfdRole efdRole : efdRoles) {
         //   System.out.println("roles = "+efdRole.getRolesName());
        //}
        //;

        //for (EfdRole allRole : allRoles) {
         //   System.out.println("roles = "+allRole.getRolesName());
        //}
        //;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        return Arrays.asList(new SimpleGrantedAuthority("User"));
    }

    @Override
    public String getPassword() {
        return "pass";
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

