package ies.g52.ShopAholytics.controller;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.*;


import ies.g52.ShopAholytics.services.SensorDataService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.views.OccupationInLast7Days;
import net.bytebuddy.asm.Advice.Local;
import ies.g52.ShopAholytics.services.SensorParkService;
import ies.g52.ShopAholytics.services.*;




@RestController
@RequestMapping("/api/sensorsdata/")
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
        
        return sensorDataService.saveSensorData(s.getData(),pid);

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
    public HashMap<String,Integer> lastHourPeopleInShopping(@PathVariable(value = "pid") int pid){
        return sensorDataService.lastHourShopping(pid);
    }

    @GetMapping("/lastHourShoppingAndParksbySensor/{pid}")
    public HashMap<String,HashMap<String,Integer>> lastHourShoppingAndParks(@PathVariable(value = "pid") int pid){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<>();
        HashMap<String, Integer> tmp= new HashMap<>();
        HashMap<String, Integer> tmp2= new HashMap<>();       
        Shopping s = shoppingServices.getShoppingById(pid);
        HashMap<String, Integer> shopping_sensors = (shoppingServices.getAllSensorsAssociatedShopping(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            HashMap<String, Integer> a = sensorDataService.lastHourCountsBySensor(entry.getValue());
            if (SensorShoppingServices.getSensorShoppingById(entry.getValue()).isPark_associated()){
                            
                tmp2.put(entry.getKey(), a.get("last_hour"));

            }
            else{
                tmp.put(entry.getKey(), a.get("last_hour"));
            }
        }
        map.put("Shopping", tmp);
        map.put("Park", tmp2);

        return map;
    }

    @GetMapping("/PeopleInShoppingToday/{pid}/{hours}")
    public int PeopleInShoppingAtXHours(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        boolean control=false;
        boolean stop =false;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int hora = data.getDate().getHour();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano && hours==hora){
                        counter++;
                        control=true;
                        stop =false;
                    }
                   
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInShoppingToday/{pid}")
    public int todayPeopleInShopping(@PathVariable(value = "pid") int pid){
        return sensorDataService.entradasShoppingHoje(pid);
    }
    @GetMapping("/AllSensorsForThatShoppingToday/{pid}")
    public HashMap<String,HashMap<String,Integer>> allSensors(@PathVariable(value = "pid") int pid){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<>();
        HashMap<String, Integer> tmp= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        HashMap<String, Integer> tmp2= new HashMap<>();
        HashMap<String, Integer> shopping_sensors = (shoppingServices.getAllSensorsAssociatedShopping(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.getEntrancesTodaySensor(entry.getValue());
            if (SensorShoppingServices.getSensorShoppingById(entry.getValue()).isPark_associated()){
                            
                tmp2.put(entry.getKey(),data);

            }
            else{
                tmp.put(entry.getKey(), data);
            }
        }
       
        map.put("Shopping", tmp);
        map.put("Park", tmp2);

        return map;
    }
    @GetMapping("/AllSensorsForThatStoreWeek/{pid}")
    public HashMap<String,Integer> allSensorsWeekStore(@PathVariable(value = "pid") int pid){
        HashMap<String, Integer> tmp= new HashMap<>();
        Store s = storeService.getStoreById(pid);
        HashMap<String, Integer> shopping_sensors = (storeService.getAllSensorsAssociatedStore(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.getEntrancesWeekSensor(entry.getValue());
            tmp.put(entry.getKey() ,data);
           
        }
  
     
        return tmp;
    }
    @GetMapping("/AllSensorsForThatStoreHour/{pid}")
    public HashMap<String,Integer> allSensorsHourStore(@PathVariable(value = "pid") int pid){
        HashMap<String, Integer> tmp= new HashMap<>();
        Store s = storeService.getStoreById(pid);
        HashMap<String, Integer> shopping_sensors = (storeService.getAllSensorsAssociatedStore(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
             HashMap<String, Integer> data = sensorDataService.lastHourCountsBySensor(entry.getValue());
            tmp.put(entry.getKey() ,data.get("last_hour"));
           
        }
  
     
        return tmp;
    }
    @GetMapping("/AllSensorsForThatStoreToday/{pid}")
    public HashMap<String,Integer> allSensorsTodayStore(@PathVariable(value = "pid") int pid){
        HashMap<String, Integer> tmp= new HashMap<>();
        Store s = storeService.getStoreById(pid);
        HashMap<String, Integer> shopping_sensors = (storeService.getAllSensorsAssociatedStore(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.getEntrancesTodaySensor(entry.getValue());
            tmp.put(entry.getKey() ,data);
           
        }
  
     
        return tmp;
    }

    @GetMapping("/AllSensorsForThatStoreMonth/{pid}")
    public HashMap<String,Integer> allSensorsMonthStore(@PathVariable(value = "pid") int pid){
        HashMap<String, Integer> tmp= new HashMap<>();
        Store s = storeService.getStoreById(pid);
        HashMap<String, Integer> shopping_sensors = (storeService.getAllSensorsAssociatedStore(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.AllSensorsMonth(entry.getValue());
            tmp.put(entry.getKey() ,data);
           
        }
  
     
        return tmp;
    }
    @GetMapping("/PeopleInShoppingSensorDay/{pid}/{day}")
    public HashMap<String,HashMap<Integer,Integer>> allSensorsWeekx2(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
        return sensorDataService.PeopleInShoppingSensor(pid,day);
    }

    @GetMapping("/AllSensorsForThatShoppingWeek/{pid}")
    public HashMap<String,HashMap<String,Integer>> allSensorsWeek(@PathVariable(value = "pid") int pid){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<>();
        HashMap<String, Integer> tmp= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        HashMap<String, Integer> tmp2= new HashMap<>();
        HashMap<String, Integer> shopping_sensors = (shoppingServices.getAllSensorsAssociatedShopping(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.getEntrancesWeekSensor(entry.getValue());
            if (SensorShoppingServices.getSensorShoppingById(entry.getValue()).isPark_associated()){
                tmp2.put(entry.getKey(),data);
            }
            else{
                tmp.put(entry.getKey(), data);
            }
        }
  
        map.put("Shopping", tmp);
        map.put("Park", tmp2);

        return map;
    }

    @GetMapping("/AllSensorsForThatShoppingMonth/{pid}")
    public HashMap<String,HashMap<String,Integer>> allSensorsMonth(@PathVariable(value = "pid") int pid){
        HashMap<String,HashMap<String,Integer>> map = new HashMap<>();
        HashMap<String, Integer> tmp= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        HashMap<String, Integer> tmp2= new HashMap<>();

        HashMap<String, Integer> shopping_sensors = (shoppingServices.getAllSensorsAssociatedShopping(pid));
        for (Map.Entry<String, Integer> entry : shopping_sensors.entrySet()) {
            int data = sensorDataService.AllSensorsMonth(entry.getValue());
            if (SensorShoppingServices.getSensorShoppingById(entry.getValue()).isPark_associated()){
                            
                tmp2.put(entry.getKey(),data);

            }
            else{
                tmp.put(entry.getKey(), data);
            }
        }
        map.put("Shopping", tmp);
        map.put("Park", tmp2);

        return map;
    }

    @GetMapping("/PeopleEntrancesTodayByShoppingOrPark/{pid}")
    public HashMap<String,Integer> PeopleEntrancesTodayByShoppingOrPark(@PathVariable(value = "pid") int pid){
        HashMap<String,Integer>  ret= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        int counter=0;
        if (s != null){
            ret.put("Shopping", sensorDataService.entradasShoppingHoje(pid));
            for (Park a : s.getParks()){
                counter+= sensorDataService.entradasTodayPark(a.getId());
            }
            ret.put("Park",counter);

        }
        return ret;
    }


    @GetMapping("/PeopleEntrancesLastHourByShoppingOrPark/{pid}")
    public HashMap<String,Integer> PeopleEntrancesLastHourByShoppingOrPark(@PathVariable(value = "pid") int pid){
        HashMap<String,Integer>  ret= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        int counter=0;
        if (s != null){
            HashMap<String, Integer> dic_shop= sensorDataService.lastHourShopping(pid);
            
            ret.put("Shopping",dic_shop.get("last_hour"));
            for (Park a : s.getParks()){
                HashMap<String, Integer> dic_park = sensorDataService.lastHourCountsPark(a.getId());
                counter+= dic_park.get("last_hour");
            }
            ret.put("Park",counter);

        }
        return ret;
    }

    @GetMapping("/PeopleEntrancesWeekByShoppingOrPark/{pid}")
    public HashMap<String,Integer> PeopleEntrancesWeekByShoppingOrPark(@PathVariable(value = "pid") int pid){
        HashMap<String,Integer>  ret= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        int counter=0;
        if (s != null){
            ret.put("Shopping", sensorDataService.EntradasNoShoppingWeek(pid));
            for (Park a : s.getParks()){
                counter+= sensorDataService.peopleInParkWeek(a.getId());
            }
            ret.put("Park",counter);

        }
        return ret;
    }

    @GetMapping("/PeopleEntrancesMonthByShoppingOrPark/{pid}")
    public HashMap<String,Integer> PeopleEntrancesMonthByShoppingOrPark(@PathVariable(value = "pid") int pid){
        HashMap<String,Integer>  ret= new HashMap<>();
        Shopping s = shoppingServices.getShoppingById(pid);
        int counter=0;
        if (s != null){
            ret.put("Shopping", sensorDataService.EntradasNoShoppingMonth(pid));
            for (Park a : s.getParks()){
                counter+= sensorDataService.peopleInParkMonth(a.getId());
            }
            ret.put("Park",counter);

        }
        return ret;
    }


    @GetMapping("/PeopleInShoppingByhours/{pid}")
    public HashMap<Integer,Integer>  PeopleInShoppingByhours(@PathVariable(value = "pid") int pid){
        return sensorDataService.PeopleInShoppingByhours(pid);
    }

    @GetMapping("/PeopleInShoppingByhoursVsLaskWeek/{pid}/{day}")
    public HashMap<String,HashMap<Integer,Integer>>  PeopleInShoppingByhoursT(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
        HashMap<Integer, Integer> a = sensorDataService.PeopleInShoppingByhours(pid, day);
        HashMap<Integer, Integer> b = sensorDataService.PeopleInShoppingByhours(pid);

        HashMap<String, HashMap<Integer, Integer>> ret= new HashMap<>();
        
        ret.put("Today", b);
        ret.put("Week", a);
        return ret;
    }

    @GetMapping("/PeopleInParkByhoursVsLaskWeek/{pid}/{day}")
    public HashMap<String,HashMap<Integer,Integer>>  PeopleInParkByhoursT(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
        HashMap<Integer, Integer> a = sensorDataService.PeopleInParkByhoursDe(pid, day);
        HashMap<Integer, Integer> b = sensorDataService.PeopleInParkByhours(pid);

        HashMap<String, HashMap<Integer, Integer>> ret= new HashMap<>();
        
        ret.put("Today", b);
        ret.put("Week", a);
        return ret;
    }

    @GetMapping("/PeopleInShoppingByhours/{pid}/{day}")
    public HashMap<Integer,Integer>  PeopleInShoppingByhours(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
        return sensorDataService.PeopleInShoppingByhours(pid, day);
    }


    //Até as horas e minutos atuais
    @GetMapping("/PeopleInShoppingTodayCompareWithLaskWeek/{pid}")
    public Map<String,Integer> peopleInSHoppingComparingWithLaskWeek(@PathVariable(value = "pid") int pid){
        return sensorDataService.compareWithLastWeekShopping(pid);
    }


    @GetMapping("/PeopleInShoppingTodayCompareWithLaskWeek/{pid}/{hours}")
    public Map<String,Integer> peopleInSHoppingComparingWithLaskWeekHours(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();
        int ano_atual=LocalDateTime.now().getYear();
        boolean end=false;
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31");                   // import Java.time.LocalDate;
        int dia_last_week=LocalDateTime.now().getDayOfYear() -7;
        int ano_last_week= LocalDateTime.now().getYear();

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


                    if(dia ==dia_atual && ano_atual == ano && horas <= hours){
                        counter++;
                    }
                    else if (dia_last_week ==dia && ano_last_week==ano && horas <= hours  ){
                        
                        counter2++;
                        end=false;
                        
                       
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

    @GetMapping("/PeopleInShoppingLast7Days/{pid}")
    public OccupationInLast7Days peopleInShoppingInLast7days(@PathVariable(value = "pid") int pid){
        return sensorDataService.ShoppingMovementLast14Days(pid);
    }

    @GetMapping("/ParksMovementInShoppingLast14Days/{pid}")
    public OccupationInLast7Days ParksMovementInShoppingLast14Days(@PathVariable(value = "pid") int pid){
        Shopping s = shoppingServices.getShoppingById(pid);
        OccupationInLast7Days ret = new  OccupationInLast7Days();
        for(Park park : s.getParks()){
            
            OccupationInLast7Days a = (sensorDataService.parkMovementLast14Days(park.getId()));
            for (Map.Entry<String, Integer> entry : a.getMapa().entrySet()) {
                String key = entry.getKey();
                int val = entry.getValue();
                ret.getMapa().put(key, ret.getMapa().get(key)+val);
            }
        }
        return ret;
    }


    @GetMapping("/PeopleInShoppingWeek/{pid}")
    public int weekPeopleInShopping(@PathVariable(value = "pid") int pid){
        return sensorDataService.EntradasNoShoppingWeek(pid);
    }

    
    @GetMapping("/PeopleInStoreInLastHour/{pid}")
    public  HashMap<String,Integer> lastHourPeopleInStore(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
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

    @GetMapping("/PeopleInStoreByhours/{pid}")
    public HashMap<Integer,Integer>  PeopleInStoreByhours(@PathVariable(value = "pid") int pid){
        return sensorDataService.PeopleInStoreByhours(pid);
    }

    @GetMapping("/PeopleInStoreByhours/{pid}/{day}")
    public HashMap<Integer,Integer>  PeopleInShoppingByhoursDe(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
        return sensorDataService.PeopleInStoreByhours(pid, day);
    }



    //Até as horas e minutos atuais
    @GetMapping("/PeopleInStoreTodayCompareWithLaskWeek/{pid}")
    public Map<String,Integer> peopleInStoreComparingWithLaskWeek(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
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
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
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
    

    @GetMapping("/PeopleInStoreTodayCompareWithLaskWeek/{pid}/{hours}")
    public Map<String,Integer> peopleInStoreComparingWithLaskWeekHours(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();
        int ano_atual=LocalDateTime.now().getYear();
        boolean end=false;
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31");                   // import Java.time.LocalDate;
        int dia_last_week=LocalDateTime.now().getDayOfYear() -7;
        int ano_last_week= LocalDateTime.now().getYear();

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
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int minutos = data.getDate().getMinute();
                    int horas = data.getDate().getHour();


                    if(dia ==dia_atual && ano_atual == ano && horas <= hours){
                        counter++;
                    }
                    else if (dia_last_week ==dia && ano_last_week==ano && horas <= hours  ){
                     
                        counter2++;
                        end=false;
                        
                       
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

    @GetMapping("/PeopleInStoreToday/{pid}")
    public int TodayPeopleInStore(@PathVariable(value = "pid") int pid){
        
        return sensorDataService.entradasTodayLojas(pid);
    }


    @GetMapping("/PeopleInStoreLast14Days/{pid}")
    public OccupationInLast7Days peopleInPeopleInStoreLast7Days(@PathVariable(value = "pid") int pid){
        OccupationInLast7Days ret= new OccupationInLast7Days();
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
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
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
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

    @GetMapping("/PeopleInStoreToday/{pid}/{hours}")
    public int PeopleInStoreAtXHours(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        boolean control=false;
        boolean stop=false;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int hora = data.getDate().getHour();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano && hours==hora){
                        counter++;
                        control=true;
                        stop =false;
                    }
                    
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInStoreWeek/{pid}")
    public int WeekPeopleInStore(@PathVariable(value = "pid") int pid){
            return sensorDataService.peopleInStoreWeek(pid);
        }

    @GetMapping("/PeopleInParkInLastHourInactive/{pid}")
    public HashMap<String,Integer> lastHourPeopleInPark(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        HashMap<String,Integer> map = new HashMap<>();
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
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



    @GetMapping("/PeopleInParkTodayCompareWithLaskWeek/{pid}/{hours}")
    public Map<String,Integer> peopleInParkComparingWithLaskWeekHours(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        int counter2=0;
        int dia_atual=LocalDateTime.now().getDayOfYear();
        int ano_atual=LocalDateTime.now().getYear();
        boolean end=false;
        LocalDate d = LocalDate.parse(ano_atual-1+"-12-31");                   // import Java.time.LocalDate;
        int dia_last_week=LocalDateTime.now().getDayOfYear() -7;
        int ano_last_week= LocalDateTime.now().getYear();

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
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int minutos = data.getDate().getMinute();
                    int horas = data.getDate().getHour();


                    if(dia ==dia_atual && ano_atual == ano && horas <= hours){
                        counter++;
                    }
                    else if (dia_last_week ==dia && ano_last_week==ano && horas <= hours  ){
                     
                        counter2++;
                        end=false;
                        
                       
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

    @GetMapping("/PeopleInParkInLastHour2/{pid}")
    public HashMap<String,Integer> lastHourPeopleInParkv2(@PathVariable(value = "pid") int pid){
        Shopping s = shoppingServices.getShoppingById(pid);
        HashMap<String,Integer> map = new HashMap<>();
        map.put("last_hour", 0);
        map.put("2_hours_ago",0);
        for (Park a : s.getParks()){
            HashMap<String, Integer> c = sensorDataService.lastHourCountsPark(a.getId());

            map.put("last_hour", map.get("last_hour")+c.get("last_hour"));
            map.put("2_hours_ago", map.get("2_hours_ago")+c.get("2_hours_ago"));


        }
        return map;
    }
   

    @GetMapping("/PeopleInParkByhours/{pid}/{day}")
    public HashMap<Integer,Integer>  PeopleInParkByhoursDe(@PathVariable(value = "pid") int pid,@PathVariable(value = "day") String day){
       return sensorDataService.PeopleInParkByhoursDe(pid, day);
    }


    @GetMapping("/PeopleInParkByhours/{pid}")
    public HashMap<Integer,Integer>  PeopleInParkByhours(@PathVariable(value = "pid") int pid){
        return sensorDataService.PeopleInParkByhours(pid);
    }

    @GetMapping("/PeopleInParkTodayCompareWithLaskWeek/{pid}")
    public Map<String,Integer> peopleInParkComparingWithLaskWeek(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
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
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
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

    @GetMapping("/PeopleInParkLast7Days/{pid}")
    public OccupationInLast7Days peopleInPeopleInParkLast7Days(@PathVariable(value = "pid") int pid){
        OccupationInLast7Days ret= new OccupationInLast7Days();
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
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

    @GetMapping("/PeopleInParkToday/{pid}")
    public int TodayPeopleInPark(@PathVariable(value = "pid") int pid){
        return sensorDataService.entradasTodayPark(pid);
    }

    @GetMapping("/PeopleInParkToday/{pid}/{hours}")
    public int TodayAtPeopleInPark(@PathVariable(value = "pid") int pid,@PathVariable(value = "hours") int hours){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        boolean control=false;
        boolean stop=false;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int hora = data.getDate().getHour();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano && hours==hora){
                        counter++;
                        control=true;
                        stop =false;
                    }
                   
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInParkWeek/{pid}")
    public int WeekPeopleInPark(@PathVariable(value = "pid") int pid){
            return sensorDataService.peopleInParkWeek(pid);
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

    @GetMapping("/CountLastHoursForParks/{id}")
    public HashMap<Integer, HashMap<String,Integer>> findAllSensorsDatas(@PathVariable int id) {
        Shopping shopping = shoppingServices.getShoppingById(id);
        Set<Park> a = shopping.getParks();
        HashMap<Integer, HashMap<String,Integer>>  map = new HashMap<>();
        for (Park parque : a){
            HashMap<String, Integer> ret = sensorDataService.lastHourCountsPark(parque.getId());
            map.put(parque.getId(), ret);
        }
        return map;
    }

    @GetMapping("/CountLastHoursForStores/{id}")
    public HashMap<Integer, HashMap<String,Integer>> findAllSensorsDatasadsa(@PathVariable int id) {
        Shopping shopping = shoppingServices.getShoppingById(id);
        Set<Store> a = shopping.getStores();
        HashMap<Integer, HashMap<String,Integer>>  map = new HashMap<>();
        for (Store parque : a){
            HashMap<String, Integer> ret = sensorDataService.lastHourCountsStore(parque.getId());
            map.put(parque.getId(), ret);
        }
        return map;
    }

}
