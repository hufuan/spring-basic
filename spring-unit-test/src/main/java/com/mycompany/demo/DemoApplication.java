package com.mycompany.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class},
        scanBasePackages={"com.mycompany.utilities"})
public class DemoApplication {

    public static void main(String[] args) {
        System.out.println("main entry");
        SpringApplication.run(DemoApplication.class, args);
    }
}
