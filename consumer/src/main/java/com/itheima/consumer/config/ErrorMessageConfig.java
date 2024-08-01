package com.itheima.consumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorMessageConfig {


    @Bean
    public DirectExchange errExchange(){
        return ExchangeBuilder.directExchange("error.direct").build();
    }
    @Bean
    public Queue errorQueue(){
        return QueueBuilder.durable("error.queue").build();
    }
    @Bean
    public Binding binding(Queue errorQueue,DirectExchange errExchange){
        return BindingBuilder.bind(errorQueue).to(errExchange).with( "error");
    }

    //可以理解为失败策略binding
    //RepublishMessageRecoverer 中，amqp template是rabbitTemplate的父类，所以可以使用autowire->
    // ->也可以使用这种方式传参，ctrl + p 查看参数时候需要注意第二个参数是string,注意此时不要用隐式注入
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate,"error.direct","error");
    }
}
