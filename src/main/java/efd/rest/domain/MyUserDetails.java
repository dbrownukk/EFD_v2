package efd.rest.domain;
/*
    @Author david
    @Create 11/02/2021 09:26

    Authority done in MyUserDetailsService

*/

import efd.model.EfdUser;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

@Setter
@Service
public class MyUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;


    public MyUserDetails() {
    }

    public MyUserDetails(EfdUser efduser) {


        System.out.println("in MyUserDetails " + efduser.getName());

        this.userName = efduser.getName();
        this.password = efduser.getPassword();
        this.active = efduser.isActive();

        System.out.println("password = " + password);


       // PasswordEncoder encoder = createDelegatingPasswordEncoder();
        PasswordEncoder encoder = new PasswordEnconderEFD();

        String result = encoder.encode("d2k2000");
        System.out.println("encoded pw sha = " + result);

    }

    /* Password Encoder to match OpenXava User Encrypt */

    public static class PasswordEnconderEFD implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            System.out.println("passwordencodeefd input = "+charSequence);
            byte[] encrypted = new byte[0];
            String source = (String) charSequence;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte[] bytes = source.getBytes();
                md.update(bytes);
                encrypted= md.digest();

               // return new BigInteger(encrypted).toString(16);

            }
            catch (Exception ex) {
                // log.error(XavaResources.getString("encrypting_password_problem"), ex);
                // throw new RuntimeException(XavaResources.getString("encrypting_password_problem"), ex);
                System.out.println("passwordencoderEFD throw "+ex);
            }
            return new BigInteger(encrypted).toString(16);
           // return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String source) {

                String encoded = encode(charSequence);

            System.out.println("encode = "+encoded);
            System.out.println("source = "+source);


            return encoded.equals(source);
            // return encrypted.toString().equals(source);
        }
    }

    @SuppressWarnings("deprecation")
    static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
        encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        return delegatingPasswordEncoder;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {


        return Arrays.asList(new SimpleGrantedAuthority("User"));
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

