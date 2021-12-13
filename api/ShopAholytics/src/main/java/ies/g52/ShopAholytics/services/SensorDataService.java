package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.repository.SensorDataRepository;

@Service
public class SensorDataService {
    @Autowired
    private SensorDataRepository repository;

    public SensorData saveSensorData(SensorData sensorData) {
        return repository.save(sensorData);
    }

    public List<SensorData> saveSensorDatas(List<SensorData> sensorDatas) {
        return repository.saveAll(sensorDatas);
    }

    public List<SensorData> getSensorDatas() {
        return repository.findAll();
    }

    public SensorData getSensorDataById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public String deleteSensorData(int id) {
        repository.deleteById(id);
        return "SensorData removed !! " + id;
    }

    public SensorData updateSensorData(SensorData sensorData) {
        SensorData existingShoppingManager = repository.findById((int)sensorData.getId()).orElse(null);
        return repository.save(existingShoppingManager);
    }
}
