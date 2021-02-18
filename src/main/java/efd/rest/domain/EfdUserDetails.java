package efd.rest.domain;
/*
    @Author david
    @Create 11/02/2021 09:26

    Authority done in MyUserDetailsService

*/

import com.openxava.naviox.model.Role;
import efd.model.EfdUser;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Setter
@Service
public class EfdUserDetails implements UserDetails {


    Collection<Role> roles;
    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    String sroles=""; // String of roles
    public EfdUserDetails() {
    }

    public EfdUserDetails(EfdUser efduser) {




        this.userName = efduser.getName();
        this.password = efduser.getPassword();
        this.active = efduser.isActive();
        roles = efduser.getRoles();

        for (Role role : roles) {
            System.out.println("roles in myuserdetails = " + role.getName());
        }

        Function<String,String> addQuotes = s -> "\"" + s + "\"";

        sroles = roles.stream()
                .map(s -> "\"" + s.getName() + "\"")
                .collect(Collectors.joining(", "));



        // PasswordEncoder encoder = createDelegatingPasswordEncoder();
        PasswordEncoder encoder = new PasswordEnconderEFD();

        this.password = efduser.getPassword();

    }

    /* Password Encoder to match OpenXava User Encrypt */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Arrays.asList(new SimpleGrantedAuthority(sroles));
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

    public static class PasswordEnconderEFD implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            System.out.println("passwordencodeefd input = " + charSequence);
            byte[] encrypted = new byte[0];
            String source = (String) charSequence;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte[] bytes = source.getBytes();
                md.update(bytes);
                encrypted = md.digest();

                // return new BigInteger(encrypted).toString(16);

            } catch (Exception ex) {
                // log.error(XavaResources.getString("encrypting_password_problem"), ex);
                // throw new RuntimeException(XavaResources.getString("encrypting_password_problem"), ex);
                System.out.println("passwordencoderEFD throw " + ex);
            }
            return new BigInteger(encrypted).toString(16);
            // return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String source) {

            String encoded = encode(charSequence);



            return encoded.equals(source);
            // return encrypted.toString().equals(source);
        }
    }

}

