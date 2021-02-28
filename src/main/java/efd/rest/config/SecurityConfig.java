package efd.rest.config;

import efd.rest.domain.EfdUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/*
    @Author david
    @Create 10/02/2021 12:05
*/
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    protected void configure(HttpSecurity http) throws Exception {
       http.httpBasic().and().authorizeRequests()
               .antMatchers("/**").authenticated().and()
       // TODO Get Authority/Role restriction working
                .formLogin()
                .failureHandler(authenticationFailureHandler());
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        System.out.println("get encoded password in Security Config");
        return new EfdUserDetails.PasswordEncoderEfd();
    }
}
