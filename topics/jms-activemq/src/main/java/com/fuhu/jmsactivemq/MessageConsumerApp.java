package com.fuhu.jmsactivemq;

import com.fuhu.jmsactivemq.config.AppConfig;
import com.fuhu.jmsactivemq.producer.MessageSender;
import com.fuhu.jmsactivemq.receiver.MessageReceiver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.jms.Message;

//@SpringBootApplication
public class MessageConsumerApp {

    public static void main(String[] args) {
        AbstractApplicationContext context= new AnnotationConfigApplicationContext(AppConfig.class);
        MessageReceiver messageReceiver = (MessageReceiver)context.getBean("messageReceiver");
        String response = messageReceiver.receiveMessage();
        System.out.println("Message Received = " + response);
        ((AbstractApplicationContext)context).close();
        //SpringApplication.run(MessageConsumerApp.class, args);

    }

}
