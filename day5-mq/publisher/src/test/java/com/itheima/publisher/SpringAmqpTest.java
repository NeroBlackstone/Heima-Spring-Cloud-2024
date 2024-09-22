package com.itheima.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        var queueName = "simple-queue";
        var message = "Hello simple amqp!";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() {
        var queueName = "work-queue";
        for (int i = 0; i < 50; i++) {
            var message = "hello, spring amqp!" + i;
            rabbitTemplate.convertAndSend(queueName, message);
        }
    }

    @Test
    public void testFanoutQueue() {
        var exchangeName = "hmall.fanout";
        var message = "Hello everyone!";
        rabbitTemplate.convertAndSend(exchangeName,null, message);
    }

    @Test
    public void testDirectQueue() {
        var exchangeName = "hmall.direct";
        var message = "蓝色!";
        rabbitTemplate.convertAndSend(exchangeName,"blue", message);
    }

    @Test
    public void testTopicQueue() {
        var exchangeName = "hmall.topic";
        var message = "中国天气!";
        rabbitTemplate.convertAndSend(exchangeName,"china.weathers", message);
    }

    @Test
    public void testSendObject() {
        var msg = new HashMap<String,Object>(2);
        msg.put("name", "Jack");
        msg.put("age", 21);
        rabbitTemplate.convertAndSend("object-queue",msg);
    }
}