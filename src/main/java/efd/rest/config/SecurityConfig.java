package efd.rest.config;

import efd.rest.domain.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
    @Author david
    @Create 10/02/2021 12:05
*/

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;
    private Object pw;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("In auth config userdetails service ");
        auth.userDetailsService(userDetailsService);
    }




    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").authenticated()
                .antMatchers("/**").hasRole("ohea_user")
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
       // String encodingId = "scrypt";
       // Map<String, PasswordEncoder> encoders = new HashMap<>();
       // encoders.put(encodingId, new SCryptPasswordEncoder());
       // encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
       // return new DelegatingPasswordEncoder(encodingId, encoders);
      //  return(new MessageDigestPasswordEncoder("SHA-1"));
    return new MyUserDetails.PasswordEnconderEFD();
    }
}
