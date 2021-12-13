package ies.g52.ShopAholytics.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.services.ShoppingServices;


@RestController
@RequestMapping("/api/")
public class ShoppingController {
    @Autowired
    private ShoppingServices shoppingServices;


    @PostMapping("/addShopping")
    public Shopping newShopping( @RequestBody Shopping s) {
        return shoppingServices.saveShopping(new Shopping (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing()));
    }

    @GetMapping("/Shoppings")
    public List<Shopping> findAllShopping() {
        List<Shopping> a = shoppingServices.getShopping();
        return a;
    }
    @GetMapping("/Shopping")
    public Shopping findShoppingById(@RequestParam(value = "id")  int id) {
        List<Shopping> a = shoppingServices.getShopping();
        
        for (Shopping qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    @PutMapping("/updateShopping")
    public Shopping updateShopping(@RequestBody Shopping user) {
        return shoppingServices.updateShopping(user);
    }

    @DeleteMapping("/deleteShopping/{id}")
    public String deleteShopping(@PathVariable int id) {
        return shoppingServices.deleteShopping(id);
    }
}
