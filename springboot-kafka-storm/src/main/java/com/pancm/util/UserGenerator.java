package com.pancm.util;

import com.pancm.pojo.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserGenerator {
    private int  id = 0;
    long currentTime = 0;
    SimpleDateFormat df = new SimpleDateFormat("MM-dd-HHmmss");//设置日期格式
    public  User getUser() {
        long currentTimeStamp = System.currentTimeMillis();
        if (currentTimeStamp > currentTime) {
            currentTime = currentTimeStamp;
        } else {
            currentTime++;
        }
        String name= "name-" + currentTime;
        int age = (int)(Math.random()*100) + 1;
        return new User(id++, name, age);
    }
    public static void main(String argv[]) {
        UserGenerator ug = new UserGenerator();
        User newUser = ug.getUser();
        System.out.println(newUser);
        newUser = ug.getUser();
        System.out.println(newUser);
    }
}
