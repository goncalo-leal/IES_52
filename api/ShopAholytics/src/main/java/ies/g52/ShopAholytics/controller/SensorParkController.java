package ies.g52.ShopAholytics.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.ParkService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;



@RestController
@RequestMapping("/api/sensorspark")
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

    @PostMapping("/entrancePark/{id_sensor}")
    public Park entranceStore(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorPark sensor = SensorParkService.getSensorParkById(id_sensor);
        Park park = parkServices.getParkById(sensor.getPark().getId());

        park.setCurrent_capacity(park.getCurrent_capacity()+1);
        parkServices.updatePark(park);
        return park;
    }

    @PostMapping("/exitPark/{id_sensor}")
    public Park exitPark(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorPark sensor = SensorParkService.getSensorParkById(id_sensor);
        Park park = parkServices.getParkById(sensor.getPark().getId());

        park.setCurrent_capacity(park.getCurrent_capacity()-1);
        parkServices.updatePark(park);

        return park;
    }
    @PostMapping("/addSensorPark/{park}")
    public SensorPark newSensorParkWithNewSensor(@PathVariable(value = "park") int park, @RequestBody Sensor s1) {
        
        if (s1.getType().equalsIgnoreCase("entrance") || s1.getType().equalsIgnoreCase("exit")){}
        else{
            return null;
        }
        Sensor s = new Sensor(s1.getType(), s1.getName());
        
        sensorService.saveSensor(s);
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
            return SensorParkService.saveSensorPark(new SensorPark(parkServices.getParkById(park), s));
        }
        return null;
    }


    @GetMapping("/SensorParks/{pid}")
    public List<Sensor> findSensorByStore(@PathVariable(value = "pid") int pid) {
        List<SensorPark> a = SensorParkService.getSensorParks();
        Park park = parkServices.getParkById(pid);
        List<Sensor> ret= new ArrayList<>();
        for (SensorPark qu: a){
            if (qu.getPark().getId() == park.getId() ){
                ret.add(qu.getSensor());
            }
        }
        return ret;
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

