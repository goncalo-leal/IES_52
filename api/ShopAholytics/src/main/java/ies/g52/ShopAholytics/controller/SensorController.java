package ies.g52.ShopAholytics.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.services.SensorService;


@RestController
@RequestMapping("/api/")
public class SensorController {
    @Autowired
    private SensorService SensorServices;


    @PostMapping("/addSensor")
    public Sensor newSensor( @RequestBody Sensor s) {
        if (s.getType().equalsIgnoreCase(SensorEnum.ENTRACE.toString())){
            return SensorServices.saveSensor(new Sensor (s.getType(),s.getName()));
        }
        else if(s.getType().equalsIgnoreCase(SensorEnum.EXIT.toString())){
            return SensorServices.saveSensor(new Sensor (s.getType(),s.getName()));
        }
        return null;
        
    }

    @GetMapping("/Sensors")
    public List<Sensor> findAllSensor() {
        List<Sensor> a = SensorServices.getSensor();
        return a;
    }
    @GetMapping("/Sensor")
    public Sensor findSensorById(@RequestParam(value = "id")  int id) {
        List<Sensor> a = SensorServices.getSensor();
        
        for (Sensor qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    @PutMapping("/updateSensor")
    public Sensor updateSensor(@RequestBody Sensor user) {
        return SensorServices.updateSensor(user);
    }

    @DeleteMapping("/deleteSensor/{id}")
    public String deleteSensor(@PathVariable int id) {
        return SensorServices.deleteSensor(id);
    }
}
