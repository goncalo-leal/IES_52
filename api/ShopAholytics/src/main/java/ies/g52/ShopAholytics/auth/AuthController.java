package ies.g52.ShopAholytics.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies.g52.ShopAholytics.models.ShoppingManager;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.repository.ShoppingManagerRepository;
import ies.g52.ShopAholytics.repository.UserRepository;

@RestController
@RequestMapping("/auth/")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingManagerRepository shoppingManagerRepository;

    @PostMapping("/login")
    public ShoppingManager login(@RequestParam(value="email") String email, @RequestParam(value="password") String password) throws Exception {
        User u = userRepository.findByEmail(email);
        if (u != null) {
            if (u.getPassword().equals(password)) {
                ShoppingManager s = shoppingManagerRepository.findById(u.getId()).orElse(null);
                System.out.println(s);
                return s;
            }
        }

        return null;
    }

}