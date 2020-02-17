package com.org.weatherlogger.service;


import com.org.weatherlogger.model.WeatherLogger;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID={APIKEY}";
    String BASE_URL2 = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}";
    String API_Key = "137af7eb2d60fc3345dbc73f7cad5715";

    @GET("/data/2.5/weather")
    Call<WeatherLogger> getWeatherData(@Query("lat") String latitude,
                                       @Query("lon") String longitude,
                                       @Query("appid") String appKey);
}
