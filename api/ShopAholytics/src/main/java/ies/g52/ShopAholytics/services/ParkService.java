package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.repository.ParkRepository;

@Service
public class ParkService {
    @Autowired
    private ParkRepository repository;

    public Park savePark(Park Park) {
        return repository.save(Park);
    }

    public List<Park> savePark(List<Park> Park) {
        return repository.saveAll(Park);
    }

    public List<Park> getParks() {
        return repository.findAll();
    }

    public Park getParkById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public Park getParkByName(String email) {
        return repository.findByName(email);
    }

    public String deletePark(int id) {
        repository.deleteById(id);
        return "Park removed !! " + id;
    }

    public Park updatePark(Park Park) {
        Park existingPark = repository.findById((int)Park.getId()).orElse(null);
        existingPark.setName(Park.getName());
        existingPark.setLocation(Park.getLocation());
        existingPark.setCapacity(Park.getCapacity());
        existingPark.setOpening(Park.getOpening());
        existingPark.setClosing(Park.getClosing());
        return repository.save(existingPark);
    }

   
}
