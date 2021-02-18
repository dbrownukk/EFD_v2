package efd.rest.config;

import efd.rest.domain.EfdUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/*
    @Author david
    @Create 10/02/2021 12:05
*/
@Slf4j
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


   // @Qualifier("efdUserDetailsService")
   // @Autowired
   // UserDetailsService userEFDDetailsService;


    private Object pw;


  //  @Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //      log.debug("In AuthManagerBuilder  ");
  //      auth.userDetailsService(userEFDDetailsService);
  //  }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
               // TODO Get Authority/Role restriction working
                // .antMatchers("/api/v1/**").hasAnyAuthority("API","ROLE_API")
                .antMatchers("/**").authenticated()
                .and().formLogin()
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
