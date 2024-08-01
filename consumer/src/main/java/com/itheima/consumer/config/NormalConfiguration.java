package com.itheima.consumer.config;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

public class NormalConfiguration {

    @Bean
    public DirectExchange normalExchange(){
        return ExchangeBuilder.directExchange("normal.direct").durable(true).build();
    }
    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable("normal.queue").deadLetterExchange("dlx.exchange").build();
    }

    @Bean
    public Binding normalBinding(Queue normalQueue,DirectExchange normalExchange){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("hi");
    }

}
