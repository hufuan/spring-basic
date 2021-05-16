package io.javabrain.isthesiteup.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlCheckController {
    private final String SITE_IS_UP = "Site is up!";
    private final String SITE_IS_DOWN = "Site is down!";
    private final String INCORRECT_URL = "Site is incorrect!";

    @GetMapping("/check")
    public String getUrlStatusMessage(@RequestParam String url) {
        System.out.println("fuhu url:" + url);
        String returnMessage = "";
        try {
            URL urlObj = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            System.out.println("fuhu url:" + url + " code: " + conn.getResponseCode());
            int responseCategory = conn.getResponseCode() / 100;
            if (responseCategory != 2 && responseCategory != 3) {
                returnMessage = SITE_IS_DOWN;
            } else {
                returnMessage = SITE_IS_UP;
            }
        } catch (MalformedURLException e) {
            System.out.println("fuhu: MalformedURLException");
            e.printStackTrace();
            returnMessage = SITE_IS_DOWN;
        } catch (IOException e) {
            returnMessage = SITE_IS_DOWN;
            System.out.println("fuhu: IOException");
            e.printStackTrace();
        }
        return returnMessage;
    }
}
