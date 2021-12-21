package ies.g52.ShopAholytics.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.services.SensorDataService;
import ies.g52.ShopAholytics.services.SensorService;


@RestController
@RequestMapping("/api/")
public class SensorDataController {
    
    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private SensorService sensorService;

    @PostMapping("/addSensorData/{pid}")
    public SensorData newSensorData(@PathVariable(value = "pid") int pid, @RequestBody SensorData s) {
        return sensorDataService.saveSensorData(new SensorData(s.getData(),sensorService.getSensorById(pid)));
    }

    @GetMapping("/SensorsDatas")
    public List<SensorData> findAllSensorsData() {
        List<SensorData> a = sensorDataService.getSensorDatas();
        return a;
    }

    @GetMapping("/SensorsData")
    public SensorData findSensorDataById(@RequestParam(value = "id")  int id) {
        List<SensorData> a = sensorDataService.getSensorDatas();
        
        for (SensorData qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    // Não faz sentido dar update dos dados
    //@PutMapping("/updateSensorData")
    //public SensorData updateSensorData(@RequestBody SensorData sensor) {
    //    return sensorDataService.updateSensorData(sensor);
    //}

    @DeleteMapping("/deleteSensorData/{id}")
    public String deleteSensorData(@PathVariable int id) {
        return sensorDataService.deleteSensorData(id);
    }
}
