package ies.g52.ShopAholytics.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.services.SensorShoppingService;
import ies.g52.ShopAholytics.services.SensorStoreService;

import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.Store;

import ies.g52.ShopAholytics.repository.ShoppingRepository;

@Service
public class ShoppingServices {
    @Autowired
    private ShoppingRepository repository;

    @Autowired
    private SensorShoppingService SensorShoppingServices;

    @Autowired
    private SensorStoreService SensorStoreServices;

    public Shopping saveShopping(Shopping Shopping) {
        return repository.save(Shopping);
    }

    public List<Shopping> saveShopping(List<Shopping> Shopping) {
        return repository.saveAll(Shopping);
    }

    public Set<Park> getAllParks(int id) {
       
        Set<Park> ret=this.getShoppingById(id).getParks();
        
        return ret;
    }

    public Set<Store> getAllStores(int id) {
       
        Set<Store> ret=this.getShoppingById(id).getStores();
        
        return ret;
    }


    public HashMap<String,Integer> getAllSensorsAssociatedShopping(int id){
       
        HashMap<String,Integer> map = new HashMap<>();

        List<SensorShopping> a = SensorShoppingServices.getSensorShoppings();
        for ( SensorShopping s : a){
            if (s.getShopping().getId() == id && s.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                map.put(s.getSensor().getName(),s.getId());
            }
        }

        return map;
    }

    public List<Shopping> getShopping() {
        return repository.findAll();
    }

    public Shopping getShoppingByName(String name) {
        return repository.findByName(name);
    }

    public Shopping getShoppingById(int id) {
        return repository.findById((int)id).orElse(null);
    }


    public String deleteShopping(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public Shopping updateShopping(Shopping user) {
        //acho que este e o user esta incompleto mas preciso de testar para saber
        Shopping existingShopping = repository.findById((int)user.getId()).orElse(null);
        existingShopping.setName(user.getName());
        existingShopping.setLocation(user.getLocation());
        existingShopping.setCapacity(user.getCapacity());
        existingShopping.setOpening(user.getOpening());
        existingShopping.setClosing(user.getClosing());
        
        return repository.save(existingShopping);
    }

}
