package ies.g52.ShopAholytics.controller;

import java.util.ArrayList;
import java.util.List;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.models.SensorStore;
//import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.services.StoreService;
//import ies.g52.ShopAholytics.services.UserService;



@RestController
@RequestMapping("/api/sensorsstore")
public class SensorStoreController {
    @Autowired
    private SensorStoreService SensorStoreServices;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private StoreService storeService;


    @PostMapping("/addSensorStore/{pid}/{store}")
    public SensorStore newSensorStore(@PathVariable(value = "pid") int pid, @PathVariable(value = "store") int store) {
        Sensor s = sensorService.getSensorById(pid);
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
            return SensorStoreServices.saveSensorStore(new SensorStore (storeService.getStoreById(store),sensorService.getSensorById(pid)));

        }
        return null;
    }

    @PostMapping("/entranceStore/{id_sensor}")
    public Store entranceStore(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorStore sensor = SensorStoreServices.getSensorStoreById(id_sensor);
        Store store = storeService.getStoreById(sensor.getStore().getId());

        store.setCurrent_capacity(store.getCurrent_capacity()+1);
        storeService.updateStore(store);

        return store;
    }

    @PostMapping("/exitStore/{id_sensor}")
    public Store exitStore(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorStore sensor = SensorStoreServices.getSensorStoreById(id_sensor);
        Store store = storeService.getStoreById(sensor.getStore().getId());

        store.setCurrent_capacity(store.getCurrent_capacity()-1);
        storeService.updateStore(store);

        return store;
    }
    @PostMapping("/addSensorStore/{store}")
    public SensorStore newSensorStoreWithNewSensor(@PathVariable(value = "store") int store, @RequestBody Sensor s1) {
        
        if (s1.getType().equals("entrace") || s1.getType().equals("exit")){}
        else{
            return null;
        }
        Sensor s = new Sensor(s1.getType(), s1.getName());

       
        sensorService.saveSensor(s);
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
            return SensorStoreServices.saveSensorStore(new SensorStore (storeService.getStoreById(store),s));

        }
        
        return null;
    }

    @GetMapping("/SensorStores/{pid}")
    public List<Sensor> findSensorByStore(@PathVariable(value = "pid") int pid) {
        List<SensorStore> a = SensorStoreServices.getSensorStores();
        Store store = storeService.getStoreById(pid);
        List<Sensor> ret= new ArrayList<>();
        for (SensorStore qu: a){
            if (qu.getStore().getId() == store.getId() ){
                ret.add(qu.getSensor());
            }
        }
        return ret;
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

    // Não faz sentido dar update deste método
    //@PutMapping("/updateSensorStore")
    //public SensorStore updateSensorStore(@RequestBody SensorStore user) {
    //    return SensorStoreServices.updateSensorStore(user);
    //}

    @DeleteMapping("/deleteSensorStore/{id}")
    public String deleteSensorStore(@PathVariable int id) {
        return SensorStoreServices.deleteSensorStore(id);
    }
}

