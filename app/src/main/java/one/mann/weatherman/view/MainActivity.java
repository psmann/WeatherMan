package one.mann.weatherman.view;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import one.mann.weatherman.GlideApp;
import one.mann.weatherman.R;
import one.mann.weatherman.viewmodel.CurrentWeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private final int LOCATION_REQUEST_CODE = 1011;
    private TextView currentTemp, maxTemp, minTemp, humidity, pressure, geoLocation, lastUpdated,
            cityName, lastChecked, sunrise, sunset, clouds, windSpeed, windDirection, visibility, description;
    private ImageView weatherIcon;
    private CurrentWeatherViewModel weatherViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;
    private boolean uiVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.weather_layout);
        constraintLayout.setVisibility(View.INVISIBLE);

        // gives permissions for both NETWORK_PROVIDER (COARSE_LOCATION) and GPS_PROVIDER
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) // check result in onRequestPermissionsResult()
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
        } else
            initObjects();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initObjects();
            } else {
                finish();
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE)
            switch (resultCode) {
                case MainActivity.RESULT_OK:
                    weatherViewModel.getWeather(true);
                    break;
                case MainActivity.RESULT_CANCELED:
                    weatherViewModel.getWeather(false);
                    break;
            }
    }

    private void initObjects() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        currentTemp = findViewById(R.id.current_temp_result);
        maxTemp = findViewById(R.id.max_temp_result);
        minTemp = findViewById(R.id.min_temp_result);
        humidity = findViewById(R.id.humidity_result);
        pressure = findViewById(R.id.pressure_result);
        geoLocation = findViewById(R.id.location_result);
        lastChecked = findViewById(R.id.last_checked_result);
        lastUpdated = findViewById(R.id.last_updated_result);
        cityName = findViewById(R.id.city_name);
        sunrise = findViewById(R.id.sunrise_result);
        sunset = findViewById(R.id.sunset_result);
        clouds = findViewById(R.id.clouds_result);
        windSpeed = findViewById(R.id.wind_speed_result);
        windDirection = findViewById(R.id.wind_direction_result);
        visibility = findViewById(R.id.visibility_result);
        description = findViewById(R.id.description);
        weatherIcon = findViewById((R.id.weather_icon));
        uiVisible = false;

        weatherViewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel.class);
        weatherViewModel.getCurrentTemperature().observe(this, s -> currentTemp.setText(s));
        weatherViewModel.getMaxTemperature().observe(this, s -> maxTemp.setText(s));
        weatherViewModel.getMinTemperature().observe(this, s -> minTemp.setText(s));
        weatherViewModel.getPressure().observe(this, s -> pressure.setText(s));
        weatherViewModel.getHumidity().observe(this, s -> humidity.setText(s));
        weatherViewModel.getLocation().observe(this, s -> geoLocation.setText(s));
        weatherViewModel.getDisplayProgressBar().observe(this, result ->
                swipeRefreshLayout.setRefreshing(result == null ? false : result));
        weatherViewModel.getLastChecked().observe(this, s -> lastChecked.setText(s));
        weatherViewModel.getLastUpdated().observe(this, s -> lastUpdated.setText(s));
        weatherViewModel.getSunrise().observe(this, s -> sunrise.setText(s));
        weatherViewModel.getSunset().observe(this, s -> sunset.setText(s));
        weatherViewModel.getClouds().observe(this, s -> clouds.setText(s));
        weatherViewModel.getWindSpeed().observe(this, s -> windSpeed.setText(s));
        weatherViewModel.getWindDirection().observe(this, s -> windDirection.setText(s));
        weatherViewModel.getVisibility().observe(this, s -> visibility.setText(s));
        weatherViewModel.getDescription().observe(this, s -> description.setText(s));
        weatherViewModel.getIconCode().observe(this, s -> GlideApp.with(this)
                .load(s)
                .skipMemoryCache(true)
                .into(weatherIcon));
        weatherViewModel.getCityName().observe(this, s -> {
            cityName.setText(s);
            if (!uiVisible)
                if (cityName.getText().toString().equals("")) {
                    checkLocationSettings();
                } else {
                    constraintLayout.setVisibility(View.VISIBLE);
                    uiVisible = true;
                }
        });
        swipeRefreshLayout.setOnRefreshListener(this::checkLocationSettings);
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        @SuppressWarnings("ConstantConditions") // Suppressed because null is being checked
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void checkLocationSettings() {
        if (!checkNetworkConnection()) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try { // Location settings are on
                task.getResult(ApiException.class);
                weatherViewModel.getWeather(true);
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try { // Location settings are off. Show a prompt and check result in onActivityResult()
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(MainActivity.this, LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException | ClassCastException ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break; // Location settings are not available on the device
                }
            }
        });
    }
}