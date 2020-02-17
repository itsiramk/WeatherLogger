package com.org.weatherlogger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.org.weatherlogger.db.AppDatabase;
import com.org.weatherlogger.db.WeatherDao;
import com.org.weatherlogger.entity.WeatherDetails;
import com.org.weatherlogger.model.WeatherLogger;
import com.org.weatherlogger.service.WeatherRepository;

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<WeatherDetails> mTasks;
    private WeatherDao mTaskDao;
    private WeatherRepository repository;
    private MutableLiveData<WeatherLogger> mutableLiveData;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(String lat,String lon,String key){
        if (mutableLiveData != null){
            return;
        }
        mTaskDao = AppDatabase.getInstance(getApplication()).weatherDao();
        repository = new WeatherRepository(getApplication());
        repository.getWeatherLiveData(lat,lon, key);
        mTasks = mTaskDao.getWeatherList();
    }

    public LiveData<WeatherDetails> getWeatherDetails() {
        return mTasks;
    }

}

