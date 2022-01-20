package ies.g52.ShopAholytics.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ies.g52.ShopAholytics.repository.UserRepository;


@EnableWebSecurity
@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

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
                .antMatchers(AuthConsts.SHOPPING_MANAGER_PROTECTED_ENDPOINTS).hasAuthority(AuthConsts.SHOPPING_MANAGER)
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

                .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(), this.userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                    .allowedHeaders("*")
                    .allowedOrigins("*")
                    .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                    .maxAge(168000)
                    .allowCredentials(true);
    }
}
