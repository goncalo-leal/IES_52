package ies.g52.ShopAholytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.User;

public interface UserRepository  extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    
}
