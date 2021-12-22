package ies.g52.ShopAholytics.controller;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.*;


import ies.g52.ShopAholytics.services.SensorDataService;
import ies.g52.ShopAholytics.services.SensorService;
import net.bytebuddy.asm.Advice.Local;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.*;




@RestController
@RequestMapping("/api/")
public class SensorDataController {
    
    @Autowired
    private SensorDataService sensorDataService;

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

    //Todos os dados dos sensores de uma loja/shopping ou parque

    @PostMapping("/addSensorData/{pid}")
    public SensorData newSensorData(@PathVariable(value = "pid") int pid, @RequestBody SensorData s) {
        /*
            Neste local , para ter a current_capacity sempre atualizada, o que vou fazer é
            ir buscar ver a que local o sensor está associado e consoante o local, vou buscar o objeto desse
            sitio e atualizo-lhe a variável
            e só depois é gerado o sensorData

            Caso o sensor não se encontre em nenhuma das 3 listas possiveis então este data não vai ser criado
            visto que não existe

            DEPOIS TEMOS DE VER SE O SENSOR ATUAL É DE ENTRADA OU DE SAIDA 
        */
        Sensor sensor= sensorService.getSensorById(pid);
        SensorPark sensor_park= SensorParkService.getSensorParkById(pid);
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
            return sensorDataService.saveSensorData(new SensorData(s.getData(),sensor));
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
            return sensorDataService.saveSensorData(new SensorData(s.getData(),sensor));
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
            return sensorDataService.saveSensorData(new SensorData(s.getData(),sensor));

        }
        return null;
    }

    @GetMapping("/PeopleInShopping/{pid}")
    public int peopleInShopping(@PathVariable(value = "pid") int pid){
        return shoppingServices.getShoppingById(pid).getCurrent_capacity();
    }
    @GetMapping("/PeopleInStore/{pid}")
    public int peopleInStore(@PathVariable(value = "pid") int pid){
        return storeService.getStoreById(pid).getCurrent_capacity();
    }
    @GetMapping("/PeopleInPark/{pid}")
    public int peopleInPark(@PathVariable(value = "pid") int pid){
        return parkServices.getParkById(pid).getCurrent_capacity();
    }
    @GetMapping("/PeopleInShoppingInLastHour/{pid}")
    public int lastHourPeopleInShopping(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int horas = data.getDate().getHour();               
                    int minutos= data.getDate().getMinute();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60;
                    if(total >= total_limite){
                        counter++;
                    }
                    else{
                        break;
                    }
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInStoreInLastHour/{pid}")
    public int lastHourPeopleInStore(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                    int horas = data.getDate().getHour();               
                    int minutos= data.getDate().getMinute();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60;
                    if(total >= total_limite){
                        counter++;
                    }
                    else{
                        break;
                    }
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInParkInLastHour/{pid}")
    public int lastHourPeopleInPark(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int horas = data.getDate().getHour();               
                    int minutos= data.getDate().getMinute();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60;
                    if(total >= total_limite){
                        counter++;
                    }
                    else{
                        break;
                    }
                }
            }  
        }
        return counter;
    }

    @GetMapping("/SensorsDatas")
    public List<SensorData> findAllSensorsData() {
        List<SensorData> a = sensorDataService.getSensorDatas();
        return a;
    }

    @GetMapping("/SensorsData")
    public SensorData findSensorDataById(@RequestParam(value = "id")  int id) {
        List<SensorData> a = sensorDataService.getSensorDatas();
        
        for (SensorData qu: a){
            if (qu.getId() == id ){
                return qu;
            }
        }
        return null;
        
    }

  

    @DeleteMapping("/deleteSensorData/{id}")
    public String deleteSensorData(@PathVariable int id) {
        return sensorDataService.deleteSensorData(id);
    }
}
