package ies.g52.ShopAholytics.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.models.User;
import ies.g52.ShopAholytics.services.StoreManagerService;
import ies.g52.ShopAholytics.services.StoreService;
import ies.g52.ShopAholytics.services.UserService;



@RestController
@RequestMapping("/api/")
public class StoreManagerController {
    @Autowired
    private StoreManagerService StoreManagerServices;

    @Autowired
    private StoreService StoreServices;

    @Autowired
    private UserService serviceUser;


    @PostMapping("/addStoreManager/{pid}/{store}")
    public StoreManager newStoreManager(@PathVariable(value = "pid") int pid, @PathVariable(value = "store") int store) {
        return StoreManagerServices.saveStoreManager(new StoreManager (serviceUser.getUserById(pid),StoreServices.getStoreById(store)));
    }

    @GetMapping("/StoreManagers")
    public List<StoreManager> findAllStoreManager() {
        List<StoreManager> a = StoreManagerServices.getStoreManagers();
        return a;
    }
    @GetMapping("/StoreManager")
    public StoreManager findStoreManagerById(@RequestParam(value = "id")  int id) {
        List<StoreManager> a = StoreManagerServices.getStoreManagers();
        
        for (StoreManager qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

    // Os updates são feitos na no store e no user
    @PutMapping("/updateStoreManager")
    public StoreManager updateStoreManager(@RequestBody StoreManager user) {
        return StoreManagerServices.updateStoreManager(user);
    }

    @DeleteMapping("/deleteStoreManager/{id}")
    public String deleteStoreManager(@PathVariable int id) {
        return StoreManagerServices.deleteStoreManager(id);
    }
}

