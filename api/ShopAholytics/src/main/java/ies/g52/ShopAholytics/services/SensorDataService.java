package ies.g52.ShopAholytics.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
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
        Sensor sensor = sensorService.getSensorById(pid);
        SensorPark sensor_park = SensorParkService.getSensorParkById(pid);
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
    public HashMap<String,Integer> lastHourCountsStore(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==id ){
                    int horas = data.getDate().getHour();               
                    int minutos= data.getDate().getMinute();
                    int segundos =data.getDate().getSecond();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    int dia= data.getDate().getDayOfYear();
                    int dia_atuais= LocalDateTime.now().getDayOfYear();
                    int ano= data.getDate().getYear();
                    int ano_atual= LocalDateTime.now().getYear();
                    int segundos_atuais=LocalTime.now().getSecond();
                    
                    if (horas_atuais > 0){
                        long total= 3600* horas+ minutos*60+segundos;
                        long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
                        if(total >= total_limite && dia == dia_atuais && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    else if (horas_atuais == 0  && horas==0){
                        long total= minutos*60+segundos;
                        long total_meia_noite=minutos_atuais*60+segundos;
                        if(total <= total_meia_noite && dia == dia_atuais && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    else if (horas_atuais-1 == -1  && horas==23){
                        long total= 3600* horas+ minutos*60+segundos;
                        long total_meia_noite=(23)*3600+ minutos_atuais*60+segundos_atuais;
                        if(total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    if (horas_atuais > 1){
                        long total= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
                        long atual= 3600*(horas)+ minutos*60+segundos;
                        long total_limite= 3600*(horas_atuais-2)+ minutos_atuais*60+segundos_atuais;
                        

                        if(total >=  atual && atual >=total_limite && dia == dia_atuais && ano == ano_atual){
                            counter2++; continue;
                        }
                    }
                    else if (horas_atuais-2 == 0  && horas==0){
                        long total_maximo=3600+minutos_atuais*60+segundos_atuais;
                        long total= minutos*60+segundos;
                        long total_meia_noite=minutos_atuais*60+segundos_atuais;
                        if(total_maximo >= total && total >= total_meia_noite && dia == dia_atuais && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -1  && horas==23){
                        long total=horas*3600+ minutos*60+segundos;
                        long total_meia_noite=23*3600+minutos_atuais*60+segundos_atuais;
                        if( total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -1  && horas==0){
                        long total_maximo=minutos_atuais*60+segundos_atuais;
                        long total= minutos*60+segundos;
                        if(total_maximo >= total && dia == dia_atuais && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -2  ){
                        long total_maximo=23*3600+minutos_atuais*60+segundos_atuais;
                        long total=horas*3600+ minutos*60+segundos;
                        long total_meia_noite=22*3600+minutos_atuais*60+segundos_atuais;
                        if(total_maximo >= total && total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                 
                    
                    
                    

                   
                }
            }  
        }
        map.put("last_hour", counter);
        map.put("2_hours_ago", counter2);
        return map;
    } 

    public HashMap<String,Integer> lastHourCountsPark(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==id ){
                    int horas = data.getDate().getHour();               
                    int minutos= data.getDate().getMinute();
                    int segundos =data.getDate().getSecond();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    int dia= data.getDate().getDayOfYear();
                    int dia_atuais= LocalDateTime.now().getDayOfYear();
                    int ano= data.getDate().getYear();
                    int ano_atual= LocalDateTime.now().getYear();
                    int segundos_atuais=LocalTime.now().getSecond();
                    
                    if (horas_atuais > 0){
                        long total= 3600* horas+ minutos*60+segundos;
                        long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
                        if(total >= total_limite && dia == dia_atuais && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    else if (horas_atuais == 0  && horas==0){
                        long total= minutos*60+segundos;
                        long total_meia_noite=minutos_atuais*60+segundos;
                        if(total <= total_meia_noite && dia == dia_atuais && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    else if (horas_atuais-1 == -1  && horas==23){
                        long total= 3600* horas+ minutos*60+segundos;
                        long total_meia_noite=(23)*3600+ minutos_atuais*60+segundos_atuais;
                        if(total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter++;
                            continue;
                        }
                    }
                    if (horas_atuais > 1){
                        long total= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
                        long atual= 3600*(horas)+ minutos*60+segundos;
                        long total_limite= 3600*(horas_atuais-2)+ minutos_atuais*60+segundos_atuais;
                        

                        if(total >=  atual && atual >=total_limite && dia == dia_atuais && ano == ano_atual){
                            counter2++; continue;
                        }
                    }
                    else if (horas_atuais-2 == 0  && horas==0){
                        long total_maximo=3600+minutos_atuais*60+segundos_atuais;
                        long total= minutos*60+segundos;
                        long total_meia_noite=minutos_atuais*60+segundos_atuais;
                        if(total_maximo >= total && total >= total_meia_noite && dia == dia_atuais && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -1  && horas==23){
                        long total=horas*3600+ minutos*60+segundos;
                        long total_meia_noite=23*3600+minutos_atuais*60+segundos_atuais;
                        if( total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -1  && horas==0){
                        long total_maximo=minutos_atuais*60+segundos_atuais;
                        long total= minutos*60+segundos;
                        if(total_maximo >= total && dia == dia_atuais && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                    else if (horas_atuais-2 == -2  ){
                        long total_maximo=23*3600+minutos_atuais*60+segundos_atuais;
                        long total=horas*3600+ minutos*60+segundos;
                        long total_meia_noite=22*3600+minutos_atuais*60+segundos_atuais;
                        if(total_maximo >= total && total >= total_meia_noite && dia == dia_atuais-1 && ano == ano_atual){
                            counter2++;
                            continue;
                        }
                    }
                 
                    
                    
                    

                   
                }
            }  
        }
        map.put("last_hour", counter);
        map.put("2_hours_ago", counter2);
        return map;
    } 
    // Não faz sentido alterar nem sequer os dados
    //public SensorData updateSensorData(SensorData sensorData) {
    //    SensorData existingShoppingManager = repository.findById((int)sensorData.getId()).orElse(null);
    //    existingShoppingManager.setData(sensorData.getData());
    //    return repository.save(existingShoppingManager);
    //}
}
