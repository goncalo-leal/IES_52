package ies.g52.ShopAholytics.auth;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AuthConsts.HEADER_KEY);
        if (header == null || !header.startsWith(AuthConsts.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken token = this.getToken(header);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getToken(String header) {
        DecodedJWT token = JWT.require(Algorithm.HMAC512(AuthConsts.SECRET.getBytes()))
                        .build()
                        .verify(header.replace(AuthConsts.TOKEN_PREFIX, ""));

        String email = token.getSubject();
        String role = token.getClaim(AuthConsts.JWT_ROLE_CLAIM).as(String.class);
        ArrayList<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));

        if (email != null) {
            return new UsernamePasswordAuthenticationToken(email, token.getSignature(), roles);
        }

        return null;
    }
}
