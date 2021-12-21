package ies.g52.ShopAholytics.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.MQService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.views.ShoppingAllSensorsView;
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

    @Autowired
    private ShoppingServices shoppingServices;

    @GetMapping("/Shoppings")
    public List<ShoppingIDMappingView> shoppingIdName() {
        return mqService.shoppingIdNameMapper();
    }

    @GetMapping("/Shopping")
    public ShoppingStoresParksView storesAndParksOfShopping(@RequestParam(value = "id")  int id) {
        return mqService.shoppingStoresParksGet(id);   
    }

    @GetMapping("/ShoppingAllSensors/{id}")
    public ShoppingAllSensorsView shoppingGetAll(@PathVariable(value = "id")  int id) {
        List<SensorShopping> sen_shop = SensorShoppingServices.getSensorShoppings();
        List<SensorPark> sen_park = SensorParkService.getSensorParks();
        List<SensorStore> sen_store = SensorStoreServices.getSensorStores();
        Shopping shopping = shoppingServices.getShoppingById(id);
        ShoppingAllSensorsView shop= new ShoppingAllSensorsView(shopping);
        
        for (SensorShopping sen : sen_shop){
            if (sen.getShopping().getId() == id ){
                shop.setShopping(sen);
            }
        }
        for (Park park : shopping.getParks()){
            for (SensorPark sen : sen_park){
                if (sen.getPark().getId() == park.getId()){
                    shop.setPark(sen);
                }
            }
        }
        for (Store store : shopping.getStores()){
            for (SensorStore sen : sen_store){
                if (sen.getStore().getId() == store.getId()){
                    shop.setStore(sen);
                }
            }
        }

        return shop; 
    }

}
