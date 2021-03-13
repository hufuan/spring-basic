package com.pancm;

import com.alibaba.fastjson.JSON;
import com.pancm.pojo.User;
import com.pancm.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.pancm.storm.TopologyApp;
import com.pancm.util.GetSpringBean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
* Title: Application
* Description:
* springBoot 主程序 
* Version:1.0.0  
* @author pancm
* @date 2018年1月5日
 */
@SpringBootApplication
public class Application{

	public static void main(String[] args) {
		// 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		GetSpringBean springBean=new GetSpringBean();
		springBean.setApplicationContext(context);

		//先启动Storm，后启动springboot的工程
		TopologyApp app = new TopologyApp();
		System.out.println("app created");
		app.runStorm(args);
		System.out.println("app.runStormv finished");
	}
	
}
