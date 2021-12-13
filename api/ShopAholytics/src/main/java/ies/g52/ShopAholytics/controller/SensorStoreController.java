package ies.g52.ShopAholytics.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;



@RestController
@RequestMapping("/api/")
public class SensorStoreController {
    @Autowired
    private SensorStoreService SensorStoreServices;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private StoreService storeService;


    @PostMapping("/addSensorStore/{pid}/{store}")
    public SensorStore newSensorStore(@PathVariable(value = "pid") int pid, @PathVariable(value = "store") int store) {
        return SensorStoreServices.saveSensorStore(new SensorStore (storeService.getStoreById(store),sensorService.getSensorById(pid)));
    }

    @GetMapping("/SensorStores")
    public List<SensorStore> findAllSensorStore() {
        List<SensorStore> a = SensorStoreServices.getSensorStores();
        return a;
    }
    @GetMapping("/SensorStore")
    public SensorStore findSensorStoreById(@RequestParam(value = "id")  int id) {
        List<SensorStore> a = SensorStoreServices.getSensorStores();
        
        for (SensorStore qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    @PutMapping("/updateSensorStore")
    public SensorStore updateSensorStore(@RequestBody SensorStore user) {
        return SensorStoreServices.updateSensorStore(user);
    }

    @DeleteMapping("/deleteSensorStore/{id}")
    public String deleteSensorStore(@PathVariable int id) {
        return SensorStoreServices.deleteSensorStore(id);
    }
}

