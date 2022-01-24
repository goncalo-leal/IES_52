package ies.g52.ShopAholytics.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.repository.UserRepository;
import ies.g52.ShopAholytics.services.ShoppingManagerService;
import ies.g52.ShopAholytics.services.StoreManagerService;
import ies.g52.ShopAholytics.auth.AuthConsts;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private StoreManagerService storeManagerService;
    private ShoppingManagerService shoppingManagerService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, StoreManagerService storeManagerService, ShoppingManagerService shoppingManagerService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.storeManagerService = storeManagerService;
        this.shoppingManagerService = shoppingManagerService;
        setFilterProcessesUrl(AuthConsts.LOG_IN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        System.out.println("bleh");
        try {
            User creds = new ObjectMapper().readValue(req.getInputStream(), User.class);
            List<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority(userRepository.findByEmail(creds.getEmail()).getAuthority()));
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), auths)
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
                        .withClaim(AuthConsts.JWT_ROLE_CLAIM, u.getAuthority())
                        .withSubject(email)
                        .withExpiresAt(new Date(System.currentTimeMillis() + AuthConsts.EXPIRATION))
                        .sign(Algorithm.HMAC512(AuthConsts.SECRET.getBytes()));

        Map<String, Object> body = new HashMap<>();
        Map<String, Object> userDTO = new HashMap<>();
        Map<String, Object> workDTO = new HashMap<>();

        userDTO.put("id", u.getId());
        userDTO.put("authority", u.getAuthority());
        userDTO.put("name", u.getName());


        StoreManager user = storeManagerService.getStoreManagerById(u.getId());
        if ( user == null) {
            workDTO.put("id", shoppingManagerService.getShoppingManagerById(u.getId()).getShopping().getId());
            body.put("shopping", workDTO);
        } else {
            workDTO.put("id", user.getStore().getId());
            body.put("store", workDTO);
        }

        body.put("user", userDTO);
        body.put("token", token);

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(res.getWriter(), body);
    }
    
}
