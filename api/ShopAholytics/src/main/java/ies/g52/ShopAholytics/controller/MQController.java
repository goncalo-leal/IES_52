package ies.g52.ShopAholytics.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.services.MQService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.views.ShoppingIDMappingView;
import ies.g52.ShopAholytics.views.ShoppingStoresParksView;


import java.util.List;

@RestController
@RequestMapping("/mq/")
public class MQController {
    @Autowired
    private MQService mqService;

    @Autowired
    private SensorShoppingService SensorShoppingServices;

    @Autowired
    private SensorStoreService SensorStoreServices;

    @Autowired
    private SensorParkService SensorParkService;

    @GetMapping("/Shoppings")
    public List<ShoppingIDMappingView> shoppingIdName() {
        return mqService.shoppingIdNameMapper();
    }

    @GetMapping("/Shopping")
    public ShoppingStoresParksView storesAndParksOfShopping(@RequestParam(value = "id")  int id) {
        return mqService.shoppingStoresParksGet(id);   
    }

    @GetMapping("/ShoppingAllSensors/{id}")
    public ShoppingStoresParksView shoppingGetAll(@PathVariable(value = "id")  int id) {
        List<SensorShopping> sen_shop = SensorShoppingServices.getSensorShoppings();
        List<SensorPark> sen_park = SensorParkService.getSensorParks();
        List<SensorStore> sen_store = SensorStoreServices.getSensorStores();

        

        return mqService.shoppingStoresParksGet(id);   
    }

}
