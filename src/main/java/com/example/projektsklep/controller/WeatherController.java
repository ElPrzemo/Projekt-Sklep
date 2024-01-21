package com.example.projektsklep.controller;


import com.example.projektsklep.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {


    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false, defaultValue = "Warsaw") String city, Model model) {
        String weatherData = weatherService.getWeatherForCity(city);
        model.addAttribute("weatherData", weatherData);
        return "weather";
    }
}