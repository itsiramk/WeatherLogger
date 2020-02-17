package com.org.weatherlogger.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.org.weatherlogger.R;
import com.org.weatherlogger.adapters.WeatherAdapter;
import com.org.weatherlogger.entity.WeatherDetails;
import com.org.weatherlogger.service.ApiService;
import com.org.weatherlogger.utils.NetworkUtils;
import com.org.weatherlogger.utils.PermissionUtils;
import com.org.weatherlogger.viewmodel.WeatherViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    WeatherAdapter weatherAdapter;
    @BindView(R.id.rvWeather)
    RecyclerView rvWeather;
    WeatherViewModel testViewModel;
    String lat, lon;
    @BindView(R.id.toolbar)
    Toolbar mActionBarToolbar;
    @BindView(R.id.tvIntro)
    TextView tvIntro;
    private LocationRequest locationRequest;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private long UPDATE_INTERVAL = 600 * 1000;  /* 6 minutes */
    private long FASTEST_INTERVAL = 600000; /* 6 min */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    public void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            mActionBarToolbar.setTitleTextAppearance(this,R.style.ToolBarStyleTablet);
        }
        mActionBarToolbar.setTitleTextColor(getResources().getColor(R.color.colorBg));
    }
    public void fetchDataFromServer(){
        testViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        testViewModel.init(lat, lon, ApiService.API_Key);
        testViewModel.getWeatherDetails().observe(this, new Observer<WeatherDetails>() {
            @Override
            public void onChanged(WeatherDetails weatherDetails) {
                if(weatherDetails!=null)
                {
                    tvIntro.setVisibility(View.GONE);
                    Log.d("Date>>>",weatherDetails.getApiRequestTime()+"");
                    setupRecyclerView(weatherDetails);
                }
            }
        });
    }

    protected void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
    }

    private void setupRecyclerView(WeatherDetails weatherResponse) {

            weatherAdapter = new WeatherAdapter(MainActivity.this, weatherResponse);
            rvWeather.setVisibility(View.VISIBLE);
            rvWeather.setLayoutManager(new LinearLayoutManager(this));
            rvWeather.setAdapter(weatherAdapter);
            weatherAdapter.notifyDataSetChanged();
            rvWeather.setItemAnimator(new DefaultItemAnimator());
            rvWeather.setNestedScrollingEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(NetworkUtils.isNetworkAvailable(getApplicationContext()))
        {
            if (id == R.id.action_save) {
                fetchDataFromServer();
                return true;
            }
        }else
        {
            Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPerms();
    }

    private void checkPerms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    displayNeverAskAgainDialog();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        } else
            startLocationUpdates();
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.perm_request));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.perm_manual), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void fetchLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        lat = String.valueOf(location.getLatitude());
                        lon = String.valueOf(location.getLongitude());
                    }
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (LOCATION_PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG).show();
                startLocationUpdates();
            } else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }
}
