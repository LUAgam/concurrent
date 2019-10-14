package io.concurrent.ioconcurrent.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {


    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue testQueue() {
        //String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        Map map = new HashMap(16);
        map.put("x-max-length", 100);
        return new Queue("test", true, false, false, map);
    }

    @PostConstruct
    public void init() {
        //指定 ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("消息唯一标识：" + correlationData + "；确认结果：" + b + "；失败原因：" + s);
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.err.println("消息主体 message : " + message + "；消息主体 message :" + i + "；描述：" + s + "；消息使用的交换器 exchange :" + s1 + "；消息使用的路由键 routing : " + s2);
    }
}
