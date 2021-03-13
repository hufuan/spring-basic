package com.pancm.config.demo;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.pancm.Application;
import com.pancm.storm.spout.KafkaInsertDataSpout;
import com.pancm.util.GetSpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.pancm.config"})

public class DbConnectionTest {
    private static final Logger logger = LoggerFactory.getLogger(DbConnectionTest.class);
    public  static void main(String[] args) {
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        ConfigurableApplicationContext context = SpringApplication.run(DbConnectionTest.class, args);
        GetSpringBean springBean=new GetSpringBean();
        springBean.setApplicationContext(context);

        DataSource dataSource = springBean.getBean(DataSource.class);
        //System.out.println("dataSource = " + dataSource);
        /*
        String[] names = context.getBeanDefinitionNames();
        int index = 0;
        for (String name : names) {
            try {
                Object tmp = context.getBean(name);
                logger.info("fuhu: >>>>>> bean[{}]: bean id: {}, class: {}" , index, name, tmp.getClass().getName());
            } catch (BeansException be) {
                logger.info("fuhu: >>>>>> bean[{}]: bean id: {}, Exception: {}", index, name, be.getMessage());
            }
            index++;
        }
        */


        try {
            // 获得连接:
            Connection conn = dataSource.getConnection();
            System.out.println("dataSource = " + dataSource);
            // 编写SQL：
            String sql = "select * from t_user  where age > ? ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //索引从1开始
            pstmt.setInt(1,10);
            // 执行sql:
            ResultSet rs = pstmt.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                System.out.println(rs.getInt("age") + "   " + rs.getString("name"));
                rowCount++;
            }
            System.out.println("ResultSet, rs.size" + rowCount);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
