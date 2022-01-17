package ies.g52.ShopAholytics.rabbitmq;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import ies.g52.ShopAholytics.services.SensorDataService;

import org.springframework.beans.factory.annotation.Autowired;

public class MQConsumer {

    @Autowired
    private SensorDataService sensorDataService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listen(String input) {
        System.out.println("   Receiver#receive input: " + input);

        JSONObject jsnobject = new JSONObject(input);
        System.out.println(jsnobject.getString("sensor_id"));
        sensorDataService.saveSensorData(
            jsnobject.getString("data"), 
            Integer.parseInt(jsnobject.getString("sensor_id"))
        );
    }

}

