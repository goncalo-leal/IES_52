package ies.g52.ShopAholytics.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.repository.UserRepository;
import ies.g52.ShopAholytics.auth.AuthConsts;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper().readValue(req.getInputStream(), User.class);
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
        String email = ((UserDetails) auth.getPrincipal()).getUsername();
        User u = userRepository.findByEmail(email);
        String token = JWT.create()
                        .withSubject(email)
                        .withExpiresAt(new Date(System.currentTimeMillis() + AuthConsts.EXPIRATION))
                        .sign(Algorithm.HMAC512(AuthConsts.SECRET.getBytes()));

        Map<String, Object> body = new HashMap<>();

        body.put("id", u.getId());
        body.put("email", u.getEmail());
        body.put("token", token);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(res.getWriter(), body);
    }
    
}
