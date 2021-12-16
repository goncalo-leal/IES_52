package ies.g52.ShopAholytics.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.ShoppingServices;


@RestController
@RequestMapping("/api/")
public class ShoppingController {
    @Autowired
    private ShoppingServices shoppingService;


    @PostMapping("/addShopping")
    public Shopping newShopping( @RequestBody Shopping s) {
        if (s.getCapacity() >0  ){
            return shoppingService.saveShopping(new Shopping (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing()));
        }
        return null;
    }

    @GetMapping("/storesShopping/{pid}")
    public Set<Store> findAllStoresShopping(@PathVariable int pid) {
        Shopping s = shoppingService.getShoppingById(pid);

        Set<Store> ret=s.getStores();
        
        return ret;
    }

    @GetMapping("/parksShopping/{pid}")
    public Set<Park> findAllParksShopping(@PathVariable int pid) {
        Shopping s = shoppingService.getShoppingById(pid);

        Set<Park> ret=s.getParks();
        
        return ret;
    }

    
    @GetMapping("/Shoppings")
    public List<Shopping> findAllShopping() {
        List<Shopping> a = shoppingService.getShopping();
        return a;
    }
    @GetMapping("/Shopping")
    public Shopping findShoppingById(@RequestParam(value = "id")  int id) {
        return shoppingService.getShoppingById(id);
    }

    @PutMapping("/updateShopping")
    public Shopping updateShopping(@RequestBody Shopping user) {
        return shoppingService.updateShopping(user);
    }

    @DeleteMapping("/deleteShopping/{id}")
    public String deleteShopping(@PathVariable int id) {
        return shoppingService.deleteShopping(id);
    }
}
