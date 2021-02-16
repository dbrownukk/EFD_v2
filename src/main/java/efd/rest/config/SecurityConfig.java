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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/*
    @Author david
    @Create 10/02/2021 12:05
*/

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userEFDDetailsService;
    private Object pw;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("In auth config userdetails service ");
        auth.userDetailsService(userEFDDetailsService);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/api/v1/**").hasRole("API")
                .and().formLogin()
         .failureHandler(authenticationFailureHandler());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new MyUserDetails.PasswordEnconderEFD();
    }
}
