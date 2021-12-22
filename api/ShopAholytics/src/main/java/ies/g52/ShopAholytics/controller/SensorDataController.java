package ies.g52.ShopAholytics.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.SensorData;
import ies.g52.ShopAholytics.services.SensorDataService;
import ies.g52.ShopAholytics.services.SensorService;


@RestController
@RequestMapping("/api/")
public class SensorDataController {
    
    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private SensorService sensorService;

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
        */

        return sensorDataService.saveSensorData(new SensorData(s.getData(),sensorService.getSensorById(pid)));
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
