package ies.g52.ShopAholytics.controller;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.models.Sensor;
import ies.g52.ShopAholytics.enumFolder.SensorEnum;
import ies.g52.ShopAholytics.models.*;


import ies.g52.ShopAholytics.services.SensorDataService;
import ies.g52.ShopAholytics.services.SensorService;
import ies.g52.ShopAholytics.views.OccupationInLast7Days;
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
                    int segundos =data.getDate().getSecond();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    int segundos_atuais=LocalTime.now().getSecond();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60+segundos;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
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
                    else{
                        stop =true;
                    }
                    if (control && stop){
                        break;
                    }
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInShoppingToday/{pid}")
    public int todayPeopleInShopping(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
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
                    
                    else{
                        break;
                    }
                    //ver se esta solução da 
                }
            }  
        }
        return counter;
    }
    //Até as horas e minutos atuais
    @GetMapping("/PeopleInShoppingTodayCompareWithLaskWeek/{pid}")
    public Map<String,Integer> peopleInSHoppingComparingWithLaskWeek(@PathVariable(value = "pid") int pid){
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
        OccupationInLast7Days ret= new OccupationInLast7Days();
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        boolean control=false;
        DayOfWeek week_day= null;
        DayOfWeek week_day_data= null;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid && data.getDate().getDayOfYear() != LocalDateTime.now().getDayOfYear()){
                    week_day_data = data.getDate().getDayOfWeek();
                    if (week_day==null){
                        week_day=week_day_data;
    
                    }
                    if (week_day_data == week_day){
                        counter++;
                    }
                    else{ 
                        ret.getMapa().put(week_day.toString(), counter);
                        counter=1;
                        week_day=week_day_data;
                    
                        if(ret.getMapa().containsKey(week_day.toString())){
                            break;
                        }
                    }
                    ret.getMapa().put(week_day.toString(), counter);
                }

            }  
        }
        return ret;
    }

    @GetMapping("/PeopleInShoppingWeek/{pid}")
    public int weekPeopleInShopping(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorShopping() != null && x.getSensorShopping().getShopping().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int dia_atual_limite=LocalDateTime.now().getDayOfYear()-7;

                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia_atual >=dia && dia >= dia_atual_limite   && ano_atual == ano){
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
                    int segundos =data.getDate().getSecond();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    int segundos_atuais=LocalTime.now().getSecond();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60+segundos;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia ==dia_atual && ano_atual == ano){
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


    @GetMapping("/PeopleInStoreLast7Days/{pid}")
    public OccupationInLast7Days peopleInPeopleInStoreLast7Days(@PathVariable(value = "pid") int pid){
        OccupationInLast7Days ret= new OccupationInLast7Days();
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        boolean control=false;
        DayOfWeek week_day= null;
        DayOfWeek week_day_data= null;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid && data.getDate().getDayOfYear() != LocalDateTime.now().getDayOfYear() ){
                    week_day_data = data.getDate().getDayOfWeek();
                    if (week_day==null){
                        week_day=week_day_data;
    
                    }
                    if (week_day_data == week_day){
                        counter++;
                    }
                    else{ 
                        ret.getMapa().put(week_day.toString(), counter);
                        counter=1;
                        week_day=week_day_data;
                    
                        if(ret.getMapa().containsKey(week_day.toString())){
                            break;
                        }
                    }
                    ret.getMapa().put(week_day.toString(), counter);
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
                    else{
                        stop =true;
                    }
                    if (control && stop){
                        break;
                    }
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInStoreWeek/{pid}")
    public int WeekPeopleInStore(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorStore() != null && x.getSensorStore().getStore().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int dia_atual_limite=LocalDateTime.now().getDayOfYear()-7;

                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia_atual >=dia && dia >= dia_atual_limite   && ano_atual == ano){
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
                    int segundos =data.getDate().getSecond();
                    int horas_atuais= LocalTime.now().getHour();
                    int minutos_atuais= LocalTime.now().getMinute();
                    int segundos_atuais=LocalTime.now().getSecond();
                    if (horas_atuais == 0 ){
                        horas_atuais=24;
                        if (horas == 0){
                            horas=24;
                        }
                    }
                    long total= 3600* horas+ minutos*60+segundos;
                    long total_limite= 3600*(horas_atuais-1)+ minutos_atuais*60+segundos_atuais;
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
                    else{
                        end=true;
                    }

                    if (counter2 != 0 && end){
                        break;
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
        boolean control=false;
        DayOfWeek week_day= null;
        DayOfWeek today= null;
        DayOfWeek week_day_data= null;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid && data.getDate().getDayOfYear() != LocalDateTime.now().getDayOfYear()){
                    week_day_data = data.getDate().getDayOfWeek();
                    if (week_day==null){
                        week_day=week_day_data;
    
                    }
                    if (week_day_data == week_day){
                        counter++;
                    }
                    else{ 
                        ret.getMapa().put(week_day.toString(), counter);
                        counter=1;
                        week_day=week_day_data;
                    
                        if(ret.getMapa().containsKey(week_day.toString())){
                            break;
                        }
                    }
                    ret.getMapa().put(week_day.toString(), counter);
                }

            }  
        }
        return ret;
    }

    @GetMapping("/PeopleInParkToday/{pid}")
    public int TodayPeopleInPark(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
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
                    
                    else{
                        break;
                    }
                }
            }  
        }
        return counter;
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
                    else{
                        stop =true;
                    }
                    if (control && stop){
                        break;
                    }
                }
            }  
        }
        return counter;
    }

    @GetMapping("/PeopleInParkWeek/{pid}")
    public int WeekPeopleInPark(@PathVariable(value = "pid") int pid){
        List<SensorData> a = sensorDataService.getSensorDatas();
        Collections.reverse(a);
        int counter=0;
        for (SensorData data : a){
            if (data.getSensor().getType().equals(SensorEnum.ENTRACE.toString())){
                Sensor x= data.getSensor();
                if (x.getSensorPark() != null && x.getSensorPark().getPark().getId()==pid ){
                    int dia =data.getDate().getDayOfYear();
                    int ano = data.getDate().getYear();
                    int dia_atual=LocalDateTime.now().getDayOfYear();
                    int dia_atual_limite=LocalDateTime.now().getDayOfYear()-7;

                    int ano_atual=LocalDateTime.now().getYear();
                    if(dia_atual >=dia && dia >= dia_atual_limite   && ano_atual == ano){
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
