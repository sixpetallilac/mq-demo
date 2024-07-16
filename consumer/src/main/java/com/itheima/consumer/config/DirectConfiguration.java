package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfiguration {

    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange("hmall.direct").build();
    }
    @Bean
    public Queue directQueue(){
        return QueueBuilder.durable("direct.queue1").build();
    }
    @Bean
    public Binding directBinding(DirectExchange directExchange,Queue directQueue){
        return BindingBuilder.bind(directQueue).to(directExchange).with("red");
    }
    @Bean
    public Binding directBinding2(DirectExchange directExchange,Queue directQueue){
        return BindingBuilder.bind(directQueue).to(directExchange).with("green");
    }
    @Bean
    public Binding directBinding3(DirectExchange directExchange,Queue directQueue){
        return BindingBuilder.bind(directQueue).to(directExchange).with("blue");
    }
}
