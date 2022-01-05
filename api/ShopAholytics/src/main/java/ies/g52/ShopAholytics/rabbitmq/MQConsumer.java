package ies.g52.ShopAholytics.rabbitmq;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class MQConsumer {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listen(String input) {
        System.out.println("   Receiver#receive input: " + input);

        JSONObject jsnobject = new JSONObject(input);
        System.out.println(jsnobject.getString("sensor_id"));
    }

}

