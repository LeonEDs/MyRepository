package com.kafka.demo.controller;

import com.kafka.demo.service.KafkaSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaDemoController
{
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private KafkaSendService kafkaSendService;

    @GetMapping("/message/send")
    public boolean send(@RequestParam String message){
        kafkaTemplate.send("testTopic",message);
        return  true;
    }

    //同步
    @GetMapping("/message/sendSync")
    public boolean sendSync(@RequestParam String message){
        kafkaSendService.sendSync("syncTopic",message);
        return  true;
    }

    //异步示例
    @GetMapping("/message/sendAnsyc")
    public boolean sendAnsys(@RequestParam String message){
        kafkaSendService.sendAnsyc("ansycTopic",message);
        return  true;
    }

    //事务消息发送
    @GetMapping("/message/sendTransaction")
    public  boolean sendTransaction(){
        kafkaTemplate.executeInTransaction(kafkaTemplate -> {
            kafkaTemplate.send("transactionTopic", "TransactionMessage");
            return true;
        });
        return true;
    }
}
