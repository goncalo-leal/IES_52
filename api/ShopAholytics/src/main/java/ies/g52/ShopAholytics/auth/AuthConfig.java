package ies.g52.ShopAholytics.auth;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ies.g52.ShopAholytics.auth.UserDetailsServiceImpl;
import ies.g52.ShopAholytics.repository.UserRepository;


@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    public AuthConfig(UserDetailsServiceImpl userDetailsServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, AuthConsts.LOG_IN_URL).permitAll()
                .antMatchers(AuthConsts.SHOPPING_MANAGER_PROTECTED_ENDPOINTS).hasAuthority("ROLE_SHOPPING_MANAGER")
                .anyRequest().authenticated()
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(
                    (req, res, ex) -> {
                        Map<String, Object> body = new HashMap<>();
                        body.put("message", "Access denied");
                        body.put("timestamp", new Date().toString());
                        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);

                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        new ObjectMapper().writeValue(res.getWriter(), body);
                    } 
                )
                .accessDeniedHandler(
                    (req, res, ex) -> {
                        Map<String, Object> body = new HashMap<>();
                        body.put("message", "Forbidden resource");
                        body.put("timestamp", new Date().toString());
                        body.put("status", HttpServletResponse.SC_FORBIDDEN);
                                        
                        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        new ObjectMapper().writeValue(res.getWriter(), body);
                    }
                )
                .and()


                .addFilter(new JWTAuthenticationFilter(authenticationManager(), this.userRepository))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

}
