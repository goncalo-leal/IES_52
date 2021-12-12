package ies.g52.ShopAholytics.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.services.ShoppingService;


@RestController
@RequestMapping("/api/")
public class ShoppingController {
    @Autowired
    private ShoppingService shoppingService;


    @PostMapping("/addShopping")
    public Shopping newShopping( @RequestBody Shopping s) {
        return shoppingService.saveShopping(new Shopping (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing()));
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
