package ies.g52.ShopAholytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.Sensor;

public interface SensorRepository  extends JpaRepository<Sensor,Integer> {
    
    
}