package com.itheima.publisher;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@SpringBootTest
@Slf4j
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

    @Test
    public void testConfigrmCallback(){
        //call back 测试
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("spring amqp 结果异常",ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                //判断成功
                if(result.isAck()){
                    log.debug("收到消息ack，发送成功");
                }else {
                    log.debug("收到消息Nack，失败reason:{}",result.getReason());
                }
            }
        });

        String exchangeName = "hmall.direct";
        String message = "test call";
        rabbitTemplate.convertAndSend(exchangeName,"aasdasdasd",message , cd);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void simpleTest(){
        Message message = MessageBuilder.withBody("hello test amqp".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue",message);
        }
    }
    @Test
    void simpleOneTest(){
        rabbitTemplate.convertAndSend("simple.queue","hello amqp");
    }
    @Test
    void delayMessage(){
        rabbitTemplate.convertAndSend("normal.direct", "hi", "hello", message -> {
            message.getMessageProperties().setExpiration("5000");
            return message;
        });
    }

    @Test
    void delayQueueTest(){
        rabbitTemplate.convertAndSend("delay.direct", "hi", "hello", message -> {
            message.getMessageProperties().setDelay(5000);
            return message;
        });
    }
}