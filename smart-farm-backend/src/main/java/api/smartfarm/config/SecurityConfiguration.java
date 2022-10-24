package api.smartfarm.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            //this is a hack for microcontroller, TODO: implement token update on ESP32
            .antMatchers(HttpMethod.POST, "/events/**").permitAll()
            .antMatchers(HttpMethod.POST, "/sensors/**").permitAll()
            .antMatchers(HttpMethod.GET).permitAll()
            //Authenticate all request
            .antMatchers("/**").fullyAuthenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .oauth2ResourceServer().jwt()
            .and()
            .and()
            .cors().and().csrf().disable();
    }
}
