package ies.g52.ShopAholytics.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.Park;
import ies.g52.ShopAholytics.models.Store;

import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.models.SensorPark;
import ies.g52.ShopAholytics.models.SensorShopping;
import ies.g52.ShopAholytics.models.SensorStore;
import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.repository.SensorDataRepository;

@Service
public class SensorDataService {
    @Autowired
    private SensorDataRepository repository;

   

    @Autowired
    private SensorService sensorService;

    @Autowired
    private SensorParkService SensorParkService;
    @Autowired
    private ParkService parkServices;

    @Autowired
    private StoreService storeService;

    @Autowired
    private SensorStoreService SensorStoreServices;

    @Autowired
    private ShoppingServices shoppingServices;

    @Autowired
    private SensorShoppingService SensorShoppingServices;

    
    public SensorData saveSensorData(SensorData sensorData) {


        return repository.save(sensorData);
    }

    public SensorData saveSensorData(String data,int pid) {
        Sensor sensor= sensorService.getSensorById(pid);
        SensorPark sensor_park= SensorParkService.getSensorParkById(pid);
        String [] partida = data.split("-");
        LocalDateTime ts = LocalDateTime.of(Integer.parseInt(partida[0]), Integer.parseInt(partida[1]), Integer.parseInt(partida[2]), Integer.parseInt(partida[3]), Integer.parseInt(partida[4]),Integer.parseInt( partida[5]));
        if (sensor_park != null){
            Park park =parkServices.getParkById(sensor_park.getPark().getId());
            //IF AQUI

            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
                park.setCurrent_capacity(park.getCurrent_capacity()-1); 

            }
            else{
                park.setCurrent_capacity(park.getCurrent_capacity()+1); 
            }
            parkServices.updatePark(park);
            return repository.save(new SensorData(data,sensor,ts)); 
        }
        SensorShopping sensor_shopping =SensorShoppingServices.getSensorShoppingById(pid);
        if (sensor_shopping != null){
            Shopping shopping = shoppingServices.getShoppingById(sensor_shopping.getShopping().getId());
            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
            shopping.setCurrent_capacity(shopping.getCurrent_capacity()-1);

            }
            else{
            shopping.setCurrent_capacity(shopping.getCurrent_capacity()+1);

            }
            shoppingServices.updateShopping(shopping);
            return repository.save(new SensorData(data,sensor,ts));
 
        }
        SensorStore sensor_store =SensorStoreServices.getSensorStoreById(pid);
        if (sensor_store != null){
            Store store = storeService.getStoreById(sensor_store.getStore().getId());
            if (sensor.getType().equals(SensorEnum.EXIT.toString())){
                store.setCurrent_capacity(store.getCurrent_capacity()-1);
            }
            else{
                store.setCurrent_capacity(store.getCurrent_capacity()+1);
            }
            storeService.updateStore(store);
            return repository.save(new SensorData(data,sensor,ts));

        }
        return null;
        
    }
     
    public List<SensorData> saveSensorDatas(List<SensorData> sensorDatas) {
        return repository.saveAll(sensorDatas);
    }

    public List<SensorData> getSensorDatas() {
        return repository.findAll();
    }

    public SensorData getSensorDataById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public String deleteSensorData(int id) {
        repository.deleteById(id);
        return "SensorData removed !! " + id;
    }

    // Não faz sentido alterar nem sequer os dados
    //public SensorData updateSensorData(SensorData sensorData) {
    //    SensorData existingShoppingManager = repository.findById((int)sensorData.getId()).orElse(null);
    //    existingShoppingManager.setData(sensorData.getData());
    //    return repository.save(existingShoppingManager);
    //}
}
