package com.example.projektsklep.controller;


import com.example.projektsklep.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class WeatherController {


    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model, Principal principal) {
        String weatherData;
        if (city != null && !city.isEmpty()) {
            weatherData = weatherService.getWeatherForCity(city);
        } else if (principal != null) {
            weatherData = weatherService.getDefaultWeather(principal.getName());
        } else {
            weatherData = "Nie jesteś zalogowany.";
        }
        model.addAttribute("weatherData", weatherData);
        return "weather";
    }
}