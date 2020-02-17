package com.org.weatherlogger.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class WeatherDetails {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Double temp;
    private Double mintemp;

    public Double getMaxtemp() {
        return maxtemp;
    }

    public void setMaxtemp(Double maxtemp) {
        this.maxtemp = maxtemp;
    }

    private Double maxtemp;

    @ColumnInfo(name = "api_request_Time")
    private Date apiRequestTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Date getApiRequestTime() {
        return apiRequestTime;
    }

    public void setApiRequestTime(Date apiRequestTime) {
        this.apiRequestTime = apiRequestTime;
    }

    public Double getMintemp() {
        return mintemp;
    }

    public void setMintemp(Double mintemp) {
        this.mintemp = mintemp;
    }
}
