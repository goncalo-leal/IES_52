package ies.g52.ShopAholytics.views;

import ies.g52.ShopAholytics.models.Sensor;

public class SensorShoppingPark {
    

    private boolean bool;
    private String name;
    private String type;

    
    public SensorShoppingPark() {
    }
    public SensorShoppingPark(boolean bool, String name, String type) {
        this.bool = bool;
        this.name = name;
        this.type = type;
    }
    public boolean isBool() {
        return bool;
    }
    public void setBool(boolean bool) {
        this.bool = bool;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    

    
}
