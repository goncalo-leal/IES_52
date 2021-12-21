package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.repository.SensorStoreRepository;

@Service
public class SensorStoreService {
    @Autowired
    private SensorStoreRepository repository;

    public SensorStore saveSensorStore(SensorStore SensorStore) {
        return repository.save(SensorStore);
    }

    public List<SensorStore> saveSensorStore(List<SensorStore> SensorStore) {
        return repository.saveAll(SensorStore);
    }

    public List<SensorStore> getSensorStores() {
        return repository.findAll();
    }

    public SensorStore getSensorStoreById(int id) {
        return repository.findById((int)id).orElse(null);
    }

   
    public String deleteSensorStore(int id) {
        repository.deleteById(id);
        return "SensorStore removed !! " + id;
    }

    public SensorStore updateSensorStore(SensorStore SensorStore) {
        SensorStore existingSensorStore = repository.findById((int)SensorStore.getId()).orElse(null);
        return repository.save(existingSensorStore);
    }

   
}
