package com.org.weatherlogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.org.weatherlogger.R;
import com.org.weatherlogger.entity.WeatherDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    Context context;
    WeatherDetails weatherDetails;
    ArrayList<BarEntry> BarEntry;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;

    public WeatherAdapter(Context context, WeatherDetails weatherDetails) {
        this.context = context;
        this.weatherDetails = weatherDetails;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeatherViewHolder holder, int position) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        holder.tvTemperatureValue.setText(weatherDetails.getTemp().toString() + " °F");
        holder.tvDateValue.setText(df.format(weatherDetails.getApiRequestTime()));
        holder.tvMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvMoreDetails.setVisibility(View.GONE);
                BarEntry = new ArrayList<>();
                BarEntryLabels = new ArrayList<String>();
                addValuesToBarEntry(weatherDetails);
                addValuesToBarEntryLabels();
                Bardataset = new BarDataSet(BarEntry, "Temperature");
                BARDATA = new BarData(BarEntryLabels, Bardataset);
                Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                holder.chart.setData(BARDATA);
                holder.chart.animateY(1000);
            }
        });
    }

    private void addValuesToBarEntry(WeatherDetails weatherDetails) {
        BarEntry.add(new BarEntry(Float.parseFloat(String.valueOf(weatherDetails.getMintemp())), 0));
        BarEntry.add(new BarEntry(Float.parseFloat(String.valueOf(weatherDetails.getTemp())), 1));
        BarEntry.add(new BarEntry(Float.parseFloat(String.valueOf(weatherDetails.getMaxtemp())), 2));
    }

    private void addValuesToBarEntryLabels() {
        BarEntryLabels.add("50");
        BarEntryLabels.add("100");
        BarEntryLabels.add("150");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView tvTemperatureValue;
        TextView tvDateValue;
        TextView tvMoreDetails;
        BarChart chart;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTemperatureValue = itemView.findViewById(R.id.tvTemperatureValue);
            tvDateValue = itemView.findViewById(R.id.tvDateValue);
            tvMoreDetails = itemView.findViewById(R.id.tvMoreDetails);
            chart = itemView.findViewById(R.id.chart);
        }
    }
}
