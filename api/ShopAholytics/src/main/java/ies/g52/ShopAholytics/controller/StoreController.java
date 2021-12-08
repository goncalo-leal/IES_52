package ies.g52.ShopAholytics.controller;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreService;


@RestController
@RequestMapping("/api/")
public class StoreController {
    @Autowired
    private StoreService StoreServices;

    @Autowired
    private ShoppingServices shoppingServices;


    @PostMapping("/addStore")
    public Store newStore( @RequestBody Store s,  @PathVariable(value = "pid") int pid) {
        return StoreServices.saveStore(new Store (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing(),shoppingServices.getShoppingById(pid)));
    }

    @GetMapping("/Stores")
    public List<Store> findAllStore() {
        List<Store> a = StoreServices.getStore();
        return a;
    }
    @GetMapping("/Store")
    public Store findStoreById(@RequestParam(value = "id")  int id) {
        List<Store> a = StoreServices.getStore();
        
        for (Store qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    @PutMapping("/updateStore")
    public Store updateStore(@RequestBody Store user) {
        return StoreServices.updateStore(user);
    }

    @DeleteMapping("/deleteStore/{id}")
    public String deleteStore(@PathVariable int id) {
        return StoreServices.deleteStore(id);
    }
}
