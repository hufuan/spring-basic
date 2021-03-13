package com.pancm.kafka.demo;

import com.alibaba.fastjson.JSON;
import com.pancm.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class MyTest {
    public  static void main(String argv[]) {
        List<User> list=new ArrayList<User>();
        User use1 = new User(1, "zhangsan", 10);
        User use2 = new User(2, "lisi", 20);
        User use3 = new User(3, "wanglaowu", 30);
        list.add(use1);
        list.add(use2);
        list.add(use3);
        String jsonStr = JSON.toJSONString(list);
        System.out.println("jsonStr= " + jsonStr);
        String msg = jsonStr;
        System.out.println("haha1");
        List<User> listUser = JSON.parseArray(msg,User.class);
        listUser.stream().forEach(usr-> System.out.println(usr));
    }
}
