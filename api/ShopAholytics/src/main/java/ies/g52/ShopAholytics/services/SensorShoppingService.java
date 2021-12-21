package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.repository.SensorShoppingRepository;

@Service
public class SensorShoppingService {
    @Autowired
    private SensorShoppingRepository repository;

    public SensorShopping saveSensorShopping(SensorShopping SensorShopping) {
        return repository.save(SensorShopping);
    }

    public List<SensorShopping> saveSensorShopping(List<SensorShopping> SensorShopping) {
        return repository.saveAll(SensorShopping);
    }

    public List<SensorShopping> getSensorShoppings() {
        return repository.findAll();
    }

    public SensorShopping getSensorShoppingById(int id) {
        return repository.findById((int)id).orElse(null);
    }

   
    public String deleteSensorShopping(int id) {
        repository.deleteById(id);
        return "SensorShopping removed !! " + id;
    }

    public SensorShopping updateSensorShopping(SensorShopping SensorShopping) {
        SensorShopping existingSensorShopping = repository.findById((int)SensorShopping.getId()).orElse(null);
        return repository.save(existingSensorShopping);
    }

   
}
