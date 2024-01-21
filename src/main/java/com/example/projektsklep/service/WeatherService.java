package com.example.projektsklep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final String apiUrl = "http://api.openweathermap.org/data/2.5/weather";

    public String getWeatherForCity(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?q=" + city + "&appid=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}