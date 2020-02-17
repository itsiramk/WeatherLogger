package com.org.weatherlogger.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.org.weatherlogger.entity.WeatherDetails;

@Dao
public interface WeatherDao {


    @Query("SELECT * FROM WeatherDetails ORDER BY api_request_Time DESC Limit 1")
    LiveData<WeatherDetails> getWeatherList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherDetails weatherTemp);


}
