package ies.g52.ShopAholytics.views;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;

import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorPark;

import ies.g52.ShopAholytics.models.SensorStore;

public class ShoppingAllSensorsView {
    
    @JsonIgnore
    private int id;
    @JsonIgnore
    private String name;

    private List<SensorShopping> shopping = new ArrayList<>();
    private List<SensorPark> park = new ArrayList<>();;
    private List<SensorStore> store =new ArrayList<>();;

    public ShoppingAllSensorsView() {
    }

    public ShoppingAllSensorsView(Shopping a) {
        this.id = a.getId();
        this.name = a.getName();
    
    }

    public int getId() {
        return id;
    }

    

    public String getName() {
        return name;
    }



    public List<SensorShopping> getShopping() {
        return shopping;
    }

    public void setShopping(SensorShopping shopping) {
        this.shopping.add(shopping);
    }

    public List<SensorPark> getPark() {
        return park;
    }

    public void setPark(SensorPark park) {
        this.park.add(park);
    }

    public List<SensorStore> getStore() {
        return store;
    }

    public void setStore(SensorStore store) {
        this.store.add(store);
    }
    

    
    
    

}
