package com.fuhu.jmsactivemq;

import com.fuhu.jmsactivemq.config.AppConfig;
import com.fuhu.jmsactivemq.producer.MessageSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

//@SpringBootApplication
public class MessageProducerApp {

    public static void main(String[] args) {

        //SpringApplication.run(MessageConsumerApp.class, args);
        AbstractApplicationContext context= new AnnotationConfigApplicationContext(AppConfig.class);
        MessageSender messageSender = context.getBean(MessageSender.class);
        messageSender.sendMessage("Hi, How are you?");
        System.out.println("Message has been sucessfully sent!");
        ((AbstractApplicationContext)context).close();
    }

}
