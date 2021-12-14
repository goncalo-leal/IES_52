package ies.g52.ShopAholytics.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.ParkService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;



@RestController
@RequestMapping("/api/")
public class SensorParkController {
    @Autowired
    private SensorParkService SensorParkService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ParkService parkServices;


    @PostMapping("/addSensorPark/{pid}/{park}")
    public SensorPark newSensorPark(@PathVariable(value = "pid") int pid, @PathVariable(value = "park") int park) {
        Sensor s = sensorService.getSensorById(pid);
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
        return SensorParkService.saveSensorPark(new SensorPark (parkServices.getParkById(park),sensorService.getSensorById(pid)));
        }
        return null;
    }

    @GetMapping("/SensorParks")
    public List<SensorPark> findAllSensorPark() {
        List<SensorPark> a = SensorParkService.getSensorParks();
        return a;
    }
    @GetMapping("/SensorPark")
    public SensorPark findSensorParkById(@RequestParam(value = "id")  int id) {
        List<SensorPark> a = SensorParkService.getSensorParks();
        
        for (SensorPark qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    // não faz sentido update para este método
    //@PutMapping("/updateSensorPark/{pid}")
    //public SensorPark updateSensorPark(@PathVariable(value = "pid") int park) {
    //    return SensorParkService.updateSensorPark(SensorParkService.getSensorParkById(park));
    //}

    @DeleteMapping("/deleteSensorPark/{id}")
    public String deleteSensorPark(@PathVariable int id) {
        return SensorParkService.deleteSensorPark(id);
    }
}

