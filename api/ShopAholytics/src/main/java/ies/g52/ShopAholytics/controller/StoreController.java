package ies.g52.ShopAholytics.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.ShoppingService;
import ies.g52.ShopAholytics.services.StoreService;


@RestController
@RequestMapping("/api/")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private ShoppingService shoppingService;


    @PostMapping("/addStore")
    public Store newStore( @RequestBody Store s,  @PathVariable(value = "pid") int pid) {
        return storeService.saveStore(new Store (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing(),shoppingService.getShoppingById(pid)));
    }

    @GetMapping("/Stores")
    public List<Store> findAllStore() {
        List<Store> a = storeService.getStore();
        return a;
    }
    @GetMapping("/Store")
    public Store findStoreById(@RequestParam(value = "id")  int id) {
        return storeService.getStoreById(id);
        
    }

    @PutMapping("/updateStore")
    public Store updateStore(@RequestBody Store user) {
        return storeService.updateStore(user);
    }

    @DeleteMapping("/deleteStore/{id}")
    public String deleteStore(@PathVariable int id) {
        return storeService.deleteStore(id);
    }
}
