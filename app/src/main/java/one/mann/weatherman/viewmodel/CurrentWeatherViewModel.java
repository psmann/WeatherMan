package one.mann.weatherman.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import one.mann.weatherman.api.WeatherResult;
import one.mann.weatherman.data.WeatherData;

public class CurrentWeatherViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MutableLiveData<String> currentTemperature, maxTemperature, minTemperature, pressure,
            humidity, location, lastChecked, lastUpdated, cityName;
    private MutableLiveData<Boolean> displayProgressBar;
    private final Double[] geoCoordinates = new Double[2];
    private LocationCallback locationCallback;
    private FusedLocationProviderClient locationProviderClient = LocationServices
            .getFusedLocationProviderClient(getApplication());
    private WeatherResult weatherResult;
    private WeatherData weatherData;

    public CurrentWeatherViewModel(@NonNull Application application) {
        super(application);
        weatherData = new WeatherData(application);
        weatherResult = new WeatherResult(application);
        currentTemperature = new MutableLiveData<>();
        maxTemperature = new MutableLiveData<>();
        minTemperature = new MutableLiveData<>();
        pressure = new MutableLiveData<>();
        humidity = new MutableLiveData<>();
        location = new MutableLiveData<>();
        lastChecked = new MutableLiveData<>();
        lastUpdated = new MutableLiveData<>();
        cityName = new MutableLiveData<>();
        displayProgressBar = new MutableLiveData<>();
        weatherData.getPreferences().registerOnSharedPreferenceChangeListener(this);
        updateWeatherUi();
    }

    private void updateWeatherUi() {
        currentTemperature.setValue(weatherData.getWeatherData(WeatherData.CURRENT_TEMP));
        maxTemperature.setValue(weatherData.getWeatherData(WeatherData.MAX_TEMP));
        minTemperature.setValue(weatherData.getWeatherData(WeatherData.MIN_TEMP));
        pressure.setValue(weatherData.getWeatherData(WeatherData.PRESSURE));
        humidity.setValue(weatherData.getWeatherData(WeatherData.HUMIDITY));
        location.setValue(weatherData.getWeatherData(WeatherData.LOCATION));
        displayProgressBar.setValue(weatherData.getProgressBar());
        lastChecked.setValue(weatherData.getWeatherData(WeatherData.LAST_CHECKED));
        lastUpdated.setValue(weatherData.getWeatherData(WeatherData.LAST_UPDATED));
        cityName.setValue(weatherData.getWeatherData(WeatherData.CITY_NAME));
    }

    @SuppressLint("MissingPermission") // locationProviderClient is being checked for permissions before this method is called
    public void geoLocation() {
        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000);

        weatherData.saveProgressBar(true);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null)
                    for (Location location : locationResult.getLocations()) {
                        geoCoordinates[0] = location.getLatitude();
                        geoCoordinates[1] = location.getLongitude();
                        weatherResult.getWeatherInfo(geoCoordinates);
                        locationProviderClient.removeLocationUpdates(this);
                    }
            }
        };
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                geoCoordinates[0] = location.getLatitude();
                geoCoordinates[1] = location.getLongitude();
                weatherResult.getWeatherInfo(geoCoordinates);
            } else
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        });
    }

    public MutableLiveData<String> getCurrentTemperature() {
        return currentTemperature;
    }

    public MutableLiveData<String> getMaxTemperature() {
        return maxTemperature;
    }

    public MutableLiveData<String> getMinTemperature() {
        return minTemperature;
    }

    public MutableLiveData<String> getPressure() {
        return pressure;
    }

    public MutableLiveData<String> getHumidity() {
        return humidity;
    }

    public MutableLiveData<String> getLocation() {
        return location;
    }

    public MutableLiveData<Boolean> getDisplayProgressBar() {
        return displayProgressBar;
    }

    public MutableLiveData<String> getLastChecked() {
        return lastChecked;
    }

    public MutableLiveData<String> getLastUpdated() {
        return lastUpdated;
    }

    public MutableLiveData<String> getCityName() {
        return cityName;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateWeatherUi();
    }
}