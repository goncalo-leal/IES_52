package ies.g52.ShopAholytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.UserState;

public interface UserStateRepository  extends JpaRepository<UserState,Integer> {
    UserState findByDescription(String description);
    
}
