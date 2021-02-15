package com.fuhu.demo.h2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.fuhu.demo.*.mapper")
public class H2Application {

	public static void main(String[] args) {
		SpringApplication.run(H2Application.class, args);
	}

}
