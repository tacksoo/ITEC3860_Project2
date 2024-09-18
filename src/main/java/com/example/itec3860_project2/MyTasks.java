package com.example.itec3860_project2;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class MyTasks {
    private int count = 1;
    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron="*/3 * * * * *")
    public void addVehicle() {
        String makeModel = RandomStringUtils.randomAlphabetic(10);
        Random r = new Random();
        int year = r.nextInt(1986, 2017);
        int price = r.nextInt(15000,45001);
        Vehicle v = new Vehicle(count++, makeModel, year, price);
        restTemplate.postForObject("http://localhost:8080/addVehicle", v, Vehicle.class);
    }
}
