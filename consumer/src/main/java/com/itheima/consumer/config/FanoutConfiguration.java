package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {

    //交换机声明
    @Bean
    public FanoutExchange fanoutExchange(){
        //return new FanoutExchange("hmall.fanout")  //方式一指定交换机名称
        return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
    }

    //队列声明
    @Bean
    public Queue fanoutQueue1(){
//        return new Queue("fanout.queue1"); //直接new
        return QueueBuilder.durable("fanout.queue1").build();
    }
    @Bean
    public Queue fanoutQueue2(){
        return QueueBuilder.durable("fanout.queue2").build();
    }

    //交换机绑定队列
    @Bean
    public Binding fanoutQueue1Binding(Queue fanoutQueue1, FanoutExchange fanoutExchange){
        //直接把上面的队列方法，交换机方法注入进来即可，如果有key还可以链式编程继续调用。
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    @Bean
    public Binding fanoutQueue2Binding(Queue fanoutQueue2, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
