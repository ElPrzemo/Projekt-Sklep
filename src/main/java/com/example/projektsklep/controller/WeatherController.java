package com.example.projektsklep.controller;


import com.example.projektsklep.exception.DataAccessException;
import com.example.projektsklep.exception.InvalidCityException;
import com.example.projektsklep.exception.WeatherServiceException;
import com.example.projektsklep.exception.WeatherServiceUnavailableException;
import com.example.projektsklep.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class WeatherController {


    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model, Principal principal) {
        Optional<String> weatherData = weatherService.getWeatherData(city, principal);
        if (weatherData.isPresent()) {
            model.addAttribute("weatherData", weatherData.get());
            return "weather";
        } else {
            model.addAttribute("error", "Nie można znaleźć danych o pogodzie.");
            return "weather_error";
        }
    }
}