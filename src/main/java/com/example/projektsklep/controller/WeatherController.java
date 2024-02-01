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

@Controller
public class WeatherController {


    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model, Principal principal) {
        String weatherData = null;
        try {
            if (city != null && !city.isEmpty()) {
                weatherData = weatherService.getWeatherForCity(city);
            } else if (principal != null) {
                weatherData = weatherService.getDefaultWeather(principal.getName());
            } else {
                weatherData = "Nie jesteś zalogowany.";
            }
        } catch (WeatherServiceException e) {
            model.addAttribute("error", "Wystąpił błąd podczas pobierania danych pogodowych");
            return "weather_error";
        } catch (InvalidCityException e) {
            model.addAttribute("error", "Podane miasto jest nieprawidłowe");
            return "invalid_city_error";
        } catch (WeatherServiceUnavailableException e) {
            model.addAttribute("error", "Serwis pogodowy jest niedostępny. Spróbuj ponownie później.");
            return "weather_error";
        } catch (DataAccessException e) {
            // ... obsługa błędów dostępu do danych
        }
        model.addAttribute("weatherData", weatherData);
        return "weather";
    }
}