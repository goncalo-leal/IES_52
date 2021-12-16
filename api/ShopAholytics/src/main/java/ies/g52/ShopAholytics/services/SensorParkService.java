package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.repository.SensorParkRepository;

@Service
public class SensorParkService {
    @Autowired
    private SensorParkRepository repository;

    public SensorPark saveSensorPark(SensorPark SensorPark) {
        return repository.save(SensorPark);
    }

    public List<SensorPark> saveSensorPark(List<SensorPark> SensorPark) {
        return repository.saveAll(SensorPark);
    }

    public List<SensorPark> getSensorParks() {
        return repository.findAll();
    }

    public SensorPark getSensorParkById(int id) {
        return repository.findById((int)id).orElse(null);
    }

   
    public String deleteSensorPark(int id) {
        repository.deleteById(id);
        return "SensorPark removed !! " + id;
    }

    public SensorPark updateSensorPark(SensorPark SensorPark) {
        SensorPark existingSensorPark = repository.findById((int)SensorPark.getId()).orElse(null);
        existingSensorPark.setSensor(SensorPark.getSensor());
        existingSensorPark.setPark(SensorPark.getPark());
        return repository.save(existingSensorPark);
    }

   
}
