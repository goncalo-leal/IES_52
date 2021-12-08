package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.services.ParkService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreService;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class ParkController {
    @Autowired
    private ParkService servicePark;

    

   @Autowired
   private ShoppingServices servicesShopping;

    @PostMapping("/addPark")
    public Park newPark( @RequestBody Park s,@PathVariable(value = "pid") int pid) {
        return servicePark.savePark(new Park(s.getName(),s.getLocation(),s.getCapacity(),s.getOpening(),s.getClosing(),servicesShopping.getShoppingById(pid)));
    }

    
    @GetMapping("/Parks")
    public List<Park> findAllParks() {
        List<Park> a = servicePark.getParks();
        return a;
    }
    

    @GetMapping("/Park")
    public Park findParkById(@RequestParam(value = "id")  int id) {
        Park a = servicePark.getParkById(id);
        return a;
        
    }



    @PutMapping("/updatePark")
    public Park updatePark(@RequestBody Park Park) {
        return servicePark.updatePark(Park);
    }

    @DeleteMapping("/deletePark/{id}")
    public String deletePark(@PathVariable int id) {
        return servicePark.deletePark(id);
    }
}