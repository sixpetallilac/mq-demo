package com.itheima.publisher;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@SpringBootTest
class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void simpleQueueTest(){
        String queueName = "simple.queue";

        String message = "こにちは";

        rabbitTemplate.convertAndSend(queueName,message);
    }

    @Test
    public void workQueueTest(){
        String queuename = "work.queue";
        for (int i = 0; i < 50; i++){
            rabbitTemplate.convertAndSend(queuename,i);
        }
    }

    @Test
    public void fanoutExchangeTest(){
        String exchangeName = "hmall.fanout";
        String msg = "hello amqp";
        rabbitTemplate.convertAndSend(exchangeName,null,msg);
    }

    @Test
    public void directExchangeTest(){
        String exchangeName = "hmall.direct";
        String msg = "yellow message ";
        rabbitTemplate.convertAndSend(exchangeName,"yellow",msg);
    }
    @Test
    public void topicExchangeTest(){
        String exchangeName = "hmall.direct";
        String msg = "topic message ";
        rabbitTemplate.convertAndSend(exchangeName,"green",msg);
    }
    @Test
    public void topicNewsExchangeTest(){
        String exchangeName = "hmall.topic";
        String msg = "china news";
        rabbitTemplate.convertAndSend(exchangeName,"china.news",msg);
    }

    @Test
    public void convertTest(){
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("name","bob");
        msg.put("age",20);
        rabbitTemplate.convertAndSend("object.queue",msg);
    }
}