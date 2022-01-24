package ies.g52.ShopAholytics.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreService;


@RestController
@RequestMapping("/api/stores")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private ShoppingServices shoppingService;


    @CrossOrigin(origins = "http://localhost:8000")
    @PostMapping("/addStore/{pid}")
    public Store newStore( @RequestBody Store s,  @PathVariable(value = "pid") int pid) {
        Shopping shopping = shoppingService.getShoppingById(pid);
        int var = shopping.getSum_shops_capacity() + s.getCapacity();
        if (s.getCapacity() > 0 && var < shopping.getCapacity()){
            shopping.setSum_shops_capacity(var);
            return storeService.saveStore(new Store (s.getLocation(),s.getName(),s.getCapacity(),s.getOpening(),s.getClosing(),shoppingService.getShoppingById(pid), s.getImg()));
        }
        return null;
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

    @CrossOrigin(origins = "http://localhost:8000")
    @PutMapping("/updateStore")
    public Store updateStore(@RequestBody Store store) {
        return storeService.updateStore(store);
    }

    @DeleteMapping("/deleteStore/{id}")
    public String deleteStore(@PathVariable int id) {
        return storeService.deleteStore(id);
    }
}
