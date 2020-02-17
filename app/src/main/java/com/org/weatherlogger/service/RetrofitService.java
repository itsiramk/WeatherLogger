package com.org.weatherlogger.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    public static String  WEATHER_URL="https://api.openweathermap.org";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
