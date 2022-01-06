package ies.g52.ShopAholytics.views;

import java.util.Map;
import java.util.HashMap;

public class OccupationInLast7Days {
    private Map<String,Integer> mapa ;


    public OccupationInLast7Days() {
        this.mapa=new HashMap<>();
        mapa.put("MONDAY", 0);
        mapa.put("TUESDAY", 0);
        mapa.put("WEDNESDAY", 0);
        mapa.put("THURSDAY", 0);
        mapa.put("FRIDAY", 0);
        mapa.put("SATURDAY", 0);
        mapa.put("SUNDAY", 0);
        mapa.put("LAST_MONDAY", 0);
        mapa.put("LAST_TUESDAY", 0);
        mapa.put("LAST_WEDNESDAY", 0);
        mapa.put("LAST_THURSDAY", 0);
        mapa.put("LAST_FRIDAY", 0);
        mapa.put("LAST_SATURDAY", 0);
        mapa.put("LAST_SUNDAY", 0);
    }
    
    


    public Map<String, Integer> getMapa() {
        return mapa;
    }




    


}
