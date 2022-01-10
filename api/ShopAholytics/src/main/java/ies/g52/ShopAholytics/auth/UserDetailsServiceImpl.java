package ies.g52.ShopAholytics.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            List<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority(user.getAuthority()));
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), auths);
        } else {
            throw new UsernameNotFoundException("User with email " +  email + " not found.");
        }
    }
}
