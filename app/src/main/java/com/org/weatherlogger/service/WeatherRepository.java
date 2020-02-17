package com.org.weatherlogger.service;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.org.weatherlogger.db.AppDatabase;
import com.org.weatherlogger.db.WeatherDao;
import com.org.weatherlogger.entity.WeatherDetails;
import com.org.weatherlogger.model.WeatherLogger;
import com.org.weatherlogger.utils.AppExecutors;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static WeatherRepository weatherRepository;

    public static WeatherRepository getInstance(){
        if (weatherRepository == null){
            weatherRepository = new WeatherRepository();
        }
        return weatherRepository;
    }

    private ApiService weatherApi;
    MutableLiveData<WeatherLogger> weatherData = new MutableLiveData<>();
    WeatherDao weatherDao;

    public WeatherRepository(Application application) {
        AppDatabase db;
        weatherApi = RetrofitService.createService(ApiService.class);
        db = AppDatabase.getInstance(application);
        weatherDao = db.weatherDao();
    }

    public WeatherRepository(){
        weatherApi = RetrofitService.createService(ApiService.class);
    }

    public MutableLiveData<WeatherLogger> getWeatherLiveData(String lat, String lon, String key){
        weatherApi.getWeatherData(lat,lon, key).enqueue(new Callback<WeatherLogger>() {
            @Override
            public void onResponse(Call<WeatherLogger> call, final Response<WeatherLogger> response) {

                Log.d("URL>>>>",response.raw().request().url()+"");
                if (response.isSuccessful()){

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            WeatherDetails weatherDetails = new WeatherDetails();
                            weatherDetails.setTemp(response.body().getMain().getTemp());
                            weatherDetails.setMaxtemp(response.body().getMain().getTempMax());
                            weatherDetails.setMintemp(response.body().getMain().getTempMin());
                            weatherDetails.setApiRequestTime(new Date());
                            weatherDao.insertWeather(weatherDetails);

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<WeatherLogger> call, Throwable t) {
                weatherData.setValue(null);
                Log.d("Error>>>",t.getMessage());
            }


        });
        return weatherData;
    }


}
