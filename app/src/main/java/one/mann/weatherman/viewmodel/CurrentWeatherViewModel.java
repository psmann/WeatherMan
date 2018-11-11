package one.mann.weatherman.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import one.mann.weatherman.api.WeatherResult;
import one.mann.weatherman.data.WeatherData;
import one.mann.weatherman.model.GpsLocation;

public class CurrentWeatherViewModel extends AndroidViewModel implements GpsLocation.GeoCoordinates,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MutableLiveData<String> currentTemperature, maxTemperature, minTemperature, pressure,
            humidity, location, lastChecked, lastUpdated, cityName, sunrise, sunset, clouds, windSpeed,
            windDirection, visibility;
    private MutableLiveData<Boolean> displayProgressBar;
    private WeatherResult weatherResult;
    private WeatherData weatherData;
    private GpsLocation gpsLocation;

    public CurrentWeatherViewModel(@NonNull Application application) {
        super(application);
        weatherData = new WeatherData(application);
        weatherResult = new WeatherResult(application);
        gpsLocation = new GpsLocation(application, this);
        currentTemperature = new MutableLiveData<>();
        maxTemperature = new MutableLiveData<>();
        minTemperature = new MutableLiveData<>();
        pressure = new MutableLiveData<>();
        humidity = new MutableLiveData<>();
        location = new MutableLiveData<>();
        lastChecked = new MutableLiveData<>();
        lastUpdated = new MutableLiveData<>();
        cityName = new MutableLiveData<>();
        sunrise = new MutableLiveData<>();
        sunset = new MutableLiveData<>();
        clouds = new MutableLiveData<>();
        windSpeed = new MutableLiveData<>();
        windDirection = new MutableLiveData<>();
        visibility = new MutableLiveData<>();
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
        sunrise.setValue(weatherData.getWeatherData(WeatherData.SUNRISE));
        sunset.setValue(weatherData.getWeatherData(WeatherData.SUNSET));
        clouds.setValue(weatherData.getWeatherData(WeatherData.CLOUDS));
        windSpeed.setValue(weatherData.getWeatherData(WeatherData.WIND_SPEED));
        windDirection.setValue(weatherData.getWeatherData(WeatherData.WIND_DIRECTION));
        visibility.setValue(weatherData.getWeatherData(WeatherData.VISIBILITY));
    }

    public void getWeather() {
        weatherData.saveProgressBar(true);
        gpsLocation.getLocation();
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

    public MutableLiveData<String> getSunrise() {
        return sunrise;
    }

    public MutableLiveData<String> getSunset() {
        return sunset;
    }

    public MutableLiveData<String> getClouds() {
        return clouds;
    }

    public MutableLiveData<String> getWindSpeed() {
        return windSpeed;
    }

    public MutableLiveData<String> getWindDirection() {
        return windDirection;
    }

    public MutableLiveData<String> getVisibility() {
        return visibility;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateWeatherUi();
    }

    @Override
    public void getCoordinates(Double[] location) {
        weatherResult.weatherCall(location);
    }
}