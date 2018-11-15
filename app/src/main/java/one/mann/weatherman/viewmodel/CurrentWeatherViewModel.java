package one.mann.weatherman.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.Objects;

import one.mann.weatherman.R;
import one.mann.weatherman.api.WeatherResult;
import one.mann.weatherman.data.WeatherData;
import one.mann.weatherman.model.GpsLocation;

public class CurrentWeatherViewModel extends AndroidViewModel implements GpsLocation.GeoCoordinates,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private MutableLiveData<WeatherData> weatherLiveData;
    private MutableLiveData<Boolean> displayLoadingBar, displayUi;
    private WeatherResult weatherResult;
    private WeatherData weatherData;
    private GpsLocation gpsLocation;

    public CurrentWeatherViewModel(@NonNull Application application) {
        super(application);
        weatherData = new WeatherData(application);
        weatherResult = new WeatherResult(application);
        gpsLocation = new GpsLocation(application, this);
        weatherLiveData = new MutableLiveData<>();
        displayLoadingBar = new MutableLiveData<>();
        displayUi = new MutableLiveData<>();
        weatherLiveData.setValue(weatherData);
        displayLoadingBar.setValue(weatherData.getLoadingBar());
        displayUi.setValue(weatherData.getUiVisibility());
        weatherData.getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void getWeather(boolean gpsEnabled) {
        weatherData.saveLoadingBar(true);
        if (gpsEnabled)
            gpsLocation.getLocation();
        else if (Objects.equals(weatherData.getWeatherData(WeatherData.LOCATION), "")) {
            weatherData.saveLoadingBar(false);
            Toast.makeText(getApplication(), R.string.gps_needed_for_location, Toast.LENGTH_SHORT).show();
        } else {
            Double[] lastLocation = new Double[]{Double.parseDouble(weatherData.getWeatherData(WeatherData.LATITUDE)),
                    Double.parseDouble(weatherData.getWeatherData(WeatherData.LONGITUDE))};
            weatherResult.weatherCall(lastLocation);
            Toast.makeText(getApplication(), R.string.no_gps_updating_previous_location, Toast.LENGTH_SHORT).show();
        }
    }

    public MutableLiveData<WeatherData> getWeatherLiveData() {
        return weatherLiveData;
    }

    public MutableLiveData<Boolean> getDisplayLoadingBar() {
        return displayLoadingBar;
    }

    public MutableLiveData<Boolean> getDisplayUi() {
        return displayUi;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) { // weatherLiveData can be split up into different objects and handled separately for efficiency
            case WeatherData.UI_VISIBILITY:
                displayUi.setValue(weatherData.getUiVisibility());
                break;
            case WeatherData.LOADING_BAR:
                displayLoadingBar.setValue(weatherData.getLoadingBar());
                break;
            default:
                weatherLiveData.setValue(weatherData);
        }
    }

    @Override
    public void getCoordinates(Double[] location) {
        weatherResult.weatherCall(location);
    }
}