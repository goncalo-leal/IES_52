package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.repository.SensorRepository;


@Service
public class SensorService {
    @Autowired
    private SensorRepository repository;

    public Sensor saveSensor(Sensor Sensor) {
        return repository.save(Sensor);
    }

    public List<Sensor> saveSensor(List<Sensor> Sensor) {
        return repository.saveAll(Sensor);
    }

    public List<Sensor> getSensor() {
        return repository.findAll();
    }

    public Sensor getSensorById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public String deleteSensor(int id) {
        repository.deleteById(id);
        return "Sensor removed !! " + id;
    }

    public Sensor updateSensor(Sensor Sensor) {
        Sensor existingSensor = repository.findById((int)Sensor.getId()).orElse(null);
        return repository.save(existingSensor);
    }

   
}
