package efd.rest.domain;
/*
    @Author david
    @Create 11/02/2021 09:26
*/

import com.openxava.naviox.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public MyUserDetails() {
    }

    public MyUserDetails(User user) {
        this.userName = user.getName();
        this.password = user.getPassword();
        this.authorities = Arrays.stream(user.getRoles().toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.active = user.isActive();

        System.out.println("in MyUserDetails "+user.getName());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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

