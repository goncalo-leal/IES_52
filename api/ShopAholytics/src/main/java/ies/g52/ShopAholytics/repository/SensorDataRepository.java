package ies.g52.ShopAholytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ies.g52.ShopAholytics.models.SensorData;

public interface SensorDataRepository extends JpaRepository<SensorData,Integer> {
    
}
