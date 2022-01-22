package ies.g52.ShopAholytics.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.views.SensorShoppingPark;



@RestController
@RequestMapping("/api/sensorsshopping")
public class SensorShoppingController {
    @Autowired
    private SensorShoppingService SensorShoppingServices;
    @Autowired
    private SensorService sensorService;

    @Autowired
    private ShoppingServices shoppingServices;


    @PostMapping("/addSensorShopping/{pid}/{shopping}")
    public SensorShopping newSensorShopping(@PathVariable(value = "pid") int pid, @PathVariable(value = "shopping") int shopping,@RequestBody SensorShoppingPark var) {
        Sensor s = sensorService.getSensorById(pid);
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
            
            return SensorShoppingServices.saveSensorShopping(new SensorShopping (shoppingServices.getShoppingById(shopping),sensorService.getSensorById(pid),var.isBool()));
         }
         return null;
    }

    @PostMapping("/entranceShopping/{id_sensor}")
    public Shopping entranceStore(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorShopping sensor = SensorShoppingServices.getSensorShoppingById(id_sensor);
        Shopping shop = shoppingServices.getShoppingById(sensor.getShopping().getId());

        shop.setCurrent_capacity(shop.getCurrent_capacity()+1);
        shoppingServices.updateShopping(shop);
        return shop;
    }

    @PostMapping("/exitShopping/{id_sensor}")
    public Shopping exitShopping(@PathVariable(value = "id_sensor") int id_sensor) {
        SensorShopping sensor = SensorShoppingServices.getSensorShoppingById(id_sensor);
        Shopping shop = shoppingServices.getShoppingById(sensor.getShopping().getId());

        shop.setCurrent_capacity(shop.getCurrent_capacity()-1);
        shoppingServices.updateShopping(shop);
        return shop;
    }
    @PostMapping("/addSensorShopping/{shopping}")
    public SensorShopping newSensorShoppingWithNewSensor(@PathVariable(value = "shopping") int shopping, @RequestBody SensorShoppingPark s1) {
        if (s1.getType().equalsIgnoreCase("entrace") || s1.getType().equalsIgnoreCase("exit")){}
        else{
            return null;
        }
        Sensor s = new Sensor(s1.getType(), s1.getName());
        
        sensorService.saveSensor(s);
        
        if (s.getSensorPark() == null && s.getSensorShopping()==null && s.getSensorStore()==null ){
            
            return SensorShoppingServices.saveSensorShopping(new SensorShopping (shoppingServices.getShoppingById(shopping),s,s1.isBool()));
         }
         return null;
    }

    @GetMapping("/SensorShoppings")
    public List<SensorShopping> findAllSensorShopping() {
        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        return a;
    }

    @GetMapping("/SensorShoppings/{pid}")
    public List<Sensor> findSensorByStore(@PathVariable(value = "pid") int pid) {
        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        Shopping shopping = shoppingServices.getShoppingById(pid);
        List<Sensor> ret= new ArrayList<>();
        for (SensorShopping qu: a){
            if (qu.getShopping().getId() == shopping.getId() ){
                ret.add(qu.getSensor());
            }
        }
        return ret;
    }
    
    /*@GetMapping("/getAllSensorShoppings/{pid}")
    public List<Sensor> findSensorByStore(@PathVariable(value = "pid") int pid) {
        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        Shopping shopping = shoppingServices.getShoppingById(pid);
        List<Sensor> ret= new ArrayList<>();
        for (SensorShopping qu: a){
            if (qu.getShopping().getId() == shopping.getId() ){
                ret.add(qu.getSensor());
            }
        }
        return ret;
    }*/



    @GetMapping("/SensorShopping")
    public SensorShopping findSensorShoppingById(@RequestParam(value = "id")  int id) {
        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        
        for (SensorShopping qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    // Não faz sentido dar update deste método
    //@PutMapping("/updateSensorShopping")
    //public SensorShopping updateSensorShopping(@RequestBody SensorShopping shopping) {
    //    return SensorShoppingServices.updateSensorShopping(shopping);
    //}

    @DeleteMapping("/deleteSensorShopping/{id}")
    public String deleteSensorShopping(@PathVariable int id) {
        return SensorShoppingServices.deleteSensorShopping(id);
    }
}

