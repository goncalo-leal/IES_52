

package ies.g52.ShopAholytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.Park;

public interface ParkRepository  extends JpaRepository<Park,Integer> {
    
    Park findByName(String name);
}