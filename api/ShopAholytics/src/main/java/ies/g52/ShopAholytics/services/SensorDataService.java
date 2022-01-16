package ies.g52.ShopAholytics.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ies.g52.ShopAholytics.views.OccupationInLast7Days;

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
    public HashMap<String,Integer> lastHourShopping(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
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
    public HashMap<String,Integer>lastHourCountsBySensor(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getId()==id){
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

    public int entradasTodayLojas(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==id ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano){
                        counter++;

                    }
                    
                    
                }
            }  
        }
        return counter;
    }
    public int getEntrancesTodaySensor(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getId() == id){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano){
                        counter++;

                    }
                    
                    
                }
            }  
        }
        return counter;
    }
    public int entradasTodayPark(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano){
                        counter++;

                    }
                    
                   
                }
            }  
        }
        return counter;
    }

    public int entradasShoppingHoje(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano){
                        counter++;

                    }
                    
                    
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
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
    public int getEntrancesWeekSensor(int id){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();

        int ano_atual=LocalDateTime.now().getYear();
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
        List<Integer> dias = new ArrayList<>();
        for (int i =1; i <=7 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }
            dias.add(tmep);
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getId() ==id ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                      
                    /*
                        Problemas com as trocas de anos
                     */
                    if(dias.contains(dia)){
                        counter++;
                        

                    }
                  
                  
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }

    public int AllSensorsMonth(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();

        int ano_atual=LocalDateTime.now().getYear();
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
        List<Integer> dias = new ArrayList<>();
        for (int i =1; i <=31 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }

            dias.add(tmep);
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getId()==pid){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                      
                    /*
                        Problemas com as trocas de anos
                     */
                    if(dias.contains(dia)){
                        counter++;
                        

                    }
                  
                  
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }
    public int EntradasNoShoppingWeek(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();

        int ano_atual=LocalDateTime.now().getYear();
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
        List<Integer> dias = new ArrayList<>();
        for (int i =1; i <=7 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }

            dias.add(tmep);
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                      
                    /*
                        Problemas com as trocas de anos
                     */
                    if(dias.contains(dia)){
                        counter++;
                        

                    }
                  
                  
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }
    public int EntradasNoShoppingMonth(int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();

        int ano_atual=LocalDateTime.now().getYear();
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
        List<Integer> dias = new ArrayList<>();
        for (int i =1; i <=31 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }

            dias.add(tmep);
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                      
                    /*
                        Problemas com as trocas de anos
                     */
                    if(dias.contains(dia)){
                        counter++;
                        

                    }
                  
                  
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }

    public int peopleInParkWeek(int pid){
        List<SensorData> a = this.getSensorDatas();
            Collections.reverse(a);
            int counter=0;
            int dia_atual=LocalDateTime.now().getDayOfYear();
    
            int ano_atual=LocalDateTime.now().getYear();
            LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
            List<Integer> dias = new ArrayList<>();
            for (int i =1; i <=7 ; i++){
                int tmep=dia_atual-i;
                if (tmep < 1){
                    if (d.lengthOfYear() == 365){
                        tmep=tmep+365;
                    }
                    else{
                        tmep=tmep+366;
                    }
                }
    
                dias.add(tmep);
            }
            for (SensorData data : a){
                if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                    Sensor x= data.getSensor();
                    if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                        int dia =data.getDate().getDayOfYear();
                        int ano = data.getDate().getYear();
                          
                       
                        /*
                            Problemas com as trocas de anos
                         */
                        if(dias.contains(dia)){
                            counter++;
    
                        }
                        else if ( dia_atual == dia){
                            continue;
                        }
                      
                        //ver se esta solução da 
                    }
                }  
            }
            return counter;
    }
    public int peopleInParkMonth(int pid){
        List<SensorData> a = this.getSensorDatas();
            Collections.reverse(a);
            int counter=0;
            int dia_atual=LocalDateTime.now().getDayOfYear();
    
            int ano_atual=LocalDateTime.now().getYear();
            LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
            List<Integer> dias = new ArrayList<>();
            for (int i =1; i <=31 ; i++){
                int tmep=dia_atual-i;
                if (tmep < 1){
                    if (d.lengthOfYear() == 365){
                        tmep=tmep+365;
                    }
                    else{
                        tmep=tmep+366;
                    }
                }
    
                dias.add(tmep);
            }
            for (SensorData data : a){
                if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                    Sensor x= data.getSensor();
                    if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                        int dia =data.getDate().getDayOfYear();
                        int ano = data.getDate().getYear();
                          
                       
                        /*
                            Problemas com as trocas de anos
                         */
                        if(dias.contains(dia)){
                            counter++;
    
                        }
                        else if ( dia_atual == dia){
                            continue;
                        }
                      
                        //ver se esta solução da 
                    }
                }  
            }
            return counter;
    }


    public int peopleInStoreWeek(int pid){
        List<SensorData> a = this.getSensorDatas();
            Collections.reverse(a);
            int counter=0;
            int dia_atual=LocalDateTime.now().getDayOfYear();
    
            int ano_atual=LocalDateTime.now().getYear();
            LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
            List<Integer> dias = new ArrayList<>();
            for (int i =1; i <=7 ; i++){
                int tmep=dia_atual-i;
                if (tmep < 1){
                    if (d.lengthOfYear() == 365){
                        tmep=tmep+365;
                    }
                    else{
                        tmep=tmep+366;
                    }
                }
    
                dias.add(tmep);
            }
            for (SensorData data : a){
                if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                    Sensor x= data.getSensor();
                    if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                        int dia =data.getDate().getDayOfYear();
                        int ano = data.getDate().getYear();
                          
                       
                        /*
                            Problemas com as trocas de anos
                         */
                        if(dias.contains(dia)){
                            counter++;
    
                        }
                        else if ( dia_atual == dia){
                            continue;
                        }
                      
                        //ver se esta solução da 
                    }
                }  
            }
            return counter;
    }
    public int peopleInStoreMonth(int pid){
        List<SensorData> a = this.getSensorDatas();
            Collections.reverse(a);
            int counter=0;
            int dia_atual=LocalDateTime.now().getDayOfYear();
    
            int ano_atual=LocalDateTime.now().getYear();
            LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
            List<Integer> dias = new ArrayList<>();
            for (int i =1; i <=31 ; i++){
                int tmep=dia_atual-i;
                if (tmep < 1){
                    if (d.lengthOfYear() == 365){
                        tmep=tmep+365;
                    }
                    else{
                        tmep=tmep+366;
                    }
                }
    
                dias.add(tmep);
            }
            for (SensorData data : a){
                if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                    Sensor x= data.getSensor();
                    if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                        int dia =data.getDate().getDayOfYear();
                        int ano = data.getDate().getYear();
                          
                       
                        /*
                            Problemas com as trocas de anos
                         */
                        if(dias.contains(dia)){
                            counter++;
    
                        }
                        else if ( dia_atual == dia){
                            continue;
                        }
                      
                        //ver se esta solução da 
                    }
                }  
            }
            return counter;
    }

    public Map<String,Integer> compareWithLastWeekShopping(int pid) {
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();
        int ano_atual=LocalDateTime.now().getYear();
        boolean end=false;
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31");                   // import Java.time.LocalDate;
        int dia_last_week=LocalDateTime.now().getDayOfYear() -7;
        int ano_last_week= LocalDateTime.now().getYear();
        int hora_atual=LocalDateTime.now().getHour();
        int minutos_atual=LocalDateTime.now().getMinute();
        if (dia_last_week < 1){
            if (d.lengthOfYear() == 365){
                dia_last_week=dia_last_week+365;
                ano_last_week--;
            }
            else{
                dia_last_week=dia_last_week+366;
                ano_last_week--;
            }
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int minutos = data.getDate().getMinute();
                    int horas = data.getDate().getHour();

                    if(dia ==dia_atual && ano_atual == ano){
                        counter++;
                    }
                    else if (dia_last_week ==dia && ano_last_week==ano   ){
                        if (horas < hora_atual){
                            counter2++;
                            end=false;
                        }
                        else if (horas == hora_atual && minutos <= minutos_atual){
                            counter2++;

                            end=false;
                        }
                        else{
                            end =true;
                        }
                    }
                   
                    
                    //ver se esta solução da 
                }
            }  
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("Today", counter);
        map.put("LastWeek", counter2);
        return map;
    }

    public HashMap<Integer,Integer>  PeopleInParkByhours( int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        HashMap<Integer,Integer> map = new HashMap<>();
        Park s = parkServices.getParkById(pid);
        int abertura=s.getOpening().getHour();
        int fecho =s.getClosing().getHour();
        while (abertura != fecho+1){
            map.put(abertura, 0);

            abertura++;

            if (abertura==24){
                abertura =0;
            }
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    int hora = data.getDate().getHour();
                    
                    if(dia ==dia_atual && ano_atual == ano  ){
                        
                        if (map.containsKey(hora)){
                            map.put(hora, map.get(hora)+1);
                        }
                        else{
                            map.put(hora, 1);
                        }
                    }
                    
                
                    

                    
                    //ver se esta solução da 
                }
            }  
        }
        return map;
    }

    public HashMap<Integer,Integer>  PeopleInParkByhoursDe( int pid, String day){
        /*
            A estrutura do dia deve ser algo como ano-mes-dia
        */
        String [] escolha=day.split("-");
        int ano_pedido=Integer.parseInt(escolha[0]);
        int mes_pedido=Integer.parseInt(escolha[1]);
        int dia_pedido=Integer.parseInt(escolha[2]);
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        HashMap<Integer,Integer> map = new HashMap<>();

        Park s = parkServices.getParkById(pid);
        int abertura=s.getOpening().getHour();
        int fecho =s.getClosing().getHour();
        while (abertura != fecho+1){
            map.put(abertura, 0);

            abertura++;

            if (abertura==24){
                abertura =0;
            }
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfMonth();
                    int mes =data.getDate().getMonthValue();

                    int ano = data.getDate().getYear();
                    int hora = data.getDate().getHour();
                    
                    if(dia ==dia_pedido && ano_pedido == ano && mes_pedido == mes  ){
                        
                        if (map.containsKey(hora)){
                            map.put(hora, map.get(hora)+1);
                        }
                        else{
                            map.put(hora, 1);
                        }
                    }
                    
                    
                    
                    //ver se esta solução da 
                }
            }  
        }
        return map;
    }
    public HashMap<Integer,Integer>  PeopleInShoppingByhours( int pid){
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        HashMap<Integer,Integer> map = new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        int abertura=s.getOpening().getHour();
        int fecho =s.getClosing().getHour();
        while (abertura != fecho+1){
            map.put(abertura, 0);

            abertura++;

            if (abertura==24){
                abertura =0;
            }
        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    int hora = data.getDate().getHour();
                    
                    if(dia ==dia_atual && ano_atual == ano  ){
                        
                        if (map.containsKey(hora)){
                            map.put(hora, map.get(hora)+1);
                        }
                        else{
                            map.put(hora, 1);
                        }
                    }
                    
                
                   

                    
                    //ver se esta solução da 
                }
            }  
        }
        return map;
    }

    public HashMap<Integer,Integer>  PeopleInShoppingByhours(int pid, String day){
        /*
            A estrutura do dia deve ser algo como ano-mes-dia
        */
        String [] escolha=day.split("-");
        int ano_pedido=Integer.parseInt(escolha[0]);
        int mes_pedido=Integer.parseInt(escolha[1]);
        int dia_pedido=Integer.parseInt(escolha[2]);

        Shopping s = shoppingServices.getShoppingById(pid);
        int abertura=s.getOpening().getHour();
        HashMap<Integer,Integer> map = new HashMap<>();
        int fecho =s.getClosing().getHour();
        while (abertura != fecho+1){
            map.put(abertura, 0);

            abertura++;

            if (abertura==24){
                abertura =0;
            }
        }
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfMonth();
                    int mes =data.getDate().getMonthValue();

                    int ano = data.getDate().getYear();
                    int hora = data.getDate().getHour();
                    
                    if(dia ==dia_pedido && ano_pedido == ano && mes_pedido == mes  ){
                        
                        if (map.containsKey(hora)){
                            map.put(hora, map.get(hora)+1);
                        }
                        else{
                            map.put(hora, 1);
                        }
                    }
                    
                   

                    
                    //ver se esta solução da 
                }
            }  
        }
        return map;
    }

    public OccupationInLast7Days parkMovementLast14Days(int pid){
    OccupationInLast7Days ret= new OccupationInLast7Days();
        List<SensorData> a = this.getSensorDatas();
        Collections.reverse(a);
        int dia_atual=LocalDateTime.now().getDayOfYear();
        int ano_atual=LocalDateTime.now().getYear();
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31"); 
        List<Integer> dias = new ArrayList<>();
        List<Integer> dias_passada = new ArrayList<>();
        for (int i =1; i <=7 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }

            dias.add(tmep);

        }
        
        for (int i =8; i <=14 ; i++){
            int tmep=dia_atual-i;
            if (tmep < 1){
                if (d.lengthOfYear() == 365){
                    tmep=tmep+365;
                }
                else{
                    tmep=tmep+366;
                }
            }

            dias_passada.add(tmep);

        }
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    String semana_dia=data.getDate().getDayOfWeek().toString();
                    if(dias.contains(dia)){
                        if (ret.getMapa().containsKey(semana_dia)){
                            ret.getMapa().put(semana_dia, ret.getMapa().get(semana_dia)+1);
                        }
                        else{
                            ret.getMapa().put(semana_dia, 1);
                        }

                    }
                    if(dias_passada.contains(dia)){
                        if (ret.getMapa().containsKey("LAST_"+semana_dia)){
                            ret.getMapa().put("LAST_"+semana_dia, ret.getMapa().get("LAST_"+semana_dia)+1);
                        }
                        else{
                            ret.getMapa().put("LAST_"+semana_dia, 1);
                        }

                    }

                   
                    
                 
                    //ver se esta solução da 
                }
            }  
        }
        return ret;
    }


}
