package ies.g52.ShopAholytics.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.services.MQService;
import ies.g52.ShopAholytics.services.ParkService;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.SensorStoreService;
import ies.g52.ShopAholytics.services.ShoppingServices;
import ies.g52.ShopAholytics.services.StoreService;
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


    @Autowired
    private ParkService parkServices;

    @Autowired
    private StoreService storeService;
    
    @Autowired
    private SensorService sensorService;

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


    @GetMapping("/getMaxPossible/{pid}")
    public Integer getMaxPossible(@PathVariable(value = "pid") int pid) {
    
        Sensor sensor= sensorService.getSensorById(pid);
        SensorPark sensor_park= SensorParkService.getSensorParkById(pid);
        if (sensor_park != null){
            Park park =parkServices.getParkById(sensor_park.getPark().getId());
            //IF AQUI

            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
                return park.getCurrent_capacity();

            }
            else{
                return park.getCapacity()-park.getCurrent_capacity();
            }
        }
        SensorShopping sensor_shopping =SensorShoppingServices.getSensorShoppingById(pid);
        if (sensor_shopping != null){
            Shopping shopping = shoppingServices.getShoppingById(sensor_shopping.getShopping().getId());
            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
                return shopping.getCurrent_capacity();

            }
            else{
                return shopping.getCapacity()-shopping.getCurrent_capacity();

            }
        }
        SensorStore sensor_store =SensorStoreServices.getSensorStoreById(pid);
        if (sensor_store != null){
            Store store = storeService.getStoreById(sensor_store.getStore().getId());
            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
                return store.getCurrent_capacity();
            }
            else{
                return store.getCapacity()-store.getCurrent_capacity();
            }

        }
        return 0;
    }
}
