package ies.g52.ShopAholytics.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MQConsumer {
    @RabbitListener(queues = MQConfig.QUEUE)
    public void consumer(Message message) {
        System.out.println(message);
    }

}

