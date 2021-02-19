package com.fuhu.demo.h2;

import com.fuhu.demo.h2.entity.Cat;
import com.fuhu.demo.h2.service.CatService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
@MapperScan("com.fuhu.demo.*.mapper")
public class H2Application {

	public static void main(String[] args) {
		System.out.println("fuhu1");
		ApplicationContext context = SpringApplication.run(H2Application.class, args);
		System.out.println("fuan2");
		CatService catService = context.getBean(CatService.class);
		System.out.println("catService: " + catService);
		List<Cat> list = catService.list();
		//list.forEach(System.out::println);
		System.out.println("fuan3");
	}

}
