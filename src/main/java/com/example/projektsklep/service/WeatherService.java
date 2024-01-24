package com.example.projektsklep.service;

import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class WeatherService {


    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;
    private UserRepository userRepository; // Załóżmy, że masz repozytorium użytkowników

    public WeatherService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getWeatherForCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiUrl + "?q=" + city + "&appid=" + apiKey + "&units=metric";
            String response = restTemplate.getForObject(url, String.class);
            return formatWeatherData(response);
        } catch (HttpClientErrorException e) {
            return "Weather information not available for the city: " + city;
        }
    }

    private String formatWeatherData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        String city = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        return String.format("City: %s\nTemperature: %.1f°C\nHumidity: %d%%\nDescription: %s",
                city, temp, humidity, description);
    }

    public String getDefaultWeather(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if (userOptional.isPresent() && userOptional.get().getAddress() != null) {
            String city = userOptional.get().getAddress().getCity();
            return getWeatherForCity(city);
        } else {
            return "Nie można znaleźć danych o pogodzie dla Twojej lokalizacji.";
        }
    }
}