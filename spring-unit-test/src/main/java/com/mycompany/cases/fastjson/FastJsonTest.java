package com.mycompany.cases.fastjson;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URL;

public class FastJsonTest {
    @Test
    public void test(){
        URL url1 = Thread.currentThread().getContextClassLoader().getResource("team.json");
        URI uri  = null;
        try {
            uri = url1.toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(uri);
        System.out.println("url1: " + url1 + "\nuri: " + uri + "\nfile: " + file);

        byte[] bufferFile = null;
        String jsonString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
        try {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                byte[] bytes = new byte[(int) file.length()];
                int num = 0;
                if ((num=fis.read(bytes)) != -1) {
                    bos.write(bytes, 0, (int) file.length());
                    bufferFile = bos.toByteArray();
                    jsonString = new String(bufferFile);
                    System.out.println("jsonString = " + jsonString);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Team team = JSONObject.parseObject(jsonString, Team.class);
        System.out.println(team);
    }
}
