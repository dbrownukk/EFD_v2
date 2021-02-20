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
        // http.authorizeRequests()
       // .antMatchers( "/v2/api-docs", "/swagger-resources/**", "/configuration/ui","/configuration/security", "/swagger-ui.html").permitAll()
               // TODO Get Authority/Role restriction working
              //  .antMatchers("/swagger-ui/")
            //  .antMatchers("/api/v1/**").hasAnyRole("API","ROLE_API")
                .antMatchers("/**").authenticated().and()
                .formLogin()
                .failureHandler(authenticationFailureHandler());
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new EfdUserDetails.PasswordEnconderEFD();
    }
}
