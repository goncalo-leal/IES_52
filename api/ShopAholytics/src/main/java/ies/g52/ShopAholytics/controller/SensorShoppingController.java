package ies.g52.ShopAholytics.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.ShoppingServices;



@RestController
@RequestMapping("/api/")
public class SensorShoppingController {
    @Autowired
    private SensorShoppingService SensorShoppingServices;
    @Autowired
    private SensorService sensorService;

    @Autowired
    private ShoppingServices shoppingServices;


    @PostMapping("/addSensorShopping/{pid}/{shopping}")
    public SensorShopping newSensorShopping(@PathVariable(value = "pid") int pid, @PathVariable(value = "shopping") int shopping) {
        return SensorShoppingServices.saveSensorShopping(new SensorShopping (shoppingServices.getShoppingById(shopping),sensorService.getSensorById(pid)));
    }

    @GetMapping("/SensorShoppings")
    public List<SensorShopping> findAllSensorShopping() {
        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        return a;
    }
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

    @PutMapping("/updateSensorShopping")
    public SensorShopping updateSensorShopping(@RequestBody SensorShopping shopping) {
        return SensorShoppingServices.updateSensorShopping(shopping);
    }

    @DeleteMapping("/deleteSensorShopping/{id}")
    public String deleteSensorShopping(@PathVariable int id) {
        return SensorShoppingServices.deleteSensorShopping(id);
    }
}

