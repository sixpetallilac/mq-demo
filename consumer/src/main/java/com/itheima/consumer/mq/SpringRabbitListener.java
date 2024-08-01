package com.itheima.consumer.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class SpringRabbitListener {

//    @RabbitListener(queues = "simple.queue")
//    public void simpleQueueListener(String message){
//        log.info("simple.queue message: {} ",message);
//    }

    @RabbitListener(queues = "work.queue")
    public void workQueueListener(String message) throws InterruptedException {
        log.info("work queue 监听者收到队列消息：_{}",message);
        Thread.sleep(25);
    }
    @RabbitListener(queues = "work.queue")
    public void workQueueListenersec(String message) throws InterruptedException {
        System.out.println("work queue 监听者收到队列消息：_"+message +"    "+ LocalTime.now());
        Thread.sleep(200);
    }
    @RabbitListener(queues = "fanout.queue1")
    public void fanoutListener1(String msg){
        log.info("消费者1：监听到 fanout.queue1 {}",msg);
    }
    @RabbitListener(queues = "fanout.queue2")
    public void fanoutListener2(String msg){
        log.info("消费者2：监听到 fanout.queue2 {}",msg);
    }
    @RabbitListener(queues = "direct.queue1")
    public void directListener1(String msg){
        log.info("direct消费者：监听到 {}",msg);
    }


    @RabbitListener(queues = "topic.queue1")
    public void topicListener1(String msg){
        log.info("topicListener1：监听到 direct.queue1 {}",msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2",durable = "true"),
            exchange = @Exchange(name = "hmall.direct",type = ExchangeTypes.DIRECT),
            key = {"yellow","green"}
    ))
    public void directListener2(String msg){
        log.info("代码指定交换机队列：{}",msg);
    }

    @RabbitListener(queues = "object.queue")
    public void objectSerializableListener(String msg){
        log.info("代码指定交换机队列：{}",msg);
    }

    @RabbitListener(queues = "simple.queue")
    public void deliberate(String msg) throws Exception {
        log.info("from simple.queue 消息：【{}】",msg);
//        throw new RuntimeException("故意处理不成功");
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dlx.queue",durable = "true"),
            exchange = @Exchange(value = "dlx.exchange",type = ExchangeTypes.DIRECT),
            key = {"hi"}
    ))
    public void listenDlxQueue(String msg){
        log.info("消费者监听到 dlx.queue的消息【{}】",msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue",durable = "true"),
            exchange = @Exchange(value = "delay.direct",delayed = "true",type = ExchangeTypes.DIRECT),
            key = {"hi"}
    ))
    public void listenDelayQueue(String msg){
        log.info("消费者监听到 dlx.queue的消息【{}】",msg);
    }
}
